package apacheopennlp;

import java.io.FileInputStream; 
import java.io.InputStream;  

import opennlp.tools.sentdetect.SentenceDetectorME; 
import opennlp.tools.sentdetect.SentenceModel;  

public class SentenceDetectionME { 
  
   public static void main(String args[]) throws Exception { 
   
      String sentence = "Hi. This is Surya. " 
         + "I am working on a project"; 
       
      
      InputStream inputStream = new FileInputStream("C:/opennlpmodels/en-sent.bin"); 
      SentenceModel model = new SentenceModel(inputStream); 
       
     
      SentenceDetectorME detector = new SentenceDetectorME(model);  
    
    
      String sentences[] = detector.sentDetect(sentence); 
    
      
      for(String sent : sentences)        
         System.out.println(sent);  
   } 
}