package ru.avisprof.accounting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.categories.Category;

/**
 * Created by Leonid on 20.04.2016.
 */
public class DbHelper extends SQLiteOpenHelper{

    private Context context;

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "database";

    //таблица категорий
    public static final String TABLE_CATEGORIES = "categories";
    public static final String KEY_CATEGORIES_ID = "id";
    public static final String KEY_CATEGORIES_NAME = "name";
    public static final String KEY_CATEGORIES_ICON = "icon";

    //таблица записей
    public static final String TABLE_RECORDS = "records";
    public static final String KEY_RECORDS_ID = "id";
    public static final String KEY_RECORDS_CATEGORY_ID = "category"; // references TABLE_CATEGORIES (KEY_CATEGORIES_ID)
    public static final String KEY_RECORDS_DATE = "date";
    public static final String KEY_RECORDS_DESCRIPTION = "description";
    public static final String KEY_RECORDS_SUM = "sum";



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTableCategories(db);
        createTableRecords(db);

        //и добавим предопределенные категории
        addPredefinedCategories(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);

        onCreate(db);

    }

    private void createTableCategories(SQLiteDatabase db) {

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES+ "("
                + KEY_CATEGORIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORIES_NAME + " TEXT, "
                + KEY_CATEGORIES_ICON + " INTEGER" +")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

    }


    private void createTableRecords(SQLiteDatabase db) {

        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_RECORDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_RECORDS_CATEGORY_ID + " INTEGER REFERENCES "+TABLE_CATEGORIES+"("+KEY_CATEGORIES_ID+"),"
                + KEY_RECORDS_DATE + " INTEGER,"
                + KEY_RECORDS_SUM + " REAL,"
                + KEY_RECORDS_DESCRIPTION + " TEXT" + ")";

        db.execSQL(CREATE_RECORDS_TABLE);
    }

    private void addPredefinedCategories(SQLiteDatabase db) {

        //addCategory(db, "Авто Бензин", "fuel");
       // addCategory(db, "Авто Прочее", "car");
        addCategory(db, "Еда", "buy");
        addCategory(db, "Фрукты", "fruit");
        addCategory(db, "Мясо", "meat");
        addCategory(db, "Напитки", "drinks");
        //addCategory(db, "Интернет", "inet");
        addCategory(db, "Одежда, обувь...", "clothes");
        addCategory(db, "Подарки", "gift");
        addCategory(db, "Прочее", "other");
        addCategory(db, "Развлечения", "fun");
        addCategory(db, "Телефон", "mobile");
        addCategory(db, "Транспортные расходы", "transport");


    }

    private void addCategory(SQLiteDatabase db, String name, String name_of_icon) {
        Category category = new Category();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_CATEGORIES_NAME, name);
        values.put(DbHelper.KEY_CATEGORIES_ICON, getIndexOfTypedArray(name_of_icon));
        db.insertOrThrow(DbHelper.TABLE_CATEGORIES, null, values);
    }

    private int getIndexOfTypedArray(String name_of_icon) {

        TypedArray imgs = context.getResources().obtainTypedArray(R.array.icon_ids);

        for (int i=0; i<imgs.length();i++) {
            if (imgs.getString(i).contains(name_of_icon)) {
                return  i;
            }
        }

        return  -1;

    }

}
