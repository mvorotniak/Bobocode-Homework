package nasapicturesstealer.infrastructure.repository;

import jakarta.persistence.QueryHint;
import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Set;

public interface NasaPictureEntityRepository extends JpaRepository<NasaPictureEntity, Long> {

    @QueryHints({@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")})
    Set<NasaPictureEntity> findByNasaIdIn(List<Integer> nasaIds);

}
