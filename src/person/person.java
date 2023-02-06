/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package person;

import java.util.ArrayList;
import project.StringFormatter;
/**
 *
 * @author daviddaillere
 */
public class person {

private String id;   
private String name; 
private String firstName; 
private String gender; 
private String tel;
private String tel2;
private String email;
private String street1;
private String street2;
private String dc;
private String postCode;
private String city;

private ArrayList<String> person;

    /**
     *
     */
    public person(){
      
    
}

    /**
     *
     * @return person name 
     */
    public String getName(){
    
    return name;
}

    /**
     *
     * @return person first name
     */
    public String getFirstName(){
    return firstName;
}

    /**
     *
     * @return person id
     */
    public String getId(){
    return id;
}

    /**
     *
     * @return gender
     */
    public String getGender(){
    return gender;
}

    /**
     *
     * @return telephone 1
     */
    public String getTel1(){
    return tel;
}

    /**
     *
     * @return telephone2
     */
    public String getTel2(){
    return tel2;
}

    /**
     *
     * @return  email
     */
    public String getEmail(){
    return email;
}

    /**
     *
     * @return street field 1
     */
    public String getStreet1(){
    return street1;
}

    /**
     *
     * @return street field 2
     */
    public String getStreet2(){
    return street2;
}

    /**
     *
     * @return district
     */
    public String getDc(){
    return dc;
}

    /**
     *
     * @return post code
     */
    public String getPostCode(){
    return postCode;
}

    /**
     *
     * @return city
     */
    public String getCity(){
    return city;
}

    /**
     *
     * @param Gender
     */
    public void setGender(String Gender){
   gender= Gender ;
}

    /**
     *
     * @param Name
     */
    public void setName(String Name){
   name= Name ;
}

    /**
     *
     * @param FirstName
     */
    public void setFirstName(String FirstName){
   firstName= FirstName ;
}

    /**
     *
     * @param Tel
     */
    public void setTel1(String Tel){
   tel= Tel ;
}

    /**
     *
     * @param Tel2
     */
    public void setTel2(String Tel2){
   tel2= Tel2 ;
}

    /**
     *
     * @param Email
     */
    public void setEmail(String Email){
   email= Email;
}

    /**
     *
     * @param Street
     */
    public void setStreet1(String Street){
   street1= Street ;
}

    /**
     *
     * @param Street2
     */
    public void setStreet2(String Street2){
   street2= Street2;
}

    /**
     *set district
     * @param Dc
     * 
     */
    public void setDc(String Dc){
   dc= Dc ;
}

    /**
     *
     * @param PostCode
     */
    public void setPostCode(String PostCode){
   postCode= PostCode;
}

    /**
     *
     * @param City
     */
    public void setCity(String City){
   city= City ;
}

}
