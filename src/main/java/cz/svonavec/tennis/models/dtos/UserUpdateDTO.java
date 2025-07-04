package cz.svonavec.tennis.models.dtos;

import cz.svonavec.tennis.models.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserUpdateDTO {
    @Schema(description = "Unique id of a user", accessMode = Schema.AccessMode.READ_ONLY,
            example = "1")
    private long id;

    @Schema(description = "Name of the user", example = "Jon Snow")
    private String name;

    @Schema(description = "List of roles of the user", example = "ADMIN", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Role> roles = new ArrayList<>();
}
