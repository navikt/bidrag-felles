package no.nav.bidrag.commons.testdatagenerering.person;

public interface Relasjon {
    public static final Relasjon SAMME_ETTERNAVN = (person, builder) -> builder.etternavn(person.getEtternavn());
    public static final Relasjon SAMME_ADRESSE = (person, builder) -> {
        builder.boadresse(person.getBoadresse());
        builder.postadresse(person.getPostadresse());
    };

    void relater(TestPerson testPerson, TestPersonBuilder tilPerson);

    public static void relater(TestPerson testPerson, TestPersonBuilder tilPerson, Relasjon... relasjoner) {
        if (relasjoner != null) {
            for (Relasjon relasjon : relasjoner) {
                relasjon.relater(testPerson, tilPerson);
            }
        }
    }
}