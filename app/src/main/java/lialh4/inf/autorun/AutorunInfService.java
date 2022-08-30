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
            }else
                Toast.makeText(this, "aaabbb", Toast.LENGTH_SHORT).show();
        }
    }
}