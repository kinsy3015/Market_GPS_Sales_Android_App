package com.example.pricetag2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication11114.R;

import org.w3c.dom.Text;

import javax.xml.validation.Validator;

public class GoodInfoActivity extends Activity {

    private TextView m_textViewGoodName;
    private TextView m_textViewDetailMean;
    private TextView m_textViewGoodPrice;
    private CheckBox m_checkBoxPlusOne;
    private CheckBox m_checkBoxDc;
    private TextView m_textViewGoodDcTerm;
    private TextView m_textViewEntpName;
    private TextView m_textViewEntpTelno;
    private TextView m_textViewEntpRoadAddr;
    private Button m_buttonCallPhone;
    private Button m_buttonGoMap;
    private String m_phoneNumber;
    private String m_xMapCoord;
    private String m_yMapCoord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodinfo);

        Intent intent = getIntent();

        InitVar(); //변수 초기화
        InitClickListener(); // 이벤트 리스너 초기화
        setIntentInfo(intent); //intent로 넘어온 정보 장착
    }

    private void InitVar() {
        m_textViewGoodName = findViewById(R.id.txt_goodName);
        m_textViewDetailMean = findViewById(R.id.txt_detailMean);
        m_textViewGoodPrice = findViewById(R.id.txt_goodPrice);
        m_checkBoxPlusOne = findViewById(R.id.checkBox_plusOne);
        m_checkBoxDc = findViewById(R.id.checkBox_Dc);
        m_textViewGoodDcTerm = findViewById(R.id.txt_goodDcTerm);
        m_textViewEntpName = findViewById(R.id.txt_entpName);
        m_textViewEntpTelno = findViewById(R.id.txt_entpTelno);
        m_textViewEntpRoadAddr = findViewById(R.id.txt_entpRoadAddr);
        m_buttonCallPhone = findViewById(R.id.btn_callPhone);
        m_buttonGoMap = findViewById(R.id.button_go_map);
        m_phoneNumber = new String();
        m_xMapCoord = new String();
        m_yMapCoord = new String();
    }

    private void setIntentInfo(Intent intent) {
        m_textViewGoodName.setText(intent.getStringExtra("goodName"));
        m_textViewDetailMean.setText(intent.getStringExtra("detailMean"));
        m_textViewGoodPrice.setText(intent.getStringExtra("goodPrice"));
        m_checkBoxPlusOne.setChecked(intent.getBooleanExtra("plusOneYn", false));
        m_checkBoxDc.setChecked(intent.getBooleanExtra("goodDcYn", false));

        long goodDcStartDay = intent.getLongExtra("goodDcStartDay", 0);
        if (goodDcStartDay != 0) {
            long startYear = goodDcStartDay / 10000;
            long startMonth = (goodDcStartDay - startYear * 10000) / 100;
            long startDay = goodDcStartDay % 100;

            long goodDcEndDay = intent.getLongExtra("goodDcEndDay", 0);
            long endYear = goodDcEndDay / 10000;
            long endMonth = (goodDcEndDay - endYear * 10000) / 100;
            long endDay = goodDcEndDay % 100;

            m_textViewGoodDcTerm.setText("이벤트 기간 : " + startYear + "-" + startMonth + "-" + startDay + " ~ " + endYear + "-" + endMonth + "-" + endDay);
        } else
            m_textViewGoodDcTerm.setVisibility(View.INVISIBLE);

        m_textViewEntpName.setText(intent.getStringExtra("entpName"));
        if (intent.getStringExtra("roadAddrDetail").contains("등록되지"))
            m_textViewEntpRoadAddr.setText(intent.getStringExtra("roadAddrBasic"));
        else
            m_textViewEntpRoadAddr.setText(intent.getStringExtra("roadAddrBasic") + " " + intent.getStringExtra("roadAddrDetail"));

        m_xMapCoord = intent.getStringExtra("xMapCoord");
        m_yMapCoord = intent.getStringExtra("yMapCoord");

        m_textViewEntpTelno.setText(intent.getStringExtra("entpTelno"));
        if(intent.getStringExtra("entpTelno").contains("등록되지")){
            m_buttonCallPhone.setClickable(false);
        }
        else
            m_buttonCallPhone.setClickable(true);

        m_phoneNumber = intent.getStringExtra("entpTelno");
    }

    private void InitClickListener() {
        m_buttonCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] phoneNumber = m_phoneNumber.split("-");
                String tel = "tel:" + phoneNumber[0] + phoneNumber[1] + phoneNumber[2];
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(tel));
                startActivity(intent);
            }
        });

        m_buttonGoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodInfoActivity.this, SplashActivity.class);
                intent.putExtra("xMapCoord", m_xMapCoord);
                intent.putExtra("yMapCoord", m_yMapCoord);
                startActivity(intent);
            }
        });

    }
}
