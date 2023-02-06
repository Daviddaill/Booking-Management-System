 



public class testit {
    private searchClient client;
     private docGenerator doc;
    testit(String name, String firstName){
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
    
    public void testFindClient(){
       if (client.findClient()== true ){
           getClientInfo();
        }
    }
}
