package cz.svonavec.tennis.models.dtos;

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

    @Schema(description = "Time of creation of the reservation", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Is the game doubles game?")
    private boolean doubles;

    @Schema(description = "Time of start of the reserved game")
    private LocalDateTime startsAt;

    @Schema(description = "Time of end of the reserved game")
    private LocalDateTime endsAt;

    @Schema(description = "Cost in czech crowns for the whole reservation", nullable = false, example = "1")
    private BigDecimal cost;

    @Schema(description = "Court that is reserved", accessMode = Schema.AccessMode.READ_ONLY)
    private CourtDTO court;

    @Schema(description = "ID of the user that is reserving the court", accessMode = Schema.AccessMode.READ_ONLY)
    private long userId;
}
