package com.ljsportapps.hebLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.ljsportapps.hebLite.admob.AdmobActivity;
import com.ljsportapps.hebLite.shoppinglist.ShoppingList;

public class Home extends AdmobActivity {
	
	Button locationBtn;
	Button listBtn;
	Button aboutBtn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        locationBtn = (Button)findViewById(R.id.home_btn_ads);
        
        locationBtn.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		startActivity(new Intent(getApplicationContext(),LocationList.class));
        	}
        });
        
        listBtn = (Button)findViewById(R.id.home_btn_list);
        
        listBtn.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		startActivity(new Intent(getApplicationContext(),ShoppingList.class));
        	}
        });
        
        aboutBtn = (Button)findViewById(R.id.home_btn_about);
        
        aboutBtn.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		startActivity(new Intent(getApplicationContext(),About.class));
        	}
        });
  }
	
}
