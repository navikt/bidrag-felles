package no.nav.bidrag.commons.util

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import java.lang.reflect.Type

/**
 * Custom JSON message converter that uses the shared ObjectMapper configuration.
 * This avoids ClassLoader/version conflicts by ensuring all deserialization
 * uses the same ObjectMapper instance.
 */
class CustomJacksonHttpMessageConverter(
    private val objectMapper: ObjectMapper = commonObjectmapper,
) : AbstractGenericHttpMessageConverter<Any>(
        MediaType.APPLICATION_JSON,
        MediaType("application", "*+json"),
    ) {
    override fun supports(clazz: Class<*>): Boolean {
        // Don't handle primitive types, strings, or byte arrays
        return !clazz.isPrimitive &&
            clazz != Int::class.java &&
            clazz != String::class.java &&
            clazz != ByteArray::class.java
    }

    override fun read(
        type: Type,
        contextClass: Class<*>?,
        inputMessage: HttpInputMessage,
    ): Any = objectMapper.readValue(inputMessage.body, objectMapper.constructType(type))

    override fun readInternal(
        clazz: Class<out Any>,
        inputMessage: HttpInputMessage,
    ): Any = objectMapper.readValue(inputMessage.body, clazz)

    override fun writeInternal(
        obj: Any,
        type: Type?,
        outputMessage: HttpOutputMessage,
    ) {
        outputMessage.body.use { os ->
            if (type == null) {
                objectMapper.writeValue(os, obj)
                return
            }
            // Guard against Spring-internal types like ResolvableType$EmptyType that
            // Jackson's TypeFactory does not know how to handle.
            try {
                val jacksonType = objectMapper.constructType(type)
                objectMapper.writerFor(jacksonType).writeValue(os, obj)
            } catch (_: IllegalArgumentException) {
                // Type is not resolvable by Jackson (e.g. ResolvableType$EmptyType);
                // fall back to untyped serialisation which uses the runtime class.
                objectMapper.writeValue(os, obj)
            }
        }
    }
}
