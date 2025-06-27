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
public class SurfaceTypeDTO {
    @Schema(description = "Unique id of a surface", accessMode = Schema.AccessMode.READ_ONLY,
            example = "1")
    private long id;

    @Schema(description = "Time of soft deletion of the surface", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deletedAt;

    @Schema(description = "Name of the surface", example = "Grass")
    private String name;

    @Schema(description = "Cost in czech crowns for a minute of play on this surface", nullable = false, example = "1")
    private BigDecimal costPerMinute;
}
