package com.ljsportapps.hebLite;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ljsportapps.hebLite.admob.AdmobActivity;
import com.ljsportapps.hebLite.feed.FeedParser;
import com.ljsportapps.hebLite.feed.Product;
import com.ljsportapps.hebLite.feed.XmlPullFeedParser;

public class ProductList extends AdmobActivity {
	private static List<Product> products;
	private ProductAdapter mAdapter;
	private TextView title;
	private ImageView imgView;
	private String link;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        RelativeLayout adMobLayout = (RelativeLayout)findViewById(R.id.ads);
		fetchAdmob(adMobLayout);
		
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.product_list));
        imgView = (ImageView)findViewById(R.id.imageView1);
        imgView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(), Home.class);
        		startActivity(i);
        	}
        });
        
        // get data generated before a config change, if it exists
		final Object data = getLastNonConfigurationInstance();
        
        Bundle extra = getIntent().getExtras();
        link = extra.getString("LINK");
        //Log.d("let's check the store id:",link);
        products = loadFeed(link);
        mAdapter = new ProductAdapter(this, data);
        
        TextView emptyTV = (TextView)findViewById(android.R.id.empty);
        emptyTV.setText("");
        
        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(mAdapter);
        
        lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				getSelected(arg2);
			}
        });
        
    }
	
	private static class ProductAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private Bitmap mIcon1;
		
		private class Image{
			URL url;
			Bitmap thumb;
		}
		
		// the background task object
		private LoadThumbsTask thumbnailGen;
		
		// an array of resources we want to display
		private Image[] images;
		
		public ProductAdapter(Context context, Object previousList){
			// Cache the LayoutInflate to avoid asking for a new one each time
			mInflater = LayoutInflater.from(context);
			
			// Icons bound to the rows
			mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruit);
			
			// if no pre-existing data, we need to generate it from scratch
			// initialize array
			images = new Image[products.size()];
			for(int i=0, j=products.size();i<j;i++){
				images[i] = new Image();
				images[i].url = products.get(i).getLink();
			}
			
			// get the thumbnail generation task 
			thumbnailGen = new LoadThumbsTask();
			
			// we want to use pre-existing data, if exists
			if (previousList !=null){
				images = (Image[])previousList;
				
				// continue processing remaining thumbs in the background
				thumbnailGen.execute(images);
				
				// no more
				return;
				
			}
			
			// Start the background task to generate thumbs
			thumbnailGen.execute(images);
			
		}
		
		@Override
		public int getCount() {
			return products.size();
		}
		
		@Override
		public Object getItem(int position){
			return position;
		}
		
		@Override
		public long getItemId(int position){
			return position;
		}
		
		private void cacheUpdated(){
			this.notifyDataSetChanged();
		}
		
		/**
		 * Getter: return generated data
		 * @return array of Image
		 */
		/*
		public Object getData() {
			// stop the task if it isn't finished
			if(thumbnailGen != null && thumbnailGen.getStatus() != AsyncTask.Status.FINISHED) {
				// cancel the task
				thumbnailGen.cancel(true);

			}

			// return generated thumbs
			return images;
		}
		
		*/
		public Bitmap loadThumb(URL url){
			// the download thumb
			Bitmap thumb = null;
			
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;
			
			try{
				URLConnection c = url.openConnection();
				c.connect();
				
				//Read data
				BufferedInputStream stream = new BufferedInputStream(c.getInputStream());
				
				// decode the data, subsampling along the way
				thumb = BitmapFactory.decodeStream(stream);
				
				// close the stream
				stream.close();
			} catch (MalformedURLException e){
				Log.e("PrudctList:", "malformed url: "+ url);
			} catch (IOException e){
				Log.e("ProductList: ", "An error has occured" + url);
			}
			
			return thumb;
		}
		
		// the class that will create a background thread and geenerate thumbs
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup group){
			ViewHolder holder;
			Image cached = images[position];
			
			//When convertView is not null, we can reuse it directly, there is not need 
			//to reinflate it.
			if (convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_icon_text, null);
				
				// Create a viewHolder and sotre reference to the two children views
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.text);
				holder.icon = (ImageView)convertView.findViewById(R.id.icon);
				holder.icon.setScaleType(ScaleType.FIT_CENTER);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView and
				// the ImageView
				holder = (ViewHolder)convertView.getTag();
				
			}
			if (cached.thumb == null){
				// Bind the data efficiently with the holder
				holder.text.setText(products.get(position).getTitle());
				holder.icon.setImageBitmap(mIcon1);
			
			} else {
				holder.text.setText(products.get(position).getTitle());
				holder.icon.setImageBitmap(cached.thumb);
			}
			return convertView;

			
		}
		
		static class ViewHolder {
			TextView text;
			ImageView icon;
		}
		
		private class LoadThumbsTask extends AsyncTask<Image, Void, Void>{
			/**
			 * Generate thumbs for each of the Image objects in the array
			 * passed to this method. This method is run in a background task.
			 */
			@Override
			protected Void doInBackground(Image... cache){
				// iterate over all images:
				for (Image i:cache){
					if (isCancelled()) return null;
					
					if (i.thumb != null) continue;
					
					i.thumb = loadThumb(i.url);
					
					// some unit of work has been completed, update the UI
					publishProgress();
				}
				return null;
			}
			
		
			/**
			 * Update the UI thread when requested by publishProgress()
			 */
			@Override
			protected void onProgressUpdate(Void... param){
				cacheUpdated();
			}
		
		}
	}
	
	//protected void onListItemClick(ListView l, View v, int position, long id) 
    
	private void getSelected(int position)
	{    
       Intent i = new Intent(this, ProductDetail.class);
       i.putExtra("TITLE", products.get(position).getTitle());
       i.putExtra("PRICE", products.get(position).getPrice());
       i.putExtra("DESC", products.get(position).getDescription());
       i.putExtra("ENDDATE", products.get(position).getEndDate());
       i.putExtra("IMAGE", products.get(position).getLink().toString());
       
       startActivity(i);
    }
	
	private List<Product> loadFeed(String rssUrl){
		List<Product> localProducts = new ArrayList<Product>();
		try{
			FeedParser parser = new XmlPullFeedParser(rssUrl);
			localProducts = parser.parse();
		}catch (Throwable t){
			Log.e("HEB WEEKLY", t.getMessage(),t);
		}
		return localProducts;
	}
	
	/**
	 * Preserve adapter data between orientation changes
	 * See: http://developer.android.com/reference/android/app/Activity.html#onRetainNonConfigurationInstance()
	*/
	/*
	@Override
	public Object onRetainNonConfigurationInstance() {
		return mAdapter.getData();
	}
	 */
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.refresh_option:
				//products.removeAll(products);
				
				products = loadFeed(link);
		        mAdapter = new ProductAdapter(this, products);
		        mAdapter.notifyDataSetChanged();
		        setListAdapter(mAdapter);
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	*/
	
}


