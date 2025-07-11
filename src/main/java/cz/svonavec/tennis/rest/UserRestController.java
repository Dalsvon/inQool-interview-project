package cz.svonavec.tennis.rest;

import cz.svonavec.tennis.facade.UserFacade;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Users management service")
public class UserRestController {
    private final UserFacade userFacade;

    @Autowired
    public UserRestController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by ID", description = "Returns a user with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> findById(
            @Parameter(description = "ID of the user to be retrieved", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(userFacade.findById(id));
    }

    @GetMapping("/phone")
    @Operation(summary = "Find user by phone number", description = "Returns a user with corresponding phone number",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("(hasRole('USER') and authentication.name == #id) or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> findByPhone(
            @Parameter(description = "Phone number of the user to be retrieved", required = true,
                    example = "+421907123456")
            @RequestParam String id) {
        return ResponseEntity.ok(userFacade.findByPhoneNumber(id));
    }

    @GetMapping
    @Operation(summary = "Find all users", description = "Returns all users",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> findAll(){
        return ResponseEntity.ok(userFacade.findAll());
    }

    @PutMapping
    @Operation(summary = "Update an user", description = "Returns an updated user with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "User data to update", required = true)
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userFacade.update(userUpdateDTO));
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "Delete an user by ID", description = "Deletes an user with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> delete(
            @Parameter(description = "ID of the user to be deleted", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(userFacade.delete(id));
    }
}
