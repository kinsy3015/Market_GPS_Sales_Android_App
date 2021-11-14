package com.example.pricetag2;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ItemData
{
    public String goodName;
    public String goodId;
    public String entpId;
    public String roadAddrBasic;
    public int itemPrice;
    public String plusoneYn;
    public String DcYn;

    public String detailMean;
    public long goodDcStartDay;
    public long goodDcEndDay;
    public String entpTelno;
    public String entpName;
    public String roadAddrDetail;
    public String xMapCoord;
    public String yMapCoord;

    public String entpTypeCode;
    public String entpAreaCode;

    public String goodSmlclsCode;

    public ItemData(){
        entpTelno = "판매점 전화번호가 등록되지 않았습니다";
    }
}
