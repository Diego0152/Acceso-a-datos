package com.iesvdc.dam.acceso.excelutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iesvdc.dam.acceso.conexion.Config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {
    
    public static XSSFWorkbook loadExcel(Connection conexion) {
        Properties prop = Config.getProperties("config.properties");
        
        String inFile = prop.getProperty("inputFile");
        XSSFWorkbook wb = null;

        try {
            wb = new XSSFWorkbook(inFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wb;
    }
    
    public static void loadToDataBase(Connection conexion, XSSFWorkbook wb) {
        final double EPSILON = 1e-10;
        PreparedStatement pStat = null;
        StringBuilder sentCreateSql = new StringBuilder();
        int numHojas = wb.getNumberOfSheets();

        try {
            for (int i = 0; i < numHojas; i++) {
                Sheet hoja = wb.getSheetAt(i);

                sentCreateSql.append("CREATE TABLE " + hoja.getSheetName() + " (\n");

                int numTablas = hoja.getLastRowNum();

                Row encabezado = hoja.getRow(0);
                Row identificarTipo = hoja.getRow(1);
                int numColumnas = encabezado.getLastCellNum();

                for (int j = 0; j < numColumnas; j++) {
                    // System.out.printf("%s: %s | ",celda.getCellType(), celda.getStringCellValue());
                    Cell encabCell = encabezado.getCell(j);
                    Cell identCell = identificarTipo.getCell(j);

                    sentCreateSql.append(encabCell.getStringCellValue() + " ");

                    switch (identCell.getCellType()) {
                        case STRING -> {
                            if (encabCell.getStringCellValue().equals("teléfono")) {
                                sentCreateSql.append("VARCHAR(12)");  
                            } else if (encabCell.getStringCellValue().equals("género")) {
                                sentCreateSql.append("enum('FEMENINO','MASCULINO','NEUTRO','OTRO') NOT NULL");
                            } else {
                                sentCreateSql.append("VARCHAR(300)");

                                if (encabCell.getStringCellValue().equals("nombre")) {
                                    sentCreateSql.append(" NOT NULL");
                                } else if (encabCell.getStringCellValue().equals("apellidos")) {
                                    sentCreateSql.append(" NOT NULL");
                                }
                            }

                        }

                        case NUMERIC -> {
                            if (DateUtil.isCellDateFormatted(encabCell)) {
                                sentCreateSql.append("DATE");
                            } else {
                                double valor = encabCell.getNumericCellValue();
                                if (Math.abs(valor - Math.floor(valor)) < EPSILON) {
                                    sentCreateSql.append("INT(10)");
                                } else {
                                    sentCreateSql.append("DOUBLE(10, 2)");
                                }
                            }
                        }

                        case BOOLEAN -> {
                            sentCreateSql.append("BOOLEAN ");
                        }

                        default -> {
                            sentCreateSql.append("");
                        }               
                        
                    }

                    if (j < numColumnas - 1) {
                        sentCreateSql.append(",\n");
                    } else {
                        sentCreateSql.append("\n");
                    }
                }

                sentCreateSql.append(") ENGINE=InnoDB;");
                pStat = conexion.prepareStatement(sentCreateSql.toString());
                if (pStat.executeUpdate() == 0) {
                    System.out.println(sentCreateSql.toString());
                    System.out.println("Tabla " + hoja.getSheetName() + " creada.");
                } else {
                    System.err.println("Error al crear la tabla.");
                }

                for (int j = 1; j < numTablas; j++) {
                    Row columna = hoja.getRow(j);

                    int numCelda = columna.getLastCellNum();

                    // System.out.println();
                    
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error: No se ha podido meter los datos a la BBDD.");
        }
        
    }
}
