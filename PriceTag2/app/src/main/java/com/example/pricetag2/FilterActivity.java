package com.example.pricetag2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication11114.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class FilterActivity extends Activity {

    private ListView m_listViewFilter;
    private FilterListAdapter adapter;
    private boolean itemFlag = false;
    private boolean shopFlag = false;
    private boolean regionFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        adapter = new FilterListAdapter();
        adapter.addButtonItem("지역");
        adapter.addButtonItem("판매점 종류");
        adapter.addButtonItem("상품 종류");
        m_listViewFilter = (ListView) findViewById(R.id.listView_filter);
        m_listViewFilter.setAdapter(adapter);
    }

    public void onFilterButtonClicked(View view){ //필터 상위항목 버튼 클릭 이벤트

        TextView textView = (TextView)view;
        switch (textView.getText().toString()) {
            case "상품 종류":
                if(itemFlag == true) {
                    itemFlag = false;
                    InitScreen();
                }
                else
                    SelectItem();
                break;
            case "지역":
                if(regionFlag == true) {
                    regionFlag = false;
                    InitScreen();
                }
                else
                    SelectRegion();
                break;
            case "판매점 종류":
                if(shopFlag == true) {
                    shopFlag = false;
                    InitScreen();
                }
                else
                    SelectShop();
                break;
        }
    }

    public void onFilterDetailClicked(View view){ //필터 하위항목 버튼 클릭 이벤트
        TextView textView = (TextView) view;
        Intent intent = new Intent();
        intent.putExtra("filter", textView.getText().toString());

        setResult(MainActivity.RESULT_CODE_FILTERACTIVITY, intent);
        finish();
    }

    private void SelectRegion(){
        adapter.ClearList();
        adapter.notifyDataSetChanged();

        adapter.addButtonItem("지역");
        adapter.notifyDataSetChanged();

        adapter.addDetailItem("서울특별시");
        adapter.addDetailItem("광주광역시");
        adapter.addDetailItem("대구광역시");
        adapter.addDetailItem("대전광역시");
        adapter.addDetailItem("부산광역시");
        adapter.addDetailItem("울산광역시");
        adapter.addDetailItem("인천광역시");
        adapter.addDetailItem("강원도");
        adapter.addDetailItem("경기도");
        adapter.addDetailItem("경상남도");
        adapter.addDetailItem("경상북도");
        adapter.addDetailItem("전라남도");
        adapter.addDetailItem("전라북도");
        adapter.addDetailItem("충청남도");
        adapter.addDetailItem("충청북도");
        adapter.addDetailItem("제주도");
        adapter.addDetailItem("세종특별자치시");

        adapter.addButtonItem("판매점 종류");
        adapter.addButtonItem("상품 종류");

        adapter.notifyDataSetChanged();

        regionFlag = true;
    }

    private void SelectItem(){
        adapter.ClearList();
        adapter.notifyDataSetChanged();

        adapter.addButtonItem("지역");
        adapter.addButtonItem("판매점 종류");
        adapter.addButtonItem("상품 종류");

        adapter.addDetailItem("농축수산물");
        adapter.addDetailItem("가공식품");
        adapter.addDetailItem("일반공산품");

        adapter.notifyDataSetChanged();

        itemFlag = true;
    }

    private void SelectShop(){
        adapter.ClearList();
        adapter.notifyDataSetChanged();

        adapter.addButtonItem("지역");
        adapter.addButtonItem("판매점 종류");

        adapter.addDetailItem("편의점");
        adapter.addDetailItem("백화점");
        adapter.addDetailItem("대형마트");
        adapter.addDetailItem("제조사");
        adapter.addDetailItem("슈퍼마켓");
        adapter.addDetailItem("전통시장");

        adapter.addButtonItem("상품 종류");

        adapter.notifyDataSetChanged();

        shopFlag = true;
    }

    private void InitScreen()
    {
        adapter.ClearList();
        adapter.notifyDataSetChanged();

        adapter.addButtonItem("지역");
        adapter.addButtonItem("판매점 종류");
        adapter.addButtonItem("상품 종류");

        adapter.notifyDataSetChanged();
    }




}
