package TwitterSentimentPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class InitialClass {


	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		

		    JsonFactory f = new JsonFactory();
		    
		    try {
				//initialize file, reader and parser.
		    	File file = new File("/Users/aligulez/Desktop/IRDMGroup/test.txt") ;
		    	InputStream inputStream = new FileInputStream(file) ;		    	
		    	JsonParser p = f.createJsonParser(inputStream) ;// p is the parser
		    	JsonGenerator g = f.createJsonGenerator(new File("/Users/aligulez/Desktop/IRDMGroup/output_data.json"), JsonEncoding.UTF8);// g is the generator
		    	
		    	JsonToken current; 
		    	
		    	current = p.nextToken();
		    	
		    	String newTwt = new String() ;
		    	
		    	//check if initial object is a start of the tweet object
		    	if (current != JsonToken.START_OBJECT){
		    		
		    		System.out.println("Error: initial char not start of object!");
		    		current = p.nextToken();//move to next token. 
		    		
		    	}//end if
	    			    	
		    	int counter = 0 ;
		    	
		    	while(p.hasCurrentToken()== true){
		    		
		    		current = p.nextToken() ; 
		    		String fieldname = p.getCurrentName() ;
		    						 
		    		
		    		//pre-processing the "text"
		    		if ("text".equals(fieldname)) {//detect text fieldname within tweet

		    		
		    			current = p.nextToken();

		    			String value = p.getValueAsString() ;//get the resulting tweet 
		    			
		    			StringTokenizer itr = new StringTokenizer(value) ;

		    			newTwt = "" ;
		    			
		    			while(itr.hasMoreTokens()) {//iterate over words in the tweet 
		    						    				
		    				String word = itr.nextToken() ;// each word in a single tweet
		    				
		    				//Detect Web Links contained within "text"
		    			    Pattern patLink1 = Pattern.compile("http:"); 
		    			    Matcher matLink1 = patLink1.matcher(word) ;
		    			    Pattern patLink2 = Pattern.compile("https:"); 
		    			    Matcher matLink2 = patLink2.matcher(word) ;
		    			    
		    			    //detect and remove punctuation within "text"
		    			    //Pattern patPunc = Pattern.compile("[\\Q][(){},.;!?<>%\\E]") ;//   [][(){},.;!?<>%
		    			    //word = patPunc.matcher(word).replaceAll("");
		    			   
		    			    
		    				if(word.charAt(0) == '@'){//for removing usernames 
		    					//do nothing
		    				} else if(matLink1.find() || matLink2.find()){ 		    			    
		    					//do nothing 
		    					//System.out.println(mat2.group(0));
		    				}else{						
		    					
		    					word = word.replaceAll("[\\Q][(){},.:;!?<>%\\E]", "") ;
		    					
		    					if (newTwt.equals("")){//for the initial word 
		    						newTwt = word; 		    		
		    					
		    					}else{		    					
		    						newTwt += (" " + word) ;//keep the word in the tweet		    						
		    					
		    					}//end if 		    						
		    				}//end if
		    			
		    			}//end while	
		    		
		    			//skip all the fields until reaching "lang"
		    			while(!("filter_level".equals(fieldname))){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName();
		    				
		    			}
		    			
		    			
		    		 }//end if 
		    		
		    		if ("lang".equals(fieldname)){

		    			current = p.nextToken();
		    			
		    			if (p.getValueAsString().equals("en")){
		    				
		    				//counter = counter + 1 ; 
		    				g.writeStartObject(); 
		    				g.writeStringField("text", newTwt);
		    				g.writeEndObject();
		    				System.out.println(newTwt) ;
		    				newTwt = "" ;
		    				
		    			
		    			}//end if 		 
		    		
		    		
		    		}//end if language checker 
	    		
		    		//if (counter == 300){ break ;}
		    		
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

/*
if ("lang".equals(fieldname)){

	current = p.nextToken();
	
	if (p.getValueAsString().equals("en")){
		
		g.writeStartObject(); 
		g.writeStringField("text", newTweet);
		g.writeEndObject();
		System.out.println(newTweet) ;
	
	}//end if 
	
}//end if language checker 
*/