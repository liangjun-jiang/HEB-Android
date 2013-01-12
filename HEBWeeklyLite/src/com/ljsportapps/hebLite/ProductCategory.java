package com.ljsportapps.hebLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ljsportapps.hebLite.admob.AdmobActivity;

public class ProductCategory extends AdmobActivity{
	public static String storeId="";
	private TextView title;
	private ImageView listImgview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extra=getIntent().getExtras();
		
		setContentView(R.layout.list);
		
		RelativeLayout adMobLayout = (RelativeLayout)findViewById(R.id.ads);
		fetchAdmob(adMobLayout);
        
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.category_list));
		listImgview = (ImageView)findViewById(R.id.imageView1);
		
        listImgview.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(), Home.class);
        		startActivity(i);
        	}
        });
        
		storeId = extra.getString("STOREID");
		if (storeId == null)
	    	   storeId = "96";
		
		ListView lv = (ListView)findViewById(android.R.id.list);
	    lv.setAdapter(new ArrayAdapter<String>(this,R.layout.list_row, mCat));
	    lv.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				getSelected(position);
			}
		});
	}
	
	protected void getSelected(int position) 
    {    
       Intent i = new Intent(getApplicationContext(), ProductList.class);
       String link = "http://heb.inserts2online.com/rss.jsp?drpStoreID="+storeId+"&categories=";
       switch(position){
	       case 0:
	    	   link += "all";
	    	   break;
	       case 1:
	    	   link += mCat[position]; //Baby
	    	   break;
	       case 2:
	    	   link += mCat[position]; //Bakery
	    	   break;
	       case 3:
	    	   link += "Beer%2C%20Wine%20%26%20Spirits";
	    	   break;
	       case 4:
	    	   link += "Combo%20Locos";
	    	   break;
	       case 5:
	    	   link += "Cooking%20Utensils";
	    	   break;
	       case 6:
	    	   link += mCat[position]; //Dairy
	    	   break;
	       case 7:
	    	   link += mCat[position]; //Deli
	    	   break;
	       case 8:
	    	   link += "Fish%20Market";
	    	   break;
	       case 9:
	    	   link += mCat[position]; //Floral
	    	   break;
	       case 10:
	    	   link += "Front%20Page";
	    	   break;
	       case 11:
	    	   link += mCat[position]; //Frozen
	    	   break;
	       case 12:
	    	   link += "General%2fSeasonal";
	    	   break;
	       case 13:
	    	   link += "Grilling%20Accessories";
	    	   break;
	       case 14:
	    	   link += mCat[position]; //Grills
	    	   break;	   
	       case 15:
	    	   link += mCat[position]; //Grocery
	    	   break;
	       case 16:
	    	   link += "Health%20and%20Beauty";
	    	   break;
	       case 17:
	    	   link += mCat[position]; //Household
	    	   break;
	      	
	       case 18:
	    	   link += "Meal%20Deal";
	    	   break;
	       case 19:
	    	   link += "Meat%20Market";
	    	   break;
	       case 20:
	    	   link += mCat[position];
	    	   break;
	       case 21:
	    	   link += mCat[position];
	    	   break;
	       case 22:
	    	   link += mCat[position];
	    	   break;
	    	default:
	    		link+=mCat[11];
	    		break;
       }
       i.putExtra("LINK", link);
       startActivity(i);
    }

	String[] mCat = {"All", "Baby",	 "Bakery",	 "Beer, Wine & Spirits",
			 "Combo Locos",	 "Cooking Utensils",	 "Dairy",
			 "Deli",	 "Fish Market",	 "Floral",
			 "Front Page",	 "Frozen",	 "General/Seasonal",
			 "Grilling Accessories",	 "Grills",	 "Grocery",
			 "Health and Beauty",	 "Household",	 "Meal Deal",
			 "Meat Market",	 "Pets",	 "Pharmacy",
			 "Produce"};

}
