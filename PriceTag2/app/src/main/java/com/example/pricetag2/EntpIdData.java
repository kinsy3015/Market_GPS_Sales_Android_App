package com.example.pricetag2;

public class EntpIdData {
    String entpAreaCode;
    String entpName;
    String entpTypeCode;
    String roadAddrBasic;
    String roadAddrDetail;
    String xMapCoord;
    String yMapCoord;
    String entpTelno;

    public EntpIdData() {
        entpAreaCode = "위치 정보가 등록되지 않았습니다";
        entpName = "판매점 이름이 등록되지 않았습니다";
        entpTypeCode = "판매점 종류가 등록되지 않았습니다";
        roadAddrBasic = "도로명 주소가 등록되지 않았습니다";
        roadAddrDetail = "도로명 상세 주소가 등록되지 않았습니다";
        xMapCoord = "0";
        yMapCoord = "0";
        entpTelno = "판매점 전화번호가 등록되지 않았습니다";
    }
}
