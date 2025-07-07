package cz.svonavec.tennis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.facade.ReservationFacade;
import cz.svonavec.tennis.factory.ReservationFactory;
import cz.svonavec.tennis.models.dtos.ReservationCreateDTO;
import cz.svonavec.tennis.models.dtos.ReservationDTO;
import cz.svonavec.tennis.models.dtos.ReservationUpdateDTO;
import cz.svonavec.tennis.security.AuthEntryPoint;
import cz.svonavec.tennis.security.AuthTokenFilter;
import cz.svonavec.tennis.security.CustomAccessDeniedHandler;
import cz.svonavec.tennis.service.JwtService;
import cz.svonavec.tennis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
@Import({AuthTokenFilter.class, AuthEntryPoint.class, CustomAccessDeniedHandler.class})
@EnableMethodSecurity(prePostEnabled = true)
public class ReservationRestControllerTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationFacade reservationFacade;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    @WithMockUser(roles = "USER")
    void findById_reservationFound_returnsReservation() throws Exception {
        // Arrange
        when(reservationFacade.findById(1L)).thenReturn(ReservationFactory.createReservationDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.doubles").value(false))
                .andExpect(jsonPath("$.cost").value(60.00))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findById_reservationNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(reservationFacade.findById(2L)).thenThrow(new ResourceNotFoundException("Reservation not found"));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_unauthenticated_returnsUnauthorized() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_reservationsFound_returnsListOfReservations() throws Exception {
        // Arrange
        List<ReservationDTO> reservations = List.of(ReservationFactory.createReservationDTORest());
        when(reservationFacade.findAll()).thenReturn(reservations);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByCourt_reservationsFound_returnsReservationsForCourt() throws Exception {
        // Arrange
        List<ReservationDTO> reservations = List.of(ReservationFactory.createReservationDTORest());
        when(reservationFacade.findByCourt(1L)).thenReturn(reservations);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/court/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].court.id").value(1L));
    }

    @Test
    @WithMockUser(username = "+420907123456", roles = "USER")
    void findByPhone_userAccessingOwnReservations_returnsReservations() throws Exception {
        // Arrange
        List<ReservationDTO> reservations = List.of(ReservationFactory.createReservationDTORest());
        when(reservationFacade.findByPhone("+420907123456", true)).thenReturn(reservations);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/user")
                        .param("phoneNumber", "+420907123456")
                        .param("futureOnly", "true")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "+420907123456", roles = "USER")
    void create_userCreatingOwnReservation_returnsCreated() throws Exception {
        // Arrange
        BigDecimal expectedCost = new BigDecimal("150.00");
        when(reservationFacade.create(any(ReservationCreateDTO.class))).thenReturn(expectedCost);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationFactory.createReservationCreateDTO())))
                .andExpect(status().isCreated())
                .andExpect(content().string("150.00"));
    }

    @Test
    @WithMockUser(username = "+420907999999", roles = "USER")
    void create_userCreatingReservationForOtherUser_returnsForbidden() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationFactory.createReservationCreateDTO())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_validData_returnsUpdatedReservation() throws Exception {
        // Arrange
        when(reservationFacade.update(any(ReservationUpdateDTO.class))).thenReturn(ReservationFactory.createReservationDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationFactory.createReservationUpdateDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cost").value(60.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_validId_returnsDeletedReservation() throws Exception {
        // Arrange
        when(reservationFacade.delete(1L)).thenReturn(ReservationFactory.createReservationDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_userRole_returnsForbidden() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_reservationNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(reservationFacade.delete(1L))
                .thenThrow(new ResourceNotFoundException("Reservation not found"));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reservation not found"));
    }
}
