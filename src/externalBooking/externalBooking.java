/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package externalBooking;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import project.InsertUpdateDelete;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class externalBooking {
    private static int id;
    private static String name;
    private static String tax;
    private static String bound;



public static void setName(String theName){
    name= theName;
}


public static void setTax(String isTax){
    tax= isTax;
}


public static void setBound(String isBound){
    bound= isBound;
}

public static void saveInDb(String db){
     String Query = "insert into externalBooking (name, tax, bound) values('" + name + "','" + tax + "','" + bound + "')";
     InsertUpdateDelete.setData(db, Query, "");
    
}

public static void initExternalBooking(String db, String serviceName){
    //search for all option stored in external booking and get the slected service name
        ResultSet rs = select.getData(db, "select * from externalBooking where name='"+name+"'");
        try {
            while (rs.next()) {
                id= rs.getInt("ID");
                name= rs.getString("name");
                tax= rs.getString("tax");
                bound= rs.getString("bound");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
}

public static int getId(){
    return id;
}

public static String getName (){
    return name ;
}

public static String getTax (){
    return tax ;
}


public static String getBound (){
    return bound ;
}

}
