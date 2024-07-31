package no.nav.bidrag.transport.felles

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper

val commonObjectmapper =
    JsonMapper
        .builder()
        .findAndAddModules()
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()

// Bisys bruker eldre versjon av jackson og støtter derfor ikke JsonMapper
val commonObjectmapperLegacy =
    ObjectMapper()
        .findAndRegisterModules()
        .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
