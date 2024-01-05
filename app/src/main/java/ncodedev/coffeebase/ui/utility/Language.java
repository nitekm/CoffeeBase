package ncodedev.coffeebase.ui.utility;

import java.util.stream.Stream;

import static ncodedev.coffeebase.ui.utility.LanguageCode.ENGLISH;

public enum Language {
    EN(ENGLISH),
    ES("\uD83C\uDDEA\uD83C\uDDF8 Español"),
    DE("\uD83C\uDDE9\uD83C\uDDEA Deutsch"),
    FR("\uD83C\uDDEB\uD83C\uDDF7 Français"),
    PL("\uD83C\uDDF5\uD83C\uDDF1 Polski"),
    PT("\uD83C\uDDF5\uD83C\uDDF9 Português");

    private final String value;
    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Stream<Language> stream() {
        return Stream.of(Language.values());
    }
}
