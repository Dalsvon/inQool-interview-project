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
    @Schema(description = "Unique id of a user",
            example = "1", nullable = false)
    private long id;

    @Schema(description = "Name of the user", example = "Jon Snow", nullable = true)
    private String name;

    @Schema(description = "List of roles of the user", example = "[\"USER\", \"ADMIN\"]", nullable = true)
    private List<Role> roles = new ArrayList<>();
}
