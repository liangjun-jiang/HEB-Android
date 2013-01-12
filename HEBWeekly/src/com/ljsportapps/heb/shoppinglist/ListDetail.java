
package com.ljsportapps.heb.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljsportapps.heb.LocationList;
import com.ljsportapps.heb.ProductDetail;
import com.ljsportapps.heb.R;

public class ListDetail extends Activity{
	private TextView title;
	private ImageView imgView;
	
	private TextView mTitle;
	private TextView mMore;
	private ImageView mIcon;
	private TextView mPrice;
	private Button mConfirm;
	private Long mRowId;
    private DbAdapter mDbHelper;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        
        title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.product_detail));
        imgView = (ImageView)findViewById(R.id.imageView1);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.vegbox));
        imgView.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent i = new Intent(getApplicationContext(),LocationList.class);
        		startActivity(i);
        	}
        });
        
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();

       
        mTitle = (TextView)findViewById(R.id.name_id);
        mPrice = (TextView)findViewById(R.id.price_id);
        mMore = (TextView)findViewById(R.id.more_id);
        mIcon = (ImageView)findViewById(R.id.image_id);
        mConfirm = (Button)findViewById(R.id.add_id);
        mConfirm.setVisibility(View.INVISIBLE);
    	
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DbAdapter.KEY_ROWID)
									: null;
		}

		populateFields();
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitle.setText(note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_TITLE)));
            mPrice.setText(note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_PRICE)));
            mMore.setText(Html.fromHtml(note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_MORE))));
            Bitmap icon;
            String imgLink = note.getString(
                    note.getColumnIndexOrThrow(DbAdapter.KEY_IMAGE));
            String largeImgLink = ProductDetail.replace(imgLink,"small","large");
           
            if(ProductDetail.urlExist(largeImgLink))
            	icon = ProductDetail.downloadImage(largeImgLink);
            else
            	icon = ProductDetail.downloadImage(imgLink);
            
            mIcon.setImageBitmap(icon);
        }
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

 
}
