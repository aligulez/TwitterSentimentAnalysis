package TwitterSentimentPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonGenerationException ;





public class InitialClass {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		    JsonFactory f = new JsonFactory();

		    try {
				
		    	File file = new File("/Users/aligulez/Desktop/IRDM-Data/exampletweets_2.txt") ;
		    	
		    	InputStream inputStream = new FileInputStream(file) ;
		    	
		    	JsonParser p = f.createJsonParser(inputStream) ;
		    	
		    	
		    	  while (p.nextToken() != JsonToken.END_OBJECT) { //loop through all the fields 
				    	
		    		  String fieldname = p.getCurrentName();
		    		  
		    		  String value = p.getValueAsString() ;
		    		  
		    		  System.out.println(fieldname) ;
		    		  System.out.println(value) ;

		    		  

				  }//end while 
				    
		    	  p.close() ;//close the parser
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  
			// CATCH statements 
		    } catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		  
		System.out.println("Hello World") ;
		
	}

}
