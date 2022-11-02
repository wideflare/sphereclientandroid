package com.wideflare.sphereclient.Launcher;

public interface GetLauncher {
    public void onResult(String appName , String appIcon  , String launcherName,String launcherIcon ,  int totalItemCount , int itemsInThisPage , int itemsPerPage , LauncherItems[] items);
    public void onLoading();
    public void onLoadfinished();
    public void onAnnouncement(String announcementBody);
    public void onAppLocation(String appLocation , double latitude , double longitude );
    public void onNextPage();
    public void onCover(String cover);
    public void onEmpty();
    public void onError();
    public void onUnderConstruction();
    public void onNotActive();
    public void onNotExist();
    public void onNotFound();
    public void onNotAcceptable();
    public void onBadRequest();
    public void onNoNextPage();
}