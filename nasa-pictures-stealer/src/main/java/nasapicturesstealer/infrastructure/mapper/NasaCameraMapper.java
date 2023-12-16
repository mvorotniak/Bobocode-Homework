package nasapicturesstealer.infrastructure.mapper;

import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NasaCameraMapper {

    NasaCamera toDomainEntity(NasaCameraEntity nasaCameraEntity);

    NasaCameraEntity toEntity(NasaCamera nasaCamera);
    
}
