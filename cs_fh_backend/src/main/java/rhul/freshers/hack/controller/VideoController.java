package rhul.freshers.hack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rhul.freshers.hack.entity.VideoDTO;
import rhul.freshers.hack.entity.VideoLinkDTO;
import rhul.freshers.hack.entity.VideoResponseDTO;
import rhul.freshers.hack.service.VideoService;

@RestController
public class VideoController {

	@Autowired
	private VideoService videoService;

	@PostMapping("/video")
	public ResponseEntity<String> saveLinkId(@RequestBody final VideoDTO videoDTO) {
		try {
			videoService.saveLink(videoDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Processing failed " + e.getLocalizedMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Link Added", HttpStatus.OK);
	}

	@PostMapping("/video/search")
	public ResponseEntity<List<VideoResponseDTO>> searchLinks(@RequestBody final VideoDTO videoDTO) {
		return new ResponseEntity<>(videoService.searchLink(videoDTO), HttpStatus.OK);

	}

	@GetMapping("/video/list")
	public ResponseEntity<List<VideoLinkDTO>> getAllVideos() {
		return new ResponseEntity<>(videoService.getAllVideos(), HttpStatus.OK);

	}

	@PostMapping("/video/delete")
	public ResponseEntity<String> delete(@RequestBody final VideoDTO videoDTO) {
		videoService.deleteLink(videoDTO);
		return new ResponseEntity<>("Video Deleted", HttpStatus.OK);

	}
}
