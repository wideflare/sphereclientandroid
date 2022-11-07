package com.wideflare.sphereclient;

import android.content.Context;

import com.android.volley.VolleyError;

import com.wideflare.sphereclient.Home.GetHome;
import com.wideflare.sphereclient.Home.GetHomeLoadMore;
import com.wideflare.sphereclient.Home.HomeItems;
import com.wideflare.sphereclient.Item.GetItem;
import com.wideflare.sphereclient.Items.GetItems;
import com.wideflare.sphereclient.Items.GetItemsLoadMore;
import com.wideflare.sphereclient.Items.Items;
import com.wideflare.sphereclient.Launcher.GetLauncher;
import com.wideflare.sphereclient.Launcher.GetLauncherLoadMore;
import com.wideflare.sphereclient.Launcher.LauncherItems;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SphereClient {

    private final String APP_KEY ;
    private final Context context;
    private int homePageNumber = 1;
    private int launcherPageNumber = 1;
    private int itemsPageNumber = 1;



   public SphereClient(String APP_KEY , Context context){
        this.APP_KEY = APP_KEY;
        this.context  = context;
    }

//    public SphereClient(String APP_KEY , Context context  , String id){
//        this.APP_KEY = APP_KEY;
//        this.context  = context;
//        this.id = id;
//    }



    ///////for item

    public void getItem(GetItem getItem , String itemId) {
        getItem.onLoading();
        String url = "https://api.wideflare.com/?action=getItem&appKey="+APP_KEY+"&itemId="+itemId;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getItem.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if (code == 401) {
                        getItem.onNotExist();
                    } else if (code == 404) {
                        getItem.onNotFound();
                    } else if (code == 405) {
                        getItem.onNotActive();
                    } else if (code == 400) {
                        getItem.onBadRequest();
                    } else if (code == 406) {
                        getItem.onNotAcceptable();
                    } else if (code == 200) {

                        if (status.equals("under-construction")) {
                            getItem.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");



                        if(jobj.getJSONObject("announcement").getBoolean("status")){
                            JSONObject announcement = jobj.getJSONObject("announcement");
                            getItem.onAnnouncement(announcement.getString("announcementBody"));
                        }

                        if(info.getJSONObject("appLocation").getBoolean("status")){
                            JSONObject location = info.getJSONObject("appLocation");
                            getItem.onAppLocation(location.getString("location") , location.getDouble("lat") , location.getDouble("lon") );
                        }

                        if(!info.getString("homeCover").isEmpty()){
                            getItem.onHomeCover(info.getString("homeCover"));
                        }


                                getItem.onResult(info.getString("appName"), info.getString("appIcon"), jobj.getString("itemName"), jobj.getJSONObject("extras"), jobj.getString("body"), jobj.getString("itemCategoryName"), jobj.getJSONObject("itemImages"), jobj.getString("itemImage"));
                    }

                    }catch(JSONException e){
                        //   console.setText(e.toString());
                        e.printStackTrace();
                    }

                }

                @Override
                public void error (VolleyError error){
                    getItem.onError(error);
                }

        }
        );
    }


    ///////for items
    //loads more items belonging to this category
    public void getItemsLoadMore(GetItemsLoadMore getItemsLoadMore , String itemCategoryId){
        itemsPageNumber++;
        getItemsLoadMore.onLoading();
        String url = "https://api.wideflare.com/?action=getItems&appKey="+APP_KEY+"&page="+itemsPageNumber+"&categoryId="+itemCategoryId;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getItemsLoadMore.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if(code == 401){
                        getItemsLoadMore.onNotExist();
                    }else if(code == 404){
                        getItemsLoadMore.onNotFound();
                    }
                    else if(code == 405){
                        getItemsLoadMore.onNotActive();
                    }
                    else if(code == 400){
                        getItemsLoadMore.onBadRequest();
                    }else if(code == 406){
                        getItemsLoadMore.onNotAcceptable();
                    }else if(code == 200){

                        if(status.equals("under-construction")){
                            getItemsLoadMore.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");
                        JSONObject nextPage_ = info.getJSONObject("nextPage");




                        int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                        






                        if(nextPage_.getBoolean("status")) {
                            getItemsLoadMore.onNextPage();
                        }else {
                            getItemsLoadMore.onNoNextPage();
                        }

                        if(itemsInThisPage != 0){
                            JSONArray _items = jobj.getJSONArray("items");
                            Items items [] = new Items[itemsInThisPage];

                            for(int i=0;i<itemsInThisPage;i++){
                                items[i] = new Items(
                                        _items.getJSONObject(i).getString("itemName"),
                                        _items.getJSONObject(i).getString("itemId"),
                                        _items.getJSONObject(i).getJSONObject("extras"),
                                        _items.getJSONObject(i).getString("itemImage")
                                );

                            }
                            getItemsLoadMore.onResult(info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                        }else {
                            getItemsLoadMore.onEmpty();
                        }

                    }

                }catch (JSONException e) {
                    //   console.setText(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void error(VolleyError error) {
                getItemsLoadMore.onError(error);
            }
        });
    }

    //loads items belonging to a category
    public void getItems(GetItems getItems , String itemCategoryId){
       itemsPageNumber = 1 ;
        getItems.onLoading();
        String url = "https://api.wideflare.com/?action=getItems&appKey="+APP_KEY+"&page=1&categoryId="+itemCategoryId;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getItems.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if(code == 401){
                        getItems.onNotExist();
                    }else if(code == 404){
                        getItems.onNotFound();
                    }
                    else if(code == 405){
                        getItems.onNotActive();
                    }
                    else if(code == 400){
                        getItems.onBadRequest();
                    }else if(code == 406){
                        getItems.onNotAcceptable();
                    }else if(code == 200){

                        if(status.equals("under-construction")){
                            getItems.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");
                        JSONObject nextPage_ = info.getJSONObject("nextPage");

                        if(info.getJSONObject("appLocation").getBoolean("status")){
                            JSONObject location = info.getJSONObject("appLocation");
                            getItems.onAppLocation(location.getString("location") , location.getDouble("lat") , location.getDouble("lon") );
                        }


                        int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                        


                        if(jobj.getJSONObject("announcement").getBoolean("status")){
                            JSONObject announcement = jobj.getJSONObject("announcement");
                            getItems.onAnnouncement(announcement.getString("announcementBody"));
                        }

                        if(!info.getString("homeCover").isEmpty()){
                            getItems.onHomeCover(info.getString("homeCover"));
                        }


                        if(nextPage_.getBoolean("status")) {
                            getItems.onNextPage();
                        }else {
                            getItems.onNoNextPage();
                        }

                        if(itemsInThisPage != 0){
                            JSONArray _items = jobj.getJSONArray("items");
                            Items items [] = new Items[itemsInThisPage];

                            for(int i=0;i<itemsInThisPage;i++){
                                items[i] = new Items(
                                        _items.getJSONObject(i).getString("itemName"),
                                        _items.getJSONObject(i).getString("itemId"),
                                        _items.getJSONObject(i).getJSONObject("extras"),
                                        _items.getJSONObject(i).getString("itemImage")
                                );

                            }
                            getItems.onResult(info.getString("appName") ,info.getString("appIcon") , info.getString("categoryName") , info.getString("categoryThumbnail") ,info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                        }else {
                            getItems.onEmpty(info.getString("appName") ,info.getString("appIcon") , info.getString("categoryName") , info.getString("categoryThumbnail"));
                        }

                    }

                }catch (JSONException e) {
                    //   console.setText(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void error(VolleyError error) {
                getItems.onError(error);
            }
        });

    }



    ///////for launcher
    //loads more launcher items
    public void getLauncherLoadMore(GetLauncherLoadMore getLauncherLoadMore , String launcherId){
       launcherPageNumber++;
        getLauncherLoadMore.onLoading();
        String url = "https://api.wideflare.com/?action=getLauncher&appKey="+APP_KEY+"&page="+launcherPageNumber+"&launcherId="+launcherId;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getLauncherLoadMore.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if(code == 401){
                        getLauncherLoadMore.onNotExist();
                    }else if(code == 404){
                        getLauncherLoadMore.onNotFound();
                    }
                    else if(code == 405){
                        getLauncherLoadMore.onNotActive();
                    }
                    else if(code == 400){
                        getLauncherLoadMore.onBadRequest();
                    }else if(code == 406){
                        getLauncherLoadMore.onNotAcceptable();
                    }else if(code == 200){

                        if(status.equals("under-construction")){
                            getLauncherLoadMore.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");
                        JSONObject nextPage_ = info.getJSONObject("nextPage");


                        int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                        

                        if(nextPage_.getBoolean("status")) {
                            getLauncherLoadMore.onNextPage();
                        }else {
                            getLauncherLoadMore.onNoNextPage();
                        }

                        if(itemsInThisPage != 0){
                            JSONArray _items = jobj.getJSONArray("items");
                            LauncherItems items [] = new LauncherItems[itemsInThisPage];

                            for(int i=0;i<itemsInThisPage;i++){
                                items[i] = new LauncherItems(
                                        _items.getJSONObject(i).getString("itemName"),
                                        _items.getJSONObject(i).getInt("actionType"),
                                        _items.getJSONObject(i).getString("action"),
                                        _items.getJSONObject(i).getString("thumbnail")
                                );

                            }
                            getLauncherLoadMore.onResult(info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                        }else {
                            getLauncherLoadMore.onEmpty();
                        }

                    }

                }catch (JSONException e) {
                    //   console.setText(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void error(VolleyError error) {
                getLauncherLoadMore.onError(error);
            }
        });
    }

    //loads launcher with its items
    public void getLauncher(GetLauncher getLauncher , String launcherId){
       launcherPageNumber = 1;
        getLauncher.onLoading();
        String url = "https://api.wideflare.com/?action=getLauncher&appKey="+APP_KEY+"&page=1&launcherId="+launcherId;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getLauncher.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if(code == 401){
                        getLauncher.onNotExist();
                    }else if(code == 404){
                        getLauncher.onNotFound();
                    }
                    else if(code == 405){
                        getLauncher.onNotActive();
                    }
                    else if(code == 400){
                        getLauncher.onBadRequest();
                    }else if(code == 406){
                        getLauncher.onNotAcceptable();
                    }else if(code == 200){

                        if(status.equals("under-construction")){
                            getLauncher.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");
                        JSONObject nextPage_ = info.getJSONObject("nextPage");

                        if(info.getJSONObject("appLocation").getBoolean("status")){
                            JSONObject location = info.getJSONObject("appLocation");
                            getLauncher.onAppLocation(location.getString("location") , location.getDouble("lat") , location.getDouble("lon") );
                        }


                        int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                        


                        if(jobj.getJSONObject("announcement").getBoolean("status")){
                            JSONObject announcement = jobj.getJSONObject("announcement");
                            getLauncher.onAnnouncement(announcement.getString("announcementBody"));
                        }

                        if(!info.getString("launcherCover").isEmpty()){
                            getLauncher.onLauncherCover(info.getString("launcherCover"));
                        }


                        if(nextPage_.getBoolean("status")) {
                            getLauncher.onNextPage();
                        }else {
                            getLauncher.onNoNextPage();
                        }

                        if(itemsInThisPage != 0){
                            JSONArray _items = jobj.getJSONArray("items");
                            LauncherItems items [] = new LauncherItems[itemsInThisPage];

                            for(int i=0;i<itemsInThisPage;i++){
                                items[i] = new LauncherItems(
                                        _items.getJSONObject(i).getString("itemName"),
                                        _items.getJSONObject(i).getInt("actionType"),
                                        _items.getJSONObject(i).getString("action"),
                                        _items.getJSONObject(i).getString("thumbnail")
                                );

                            }
                             getLauncher.onResult(info.getString("appName") ,info.getString("appIcon") , info.getString("launcherName") , info.getString("launcherThumbnail") ,info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                        }else {
                            getLauncher.onEmpty(info.getString("appName") ,info.getString("appIcon") , info.getString("launcherName") , info.getString("launcherThumbnail"));
                        }

                    }

                }catch (JSONException e) {
                    //   console.setText(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void error(VolleyError error) {
                getLauncher.onError(error);
            }
        });

    }



    //////for home launcher
    //loads more home launcher items
    public void getHomeLoadMore(GetHomeLoadMore getHomeLoadMore){
       homePageNumber++;
       getHomeLoadMore.onLoading();
        String url = "https://api.wideflare.com/?action=getHome&appKey="+APP_KEY+"&page="+homePageNumber;
        new VolleyGet(context, url, new Fetch() {
            @Override
            public void getData(String response) {
                getHomeLoadMore.onLoadfinished();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject responseObj = jobj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    String status = responseObj.getString("status");
                    if(code == 401){
                        getHomeLoadMore.onNotExist();
                    }else if(code == 404){
                        getHomeLoadMore.onNotFound();
                    }
                    else if(code == 405){
                        getHomeLoadMore.onNotActive();
                    }
                    else if(code == 400){
                        getHomeLoadMore.onBadRequest();
                    }else if(code == 406){
                        getHomeLoadMore.onNotAcceptable();
                    }else if(code == 200){

                        if(status.equals("under-construction")){
                            getHomeLoadMore.onUnderConstruction();
                            return;
                        }

                        JSONObject info = jobj.getJSONObject("info");
                        JSONObject nextPage_ = info.getJSONObject("nextPage");

                        int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                        

                        if(nextPage_.getBoolean("status")) {
                            getHomeLoadMore.onNextPage();
                        }else {
                            getHomeLoadMore.onNoNextPage();
                        }

                        if(itemsInThisPage != 0){
                            JSONArray _items = jobj.getJSONArray("items");
                            HomeItems items [] = new HomeItems[itemsInThisPage];

                            for(int i=0;i<itemsInThisPage;i++){
                                items[i] = new HomeItems(
                                        _items.getJSONObject(i).getString("itemName"),
                                        _items.getJSONObject(i).getInt("actionType"),
                                        _items.getJSONObject(i).getString("action"),
                                        _items.getJSONObject(i).getString("thumbnail")
                                );

                            }

                            getHomeLoadMore.onResult(info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                        }else {
                            getHomeLoadMore.onEmpty();
                        }


                    }



                }catch (JSONException e) {
                    //   console.setText(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void error(VolleyError error) {
                getHomeLoadMore.onError(error);
            }
        });

    }

    //loads home launcher with items
    public void getHome(GetHome home){
        home.onLoading();
        String url = "https://api.wideflare.com/?action=getHome&appKey="+APP_KEY+"&page=1";
            new VolleyGet(context, url, new Fetch() {
                @Override
                public void getData(String response) {
                    home.onLoadfinished();
                    try {
                        JSONObject jobj = new JSONObject(response);
                        JSONObject responseObj = jobj.getJSONObject("response");

                        int code = responseObj.getInt("code");
                        String status = responseObj.getString("status");
                        if(code == 401){
                          home.onNotExist();
                        }else if(code == 404){
                           home.onNotFound();
                        }
                        else if(code == 405){
                           home.onNotActive();
                        }
                        else if(code == 400){
                           home.onBadRequest();
                        }else if(code == 406){
                            home.onNotAcceptable();
                        }else if(code == 200){

                            if(status.equals("under-construction")){
                               home.onUnderConstruction();
                                return;
                            }

                            JSONObject info = jobj.getJSONObject("info");
                            JSONObject nextPage_ = info.getJSONObject("nextPage");

                            if(info.getJSONObject("appLocation").getBoolean("status")){
                                JSONObject location = info.getJSONObject("appLocation");
                                home.onAppLocation(location.getString("location") , location.getDouble("lat") , location.getDouble("lon") );
                            }


                            int itemsInThisPage = Integer.parseInt(info.getString("itemsInThisPage"));

                            


                            if(jobj.getJSONObject("popup").getBoolean("status")){
                                JSONObject popup = jobj.getJSONObject("popup");
                                home.onPopup(popup.getString("popupTitle")  , popup.getString("popupMessage"));
                            }

                            if(jobj.getJSONObject("message").getBoolean("status")){
                                JSONObject message = jobj.getJSONObject("message");
                                home.onMessage(message.getString("messageTitle") , message.getString("messageBody"));
                            }

                            if(jobj.getJSONObject("announcement").getBoolean("status")){
                                JSONObject announcement = jobj.getJSONObject("announcement");
                                home.onAnnouncement(announcement.getString("announcementBody"));
                            }

                            if(!info.getString("homeCover").isEmpty()){
                                home.onHomeCover(info.getString("homeCover"));
                            }


                            if(nextPage_.getBoolean("status")) {
                               home.onNextPage();
                            }else {
                                home.onNoNextPage();
                            }

                            if(itemsInThisPage != 0){
                                JSONArray _items = jobj.getJSONArray("items");
                                HomeItems items [] = new HomeItems[itemsInThisPage];

                                for(int i=0;i<itemsInThisPage;i++){
                                    items[i] = new HomeItems(
                                            _items.getJSONObject(i).getString("itemName"),
                                            _items.getJSONObject(i).getInt("actionType"),
                                            _items.getJSONObject(i).getString("action"),
                                            _items.getJSONObject(i).getString("thumbnail")
                                    );

                                }

                                home.onResult(info.getString("appName") ,info.getString("appIcon") ,info.getInt("totalItemCount")  ,  info.getInt("itemsInThisPage") , info.getInt("itemsPerPage")  , items);

                            }else {
                               home.onEmpty(info.getString("appName") ,info.getString("appIcon") );
                            }


                        }



                    }catch (JSONException e) {
                        //   console.setText(e.toString());
                        e.printStackTrace();
                    }

                }

                @Override
                public void error(VolleyError error) {
                        home.onError(error);
                }
            });
        }


}
