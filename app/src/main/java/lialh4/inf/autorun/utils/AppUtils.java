package lialh4.inf.autorun.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;

public class AppUtils extends SPUtils{
    public static final int REMOVE = -2, SET_RETURN_HOME = -3, UNSET_RETURN_HOME = -4;
    public final Gson gson;

    public static class App {
        public String packageName;
        public boolean returnHome;
        public float delay;
    }

    public AppUtils(Context context, boolean edit) {
        super(context, edit);
        gson = new Gson();
    }

    public LinkedList<App> getApps() {
        String json = sp.getString("apps", null);
        LinkedList<App> apps = new LinkedList<>();
        if (json != null)
            apps = gson.fromJson(json, new TypeToken<LinkedList<App>>() {
            }.getType());
        return apps;
    }

    public boolean addApp(String packageName) {
        LinkedList<App> apps = getApps();
        for (App app : apps) {
            if (packageName.equals(app.packageName))
                return false;
        }
        App app = new App();
        app.packageName = packageName;
        app.returnHome = false;
        app.delay = 1;
        apps.add(app);
        editor.putString("apps", gson.toJson(apps));
        editor.apply();
        return true;
    }

    public void editApp(String packageName, float operations) {
        LinkedList<App> apps = getApps();
        for (App app : apps) {
            if (packageName.equals(app.packageName)) {
                apps.remove(app);
                if (operations != REMOVE) {
                    if (operations == SET_RETURN_HOME)
                        app.returnHome = true;
                    else if (operations == UNSET_RETURN_HOME)
                        app.returnHome = false;
                    else
                        app.delay = operations;
                    apps.add(app);
                }
                break;
            }
        }
        editor.putString("apps", gson.toJson(apps));
        editor.apply();
    }
}
