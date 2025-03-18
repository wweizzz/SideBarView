package com.lzj.sidebarviewdemo.bean;

import com.lzj.sidebarviewdemo.utils.PinYinStringHelper;

public class SortBean {

    private String name;
    private String address;
    private String pinyin;
    private String first;//首字母

    public SortBean(String name, String address) {
        this.address = address;
        this.name = name;
        this.pinyin = PinYinStringHelper.getPingYin(name);//全拼
        this.first = PinYinStringHelper.getAlpha(name);//大写首字母或特殊字符
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
