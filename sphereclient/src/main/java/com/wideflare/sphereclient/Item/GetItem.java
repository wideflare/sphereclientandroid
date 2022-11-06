package com.wideflare.sphereclient.Item;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface GetItem {
    public void onResult(String appName , String appIcon  , String itemName , JSONObject extras  , String body , String itemCategoryName , JSONObject itemImages , String itemImage);
    public void onLoading();
    public void onLoadfinished();
    public void onAnnouncement(String announcementBody);
    public void onAppLocation(String appLocation , double latitude , double longitude );
    public void onHomeCover(String cover);
    public void onError(VolleyError error);
    public void onUnderConstruction();
    public void onNotActive();
    public void onNotExist();
    public void onNotFound();
    public void onNotAcceptable();
    public void onBadRequest();
}