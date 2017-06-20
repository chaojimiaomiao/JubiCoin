package com.zhaibingjie.jubicoin;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zhaibingjie on 2017/6/17.
 */

public class ParamFactory {

    static String Bath = "jubi.com";
    static RequestQueue queue;

    public static RequestQueue getVolley(Activity mActivity) {
        if (queue == null) {
            queue = Volley.newRequestQueue(mActivity);
        }
        return queue;
    }

    public static String getPrice(String coinName) {
        String url = "http://www.jubi.com/api/v1/ticker?coin=";
        url += coinName;

        return url;
    }
}
