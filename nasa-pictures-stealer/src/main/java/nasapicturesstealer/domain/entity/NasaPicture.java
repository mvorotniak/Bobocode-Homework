package nasapicturesstealer.domain.entity;

import lombok.Builder;

@Builder
public record NasaPicture(Integer nasaId, String imgSrc, NasaCamera nasaCamera) {

}
