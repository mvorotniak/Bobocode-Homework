package nasapicturesstealer.domain.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NasaPicture {

  private Integer nasaId;
  
  private String imgSrc;
  
  private NasaCamera nasaCamera;
  
}
