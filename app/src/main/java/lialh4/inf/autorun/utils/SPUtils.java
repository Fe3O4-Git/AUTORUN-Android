package lialh4.inf.autorun.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import lialh4.inf.autorun.R;

public class SPUtils {
    public final SharedPreferences sp;
    public SharedPreferences.Editor editor;
    public SPUtils(Context context, boolean edit){
        String spName = context.getString(R.string.app_name).toLowerCase();
        sp = context.getSharedPreferences(spName, Activity.MODE_PRIVATE);
        if(edit)
            editor = sp.edit();
    }
}
