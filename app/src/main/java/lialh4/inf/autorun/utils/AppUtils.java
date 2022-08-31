package lialh4.inf.autorun.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class AppUtils extends SPUtils{
    public static final int REMOVE = -2, SET_RETURN_HOME = -3, UNSET_RETURN_HOME = -4;
    public final Gson gson;

    public static class App {
        public String packageName;
        public boolean returnHome;
        public float delay;
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

    public boolean addApp(String packageName) {
        Set<String> jsonSet = getAppJsonSet();
        for (String json : jsonSet) {
            App app = gson.fromJson(json, App.class);
            if (packageName.equals(app.packageName))
                return false;
        }
        App app = new App();
        app.packageName = packageName;
        app.returnHome = false;
        app.delay = 1;
        jsonSet.add(gson.toJson(app));
        updateAppJsonSet(jsonSet);
        return true;
    }

    public void editApp(String packageName, float operations) {
        Set<String> jsonSet = getAppJsonSet();
        for(String json:jsonSet){
            App app = gson.fromJson(json,App.class);
            if(packageName.equals(app.packageName)){
                jsonSet.remove(json);
                if(operations != REMOVE) {
                    if(operations == SET_RETURN_HOME)
                        app.returnHome = true;
                    else if(operations == UNSET_RETURN_HOME)
                        app.returnHome = false;
                    else
                        app.delay = operations;
                    jsonSet.add(gson.toJson(app));
                }
                break;
            }
        }
        updateAppJsonSet(jsonSet);
    }
}
