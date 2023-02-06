 

import edu.duke.*;
import java.util.*;
/**
 * Write a description of writeContract here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class docGenerator {
    private FileResource dataBase;  
    private FileResource contract;
    private searchClient client;
    private HashMap<String,String> infoClient;
    private static String  nom;
    private static String prenom;
    
    public docGenerator(String name, String firstName){
        dataBase= new FileResource("data/gestion client.csv");
        contract= new FileResource("data/Contrat de location.txt");
        nom= name;
        prenom= firstName;
        client= new searchClient(nom, prenom);
        infoClient= client.getInfo();
    }
    
    private String processWord(String w){
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        if (first == -1 || last == -1){
            return w;
        }
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        String sub = infoClient.get(w.substring(first+1,last));           
        return prefix+sub+suffix;
    }
    
    private String generateWords(){              
       String newContract = ""; 
       
       for(String word : contract.words()){
           
           newContract = newContract + processWord(word) + " ";
           
       }
        
       return newContract;
    }
    
    private String createContract(){
      StringBuilder sb= new StringBuilder(generateWords());
       for(int i =0; i< sb.length(); i++){
        int start= i;
        int end= i+1;   
        String check = sb.substring(start,end);
          if( check.equals("*")){
              sb.replace(start, end, System.lineSeparator());
             
        }
      }
      
      String answer= sb.toString();
      return answer;
    }
    
    public void printContract(){
       System.out.println(createContract());
    }
    
    
    public void printAddress(){
       String sex= infoClient.get("denomination");
       String firstName= infoClient.get("prenom");
       String name= infoClient.get("nom");
       String street= infoClient.get("rue");
       String postcode= infoClient.get("cp");
       String city= infoClient.get("ville");
       
       String client= sex+ " "+firstName+ " "+name;
       String lastLine= postcode+ ", "+city;
       
       System.out.println(client);
       int charsWritten = 0;
       int lineWidth= 30;
       for(String w : street.split("\\s+")){
           if (charsWritten + w.length() > lineWidth){
		System.out.println();
		charsWritten = 0;
           }
	   System.out.print(w+" ");
	   charsWritten += w.length() + 1;
	}
        System.out.println();
	System.out.print(lastLine);
	
    }
    
}

