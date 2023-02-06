/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import document.document;
import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author daviddaillere
 */
public class excelFile {

    public static void insertData(String db) {
        try {
//declare file name to be create   
            String filename = document.getPath(db, "clients") + "mes clients.xls";
//creating an instance of HSSFWorkbook class  
            HSSFWorkbook workbook = new HSSFWorkbook();
//invoking creatSheet() method and passing the name of the sheet to be created   
            HSSFSheet sheet = workbook.createSheet("Clients");
//creating the 0th row using the createRow() method  
            HSSFRow rowhead = sheet.createRow((short) 0);
//creating cell by using the createCell() method and setting the values to the cell by using the setCellValue() method  
            rowhead.createCell(0).setCellValue("ID");
            rowhead.createCell(1).setCellValue("Nom");
            rowhead.createCell(2).setCellValue("Prénom");
            rowhead.createCell(3).setCellValue("Genre");
            rowhead.createCell(4).setCellValue("Tel");
            rowhead.createCell(5).setCellValue("Tel2");
            rowhead.createCell(6).setCellValue("Email");
            rowhead.createCell(7).setCellValue("Adresse1");
            rowhead.createCell(8).setCellValue("Adresse2");
            rowhead.createCell(9).setCellValue("Commune dlg");
            rowhead.createCell(10).setCellValue("Cp");
            rowhead.createCell(11).setCellValue("Ville");
            rowhead.createCell(12).setCellValue("Identité");
//creating the 1st row  

//inserting data in a new row 
            ResultSet rs = select.getData(db, "select * from client where blackList= 'false'");
            int num = 1;
            try {
                while (rs.next()) {

                    HSSFRow row = sheet.createRow((short) num);
                    row.createCell(0).setCellValue(rs.getString("ID"));
                    row.createCell(1).setCellValue(rs.getString("name"));
                    row.createCell(2).setCellValue(rs.getString("firstName"));
                    row.createCell(3).setCellValue(rs.getString("gender"));
                    row.createCell(4).setCellValue(rs.getString("mobileNumber"));
                    row.createCell(5).setCellValue(rs.getString("tel2"));
                    row.createCell(6).setCellValue(rs.getString("email"));
                    row.createCell(7).setCellValue(rs.getString("street"));
                    row.createCell(8).setCellValue(rs.getString("street2"));
                    row.createCell(9).setCellValue(rs.getString("district"));
                    row.createCell(10).setCellValue(rs.getString("cp"));
                    row.createCell(11).setCellValue(rs.getString("city"));
                    row.createCell(12).setCellValue(rs.getString("IDproof"));

                    num += 1;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);

            sheet = workbook.createSheet("Client indésirables");
//creating the 0th row using the createRow() method  
            rowhead = sheet.createRow((short) 0);
//creating cell by using the createCell() method and setting the values to the cell by using the setCellValue() method  
            rowhead.createCell(0).setCellValue("ID");
            rowhead.createCell(1).setCellValue("Nom");
            rowhead.createCell(2).setCellValue("Prénom");
            rowhead.createCell(3).setCellValue("Genre");
            rowhead.createCell(4).setCellValue("Tel");
            rowhead.createCell(5).setCellValue("Tel2");
            rowhead.createCell(6).setCellValue("Email");
            rowhead.createCell(7).setCellValue("Adresse1");
            rowhead.createCell(8).setCellValue("Adresse2");
            rowhead.createCell(9).setCellValue("Commune dlg");
            rowhead.createCell(10).setCellValue("Cp");
            rowhead.createCell(11).setCellValue("Ville");
            rowhead.createCell(12).setCellValue("Identité");
//creating the 1st row  

//inserting data in a new row 
            rs = select.getData(db, "SELECT * FROM client WHERE blackList= 'true'");
            num = 1;
            try {
                while (rs.next()) {

                    HSSFRow row = sheet.createRow((short) num);
                    row.createCell(0).setCellValue(rs.getString("ID"));
                    row.createCell(1).setCellValue(rs.getString("name"));
                    row.createCell(2).setCellValue(rs.getString("firstName"));
                    row.createCell(3).setCellValue(rs.getString("gender"));
                    row.createCell(4).setCellValue(rs.getString("mobileNumber"));
                    row.createCell(5).setCellValue(rs.getString("tel2"));
                    row.createCell(6).setCellValue(rs.getString("email"));
                    row.createCell(7).setCellValue(rs.getString("street"));
                    row.createCell(8).setCellValue(rs.getString("street2"));
                    row.createCell(9).setCellValue(rs.getString("district"));
                    row.createCell(10).setCellValue(rs.getString("cp"));
                    row.createCell(11).setCellValue(rs.getString("city"));
                    row.createCell(12).setCellValue(rs.getString("IDproof"));

                    num += 1;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();

//closing the Stream  
//closing the workbook  
            workbook.close();
            rs.close();
//prints the message on the console  
            System.out.println("Excel file has been generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
 

