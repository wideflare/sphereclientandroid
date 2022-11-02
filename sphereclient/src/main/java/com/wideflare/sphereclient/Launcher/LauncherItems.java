package com.wideflare.sphereclient.Launcher;


public  class LauncherItems {
    public  String itemName;
    public int actionType;
    public  String action;
    public String itemImage;

    public LauncherItems(String itemName, int actionType , String action , String thumbnail){
        this.itemName = itemName;
        this.actionType = actionType;
        this.action = action;
        this.itemImage = thumbnail;
    }
}