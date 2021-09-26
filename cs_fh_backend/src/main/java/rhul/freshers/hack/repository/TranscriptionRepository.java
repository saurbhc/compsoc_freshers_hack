package rhul.freshers.hack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rhul.freshers.hack.entity.Transcription;

@Repository
@Transactional(readOnly = true)
@RepositoryRestResource(collectionResourceRel = "transcriptions", path = "transcriptions")
public interface TranscriptionRepository extends JpaRepository<Transcription, UUID> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "delete from transcription t where transcription_id in " 
	+ "(select transcription_id from video_word vw "
	+ "join video v on v.video_id = vw.video_id where v.link = :videoLink)", nativeQuery = true)
	public void deleteTranscriptOfVideo(final String videoLink);

}
