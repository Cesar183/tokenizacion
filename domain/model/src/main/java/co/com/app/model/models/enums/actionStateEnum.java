package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum actionStateEnum {

    APROBADA("EXT", "APPROVED", "Exitosa"),
    DECLINADA("DEC", "DECLINED", "Declinada"),
    DENEGADA("DEC", "DENIED", "Denegada"),
    EXITOSA("NCO", "SUCCESSFUL", "Exitosa"),
    REQUIERE_AUTENTICACION_ADICIONAL("EXT", "REQUIRE_ADDITIONAL_AUTHENTICATION", "Requiere Autenticación Adicional"),
    REQUIERE_AUTENTICACION_ADICIONAL_NO_CONFIRMADA("NCO", "REQUIRE_ADDITIONAL_AUTHENTICATION_NCO", "Requiere Autenticación Adicional No Confirmada");


    private final String code;
    private final String description;
    private final String meaning;

    public static String meaning(String abbreviation) {
        return Arrays.stream(actionStateEnum.values())
                .filter(d -> d.code.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.meaning).orElse(null);
    }

    public static String description(String abbreviation) {
        return Arrays.stream(actionStateEnum.values())
                .filter(d -> d.code.equalsIgnoreCase(abbreviation))
                .findFirst()
                .map(d -> d.description).orElse(null);
    }

    public static String code(String description) {
        return Arrays.stream(actionStateEnum.values())
                .filter(d -> d.description.contains(description))
                .findFirst()
                .map(d -> d.code).orElse(null);
    }

    public static actionStateEnum fromValue(String value){
        for(actionStateEnum stateEnum: actionStateEnum.values()){
            if(stateEnum.code.equalsIgnoreCase(value)){
                return stateEnum;
            }
        }
        throw new IllegalArgumentException("El valor del parametro "+ value +" no hace parte de los valores válidos");
    }
}
