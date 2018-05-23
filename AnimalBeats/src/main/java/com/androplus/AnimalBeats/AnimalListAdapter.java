package com.androplus.AnimalBeats;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by daimajia on 14-5-29.
 */
public class AnimalListAdapter extends BaseAdapter{
    private Context mContext;
    JSONArray mJsonArray;
    public AnimalListAdapter(Context context, JSONArray jsonArray) {
        mContext = context;
        mJsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
			return  mJsonArray.getJSONObject(position).getString("Title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = (TextView)LayoutInflater.from(mContext).inflate(R.layout.item,null);
        t.setText(getItem(position).toString());
        t.setTypeface(Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.kids_font)));
        return t;
    }
}
