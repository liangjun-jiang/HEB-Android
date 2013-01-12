

package com.ljsportapps.heb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ljsportapps.heb.coupon.CouponList;
import com.ljsportapps.heb.shoppinglist.ShoppingList;

public class Main extends Activity implements OnItemClickListener {
	ImageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        GridView g = (GridView) findViewById(R.id.myGrid);
        adapter = new ImageAdapter(this);
        g.setAdapter(adapter);
        
        g.setOnItemClickListener(this);
        
        
    }

    /**
	 * Open new activity to show the selected 
	 * full-screen in a new Activity.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		openActivity((Integer)(adapter.getItem(position)));
	}
	
    private void openActivity(int position){
    	Intent i;
    	switch(position){
    	case 0:
    		i = new Intent().setClass(this,LocationList.class);
    		startActivity(i);
    		break;
    	case 1:
    		i = new Intent().setClass(this, ShoppingList.class);
    		startActivity(i);
    		break;
    	case 2:
    		i = new Intent().setClass(this, About.class);
    		startActivity(i);
    		break;
    	default:
    		i = new Intent().setClass(this,LocationList.class);
    		startActivity(i);
    		break;
    	}
    }
    
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
		public int getCount() {
            return mThumbIds.length;
        }

        @Override
		public Object getItem(int position) {
            return position;
        }

        @Override
		public long getItemId(int position) {
            return position;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);

            return imageView;
        }

        private Context mContext;

        private Integer[] mThumbIds = {
                R.drawable.ads, 
                R.drawable.list, R.drawable.about
              
        };
    }

}
