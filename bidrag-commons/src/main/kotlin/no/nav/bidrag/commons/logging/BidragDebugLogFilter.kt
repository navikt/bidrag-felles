package no.nav.bidrag.commons.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import no.nav.bidrag.commons.unleash.FellesUnleashFeatures

class BidragDebugLogFilter : Filter<ILoggingEvent>() {
    override fun decide(event: ILoggingEvent): FilterReply =
        if (event.level == Level.DEBUG) {
            if (FellesUnleashFeatures.DEBUG_LOGGING.isEnabled) {
                FilterReply.NEUTRAL
            } else {
                FilterReply.DENY
            }
        } else {
            FilterReply.NEUTRAL
        }
}
