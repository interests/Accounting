package ru.avisprof.accounting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import ru.avisprof.accounting.categories.Category;
import ru.avisprof.accounting.records.Record;
import ru.avisprof.accounting.records.RecordSevice;
import ru.avisprof.accounting.supporting.SharedMethods;

/**
 * Created by Leonid on 20.04.2016.
 */
public class DbManager {
    private static final String TAG = "DbManager";

    private static DbManager mInstance;
    private SQLiteDatabase mDb;
    private Context mContext;
    private DbHelper mDbHelper;

    private DbManager(Context context) {
        mContext = context;
        mDbHelper = new DbHelper(context);
    }

    public static DbManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbManager(context);
        }

        return mInstance;
    }

    public void openWritable() {
        mDb = mDbHelper.getWritableDatabase();
    }

    public void openReadable() {
        mDb = mDbHelper.getReadableDatabase();
    }

    public void close() {
        mDb.close();
    }

    private boolean isCursorEmpty(Cursor cursor) {
        if (cursor==null) {
            close();
            return true;
        }

        if (cursor.getCount()==0) {
            cursor.close();
            close();
            return true;
        }

        return false;
    }

    public ArrayList<Category> getCategories() {
        openReadable();
        Cursor cursor = mDb.query(DbHelper.TABLE_CATEGORIES, null, null, null, null, null, DbHelper.KEY_CATEGORIES_NAME+" ASC");

        ArrayList<Category> list = new ArrayList<Category>();

        if (isCursorEmpty(cursor)) {
            return list;
        }

        cursor.moveToFirst();

        do {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_NAME)));
            category.setImageId(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_ICON)));

            list.add(category);
        } while (cursor.moveToNext());

        cursor.close();
        close();

        return list;
    }


    public int addCategory(Category category) {
        openWritable();

        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_CATEGORIES_NAME, category.getName());
        values.put(DbHelper.KEY_CATEGORIES_ICON, category.getImageId());

        int result = (int)mDb.insertOrThrow(DbHelper.TABLE_CATEGORIES, null, values);
        close();
        return result;
    }

    public int updateCategory(Category category) {
        openWritable();

        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_CATEGORIES_ID, category.getId());
        values.put(DbHelper.KEY_CATEGORIES_NAME, category.getName());
        values.put(DbHelper.KEY_CATEGORIES_ICON, category.getImageId());

        String where = String.format("%s=%d", DbHelper.KEY_CATEGORIES_ID, category.getId());
        int result = mDb.update(DbHelper.TABLE_CATEGORIES, values, where, null);
        close();

        return result;
    }

    public int deleteCategory(int categoryId) {

        openWritable();
        String where = String.format("%s=%d", DbHelper.KEY_CATEGORIES_ID, categoryId);
        int result = mDb.delete(DbHelper.TABLE_CATEGORIES, where, null);
        close();

        return result;
    }

    public Category getCategoryById(int categoryId) {
        if (categoryId == -1) {
            return null;
        }

        openReadable();
        String where = String.format("%s=%d", DbHelper.KEY_CATEGORIES_ID, categoryId);
        Cursor cursor = mDb.query(DbHelper.TABLE_CATEGORIES, null, where, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            return null;
        }

        cursor.moveToFirst();

        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_NAME)));
        category.setImageId(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_CATEGORIES_ICON)));

        cursor.close();
        close();

        return category;
    }



    public int addRecord(Record record) {
        openWritable();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_RECORDS_CATEGORY_ID, record.getCategoryId());
        values.put(DbHelper.KEY_RECORDS_DATE, record.getDate().getTime());
        values.put(DbHelper.KEY_RECORDS_SUM, record.getSum());
        values.put(DbHelper.KEY_RECORDS_DESCRIPTION, record.getDescription());
        int result = (int)mDb.insertOrThrow(DbHelper.TABLE_RECORDS, null, values);
        close();
        return result;
    }

    public int deleteRecord(int recordId) {
        openWritable();
        String where = String.format("%s=%d", DbHelper.KEY_RECORDS_ID, recordId);
        int result = mDb.delete(DbHelper.TABLE_RECORDS, where, null);
        close();
        return result;
    }

    public int updateRecord(Record record) {
        openWritable();

        String where = String.format("%s=%d", DbHelper.KEY_RECORDS_ID, record.getId());

        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_RECORDS_ID, record.getId());
        values.put(DbHelper.KEY_RECORDS_CATEGORY_ID, record.getCategoryId());
        values.put(DbHelper.KEY_RECORDS_DATE, record.getDate().getTime());
        values.put(DbHelper.KEY_RECORDS_SUM, record.getSum());
        values.put(DbHelper.KEY_RECORDS_DESCRIPTION, record.getDescription());

        int result = mDb.update(DbHelper.TABLE_RECORDS, values, where, null);
        close();
        return result;
    }


    public Record getRecordById(int recordId) {
        if (recordId == -1) {
            return null;
        }

        openReadable();

        String text_query =
                "SELECT r.id"+", "                        	        //0
                        + "r.category as 'categoryId'"+", "			//1
                        + "c.name as 'categoryName'"+", "			//2
                        + "c.icon as 'categoryIcon'"+", "			//3
                        + "r.date"+", "							    //4
                        + "r.sum"+", "								//5
                        + "r.description"							//6
                        + " from records r"
                        + " LEFT JOIN categories c ON r.category = c.id"
                        +" WHERE r.id = ?";

        Cursor cursor = mDb.rawQuery(text_query,
                new String[] { Integer.toString(recordId) });

        if (isCursorEmpty(cursor)) {
            return null;
        }

        cursor.moveToFirst();

        int categoryId = cursor.getInt(1);
        String categoryName = cursor.getString(2);
        int categoryImageID = cursor.getInt(3);
        Long record_date = cursor.getLong(4);
        Double record_sum = cursor.getDouble(5);
        String record_description = cursor.getString(6);

        Log.i(TAG, "getRecordById " + recordId);

        cursor.close();
        close();

        Category category = null;
        if (categoryId != -1) {
            category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            category.setImageId(categoryImageID);
        }


        Record record = new Record();
        record.setId(recordId);
        record.setCategory(category);
        record.setDate(new Date(record_date));
        record.setDescription(record_description);
        record.setSum(record_sum);

        return record;

    }

    //получим список с ID записей - это нужно для увеличения быстродействия
    //служебный класс был создан для того, чтобы вставить служебные разделители
    //т.е. мы определяем - если это новая дата, то сначала вставляем разделитель
    //а затем уже добавляем непосредственно запись
    public ArrayList<RecordSevice> getRecordsIDs() {

        String text_query = "SELECT "+DbHelper.KEY_RECORDS_ID
                +", "+DbHelper.KEY_RECORDS_DATE
                +", "+DbHelper.KEY_RECORDS_SUM
                +" FROM "+DbHelper.TABLE_RECORDS
                +" ORDER BY "+DbHelper.KEY_RECORDS_DATE+" DESC";

        openReadable();
        Cursor cursor = mDb.rawQuery(text_query, new String[]{});

        Date lastDate = null;

        ArrayList<RecordSevice> list = new ArrayList<RecordSevice>();

        if (isCursorEmpty(cursor)) {
            return list;
        }

        cursor.moveToFirst();

        double total = 0; //для подсчета итогов по расходу за день
        int group_index = 0; //индекс группы, для которой потом установим сумму расхода за день

        do {
            int recordID = cursor.getInt(0);
            long startDate = cursor.getLong(1);
            double sum = cursor.getDouble(2);

            Date currentDate = new Date(startDate);
            if (!SharedMethods.isSameDate(lastDate, currentDate)) {

                if (total != 0) {
                    //установим итог по расходу за день
                    RecordSevice oldRecordHeader = list.get(group_index);
                    oldRecordHeader.setSum(total);
                    list.set(group_index, oldRecordHeader);
                }
                total = 0; //обнулим итоги

                // добавляем разделитель
                RecordSevice recordHeader = new RecordSevice();
                recordHeader.setHeader(true);
                recordHeader.setDate(SharedMethods.getStartOfDay(startDate));
                list.add(recordHeader);

                group_index = list.size()-1;

                // и запоминаем текущую дату
                lastDate = currentDate;
            }

            RecordSevice recordService = new RecordSevice();
            recordService.setRecordID(recordID);
            recordService.setDate(startDate);
            recordService.setHeader(false);
            list.add(recordService);

            total = total + sum;

        } while (cursor.moveToNext());

        if (total != 0) {
            //установим итог по расходу за день
            RecordSevice oldRecordHeader = list.get(group_index);
            oldRecordHeader.setSum(total);
            list.set(group_index, oldRecordHeader);
        }


        cursor.close();
        close();

        return list;

    }
}
