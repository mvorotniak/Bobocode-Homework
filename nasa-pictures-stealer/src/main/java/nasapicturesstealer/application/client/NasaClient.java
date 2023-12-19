package nasapicturesstealer.application.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nasapicturesstealer.config.NasaProperties;
import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.domain.entity.NasaFields;
import nasapicturesstealer.domain.entity.NasaPicture;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Component
public class NasaClient {

    private final NasaProperties nasaProperties;

    private final RestTemplate restTemplate;
    
    public List<NasaPicture> stealNasaPictures(int sol) {
        String url = UriComponentsBuilder.fromHttpUrl(this.nasaProperties.getUrl())
                .queryParam(NasaFields.SOL, sol)
                .queryParam(NasaFields.API_KEY, this.nasaProperties.getKey())
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

        return jsonNodes.stream()
                .map(node -> NasaPicture.builder()
                        .nasaId(node.get(NasaFields.ID).asInt())
                        .imgSrc(node.get(NasaFields.IMG_SRC).asText())
                        .nasaCamera(buildNasaCamera(node))
                        .build())
                .toList();
    }

    private static NasaCamera buildNasaCamera(JsonNode node) {
        final JsonNode cameraNode = node.get(NasaFields.CAMERA);
        return NasaCamera.builder()
                .nasaId(cameraNode.get(NasaFields.ID).asInt())
                .name(cameraNode.get(NasaFields.NAME).asText())
                .build();
    }
    
}
