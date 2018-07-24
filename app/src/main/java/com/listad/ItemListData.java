package com.listad;

public class ItemListData {
    private String img_url;
    private String name;
    private String type1_price;
    private String type2_price;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1_price() {
        return type1_price;
    }

    public void setType1_price(String type1_price) {
        this.type1_price = type1_price;
    }

    public String getType2_price() {
        return type2_price;
    }

    public void setType2_price(String type2_price) {
        this.type2_price = type2_price;
    }

    public ItemListData(String img_url, String name, String type1_price, String type2_price) {

        this.img_url = img_url;
        this.name = name;
        this.type1_price = type1_price;
        this.type2_price = type2_price;
    }
}
