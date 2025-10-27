package com.iesvdc.dam.acceso.modelo;
/**
 * El modelo que almacena información de un campo y sus propiedades.
 */

public class FieldModel {

    public FieldModel() {
        this.name = "";
        this.type = null;
    }

    public FieldModel(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }


    public FieldType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
    private final String name;
    private final FieldType type;

}
