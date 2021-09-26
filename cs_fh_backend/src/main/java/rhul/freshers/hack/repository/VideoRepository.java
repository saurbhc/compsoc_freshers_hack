package rhul.freshers.hack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rhul.freshers.hack.entity.Video;

@Repository
@Transactional(readOnly = true)
@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository extends JpaRepository<Video, UUID> {

	
	@Query("select distinct a from Video a join fetch a.videoWords pa where pa.word = (:searchString) order by pa.seconds ASC")
	public List<Video> getVideoBySearchString(String searchString);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	public void deleteByLink(String link);
}
