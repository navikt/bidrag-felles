package no.nav.bidrag.transport.felles

import com.fasterxml.jackson.databind.json.JsonMapper

internal val objectmapper =
    JsonMapper.builder()
        .findAndAddModules()
        .build()
