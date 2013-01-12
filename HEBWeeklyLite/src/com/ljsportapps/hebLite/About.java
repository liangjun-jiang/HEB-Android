package com.ljsportapps.hebLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ljsportapps.hebLite.admob.AdmobActivity;

public class About extends AdmobActivity {
	private TextView title;
	private ImageView vegImgView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		RelativeLayout adMobLayout = (RelativeLayout)findViewById(R.id.ads);
		fetchAdmob(adMobLayout);
		
		title = (TextView)findViewById(R.id.title);
		title.setText(R.string.about_string);
		
		vegImgView = (ImageView)findViewById(R.id.imageView1);
		vegImgView.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	Intent i = new Intent(getApplicationContext(), Home.class);
		    	startActivity(i);
		    }
		 });
		
	}

}
