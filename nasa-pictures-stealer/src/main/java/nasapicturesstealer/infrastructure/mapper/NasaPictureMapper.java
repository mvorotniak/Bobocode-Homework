package nasapicturesstealer.infrastructure.mapper;

import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = NasaCameraMapper.class)
public interface NasaPictureMapper {
    
    @Mapping(source = "nasaCameraEntity", target = "nasaCamera")
    NasaPicture toDomainEntity(NasaPictureEntity nasaPictureEntity);
    
    NasaPictureEntity toEntity(NasaPicture nasaPicture);
    
}
