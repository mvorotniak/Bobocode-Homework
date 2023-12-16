package nasapicturesstealer.infrastructure.repository;

import java.util.List;

import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

public interface NasaPictureEntityRepository extends JpaRepository<NasaPictureEntity, Long> {

  @Query("SELECT new org.springframework.data.util.Pair(picture.nasaId, picture.id) " 
    + "FROM NasaPictureEntity picture WHERE picture.nasaId IN (:ids)")
  List<Pair<Integer, Long>> findAllByNasaIds(@Param("ids") List<Integer> nasaIds);

}
