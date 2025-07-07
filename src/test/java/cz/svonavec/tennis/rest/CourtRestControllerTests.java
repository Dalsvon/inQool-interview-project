package cz.svonavec.tennis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.facade.CourtFacade;
import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourtRestController.class)
@Import({AuthTokenFilter.class, AuthEntryPoint.class, CustomAccessDeniedHandler.class})
@EnableMethodSecurity(prePostEnabled = true)
public class CourtRestControllerTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourtFacade courtFacade;

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
    void findById_courtFound_returnsCourt() throws Exception {
        // Arrange
        when(courtFacade.findById(1L)).thenReturn(CourtFactory.createCourtDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Standard tennis court"))
                .andExpect(jsonPath("$.surface.id").value(1L))
                .andExpect(jsonPath("$.surface.name").value("Grass"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findById_courtNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(courtFacade.findById(2L)).thenThrow(new ResourceNotFoundException("Court not found"));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courts/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Court not found"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_courtsFound_returnsListOfCourts() throws Exception {
        // Arrange
        List<CourtDTO> courts = List.of(CourtFactory.createCourtDTORest());
        when(courtFacade.findAll()).thenReturn(courts);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Standard tennis court"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_validCourtData_returnsCreated() throws Exception {
        // Arrange
        when(courtFacade.create(any(CourtCreateDTO.class))).thenReturn(CourtFactory.createCourtDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/courts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CourtFactory.createCourtCreateDTORest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Standard tennis court"))
                .andExpect(jsonPath("$.surface.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_userRole_returnsForbidden() throws Exception {
        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/courts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CourtFactory.createCourtDTORest())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_validData_returnsUpdatedCourt() throws Exception {
        // Arrange
        when(courtFacade.update(eq(1L), eq("Updated description"), eq(2L)))
                .thenReturn(CourtFactory.createCourtDTORest());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/api/courts/1")
                        .param("description", "Updated description")
                        .param("surfaceTypeId", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Standard tennis court"));
    }

   @Test
    @WithMockUser(roles = "ADMIN")
    void delete_validId_returnsDeletedCourt() throws Exception {
        // Arrange
        when(courtFacade.delete(1L)).thenReturn(CourtFactory.createCourtDTORest());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
