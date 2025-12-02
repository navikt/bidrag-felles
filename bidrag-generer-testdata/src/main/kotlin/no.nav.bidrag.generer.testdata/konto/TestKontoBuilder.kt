package no.nav.bidrag.generer.testdata.konto

import no.nav.bidrag.domene.enums.diverse.LandkoderIso3
import no.nav.bidrag.domene.enums.samhandler.Valutakode
import java.util.UUID
import kotlin.random.Random

@Suppress("unused")
class TestKontoBuilder {
    private var bankNummer: Int? = null
    private var kontotype: Int? = null
    private var norskKontonummer: Boolean = true
    private var iban: String? = null
    private var swift: String? = null
    private var banknavn: String? = null
    private var landkodeBank: LandkoderIso3? = null
    private var bankCode: String? = null
    private var valutakode: Valutakode? = null

    fun bankNummer(bankNummer: Int): TestKontoBuilder {
        this.bankNummer = bankNummer
        return this
    }

    fun kontotype(kontotype: Int): TestKontoBuilder {
        this.kontotype = kontotype
        return this
    }

    fun norskKontonummer(norskKontonummer: Boolean): TestKontoBuilder {
        this.norskKontonummer = norskKontonummer
        return this
    }

    fun iban(iban: String?): TestKontoBuilder {
        this.iban = iban
        return this
    }

    fun swift(swift: String?): TestKontoBuilder {
        this.swift = swift
        return this
    }

    fun banknavn(banknavn: String?): TestKontoBuilder {
        this.banknavn = banknavn
        return this
    }

    fun landkodeBank(landkodeBank: LandkoderIso3?): TestKontoBuilder {
        this.landkodeBank = landkodeBank
        return this
    }

    fun bankCode(bankCode: String?): TestKontoBuilder {
        this.bankCode = bankCode
        return this
    }

    fun valutakode(valutakode: Valutakode?): TestKontoBuilder {
        this.valutakode = valutakode
        return this
    }

    fun opprett(): TestKonto =
        if (norskKontonummer) {
            TestKonto(
                norskKontonummer =
                    NorskKontonummerBuilder()
                        .bankNummer(bankNummer ?: Random.nextInt(10000))
                        .kontotype(kontotype ?: Random.nextInt(100))
                        .randomKundenummer()
                        .toString(),
            )
        } else {
            val faktiskLandkodeBank = landkodeBank ?: genererLandkodeIso3()
            TestKonto(
                iban = iban ?: UUID.randomUUID().toString(),
                swift = swift ?: UUID.randomUUID().toString(),
                bankNavn = banknavn ?: "BANK OF ${faktiskLandkodeBank.visningsnavn}",
                landkodeBank = landkodeBank ?: faktiskLandkodeBank,
                bankCode = bankCode ?: Random.nextInt().toString(),
                valutakode = valutakode ?: genererValutakode(),
            )
        }

    companion object {
        fun konto(): TestKontoBuilder = TestKontoBuilder()
    }
}

@Suppress("unused")
fun genererNorskKontonummer(
    bankNummer: Int? = null,
    kontotype: Int? = null,
): TestKonto {
    val builder = TestKontoBuilder.konto()
    bankNummer?.let { builder.bankNummer(it) }
    kontotype?.let { builder.kontotype(it) }
    return builder.opprett()
}

@Suppress("unused")
fun genererUtenlandsKontonummer(
    iban: String? = null,
    swift: String? = null,
    banknavn: String? = null,
    landkodeBank: LandkoderIso3? = null,
    bankCode: String? = null,
    valutakode: Valutakode? = null,
): TestKonto {
    val builder = TestKontoBuilder.konto()
    iban?.let { builder.iban(it) }
    swift?.let { builder.swift(it) }
    banknavn?.let { builder.banknavn(it) }
    landkodeBank?.let { builder.landkodeBank(it) }
    bankCode?.let { builder.bankCode(it) }
    valutakode?.let { builder.valutakode(it) }
    return builder.opprett()
}

fun genererLandkodeIso3(): LandkoderIso3 = LandkoderIso3.entries.random()

fun genererValutakode(): Valutakode = Valutakode.entries.random()
