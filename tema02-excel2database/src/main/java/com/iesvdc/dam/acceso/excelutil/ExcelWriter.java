package com.iesvdc.dam.acceso.excelutil;

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
        String sentSHTB = "SHOW TABLES";

        try (PreparedStatement pStatST = conexion.prepareStatement(sentSHTB)) { 
            ResultSet resSet = pStatST.executeQuery();

            while (resSet.next()) {
                StringBuilder sbData = new StringBuilder();
                String nombreHoja = resSet.getString(1);
                String sentSL = "SELECT * FROM " + nombreHoja;

                try (PreparedStatement pStatSL = conexion.prepareStatement(sentSL)) {
                    ResultSet rsDatos = pStatSL.executeQuery();
                    

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
