/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package person;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import project.StringFormatter;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class searchClient {

    private int searchStatus;
    private ArrayList<String> client;
    private HashMap<Integer, ArrayList<String>> clientMap;
    private HashMap<String, String> commentMap;
    private String db;
    private int id = 0;

 

    /**
     *init instance variable db and clientMap to get info about all existing client with same firstName and name   
     * @param dataBase
     * @param name
     * @param firstName
     */
    public searchClient(String dataBase, String name, String firstName) {
        db = dataBase;
        search(name, firstName);
        System.out.println(clientMap);

    }
    


    /**
     *Return the searchStatus of the object,
     * @return
     * 0= client doesn't exist, 1= only one client exist, 2= several client exist
     */
    public int getSearchStatus() {
        return searchStatus;
    }


    /**
     *set search status to 0 
     *(when you want to create a new client with the same name and first name than an already existing client)  
     */
    public void resetSearchStatus() {
        searchStatus = 0;
    }

  

    /**
     *provide an id list of all client with the same name and first name  
     * @return
     *list of client id
     */
    public ArrayList<Integer> getIds() {
        System.out.println("start getIds");
        var list = new ArrayList<Integer>();
        for (int i : clientMap.keySet()) {
            list.add(i);
        }
        return list;
    }
// provide the only one key of the map

    /**
     *provide the last save clientid
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *set the first client id of the clientMap (to use if there is only one client)
     */
    public void setId() {
        id = getIds().get(0);
    }

    /**
     *set the instance variable clientId  to the id of your choice
     * @param clientId
     */
    public void setID(int clientId) {
        id = clientId;
    }

//provide an ArrayList with all the client details, to be use with client class constructor to have all instance variable already set   

    /**
     *get the client details of the given client id if this Id is in the map
     * @param clientID
     * @return  client list: 
     * 0= gender, 1= name, 2 = first name, 3 = tel1, 4= tel2, 5= email, 6= street1
     * 7=  street2, 8= dc, 9= postCode, 10= city, 11= IDproof, 12= blacklist,
     */
    public ArrayList<String> getClientDetails(int clientID) {
        if (clientMap.containsKey(clientID)) {
            client = clientMap.get(clientID);
        }
        return client;

    }

//search client id in database and if found add the client details in an array list 
//then put in clientMap key= clientID and value = arraylist; 
//list details: 0= gender, 1= name, 2 = firstname, 3 = tel1, 4= tel2, 5= email, 6= street1
//              7=  street2, 8= dc, 9= postCode, 10= city, 11= IDproof, 12= blacklist,
//search for all client with same firstname and name and call the method setclient() to add in clientMap
    private void findClient(String name, String firstName) {
        System.out.println("find client");
        ResultSet rs = select.getData(db, "select * from client where name= '" + name + "' and firstName='" + firstName + "'");
        try {
            while (rs.next()) {
                int clientID = rs.getInt("ID");
                var list = new ArrayList<String>();
                list.add(rs.getString("gender"));
                list.add(rs.getString("name"));
                list.add(rs.getString("firstName"));
                list.add(rs.getString("mobileNumber"));
                list.add(rs.getString("tel2"));
                list.add(rs.getString("email"));
                list.add(rs.getString("street"));
                list.add(rs.getString("street2"));
                list.add(rs.getString("district"));
                list.add(rs.getString("cp"));
                list.add(rs.getString("city"));
                list.add(rs.getString("IDproof"));
                list.add(rs.getString("blacklist"));
                client = list;
                clientMap.put(clientID, list);
            }
// if there is more than one client found then add in list all new client's id found in data base with same name and set checkclient to 2( more than one client)
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }



    /**
     *build clientMap with all client found with same name and first name  (key= clientID, value= list of client details ).Then set searchStatus (0= no client found, 1=only 1 client found, 2= many client found)
     * @param name
     * @param firstName
     */
    public void search(String name, String firstName) {
        clientMap = new HashMap<Integer, ArrayList<String>>();
//clean name and firstName 
        System.out.println("starting search");
        name = StringFormatter.clean(name);
        firstName = StringFormatter.clean(firstName);
        findClient(name, firstName);
        if (clientMap == null) {
            searchStatus = 0;
        } else {

            if (clientMap.size() == 1) {
                searchStatus = 1;
            }
            if (clientMap.size() > 1) {
                searchStatus = 2;
            }
        }
    }

  

    /**  
     * @param clientID
     * @return list of all client checkIn dates
     */
    public ArrayList<String> getDates(int clientID) {
        System.out.println("getDate");
        var list = new ArrayList<String>();
        //set comments combobox and panel
        ResultSet rs = select.getData(db, "SELECT * FROM  booking where clientID='" + clientID + "' and  status != 'annulé' ORDER BY checkIn");
        try {

            while (rs.next()) {
                list.add(rs.getString("booking.checkIn"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }

    /**
     *
     * @param clientId
     * @param date
     * @return  a booking comment of the client 
     */
    public String getComment(int clientId, String date) {
        System.out.println("getComment");
        String comment = "";
        ResultSet rs = select.getData(db, "SELECT * FROM  booking where clientID='" + clientId + "' and checkIn='" + date + "'");
        try {
            //display comment in the comment panel
            while (rs.next()) {
                comment = rs.getString("comment");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return comment;
    }

    /**
     *clear all instance variable
     */
    public void clearAll() {
        if (commentMap != null) {
            commentMap.clear();
        }
        if (clientMap != null) {
            clientMap.clear();
        }
        if (client != null) {
            client.clear();
        }
        db = null;
        searchStatus = 0;

    }
}
