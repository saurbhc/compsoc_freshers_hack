package rhul.freshers.hack.entity;

public class VideoSecond {
	
	private String transcription;
	
	private String link;

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public String getLink() {
		return link;
	}

	public VideoSecond(String transcription, String link) {
		super();
		this.transcription = transcription;
		this.link = link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
