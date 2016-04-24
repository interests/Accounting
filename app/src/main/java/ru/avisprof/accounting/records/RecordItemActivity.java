package ru.avisprof.accounting.records;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.categories.Category;
import ru.avisprof.accounting.categories.CategoryAdapter;
import ru.avisprof.accounting.images.GridViewActivity;
import ru.avisprof.accounting.images.ImageItem;
import ru.avisprof.accounting.sqlite.DbManager;
import ru.avisprof.accounting.supporting.SharedMethods;


public class RecordItemActivity extends AppCompatActivity {
	
	public static final String EXTRA_RECORD_ID = "ru.avisprof.accounting.recordId";
	private static final String TAG = "RecordItemActivity";
	
	private int mRecordId;
	private Record mRecord;	
	private Category mCategory;
	private Date mDate;

	private TextView txt_Date, txt_category;
	private EditText edt_description;
	private EditText edt_sum;
	private ImageView category_image;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_edit);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Calendar c = Calendar.getInstance();
		
		mRecordId = getIntent().getIntExtra(EXTRA_RECORD_ID, -1);
		if (mRecordId == -1) {
			return;
		}	

		mRecord = DbManager.getInstance(getApplicationContext()).getRecordById(mRecordId);
		if (mRecord == null) {
			return;
		}


		mDate = mRecord.getDate();
		mCategory = mRecord.getCategory();

		edt_sum = (EditText) findViewById(R.id.editText_sum);
		edt_sum.setText(SharedMethods.getSumString(mRecord.getSum()));

		category_image = (ImageView) findViewById(R.id.category_image);

		edt_description = (EditText)findViewById(R.id.editText_description);
		edt_description.setText(mRecord.getDescription());
		
		txt_category = (TextView)findViewById(R.id.txt_category);
		if (mCategory!=null) {
			txt_category.setText(mCategory.toString());			
		} else {
			txt_category.setText("");
		}
		
		txt_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(RecordItemActivity.this);
				builder.setTitle(R.string.choose_category);

				final ArrayList<Category> mCategories = DbManager.getInstance(getApplication()).getCategories();

				//откроем окно для выбора категории
				ArrayAdapter<Category> adapter = new CategoryAdapter(RecordItemActivity.this, mCategories);
				builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						mCategory = mCategories.get(item);
						txt_category.setText(mCategory.toString());
						updateDisplay();

					}
				});
				builder.create().show();


			}
		});
		

		txt_Date = (TextView)findViewById(R.id.txt_Date);
		txt_Date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(mDate);
				int year = calendar.get(Calendar.YEAR);
				final int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(RecordItemActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						final Calendar calendar = Calendar.getInstance();
						calendar.setTime(mDate);
						calendar.set(Calendar.YEAR, year);
						calendar.set(Calendar.MONTH, monthOfYear);
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

						mDate = calendar.getTime();
						updateDisplay();
					}
				}, year, month, day);
				dialog.show();
			}
		});

		
		updateDisplay();
	}
	

	
	private void updateDisplay() {
			
		txt_Date.setText(SharedMethods.getDateString(mDate));

		
		if (!getDescription().isEmpty()) {
			edt_description.setError(null);
		}

		int imageId = mCategory.getImageId();
		if (mCategory == null || imageId == -1) {
			category_image.setImageBitmap(null);
		} else {
			ImageItem item = GridViewActivity.getImageItems(getApplicationContext()).get(imageId);
			category_image.setImageBitmap(item.getImage());
		}
		

	}
	
	private String getDescription() {
		return edt_description.getText().toString();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
            return true;
		case R.id.menu_item_save:
			saveRecord();
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveRecord() {
		mRecord.setDate(mDate);
		mRecord.setCategory(mCategory);
		mRecord.setDescription(edt_description.getText().toString());
		mRecord.setSum(Double.valueOf(edt_sum.getText().toString()));
		DbManager.getInstance(getApplicationContext()).updateRecord(mRecord);
	}


}
