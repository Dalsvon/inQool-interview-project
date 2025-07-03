package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@Table(name = "Surface")
public class SurfaceType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @Column(name = "cost_per_minute")
    private BigDecimal costPerMinute;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurfaceType surfaceType)) {
            return false;
        }
        return Objects.equals(getName(), surfaceType.getName()) && Objects.equals(getCostPerMinute(), surfaceType.getCostPerMinute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCostPerMinute());
    }
}
