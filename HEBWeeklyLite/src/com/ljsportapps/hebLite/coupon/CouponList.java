package com.ljsportapps.hebLite.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljsportapps.hebLite.Home;
import com.ljsportapps.hebLite.R;

public class CouponList extends Activity{
	/** Called when the activity is first created. */
	//private String MY_BANNER_UNIT_ID = "a14f05cc51d50c5";
	TextView title;
	Button rightButton;
	ImageView imgView;
	LinearLayout adMobLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.coupon_list));
        
        imgView = (ImageView)findViewById(R.id.imageView1);
        imgView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(), Home.class);
        		startActivity(i);
        	}
        });
        
        TextView tx = (TextView)findViewById(android.R.id.empty);
        tx.setText(getResources().getString(R.string.upgrade));
        /*
        // Lookup R.layout.main
        adMobLayout = (LinearLayout)findViewById(R.id.admob);
          
        // Create the adView
        // Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
        AdView adView = new AdView(this, AdSize.BANNER, MY_BANNER_UNIT_ID);
      
        // Add the adView to it
        adMobLayout.addView(adView);
         
        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
        request.addTestDevice(AdRequest.TEST_EMULATOR);
	    request.addTestDevice("D9D1BE8B7EDC577819B4B64BF1A6E8F4");
	    adView.loadAd(request); 
	    */  
    }

	
}
