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
import java.util.ArrayList;
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

    @Column(nullable = false, updatable = false)
    private Integer nasaId;

    @Column(nullable = false, updatable = false)
    private String name;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Version
    private long version;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "camera", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<NasaPictureEntity> pictures = new ArrayList<>();

    public NasaCameraEntity(Integer nasaId,
                            String name) {
        this.nasaId = nasaId;
        this.name = name;
    }

}
