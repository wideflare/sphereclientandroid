package com.wideflare.sphereclient.Items;

public interface GetItemsLoadMore {
    public void onResult( int totalItemCount , int itemsInThisPage , int itemsPerPage , Items[] items);
    public void onLoading();
    public void onLoadfinished();
    public void onNextPage();
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
