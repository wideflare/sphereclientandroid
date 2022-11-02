package com.wideflare.sphereclient.Home;

 public  class HomeItems {
   public  String itemName;
   public int actionType;
   public  String action;
   public String itemImage;

  public HomeItems(String itemName, int actionType , String action , String thumbnail){
        this.itemName = itemName;
        this.actionType = actionType;
        this.action = action;
        this.itemImage = thumbnail;
    }
}
