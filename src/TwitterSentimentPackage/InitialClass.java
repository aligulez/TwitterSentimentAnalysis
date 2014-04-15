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
		    	String createdAt = new String() ;//for writing "created_at" field in JSON File  
		    	int favoriteCount = 0; ;
		    	int retweetCount = 0 ;
		    	int followersCount = 0 ;
		    	
		    	//check if initial object is a start of the tweet object
		    	if (current != JsonToken.START_OBJECT){
		    		
		    		System.out.println("Error: initial char not start of object!");
		    		current = p.nextToken();//move to next token. 
		    		
		    	}//end if
	    			    			    	
		    	while(p.hasCurrentToken()== true){
		    				    		
		    		current = p.nextToken() ; 
		    		String fieldname = p.getCurrentName() ;			    

//DETECT "created_at" field
		    		if("created_at".equals(fieldname)){
		    			
		    			current = p.nextToken() ;//move to value field 
		    			createdAt = p.getValueAsString() ;
		    		
		    		}//end if
		    		
		    
		    		if ("id_str".equals(fieldname)) {//First recognize "id_str" for recognizing the right "text" field.
		    			
		    			while(!("text".equals(fieldname))){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName();
		    				
		    			}//end while
		    			
//DETECT "text" field
		    			if("text".equals(fieldname)){//detect text fieldname within tweet
		    		
		    			current = p.nextToken();

		    			String value = p.getValueAsString() ;//get the resulting tweet 
		    			
		    			StringTokenizer itr = new StringTokenizer(value) ;
		    			
		    			newTwt = "" ;
		    			
// PRE-PROCESSING THE "TEXT" : TWEET
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
		    		
//DETECT "followers_count" field
		    			//move to user Object Field. 
		    			while(!("user".equals(fieldname))){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName() ;
		    				
		    			}
		    			current = p.nextToken() ;
		    			
		    			if(current == JsonToken.START_OBJECT){//detect user object start 
		    				
		    				while(!("followers_count".equals(fieldname))){//move to followers_count within User object. 
			    				current = p.nextToken();		    				
			    				fieldname = p.getCurrentName() ;
			    			}
		    				
		    				p.nextToken() ;//move to value field of "followers_count".
		    				followersCount = p.getValueAsInt() ;
		    			}
		    			
//DETECT "retweet_count" field
		    			while(!("retweet_count".equals(fieldname))){//skip until reaching "retweet_count" field.
		    						    						    				
		    				if(p.hasCurrentToken() == true){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName(); } else {break ;}
		    				
		    			}//end while
		    			 
		    			current = p.nextToken() ;//move by 1 token to get to value field of "retweet_count" field. 
		    			fieldname = p.getCurrentName();
		    			
		    			if("retweet_count".equals(fieldname)){		    				
		    				retweetCount = p.getValueAsInt() ;		    				
		    			}
		    			
		    			current = p.nextToken() ;//move by 1 token to get to "favorite_count" field. 
		    			fieldname = p.getCurrentName();

//DETECT "favorite_count" field
		    			if("favorite_count".equals(fieldname)){
		    				
		    				current = p.nextToken() ;
		    				favoriteCount = p.getValueAsInt();//
		    			}
		    			
		    			while(!("filter_level".equals(fieldname))){//skip until the end of object where next field is "lang".
		    				
		    				if(p.hasCurrentToken() == true){
		    				current = p.nextToken() ;
		    				fieldname = p.getCurrentName(); } else {break ;}
		    				
		    			}//end while
		    			 
		    			    			
		    			
		    			
		    		}//end internal if	
		    	 }//end if 

//DETECT "lang" field
		    		//detect tweet language, only print if its in English. 
		    		if ("lang".equals(fieldname)){

		    			current = p.nextToken();
		    			
		    			String Valyu = p.getValueAsString() ;
		    			
		    			if (Valyu.equals("en")){
		    					
//WRITE TO FILE 
		    				//f0.write(newTwt + newLine); UNCOMMENT FOR WRITING TO TEXT FILE
		    				g.writeStartObject(); 
		    				g.writeStringField("created_at", createdAt);
		    				g.writeStringField("text", newTwt);
		    				g.writeNumberField("favorite_count", favoriteCount);
		    				g.writeNumberField("followers_count", followersCount) ;
		    				g.writeNumberField("retweet_count", retweetCount) ;
		    				g.writeEndObject();
		    				System.out.println(followersCount + "-----" + retweetCount + "-----" + favoriteCount + "------" + createdAt) ;
		    				
		    				//reset fields. 
		    				newTwt = "" ;	
		    				createdAt = "" ;
		    				favoriteCount = 0 ; 
		    				retweetCount = 0 ;
		    				followersCount = 0 ;
		    				
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
