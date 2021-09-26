package rhul.freshers.hack.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "video_word")
public class VideoWord {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "video_word_id", unique = true, nullable = false)
	private UUID videoWordId;
	
	@Column(name = "video_id", updatable = false, insertable = false)
	private UUID videoId;
	
	@Column(name = "word")
	private String word;
	
	private long seconds;
	
	@Column(name = "transcription_id")
	private UUID transcriptionId;

	public UUID getTranscriptionId() {
		return transcriptionId;
	}

	public void setTranscriptionId(UUID transcriptionId) {
		this.transcriptionId = transcriptionId;
	}

	public UUID getVideoWordId() {
		return videoWordId;
	}

	public void setVideoWordId(UUID videoWordId) {
		this.videoWordId = videoWordId;
	}

	public UUID getVideoId() {
		return videoId;
	}

	public void setVideoId(UUID videoId) {
		this.videoId = videoId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getSeconds() {
		return seconds;
	}

	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
	
	

}
