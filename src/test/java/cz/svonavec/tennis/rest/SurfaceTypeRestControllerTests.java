package cz.svonavec.tennis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.facade.SurfaceTypeFacade;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurfaceTypeRestController.class)
@Import({AuthTokenFilter.class, AuthEntryPoint.class, CustomAccessDeniedHandler.class})
@EnableMethodSecurity(prePostEnabled = true)
public class SurfaceTypeRestControllerTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SurfaceTypeFacade surfaceTypeFacade;

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
    void findById_surfaceFound_returnsSurface() throws Exception {
        // Arrange
        when(surfaceTypeFacade.findById(1L)).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/surfaces/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Grass"))
                .andExpect(jsonPath("$.costPerMinute").value(BigDecimal.ONE));
    }

    @Test
    void findById_unauthenticated_returnsUnauthorized() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/surfaces/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_surfacesFound_returnsListOfSurfaces() throws Exception {
        // Arrange
        List<SurfaceTypeDTO> surfaces = List.of(SurfaceTypeFactory.createSurfaceTypeDTORest());
        when(surfaceTypeFacade.findAll()).thenReturn(surfaces);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/surfaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Grass"))
                .andExpect(jsonPath("$[0].costPerMinute").value(BigDecimal.ONE));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_validSurfaceData_returnsCreated() throws Exception {
        // Arrange
        when(surfaceTypeFacade.create(any(SurfaceTypeCreateDTO.class))).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/surfaces")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SurfaceTypeFactory.createSurfaceTypeCreateDTORest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Grass"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_userRole_returnsForbidden() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/surfaces")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SurfaceTypeFactory.createSurfaceTypeCreateDTORest())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_validData_returnsUpdatedSurface() throws Exception {
        // Arrange
        when(surfaceTypeFacade.update(eq(1L), eq(new BigDecimal("1")), eq("Clay")))
                .thenReturn(SurfaceTypeFactory.createSurfaceTypeDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/surfaces/1")
                        .param("cost", "1")
                        .param("name", "Clay")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Grass"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_surfaceNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(surfaceTypeFacade.update(eq(2L), any(), any()))
                .thenThrow(new ResourceNotFoundException("Surface not found"));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/surfaces/2")
                        .param("name", "Updated Surface")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Surface not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_validId_returnsDeletedSurface() throws Exception {
        // Arrange
        when(surfaceTypeFacade.delete(1L)).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/surfaces/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
