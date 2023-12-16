package nasapicturesstealer.rest.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import nasapicturesstealer.application.service.NasaService;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.rest.dto.NasaPictureDTO;
import nasapicturesstealer.rest.dto.NasaPicturesRequestDTO;
import nasapicturesstealer.rest.mapper.NasaPictureDTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NasaPicturesController {
  
  private final NasaService nasaService;
  
  private final NasaPictureDTOMapper nasaPictureDTOMapper;
  
  @PostMapping("/pictures/steal")
  public ResponseEntity<List<NasaPictureDTO>> getNasaPictures(@RequestBody NasaPicturesRequestDTO nasaPicturesRequestDTO) {
    List<NasaPicture> pictures = nasaService.getAllNasaPictures(nasaPicturesRequestDTO.getSol());
    
    return ResponseEntity.ok(this.nasaPictureDTOMapper.toResponseDTO(pictures));
  }

}
