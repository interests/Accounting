package ru.avisprof.accounting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import ru.avisprof.accounting.categories.CategoriesActivity;
import ru.avisprof.accounting.categories.Category;
import ru.avisprof.accounting.categories.CategoryAdapter;
import ru.avisprof.accounting.records.Record;
import ru.avisprof.accounting.records.RecordItemActivity;
import ru.avisprof.accounting.records.RecordSevice;
import ru.avisprof.accounting.records.RecordsListViewAdapter;
import ru.avisprof.accounting.sqlite.DbManager;
import ru.avisprof.accounting.supporting.NumberEditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int DELETE_RECORD_ID = 0;
    private static final int REQUEST_ADD_ITEM = 1;
    private static final int REQUEST_VIEW_ITEM = 2;

    private ArrayList<RecordSevice> mRecords;
    private Record mCurrentRecord; // для обработки удаления записей.
    private ListView mList;
    private GetRecordsTask task;

    private Category mCurrentCategory;
    private double mCurrentSumm;
    private String mCurrentDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList = (ListView) findViewById(android.R.id.list);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordSevice recordService = mRecords.get(position);
                if (recordService.isHeader()) {
                    return;
                }

                openRecordItem(recordService.getRecordID());
            }
        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordSevice recordService = mRecords.get(position);
                if (recordService.isHeader()) {
                    return false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.question_delete_record);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbManager.getInstance(getApplicationContext()).deleteRecord(recordService.getRecordID());
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //обнулим предыдущие значения
                mCurrentCategory = null;
                mCurrentSumm = 0;
                mCurrentDescription = "";
                openChooseCategoryDialog();
            }
        });

        updateUI();
    }

    private void openRecordItem(final int recordId) {
        Intent i = new Intent(MainActivity.this, RecordItemActivity.class);
        i.putExtra(RecordItemActivity.EXTRA_RECORD_ID, recordId);
        startActivityForResult(i, REQUEST_VIEW_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            updateUI();
        }
    }

    private void openChooseCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.choose_category);

        final ArrayList<Category> mCategories = DbManager.getInstance(getApplication()).getCategories();

        //откроем окно для выбора категории
        ArrayAdapter<Category> adapter = new CategoryAdapter(MainActivity.this, mCategories);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                mCurrentCategory = mCategories.get(item);
                openChooseSumDialog();

            }
        });
        builder.create().show();

    }

    private void openChooseSumDialog() {

        final EditText summ_view = new NumberEditText(MainActivity.this);
        summ_view.setTextSize(45);
        summ_view.setGravity(Gravity.RIGHT);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.choose_sum);
        builder.setView(summ_view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mCurrentSumm = Double.valueOf(summ_view.getText().toString()).doubleValue();

                if (mCurrentSumm == 0) {
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(summ_view.getWindowToken(), 0);

                openDescriptionDialog();

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(summ_view.getWindowToken(), 0);
            }
        });
        builder.create().show();

        summ_view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }

    private void openDescriptionDialog() {

        final EditText description_view = new EditText(MainActivity.this);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.choose_description);
        builder.setView(description_view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mCurrentDescription = description_view.getText().toString();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(description_view.getWindowToken(), 0);

                addRecord();

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(description_view.getWindowToken(), 0);
            }
        });
        builder.create().show();

        description_view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }

    private void addRecord() {
        Record record = new Record();
        record.setDate(new Date());
        record.setCategory(mCurrentCategory);
        record.setSum(mCurrentSumm);
        record.setDescription(mCurrentDescription);

        int recordId = DbManager.getInstance(getApplicationContext()).addRecord(record);

        updateUI();

    }

    private void updateUI() {

        //получим записи через AsyncTask
        task = new GetRecordsTask(this);
        task.execute();

    }

    public class GetRecordsTask extends AsyncTask<Void, Void, ArrayList<RecordSevice>> {
        private Context context;

        public GetRecordsTask(Activity context) {
            this.context = context;
        }

        @Override
        protected ArrayList<RecordSevice> doInBackground(Void... params) {
            ArrayList<RecordSevice> recordsList = DbManager.getInstance(getApplicationContext()).getRecordsIDs();
            return recordsList;
        }

        @Override
        protected void onPostExecute(ArrayList<RecordSevice> recordsList) {
            mRecords = recordsList;
            if (mRecords != null) {
                mList = (ListView) findViewById(android.R.id.list);
                RecordsListViewAdapter adapter = new RecordsListViewAdapter(context, mRecords);
                mList.setAdapter(adapter);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_categories) {
            Intent i = new Intent(getApplication(), CategoriesActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
