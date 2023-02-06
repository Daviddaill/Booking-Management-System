 

import edu.duke.*;
import org.apache.commons.csv.*;
import java.util.*;
/**
 * Write a description of searchClient6 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class searchClient {
    private FileResource dataBase;
    private HashMap<Integer, ArrayList> storeInfoMap;
    private HashMap<String,String> clientMap;
    private Map<String,Integer> headerMap;
    private String nom;
    private String prenom;
    
    
    public searchClient(String name, String firstName){
        name= name.toLowerCase();
        firstName= firstName.toLowerCase();
        nom=name;
        prenom= firstName;
        dataBase= new FileResource("data/gestion client.csv");         
        initializeHeaderMap(name, firstName);
        initializeInfoByCatMap(name,firstName);
        storeInfo(name, firstName);
    }
    
    private int infoSize(){
        CSVParser parser= dataBase.getCSVParser();
        int answer=0;
        for( CSVRecord record: parser){
          answer= record.size();
          break;
        }
        
        return answer;
       }
    
    private HashMap<String,String> initializeInfoByCatMap(String name, String firstName){
        
        clientMap= new HashMap<String,String>();
        CSVParser parser= dataBase.getCSVParser();
         
         for( CSVRecord record: parser){
           String CurrName= record.get("nom").toLowerCase();  
           String currFirstName= record.get("prenom").toLowerCase();
          if( CurrName.equals(name) && currFirstName.equals(firstName)){
              for (String s : headerMap.keySet()) {
                // process each key in turn 
              int i = 0;
              if(i< headerMap.size()){            
                    clientMap.put(s, record.get(s));
                    i+= 1;               
              }
              else{
                clientMap.clear();
                clientMap.put(s, record.get(s));                    
              }     
          
           }
          
         }
        }
        
        if( ! clientMap.containsKey("nom")){
            clientMap.put("sorry", "le client n'existe pas");
        }
        
        setUpperLowerCase();
        
        return clientMap;
        
    }
        
    private HashMap<String,String> setUpperLowerCase(){
            
        for (String s : clientMap.keySet()) {
                  // process each key in turn 
                  s= s.toLowerCase();
                  if(s.equals("nom")||s.equals("prenom")||s.equals("rue")||s.equals("ville")){
                     String word=clientMap.get(s);
                     word.toLowerCase();
                     StringBuilder sb= new StringBuilder(word);
                      for(int i =0; i< word.length(); i++){
                          int start= i;
                          int end= i+1;   
                          String check = word.substring(start,end);
                          if( check.equals(" ")){
       
                              sb.setCharAt(end,Character.toUpperCase(sb.charAt(end)));
                            }
                     }       
                     sb.setCharAt(0,Character.toUpperCase(sb.charAt(0)));
                     String answer= sb.toString();                     
                     clientMap.put(s, answer);
                    }
           } 
                
        return clientMap;
    }
       
    private Map<String,Integer> initializeHeaderMap(String name, String firstName){
        CSVParser parser= dataBase.getCSVParser();        
        headerMap = parser.getHeaderMap();
        return headerMap;
    }
    
       
    private HashMap<Integer, ArrayList> storeInfo(String name, String firstName){
       storeInfoMap= new HashMap<Integer, ArrayList>();
       CSVParser parser= dataBase.getCSVParser();
       String unknown= "le client n'existe pas";
       int numberOfStay= 1;
        for( CSVRecord record: parser){
           if( record.get("nom").equals(name) && record.get("prenom").equals(firstName)){
              // get the name of that line;
              ArrayList<String> storeInfo= new ArrayList<String>();
              
            for(int i=0; i< infoSize(); i++){
                  storeInfo.add(record.get(i));
                  
                }
              storeInfoMap.put( numberOfStay, storeInfo);
              numberOfStay+=1;
          }
          
       }
       
       
       return storeInfoMap;
    }
    
     public boolean findClient(){
     
       if( clientMap.containsKey("sorry")){
          return false; 
       }
         
         return true; 
    }
    
    public HashMap<String,String> getInfo(){
        return clientMap;
    }
    
    public void printMoreInfo(){
        System.out.println("");
        System.out.println("info complementaire: ");
        
        for (Integer i : storeInfoMap.keySet()) {
        // process each key in turn 
        ArrayList<String> info= storeInfo(nom, prenom).get(i);
        String checkIn= info.get(headerMap.get("checkin"));
        String checkOut= info.get(headerMap.get("checkout"));
        String amount= info.get(headerMap.get("montant"));
        String comment= info.get(headerMap.get("commentaire"));
        
        System.out.println("sejour "+ i+":");       
        System.out.println("date: du "+checkIn+" au "+checkOut);
        System.out.println("montant: "+ amount);
        System.out.println("commentaire: "+ comment);
        System.out.println("");
       } 
    }
    
    public void printClient(){
        if( clientMap.get("name") != "le client n'existe pas"){
            String sex= clientMap.get("denomination");
            String adress= clientMap.get("rue")+", "+clientMap.get("cp")+ ", "+clientMap.get("ville")+".";
            String email= clientMap.get("email");
            String tel= clientMap.get("telephone");
            System.out.println("nom: "+sex+" "+ clientMap.get("nom")+ " "+ clientMap.get("prenom")+".");
            System.out.println("adresse: "+adress);
            System.out.println("email: "+email);
            System.out.println("tel: "+tel);
            int mapSize= storeInfoMap.size();
            System.out.println("nombre de sejour: "+mapSize);
        
       }
       else{
           System.out.println(clientMap.get(nom)+" "+ clientMap.get(prenom)+ " n'existe pas");
        }
    }
    
    public String getName(){
        
        return clientMap.get("nom");
    }
    
    public String getFirstName(){
        
        return clientMap.get("prenom");
    }
    
    public String getStreet(){
        return clientMap.get("rue");
    }
    
    public String getCP(){
        return clientMap.get("cp");
    }
    
    public String getCity(){
        return clientMap.get("ville");
    }
    
    public String getCountry(){
        return clientMap.get("pays");
    }
    
    public String getphone(){
        return clientMap.get("telephone");
    }
    
    public String getEmail(){
        return clientMap.get("email");
    }
    
    public String getStay(){
        int num= storeInfoMap.size();
        String stay= Integer.toString(num);
        return stay;
    }
        
    public void tester(){
        System.out.println(getInfo());
        
    }
    
    
}

    

