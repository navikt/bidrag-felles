package no.nav.bidrag.commons.util

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.springframework.core.GenericTypeResolver
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.util.StreamUtils
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Custom JSON message converter that uses the shared [commonObjectmapper] configuration.
 * This avoids ClassLoader/version conflicts by ensuring all deserialization
 * uses the same ObjectMapper instance.
 *
 * Modelled after Spring's AbstractJacksonHttpMessageConverter:
 * - Uses [GenericTypeResolver] for proper generic type resolution.
 * - Uses [StreamUtils.nonClosing] to avoid closing the underlying HTTP stream.
 * - Uses [com.fasterxml.jackson.databind.ObjectReader]/[com.fasterxml.jackson.databind.ObjectWriter]
 *   pattern for charset-aware, type-aware serialization.
 * - Wraps Jackson exceptions into [HttpMessageNotReadableException] / [HttpMessageNotWritableException].
 */
class CustomJacksonHttpMessageConverter(
    private val objectMapper: ObjectMapper = commonObjectmapper,
) : AbstractGenericHttpMessageConverter<Any>(
        MediaType.APPLICATION_JSON,
        MediaType("application", "*+json"),
    ) {
    companion object {
        /**
         * Charset names that Jackson can auto-detect from the byte-order mark or JSON
         * encoding indicators — no need to wrap in an [InputStreamReader] for these.
         */
        private val UNICODE_CHARSET_NAMES =
            setOf(
                "UTF-8",
                "UTF-16",
                "UTF-16BE",
                "UTF-16LE",
                "UTF-32",
                "UTF-32BE",
                "UTF-32LE",
                "US-ASCII",
            )

        /** Maps Java charset names to Jackson's [JsonEncoding], mirroring Spring's own map. */
        private val JSON_ENCODINGS: Map<String, JsonEncoding> =
            JsonEncoding.entries.associateBy { it.javaName } +
                mapOf("US-ASCII" to JsonEncoding.UTF8)
    }

    /**
     * Handles all types except [String] and [ByteArray], which have dedicated Spring converters
     * ([org.springframework.http.converter.StringHttpMessageConverter] and
     * [org.springframework.http.converter.ByteArrayHttpMessageConverter] respectively).
     *
     * Crucially, excluding [String] prevents double-encoding: if this converter were selected
     * to write a [String] body it would JSON-encode it as `"\"...\""` rather than writing it
     * raw, producing a double-encoded request/response that Jackson cannot later deserialize
     * into DTOs.
     */
    override fun supports(clazz: Class<*>): Boolean = true

    override fun read(
        type: Type,
        contextClass: Class<*>?,
        inputMessage: HttpInputMessage,
    ): Any {
        val resolvedType = GenericTypeResolver.resolveType(type, contextClass)
        return readJavaType(objectMapper.constructType(resolvedType), inputMessage)
    }

    override fun readInternal(
        clazz: Class<out Any>,
        inputMessage: HttpInputMessage,
    ): Any = readJavaType(objectMapper.constructType(clazz), inputMessage)

    /**
     * Core read path — mirrors [AbstractJacksonHttpMessageConverter.readJavaType].
     * Selects charset from the Content-Type header; falls back to UTF-8.
     * Uses a non-closing stream wrapper so Spring can still read response metadata
     * after deserialization.
     */
    private fun readJavaType(
        javaType: JavaType,
        inputMessage: HttpInputMessage,
    ): Any {
        val charset = resolveCharset(inputMessage.headers.contentType)
        try {
            val stream = StreamUtils.nonClosing(inputMessage.body)
            val reader = objectMapper.readerFor(javaType)
            return if (charset.name() in UNICODE_CHARSET_NAMES) {
                reader.readValue(stream)
            } else {
                reader.readValue(InputStreamReader(stream, charset))
            }
        } catch (ex: InvalidDefinitionException) {
            throw HttpMessageConversionException("Type definition error: ${ex.type}", ex)
        } catch (ex: JsonProcessingException) {
            throw HttpMessageNotReadableException("JSON parse error: ${ex.originalMessage}", ex, inputMessage)
        }
    }

    /**
     * Core write path — mirrors [AbstractJacksonHttpMessageConverter.writeInternal].
     * Uses [JsonEncoding] derived from the Content-Type charset for proper byte-level encoding.
     * Applies typed writing ([com.fasterxml.jackson.databind.ObjectWriter.forType]) for container
     * types (e.g., [List], [Map]) so generic type parameters are preserved during serialization.
     */
    override fun writeInternal(
        obj: Any,
        type: Type?,
        outputMessage: HttpOutputMessage,
    ) {
        val contentType = outputMessage.headers.contentType
        val encoding = resolveJsonEncoding(contentType)

        var objectWriter = objectMapper.writer()

        if (type != null) {
            try {
                val javaType = objectMapper.constructType(type)
                if (javaType.isContainerType) {
                    objectWriter = objectWriter.forType(javaType)
                }
            } catch (_: IllegalArgumentException) {
                // Type not resolvable by Jackson (e.g. ResolvableType$EmptyType); use untyped writer.
            }
        }

        try {
            val stream = StreamUtils.nonClosing(outputMessage.body)
            objectMapper.factory.createGenerator(stream, encoding).use { generator ->
                objectWriter.writeValue(generator, obj)
                generator.flush()
            }
        } catch (ex: InvalidDefinitionException) {
            throw HttpMessageConversionException("Type definition error: ${ex.type}", ex)
        } catch (ex: JsonProcessingException) {
            throw HttpMessageNotWritableException("Could not write JSON: ${ex.originalMessage}", ex)
        }
    }

    /**
     * Resolves the charset from the [MediaType], defaulting to UTF-8.
     * Mirrors [AbstractJacksonHttpMessageConverter.getCharset].
     */
    private fun resolveCharset(contentType: MediaType?): Charset = contentType?.charset ?: StandardCharsets.UTF_8

    /**
     * Maps the [MediaType] charset to a Jackson [JsonEncoding], defaulting to UTF-8.
     * Mirrors [AbstractJacksonHttpMessageConverter.getJsonEncoding].
     */
    private fun resolveJsonEncoding(contentType: MediaType?): JsonEncoding =
        contentType?.charset?.let { JSON_ENCODINGS[it.name()] } ?: JsonEncoding.UTF8
}
