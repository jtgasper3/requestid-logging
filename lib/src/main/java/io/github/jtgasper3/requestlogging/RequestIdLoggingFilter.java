package io.github.jtgasper3.requestlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter that makes the X-Request-Id header available in the MDC for logging.
 * If not value is passed in the request, then the X-Request-Id value is generated.
 */
@WebFilter(filterName = "requestIdLoggingFilter", urlPatterns = "/*")
public class RequestIdLoggingFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(RequestIdLoggingFilter.class);
    private final static String REQUEST_ID_HEADER_NAME = "RequestIdHeaderName";
    private final static String REQUEST_ID_MDC_NAME = "RequestIdMdcName";

    private String requestIdHeaderName = "X-Request-Id";
    private String requestIdMdcName = "X-Request-Id";

    /**
     * Populates the X-Request-Id MDC value with a value, from the header or new
     *
     * @param servletRequest {@inheritDoc}
     * @param servletResponse {@inheritDoc}
     * @param filterChain {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                String requestId = ((HttpServletRequest) servletRequest).getHeader(requestIdHeaderName);
                if (requestId == null) {
                    requestId = generateUniqueId();
                }
                MDC.put(requestIdMdcName, requestId);
            }
            
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(requestIdMdcName);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param filterConfig {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing filter: {}", this);

        String configRequestIdHeaderName = filterConfig.getInitParameter(REQUEST_ID_HEADER_NAME);
        if (configRequestIdHeaderName != null) {
            this.requestIdHeaderName = configRequestIdHeaderName;
        }

        String configRequestIdMdcName = filterConfig.getInitParameter(REQUEST_ID_MDC_NAME);
        if (configRequestIdMdcName != null) {
            this.requestIdMdcName = configRequestIdMdcName;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        LOG.info("Destroying filter: {}", this);
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}