package com.ljsportapps.heb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	private TextView title;
	private ImageView vegImgView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		title = (TextView)findViewById(R.id.title);
		title.setText(R.string.about_string);
		
		vegImgView = (ImageView)findViewById(R.id.imageView1);
		vegImgView.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	Intent i = new Intent(getApplicationContext(),Main.class);
		    	startActivity(i);
		    }
		 });
	}

}
