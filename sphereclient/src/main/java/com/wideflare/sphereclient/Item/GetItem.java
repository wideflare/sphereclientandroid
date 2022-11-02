package com.wideflare.sphereclient.Item;

import org.json.JSONObject;

public interface GetItem {
    public void onResult(String appName , String appIcon  , String cover , String itemName , JSONObject extras  , String body , String itemCategoryName , JSONObject itemImages , String itemImage);
    public void onLoading();
    public void onLoadfinished();
    public void onAnnouncement(String announcementBody);
    public void onAppLocation(String appLocation , double latitude , double longitude );
    public void onCover(String cover);
    public void onError();
    public void onUnderConstruction();
    public void onNotActive();
    public void onNotExist();
    public void onNotFound();
    public void onNotAcceptable();
    public void onBadRequest();
}