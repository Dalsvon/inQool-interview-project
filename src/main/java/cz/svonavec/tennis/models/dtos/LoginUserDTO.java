package cz.svonavec.tennis.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "phoneNumber")
public class LoginUserDTO {
    @Schema(description = "Unique telephone number of the user. The number must begin with + character and county code (and area code in necessary). Do not include spaces.", example = "+412908123456")
    private String phoneNumber;

    @Schema(description = "Name of the user", example = "Jon Snow")
    private String name;

    // Unhashed password of the user - different from the entity password
    @Size(min = 8)
    @Schema(description = "Password of the user consisting from at least 8 characters. Include capital letters and numbers.", example = "NewYorkIsSoCool33")
    private String password;
}