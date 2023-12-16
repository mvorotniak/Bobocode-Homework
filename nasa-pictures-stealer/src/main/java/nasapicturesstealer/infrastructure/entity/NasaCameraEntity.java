package nasapicturesstealer.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CAMERAS")
public class NasaCameraEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private Integer nasaId;
  
  private String name;

  @CreationTimestamp
  private LocalDateTime createdAt;
  
  @OneToMany(mappedBy = "nasaCameraEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<NasaPictureEntity> pictureEntities;

}
