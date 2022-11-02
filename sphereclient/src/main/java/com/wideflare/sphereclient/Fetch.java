package com.wideflare.sphereclient;

import com.android.volley.VolleyError;

 interface Fetch {
    public void getData(String response);
    public  void error(VolleyError error);
}
