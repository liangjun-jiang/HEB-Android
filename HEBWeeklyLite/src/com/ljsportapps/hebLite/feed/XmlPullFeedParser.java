package com.ljsportapps.hebLite.feed;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class XmlPullFeedParser extends BaseFeedParser {

	public XmlPullFeedParser(String feedUrl) {
		super(feedUrl);
	}

	@Override
	public List<Product> parse() {
		List<Product> products = null;
		XmlPullParser parser = Xml.newPullParser();
		try{
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			Product currentProduct = null;
			boolean done = false;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				String name = null;
				switch(eventType){
					case XmlPullParser.START_DOCUMENT:
						products = new ArrayList<Product>();
						break;
					case XmlPullParser.START_TAG:
						name = parser.getName();
						String temp;
						if (name.equalsIgnoreCase(ITEM)){
							currentProduct = new Product();
						} else if (currentProduct !=null){
							if (name.equalsIgnoreCase(ITEM_IMAGE)){
								temp = parser.nextText();
								currentProduct.setLink(temp);
							} else if (name.equalsIgnoreCase(DESCRIPTION)){
								temp = parser.nextText().trim();
								currentProduct.setDescription(temp);
							} else if (name.equalsIgnoreCase(PRICE)){
								temp = parser.nextText();
								currentProduct.setPrice(temp);
							}else if (name.equalsIgnoreCase(END_DATE)){
								temp = parser.nextText();
								currentProduct.setEndDate(temp);
							} else if (name.equalsIgnoreCase(TITLE)){
								temp = parser.nextText();
								currentProduct.setTitle(temp);
							} 
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(ITEM) && currentProduct !=null){
							products.add(currentProduct);
						} else if(name.equalsIgnoreCase(CHANNEL)){
							done = true;
						}
						break;
				}
				eventType = parser.next();
				
			}
			
		}catch (Exception e){
			Log.e("H-E-B weekly::PullFeedParser", e.getMessage(),e);
			throw new RuntimeException(e);
		}
		
		return products;
	}

}
