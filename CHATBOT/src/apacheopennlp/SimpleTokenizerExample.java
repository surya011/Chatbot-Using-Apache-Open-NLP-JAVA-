package apacheopennlp;

import opennlp.tools.tokenize.SimpleTokenizer;  
public class SimpleTokenizerExample { 
   public static void main(String args[]){ 
     
      String sentence = "hey whats going on ? How can i help you"; 
      
    
      
      SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;  
       
    
      String tokens[] = simpleTokenizer.tokenize(sentence);  
       
     
      for(String token : tokens) {         
         System.out.println(token);  
      }       
   }  
}