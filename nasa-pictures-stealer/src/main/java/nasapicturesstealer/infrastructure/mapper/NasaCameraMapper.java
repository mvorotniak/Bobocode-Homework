package nasapicturesstealer.infrastructure.mapper;

import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NasaCameraMapper {

    NasaCamera toDomainEntity(NasaCameraEntity nasaCameraEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    NasaCameraEntity toEntity(NasaCamera nasaCamera);
    
}
