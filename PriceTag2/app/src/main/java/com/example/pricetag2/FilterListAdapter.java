package com.example.pricetag2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication11114.R;

import java.util.ArrayList;

public class FilterListAdapter extends BaseAdapter {

    private ArrayList<FilterData> filterDatalist = new ArrayList<FilterData>();
    private static final int TYPE_BUTTON = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_MAX = 2;

    public static class FilterViewHolder {
        TextView textViewItemName;

        public void bind(String itemName){
            textViewItemName.setText(itemName);
        }
    }

    public void ClearList() {
        filterDatalist.clear();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX;
    }

    @Override
    public int getItemViewType(int position) {
        return filterDatalist.get(position).getM_type();
    }

    @Override
    public int getCount() {
        return filterDatalist.size();
    }

    @Override
    public Object getItem(int position) {
        return filterDatalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterViewHolder filterViewHolder;
        Context context = parent.getContext();
        int viewType = getItemViewType(position);

        if (convertView == null){

            FilterData filterData = filterDatalist.get(position);
            filterViewHolder = new FilterViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (viewType)
            {
                case TYPE_BUTTON:
                    convertView = inflater.inflate(R.layout.filterbutton, parent, false);
                    filterViewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.txt_filterButton);
                    break;
                case TYPE_DETAIL:
                    convertView = inflater.inflate(R.layout.filter_detail, parent, false);
                    filterViewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.txt_filterDetail);
                    break;
            }


            filterViewHolder.bind(filterData.getItemName());
            convertView.setTag(filterViewHolder);
        }
        else {
            filterViewHolder = (FilterViewHolder) convertView.getTag();
        }

        filterViewHolder.bind(filterDatalist.get(position).getItemName());

        return convertView;
    }

    public void addDetailItem(String itemName) {
        FilterData filterData = new FilterData();

        filterData.setM_type(TYPE_DETAIL);
        filterData.setItemName(itemName);

        filterDatalist.add(filterData);
    }

    public void addButtonItem(String itemName) {
        FilterData filterData = new FilterData();

        filterData.setM_type(TYPE_BUTTON);
        filterData.setItemName(itemName);

        filterDatalist.add(filterData);
    }
}
