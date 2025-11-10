package com.iesvdc.dam.acceso.excelutil;

import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.iesvdc.dam.acceso.conexion.Config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class ExcelWriter {
    public static String loadDatabaseDatos(Connection conexion) {
        Properties prop = Config.getProperties("config.properties");
        XSSFWorkbook wb = new XSSFWorkbook();
        String sentSHTB = "SHOW TABLES";

        try (PreparedStatement pStatST = conexion.prepareStatement(sentSHTB)) { 
            ResultSet resSet = pStatST.executeQuery();

            while (resSet.next()) {
                String nombreHoja = resSet.getString(1);

                String sentSL = "SELECT * FROM " + nombreHoja;

                Sheet hoja = wb.createSheet(nombreHoja);
                

                try (PreparedStatement pStatSL = conexion.prepareStatement(sentSL)) {
                    ResultSet rsDatos = pStatSL.executeQuery();
                    ResultSetMetaData rsMetaDatos = rsDatos.getMetaData();
                    int numColumnas = rsMetaDatos.getColumnCount();

                    Row cabecera = hoja.createRow(0);

                    for (int i = 1; i < numColumnas; i++) {
                        Cell celda = cabecera.createCell(i - 1);
                        celda.setCellValue(rsMetaDatos.getColumnName(i));

                    }
                    int numFilas = 1;

                    while (rsDatos.next()) {
                        for (int i = 1; i < numColumnas; i++) {
                            Row fila = hoja.createRow(numFilas++);
                            Cell celda = fila.createCell(i - 1);
                            celda.setCellValue(rsDatos.getString(i));
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            }
            String outFile = prop.getProperty("outputFile");
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                wb.write(out);
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            wb.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
