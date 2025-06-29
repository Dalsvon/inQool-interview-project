package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "court")
    private List<Reservation> reservations;

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
