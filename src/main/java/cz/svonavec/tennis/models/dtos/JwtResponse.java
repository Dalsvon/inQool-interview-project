package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "phoneNumber")
@Schema(description = "JWT response with access and refresh tokens")
public class JwtResponse {
    @Schema(description = "Access token")
    private String accessToken;

    @Schema(description = "Refresh token")
    private String refreshToken;

    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Unique user ID", example = "1")
    private Long id;

    @Schema(description = "Unique telephone number of the user. The number must begin with + character and county code (and area code in necessary). Do not include spaces.", example = "+412908123456")
    private String phoneNumber;

    @Schema(description = "Name of the user", example = "Jon Snow")
    private String name;

    public JwtResponse(String accessToken, String refreshToken, Long id,
                       String phoneNumber, String name) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
