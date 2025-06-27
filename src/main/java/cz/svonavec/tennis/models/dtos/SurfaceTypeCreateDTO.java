package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class SurfaceTypeCreateDTO {
    @Schema(description = "Name of the surface", example = "Grass")
    private String name;

    @Schema(description = "Cost in czech crowns for a minute of play on this surface", example = "1")
    private BigDecimal costPerMinute;
}
