package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@Table(name = "Reservation")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "doubles")
    private boolean doubles;

    @NotNull
    @Column(name = "starts_at")
    private LocalDateTime startsAt;

    @NotNull
    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // So that administrator can change the cost regardless of the surface and time
    @NotNull
    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="court_id")
    private Court court;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation reservation)) {
            return false;
        }
        return Objects.equals(getStartsAt(), reservation.getStartsAt()) && Objects.equals(getEndsAt(), reservation.getEndsAt()) && Objects.equals(getCourt(), reservation.getCourt()) && Objects.equals(getCost(), reservation.getCost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartsAt(), getEndsAt(), getCost(), getCourt());
    }
}
