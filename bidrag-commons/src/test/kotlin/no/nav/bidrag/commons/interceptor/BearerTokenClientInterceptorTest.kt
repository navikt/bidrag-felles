package no.nav.bidrag.commons.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.bidrag.commons.web.interceptor.BearerTokenClientInterceptor
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.OAuth2GrantType
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.jwt.JwtTokenClaims
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.net.URI

class BearerTokenClientInterceptorTest {
    private lateinit var bearerTokenClientInterceptor: BearerTokenClientInterceptor

    private val oAuth2AccessTokenService = mockk<OAuth2AccessTokenService>(relaxed = true)

    @BeforeEach
    fun setup() {
        bearerTokenClientInterceptor =
            BearerTokenClientInterceptor(
                oAuth2AccessTokenService,
                clientConfigurationProperties,
            )
    }

    @AfterEach
    internal fun tearDown() {
        clearBrukerContext()
    }

    @Test
    fun `intercept bruker grant type client credentials når det ikke er noen request context`() {
        val req = mockk<HttpRequest>(relaxed = true, relaxUnitFun = true)
        every { req.uri } returns URI("http://firstResource.no")
        val execution = mockk<ClientHttpRequestExecution>(relaxed = true)

        bearerTokenClientInterceptor.intercept(req, ByteArray(0), execution)

        verify {
            oAuth2AccessTokenService.getAccessToken(clientConfigurationProperties.registration["1"])
        }
    }

    @Test
    fun `intercept bruker grant type jwt token når det finnes saksbehandler context`() {
        mockBrukerContext("saksbehandler")

        val req = mockk<HttpRequest>(relaxed = true, relaxUnitFun = true)
        every { req.uri } returns URI("http://firstResource.no")
        val execution = mockk<ClientHttpRequestExecution>(relaxed = true)

        bearerTokenClientInterceptor.intercept(req, ByteArray(0), execution)

        verify {
            oAuth2AccessTokenService.getAccessToken(
                ClientProperties(
                    clientConfigurationProperties.registration["1"]?.tokenEndpointUrl,
                    clientConfigurationProperties.registration["1"]?.wellKnownUrl,
                    OAuth2GrantType.JWT_BEARER,
                    clientConfigurationProperties.registration["1"]?.scope,
                    clientConfigurationProperties.registration["1"]?.authentication,
                    clientConfigurationProperties.registration["1"]?.resourceUrl,
                    clientConfigurationProperties.registration["1"]?.tokenExchange,
                ),
            )
        }
    }

    fun mockBrukerContext(preferredUsername: String) {
        val tokenValidationContext = mockk<TokenValidationContext>()
        val jwtTokenClaims = mockk<JwtTokenClaims>()
        val requestAttributes = mockk<RequestAttributes>()
        RequestContextHolder.setRequestAttributes(requestAttributes)
        every {
            requestAttributes.getAttribute(
                SpringTokenValidationContextHolder::class.java.name,
                RequestAttributes.SCOPE_REQUEST,
            )
        } returns tokenValidationContext
        every { tokenValidationContext.getClaims("aad") } returns jwtTokenClaims
        every { jwtTokenClaims.get("preferred_username") } returns preferredUsername
        every { jwtTokenClaims.get("NAVident") } returns preferredUsername
    }

    fun clearBrukerContext() {
        RequestContextHolder.resetRequestAttributes()
    }
}
