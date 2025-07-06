package cz.svonavec.tennis.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class handles unauthorized access to API endpoints by returning 401 code.
 */
@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Sends 401 error and message via response for auth exceptions.
     *
     * @param request servlet request
     * @param response servlet response
     * @param authException thrown exception
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}