package apacheopennlp;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.*;

public class IntentTrainer {

    public static void main(String[] args) throws  Exception {
    	Scanner scanner = new Scanner(System.in);

        File trainingDirectory = (new File("C:/Users/SAVAGE/workspace/CHATBOT/train"));
        String[] slots = new String[0];
        if (args.length > 1) {
            slots = args[1].split(",");
        }

        if (!trainingDirectory.isDirectory()) {
            throw new IllegalArgumentException("TrainingDirectory is not a directory: " + trainingDirectory.getAbsolutePath());
        }

        List<ObjectStream<DocumentSample>> categoryStreams = new ArrayList<ObjectStream<DocumentSample>>();
        for (File trainingFile : trainingDirectory.listFiles()) {
            String intent = trainingFile.getName().replaceFirst("[.][^.]+$", "");
            ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
            ObjectStream<DocumentSample> documentSampleStream = new IntentDocumentSampleStream(intent, lineStream);
            categoryStreams.add(documentSampleStream);
        }

        ObjectStream<DocumentSample> combinedDocumentSampleStream = ObjectStreamUtils.concatenateObjectStream(categoryStreams);

        TrainingParameters trainingParams = new TrainingParameters();
        trainingParams.put(TrainingParameters.ITERATIONS_PARAM, 10);
        trainingParams.put(TrainingParameters.CUTOFF_PARAM, 0);

        DoccatModel doccatModel = DocumentCategorizerME.train("en", combinedDocumentSampleStream, trainingParams, new DoccatFactory());
        combinedDocumentSampleStream.close();

        List<TokenNameFinderModel> tokenNameFinderModels = new ArrayList<TokenNameFinderModel>();

        for (String slot : slots) {
            List<ObjectStream<NameSample>> nameStreams = new ArrayList<ObjectStream<NameSample>>();
            for (File trainingFile : trainingDirectory.listFiles()) {
                ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
                ObjectStream<NameSample> nameSampleStream = new NameSampleDataStream(lineStream);
                nameStreams.add(nameSampleStream);
            }
            ObjectStream<NameSample> combinedNameSampleStream = ObjectStreamUtils.concatenateObjectStream(nameStreams);

            TokenNameFinderModel tokenNameFinderModel = NameFinderME.train("en", slot, combinedNameSampleStream, trainingParams, new TokenNameFinderFactory());
            combinedNameSampleStream.close();
            tokenNameFinderModels.add(tokenNameFinderModel);
        }


        DocumentCategorizerME categorizer = new DocumentCategorizerME(doccatModel);
        NameFinderME[] nameFinderMEs = new NameFinderME[tokenNameFinderModels.size()];
        for (int i = 0; i < tokenNameFinderModels.size(); i++) {
            nameFinderMEs[i] = new NameFinderME(tokenNameFinderModels.get(i));
        }

        System.out.println("Training complete. Ready.");
        System.out.print(">");
        String s;

        InputStream modelIn = new FileInputStream("./models/en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);
     

        while ((s =scanner.nextLine())!= null) {
            double[] outcome = categorizer.categorize(tokenizer.tokenize(s));
           // System.out.print(" action: '" + categorizer.getBestCategory(outcome) + "', args:> ");
          //  System.out.println();
            
          
            String[] tokens = tokenizer.tokenize(s);
            
            
            
            InputStream inputStreamNameFinder = new  FileInputStream("./models/en-ner-person.bin");
            TokenNameFinderModel ml = new TokenNameFinderModel(inputStreamNameFinder);
            NameFinderME nameFinder = new NameFinderME(ml);
            
            
      ///add   
            	
                Span[] spans = nameFinder.find(tokens);
               // String[] names = Span.spansToStrings(spans, tokens);
               // for(Span ll: spans)        
                 //   System.out.println(tokens[ll.getStart()]); 
            
                
                
                
          //  System.out.println("} }");
            String res= categorizer.getBestCategory(outcome);
          //  System.out.println(" res "+res );
            if(Objects.equals(res,"1greet"))
            {  
         	   
            
         	   try (BufferedReader br = new BufferedReader(new FileReader("./output/1greet.txt"))) {
         		   String line = null;
         		   while ((line = br.readLine()) != null) {
         		       System.out.println(line);
         		   }
         		}
            
            }
            else if(Objects.equals(res,"whomade"))
            {  
         	   
            
         	   try (BufferedReader br = new BufferedReader(new FileReader("./output/boss.txt"))) {
         		   String line = null;
         		   while ((line = br.readLine()) != null) {
         		       System.out.println(line);
         		   }
         		}
            
            }
            else if(Objects.equals(res,"quit"))
            {  
         	   
            
         	   try (BufferedReader br = new BufferedReader(new FileReader("./output/endconv.txt"))) {
         		   String line = null;
         		   while ((line = br.readLine()) != null) {
         		       System.out.println(line);
         		   }
         		}
            
            }
            else if(Objects.equals(res,"GRN"))
            {  
         	   
            
         	   try (BufferedReader br = new BufferedReader(new FileReader("./output/GRN.txt"))) {
         		   String line = null;
         		   while ((line = br.readLine()) != null) {
         		       System.out.println(line);
         		      
         		   }for(Span ll: spans)        
                       System.out.println(tokens[ll.getStart()]); 
         		}
            
            }
            else if(Objects.equals(res,"wish"))
            {  
            	System.out.print(tokens[0]+"  "+tokens[1]+" ");
         	   
            
         	   try (BufferedReader br = new BufferedReader(new FileReader("./output/wish.txt"))) {
         		   String line = null;
         		   while ((line = br.readLine()) != null) {
         		       System.out.println(line);
         		   }
         		}
            
            }
           

            
          
           
            System.out.print(">");

        }
    }

}
