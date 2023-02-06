/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package person;

/**
 *
 * @author daviddaillere
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import project.InsertUpdateDelete;
import project.select;
/**
 *
 * @author daviddaillere
 */
public class client extends person {
    
    
    
   
    private  int id;
    private  String idProof;
    private String blackList;
    private int searchStatus;
   
    /**
     *
     */
    public client(){  
}


    /**
     *
     * @param clientDetails
     */
    
/** build a client object and set all client's details provided from a list of Strings.
 * @param clientDetails
* 0= gender, 1= name, 2 = firstname, 3 = tel1, 4= tel2, 5= email, 6= street1
* 7=  street2, 8= dc, 9= postCode, 10= city, 11= IDproof, 12= blacklist,
*/
public client(ArrayList<String> clientDetails){
    setClientFromList(clientDetails);
    
}

private void setClientFromList(ArrayList<String> clientDetails){
   setGender(clientDetails.get(0));
   setName(clientDetails.get(1));
   setFirstName(clientDetails.get(2));
   setTel1(clientDetails.get(3));
   setTel2(clientDetails.get(4));
   setEmail(clientDetails.get(5));
   setStreet1(clientDetails.get(6));
   setStreet2(clientDetails.get(7));
   setDc(clientDetails.get(8));
   setPostCode(clientDetails.get(9));
   setCity(clientDetails.get(10));
   setIdProof(clientDetails.get(11));
   setBlackList(clientDetails.get(12));

}
    
    /**
     *
     * @param IdProof
     */
    public void setIdProof(String IdProof){
   idProof= IdProof ;
}

    /**
     *
     * @param BlackList
     */
    public void setBlackList(String BlackList){
   blackList= BlackList;
 }
 
    /**
     *
     * @return
     */
    public int getID(){
   return id;
 }
 
    /**
     *
     * @return proof of id
     */
    public String getIdProof(){
   return idProof ;
}

    /**
     *see if client is set as blacklisted or not
     * @return
     * true if client is blacklisted, and false if not
     */
    public String getBlackList(){
   return blackList;
 }
 
 

    /**
     *create a new client into the choosen database 
     * @param db
     */
    public void save(String db){
        initID(db); 
        String Query = "INSERT INTO client (ID, name,firstName, district, gender, mobileNumber,tel2, email, street,street2, cp, city, IDproof, blackList) values('" + id + "','" + getName() + "','" + getFirstName() + "','" + getDc() + "','" + getGender() + "','" + getTel1() + "','" + getTel2() + "','" + getEmail() + "','" + getStreet1() + "','" + getStreet2() + "','" + getPostCode() + "','" + getCity() + "','" + idProof + "', 'false')";
         InsertUpdateDelete.setData(db, Query, "");
    }
    
   

    /**
     *update the client in the clientTable from choosen database 
     * @param db
     * @param clientId
     */
    public  void update(String db , int clientId){
        String Query = "UPDATE client set name= '" + getName() + "' ,firstName= '" + getFirstName() + "', district= '" + getDc() + "' , gender= '" + getGender() + "' , mobileNumber= '" + getTel1() + "' ,tel2= '" + getTel2() + "' , email= '" + getEmail() + "', street= '" + getStreet1() + "' , street2= '" + getStreet2()+ "' , cp= '" + getPostCode() + "' , city= '" + getCity() + "' , IDproof= '" + idProof + "', blackList= '" + blackList + "'  WHERE id= '" + clientId + "'";
        InsertUpdateDelete.setData(db, Query, "");
    }
    
    

    /**
     *generate a newID for the client by looking at the max id in database and take the next one
     * @param db
     */
    public void initID(String db){
        try {
            String Query = "select max(ID) from client";
            ResultSet rs = select.getData(db, Query);
            while (rs.next()) {
                id = rs.getInt(1)+1;
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

}

