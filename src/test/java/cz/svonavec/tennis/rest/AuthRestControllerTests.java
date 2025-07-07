package cz.svonavec.tennis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.svonavec.tennis.config.AuthTestSecurityConfig;
import cz.svonavec.tennis.facade.UserFacade;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.service.JwtService;
import cz.svonavec.tennis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRestController.class)
@Import(AuthTestSecurityConfig.class)
public class AuthRestControllerTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserFacade userFacade;

    private Authentication testAuthentication;

    private User testUser;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();

        testUser = new User();
        testUser.setId(1L);
        testUser.setPhoneNumber("+420907123456");
        testUser.setName("John Doe");
        testUser.setPassword("hashedPassword");
        testUser.setRoles(List.of(Role.USER));
        testUser.setCreatedAt(LocalDateTime.now());

        testAuthentication = new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities());
    }

    @Test
    void login_validCredentials_returnsJwtResponse() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(testAuthentication);
        when(jwtService.generateToken(testUser)).thenReturn("access.jwt.token");
        when(jwtService.generateRefreshToken(testUser)).thenReturn("refresh.jwt.token");

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserFactory.createLoginDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.jwt.token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh.jwt.token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.phoneNumber").value("+420907123456"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void login_invalidJsonFormat_returnsBadRequest() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_validData_returnsCreatedUser() throws Exception {
        // Arrange
        when(userFacade.register(any(UserRegisterDTO.class))).thenReturn(UserFactory.createUserDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserFactory.createRegisterDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.phoneNumber").value("+421123456789"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }

    @Test
    void refresh_validRefreshToken_returnsNewTokens() throws Exception {
        // Arrange
        String refreshToken = "valid.refresh.token";
        String phoneNumber = "+420907123456";
        when(jwtService.extractUsername(refreshToken)).thenReturn(phoneNumber);
        when(jwtService.validateTokenPhone(refreshToken, phoneNumber, "refresh")).thenReturn(true);
        when(jwtService.generateTokenFromPhone(phoneNumber)).thenReturn("new.access.token");

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new.access.token"))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    void refresh_invalidRefreshToken_returnsUnauthorized() throws Exception {
        // Arrange
        String invalidRefreshToken = "invalid.refresh.token";
        String phoneNumber = "+420907123456";
        when(jwtService.extractUsername(invalidRefreshToken)).thenReturn(phoneNumber);
        when(jwtService.validateTokenPhone(invalidRefreshToken, phoneNumber, "refresh")).thenReturn(false);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                        .param("refreshToken", invalidRefreshToken))
                .andExpect(status().isUnauthorized());
    }
}
