package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"courtId", "startsAt"})
public class ReservationCreateDTO {
    @Schema(description = "Is the game doubles game?")
    private boolean doubles;

    @Schema(description = "Time of start of the reserved game")
    private LocalDateTime startsAt;

    @Schema(description = "Time of end of the reserved game")
    private LocalDateTime endsAt;

    @Schema(description = "Id of court that is reserved")
    private Long courtId;
}
