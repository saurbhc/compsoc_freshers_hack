package rhul.freshers.hack.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transcription")
public class Transcription {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "transcription_id", unique = true, nullable = false)
	private UUID transcriptionId;
	
	@Column(name = "transcription_text")
	private String transcriptionText;

	public UUID getTranscriptionId() {
		return transcriptionId;
	}

	public void setTranscriptionId(UUID transcriptionId) {
		this.transcriptionId = transcriptionId;
	}

	public String getTranscriptionText() {
		return transcriptionText;
	}

	public void setTranscriptionText(String transcriptionText) {
		this.transcriptionText = transcriptionText;
	}

}
