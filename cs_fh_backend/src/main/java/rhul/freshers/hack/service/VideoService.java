package rhul.freshers.hack.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.WordInfo;

import rhul.freshers.hack.entity.Transcription;
import rhul.freshers.hack.entity.Video;
import rhul.freshers.hack.entity.VideoDTO;
import rhul.freshers.hack.entity.VideoLinkDTO;
import rhul.freshers.hack.entity.VideoResponseDTO;
import rhul.freshers.hack.entity.VideoSecond;
import rhul.freshers.hack.entity.VideoWord;
import rhul.freshers.hack.repository.TranscriptionRepository;
import rhul.freshers.hack.repository.VideoRepository;

@Service
public class VideoService {

	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private TranscriptionRepository transcriptionRepository;

	@Value("${googleCredentialsFilePath}")
	private String googleCredentialsFile;

	private SpeechSettings settings;

	@PostConstruct
	public void init() {

		CredentialsProvider credentialsProvider;
		try {
			credentialsProvider = FixedCredentialsProvider
					.create(ServiceAccountCredentials.fromStream(new FileInputStream(googleCredentialsFile)));
			settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveLink(VideoDTO videoDTO) throws Exception {

		Video video = asyncRecognizeWords(videoDTO);
		videoRepository.save(video);

	}

	public List<VideoLinkDTO> getAllVideos() {

		List<Video> videos = videoRepository.findAll();

		List<VideoLinkDTO> videoLinkDTOs = new ArrayList<>(videos.size());

		try {
			for (Video video : videos) {
				String audioLink = video.getAudioLink();
				String videoName = getVideoName(audioLink);

				videoLinkDTOs.add(new VideoLinkDTO(videoName, video.getLink()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return videoLinkDTOs;

	}

	private String getVideoName(String audioLink) {
		return audioLink.substring(audioLink.lastIndexOf("/") + 1);
	}

	public List<VideoResponseDTO> searchLink(VideoDTO videoDTO) {

		List<VideoResponseDTO> videoResponseList = new ArrayList<>();

		try {
			List<Video> videoBySearchString = videoRepository.getVideoBySearchString(videoDTO.getSearchString());

			for (Video video : videoBySearchString) {
				VideoResponseDTO videoResponseDTO = new VideoResponseDTO();
				String link = video.getLink();
				videoResponseDTO.setVideoLink(link);
				videoResponseDTO.setVideoName(getVideoName(video.getAudioLink()));

				List<VideoSecond> videoSecondsLinks = new ArrayList<>();

				List<UUID> transcriptionId = new ArrayList<>();

				Set<UUID> transcriptionIds = video.getVideoWords().stream().map(VideoWord::getTranscriptionId)
						.collect(Collectors.toSet());

				Map<UUID, String> transcriptions = transcriptionRepository.findAllById(transcriptionIds).stream()
						.collect(Collectors.toMap(Transcription::getTranscriptionId,
								Transcription::getTranscriptionText));

				for (VideoWord videoWord : video.getVideoWords()) {
					UUID transcriptionId2 = videoWord.getTranscriptionId();
					if (!transcriptionId.contains(transcriptionId2)) {
						transcriptionId.add(transcriptionId2);
						videoSecondsLinks.add(new VideoSecond(transcriptions.get(transcriptionId2),
								link + "?t=" + videoWord.getSeconds()));
					}
				}

				videoResponseDTO.setVideoSecondsLinks(videoSecondsLinks);

				videoResponseList.add(videoResponseDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return videoResponseList;

	}

	public Video asyncRecognizeWords(VideoDTO videoDTO) throws Exception {

		Video video = new Video();
		video.setCreatedAt("Rinki");
		video.setAudioLink(videoDTO.getGcpAudioLink());
		video.setLink(videoDTO.getYoutubeVideoLink());

		List<VideoWord> videoWords = new ArrayList<>();

		// Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		try (SpeechClient speech = SpeechClient.create(settings)) {

			// Configure remote file request for FLAC
			RecognitionConfig config = RecognitionConfig.newBuilder().setLanguageCode("en-US").setSampleRateHertz(16000)
					.setEnableWordTimeOffsets(true).build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(videoDTO.getGcpAudioLink()).build();

			// Use non-blocking call for getting file transcription
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = speech
					.longRunningRecognizeAsync(config, audio);
			while (!response.isDone()) {
				System.out.println("Waiting for response...");
				Thread.sleep(10000);
			}

			List<SpeechRecognitionResult> results = response.get().getResultsList();

			for (SpeechRecognitionResult result : results) {

				// There can be several alternative transcripts for a given chunk of speech.
				// Just use the
				// first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				String transcript = alternative.getTranscript();
				System.out.printf("Transcription: %s\n", transcript);

				Transcription transcription = new Transcription();
				transcription.setTranscriptionText(transcript);

				Transcription transcript2 = transcriptionRepository.save(transcription);

				for (WordInfo wordInfo : alternative.getWordsList()) {

					VideoWord videoWord = new VideoWord();

					String word = wordInfo.getWord();
					System.out.println(word);

					long seconds = wordInfo.getStartTime().getSeconds();
					System.out.printf("\t%s.%s sec - %s.%s sec\n", seconds,
							wordInfo.getStartTime().getNanos() / 100000000, wordInfo.getEndTime().getSeconds(),
							wordInfo.getEndTime().getNanos() / 100000000);

					videoWord.setWord(word);
					videoWord.setSeconds(seconds);
					videoWord.setTranscriptionId(transcript2.getTranscriptionId());

					System.out.println(new ObjectMapper().writeValueAsString(videoWord));
					videoWords.add(videoWord);

					System.out.println(new ObjectMapper().writeValueAsString(videoWords));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		video.setVideoWords(videoWords);
		System.out.println(new ObjectMapper().writeValueAsString(videoWords));

		return video;

	}

}
