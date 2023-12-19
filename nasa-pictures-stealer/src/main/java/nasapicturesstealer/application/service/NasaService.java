package nasapicturesstealer.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nasapicturesstealer.application.client.NasaClient;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.repository.NasaPicturesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NasaService {

  private final NasaClient nasaClient;

  private final NasaPicturesRepository nasaPictureRepository;

  public List<NasaPicture> stealAndSaveNasaPictures(int sol) {
    List<NasaPicture> nasaPictures = this.nasaClient.stealNasaPictures(sol);

    List<NasaPicture> savedNasaPictures = this.nasaPictureRepository.saveDomainEntities(nasaPictures);

    log.info("Successfully saved [{}] Nasa pictures for sol=[{}] into database", savedNasaPictures.size(), sol);

    return savedNasaPictures;
  }

}
