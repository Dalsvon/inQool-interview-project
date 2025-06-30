package cz.svonavec.tennis.rest;

import cz.svonavec.tennis.facade.SurfaceTypeFacade;
import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/surfaces")
@Tag(name = "Surface Types", description = "surface type management service")
public class SurfaceTypeRestController {
    private final SurfaceTypeFacade surfaceTypeFacade;

    @Autowired
    public SurfaceTypeRestController(SurfaceTypeFacade surfaceTypeFacade) {
        this.surfaceTypeFacade = surfaceTypeFacade;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find surface by ID", description = "Returns a surface with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Surface found"),
            @ApiResponse(responseCode = "404", description = "Surface not found")
    })
    public ResponseEntity<SurfaceTypeDTO> findById(
            @Parameter(description = "ID of the surface type to be retrieved", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(surfaceTypeFacade.findById(id));
    }

    @GetMapping
    @Operation(summary = "Find all surfaces", description = "Returns all surfaces")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Surfaces found")
    })
    public ResponseEntity<List<SurfaceTypeDTO>> findAll(){
        return ResponseEntity.ok(surfaceTypeFacade.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a new surface", description = "Creates a new surface type and returns it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Surface type created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SurfaceTypeDTO> createReservation(
            @Parameter(description = "Surface data to create", required = true)
            @Valid @RequestBody SurfaceTypeCreateDTO surfaceTypeCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surfaceTypeFacade.create(surfaceTypeCreateDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update surface by ID", description = "Returns a updated surface with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Surface found and updated"),
            @ApiResponse(responseCode = "404", description = "Surface not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SurfaceTypeDTO> update(
            @Parameter(description = "ID of the surface type to be retrieved", required = true,
                    example = "1")
            @PathVariable long id,
            @Parameter(description = "Name of the surface", example = "Grass")
            @RequestParam(required = false) String name,
            @Parameter(description = "Cost of the surface per minute in CZ (will be rounded to 2 decimal places)", example = "3.25")
            @RequestParam(required = false) BigDecimal cost) {
        return ResponseEntity.ok(surfaceTypeFacade.update(id, cost, name));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete surface by ID", description = "Deletes a surface with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Surface found and deleted"),
            @ApiResponse(responseCode = "404", description = "Surface not found")
    })
    public ResponseEntity<SurfaceTypeDTO> delete(
            @Parameter(description = "ID of the surface type to be deleted", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(surfaceTypeFacade.delete(id));
    }
}
