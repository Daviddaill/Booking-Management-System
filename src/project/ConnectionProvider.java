/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/**
 *
 * @author daviddaillere
 */
public class ConnectionProvider {
    public static Connection getCon(String database){
       
    try{
      String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "Gérer Mon Gite"+File.separator + "Base de Donnée"+File.separator;
        String url = "jdbc:h2:"+home;
     Class.forName("org.h2.Driver"); 
     Connection con = DriverManager.getConnection(url+database,"","");
     return con;
    }
    catch (Exception e){
        return null;
    }
    }
    
   
}


