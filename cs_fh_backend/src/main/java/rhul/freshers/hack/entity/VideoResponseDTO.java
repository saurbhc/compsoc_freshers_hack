package rhul.freshers.hack.entity;

import java.util.List;

public class VideoResponseDTO {
	
	private String videoLink;
	
	private String videoName;
	
	private List<VideoSecond> videoSecondsLinks;

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public List<VideoSecond> getVideoSecondsLinks() {
		return videoSecondsLinks;
	}

	public void setVideoSecondsLinks(List<VideoSecond> videoSecondsLinks) {
		this.videoSecondsLinks = videoSecondsLinks;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

}
