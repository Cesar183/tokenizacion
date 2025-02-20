package co.com.app.model.models.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum businessTypeEnum {
    BILLETERA("BILL","WALLET"),
    COMERCIO("COM","ECOM"),
    DESCONOCIDO("DES","");

    private final String code;
    private final String description;

}
