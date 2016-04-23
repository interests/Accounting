package ru.avisprof.accounting.categories;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.sqlite.DbManager;

public class CategoriesActivity  extends AppCompatActivity {
	
	private static final String TAG = "CategoriesActivity";
	private ListView mList;
	private ArrayList<Category> mCategories;
	public static final int REQUEST_CODE_VIEW = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.category_main);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openCategoryActivity(-1);
			}
		});

		
		ListView mList = (ListView)findViewById(android.R.id.list);

		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Category category = mCategories.get(position);
				int categoryId = category.getId();
				openCategoryActivity(categoryId);


			}
		});

		mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Category category = mCategories.get(position);
				
				Builder builder = new Builder(CategoriesActivity.this);
				builder.setTitle(getString(R.string.question_delete_category)+" "+category.getName()+"?");
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DbManager.getInstance(getApplicationContext()).deleteCategory(category.getId());
						updateUI();		
					}
				});
				
				builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				
				builder.create().show();
				
				return true;
			}
		});
		
		
		updateUI();
		
	
	}

	private void openCategoryActivity(int categoryId) {
		Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
		i.putExtra(CategoryActivity.EXTRA_CATEGORY_ID, categoryId);
		startActivityForResult(i, REQUEST_CODE_VIEW);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_VIEW) {
			updateUI();
		}
	}
	
	private void updateUI() {
		ListView mList = (ListView)findViewById(android.R.id.list);
		mCategories = DbManager.getInstance(getApplication()).getCategories();
		ArrayAdapter<Category> adapter = new CategoryAdapter(CategoriesActivity.this, mCategories);
		mList.setAdapter(adapter);	
	}
	
	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		return super.onCreateView(parent, name, context, attrs);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			openCategoryActivity(-1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

			

}
