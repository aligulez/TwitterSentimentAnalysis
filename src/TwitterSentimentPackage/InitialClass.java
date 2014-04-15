package TwitterSentimentPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
		    	File file = new File("/Users/aligulez/Desktop/IRDMGroup/exampletweets_2.txt") ;
		    	InputStream inputStream = new FileInputStream(file) ;		    	
		    	JsonParser p = f.createJsonParser(inputStream) ;// p is the parser
		    	JsonGenerator g = f.createJsonGenerator(new File("/Users/aligulez/Desktop/IRDMGroup/output_data.json"), JsonEncoding.UTF8);// g is the generator
		    	//UNCOMMENT BELOW FOR WRITING TO TEXT FILE
		    	//FileWriter f0 = new FileWriter("/Users/aligulez/Desktop/IRDMGroup/output.txt");
		    	//String newLine = System.getProperty("line.separator");

		    	JsonToken current; 
		    	
		    	current = p.nextToken();
		    	
		    	String newTwt = new String() ;//for writing the pre-processed tweet 
		    	 
		    	
		    	//check if initial object is a start of the tweet object
		    	if (current != JsonToken.START_OBJECT){
		    		
		    		System.out.println("Error: initial char not start of object!");
		    		current = p.nextToken();//move to next token. 
		    		
		    	}//end if
	    			    			    	
		    	while(p.hasCurrentToken()== true){
		    				    		
		    		current = p.nextToken() ; 
		    		String fieldname = p.getCurrentName() ;			    
		    				    		
		    		
		    		//pre-processing the "text"
		    		if ("id_str".equals(fieldname)) {//First recognize "id_str" for recognizing the right "text" field.
		    			
		    			while(!("text".equals(fieldname))){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName();
		    				
		    			}//end while
		    			
		    			if("text".equals(fieldname)){//detect text fieldname within tweet
		    		
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
		    			    
		    				if(word.charAt(0) == '@'){//for removing usernames 
		    					//do nothing
		    				} else if(matLink1.find() || matLink2.find()){ 		    			    
		    					//do nothing 
		    				}else{						
		    					
		    					//detect and remove punctuation within "text"			    			   
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
		    				
		    				if(p.hasCurrentToken() == true){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName(); } else {break ;}
		    				
		    			}//end while
		    			
		    		}//end internal if	
		    	 }//end if 

		    		//detect tweet language, only print if its in English. 
		    		if ("lang".equals(fieldname)){

		    			current = p.nextToken();
		    			
		    			String Valyu = p.getValueAsString() ;
		    			
		    			if (Valyu.equals("en")){
		    						    				
		    				//f0.write(newTwt + newLine); UNCOMMENT FOR WRITING TO TEXT FILE
		    				g.writeStartObject(); 
		    				g.writeStringField("text", newTwt);
		    				g.writeEndObject();
		    				System.out.println(newTwt) ;
		    				newTwt = "" ;		    						    			
		    			}//end if 		 
		    			
		    		}//end if language checker 
	    		
		    				
		    	}//end while 
		    	
		    
		    	//will not complete unless closed 
		    	//f0.close(); UNCOMMENT FOR WRITING TO TEXT FILE
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
