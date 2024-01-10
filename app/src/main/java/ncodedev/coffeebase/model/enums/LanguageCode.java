package ncodedev.coffeebase.model.enums;

import java.util.stream.Stream;

public enum LanguageCode {
    EN("\uD83C\uDDEC\uD83C\uDDE7 English"),
    ES("\uD83C\uDDEA\uD83C\uDDF8 Español"),
    DE("\uD83C\uDDE9\uD83C\uDDEA Deutsch"),
    FR("\uD83C\uDDEB\uD83C\uDDF7 Français"),
    PL("\uD83C\uDDF5\uD83C\uDDF1 Polski"),
    PT("\uD83C\uDDF5\uD83C\uDDF9 Português");

    private final String value;
    LanguageCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Stream<LanguageCode> stream() {
        return Stream.of(LanguageCode.values());
    }
}
