package nasapicturesstealer.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PICTURES")
public class NasaPictureEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ToString.Exclude
  @JoinColumn(name = "CAMERA_ID", nullable = false)
  @ManyToOne(cascade = CascadeType.PERSIST)
  private NasaCameraEntity nasaCameraEntity;
  
  private Integer nasaId;
  
  private String imgSrc;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
