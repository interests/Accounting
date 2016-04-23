package ru.avisprof.accounting.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.images.GridViewActivity;
import ru.avisprof.accounting.images.ImageItem;
import ru.avisprof.accounting.sqlite.DbManager;

public class CategoryActivity extends AppCompatActivity {
	
	private static final String TAG = "CategoryActivity";
	public static final String EXTRA_CATEGORY_ID = "ru.avisprof.accounting.EXTRA_CATEGORY_ID";
	private Category mCategory;
	private static final int REQUEST_CHOOSE_IMAGE = 1;
	private int imageID;
	private ImageView imageView_icon;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_edit);
		
		imageView_icon = (ImageView)findViewById(R.id.imageView_icon);
		imageView_icon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
				startActivityForResult(i, REQUEST_CHOOSE_IMAGE);
				
			}
		});
		
		EditText edt = (EditText)findViewById(R.id.edtCategoryName);
		
		int id = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
		
		if (id != -1) {
			mCategory = DbManager.getInstance(getApplicationContext()).getCategoryById(id);
			edt.setText(mCategory.getName());
			imageID = mCategory.getImageId();
		} else {
			mCategory = null;
			imageID = -1;
		}

		updateUI();
		
	}
	
	private void updateUI() {
		
		if (imageID == -1) {
			imageView_icon.setImageBitmap(null);
		} else {
			ImageItem imageItem = GridViewActivity.getImageItems(getApplicationContext()).get(imageID);
			imageView_icon.setImageBitmap(imageItem.getImage());
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CHOOSE_IMAGE) {	
				imageID = data.getIntExtra(GridViewActivity.EXTRA_POSITION, -1);
				updateUI();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_save:
			EditText edt = (EditText)findViewById(R.id.edtCategoryName);
			String new_name = edt.getText().toString();
			if (new_name.equals("")) {
				return true;
			}

			if (mCategory==null) {
				//добавление новой категории
				mCategory = new Category();
				mCategory.setName(new_name);
				mCategory.setImageId(imageID);
				DbManager.getInstance(getApplicationContext()).addCategory(mCategory);
			} else {
				//редактирование существующей категории
				mCategory.setName(new_name);
				mCategory.setImageId(imageID);
				DbManager.getInstance(getApplicationContext()).updateCategory(mCategory);
			}

			setResult(CategoriesActivity.REQUEST_CODE_VIEW);
			finish();
			return true;
		}
		
		return true;
	}


}
