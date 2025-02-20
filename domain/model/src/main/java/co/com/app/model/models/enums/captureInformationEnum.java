package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum captureInformationEnum {

    CAMERA("CAM","CAMERA"),
    MANUAL("MAN","MANUAL"),
    BANK_APP("BAP","BANK_APP"),
    ON_FILE("OFI","ON-FILE"),
    READER_MODE("RM","READER_MODE"),
    TOKEN("TOK","TOKEN"),
    CHIP_DIP("CHD","CHIP_DIP"),
    UNKNOWN("UNK","UNKNOWN");

    private final String abbreviation;
    private final String meaning;

    public static String meaning(String abbreviation) {
        return Arrays.stream(captureInformationEnum.values())
                .filter(d -> d.abbreviation.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.meaning).orElse("");
    }

    public static String abbreviation(String meaning) {
        return Arrays.stream(captureInformationEnum.values())
                .filter(d -> d.meaning.equalsIgnoreCase(meaning))
                .findFirst()
                .map(d -> d.abbreviation).orElse("");
    }
}
