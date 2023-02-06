/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package person;


import java.util.ArrayList;
import javax.swing.JOptionPane;
import project.InsertUpdateDelete;
import project.StringFormatter;
import project.select;
import java.sql.ResultSet;


/**
 *
 * @author daviddaillere
 */
public class owner extends person {

    private String company;
    private String emailPassword;
    private String logo;
    private String siret;
    private String ape;
    private String website;
    private String facebook;
    private String insta;
    private String google;
    private String tripAdvisor;
    private String otherReview;
    private ArrayList<String> ownerDetails;

    /**
     *
     * @param ownerDetails
     */
    public owner() {

    }

    public owner(String db) {
        initOwnerDetails(db);
        setOwner();
    }

    public void initOwnerDetails(String db) {
        ownerDetails = new ArrayList<String>();
        try {
            //search my info in sql
            ResultSet rs = select.getData(db, "select * from myInfo where ID=1");
            //set all information from myInfo in "mes informations" page from my info table
            while (rs.next()) {
                ownerDetails.add(rs.getString("company"));
                ownerDetails.add(rs.getString("name"));
                ownerDetails.add(rs.getString("firstName"));
                ownerDetails.add(rs.getString("tel"));
                ownerDetails.add(rs.getString("tel2"));
                ownerDetails.add(rs.getString("email"));
                ownerDetails.add(rs.getString("password"));
                ownerDetails.add(rs.getString("street"));
                ownerDetails.add(rs.getString("street2"));
                ownerDetails.add(rs.getString("district"));
                ownerDetails.add(rs.getString("cp"));
                ownerDetails.add(rs.getString("city"));
                ownerDetails.add(rs.getString("idCompany"));
                ownerDetails.add(rs.getString("APEcode"));
                ownerDetails.add(rs.getString("website"));
                ownerDetails.add(rs.getString("facebook"));
                ownerDetails.add(rs.getString("instagram"));
                ownerDetails.add(rs.getString("google"));
                ownerDetails.add(rs.getString("tripAdvisor"));
                ownerDetails.add(rs.getString("otherReview"));
            }
            rs.close();
            //search if a logo is stored and get the name
            ResultSet rs2 = select.getData(db, "select * from storedInfo where name='logoName'");
            //search and set the name of the logo path in "mes informations" page from storedInfo table
            while (rs2.next()) {
                logo = (rs2.getString("info"));
            }
            rs2.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * set the ownerDetails's details from the list
     *
     * @param ownerDetails 0= company, 1= name, 2 = first name, 3= tel1, 4=
     * tel2, 5= email, 6= email password, 7= street1, 8= street2, 9= district,
     * 10= post code 11= city, 12= siret, 13= ape, 14= website, 15= facebook,16=
     * insta, 17=google, 18= trip advisor, 19= other review.
     */
    private void setOwner() {
        if(! ownerDetails.isEmpty()){
        company = ownerDetails.get(0);
        setName(ownerDetails.get(1));
        setFirstName(ownerDetails.get(2));
        setTel1(ownerDetails.get(3));
        setTel2(ownerDetails.get(4));
        setEmail(ownerDetails.get(5));
        emailPassword = ownerDetails.get(6);
        setStreet1(ownerDetails.get(7));
        setStreet2(ownerDetails.get(8));
        setDc(ownerDetails.get(9));
        setPostCode(ownerDetails.get(10));
        setCity(ownerDetails.get(11));
        siret = ownerDetails.get(12);
        ape = ownerDetails.get(13);
        website = ownerDetails.get(14);
        facebook = ownerDetails.get(15);
        insta = ownerDetails.get(16);
        google = ownerDetails.get(17);
        tripAdvisor = ownerDetails.get(18);
        otherReview = ownerDetails.get(19);
        }
    }

    public void setCompany(String myCompany) {
        company = StringFormatter.clean(myCompany);
    }

    public void setEmailPassword(String myPassword) {
        if(myPassword == null){
        emailPassword="";
        }else{
       emailPassword = StringFormatter.simpleClean(myPassword);
        }
        
    }

    public void setSiret(String mySiret) {
        if(mySiret == null){
            siret="";
        }else{
        siret = StringFormatter.simpleClean(mySiret);
        }
    }

    public void setApe(String myApe) {
        if(myApe==null){
           ape=""; 
        } else{
        ape = StringFormatter.simpleClean(myApe);
        }
    }

    public void setLogo(String myLogo) {
        if(myLogo == null){
            logo="";
        } else{
        logo = StringFormatter.simpleClean(myLogo);
        }
    }

    public void setWebsite(String myWebsite) {
        if(myWebsite == null){
            website="";
        } else{
        website = StringFormatter.simpleClean(myWebsite);
        }
    }

    public void setFacebook(String myFacebook) {
        if (myFacebook == null){
            facebook= "";
        } else{
        facebook = StringFormatter.simpleClean(myFacebook);
        }
    }

    public void setInsta(String myInsta) {
        if(myInsta== null){
            insta="";
        } else{
        insta = StringFormatter.simpleClean(myInsta);
        }
    }

    public void setGoogle(String myGoogle) {
        if(myGoogle==null){
            google="";
        } else{
        google = StringFormatter.simpleClean(myGoogle);
        }
    }

    public void setTripAdvisor(String myTripAdvisor) {
        if(myTripAdvisor==null){
            tripAdvisor= "";
        } else{
        tripAdvisor = StringFormatter.simpleClean(myTripAdvisor);
        }
    }

    public void setOtherReview(String myOtherReview) {
        if(myOtherReview==null){
            otherReview="";
        } else{
        otherReview = StringFormatter.simpleClean(myOtherReview);
        }
    }

    public String getCompany() {
        return company;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public String getSiret() {
        return siret;
    }

    public String getApe() {
        return ape;
    }

    public String getLogo() {
        return logo;
    }

    public String getWebsite() {
        return website;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInsta() {
        return insta;
    }

    public String getGoogle() {
        return google;
    }

    public String getTripAdvisor() {
        return tripAdvisor;
    }

    public String getOtherReview() {
        return otherReview;
    }

    public void saveOwner(String db) {
        int check = 0;
        ResultSet rs = select.getData(db, "SELECT * FROM myInfo WHERE ID='1'");
        try {
            while (rs.next()) {
//if a company name already exist, check=1;  
                check = 1;
                break;
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        if (check == 1) {
            updateOwner(db);
        } else {
            createOwner(db);
        }
    }

    public void updateOwner(String db) {
        String query = "update myInfo set company= '" + getCompany() + "', name= '" + getName() + "' ,firstName= '" + getFirstName() + "',tel= '" + getTel1() + "',tel2= '" + getTel2() + "', email= '" + getEmail() + "', password= '" + getEmailPassword() + "', street= '" + getStreet1() + "' , street2= '" + getStreet2() + "' ,  district= '" + getDc() + "' , cp= '" + getPostCode() + "' , city= '" + getCity() + "' , idCompany= '" + getSiret() + "', website= '" + getWebsite() + "', facebook= '" + getFacebook() + "', instagram= '" + getInsta() + "', google= '" + getGoogle() + "', tripAdvisor= '" + getTripAdvisor() + "', otherReview= '" + getOtherReview() + "', APEcode= '" + getApe() + "' WHERE ID= '1'";
        InsertUpdateDelete.setData(db, query, "Vos informations ont été modifiées avec succès ");
    }

    public void createOwner(String db) {
        String query = "insert into myInfo ( ID,company,name,firstName, tel,tel2, email,password, street,street2, district, cp, city, idCompany,APEcode, website, facebook, instagram, google, tripAdvisor, otherReview) values('1','" + company + "','" + getName() + "','" + getFirstName() + "','" + getTel1() + "','" + getTel2() + "','" + getEmail() + "','" + getEmailPassword() + "','" + getStreet1() + "','" + getStreet2() + "','" + getDc() + "','" + getPostCode() + "','" + getCity() + "','" + siret + "','" + ape + "','" + website + "','" + facebook + "','" + insta + "','" + google + "','" + tripAdvisor + "','" + otherReview + "')";
        InsertUpdateDelete.setData(db, query, "Vos informations ont été enregistrés avec succès ");
    }

    public static void saveLogo(String db, String logoName) {
        String query = "insert into storedInfo ( name, info) values('logoName','" + logoName + "')";
        InsertUpdateDelete.setData(db, query, "");
    }

    public static void deleteLogo(String db) {
        String Query = "DELETE FROM storedInfo WHERE name = 'logoName'";
        InsertUpdateDelete.setData(db, Query, "");
    }
    
    
    

}
