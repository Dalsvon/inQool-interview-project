package cz.svonavec.tennis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.svonavec.tennis.facade.UserFacade;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.models.entities.Role;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Import({AuthTokenFilter.class, AuthEntryPoint.class, CustomAccessDeniedHandler.class})
@EnableMethodSecurity(prePostEnabled = true)
public class UserRestControllerTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserFacade userFacade;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_userFound_returnsUser() throws Exception {
        // Arrange
        when(userFacade.findById(1L)).thenReturn(UserFactory.createUserDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.phoneNumber").value("+421123456789"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }

    @Test
    @WithMockUser(username = "+421123456789", roles = "USER")
    void findByPhone_userAccessingOwnData_returnsUser() throws Exception {
        // Arrange
        when(userFacade.findByPhoneNumber("+421123456789")).thenReturn(UserFactory.createUserDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/phone")
                        .param("id", "+421123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.phoneNumber").value("+421123456789"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(username = "+421999999999", roles = "USER")
    void findByPhone_userAccessingOtherData_returnsForbidden() throws Exception {
        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/phone")
                        .param("id", "+421123456789"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_usersFound_returnsListOfUsers() throws Exception {
        // Arrange
        UserDTO adminUser = new UserDTO();
        adminUser.setId(2L);
        adminUser.setPhoneNumber("+421987654321");
        adminUser.setName("Admin User");
        adminUser.setRoles(List.of(Role.ADMIN, Role.USER));
        adminUser.setCreatedAt(LocalDateTime.now());

        List<UserDTO> users = List.of(UserFactory.createUserDTORest(), adminUser);
        when(userFacade.findAll()).thenReturn(users);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Admin User"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_validData_returnsUpdatedUser() throws Exception {
        // Arrange
        when(userFacade.update(any(UserUpdateDTO.class))).thenReturn(UserFactory.createUserDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserFactory.createUserUpdateDTORest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_validId_returnsDeletedUser() throws Exception {
        // Arrange
        when(userFacade.delete(1L)).thenReturn(UserFactory.createUserDTORest());

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
