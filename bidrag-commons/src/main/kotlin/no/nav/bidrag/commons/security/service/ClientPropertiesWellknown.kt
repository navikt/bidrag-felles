package no.nav.bidrag.commons.security.service

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI

@Validated
@ConfigurationProperties("no.nav.security.jwt.client")
data class ClientConfigurationWellknownProperties(
    val registration:
        @NotEmpty @Valid
        Map<String, ClientPropertiesWellknown>,
)

class ClientPropertiesWellknown(
    val wellKnownUrl: URI? = null,
)
