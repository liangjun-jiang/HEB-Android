package com.ljsportapps.heb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljsportapps.heb.shoppinglist.DbAdapter;
import com.ljsportapps.heb.shoppinglist.ShoppingList;

public class ProductDetail extends Activity{
	private TextView mTitle;
	private ImageView mIcon;
	private TextView mPrice;
	private TextView mMore;
	private Button mAdd;
	private TextView title;
	private ImageView listImgView;
	
	
	private String name;
    private String price;
    private String description;
    private String endDate;
    private String imgLink;
    private String largeImgLink;
    
    private DbAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.product_detail));
        
        listImgView = (ImageView)findViewById(R.id.imageView1);
        listImgView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(),ShoppingList.class);
        		startActivity(i);
        	}
        });
        
        // Open DB
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        
        // Get Extras to init data
        Bundle extra = getIntent().getExtras();
        name = extra.getString("TITLE");
        price = extra.getString("PRICE");
        description = extra.getString("DESC");
        imgLink = extra.getString("IMAGE");
        
        mTitle = (TextView)findViewById(R.id.name_id);
        mTitle.setText(name);
        
        mPrice = (TextView)findViewById(R.id.price_id);
        mPrice.setText(price);
        mMore = (TextView)findViewById(R.id.more_id);
        mMore.setText(Html.fromHtml(description));
        mIcon = (ImageView)findViewById(R.id.image_id);
        
        largeImgLink = replace(imgLink, "small", "large");
        if (urlExist(largeImgLink))
        	mIcon.setImageBitmap(downloadImage(largeImgLink));
        else
        	mIcon.setImageBitmap(downloadImage(imgLink));	
		
        mAdd = (Button)findViewById(R.id.add_id);
        mAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shopping_cart, 0, 0, 0);
        mAdd.setCompoundDrawablePadding(2);
        mAdd.setOnClickListener(new OnClickListener(){
      			@Override
			public void onClick(View arg0) {
      			addIntoShoppingList();
			}
        });
        
    }
	
	private void addIntoShoppingList(){
		mDbHelper.createNote(name, price, description, imgLink, 0);
		Toast.makeText(getApplicationContext(), "Item added.", Toast.LENGTH_SHORT).show();
	}
	// replace the "small" of  the imageLink  with "large"
	public static String replace(String str, String pattern, String replace)
	{
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		
		while ((e = str.indexOf(pattern, s)) >=0){
			result. append(str.substring(s,e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		
		return result.toString();
	}
	
	public static Bitmap downloadImage(String imgLink) 
	{
		Bitmap thumb = null;
		try{
			URL u = new URL(imgLink);
			URLConnection c = u.openConnection();
			
			c.connect();
			
			// read data
			BufferedInputStream stream = new BufferedInputStream(c.getInputStream());

			// decode the data, subsampling along the way
			thumb = BitmapFactory.decodeStream(stream);

			// close the stream
			stream.close();
		}catch (MalformedURLException e) {
			Log.e("Product Detail:", "malformed url: " + imgLink);
		} catch (IOException e) {
			Log.e("Product Detail:", "An error has occurred downloading the image: " + imgLink);
		}
		
		return thumb;
	}
	
	public static boolean urlExist(String url)
	{
		try{
			 HttpURLConnection.setFollowRedirects(false);
		      // note : you may also need
		      //        HttpURLConnection.setInstanceFollowRedirects(false)
		      HttpURLConnection con =
		         (HttpURLConnection) new URL(url).openConnection();
		      con.setRequestMethod("HEAD");
		      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (MalformedURLException e){
			return false;
			
		} catch (IOException e){
			return false;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}
	
	

}
