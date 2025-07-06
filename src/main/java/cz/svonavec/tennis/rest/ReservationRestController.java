package cz.svonavec.tennis.rest;

import cz.svonavec.tennis.facade.ReservationFacade;
import cz.svonavec.tennis.models.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Reservation management service")
public class ReservationRestController {
    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationRestController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find reservation by ID", description = "Returns a reservation with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> findById(
            @Parameter(description = "ID of the reservation to be retrieved", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(reservationFacade.findById(id));
    }

    @GetMapping
    @Operation(summary = "Find all reservations", description = "Returns all reservations",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> findAll(){
        return ResponseEntity.ok(reservationFacade.findAll());
    }

    @GetMapping("/court/{id}")
    @Operation(summary = "Find all reservations for the court",
            description = "Returns all reservations for the given court ordered by date of creation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> findByCourt(
            @Parameter(description = "ID of the court", required = true,
                    example = "1")
            @PathVariable long id){
        return ResponseEntity.ok(reservationFacade.findByCourt(id));
    }

    @GetMapping("/user")
    @Operation(summary = "Find all reservations for the user",
            description = "Returns all reservations for the given user and optionally only those starting in future",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations found")
    })
    @PreAuthorize("(hasRole('USER') and authentication.name == #phoneNumber) or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> findByPhone(
            @Parameter(description = "Phone number of the user", required = true,
                    example = "+420907123456")
            @RequestParam String phoneNumber,
            @Parameter(description = "Find only reservations in the future")
            @RequestParam(defaultValue = "false") boolean futureOnly) {
        return ResponseEntity.ok(reservationFacade.findByPhone(phoneNumber, futureOnly));
    }

    @PostMapping
    @Operation(summary = "Create a new reservation",
            description = "Creates a new reservation and returns its cost in CZK.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("(hasRole('USER') and authentication.name == #reservationCreateDTO.phoneNumber) or hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> create(
            @Parameter(description = "Reservation data to create", required = true)
            @Valid @RequestBody ReservationCreateDTO reservationCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationFacade.create(reservationCreateDTO));
    }

    @PutMapping
    @Operation(summary = "Update reservation", description = "Returns a updated reservation with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found and updated"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> update(
            @Parameter(description = "Reservation data to update", required = true)
            @Valid @RequestBody ReservationUpdateDTO reservationUpdateDTO) {
        return ResponseEntity.ok(reservationFacade.update(reservationUpdateDTO));
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "Delete reservation by ID", description = "Deletes a reservation with corresponding ID",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found and deleted"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> delete(
            @Parameter(description = "ID of the reservation to be deleted", required = true,
                    example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(reservationFacade.delete(id));
    }
}
