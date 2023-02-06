/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

/**
 *
 * @author daviddaillere
 */
    public class StringFormatter {  
       
    public static String capitalizeWord(String str){  
        String words[]=str.split("\\s");  
        String capitalizeWord="";  
        int getQuote=0;
        int getBar=0;
        for(String w:words){ 
            getQuote= w.indexOf("'");
            getBar=w.indexOf("-");
            if(getQuote == 1 && getQuote<= w.length()-2){
            int upperIndex= getQuote+1;
            int afterQuoteIndex= getQuote+2;
            String first=w.substring(0, upperIndex);  
            String upper=w.substring(upperIndex, afterQuoteIndex);
            String afterUpper= w.substring(afterQuoteIndex);
            capitalizeWord+=first+upper.toUpperCase()+afterUpper+" ";  
            }
            
            else if(getBar >=1 && getBar<= w.length()-2){
            int upperIndex= getBar+1;
            int afterBarIndex= getBar+2;
            String firstLetter= w.substring(0,1);
 
            String first=w.substring(1, upperIndex);  
            String upper=w.substring(upperIndex, afterBarIndex);
            String afterUpper= w.substring(afterBarIndex);
            capitalizeWord +=firstLetter.toUpperCase()+first+upper.toUpperCase()+afterUpper+" ";
            
            }
            else{
            String first=w.substring(0,1);  
            String afterfirst=w.substring(1);  
            capitalizeWord+= first.toUpperCase()+afterfirst+" ";  
            }
        }  
        return capitalizeWord.trim();  
    }
    
    public static String clean(String s) {
        if (s == null) {
            s = "";
        } else {
            if (!s.equals("")) {
                s = s.trim();
                s = s.toLowerCase();
                s = StringFormatter.capitalizeWord(s);
                s = s.replace("\'", "\'\'");
            }
        }
        //s= s.replace("\"\"","");
        return s;
    }

    public static String simpleClean(String s) {
        if (s == null) {
            s = "";
        } else {
            if (!s.equals("")) {
                s = s.trim();
                s = s.replace("\'", "\'\'");
            }
        }
        //s= s.replace("\"\"","");
        return s;
    }
    
    public static String cleanNumber(String s){
       return  s.replaceAll(",",".");
    }
}
