package no.nav.bidrag.commons.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import no.nav.bidrag.commons.CorrelationId
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Component
class CorrelationIdFilter : Filter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val httpServletResponse = servletResponse as HttpServletResponse
        val method = httpServletRequest.method
        val requestURI: String = httpServletRequest.requestURI
        if (isNotRequestContaining(requestURI, "/actuator/", "/api-docs/", "/swagger-")) {
            val correlationId: CorrelationId =
                if (httpServletRequest.getHeader(CORRELATION_ID_HEADER) != null) {
                    CorrelationId.existing(httpServletRequest.getHeader(CORRELATION_ID_HEADER))
                } else {
                    generateCorreleationIdToHttpHeaderOnResponse(
                        httpServletResponse,
                        CorrelationId.generateTimestamped(fetchLastPartOfRequestUri(requestURI)),
                    )
                }
            MDC.put(CORRELATION_ID_MDC, correlationId.get())
            logger.debug("{} is prosessing {} {}", CorrelationIdFilter::class.java.simpleName, method, requestURI)
        }
        filterChain.doFilter(servletRequest, servletResponse)
        MDC.clear()
    }

    private fun isNotRequestContaining(
        requestURI: String,
        vararg uriParts: String,
    ): Boolean = uriParts.none { requestURI.contains(it) }

    private fun generateCorreleationIdToHttpHeaderOnResponse(
        httpServletResponse: HttpServletResponse,
        correlationId: CorrelationId,
    ): CorrelationId {
        httpServletResponse.addHeader(CORRELATION_ID_HEADER, correlationId.get())
        return correlationId
    }

    private fun fetchLastPartOfRequestUri(requestUri: String): String =
        if (requestUri.contains("/")) {
            fetchLastPartOfRequestUriContainingPlainText(requestUri)
        } else {
            requestUri
        }

    private fun fetchLastPartOfRequestUriContainingPlainText(requestUri: String): String {
        val reversedUriParts = reverseUriPartsBySlash(requestUri)
        val lastUriPsty = if (reversedUriParts.isEmpty()) "" else reversedUriParts[0]
        return if (lastUriPsty.matches(Regex("^[a-zA-Z]+$")) || lastUriPsty.isBlank()) {
            lastUriPsty
        } else {
            reversedUriParts[1] + '/' + lastUriPsty
        }
    }

    private fun reverseUriPartsBySlash(requestUri: String): List<String> =
        requestUri
            .split("/".toRegex())
            .dropLastWhile {
                it.isEmpty()
            }.reversed()

    companion object {
        @JvmStatic
        fun fetchCorrelationIdForThread(): String = CorrelationId.fetchCorrelationIdForThread()

        const val CORRELATION_ID_MDC = "correlationId"
        const val CORRELATION_ID_HEADER: String = CorrelationId.CORRELATION_ID_HEADER
    }
}
