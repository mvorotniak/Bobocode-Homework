package nasapicturesstealer.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@EqualsAndHashCode(exclude = {"camera"})
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pictures")
public class NasaPictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @JoinColumn(name = "camera_id", nullable = false)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private NasaCameraEntity camera;

    private Integer nasaId;

    private String imgSrc;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Version
    private long version;

    public NasaPictureEntity(NasaCameraEntity camera,
                             Integer nasaId,
                             String imgSrc) {
        this.camera = camera;
        this.nasaId = nasaId;
        this.imgSrc = imgSrc;
    }

}
