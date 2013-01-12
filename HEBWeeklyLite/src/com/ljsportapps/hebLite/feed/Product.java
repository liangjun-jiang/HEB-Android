package com.ljsportapps.hebLite.feed;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Product implements Comparable<Product>{
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	private String title;
	private String price;
	private String end_date;
	private String description;
	private URL imgLink;
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title.trim();
	}
	
	public String getPrice(){
		return price;
	}
	
	public void setPrice(String price){
		this.price = price.trim();
	}
	
	public String getEndDate(){
		return end_date;
	}
	
	public void setEndDate(String endDate){
		this.end_date = endDate.trim();
	}
	
	
	public URL getLink(){
		return imgLink;
	}

	public void setLink(String link){
		try{
			this.imgLink = new URL(link);
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description)
	{
		//this.description = "";
		this.description =  description;
	}
	
	public Product copy(){
		Product copy = new Product();
		copy.title = title;
		copy.imgLink = imgLink;
		copy.description = description;
		copy.price = price;
		copy.end_date = end_date;
		return copy;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Title: ");
		sb.append(title);
		sb.append('\n');
		sb.append("Price: ");
		sb.append(price);
		sb.append('\n');
		sb.append("Expire on: ");
		sb.append(end_date);
		
		return sb.toString();
		
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime*result + ((title == null)?0:title.hashCode());
		result = prime*result + ((description == null)?0:description.hashCode());
		result = prime*result + ((imgLink == null)?0:imgLink.hashCode());
		
		return result;
		
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (title == null){
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (imgLink == null){
			if (other.imgLink != null)
				return false;
		} else if (!imgLink.equals(other.imgLink))
			return false;
		if (price == null){
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		
		return true;
	}
	
	@Override
	public int compareTo(Product another) {
		if (another == null)  return 1;
		// sort descending, most recent first
		return another.title.compareTo(title);
	}

}
