package cz.svonavec.tennis.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class handles authorized access to API endpoints with insufficient privileges or roles by returning 403 code.
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * Sends 403 error and message via response for access denied exceptions.
     *
     * @param request servlet request
     * @param response servlet response
     * @param accessDeniedException thrown exception
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access denied: {}", accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("text/plain");
        response.getWriter().write("Error: Forbidden - Access denied");
        response.getWriter().flush();
    }
}
