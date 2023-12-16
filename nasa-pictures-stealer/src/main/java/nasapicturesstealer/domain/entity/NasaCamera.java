package nasapicturesstealer.domain.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NasaCamera {
    
    private Integer nasaId;
    
    private String name;
    
}
