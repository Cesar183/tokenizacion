package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum customerCardEnum {

    DEBITO("DEB","DEBITO", "DB"),
    CREDITO("CRE","CRÉDITO", "CR");

    private final String abbreviation;
    private final String meaning;
    private final String code;

    public static String meaning(String abbreviation) {
        return Arrays.stream(customerCardEnum.values())
                .filter(d -> d.abbreviation.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.meaning).orElse("");
    }

    public static String code(String abbreviation) {
        return Arrays.stream(customerCardEnum.values())
                .filter(d -> d.abbreviation.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.code).orElse("");
    }

    public static String abbreviation(String code) {
        return Arrays.stream(customerCardEnum.values())
                .filter(d -> d.code.equalsIgnoreCase(code))
                .findFirst()
                .map(d -> d.abbreviation).orElse("");
    }

    public static customerCardEnum fromValue(String value){
        for(customerCardEnum customerCardEnum: customerCardEnum.values()){
            if(customerCardEnum.abbreviation.equalsIgnoreCase(value)){
                return customerCardEnum;
            }
        }
        throw new IllegalArgumentException("El valor del parametro "+ value +" no hace parte de los valores válidos");
    }
}
