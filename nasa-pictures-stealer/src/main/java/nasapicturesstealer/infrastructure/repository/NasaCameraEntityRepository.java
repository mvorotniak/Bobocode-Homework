package nasapicturesstealer.infrastructure.repository;

import java.util.List;

import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

public interface NasaCameraEntityRepository extends JpaRepository<NasaCameraEntity, Long> {

  @Query("SELECT new org.springframework.data.util.Pair(camera.nasaId, camera.id)" 
    + " FROM NasaCameraEntity camera WHERE camera.nasaId IN (:ids)")
  List<Pair<Integer, Long>> findAllByNasaIds(@Param("ids") List<Integer> nasaIds);
  
}
