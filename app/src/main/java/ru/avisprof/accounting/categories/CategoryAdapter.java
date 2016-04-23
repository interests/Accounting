package ru.avisprof.accounting.categories;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.images.GridViewActivity;
import ru.avisprof.accounting.images.ImageItem;


public class CategoryAdapter extends ArrayAdapter<Category>{
	
	private Context context;
	private ArrayList<Category> categoryList;
	private ArrayList<ImageItem> imageList;
	

	public CategoryAdapter(Context context, ArrayList<Category> categories) {
		super(context, R.layout.category_item, categories);
		this.context = context;
		this.categoryList = categories;
		imageList = GridViewActivity.getImageItems(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.category_item, parent, false);
			holder = new ViewHolder();
			holder.categoryImage = (ImageView)view.findViewById(R.id.category_image);
			holder.categoryName = (TextView)view.findViewById(R.id.category_name);
			view.setTag(holder);	
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Category category = categoryList.get(position);
		holder.categoryName.setText(category.getName());
		
		
		int imageId = category.getImageId();
		if (imageId != -1) {
			ImageItem item = imageList.get(imageId);
			holder.categoryImage.setImageBitmap(item.getImage());			
		} else {
			//holder.categoryImage.setImageBitmap(null);
		}
		
		return view;
	}
	
	static class ViewHolder {
		TextView categoryName;
		ImageView categoryImage;
	}

}
