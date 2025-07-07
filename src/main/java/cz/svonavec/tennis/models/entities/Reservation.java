package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
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

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // So that administrator can change the cost regardless of the surface and time
    @NotNull
    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="court_id")
    private Court court;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private static BigDecimal DOUBLES_MULTIPLIER = BigDecimal.valueOf(1.5);

    /**
     * This method calculates cost for the whole reservation. This cost can be different from the value calculated here,
     * because administrator can change it (for example for discount).
     *
     * Cost is calculated from cost per minute for surface type multiplied by whole minutes in reservation.
     * If the reservation is less than minute long, customer still pays for 1 minute. This cost is then multiplied by
     * DOUBLES_MULTIPLIER if the game is doubles game.
     */
    public void calculateCost() {
        if (court == null || court.getSurface() == null || getStartsAt() == null || getEndsAt() == null) {
            cost = BigDecimal.ZERO;
            return;
        }
        Duration duration = Duration.between(startsAt, endsAt);
        long minutes = duration.toMinutes();
        BigDecimal gameMultiplier = isDoubles() ? DOUBLES_MULTIPLIER : BigDecimal.ONE;
        BigDecimal minuteCost = court.getSurface().getCostPerMinute().multiply(gameMultiplier);
        // You pay for minutes you play, unless you create a reservation lasting less than a minute.
        // In such case you pay for 1 minute
        cost = minutes != 0 ? minuteCost.multiply(BigDecimal.valueOf(minutes)) : minuteCost;
        cost = cost.setScale(2, java.math.RoundingMode.HALF_UP);
    }

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
