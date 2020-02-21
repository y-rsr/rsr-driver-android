package com.countrycodepicker;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.countrycodepicker.R.drawable;
import com.textview.BoldCustomTextView;
import com.textview.CustomTextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class CountryListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private FragmentActivity myContext;
    private List<Country> myCountriesList;
    private LayoutInflater myInflater;

    private int getResId(String drawableName) {

        try {
            Class<drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    public CountryListAdapter(FragmentActivity aContext, List<Country> aCountriesList) {
        myInflater = LayoutInflater.from(aContext);
        this.myContext = aContext;
        this.myCountriesList = aCountriesList;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = myInflater.inflate(R.layout.header, parent, false);
            holder.text = (BoldCustomTextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + myCountriesList.get(position).getName().subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    class HeaderViewHolder {
        BoldCustomTextView text;
    }

    @Override
    public long getHeaderId(int position) {
        return myCountriesList.get(position).getName().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return myCountriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = myInflater.inflate(R.layout.row, parent, false);
            holder.text = (CustomTextView) convertView.findViewById(R.id.row_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.row_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(myCountriesList.get(position).getName() + "(" + myCountriesList.get(position).getDialCode() + ")");
        String drawableName = "flag_" + myCountriesList.get(position).getCode().toLowerCase(Locale.ENGLISH);
        holder.imageView.setImageResource(getResId(drawableName));

        return convertView;
    }

    class ViewHolder {
        CustomTextView text;
        ImageView imageView;
    }
}