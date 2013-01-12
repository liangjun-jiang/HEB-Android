package com.ljsportapps.heb.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.util.Log;

public class HebPlace {
	public static String queryPlace(Location loc){
		StringBuilder builder = new StringBuilder();
    	HttpClient client = new DefaultHttpClient();
    	String url;
    	
 		try{
 			url = "https://maps.googleapis.com/maps/api/place/search/json?location="+Double.toString(loc.getLatitude())+","+Double.toString(loc.getLongitude())+"&radius=5000&types=grocery_or_supermarket&name=H-E-B&sensor=false&key=AIzaSyDI9oKyroNMwBTCSWEoSgVfrKtvQ10S3jw";
			//Log.d("HebPlace",url);
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse response = client.execute(httpGet);
    		StatusLine statusLine = response.getStatusLine();
    		int statusCode = statusLine.getStatusCode();
    		if (statusCode == 200){
    			HttpEntity entity = response.getEntity();
    			InputStream content = entity.getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			String line;
    			while((line=reader.readLine())!=null){
    				builder.append(line);
    			}
    		} else{
    			Log.e(HebPlace.class.toString(), "Failed to download file");
    		}
    	} catch(ClientProtocolException e){
    		e.printStackTrace();
    	} catch (IOException e){
    		e.printStackTrace();
    	} 
    	
    	return builder.toString();
	}

}
