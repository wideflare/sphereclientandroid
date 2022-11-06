package com.wideflare.sphereclient.Home;

import com.android.volley.VolleyError;

public  interface GetHome {
         public void onResult(String appName , String appIcon , int totalItemCount , int itemsInThisPage , int itemsPerPage , HomeItems[] items);
         public void onLoading();
         public void onLoadfinished();
         public void onPopup(String popupTitle , String popupMessage);
         public void onAnnouncement(String announcementBody);
         public void onMessage(String messageTitle , String messageBody);
         public void onAppLocation(String appLocation , double latitude , double longitude );
         public void onNextPage();
         public void onHomeCover(String cover);
         public void onEmpty(String appName , String appIcon);
         public void onError(VolleyError error);
         public void onUnderConstruction();
         public void onNotActive();
         public void onNotExist();
         public void onNotFound();
         public void onNotAcceptable();
         public void onBadRequest();
         public void onNoNextPage();
}

