package cz.svonavec.tennis.rest;

import cz.svonavec.tennis.facade.UserFacade;
import cz.svonavec.tennis.models.dtos.JwtResponse;
import cz.svonavec.tennis.models.dtos.RefreshResponse;
import cz.svonavec.tennis.models.dtos.LoginUserDTO;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthRestController {
    private final UserFacade userFacade;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthRestController(UserFacade userFacade, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userFacade = userFacade;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Sign in", description = "Authenticate user and return access/refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Incorrect phone number or password")
    })
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginUserDTO loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getPhoneNumber(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        User user = (User) userDetails;

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken, user.getId(), user.getPhoneNumber(), user.getName()));
    }

    @PostMapping(("/register"))
    @Operation(summary = "Register a user", description = "Registers user and returns it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<UserDTO> register(
            @Parameter(description = "User data to create", required = true)
            @Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userFacade.register(userRegisterDTO));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh tokens", description = "Refresh access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token valid. New tokens returned"),
            @ApiResponse(responseCode = "401", description = "Authentication failed.")
    })
    public ResponseEntity<RefreshResponse> refresh(
            @Parameter(description = "Refresh token", required = true) @RequestParam String refreshToken) {
        String phone = jwtService.extractUsername(refreshToken);
        if (jwtService.validateTokenPhone(refreshToken, phone, "refresh")) {
            String accessToken = jwtService.generateTokenFromPhone(phone);
            return ResponseEntity.ok(new RefreshResponse(accessToken, refreshToken, phone));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
