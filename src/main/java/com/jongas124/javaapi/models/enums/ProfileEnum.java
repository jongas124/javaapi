package com.jongas124.javaapi.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileEnum {
    ADMIN(1, "ROLE_ADMIN"),
    USER(2,"ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnum toEnum(Integer code) {
        ProfileEnum[] values = ProfileEnum.values();

        if(code == null) {
            return null;
        }

        for (int i = 0; i < values.length; i++) {
            if (code == values[i].getCode()) {
                return values[i];
            }
        }
        
        throw new IllegalArgumentException("Código inválido: " + code);
    }
}
