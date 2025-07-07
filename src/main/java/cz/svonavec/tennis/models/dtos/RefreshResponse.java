package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "phoneNumber")
@Schema(description = "Refresh response with access and refresh tokens")
public class RefreshResponse {
    @Schema(description = "Access token")
    private String accessToken;

    @Schema(description = "Refresh token")
    private String refreshToken;

    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Unique telephone number of the user. The number must begin with + character and county code (and area code in necessary). Do not include spaces.", example = "+412908123456")
    private String phoneNumber;

    public RefreshResponse(String accessToken, String refreshToken,
                       String phoneNumber) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.phoneNumber = phoneNumber;
    }
}
