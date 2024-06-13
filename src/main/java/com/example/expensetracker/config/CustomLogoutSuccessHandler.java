package com.example.expensetracker.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    // Invalidate the session
    if (request.getSession() != null) {
      request.getSession().invalidate();
    }

    // Delete the JSESSIONID cookie
    Cookie cookie = new Cookie("JSESSIONID", null);
    cookie.setPath(request.getContextPath());
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    // Redirect to login page with logout message
    response.sendRedirect("/login?logout");
  }
}