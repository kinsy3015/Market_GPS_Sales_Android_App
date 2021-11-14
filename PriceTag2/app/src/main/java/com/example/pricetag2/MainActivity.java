package com.example.pricetag2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication11114.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView m_listVIew = null;
    private SearchView search_view;
    private Button m_btnFilter;
    private Button m_btnOrderByName;
    private Button m_btnOrderByPrice;
    private FirebaseDatabase m_database;
    private DatabaseReference m_dbRef;
    private ArrayList<ItemData> m_itemDataList;
    private ArrayList<ItemData> m_itemDataLstOriginal;
    private ArrayList<ItemData> m_itemDataList2000;
    private ListAdapter m_listViewAdapter;
    private boolean m_orderByPriceFlag;
    private boolean m_orderByNameFlag;
    public static final int REQUEST_CODE_FILTERACTIVITY = 1001;
    public static final int RESULT_CODE_FILTERACTIVITY = 1002;
    public static final int REQUEST_CODE_FIRSTSCREENACTIVITY = 1003;
    public static final int RESULT_CODE_FIRSTSCREENACTIVITY = 1004;
    public static final int REQUEST_CODE_LOADINGACTIVITY = 1005;
    public static final int RESULT_CODE_LOADINGACTIVITY = 1006;
    private HashMap<String, Integer> goodIdNCount;
    private HashMap<String, EntpIdData> m_entpIdMap;
    private HashMap<String, GoodIdData> m_goodIdMap;
    private HashMap<String, String> m_filterDetailMap;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search); //레이아웃 화면에 장착

        InitVar(); //변수 초기화
        InitListener(); //이벤트리스너 초기화

        Intent intent = new Intent(this, LoadingActivity.class); //로딩 액티비티 시작
        startActivityForResult(intent, REQUEST_CODE_LOADINGACTIVITY);

    }

    private void InitListView(ArrayList<ItemData> itemDataArrayList) {
// ListView, Adapter 생성 및 연결 ------------------------
        if (!m_listViewAdapter.isEmpty())
            m_listViewAdapter.ClearList();

        for (ItemData itemData : itemDataArrayList) {
            m_listViewAdapter.addItem(itemData);
        }

        m_listVIew.setAdapter(m_listViewAdapter);
        m_listViewAdapter.notifyDataSetChanged(); //리스트 뷰 아이템 바뀌었음을 알림
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSearchLayoutItemClicked(View view) { //리스트 뷰 온클릭 이벤트
        Intent intent = new Intent(MainActivity.this, GoodInfoActivity.class); //굿인포 액티비티 준비

        //쿼리문 돌릴 곳
        LinearLayout linearLayout = (LinearLayout) view;
        TextView textView = (TextView) linearLayout.getChildAt(0);
        intent.putExtra("goodName", textView.getText().toString());

        String itemId = textView.getTooltipText().toString();
        textView = (TextView) linearLayout.getChildAt(3);
        String entpId = textView.getTooltipText().toString();

        textView = (TextView) linearLayout.getChildAt(1);
        intent.putExtra("goodPrice", textView.getText().toString());

        ItemData selectedItemData = new ItemData();

        for (ItemData itemData : m_itemDataList) { //m_itemDataList 에서 itemData 하나씩 순차적으로 꺼냄
            if (itemData.goodId.equals(itemId) && itemData.entpId.equals(entpId)) {
                selectedItemData = itemData;
                break;
            }
        }

        intent.putExtra("detailMean", selectedItemData.detailMean);

        if (selectedItemData.DcYn.equals("Y"))
            intent.putExtra("goodDcYn", true);
        else
            intent.putExtra("goodDcYn", false);
        if (selectedItemData.plusoneYn.equals("Y"))
            intent.putExtra("plusOneYn", true);
        else
            intent.putExtra("plusOneYn", false);


        intent.putExtra("goodDcStartDay", selectedItemData.goodDcStartDay);
        intent.putExtra("goodDcEndDay", selectedItemData.goodDcEndDay);
        intent.putExtra("entpName", selectedItemData.entpName);
        intent.putExtra("entpTelno", selectedItemData.entpTelno);
        intent.putExtra("roadAddrBasic", selectedItemData.roadAddrBasic);
        intent.putExtra("roadAddrDetail", selectedItemData.roadAddrDetail);
        intent.putExtra("xMapCoord", selectedItemData.xMapCoord);
        intent.putExtra("yMapCoord", selectedItemData.yMapCoord);

        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void InitListener() {

        //필터 버튼 눌렀을 경우
        m_btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FILTERACTIVITY);
            }
        });

        //가격 순 정렬 버튼 클릭 시
        m_btnOrderByPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                m_orderByPriceFlag = !m_orderByPriceFlag;
                if (m_orderByPriceFlag) {
                    m_itemDataList2000.sort(new Comparator<ItemData>() {
                        @Override
                        public int compare(ItemData o1, ItemData o2) {
                            return o1.itemPrice - o2.itemPrice;
                        }
                    });
                } else {
                    m_itemDataList2000.sort(new Comparator<ItemData>() {
                        @Override
                        public int compare(ItemData o1, ItemData o2) {
                            return o2.itemPrice - o1.itemPrice;
                        }
                    });
                }

                InitListView(m_itemDataList2000);
            }
        });

        m_btnOrderByName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) { //이름 정렬 구현
                m_itemDataList2000.sort(new Comparator<ItemData>() {
                    @Override
                    public int compare(ItemData o1, ItemData o2) {
                        String left = o1.goodName;
                        String right = o2.goodName;

                        int leftLen = left.length();
                        int rightLen = right.length();
                        int minLen = Math.min(leftLen, rightLen);

                        if (m_orderByNameFlag) {
                            m_orderByNameFlag = false;
                            for (int i = 0; i < minLen; i++) {
                                byte leftChar = (byte) left.charAt(i);
                                byte rightChar = (byte) right.charAt(i);

                                Log.d("SORT", Integer.toString(leftChar - rightChar));
                                if (leftChar != rightChar) {
                                    return leftChar - rightChar;
                                }
                            }

                            return leftLen - rightLen;
                        } else {
                            m_orderByNameFlag = true;
                            for (int i = 0; i < minLen; i++) {
                                byte leftChar = (byte) left.charAt(i);
                                byte rightChar = (byte) right.charAt(i);

                                if (leftChar != rightChar) {
                                    return rightChar - leftChar;
                                }
                            }

                            return rightLen - leftLen;
                        }
                    }
                });
                InitListView(m_itemDataList2000);
            }
        });

        m_dbRef = m_database.getReference().child("pricetag-c7b1c").child("entpInfo").child("-MNC5yIY5Ea5W4fLUwrU").getRef();
        m_dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot_entpInfo : snapshot.getChildren()) {
                    EntpIdData entpIdData = new EntpIdData();

                    entpIdData.entpAreaCode = snapshot_entpInfo.child("entpAreaCode").getValue().toString();
                    entpIdData.entpName = snapshot_entpInfo.child("entpName").getValue().toString();
                    entpIdData.entpTypeCode = snapshot_entpInfo.child("entpTypeCode").getValue().toString();

                    if (snapshot_entpInfo.child("roadAddrBasic").exists()) //데이터 없는 경우 제외
                        entpIdData.roadAddrBasic = snapshot_entpInfo.child("roadAddrBasic").getValue().toString();

                    if (snapshot_entpInfo.child("roadAddrDetail").exists()) //데이터 없는 경우 제외
                        entpIdData.roadAddrDetail = snapshot_entpInfo.child("roadAddrDetail").getValue().toString();

                    m_entpIdMap.put(snapshot_entpInfo.child("entpId").getValue().toString(), entpIdData);
                }

                DatabaseReference dbRef_entpDetail = m_database.getReference().child("pricetag-c7b1c").child("entpDetail");
                dbRef_entpDetail.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (Map.Entry<String, EntpIdData> entry : m_entpIdMap.entrySet()) {
                            for (DataSnapshot snapshot_entpDetail : snapshot.child(entry.getKey()).getChildren()) {
                                EntpIdData entpIdData = entry.getValue();
                                if (!snapshot_entpDetail.child("xMapCoord").exists())
                                    continue;

                                entpIdData.xMapCoord = snapshot_entpDetail.child("xMapCoord").getValue().toString();
                                entpIdData.yMapCoord = snapshot_entpDetail.child("yMapCoord").getValue().toString();

                                if (snapshot_entpDetail.child("entpTelno").exists())
                                    entpIdData.entpTelno = snapshot_entpDetail.child("entpTelno").getValue().toString();
                                m_entpIdMap.replace(entry.getKey(), entpIdData);
                            }
                        }

                        DatabaseReference dbRef_goodInfo = m_database.getReference().child("pricetag-c7b1c").child("goodInfo").child("-MNC5xy68OcceXKYLnYM").child("item").orderByChild("goodId").getRef();

                        dbRef_goodInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot snapshot_goodId : snapshot.getChildren()) {
                                    GoodIdData goodIdData = new GoodIdData();

                                    goodIdData.goodName = snapshot_goodId.child("goodName").getValue().toString();
                                    if (snapshot_goodId.child("detailMean").exists())
                                        goodIdData.detailMean = snapshot_goodId.child("detailMean").getValue().toString();

                                    goodIdData.goodSmlclsCode = snapshot_goodId.child("goodSmlclsCode").getValue().toString();

                                    m_goodIdMap.put(snapshot_goodId.child("goodId").getValue().toString(), goodIdData);
                                }

                                DatabaseReference dbRef_goodEntpInfo;

                                for (DataSnapshot snapshot_goodId : snapshot.getChildren()) {

                                    String goodEntpInfo = "goodEntpInfo" + findNearFriday();
                                    Log.d("FRIDAY", goodEntpInfo);

                                    dbRef_goodEntpInfo = m_database.getReference().child("pricetag-c7b1c").child("goodEntpInfo20201127").child(snapshot_goodId.child("goodId").getValue().toString());
                                    dbRef_goodEntpInfo.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot snapshot_goodEntpInfoUID : snapshot.getChildren()) { //uid
                                                for (DataSnapshot snapshot_goodEntpInfo : snapshot_goodEntpInfoUID.getChildren()) { //진짜

                                                    if (!snapshot_goodEntpInfo.child("goodId").exists()) //잘못된 정보 받아온 경우 예외처리
                                                        continue;

                                                    ItemData itemData = new ItemData();
                                                    itemData.goodId = snapshot_goodEntpInfo.child("goodId").getValue().toString();
                                                    itemData.entpId = snapshot_goodEntpInfo.child("entpId").getValue().toString();

                                                    if (snapshot_goodEntpInfo.child("goodDcYn").exists())
                                                        itemData.DcYn = snapshot_goodEntpInfo.child("goodDcYn").getValue().toString();
                                                    else
                                                        itemData.DcYn = "N";

                                                    if (snapshot_goodEntpInfo.child("plusoneYn").exists())
                                                        itemData.plusoneYn = snapshot_goodEntpInfo.child("plusoneYn").getValue().toString();
                                                    else
                                                        itemData.plusoneYn = "N";

                                                    itemData.itemPrice = Integer.parseInt(snapshot_goodEntpInfo.child("goodPrice").getValue().toString());

                                                    if (!goodIdNCount.containsKey(itemData.goodId))
                                                        goodIdNCount.put(itemData.goodId, 0);
                                                    goodIdNCount.replace(itemData.goodId, goodIdNCount.get(itemData.goodId) + 1);

                                                    EntpIdData entpIdData = m_entpIdMap.get(itemData.entpId);
                                                    itemData.entpAreaCode = entpIdData.entpAreaCode;
                                                    itemData.entpName = entpIdData.entpName;
                                                    itemData.entpTypeCode = entpIdData.entpTypeCode;
                                                    itemData.roadAddrBasic = entpIdData.roadAddrBasic;
                                                    itemData.roadAddrDetail = entpIdData.roadAddrDetail;
                                                    itemData.entpTelno = entpIdData.entpTelno;
                                                    itemData.xMapCoord = entpIdData.xMapCoord;
                                                    itemData.yMapCoord = entpIdData.yMapCoord;

                                                    GoodIdData goodIdData = m_goodIdMap.get(itemData.goodId);
                                                    if (!goodIdData.detailMean.isEmpty())
                                                        itemData.detailMean = goodIdData.detailMean;

                                                    itemData.goodName = goodIdData.goodName;
                                                    itemData.goodSmlclsCode = goodIdData.goodSmlclsCode;

                                                    if (!itemData.entpTelno.contains("판매점"))
                                                        Log.d("판매점", itemData.entpName);

                                                    m_itemDataList.add(itemData);
                                                    m_itemDataLstOriginal.add(itemData);
                                                }
                                            }
                                            limit2000ItmArrayList();
                                            InitListView(m_itemDataList2000);
                                            Log.d("InitListView", "init");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d("onCancelled", "goodEntpInfo failure");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("onCancelled", "goodInfo failure");
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "entpInfo failure");
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private String findNearFriday() {
        String friday;
        Calendar calendar = Calendar.getInstance();

        friday = Integer.toString(calendar.get(Calendar.YEAR));
        friday += Integer.toString(calendar.get(Calendar.MONTH) + 1);

        if (calendar.get(Calendar.DAY_OF_WEEK) > 5) { //금요일, 토요일 경우
            friday += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) + 6 - calendar.get(Calendar.DAY_OF_WEEK));
        } else { //토요일 금요일 제외
            friday += Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) - 1 - calendar.get(Calendar.DAY_OF_WEEK));
        }
        return friday;
    }

    private void limit2000ItmArrayList() {

        int i = 0;
        m_itemDataList2000.clear();

        for (ItemData itemData : m_itemDataList) {
            if (i > 2000)
                break;

            m_itemDataList2000.add(itemData);
            i++;
        }
    }

    private void InitVar() {
        m_orderByPriceFlag = false;
        m_orderByNameFlag = false;

        m_btnFilter = findViewById(R.id.btn_filter);
        m_btnOrderByName = findViewById(R.id.btn_orderByName);
        m_btnOrderByPrice = findViewById(R.id.btn_orderByPrice);
        search_view = findViewById(R.id.search_button);

        m_listVIew = findViewById(R.id.listView_search);
        m_database = FirebaseDatabase.getInstance();
        m_database.setPersistenceEnabled(true);
        m_itemDataList = new ArrayList<ItemData>();
        m_itemDataList2000 = new ArrayList<ItemData>();
        m_itemDataLstOriginal = new ArrayList<ItemData>();

        goodIdNCount = new HashMap<String, Integer>();
        m_entpIdMap = new HashMap<String, EntpIdData>();
        m_goodIdMap = new HashMap<String, GoodIdData>();
        m_filterDetailMap = new HashMap<String, String>();
        InitFilterDetailMap();
        m_listViewAdapter = new ListAdapter();
    }

    private void InitFilterDetailMap() {
        m_filterDetailMap.put("서울특별시", "020100000");
        m_filterDetailMap.put("광주광역시", "020200000");
        m_filterDetailMap.put("대구광역시", "020300000");
        m_filterDetailMap.put("대전광역시", "020400000");
        m_filterDetailMap.put("부산광역시", "020500000");
        m_filterDetailMap.put("울산광역시", "020600000");
        m_filterDetailMap.put("인천광역시", "020700000");
        m_filterDetailMap.put("강원도", "020800000");
        m_filterDetailMap.put("경기도", "020900000");
        m_filterDetailMap.put("경상남도", "021000000");
        m_filterDetailMap.put("경상북도", "021100000");
        m_filterDetailMap.put("전라남도", "021200000");
        m_filterDetailMap.put("전라북도", "021300000");
        m_filterDetailMap.put("충청남도", "021400000");
        m_filterDetailMap.put("충청북도", "021500000");
        m_filterDetailMap.put("제주도", "021600000");
        m_filterDetailMap.put("세종특별자치시", "021700000");

        m_filterDetailMap.put("편의점", "CS");
        m_filterDetailMap.put("백화점", "DP");
        m_filterDetailMap.put("대형마트", "LM");
        m_filterDetailMap.put("제조사", "MF");
        m_filterDetailMap.put("슈퍼마켓", "SM");
        m_filterDetailMap.put("전통시장", "TR");

        m_filterDetailMap.put("농축수산물", "030100000");
        m_filterDetailMap.put("가공식품", "030200000");
        m_filterDetailMap.put("일반공산품", "030300000");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //필터 액티비티에서 클릭한 버튼 결과 가져옴
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_FILTERACTIVITY)
            if (resultCode == MainActivity.RESULT_CODE_FILTERACTIVITY) {
                Toast.makeText(getApplicationContext(), data.getExtras().getString("filter"), Toast.LENGTH_SHORT).show();

                onFilterChangeScreen(data.getExtras().getString("filter"));
            }

        if (requestCode == MainActivity.REQUEST_CODE_FIRSTSCREENACTIVITY)
            if (resultCode == MainActivity.RESULT_CODE_FIRSTSCREENACTIVITY) {
                searchData(data.getStringExtra("query"));
            }

        if (requestCode == MainActivity.REQUEST_CODE_LOADINGACTIVITY)
            if (resultCode == MainActivity.RESULT_CODE_LOADINGACTIVITY) {
                Intent intent = new Intent(this, FirstScreenActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FIRSTSCREENACTIVITY);
            }

    }

    public void searchData(String query) {
        ArrayList<ItemData> itemDataArrayList = new ArrayList<ItemData>();

        Toast.makeText(MainActivity.this, query + "-검색", Toast.LENGTH_SHORT).show();

        for (ItemData itemData : m_itemDataLstOriginal) {
            if (itemData.goodName.contains(query) || itemData.entpName.contains(query))
                itemDataArrayList.add(itemData);
        }

        if (itemDataArrayList.isEmpty()) {
            Toast.makeText(MainActivity.this, query + "에 대한 내용을 찾지 못했습니다", Toast.LENGTH_SHORT).show();
            m_itemDataList.clear();
            for (ItemData itemData : m_itemDataLstOriginal) {
                m_itemDataList.add(itemData);
            }
        } else {
            m_itemDataList.clear();
            m_itemDataList = itemDataArrayList;
        }

        limit2000ItmArrayList();
        InitListView(m_itemDataList2000);
    }

    public void onFilterChangeScreen(String filterName) {
        String filterValue = m_filterDetailMap.get(filterName);

        //어떤 상품에 속하는 필터인지 구분
        if (filterValue.length() == 2) { //판매점 종류

            ArrayList<ItemData> itemDataArrayList = new ArrayList<ItemData>();

            Toast.makeText(MainActivity.this, filterValue + "-검색", Toast.LENGTH_SHORT).show();

            for (ItemData itemData : m_itemDataList2000) {
                if (itemData.entpTypeCode.contains(filterValue))
                    itemDataArrayList.add(itemData);
            }

            if (itemDataArrayList.isEmpty()) {
                Toast.makeText(MainActivity.this, filterValue + "에 대한 내용을 찾지 못했습니다", Toast.LENGTH_SHORT).show();
                m_itemDataList.clear();
                m_itemDataList = m_itemDataList2000;
            } else {
                m_itemDataList.clear();
                m_itemDataList = itemDataArrayList;
            }
        } else if (Integer.parseInt(filterValue) > 30000000) { //식품 분류
            Log.d("onFilterChangeScreen", "식품 분류");

            ArrayList<ItemData> itemDataArrayList = new ArrayList<ItemData>();

            Toast.makeText(MainActivity.this, filterValue + "-검색", Toast.LENGTH_SHORT).show();

            for (ItemData itemData : m_itemDataList2000) {
                if (Integer.parseInt(itemData.goodSmlclsCode) % Integer.parseInt(filterValue) < 100000)
                    itemDataArrayList.add(itemData);
            }

            if (itemDataArrayList.isEmpty()) {
                Toast.makeText(MainActivity.this, filterValue + "에 대한 내용을 찾지 못했습니다", Toast.LENGTH_SHORT).show();
                m_itemDataList.clear();
                m_itemDataList = m_itemDataList2000;
            } else {
                m_itemDataList.clear();
                m_itemDataList = itemDataArrayList;
            }
        } else { //지역 분류
            Log.d("onFilterChangeScreen", "지역 분류");

            ArrayList<ItemData> itemDataArrayList = new ArrayList<ItemData>();

            Toast.makeText(MainActivity.this, filterValue + "-검색", Toast.LENGTH_SHORT).show();

            for (ItemData itemData : m_itemDataList2000) {
                if (Integer.parseInt(itemData.entpAreaCode) % Integer.parseInt(filterValue) < 100000)
                    itemDataArrayList.add(itemData);
            }

            if (itemDataArrayList.isEmpty()) {
                Toast.makeText(MainActivity.this, filterValue + "에 대한 내용을 찾지 못했습니다", Toast.LENGTH_SHORT).show();
                m_itemDataList.clear();
                m_itemDataList = m_itemDataList2000;
            } else {
                m_itemDataList.clear();
                m_itemDataList = itemDataArrayList;
            }
        }

        limit2000ItmArrayList();
        InitListView(m_itemDataList2000);
    }
}

