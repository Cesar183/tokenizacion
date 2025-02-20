package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
@AllArgsConstructor
public enum tokenizationStateEnum {
    ACTIVO("ACT","ACTIVATE","Activo"),
    ELIMINADO("ELI","ERASE","Eliminado"),
    SUSPENSION("SUS","SUSPEND","Suspendido"),
    ACTUALIZACION("ATL","UPDATE","Actualizado"),
    RENOVACION("REN","RENEW","Renovado"),
    BORRADO("BOR","DELETE","Borrado"),
    DISPOSITIVO_VINCULADO("DVI","DEVICE_BOUND", "Dispositivo Vinculado"),
    TITULAR_TARJETA("COT","CARDHOLDER_STEPUP_OTP","TITULAR DE LA TARJETA PASO ADELANTE OTP"),
    DESUSPENSION("RES","RESUME","Desuspensión"),
    DISPOSITIVO_DESVINCULADO("DUN","DEVICE_UNBOUND","Dispositivo No Vinculado"),
    CARDHOLDER_SETUP_ISSUER_APP("CAP","CARDHOLDER_STEPUP_ISSUER_APP","Pendiente autenticación step-up a través de la aplicación del emisor"),
    CARDHOLDER_SETUP_CALL_CENTER("CCE","CARDHOLDER_STEPUP_CALL_CENTER","Pendiente autenticación step-up llamando al centro de llamadas"),
    CARDHOLDER_SETUP_APPROVED("CAR","CARDHOLDER_STEPUP_APPROVED","Paso autenticación step-up");


    private final String code;
    private final String description;
    private final String meaning;

    public static String description(String code) {
        return Arrays.stream(tokenizationStateEnum.values())
                .filter(d -> d.code.equalsIgnoreCase(code))
                .findFirst()
                .map(d -> d.description).orElse(null);
    }

    public static String code(String description) {
        return Arrays.stream(tokenizationStateEnum.values())
                .filter(d -> d.description.equalsIgnoreCase(description))
                .findFirst()
                .map(d -> d.code).orElse(null);
    }
    public static tokenizationStateEnum fromValue(String value){
        for(tokenizationStateEnum tokenizationStateEnum : tokenizationStateEnum.values()){
            if(tokenizationStateEnum.code.equalsIgnoreCase(value)){
                return tokenizationStateEnum;
            }
        }
        throw new IllegalArgumentException("El valor del parametro "+ value +" no hace parte de los valores válidos");
    }

}
