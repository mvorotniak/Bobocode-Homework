package nasapicturesstealer.infrastructure.repository;

import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NasaCameraEntityRepository extends JpaRepository<NasaCameraEntity, Long> {

  Optional<NasaCameraEntity> findByNasaId(Integer nasaId);

}
