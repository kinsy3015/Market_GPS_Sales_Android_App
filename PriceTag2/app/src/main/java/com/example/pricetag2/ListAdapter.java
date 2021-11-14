package com.example.pricetag2;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myapplication11114.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    private ArrayList<ItemData> m_itemDataList = new ArrayList<ItemData>();

    public static class ListViewHolder {
        TextView textView_itemName;
        TextView textVIew_price;
        TextView textView_entpPosition;
        CheckBox checkBox_DcYn;
        CheckBox checkBox_plusOneYn;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(String itemName, String itemId, int itemPrice, String roadAddrBasic, String entpId, boolean DcYn, boolean plusOneYn){

            String goodPrice = itemPrice + "원";

            textView_itemName.setText(itemName);
            textView_itemName.setTooltipText(itemId);
            textVIew_price.setText(goodPrice);
            textView_entpPosition.setText(roadAddrBasic);
            textView_entpPosition.setTooltipText(entpId);

            if (DcYn)
                checkBox_DcYn.setChecked(true);
            else
                checkBox_DcYn.setChecked(false);

            if (plusOneYn)
                checkBox_plusOneYn.setChecked(true);
            else
                checkBox_plusOneYn.setChecked(false);
        }


    }

    public void ClearList(){
        m_itemDataList.clear();
    }

    public void addItem(ItemData itemData) {
        m_itemDataList.add(itemData);
    }

    @Override
    public int getCount()
    {
        return m_itemDataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_itemDataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ListViewHolder listViewHolder;

        if (convertView == null) {
            ItemData itemData = m_itemDataList.get(position);
            listViewHolder = new ListViewHolder();
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.search_click, parent, false);

            listViewHolder.textView_itemName = (TextView) convertView.findViewById(R.id.textView_itemName);
            listViewHolder.textVIew_price = (TextView) convertView.findViewById(R.id.textVIew_price);
            listViewHolder.textView_entpPosition = (TextView) convertView.findViewById(R.id.textView_entpPosition);
            listViewHolder.checkBox_DcYn = (CheckBox) convertView.findViewById(R.id.checkbox_searchClickDcYn);
            listViewHolder.checkBox_plusOneYn = (CheckBox) convertView.findViewById(R.id.checkbox_searchClickPlusOneYn);


            convertView.setTag(listViewHolder);
        }
        else
            listViewHolder = (ListViewHolder)convertView.getTag();

        listViewHolder.bind(m_itemDataList.get(position).goodName, m_itemDataList.get(position).goodId,
                m_itemDataList.get(position).itemPrice, m_itemDataList.get(position).roadAddrBasic,
                m_itemDataList.get(position).entpId, m_itemDataList.get(position).DcYn.equals("Y"), m_itemDataList.get(position).plusoneYn.equals("Y"));
        
        return convertView;
    }
}

