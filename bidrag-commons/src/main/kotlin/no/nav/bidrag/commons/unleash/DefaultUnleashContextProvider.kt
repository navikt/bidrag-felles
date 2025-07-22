package no.nav.bidrag.commons.unleash

import io.getunleash.UnleashContext
import io.getunleash.UnleashContextProvider
import no.nav.bidrag.commons.web.MdcConstants
import org.slf4j.MDC

class DefaultUnleashContextProvider : UnleashContextProvider {
    override fun getContext(): UnleashContext {
        val userId = MDC.get("user")
        val context =
            UnleashContext
                .builder()
                .userId(userId)
                .appName(MDC.get("applicationKey"))

        MDC.get(MdcConstants.MDC_ENHET)?.let {
            context.addProperty("enhet", it)
        }
        MDC.get(MdcConstants.MDC_SAKSNUMMER)?.let {
            context.addProperty("saksnummer", it)
        }
        return context.build()
    }
}
