package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum IdentificationTypeEnum {

    CARNET_DIPLOMATICO("CD", "TIPDOC_FS000","0"),
    CEDULA_DE_CIUDADANIA("CC", "TIPDOC_FS001", "1"),
    CEDULA_DE_EXTRANJERIA("CE", "TIPDOC_FS002", "2"),
    NIT("NIT", "TIPDOC_FS003", "3"),
    TARJETA_DE_IDENTIDAD("TI", "TIPDOC_FS004", "4"),
    PASAPORTE("PASS", "TIPDOC_FS005", "5"),
    ID_EXTRANJERO_PN_NO_RESIDENTE_EN_COLOMBIA("ID", "TIPDOC_FS006", "6"),
    ID_EXTRANJERO_PJ_NO_RESIDENTE_EN_COLOMBIA("IEJPNREC", "TIPDOC_FS007", "7"),
    FIDEICOMISO("FC", "TIPDOC_FS008","8"),
    REGISTRO_CIVIL("RC", "TIPDOC_FS009", "9");

    private final String abbreviation;
    private final String meaning;
    private final String code;

    public static String meaning(String abbreviation) {
        return Arrays.stream(IdentificationTypeEnum.values())
                .filter(d -> d.abbreviation.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.meaning).orElse(null);
    }

    public static String code(String meaning) {
        return Arrays.stream(IdentificationTypeEnum.values())
                .filter(d -> d.meaning.equalsIgnoreCase(meaning))
                .findFirst()
                .map(d -> d.code).orElseThrow(() -> new IllegalArgumentException("El valor del parametro "+ meaning +" no hace parte de los valores válidos"));
    }

    public static IdentificationTypeEnum fromValue(String value){
        for(IdentificationTypeEnum typeEnum: IdentificationTypeEnum.values()){
            if(typeEnum.meaning.equalsIgnoreCase(value)){
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("El valor del parametro "+ value +" no hace parte de los valores válidos");
    }

}
