package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "surfaceId")
public class CourtCreateDTO {
    @Schema(description = "Description of the court", nullable = true, example = "First outdoor court on the left from the entrance")
    private String description;

    @Schema(description = "Id of surface of the court", example = "1")
    private Long surfaceId;
}
