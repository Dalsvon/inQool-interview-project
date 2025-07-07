package cz.svonavec.tennis.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTests {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;
    private final String testPhone = "+420908123456";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "7a99b411eb69e9e0d8d1f9467ca853f3dd2c2ac930d5471b2b8b9c44edb9517e");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1800000L);
        ReflectionTestUtils.setField(jwtService, "jwtRefreshExpiration", 60480000L);
    }

    @Test
    void getJwtExpiration_getsExpirationLength_returnsTheExpirationLength() {
        // Act
        long result = jwtService.getJwtExpiration();

        // Assert
        assertThat(result).isEqualTo(1800000L);
    }

    @Test
    void getJwtRefreshExpiration_getsRefreshExpirationLength_returnsTheRefreshExpirationLength() {
        // Act
        long result = jwtService.getJwtRefreshExpiration();

        // Assert
        assertThat(result).isEqualTo(60480000L);
    }

    @Test
    void generateToken_generatesTokenFromUserDetail_returnsToken() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertThat(token).isNotNull();
        assertFalse(token.isEmpty());
        Claims claims = jwtService.extractTokenClaims(token);
        assertThat(testPhone).isEqualTo(claims.getSubject());
        assertThat(claims.get("type", String.class)).isEqualTo("access");

        assertThat(claims.getExpiration()).isBefore((new Date(System.currentTimeMillis() + 1800000L)));
        assertThat(claims.getExpiration()).isAfter((new Date(System.currentTimeMillis())));
    }

    @Test
    void generateTokenFromPhone_generatesTokenFromPhone_returnsToken() {
        // Act
        String token = jwtService.generateTokenFromPhone(testPhone);

        // Assert
        assertThat(token).isNotNull();
        assertFalse(token.isEmpty());
        Claims claims = jwtService.extractTokenClaims(token);
        assertThat(testPhone).isEqualTo(claims.getSubject());
        assertThat(claims.get("type", String.class)).isEqualTo("access");

        assertThat(claims.getExpiration()).isBefore((new Date(System.currentTimeMillis() + 1800000L)));
        assertThat(claims.getExpiration()).isAfter((new Date(System.currentTimeMillis())));
    }

    @Test
    void generateRefreshToken_generatesRefreshTokenFromUserDetail_returnsToken() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);

        // Act
        String token = jwtService.generateRefreshToken(userDetails);

        // Assert
        assertThat(token).isNotNull();
        assertFalse(token.isEmpty());
        Claims claims = jwtService.extractTokenClaims(token);
        assertThat(testPhone).isEqualTo(claims.getSubject());
        assertThat(claims.get("type", String.class)).isEqualTo("refresh");

        assertThat(claims.getExpiration()).isBefore((new Date(System.currentTimeMillis() + 60480000L)));
        assertThat(claims.getExpiration()).isAfter((new Date(System.currentTimeMillis())));
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean result = jwtService.validateToken(token, userDetails, "access");

        // Assert
        assertTrue(result);
    }

    @Test
    void validateToken_validTokenNoType_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean result = jwtService.validateToken(token, userDetails, null);

        // Assert
        assertTrue(result);
    }

    @Test
    void validateToken_validTokenRefresh_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateRefreshToken(userDetails);

        // Act
        boolean result = jwtService.validateToken(token, userDetails, "refresh");

        // Assert
        assertTrue(result);
    }

    @Test
    void validateToken_expiredToken_returnsFalse() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String token = jwtService.generateToken(userDetails);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1800000L);

        // Act
        boolean result = jwtService.validateToken(token, userDetails, "access");

        // Assert
        assertFalse(result);
    }

    @Test
    void validateTokenPhone_validToken_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean result = jwtService.validateTokenPhone(token, testPhone, "access");

        // Assert
        assertTrue(result);
    }

    @Test
    void validateTokenPhone_validTokenNoType_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean result = jwtService.validateTokenPhone(token, testPhone, null);

        // Assert
        assertTrue(result);
    }

    @Test
    void validateRefreshTokenPhone_validTokenRefresh_returnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testPhone);
        String token = jwtService.generateRefreshToken(userDetails);

        // Act
        boolean result = jwtService.validateTokenPhone(token, testPhone, "refresh");

        // Assert
        assertTrue(result);
    }

    @Test
    void validateTokenPhone_expiredToken_returnsFalse() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String token = jwtService.generateToken(userDetails);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1800000L);

        // Act
        boolean result = jwtService.validateTokenPhone(token, testPhone, "access");

        // Assert
        assertFalse(result);
    }
}
