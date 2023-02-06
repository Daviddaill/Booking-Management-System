/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package person;

import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import java.sql.*;
import javax.swing.JOptionPane; 
import project.InsertUpdateDelete;
/**
 *
 * @author daviddaillere
 */
public class manageClient {
   

    public static void BlackList(String db, int ID, boolean b){
        
            if(b){
            String Query = "UPDATE client set blackList= 'true' WHERE ID= '"+ID+"'";
            InsertUpdateDelete.setData(db, Query, "");
            } else{
               String Query = "UPDATE client set blackList= 'false'";
            InsertUpdateDelete.setData(db, Query, ""); 
            }
             
        
    }
}
