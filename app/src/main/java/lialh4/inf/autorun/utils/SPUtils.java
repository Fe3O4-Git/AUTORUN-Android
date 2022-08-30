package lialh4.inf.autorun.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

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

    public Set<String> getStringSet(String name){
        return new HashSet<>(sp.getStringSet(name, new HashSet<>()));
    }

    public void updateStringSet(String name, Set<String> StringSet){
        editor.putStringSet(name, StringSet);
        editor.apply();
    }
}
