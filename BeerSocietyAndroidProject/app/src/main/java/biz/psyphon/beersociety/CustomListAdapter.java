package biz.psyphon.beersociety;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

/**
 * Created by Will on 12/3/2016 for BeerSociety.
 * Service class to adapt list of event items to listview
 */

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<EventItem> listData;
    private LayoutInflater mLayoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<EventItem> listData) {
        this.listData = listData;
        mLayoutInflater = LayoutInflater.from(aContext);
    }

    public ArrayList<EventItem> getListData() {
        return listData;
    }

    public void setListData(ArrayList<EventItem> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.title);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.descriptionView.setText(listData.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView descriptionView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
