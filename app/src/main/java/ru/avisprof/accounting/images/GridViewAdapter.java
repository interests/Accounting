package ru.avisprof.accounting.images;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<ImageItem> mItems = new ArrayList<ImageItem>();

	public GridViewAdapter(Context context, ArrayList<ImageItem> items) {
		mContext = context;
		mItems = items; 
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		
		ImageItem item = mItems.get(position);
		imageView.setImageBitmap(item.getImage());

		return imageView;
		
	}
	

	@Override
	public int getCount() {
		return mItems.size();
	}


	@Override
	public Object getItem(int arg0) {
		return null;
	}


	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
