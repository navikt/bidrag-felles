package no.nav.bidrag.commons.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC

class EnhetFilter : Filter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        if (servletRequest is HttpServletRequest) {
            val requestURI = servletRequest.requestURI
            if (isNotRequestToActuatorEndpoint(requestURI)) {
                val enhetsnummer = servletRequest.getHeader(X_ENHET_HEADER)
                if (enhetsnummer != null) {
                    ENHETSNUMMER_VALUE.set(enhetsnummer)
                    MDC.put(ENHET_MDC, enhetsnummer)
                    (servletResponse as HttpServletResponse).addHeader(X_ENHET_HEADER, enhetsnummer)
                    logger.info("Behandler request '{}' for enhet med enhetsnummer {}", requestURI, enhetsnummer)
                } else {
                    ENHETSNUMMER_VALUE.set(null)
                    logger.info("Behandler request '{}' uten informasjon om enhetsnummer.", requestURI)
                }
            }
        } else {
            val filterRequest = servletRequest.javaClass.simpleName
            logger.error("Filtrering gjøres ikke av en HttpServletRequest: $filterRequest")
        }
        filterChain.doFilter(servletRequest, servletResponse)
        MDC.clear()
    }

    private fun isNotRequestToActuatorEndpoint(requestURI: String?): Boolean {
        checkNotNull(requestURI) { "should only use this class in an web environment which receives requestUri!!!" }
        return !requestURI.contains("/actuator/")
    }

    companion object {
        private val ENHETSNUMMER_VALUE = ThreadLocal<String>()
        private const val ENHET_MDC = "enhet"
        const val X_ENHET_HEADER = "X-Enhet"

        @JvmStatic
        fun fetchForThread(): String = ENHETSNUMMER_VALUE.get() ?: ""
    }
}
