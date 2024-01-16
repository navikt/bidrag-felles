package no.nav.bidrag.commons.service

import no.nav.bidrag.commons.util.LoggingRetryListener
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

fun retryTemplate(details: String? = null): RetryTemplate {
    val retryTemplate = RetryTemplate()
    val fixedBackOffPolicy = FixedBackOffPolicy()
    fixedBackOffPolicy.backOffPeriod = 500L
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy)
    val retryPolicy = SimpleRetryPolicy()
    retryPolicy.maxAttempts = 3
    retryTemplate.setRetryPolicy(retryPolicy)
    retryTemplate.setThrowLastExceptionOnExhausted(true)
    retryTemplate.registerListener(LoggingRetryListener(details))
    return retryTemplate
}
