package rhul.freshers.hack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rhul.freshers.hack.entity.Transcription;

@Repository
@Transactional(readOnly = true)
@RepositoryRestResource(collectionResourceRel = "transcriptions", path = "transcriptions")
public interface TranscriptionRepository extends JpaRepository<Transcription, UUID> {

}
