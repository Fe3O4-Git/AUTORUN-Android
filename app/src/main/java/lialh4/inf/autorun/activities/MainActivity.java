package lialh4.inf.autorun.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lialh4.inf.autorun.AutorunInfService;
import lialh4.inf.autorun.R;
import lialh4.inf.autorun.utils.AppUtils;
import lialh4.inf.autorun.utils.UIUtils;

public class MainActivity extends BaseActivity {
    private static final int SELECT_APP_REQUEST_CODE = 1;
    private AppUtils appUtils;
    private ConstraintLayout rootCl;
    private CardView card;
    private FloatingActionButton addBtn;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test:
                Snackbar.make(rootCl, getText(R.string.cant_do), Snackbar.LENGTH_LONG).setAnchorView(addBtn).show();
                return true;
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
        appUtils = new AppUtils(this, true);
        rootCl = findViewById(R.id.main_root_cl);
        card = findViewById(R.id.service_state_card);
        addBtn = findViewById(R.id.add_button);
        card.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        addBtn.setOnClickListener(view -> startActivityForResult(new Intent(this, SelectAppActivity.class), SELECT_APP_REQUEST_CODE));
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
            if (!appUtils.addApp(intent.getStringExtra("pkg")))
                Snackbar.make(rootCl, R.string.app_already_exist, Snackbar.LENGTH_SHORT).setAnchorView(addBtn).show();
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
            card.setCardBackgroundColor(getResources().getColor(R.color.green));
            img.setImageResource(R.drawable.ic_yes);
            text.setText(R.string.service_enabled);
        }else{
            card.setCardBackgroundColor(getResources().getColor(R.color.orange));
            img.setImageResource(R.drawable.ic_no);
            text.setText(R.string.service_disabled);
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshApps(){
        int dp16 = UIUtils.dp2px(16);
        LinearLayout llApp = findViewById(R.id.ll_app);
        llApp.removeAllViews();
        Set<AppUtils.App> apps = appUtils.getApps();
        PackageManager pm = getPackageManager();
        ApplicationInfo info;
        for(AppUtils.App app:apps) {
            try {
                info = pm.getApplicationInfo(app.packageName,0);
                AtomicInteger operation = new AtomicInteger();
                TextView prefix = new TextView(this);
                prefix.setText(R.string.delay_hint_prefix);
                prefix.setTextColor(getResources().getColor(R.color.black));
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                String text = Float.toString(app.delay);
                if(text.endsWith(".0"))
                    text=text.substring(0,text.length()-2);
                editText.setText(text);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String text = editText.getText().toString();
                        if(text.isEmpty()||text.endsWith("."))
                            return;
                        float delay = Float.parseFloat(text);
                        appUtils.editApp(app.packageName,delay);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                TextView suffix = new TextView(this);
                suffix.setText(R.string.delay_hint_suffix);
                suffix.setTextColor(getResources().getColor(R.color.black));
                LinearLayout llS = new LinearLayout(this);
                llS.setPadding(0,0,dp16,0);
                llS.setGravity(Gravity.CENTER_VERTICAL);
                if(!app.returnHome)
                    llS.setVisibility(View.GONE);
                llS.addView(prefix);
                llS.addView(editText);
                llS.addView(suffix);
                SwitchMaterial sw = new SwitchMaterial(this);
                sw.setText(R.string.return_home);
                sw.setChecked(app.returnHome);
                sw.setOnClickListener(view -> {
                    if(sw.isChecked()) {
                        operation.set(AppUtils.SET_RETURN_HOME);
                        llS.setVisibility(View.VISIBLE);
                    }else{
                        operation.set(AppUtils.UNSET_RETURN_HOME);
                        llS.setVisibility(View.GONE);
                    }
                    appUtils.editApp(app.packageName, operation.get());
                });
                Button delBtn = new Button(this);
                delBtn.setText(R.string.delete);
                delBtn.setAllCaps(false);
                delBtn.setOnClickListener(view -> {
                    operation.set(AppUtils.REMOVE);
                    appUtils.editApp(app.packageName,operation.get());
                    refreshApps();
                });
                LinearLayout llH = new LinearLayout(this);
                llH.setPadding(dp16,0,dp16,dp16);
                llH.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
                llH.addView(llS);
                llH.addView(sw);
                llH.addView(delBtn);
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(UIUtils.genAppInfoLL(this, info.loadIcon(pm), info.loadLabel(pm).toString(), app.packageName));
                ll.addView(llH);
                CardView card = new CardView(this);
                card.setRadius(dp16);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(dp16,dp16,dp16,dp16);
                card.setLayoutParams(lp);
                card.addView(ll);
                llApp.addView(card);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}