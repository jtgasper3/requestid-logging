package io.github.jtgasper3.requestlogging;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.MDC;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class RequestLoggerTest {
    FilterChain filterChain;
    HttpServletRequest  httpServletRequest;
    HttpServletResponse httpServletResponse;

    @BeforeTest public void setup() {
        filterChain = Mockito.mock(FilterChain.class);
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        httpServletResponse = Mockito.mock(HttpServletResponse.class);
    }

    @Test public void GenerateOurOwnID() throws IOException, ServletException {
        RequestIdLoggingFilter classUnderTest = new RequestIdLoggingFilter();

        when(httpServletRequest.getHeader("X-Request-Id")).thenReturn("abcd-123");
        doAnswer((Answer<Void>) invocation -> {
            final String requestId = MDC.get("X-Request-Id");
            assertNotNull(requestId);
            assertEquals(requestId, "abcd-123");
            return null;
        })
        .when(filterChain)
        .doFilter(anyObject(), anyObject());

        classUnderTest.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test public void ServerProviderRequestId() throws IOException, ServletException {
        RequestIdLoggingFilter classUnderTest = new RequestIdLoggingFilter();

        doReturn(null).when(httpServletRequest).getHeader("X-Request-Id");
        doAnswer((Answer<Void>) invocation -> {
            final String requestId = MDC.get("X-Request-Id");
            assertNotNull(requestId);
            assertNotEquals(requestId, "");
            return null;
        })
        .when(filterChain)
        .doFilter(anyObject(), anyObject());

        classUnderTest.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }
}
