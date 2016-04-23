package ru.avisprof.accounting.images;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import ru.avisprof.accounting.R;

public class GridViewActivity extends Activity {
	
	public static final String EXTRA_POSITION = "ru.avisprof.accounting.icon_position";
	private GridView gridView;
	private GridViewAdapter gridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view_activity);

		GridView gridView = (GridView)findViewById(R.id.gridView);
		gridAdapter = new GridViewAdapter(this, getImageItems(getApplicationContext()));
		gridView.setAdapter(gridAdapter);
		
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent i = new Intent();
				i.putExtra(EXTRA_POSITION, position);			
				setResult(RESULT_OK, i);
				finish();
				
			}
		}); 
		 
		
	}
	
	public static ArrayList<ImageItem> getImageItems(Context context) {
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		TypedArray imgs = context.getResources().obtainTypedArray(R.array.icon_ids);

		for (int i=0; i<imgs.length();i++) {
			Bitmap bitmap  = BitmapFactory.decodeResource(context.getResources(), imgs.getResourceId(i, -1));
			imageItems.add(new ImageItem(bitmap, "Image # "+i));
		}
		
		return imageItems;
	}

}
