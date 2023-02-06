/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email;

/**
 *
 * @author daviddaillere
 */
public class setSMTP {
    private String Email= "";
    private String Host="";
    private String Port="587";
    

    public setSMTP(String email){
        Email= email;
        setSettings();
}
   private  void setSettings(){
        if (Email.contains("@orange")){
            Host= "smtp.orange.fr";
            Port= "465";
        }
        else if (Email.contains("@yahoo")){
            Host= "smtp.mail.yahoo.com";
            Port= "587";
        }
        else if (Email.contains("@sfr")){
            Host= "smtp.sfr.fr";
            Port= "465";
        }
        else if(Email.indexOf("@")!= -1){
            int index = Email.indexOf("@") + 1;
            Host= Email.substring(index);
            Port= "465";
        }
        
    }
    
    public String getPort(){
        return Port;
    }
    
    public String getHost(){
        return Host;
    }
    
    
}