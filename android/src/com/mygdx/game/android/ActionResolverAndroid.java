package com.mygdx.game.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.mygdx.game.ActionResolver;

/**
 * Created by Master on 1/5/2016.
 */
public class ActionResolverAndroid implements ActionResolver {
    Handler handler;
    Context context;
    ConnectivityManager connectivityManager;

    public ActionResolverAndroid(Context context) {
        handler = new Handler();
        this.context = context;
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public boolean checkInternet() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
}
