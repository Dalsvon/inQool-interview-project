package cz.svonavec.tennis.models.dtos;

import cz.svonavec.tennis.models.entities.SurfaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CourtDTO {

    @Schema(description = "Unique id of a court", accessMode = Schema.AccessMode.READ_ONLY,
            example = "1")
    private long id;

    @Schema(description = "Time of soft deletion of the court", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deletedAt;

    @Schema(description = "Description of the court", nullable = true, example = "First outdoor court on the left from the entrance")
    private String description;

    @Schema(description = "Surface of the court")
    private SurfaceTypeDTO surface;
}
