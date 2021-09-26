package rhul.freshers.hack.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "video")
public class Video {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "video_id", unique = true, nullable = false)
	private UUID videoId;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "audio_link")
	private String audioLink;
	
	@Column(name = "created_at")
	private String createdAt;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "video_id", nullable = false)
	private List<VideoWord> videoWords;

	public List<VideoWord> getVideoWords() {
		return videoWords;
	}

	public void setVideoWords(List<VideoWord> videoWords) {
		this.videoWords = videoWords;
	}

	public UUID getVideoId() {
		return videoId;
	}

	public void setVideoId(UUID videoId) {
		this.videoId = videoId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getAudioLink() {
		return audioLink;
	}

	public void setAudioLink(String audioLink) {
		this.audioLink = audioLink;
	}

	


}
