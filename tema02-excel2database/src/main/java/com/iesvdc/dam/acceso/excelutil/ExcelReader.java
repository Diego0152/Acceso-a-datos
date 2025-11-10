package com.iesvdc.dam.acceso.excelutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iesvdc.dam.acceso.conexion.Config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {
    
    public static XSSFWorkbook loadExcel(Connection conexion) {
        // Sincronizamos la configuración de la base de datos.
        Properties prop = Config.getProperties("config.properties");

        // Recogemos el archivo de 
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

        int numHojas = wb.getNumberOfSheets();

        StringBuilder sentSQLCRTB = new StringBuilder();
        StringBuilder sentSQLINVL = new StringBuilder();

        try {

            Statement pStatIN = conexion.createStatement();
            Statement pStatCT = conexion.createStatement();

            for (int i = 0; i < numHojas; i++) {
                Sheet hoja = wb.getSheetAt(i);

                if (sentSQLCRTB.length() != 0) 
                    sentSQLCRTB.setLength(0);
                
                if (sentSQLINVL.length() != 0) 
                    sentSQLINVL.setLength(0);
                
                sentSQLCRTB.append("CREATE TABLE " + hoja.getSheetName() + " (\n");
                sentSQLINVL.append("INSERT INTO " + hoja.getSheetName() + " (");

                int numTablas = hoja.getLastRowNum();

                Row encabezado = hoja.getRow(0);
                Row identificarTipo = hoja.getRow(1);
                int numColumnas = encabezado.getLastCellNum();

                for (int j = 0; j < numColumnas; j++) {
                    // System.out.printf("%s: %s | ",celda.getCellType(), celda.getStringCellValue());
                    Cell encabCell = encabezado.getCell(j);
                    Cell identCell = identificarTipo.getCell(j);

                    sentSQLCRTB.append(encabCell.getStringCellValue() + " ");
                    sentSQLINVL.append(encabCell.getStringCellValue());

                    switch (identCell.getCellType()) {
                        case STRING -> {
                            if (encabCell.getStringCellValue().equals("teléfono")) {
                                sentSQLCRTB.append("VARCHAR(12)");  
                            } else if (encabCell.getStringCellValue().equals("género")) {
                                sentSQLCRTB.append("enum('FEMENINO','MASCULINO','NEUTRO','OTRO') NOT NULL");
                            } else {
                                sentSQLCRTB.append("VARCHAR(300)");

                                if (encabCell.getStringCellValue().equals("nombre")) {
                                    sentSQLCRTB.append(" NOT NULL");
                                } else if (encabCell.getStringCellValue().equals("apellidos")) {
                                    sentSQLCRTB.append(" NOT NULL");
                                }

                            }
                        }

                        case NUMERIC -> {
                            if (DateUtil.isCellDateFormatted(encabCell)) {
                                sentSQLCRTB.append("DATE");
                            } else {
                                double valor = encabCell.getNumericCellValue();
                                if (Math.abs(valor - Math.floor(valor)) < EPSILON) {
                                    sentSQLCRTB.append("INT(10)");
                                } else {
                                    sentSQLCRTB.append("DOUBLE(10, 2)");
                                }
                                
                            }
                        }

                        case BOOLEAN -> {
                            sentSQLCRTB.append("BOOLEAN ");
                        }

                        default -> {
                            sentSQLCRTB.append("");
                        }               
                        
                    }

                    if (j < numColumnas - 1) {
                        sentSQLCRTB.append(",\n");
                    } else {
                        sentSQLCRTB.append("\n");
                    }

                    if (j < numColumnas - 1) {
                        sentSQLINVL.append(", ");
                    } else {
                        sentSQLINVL.append(")\n");
                    }

                }

                sentSQLINVL.append("VALUES (");

                for (int k = 1; k <= numTablas; k++) {
                    Row fila = hoja.getRow(k);
                    int numCeldas = fila.getLastCellNum();
                    
                    for (int l = 0; l < numCeldas; l++) {
                        sentSQLINVL.append("'" + fila.getCell(l) + "'");

                        if (l < numColumnas - 1) {
                            sentSQLINVL.append(", ");
                        } else {
                            sentSQLINVL.append("");
                        }
                        
                    } 
                    
                    sentSQLINVL.append(")");
                    if (k <= numTablas - 1) {
                        sentSQLINVL.append(",\n(");
                    } else {
                        sentSQLINVL.append(";\n");
                    }
                }

                sentSQLCRTB.append(") ENGINE=InnoDB;");
                int verifCT = pStatCT.executeUpdate(sentSQLCRTB.toString());
                if (verifCT == 0) {
                    System.out.println("\nTabla " + hoja.getSheetName() + " creada.\n");
                    System.out.println(sentSQLCRTB.toString());
                    System.out.println();
                    
                } else {
                    System.err.println("Error al crear la tabla.");
                }
                
                int verifIN = pStatIN.executeUpdate(sentSQLINVL.toString());
                
                if (verifIN > 0) {
                    System.out.println("Datos insertados de: " + hoja.getSheetName() + "\n");
                    System.out.println();
                    
                } else {
                    System.err.println("Error al crear la tabla.");
                }
                
            }
            
        } catch (SQLException e) {
            System.err.println("Error: Ha habido un problema en el método loadToDataBase.");
            e.printStackTrace();
        }
        
    }
}
