package no.nav.bidrag.commons.unleash

import io.getunleash.variant.Variant

/**
 * Eksempel for hvordan UnleasFeatures kan defineres.
 *
 * Dette kan da kalles på følgende måte:
 * ```kotlin
 *   UnleashFeatures.STENG_VEDTAK.isEnabled
 * ```
 */
enum class ExampleUnleashFeatures(
    private val featureName: String,
    defaultValue: Boolean,
) {
    STENG_VEDTAK("vedtakssperre", false),
    ;

    private var defaultValue = false

    init {
        this.defaultValue = defaultValue
    }

    val isEnabled: Boolean
        get() = UnleashFeaturesProvider.isEnabled(featureName, defaultValue)

    val variant: Variant?
        get() = UnleashFeaturesProvider.getVariant(featureName)
}
