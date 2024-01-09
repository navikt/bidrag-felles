package no.nav.bidrag.domene.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import no.nav.bidrag.domene.enums.beregning.ResultatkodeBarnebidrag
import no.nav.bidrag.domene.enums.beregning.ResultatkodeForskudd
import no.nav.bidrag.domene.enums.beregning.ResultatkodeSærtilskudd
import no.nav.bidrag.domene.enums.diverse.Språk
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Sivilstandskode
import java.net.URL

typealias VisningsnavnKodeMap = Map<String, Visningsnavn>
internal val objectmapper = ObjectMapper(YAMLFactory()).findAndRegisterModules().registerKotlinModule()
private val visningsnavnCache: Map<String, VisningsnavnKodeMap> = emptyMap()
fun visningsnavnMangler(kode: String): Nothing =
    throw RuntimeException("Fant ingen visningsnavn for kode $kode")
data class Visningsnavn(
    val intern: String,
    val bruker: Map<Språk, String> = mapOf(Språk.NB to intern),
)

val Inntektsrapportering.Companion.visningsnavnSomKreverÅrstall get() = listOf(Inntektsrapportering.AINNTEKT, Inntektsrapportering.KAPITALINNTEKT, Inntektsrapportering.LIGNINGSINNTEKT)
val Inntektsrapportering.visningsnavn get() = lastVisningsnavnFraFil("inntekttype.yaml")[name] ?: visningsnavnMangler(name)
fun Inntektsrapportering.visningsnavnIntern(årstall: Int?) = "${visningsnavn.intern} $årstall".trim()
val Sivilstandskode.visningsnavn get() = lastVisningsnavnFraFil("sivilstand.yaml")[name] ?: visningsnavnMangler(name)
val Bostatuskode.visningsnavn get() = lastVisningsnavnFraFil("bostatus.yaml")[name] ?: visningsnavnMangler(name)
val ResultatkodeForskudd.visningsnavn get() = lastVisningsnavnFraFil("resultat.yaml", "FORSKUDD")[name] ?: visningsnavnMangler(name)
val ResultatkodeBarnebidrag.visningsnavn get() = lastVisningsnavnFraFil("resultat.yaml", "BARNEBIDRAG")[name] ?: visningsnavnMangler(name)
val ResultatkodeSærtilskudd.visningsnavn get() = lastVisningsnavnFraFil("resultat.yaml", "SÆRTILSKUDD")[name] ?: visningsnavnMangler(name)

private fun lastVisningsnavnFraFil(filnavn: String, category: String? = null): VisningsnavnKodeMap {
    val fil = hentFil("/kodeverk/visningsnavn/$filnavn")
    return visningsnavnCache.getOrDefault(filnavn, if (category != null) lastVisningsnavnFraFilForKategory(fil, category) else objectmapper.readValue(fil))
}

private fun lastVisningsnavnFraFilForKategory(fil: URL, kategory: String): VisningsnavnKodeMap {
    val jsonNode: Map<String, VisningsnavnKodeMap> = objectmapper.readValue(fil)
    return jsonNode[kategory] ?: throw RuntimeException("Fant ikke visningsnavn for kategory $kategory i filsti $fil")
}

private fun hentFil(filsti: String) = Visningsnavn::class.java.getResource(filsti) ?: throw RuntimeException("Fant ingen fil på sti $filsti")
