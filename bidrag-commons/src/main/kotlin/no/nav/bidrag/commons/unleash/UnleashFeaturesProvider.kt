package no.nav.bidrag.commons.unleash

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.util.UnleashConfig
import io.getunleash.variant.Variant
import java.util.UUID

class UnleashFeaturesProvider(
    unleashProperties: UnleashProperties,
) {
    init {
        val config =
            UnleashConfig
                .builder()
                .appName(unleashProperties.appName)
                .environment(unleashProperties.environment)
                .instanceId(UUID.randomUUID().toString())
                .unleashAPI("${unleashProperties.unleashApi}/api/")
                .apiKey(unleashProperties.apiKey)
                .synchronousFetchOnInitialisation(true)
                .unleashContextProvider(DefaultUnleashContextProvider())
                .build()

        instance = DefaultUnleash(config)
    }

    companion object {
        private var instance: Unleash? = null

        private fun getInstance(): Unleash {
            checkNotNull(instance) { "Unleash instance not initialized. Application context may not be loaded yet." }
            return instance!!
        }

        fun isEnabled(feature: String): Boolean = isEnabled(feature, false)

        fun getVariant(feature: String): Variant? =
            try {
                getInstance().getVariant(feature)
            } catch (e: Exception) {
                null
            }

        fun isEnabled(
            feature: String,
            defaultValue: Boolean,
        ): Boolean =
            try {
                getInstance().isEnabled(feature, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
    }
}
