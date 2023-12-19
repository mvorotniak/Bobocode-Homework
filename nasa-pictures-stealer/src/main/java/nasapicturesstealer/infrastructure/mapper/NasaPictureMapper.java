package nasapicturesstealer.infrastructure.mapper;

import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = NasaCameraMapper.class)
public interface NasaPictureMapper {
    
    @Mapping(source = "camera", target = "nasaCamera")
    NasaPicture toDomainEntity(NasaPictureEntity picture);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(source = "nasaPicture.nasaId", target = "nasaId")
    @Mapping(source = "cameraEntity", target = "camera")
    NasaPictureEntity toEntity(NasaPicture nasaPicture, NasaCameraEntity cameraEntity);
    
}
