package com.technogise.leavemanagement.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.Cookie;

public class JWTRequestFilterTest  {

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private FilterChain filterChain;

    private JWTRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JWTRequestFilter(jwtUtils);
    }

    @Test
    @DisplayName("doFilterInternal should authenticate when auth cookie is present and valid")
    void testDoFilterInternal_AuthCookiePresentAndValid() throws Exception {
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    Cookie authCookie = new Cookie("AUTH-TOKEN", "validToken");
    request.setCookies(authCookie);
    Authentication mockAuth = mock(Authentication.class);
    when(jwtUtils.verifyAndGetAuthentication(anyString())).thenReturn(mockAuth);

    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    verify(jwtUtils).verifyAndGetAuthentication("validToken");
    assertEquals(mockAuth, SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilterInternal should not authenticate when auth cookie is present but invalid")
    void testDoFilterInternal_AuthCookiePresentButInvalid() throws Exception {
      
       MockHttpServletRequest request = new MockHttpServletRequest();
       MockHttpServletResponse response = new MockHttpServletResponse();
       Cookie authCookie = new Cookie("AUTH-TOKEN", "invalidToken");
       request.setCookies(authCookie);
       when(jwtUtils.verifyAndGetAuthentication(anyString())).thenReturn(null);

       jwtRequestFilter.doFilterInternal(request, response, filterChain);

       verify(jwtUtils).verifyAndGetAuthentication("invalidToken");
       assertNull(SecurityContextHolder.getContext().getAuthentication());
       verify(filterChain).doFilter(request, response);
    }
}
