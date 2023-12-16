package nasapicturesstealer.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.domain.entity.NasaFields;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.repository.NasaPicturesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class NasaService {

  @Value("${nasa.url}")
  private String nasaUrl;
  
  @Value("${nasa.key}")
  private String nasaKey;
  
  private final NasaPicturesRepository nasaPictureRepository;
  
  private final RestTemplate restTemplate;

  public List<NasaPicture> getAllNasaPictures(int sol) {
    String url = UriComponentsBuilder.fromHttpUrl(nasaUrl)
            .queryParam(NasaFields.SOL, sol)
            .queryParam(NasaFields.API_KEY, nasaKey)
            .toUriString();
    
    log.info("Calling Nasa Service with url=[{}] with sol=[{}] to get pictures...", url, sol);
    
    Optional<JsonNode> jsonNode = Optional.ofNullable(restTemplate.getForObject(url, JsonNode.class));
    List<JsonNode> jsonNodes = jsonNode
            .map(obj -> StreamSupport.stream(obj.findValue(NasaFields.PHOTOS).spliterator(), true).toList())
            .orElse(Collections.emptyList());
    
    log.info("Found a total of [{}] pictures in Nasa Service by sol=[{}]", jsonNodes.size(), sol);
    
    if (jsonNodes.isEmpty()) {
      return Collections.emptyList();
    }
    
    List<NasaPicture> pictures = jsonNodes.stream()
            .map(node -> NasaPicture.builder()
                    .nasaId(node.get(NasaFields.ID).asInt())
                    .imgSrc(node.get(NasaFields.IMG_SRC).asText())
                    .nasaCamera(NasaCamera.builder()
                            .nasaId(node.get(NasaFields.CAMERA).get(NasaFields.ID).asInt())
                            .name(node.get(NasaFields.CAMERA).get(NasaFields.NAME).asText())
                            .build())
                    .build())
            .toList();

    List<NasaPicture> savedNasaPictures = this.nasaPictureRepository.saveDomainEntities(pictures);
    
    log.info("Successfully saved [{}] Nasa pictures for sol=[{}] into database", savedNasaPictures.size(), sol);
    
    return savedNasaPictures;
  }
  
}
