package no.nav.bidrag.commons.unleash

import io.getunleash.UnleashContext
import io.getunleash.UnleashContextProvider
import org.slf4j.MDC

class DefaultUnleashContextProvider : UnleashContextProvider {
    override fun getContext(): UnleashContext {
        val userId = MDC.get("user")
        return UnleashContext
            .builder()
            .userId(userId)
            .appName(MDC.get("applicationKey"))
            .build()
    }
}
