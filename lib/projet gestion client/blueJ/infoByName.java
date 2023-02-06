 



/**
 * Write a description of infoByName here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class infoByName {
     private searchClient client;
     private docGenerator doc;
    infoByName(String name, String firstName){
        client= new searchClient(name, firstName);
        doc= new docGenerator(name,firstName);
    }
    
    public void getClientInfo(){
        client.printClient();
        
    }
    
    public void getClientBookings(){
        client.printMoreInfo();
    }
    
    public void getAdresse(){
        doc.printAddress();
    }
    
    public void getContract(){
        doc.printContract();
    }
}
