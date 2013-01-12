package com.ljsportapps.hebLite.admob;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


public class AdmobActivity extends Activity{
	private String MY_BANNER_UNIT_ID = "a14f05cc51d50c5";
	private AdView adView;
	
	protected void fetchAdmob(View root){
		// Create the adView
	    adView = new AdView(this, AdSize.BANNER, MY_BANNER_UNIT_ID);
	    
	  
	    // Add the adView to root
	    ((ViewGroup) root).addView(adView);
	     
	    // Initiate a generic request to load it with an ad
	    AdRequest request = new AdRequest();
	    request.addTestDevice(AdRequest.TEST_EMULATOR);
	    request.addTestDevice("D9D1BE8B7EDC577819B4B64BF1A6E8F4");
	    adView.loadAd(request);   
	}
}
