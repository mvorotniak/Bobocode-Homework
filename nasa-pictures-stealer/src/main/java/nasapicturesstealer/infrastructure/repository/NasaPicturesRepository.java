package nasapicturesstealer.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nasapicturesstealer.domain.entity.NasaCamera;
import nasapicturesstealer.domain.entity.NasaPicture;
import nasapicturesstealer.infrastructure.entity.NasaCameraEntity;
import nasapicturesstealer.infrastructure.entity.NasaPictureEntity;
import nasapicturesstealer.infrastructure.mapper.NasaCameraMapper;
import nasapicturesstealer.infrastructure.mapper.NasaPictureMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NasaPicturesRepository {

    private final NasaPictureEntityRepository nasaPictureEntityRepository;
    
    private final NasaCameraEntityRepository nasaCameraEntityRepository;
    
    private final NasaPictureMapper nasaPictureMapper;
    
    private final NasaCameraMapper nasaCameraMapper;

    /**
     * Saves a collection of NasaPicture domain entities to the database.
     * It groups pictures by camera and processes them accordingly.
     *
     * @param pictures The list of NasaPicture domain entities to be saved.
     * @return A list of saved NasaPicture domain entities.
     */
    @Transactional
    public List<NasaPicture> saveDomainEntities(Collection<NasaPicture> pictures) {
        Map<NasaCamera, List<NasaPicture>> cameraToPicturesMap =
                pictures.stream().collect(Collectors.groupingBy(NasaPicture::nasaCamera));

        return cameraToPicturesMap.entrySet().stream()
                                  .flatMap(entry -> processCameraPictures(entry.getKey(), entry.getValue()).stream())
                                  .collect(Collectors.toList());
    }

    /**
     * Processes and saves pictures for a given camera.
     * It checks if the camera already exists in the database and proceeds accordingly.
     *
     * @param camera       The NasaCamera domain entity.
     * @param picturesList The list of NasaPicture domain entities associated with the camera.
     * @return A list of processed and saved NasaPicture domain entities.
     */
    private List<NasaPicture> processCameraPictures(NasaCamera camera,
                                                    List<NasaPicture> picturesList) {
        Optional<NasaCameraEntity> nasaCameraOpt = nasaCameraEntityRepository.findByNasaId(camera.nasaId());
        return nasaCameraOpt.map(cameraEntity -> processExistingCamera(picturesList, cameraEntity))
                            .orElseGet(() -> processNewCamera(camera, picturesList));
    }

    /**
     * Handles the processing and saving of pictures that not exists in the database.
     *
     * @param picturesList The list of NasaPicture domain entities.
     * @param cameraEntity The corresponding NasaCameraEntity from the database.
     * @return A list of processed and saved NasaPicture domain entities.
     */
    private List<NasaPicture> processExistingCamera(List<NasaPicture> picturesList,
                                                    NasaCameraEntity cameraEntity) {
        Map<Integer, NasaPictureEntity> storedPictures = getStoredPictures(picturesList);
        List<NasaPictureEntity> newPictures = getNewPictures(picturesList,
                                                             cameraEntity,
                                                             storedPictures);

        return saveAndMapPictures(newPictures, storedPictures.values());
    }

    /**
     * Handles the processing and saving of pictures for a new camera not yet in the database.
     *
     * @param camera       The new NasaCamera domain entity.
     * @param picturesList The list of NasaPicture domain entities associated with the new camera.
     * @return A list of processed and saved NasaPicture domain entities.
     */
    private List<NasaPicture> processNewCamera(NasaCamera camera,
                                               List<NasaPicture> picturesList) {
        NasaCameraEntity cameraEntity = nasaCameraEntityRepository.save(nasaCameraMapper.toEntity(camera));
        List<NasaPictureEntity> pictureEntities = createPictureEntities(picturesList,
                                                                        cameraEntity);

        return saveAndMapPictures(pictureEntities, List.of());
    }

    /**
     * Creates NasaPictureEntity objects for a list of NasaPicture domain entities.
     *
     * @param picturesList The list of NasaPicture domain entities.
     * @param cameraEntity The NasaCameraEntity to associate with these pictures.
     * @return A list of NasaPictureEntity objects.
     */
    private List<NasaPictureEntity> createPictureEntities(List<NasaPicture> picturesList,
                                                          NasaCameraEntity cameraEntity) {
        return picturesList.stream()
                           .map(picture -> nasaPictureMapper.toEntity(picture, cameraEntity))
                           .collect(Collectors.toList());
    }

    /**
     * Retrieves stored pictures from the database based on the given list of NasaPicture domain entities.
     *
     * @param picturesList The list of NasaPicture domain entities.
     * @return A map of Nasa IDs to corresponding NasaPictureEntity objects.
     */
    private Map<Integer, NasaPictureEntity> getStoredPictures(List<NasaPicture> picturesList) {
        return nasaPictureEntityRepository.findByNasaIdIn(getPictureNasaIds(picturesList))
                                          .stream()
                                          .collect(Collectors.toMap(NasaPictureEntity::getNasaId, Function.identity()));
    }

    /**
     * Filters and creates new picture entities for pictures that are not already stored in the database.
     *
     * @param picturesList    The list of NasaPicture domain entities.
     * @param cameraEntity    The corresponding NasaCameraEntity.
     * @param storedPictures  A map of already stored pictures.
     * @return A list of new NasaPictureEntity objects for pictures not already stored.
     */
    private List<NasaPictureEntity> getNewPictures(List<NasaPicture> picturesList,
                                                   NasaCameraEntity cameraEntity,
                                                   Map<Integer, NasaPictureEntity> storedPictures) {
        return picturesList.stream()
                           .filter(Predicate.not(picture -> storedPictures.containsKey(picture.nasaId())))
                           .map(picture -> nasaPictureMapper.toEntity(picture, cameraEntity))
                           .collect(Collectors.toList());
    }

    /**
     * Saves new picture entities and maps both new and persisted pictures to their domain representation.
     *
     * @param newPictures        The list of new NasaPictureEntity objects to be saved.
     * @param persistedPictures  A collection of already persisted NasaPictureEntity objects.
     * @return A list of NasaPicture domain entities representing both new and persisted entities.
     */
    private List<NasaPicture> saveAndMapPictures(List<NasaPictureEntity> newPictures,
                                                 Collection<NasaPictureEntity> persistedPictures) {
        if(newPictures.isEmpty()){
            log.info("No new pictures to save");
            return mapToDomainEntities(persistedPictures.stream().toList());
        } else {
            List<NasaPictureEntity> savedPictures = nasaPictureEntityRepository.saveAll(newPictures);
            if(!persistedPictures.isEmpty()){
                savedPictures.addAll(persistedPictures);
            }
            return mapToDomainEntities(savedPictures);
        }
    }

    /**
     * Converts a list of NasaPictureEntity objects to their corresponding domain entities.
     *
     * @param pictureEntities A list of NasaPictureEntity objects.
     * @return A list of NasaPicture domain entities.
     */
    private List<NasaPicture> mapToDomainEntities(List<NasaPictureEntity> pictureEntities) {
        return pictureEntities.stream().map(nasaPictureMapper::toDomainEntity)
                              .collect(Collectors.toList());
    }

    /**
     * Extracts and returns NASA IDs from a list of NasaPicture domain entities.
     *
     * @param picturesList The list of NasaPicture domain entities.
     * @return A list of NASA IDs.
     */
    private static List<Integer> getPictureNasaIds(List<NasaPicture> picturesList) {
        return picturesList.stream()
                           .map(NasaPicture::nasaId)
                           .toList();
    }

}
