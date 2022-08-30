package lialh4.inf.autorun.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class AppUtils extends SPUtils{
    public final Gson gson;

    public static class App {
        public String packageName;
        public boolean returnHome;
        public int delay;
    }

    public AppUtils(Context context, boolean edit){
        super(context, edit);
        gson = new Gson();
    }

    private Set<String> getAppJsonSet(){
        return getStringSet("apps");
    }

    private void updateAppJsonSet(Set<String> jsonSet){
        updateStringSet("apps", jsonSet);
    }

    public Set<App> getApps(){
        Set<String> jsonSet = getAppJsonSet();
        Set<App> apps = new HashSet<>();
        for(String json:jsonSet){
            App app = gson.fromJson(json, App.class);
            apps.add(app);
        }
        return apps;
    }

    public void addApp(String packageName){
        Set<String> jsonSet = getAppJsonSet();
        App app = new App();
        app.packageName = packageName;
        app.returnHome = false;
        app.delay = 0;
        jsonSet.add(gson.toJson(app));
        updateAppJsonSet(jsonSet);
    }

    public void delApp(String packageName){
        Set<String> jsonSet = getAppJsonSet();
        for(String json:jsonSet){
            App app = gson.fromJson(json,App.class);
            if(packageName.equals(app.packageName)){
                jsonSet.remove(json);
                break;
            }
        }
        updateAppJsonSet(jsonSet);
    }

    public static LinearLayout genAppInfoLL(Context context, Drawable icon, String name, String packageName){
        ImageView img = new ImageView(context);
        img.setImageDrawable(icon);
        img.setLayoutParams(new RelativeLayout.LayoutParams(180, 180));

        TextView tv1 = new TextView(context);
        tv1.setText(name);

        TextView tv2 = new TextView(context);
        tv2.setText(packageName);

        LinearLayout llV = new LinearLayout(context);
        llV.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,0,0,0);
        params.gravity = Gravity.CENTER_VERTICAL;
        llV.setLayoutParams(params);
        llV.addView(tv1);
        llV.addView(tv2);

        LinearLayout llH = new LinearLayout(context);
        llH.addView(img);
        llH.addView(llV);

        return llH;
    }
}
