package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@Table(name = "Court")
public class Court implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="surface_id")
    private SurfaceType surface;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Court court)) {
            return false;
        }
        return Objects.equals(getDescription(), court.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
