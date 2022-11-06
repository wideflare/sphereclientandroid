package com.wideflare.sphereclient.Items;

import com.android.volley.VolleyError;

public interface GetItems {
    public void onResult(String appName , String appIcon  , String categoryName ,String categoryIcon  , int totalItemCount , int itemsInThisPage , int itemsPerPage , Items[] items);
    public void onLoading();
    public void onLoadfinished();
    public void onAnnouncement(String announcementBody);
    public void onAppLocation(String appLocation , double latitude , double longitude );
    public void onNextPage();
    public void onHomeCover(String cover);
    public void onEmpty(String appName, String appIcon, String categoryName, String categoryIcon);
    public void onError(VolleyError error);
    public void onUnderConstruction();
    public void onNotActive();
    public void onNotExist();
    public void onNotFound();
    public void onNotAcceptable();
    public void onBadRequest();
    public void onNoNextPage();
}
