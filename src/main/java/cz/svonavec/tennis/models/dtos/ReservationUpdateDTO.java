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
public class ReservationUpdateDTO {
    @Schema(description = "Unique id of a reservation", example = "1")
    private long id;

    @Schema(description = "Is the game doubles game?", nullable = true)
    private boolean doubles;

    @Schema(description = "Time of start of the reserved game", nullable = true)
    private LocalDateTime startsAt;

    @Schema(description = "Time of end of the reserved game", nullable = true)
    private LocalDateTime endsAt;

    @Schema(description = "Cost in czech crowns for the whole reservation", nullable = true, example = "1")
    private BigDecimal cost;
}
