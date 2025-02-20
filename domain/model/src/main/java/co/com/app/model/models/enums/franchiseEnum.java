package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum franchiseEnum {

    VISA("VSA","VISA", "4"),
    AMEX("AMX","AMEX", "3"),
    MASTERCARD("MCA","MASTERCARD", "5");

    private final String code;
    private final String meaning;
    private final String digit;

    public static boolean validate(String value){
        for(franchiseEnum franchiseEnum: franchiseEnum.values()){
            if(franchiseEnum.code.equals(value)){
                return true;
            }
        }
        return false;
    }

    public static String code(String digit) {
        return Arrays.stream(franchiseEnum.values())
                .filter(d -> d.digit.equals(digit))
                .findFirst()
                .map(d -> d.code).orElse(null);
    }

    public static String digit(String code) {
        return Arrays.stream(franchiseEnum.values())
                .filter(d -> d.code.equalsIgnoreCase(code))
                .findFirst()
                .map(d -> d.digit).orElse(null);
    }

    public static franchiseEnum fromValue(String code) {
        return Arrays.stream(franchiseEnum.values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid franchise code: " + code));
    }
}
