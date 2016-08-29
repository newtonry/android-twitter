package com.codepath.apps.twitterclient.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by rnewton on 8/28/16.
 */
public class Reachability {
    static public boolean isOnline(Context context) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "The internet is not connected", Toast.LENGTH_SHORT).show();

        return false;
    }

}
