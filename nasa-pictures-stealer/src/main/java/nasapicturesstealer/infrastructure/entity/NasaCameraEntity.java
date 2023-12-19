package nasapicturesstealer.infrastructure.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.OffsetDateTime;
import java.util.List;

@EqualsAndHashCode(exclude = {"pictures"})
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cameras")
public class NasaCameraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nasa_id")
    private Integer nasaId;

    private String name;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Version
    private long version;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "camera", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<NasaPictureEntity> pictures;

    public NasaCameraEntity(Integer nasaId,
                            String name) {
        this.nasaId = nasaId;
        this.name = name;
    }

}
