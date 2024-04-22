package no.nav.bidrag.transport.samhandler

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.ident.SamhandlerId

data class SamhandlerDto(
    @Deprecated(message = "Replaced by samhandlerId", level = DeprecationLevel.ERROR)
    val tssId: SamhandlerId?,
    @Schema(description = "Identen til samhandler")
    val samhandlerId: SamhandlerId?,
    @Schema(description = "Navn på samhandler")
    val navn: String?,
    @Schema(description = "Offentlig id for samhandlere.")
    val offentligId: String? = null,
    @Schema(
        description =
            "Type offentlig id. " +
                "F.eks ORG for norske organisasjonsnummere, " +
                "UTOR for utenlandske organisasjonsnummere, " +
                "FNR for norske personnummer.",
    )
    val offentligIdType: String? = null,
    @Schema(description = "Definerer hvilket område samhandleren er knyttet til.")
    val områdekode: Områdekode? = null,
    @Schema(description = "Samhandlerens adresse.")
    val adresse: AdresseDto? = null,
    @Schema(description = "Samhandlerens kontonummer. Kontonummer er ikke inkludert i søkeresultat mot TSS, kun i oppslag.")
    val kontonummer: KontonummerDto? = null,
    @Schema(description = "Kontaktperson for samhandler. Benyttes primært av utland.")
    val kontaktperson: String? = null,
    @Schema(description = "Kontakt epost for samhandler. Benyttes primært av utland.")
    val kontaktEpost: String? = null,
    @Schema(description = "Kontakt telefon for samhandler. Benyttes primært av utland.")
    val kontaktTelefon: String? = null,
    @Schema(description = "Fritekstfelt for notater.")
    val notat: String? = null,
)
