/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;


import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;
/**
 *
 * @author daviddaillere
 */
public class createDB {
        
    
        public static void newDB(String name) {
        // Defines the JDBC URL. As you can see, we are not specifying
        // the database name in the URL.
        
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "Gérer Mon Gite"+File.separator + "Base de Donnée"+ File.separator+name;
        String url = "jdbc:h2:"+home;

        // Defines username and password to connect to database server.
        String username = "";
        String password = "";
        
       // SQL command to create a database in MySQL.
        String sql = "CREATE DATABASE IF NOT EXISTS "+name;

        try  {
            Class.forName("org.h2.Driver"); 

          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


