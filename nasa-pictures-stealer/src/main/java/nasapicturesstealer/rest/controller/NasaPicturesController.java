package nasapicturesstealer.rest.controller;

import lombok.RequiredArgsConstructor;
import nasapicturesstealer.application.service.NasaService;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.rest.dto.NasaPictureDTO;
import nasapicturesstealer.rest.dto.NasaPicturesRequestDTO;
import nasapicturesstealer.rest.mapper.NasaPictureDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NasaPicturesController {
  
  private final NasaService nasaService;
  
  private final NasaPictureDTOMapper nasaPictureDTOMapper;
  
  @PostMapping("/pictures/steal")
  @ResponseStatus(HttpStatus.CREATED)
  public List<NasaPictureDTO> saveNasaPictures(@RequestBody NasaPicturesRequestDTO nasaPicturesRequestDTO) {
    List<NasaPicture> pictures = nasaService.stealAndSaveNasaPictures(nasaPicturesRequestDTO.sol());
    
    return this.nasaPictureDTOMapper.toResponseDTO(pictures);
  }

}
