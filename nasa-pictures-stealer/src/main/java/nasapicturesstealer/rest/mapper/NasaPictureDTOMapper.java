package nasapicturesstealer.rest.mapper;

import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.rest.dto.NasaPictureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NasaPictureDTOMapper {
  
  List<NasaPictureDTO> toResponseDTO(List<NasaPicture> nasaPictures);

  @Mapping(source = "nasaCamera.name", target = "cameraName")
  NasaPictureDTO toNasaPictureDTO(NasaPicture nasaPicture);

}
