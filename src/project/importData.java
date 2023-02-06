/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author daviddaillere
 */
public class importData {

    public static void client(String db) {
        int newClientID = 1;
        String Query = "select max(ID) from client";
        ResultSet rs = select.getData(db, Query);
        try {
            while (rs.next()) {
                newClientID = rs.getInt(1);
                newClientID += 1;

            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(importData.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Reader reader = Files.newBufferedReader(Paths.get("documents/Importer clients.csv"));  
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT); 
            
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                
                String gender = StringFormatter.simpleClean(csvRecord.get("0"));
                String name = StringFormatter.simpleClean(csvRecord.get("1"));
                String firstName = StringFormatter.simpleClean(csvRecord.get("2"));
                String tel = StringFormatter.simpleClean(csvRecord.get("3"));
                String email = StringFormatter.simpleClean(csvRecord.get("4"));
                String street = StringFormatter.simpleClean(csvRecord.get("5"));
                String district = StringFormatter.simpleClean(csvRecord.get("6"));
                String cp = StringFormatter.simpleClean(csvRecord.get("7"));
                String city = StringFormatter.simpleClean(csvRecord.get("8"));
                String country = StringFormatter.simpleClean(csvRecord.get("9"));
                String identity = StringFormatter.simpleClean(csvRecord.get("10"));
                Query = "insert into client (ID, name,firstName, district, gender, mobileNumber, email, street, cp, city, country, IDproof) value('" + newClientID + "','" + name + "','" + firstName + "','" + district + "','" + gender + "','" + tel + "','" + email + "','" + street + "','" + cp + "','" + city + "','" + country + "','" + identity + "')";
                InsertUpdateDelete.setData(db, Query, "");
                newClientID += 1;
                

            }
            JOptionPane.showMessageDialog(null, "CLients importés avec succès");
        } catch (IOException e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }
}

