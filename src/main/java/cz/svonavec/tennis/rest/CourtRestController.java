package cz.svonavec.tennis.rest;

import cz.svonavec.tennis.facade.CourtFacade;
import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
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

import java.util.List;

@RestController
@RequestMapping("/api/courts")
@Tag(name = "Courts", description = "Court management service")
public class CourtRestController {
    private final CourtFacade courtFacade;

    @Autowired
    public CourtRestController(CourtFacade courtFacade) {
        this.courtFacade = courtFacade;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find court by ID", description = "Returns a court with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Court found"),
            @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<CourtDTO> findById(
            @Parameter(description = "ID of the court to be retrieved", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(courtFacade.findById(id));
    }

    @GetMapping
    @Operation(summary = "Find all courts", description = "Returns all courts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courts found")
    })
    public ResponseEntity<List<CourtDTO>> findAll(){
        return ResponseEntity.ok(courtFacade.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a new court", description = "Creates a new court and returns it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Court created successfully"),
            @ApiResponse(responseCode = "404", description = "Surface type not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<CourtDTO> create(
            @Parameter(description = "Court data to create", required = true)
            @Valid @RequestBody CourtCreateDTO courtCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courtFacade.create(courtCreateDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update court by ID", description = "Returns a updated court with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Court found and updated"),
            @ApiResponse(responseCode = "404", description = "Court or surface not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<CourtDTO> update(
            @Parameter(description = "ID of the court to be retrieved", required = true,
                    example = "1")
            @PathVariable long id,
            @Parameter(description = "Description of the court (location, or any notes)", example = "Fourth court on the left. Indoor footwear required.")
            @RequestParam(required = false) String description,
            @Parameter(description = "Id of the surface type for this court", example = "1")
            @RequestParam(required = false) Long surfaceTypeId) {
        return ResponseEntity.ok(courtFacade.update(id, description, surfaceTypeId));
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "Delete court by ID", description = "Deletes a court with corresponding ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Court found and deleted"),
            @ApiResponse(responseCode = "404", description = "Court not found")
    })
    public ResponseEntity<CourtDTO> delete(
            @Parameter(description = "ID of the court to be deleted", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(courtFacade.delete(id));
    }
}
