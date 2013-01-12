package com.ljsporapps.heb.admob;

import android.app.Activity;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


public class Admob {
	private String MY_BANNER_UNIT_ID = "a14f05cc51d50c5";
	private AdView adView;
	
	public void fetchAdmob(Activity context){
		// Create the adView
	    // Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
		LinearLayout layout = new LinearLayout(null);
	    adView = new AdView(context, AdSize.BANNER, MY_BANNER_UNIT_ID);
	  
	    // Add the adView to it
	    layout.addView(adView);
	     
	    // Initiate a generic request to load it with an ad
	    AdRequest request = new AdRequest();
	    request.addTestDevice(AdRequest.TEST_EMULATOR);
	    request.addTestDevice("D9D1BE8B7EDC577819B4B64BF1A6E8F4");
	    adView.loadAd(request);   
	}
}
