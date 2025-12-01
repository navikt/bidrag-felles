package no.nav.bidrag.generer.testdata.samhandler

import no.nav.bidrag.domene.enums.diverse.Språk
import no.nav.bidrag.domene.enums.samhandler.OffentligIdType
import no.nav.bidrag.domene.enums.samhandler.Områdekode
import no.nav.bidrag.domene.ident.SamhandlerId
import no.nav.bidrag.generer.testdata.adresse.genererAdresseMedPoststed
import no.nav.bidrag.generer.testdata.konto.TestKonto
import no.nav.bidrag.generer.testdata.konto.genererNorskKontonummer
import no.nav.bidrag.generer.testdata.navn.genererEtternavn
import no.nav.bidrag.generer.testdata.navn.genererFornavn
import kotlin.random.Random

@Suppress("unused")
class TestSamhandlerBuilder {
    private var samhandlerId: SamhandlerId? = genererSamhandlerId()
    private var navn: String? = genererFornavn() + " " + genererEtternavn()
    private var offentligId: String? = null
    private var offentligIdType: OffentligIdType? = OffentligIdType.entries.random()
    private var områdekode: Områdekode? = Områdekode.entries.random()
    private var språk: String? = genererSpråk().name
    private var adresse: String? = genererAdresseMedPoststed()
    private var kontonummer: TestKonto? = genererNorskKontonummer()
    private var kontaktperson: String? = genererFornavn() + " " + genererEtternavn()
    private var kontaktEpost: String? = genererFornavn() + "@" + genererEtternavn() + ".no"
    private var kontaktTelefon: String? = genererTelefonnummer()
    private var notat: String? = null
    private var erOpphørt: Boolean? = false

    fun medSamhandlerId(samhandlerId: SamhandlerId): TestSamhandlerBuilder {
        this.samhandlerId = samhandlerId
        return this
    }

    fun medNavn(navn: String): TestSamhandlerBuilder {
        this.navn = navn
        return this
    }

    fun medOffentligId(offentligId: String?): TestSamhandlerBuilder {
        this.offentligId = offentligId
        return this
    }

    fun medOffentligIdType(offentligIdType: OffentligIdType?): TestSamhandlerBuilder {
        this.offentligIdType = offentligIdType
        return this
    }

    fun medOmrådekode(områdekode: Områdekode?): TestSamhandlerBuilder {
        this.områdekode = områdekode
        return this
    }

    fun medSpråk(språk: String?): TestSamhandlerBuilder {
        this.språk = språk
        return this
    }

    fun medAdresse(adresse: String?): TestSamhandlerBuilder {
        this.adresse = adresse
        return this
    }

    fun medKontonummer(kontonummer: TestKonto?): TestSamhandlerBuilder {
        this.kontonummer = kontonummer
        return this
    }

    fun medKontaktperson(kontaktperson: String?): TestSamhandlerBuilder {
        this.kontaktperson = kontaktperson
        return this
    }

    fun medKontaktEpost(kontaktEpost: String?): TestSamhandlerBuilder {
        this.kontaktEpost = kontaktEpost
        return this
    }

    fun medKontaktTelefon(kontaktTelefon: String?): TestSamhandlerBuilder {
        this.kontaktTelefon = kontaktTelefon
        return this
    }

    fun medNotat(notat: String?): TestSamhandlerBuilder {
        this.notat = notat
        return this
    }

    fun medErOpphørt(erOpphørt: Boolean?): TestSamhandlerBuilder {
        this.erOpphørt = erOpphørt
        return this
    }

    fun opprett(): TestSamhandler {

        return TestSamhandler(
            samhandlerId = samhandlerId,
            navn = navn,
            offentligId = offentligId,
            offentligIdType = offentligIdType,
            områdekode = områdekode,
            språk = språk,
            adresse = adresse,
            kontonummer = kontonummer,
            kontaktperson = kontaktperson,
            kontaktEpost = kontaktEpost,
            kontaktTelefon = kontaktTelefon,
            notat = notat,
            erOpphørt = erOpphørt
        )
    }

    companion object {
        private var samhandlerNr = 0

        fun samhandler(): TestSamhandlerBuilder = TestSamhandlerBuilder()
    }
}

fun genererSamhandler(): TestSamhandler = TestSamhandlerBuilder.samhandler().opprett()

fun genererSamhandlerId(): SamhandlerId {
    val randomSuffix = Random.nextInt(0, 10_000_000)
    return SamhandlerId("85${randomSuffix.toString().padStart(7, '0')}")
}

fun genererSpråk(): Språk = Språk.entries.random()

fun genererTelefonnummer(): String {
    return Random.nextInt(10_000_000, 100_000_000).toString()
}