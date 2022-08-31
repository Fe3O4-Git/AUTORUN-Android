package lialh4.inf.autorun.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lialh4.inf.autorun.BuildConfig;
import lialh4.inf.autorun.R;
import lialh4.inf.autorun.utils.UIUtils;

public class SelectAppActivity extends BaseActivity {

    LinearLayout ll;
    MenuItem showSystemAppSwitch;

    public static class AppInfo{
        public String name;
        public String packageName;
        public Drawable icon;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_list_menu, menu);
        showSystemAppSwitch = menu.findItem(R.id.show_system_apps_switch);
        refreshAppList();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_system_apps_switch) {
            item.setChecked(!item.isChecked());
            refreshAppList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
        ll = findViewById(R.id.ll);
    }

    public Set<AppInfo> getAppList(boolean getSystemApp){
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        //noinspection ComparatorCombinators
        Set<AppInfo> appInfoSet = new TreeSet<>((appInfo1, appInfo2) -> appInfo1.name.compareTo(appInfo2.name));
        for(ApplicationInfo app: apps){
            if(((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0 && !getSystemApp) || app.packageName.equals(BuildConfig.APPLICATION_ID))
                continue;
            AppInfo appInfo = new AppInfo();
            appInfo.name = app.loadLabel(pm).toString();
            appInfo.packageName = app.packageName;
            appInfo.icon = app.loadIcon(pm);
            appInfoSet.add(appInfo);
        }
        return appInfoSet;
    }

    private void buildAppList(Set<AppInfo> appInfoSet){
        for(AppInfo appInfo: appInfoSet) {
            LinearLayout llH = UIUtils.genAppInfoLL(this, appInfo.icon, appInfo.name, appInfo.packageName);
            llH.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra("pkg", appInfo.packageName);
                setResult(RESULT_OK, intent);
                finish();
            });
            ll.addView(llH);
        }
    }

    private void refreshAppList() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        ll.removeAllViews();
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            Set<AppInfo> appInfoSet = getAppList(showSystemAppSwitch.isChecked());
            runOnUiThread(() -> {
                buildAppList(appInfoSet);
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }
}