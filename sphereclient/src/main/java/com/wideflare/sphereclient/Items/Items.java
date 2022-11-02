package com.wideflare.sphereclient.Items;

import org.json.JSONObject;

public class Items {
    public String itemName;
    public String itemId;
    public JSONObject extras;
    public String itemImage;

    public Items(String itemName ,String itemId ,JSONObject extras , String itemImage){
        this.itemName = itemName;
        this.itemId = itemId;
        this.extras = extras;
        this.itemImage = itemImage;
    }
}
