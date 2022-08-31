package lialh4.inf.autorun;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Set;

import lialh4.inf.autorun.utils.AppUtils;

public class AutorunInfService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected(){
        AppUtils appUtils = new AppUtils(this,false);
        Set<AppUtils.App> apps = appUtils.getApps();
        for(AppUtils.App app: apps){
            Intent intent = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if(intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                if(app.returnHome){
                    int time = Math.round(1000 * app.delay);
                    if(time!=0)
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    Intent intentHome = new Intent(Intent.ACTION_MAIN);
                    intentHome.addCategory(Intent.CATEGORY_HOME);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);
                }
            }else
                Toast.makeText(this, getText(R.string.cant_start)+app.packageName, Toast.LENGTH_SHORT).show();
        }
    }
}