package com.iesvdc.dam.acceso.modelo;

import java.util.List;

/**
 * El modelo que almacena el libro o lista de tablas.
 */
public class WorkbookModel {

    public WorkbookModel() {
        this.tables = null;
    }

    public WorkbookModel(List<TableModel> tables) {
        this.tables = tables;
    }

    public List<TableModel> getTables() {
        return this.tables;
    }

    @Override
    public String toString() {
        return "{" +
            " tables='" + getTables() + "'" +
            "}";
    }
    private final List<TableModel> tables;

}
