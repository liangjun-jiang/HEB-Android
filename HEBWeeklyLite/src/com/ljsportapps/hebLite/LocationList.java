package com.ljsportapps.hebLite;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ljsportapps.hebLite.location.HebPlace;

public class LocationList extends ListActivity {
	
	private Location currentLocation;
	//private Location lastKnownLocation;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private LocationAdapter mAdapter;
	private TextView title;
	private ImageView imgView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list);
        
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.location_list));
		
        imgView = (ImageView)findViewById(R.id.imageView1);
        
        imgView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(),Home.class);
        		startActivity(i);
        	}
        });
        
        // get data generated before a config change, if it exists
		final Object data = getLastNonConfigurationInstance();
		
		mAdapter = new LocationAdapter(this, data);
		setListAdapter(mAdapter);
		
		
		mAdapter.clearPlaces();
		// Get an instance of the android system LocationManager 
		// so we can access the phone's GPS receiver
		this.locationManager = 
			(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	    TextView tv = (TextView)findViewById(android.R.id.empty);
	    tv.setText(getResources().getString(R.string.wait));
		
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		
		this.locationListener = new LocationListener()
		{
			@Override
			public void onLocationChanged(Location arg0) 
			{
				handleLocationChanged(arg0);
				
			}
		
			@Override
			public void onProviderDisabled(String arg0) {
				//Log.v("", "Disabled");
				Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(i);
			}
		
			@Override
			public void onProviderEnabled(String arg0) {
				//Log.v("", "Enabled");
				Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
			}
		
			@Override
			public void onStatusChanged(String arg0, int status, Bundle arg2) {
				switch(status){
				case LocationProvider.OUT_OF_SERVICE:
					//Log.v("", "Status changed, Out of service");
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.status1), Toast.LENGTH_SHORT).show();
					break;
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					//Log.v("","Status changed: Temporaritly Unavailables");
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.status2), Toast.LENGTH_SHORT).show();
					break;
				case LocationProvider.AVAILABLE:
					//Log.v("","Status changed: Aavailables");
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.status3), Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		// Subscribe to the location manager's updates on the current location
		this.locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
		
		
	}
	
	private void handleHEBLookup()
    {
    	if (this.currentLocation != null)
    	{
    		// Kickoff an asynchronous task to fire the reverse geocoding
    		// request off to google places
    		HEBLookUpTask task = new HEBLookUpTask();
    		task.applicationContext = this;
    		task.execute();
    	}
    	else
    	{
    		// If we don't know our location yet, we can't do reverse
    		// geocoding - display a please wait message
    		showToast("Please wait until we have a location fix from the gps");
    	}
    }
	
	private static class LocationAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		
		private List<String> mPlaces = new ArrayList<String>();
		public LocationAdapter(Context context, Object loc){
			mInflater = LayoutInflater.from(context);
		}

		
		@Override
		public View getView(int position, View convertView,
				ViewGroup paramViewGroup) {
			ViewHolder holder;
			//String cached = places.get(position);
			
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
				holder.text.setText(mPlaces.get(position));
				holder.icon.setImageResource(R.drawable.heb_red);
			
			return convertView;
			
		}
		
		@Override
		public int getCount() {
			return mPlaces.size();
		}


		@Override
		public Object getItem(int paramInt) {
			return mPlaces.get(paramInt);
		}


		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}
		
		
		 public void clearPlaces() {
	            mPlaces.clear();
	            notifyDataSetChanged();
	     }
	        
        public void addPlaces(String place) {
        	mPlaces.add(place);
            notifyDataSetChanged();
        }
		static class ViewHolder {
			TextView text;
			ImageView icon;
		}
	}
	
	private void initPlaces(String queryResult){
		if (queryResult != ""){
	        try{
	        	JSONObject jsonObj = new JSONObject(queryResult);
	    		JSONArray results = jsonObj.getJSONArray("results");
	        	mAdapter.clearPlaces();
	        	for (int i=0; i<results.length();i++){
	        		JSONObject jsonObject = results.getJSONObject(i);
	        		if (jsonObject.getString("name").equalsIgnoreCase("H-E-B"))
	        			mAdapter.addPlaces(jsonObject.getString("vicinity"));
	        	}
	        	
	        } catch (Exception e){
	        	e.printStackTrace();
	        }
		} else{
			String message = getResources().getString(R.string.remind_no_gps);
			showToast(message);
		}
	}
	private void handleLocationChanged(Location loc)
	{
		// Save the latest location
		this.currentLocation = loc;
		//lastKnownLocation = this.currentLocation;
		handleHEBLookup();
	}
	
	public class HEBLookUpTask extends AsyncTask <Void, Void, String>
	 {
	    	private ProgressDialog dialog;
	    	protected Context applicationContext;
	    	
	    	@Override
	    	protected void onPreExecute()
	    	{
	    		this.dialog = ProgressDialog.show(applicationContext, getResources().getString(R.string.wait), 
	    				getResources().getString(R.string.request), true);
	    	}
	    	
			@Override
			protected String doInBackground(Void... params) 
			{
				
				String localityName = "";
				if (currentLocation != null)
				{
					localityName = HebPlace.queryPlace(currentLocation);
				}
				
				return localityName;
			}
			
			@Override
			protected void onPostExecute(String result)
			{
				this.dialog.cancel();
				initPlaces(result);
			}
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{    
	   Intent i = new Intent(this, ProductCategory.class);
	   String storeId = "";
	   storeId = getStoreId(getStreetNumber((String)mAdapter.getItem(position)));
	   //showToast("storeId "+storeId);
	 
	   i.putExtra("STOREID", storeId);
	   startActivity(i);
}

	public void showToast(CharSequence message)
	{
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
	
	// Get the street number using regexp from Google Places JSON result
	public String getStreetNumber(String streetAdr){
		//3721 Vanderll St.
		String reg_pattern="\\d+";
		try{
			Pattern patt = Pattern.compile(reg_pattern);
			Matcher matcher = patt.matcher(streetAdr);
			if(matcher.find()){
				return matcher.group();
			} else
				return "not found";
		}catch(RuntimeException e){
			Log.e("","Error");
		}
		return null;
	}
	
	
	 // Find the Store Id from the mapping file of heb_street_store.txt
	public String getStoreId(String streetNum){
		Resources res = getResources();
		String street_num = "";
		String store_id = "96";
		try {
			InputStream in_s = res.openRawResource(R.raw.street_store);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
			String line="";
			while ((line = reader.readLine())!=null){
				StringTokenizer tokens = new StringTokenizer(line,"=");
				street_num = tokens.nextToken();
				store_id = tokens.nextToken();
				//Log.d("weekly", "street num:"+street_num);
				if(street_num.equalsIgnoreCase(streetNum)){
					break;
				}
				
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return store_id; //let storeId=1, supposed it's the headquarter store
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}
	
	
	
	
}
