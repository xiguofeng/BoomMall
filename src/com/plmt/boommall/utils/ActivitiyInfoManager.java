
package com.plmt.boommall.utils;

import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class ActivitiyInfoManager {

    public static HashMap<String, Activity> activitityMap= new HashMap<String, Activity>();
    
    

    public static Activity putActivityMap(String name, Activity activity) {
        return activitityMap.put(name, activity);
    }

    public static Activity getActivitityMap(String name) {
        return activitityMap.get(name);
    }

    public static String getCurrentActivityName(Context context){
       
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String acitvityName = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        
        return acitvityName;
        
    }
    
    public static int getcount(){
        return activitityMap.size();
    }
    
    public static Boolean isAlreadyIn(String name){
        if (activitityMap.containsKey(name)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void finishActivity(String name) {
        Activity activity = activitityMap.remove(name);
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();                
            }
        }
    }

}
