package com.ljsportapps.hebLite.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser implements FeedParser {
	// name of the XML tags
	static final String CHANNEL = "channel";
	static final String PUB_DATE = "pubate";
	static final String ITEM = "item";
	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String ITEM_IMAGE = "itemimage";
	static final String PRICE = "price";
	static final String END_DATE = "edate";
	
	
	private final URL feedUrl;
	
	protected BaseFeedParser(String feedUrl){
		try{
			this.feedUrl = new URL(feedUrl);
			
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getInputStream(){
		try{
			return feedUrl.openConnection().getInputStream();
		} catch(IOException e){
			throw new RuntimeException(e);
		}
	}

}
