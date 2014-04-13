package TwitterSentimentPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonGenerationException ;

public class InitialClass {


	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		    JsonFactory f = new JsonFactory();
		    String newTweet ;
		    
		    try {
				//initialise file, reader and parser.
		    	File file = new File("/Users/aligulez/Desktop/IRDM-Data/tweets_snippet.txt") ;
		    	InputStream inputStream = new FileInputStream(file) ;		    	
		    	JsonParser p = f.createJsonParser(inputStream) ;// p is the parser
		    	JsonGenerator g = f.createJsonGenerator(new File("/Users/aligulez/Desktop/IRDM-Data/output_data.json"), JsonEncoding.UTF8);// g is the generator
		    	
		    	JsonToken current; 
		    	
		    	current = p.nextToken();
		    	
		    	//check if initial object is a start of the tweet object
		    	if (current != JsonToken.START_OBJECT){
		    		
		    		System.out.println("Error: initial char not start of object!");
		    		current = p.nextToken();//move to next token. 
		    		
		    	}//end if
	    			    	
		    	while(p.hasCurrentToken()== true){
		    		
		    		current = p.nextToken() ; 
		    		String fieldname = p.getCurrentName() ;
		    		
		    		if ("text".equals(fieldname)) {//detect text fieldname within tweet

		    			current = p.nextToken();

		    			String value = p.getValueAsString() ;//get the resulting tweet 
		    			
		    			StringTokenizer itr = new StringTokenizer(value) ;

		    			newTweet = "";

		    			while(itr.hasMoreTokens()) {//iterate over words in the tweet 
		    						    				
		    				String word = itr.nextToken() ;// each word in a single tweet
		    						    				
		    				for (int i = 0; i < word.length(); i++ ){
		    					
		    					if(word.charAt(i) == '@'){
		    						
		    						break; //break the for loop
		    					
		    					}else{
		    						
		    						if (newTweet.equals("")){//for the initial word 
		    							newTweet = word; 
		    							break; //break for not to replicate words
		    						}else{
		    							
		    						newTweet += (" " + word) ;//keep the word in the tweet
		    						break;//break for not to replicate words
		    						}
		    						
		    					}//end if
		    					
		    				}//end for 
		    			
		    			
		    			}//end while	
		    			
		    			g.writeStartObject(); 
		    			g.writeStringField("text", newTweet);
		    			g.writeEndObject();
		    			
		    			
		    		 }//end if 
		    
		    	}//end while 
		    			
		    	//will not complete unless closed 
		    	g.close(); // close generator 
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



/*	CONSIDER INTERNAL OBJECTS AND DETECT
while (p.nextToken() != JsonToken.END_OBJECT) { //loop through all the fields 
  
	  String fieldname = p.getCurrentName();
	  System.out.println(fieldname) ;

	  current = p.nextToken();
	  
	  if (current == JsonToken.START_OBJECT){
		  		    	
		  while(p.nextToken() != JsonToken.END_OBJECT){
			  //do nothing 
		  }
		
	  }//end internal object checkers 
	  

}//end tweet object 
*/