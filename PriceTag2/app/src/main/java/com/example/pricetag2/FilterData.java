package com.example.pricetag2;

import java.util.ArrayList;

public class FilterData {

    private String itemName;
    private int m_type; //상위항목 하위항목 구분

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setM_type(int type) {
        this.m_type = type;
    }

    public int getM_type() {
        return m_type;
    }
}
