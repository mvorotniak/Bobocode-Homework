package nasapicturesstealer.infrastructure.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import nasapicturesstealer.infrastructure.mapper.NasaCameraMapper;
import nasapicturesstealer.infrastructure.mapper.NasaPictureMapper;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NasaPicturesRepository {

    private final NasaPictureEntityRepository nasaPictureEntityRepository;
  
    private final NasaCameraEntityRepository nasaCameraEntityRepository;
    
    private final NasaPictureMapper nasaPictureMapper;
    
    private final NasaCameraMapper nasaCameraMapper;
    
    public List<NasaPicture> saveDomainEntities(List<NasaPicture> pictures) {
      Map<Integer, Long> nasaIdToPictureId = this.nasaPictureEntityRepository
        .findAllByNasaIds(pictures.stream().map(NasaPicture::getNasaId).toList())
        .stream()
        .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

      Map<Integer, Long> nasaIdToCameraId = this.nasaCameraEntityRepository
        .findAllByNasaIds(pictures.stream().map(NasaPicture::getNasaCamera).map(NasaCamera::getNasaId).toList())
        .stream()
        .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
      
        List<NasaCameraEntity> camerasWithPhotos = pictures.stream()
          .collect(Collectors.groupingBy(NasaPicture::getNasaCamera))
          .entrySet()
          .stream()
          .map(entry -> {
              NasaCameraEntity cameraEntity = this.nasaCameraMapper.toEntity(entry.getKey())
                .toBuilder()
                .id(nasaIdToCameraId.get(entry.getKey().getNasaId()))
                .build();
              List<NasaPictureEntity> nasaPictureEntities = entry.getValue()
                .stream()
                .map(nasaPicture -> this.nasaPictureMapper.toEntity(nasaPicture)
                    .toBuilder()
                    .id(nasaIdToPictureId.get(nasaPicture.getNasaId()))
                    .nasaCameraEntity(cameraEntity)
                    .build())
                .toList();
              
              cameraEntity.setPictureEntities(nasaPictureEntities);
              
              return cameraEntity;
          })
          .toList();
        
        return nasaCameraEntityRepository.saveAll(camerasWithPhotos).stream()
                .map(NasaCameraEntity::getPictureEntities)
                .flatMap(Collection::stream)
                .map(nasaPictureMapper::toDomainEntity)
                .toList();
    }

}
