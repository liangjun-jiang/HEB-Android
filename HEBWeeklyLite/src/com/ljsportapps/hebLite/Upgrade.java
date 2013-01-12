package com.ljsportapps.hebLite;

import android.os.Bundle;
import android.widget.RelativeLayout;
import com.ljsportapps.hebLite.admob.AdmobActivity;


public class Upgrade extends AdmobActivity {
	
	RelativeLayout adMobLayout;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		RelativeLayout adMobLayout = (RelativeLayout)findViewById(R.id.ads);
	
		fetchAdmob(adMobLayout);
		
	}

}
