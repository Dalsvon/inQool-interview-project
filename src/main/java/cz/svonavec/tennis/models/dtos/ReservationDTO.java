package cz.svonavec.tennis.models.dtos;

import cz.svonavec.tennis.models.entities.Court;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReservationDTO {
    @Schema(description = "Unique id of a reservation", accessMode = Schema.AccessMode.READ_ONLY,
            example = "1")
    private long id;

    @Schema(description = "Time of soft deletion of the reservation", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deletedAt;

    @Schema(description = "Is the game doubles game?")
    private boolean doubles;

    @Schema(description = "Time of start of the reserved game")
    private LocalDateTime startsAt;

    @Schema(description = "Time of end of the reserved game")
    private LocalDateTime endsAt;

    @Schema(description = "Cost in czech crowns for the whole reservation", nullable = false, example = "1")
    private BigDecimal cost;

    @Schema(description = "Court that is reserved", accessMode = Schema.AccessMode.READ_ONLY)
    private Court court;
}
