package ru.avisprof.accounting.records;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import ru.avisprof.accounting.R;
import ru.avisprof.accounting.categories.Category;
import ru.avisprof.accounting.images.GridViewActivity;
import ru.avisprof.accounting.images.ImageItem;
import ru.avisprof.accounting.sqlite.DbManager;
import ru.avisprof.accounting.supporting.SharedMethods;

/**
 * Created by Leonid on 22.04.2016.
 */
public class RecordsListViewAdapter extends BaseAdapter {

    private Context mContext;
    private static final String TAG = "RecordsListViewAdapter";
    private static final int TYPES_COUNT = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_RECORD = 1;
    private ArrayList<RecordSevice> mRecordSevices;
    private ArrayList<ImageItem> mImageItems;


    public RecordsListViewAdapter(Context context, ArrayList<RecordSevice> dataSource) {
        mContext = context;
        mRecordSevices = dataSource;
        mImageItems = GridViewActivity.getImageItems(context);
   }

    @Override
    public int getViewTypeCount() {
        return TYPES_COUNT;
    }


    @Override
    public int getCount() {
        return mRecordSevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecordSevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mRecordSevices.get(position).getRecordID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordSevice recordService = mRecordSevices.get(position);


        if (recordService.isHeader()) {

            View view;

            if (convertView == null) {
                view = newHeader(mContext, parent);
            } else {
                view = convertView;
            }

            bindHeader(view, mContext, recordService.getDate());

            return view;

        } else {

            View view;

            if (convertView == null) {
                view = newView(mContext, parent);
            } else {
                view = convertView;
            }

            int recordId = recordService.getRecordID();
            bindView(view, mContext, recordId);

            return view;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (((RecordSevice)getItem(position)).isHeader()) {
            return TYPE_HEADER;
        } else {
            return TYPE_RECORD;
        }
    }

    public View newHeader(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_list_separator, parent, false);

        ViewHolderSeparator holder = new ViewHolderSeparator();
        holder.textView_dateSeparator = (TextView) view.findViewById(R.id.textView_dateSeparator);
        view.setTag(holder);

        return view;
    }

    public void bindHeader(View view, Context context, long date) {
        ViewHolderSeparator holder = (ViewHolderSeparator)view.getTag();
        if (holder!=null) {
            holder.textView_dateSeparator.setText(SharedMethods.getDateHeaderString(new Date(date)));
        }

    }

    public View newView(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_list_item, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.imageView_category = (ImageView) view.findViewById(R.id.imageView_category);
        holder.textView_category = (TextView) view.findViewById(R.id.textView_category);
        holder.textView_sum = (TextView) view.findViewById(R.id.textView_sum);
        holder.textView_descriprion =  (TextView) view.findViewById(R.id.textView_description);
        view.setTag(holder);

        return view;
    }

    public void bindView(View view, Context context, int recordId) {

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder!=null)  {

            Record record = DbManager.getInstance(context).getRecordById(recordId);
            if (record == null) {
                return;
            }
            Log.d(TAG, "record id "+recordId+ " is "+record.getDescription());

            Category category = record.getCategory();
            String category_text = category.getName();

            if (category == null) {
                holder.imageView_category.setImageBitmap(null);
            } else {
                //category_text = category_text + (category_text.equals("") ? "" : ", ") + category.toString();
                ImageItem item = mImageItems.get(category.getImageId());
                holder.imageView_category.setImageBitmap(item.getImage());
            }

            holder.textView_category.setText(category_text);
            holder.textView_descriprion.setText(record.getDescription());
            holder.textView_sum.setText(SharedMethods.getSumString(record.getSum()));
        }
    }

    static class ViewHolder {
        ImageView imageView_category;
        TextView textView_category;
        TextView textView_sum;
        TextView textView_descriprion;
    }

    static class ViewHolderSeparator {
        TextView textView_dateSeparator;
    }
}



