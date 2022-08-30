package lialh4.inf.autorun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.Set;

import lialh4.inf.autorun.utils.AppUtils;

public class MainActivity extends BaseActivity {
    private static final int SELECT_APP_REQUEST_CODE = 1;
    private AppUtils appUtils;
    private CardView card;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences("autorun", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.switch_theme_pink:
                updateTheme(R.style.Theme_pink);
                return true;
            case R.id.switch_theme_blue:
                updateTheme(R.style.Theme_blue);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUtils = new AppUtils(this,true);
        card = findViewById(R.id.service_state_card);
        card.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        findViewById(R.id.add_button).setOnClickListener(view -> startActivityForResult(new Intent(this, SelectAppActivity.class), SELECT_APP_REQUEST_CODE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceCard(card);
        refreshApps();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SELECT_APP_REQUEST_CODE && resultCode == RESULT_OK) {
            appUtils.addApp(intent.getStringExtra("pkg"));
        }
    }

    private boolean isServiceEnabled(){
        String prefString = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString!= null && prefString.contains(this.getPackageName() + "/" + AutorunInfService.class.getName());
    }

    private void updateServiceCard(CardView card){
        ImageView img = findViewById(R.id.service_state_img);
        TextView text = findViewById(R.id.service_state_text);
        TextView textDes = findViewById(R.id.service_state_text_des);
        textDes.setText(R.string.service_description);
        if (isServiceEnabled()){
            card.setCardBackgroundColor(getResources().getColor(R.color.trans_pink));
            img.setImageResource(R.drawable.ic_yes);
            text.setText(R.string.service_enabled);
        }else{
            card.setCardBackgroundColor(getResources().getColor(R.color.orange));
            img.setImageResource(R.drawable.ic_no);
            text.setText(R.string.service_disabled);
        }
    }

    private void refreshApps(){
        LinearLayout llApp = findViewById(R.id.ll_app);
        llApp.removeAllViews();
        Set<AppUtils.App> apps = appUtils.getApps();
        PackageManager pm = getPackageManager();
        ApplicationInfo info;
        for(AppUtils.App app:apps) {
            try {
                info = pm.getApplicationInfo(app.packageName,0);
                Button delBtn = new Button(this);
                delBtn.setText(R.string.delete);
                delBtn.setOnClickListener(view -> {
                    appUtils.delApp(app.packageName);
                    refreshApps();
                });
                LinearLayout llH = new LinearLayout(this);
                llH.setGravity(Gravity.END);
                llH.addView(delBtn);
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(AppUtils.genAppInfoLL(this, info.loadIcon(pm), info.loadLabel(pm).toString(), app.packageName));
                ll.addView(llH);
                CardView card = new CardView(this);
                card.addView(ll);
                llApp.addView(card);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}