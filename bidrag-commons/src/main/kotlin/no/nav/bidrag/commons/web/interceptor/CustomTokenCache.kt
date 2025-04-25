package no.nav.bidrag.commons.web.interceptor

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Expiry
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.oauth2.AbstractOAuth2GrantRequest
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse
import java.util.concurrent.TimeUnit.SECONDS

fun ClientProperties.customHashCode(): Int {
    val authHash =
        this.authentication.clientAuthMethod.value
            .hashCode() +
            this.authentication.clientId.hashCode() +
            (this.authentication.clientSecret?.hashCode() ?: -1) + (this.authentication.clientJwk?.hashCode() ?: -1) +
            (this.authentication.clientRsaKey?.hashCode() ?: -1)
    val tokenXChangeCache = (tokenExchange?.audience?.hashCode() ?: -1) + (tokenExchange?.resource?.hashCode() ?: -1)
    return this.tokenEndpointUrl.hashCode() +
        this.resourceUrl.hashCode() +
        this.grantType.hashCode() +
        this.scope.hashCode() + authHash + tokenXChangeCache
}

class CustomEqualityCache<K : Any, V>(
    private val cache: Cache<K, V>,
    private val equalityChecker: (K, K) -> Boolean,
) : Cache<K, V> by cache {
    override fun getIfPresent(key: K): V? =
        cache
            .asMap()
            .entries
            .firstOrNull { equalityChecker(it.key, key) }
            ?.value

    override fun get(
        key: K,
        mappingFunction: java.util.function.Function<in K, out V>,
    ): V = getIfPresent(key) ?: cache.get(key, mappingFunction)
}

object CustomOAuth2CacheFactory {
    @JvmStatic
    fun <T : Any> accessTokenResponseCache(
        maximumSize: Long,
        skewInSeconds: Long,
    ): Cache<T, OAuth2AccessTokenResponse> {
        val caffeineCache =
            Caffeine
                .newBuilder()
                .maximumSize(maximumSize)
                .expireAfter(evictOnResponseExpiresIn(skewInSeconds))
                .build<T, OAuth2AccessTokenResponse>()
        return CustomEqualityCache(caffeineCache, { key1, key2 ->
            if (key1 is AbstractOAuth2GrantRequest && key2 is AbstractOAuth2GrantRequest) {
                key1.grantType == key2.grantType && key1.clientProperties.customHashCode() == key2.clientProperties.customHashCode()
            } else {
                key1.hashCode() == key2.hashCode()
            }
        })
    }

    private fun <T : Any> evictOnResponseExpiresIn(skewInSeconds: Long): Expiry<T, OAuth2AccessTokenResponse> =
        object : Expiry<T, OAuth2AccessTokenResponse> {
            override fun expireAfterCreate(
                key: T,
                response: OAuth2AccessTokenResponse,
                currentTime: Long,
            ) = SECONDS.toNanos(
                if (response.expires_in!! >
                    skewInSeconds
                ) {
                    response.expires_in!! - skewInSeconds
                } else {
                    response.expires_in!!.toLong()
                },
            )

            override fun expireAfterUpdate(
                key: T,
                response: OAuth2AccessTokenResponse,
                currentTime: Long,
                currentDuration: Long,
            ) = currentDuration

            override fun expireAfterRead(
                key: T,
                response: OAuth2AccessTokenResponse,
                currentTime: Long,
                currentDuration: Long,
            ) = currentDuration
        }
}
