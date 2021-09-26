package rhul.freshers.hack.entity;

public class VideoLinkDTO {
	
	private String youtubeVideoName;
	
	private String youtubeVideoLink;

	public String getYoutubeVideoName() {
		return youtubeVideoName;
	}

	public void setYoutubeVideoName(String youtubeVideoName) {
		this.youtubeVideoName = youtubeVideoName;
	}

	public String getYoutubeVideoLink() {
		return youtubeVideoLink;
	}

	public void setYoutubeVideoLink(String youtubeVideoLink) {
		this.youtubeVideoLink = youtubeVideoLink;
	}

	public VideoLinkDTO(String youtubeVideoName, String youtubeVideoLink) {
		super();
		this.youtubeVideoName = youtubeVideoName;
		this.youtubeVideoLink = youtubeVideoLink;
	}

}
