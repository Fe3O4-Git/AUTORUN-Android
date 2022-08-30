package lialh4.inf.autorun;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import lialh4.inf.autorun.utils.SPUtils;

public class BaseActivity extends AppCompatActivity {
    SPUtils spUtils;

    private void loadTheme(){
        setTheme(spUtils.sp.getInt("theme", R.style.Theme_pink));
    }

    protected void updateTheme(int theme){
        spUtils.editor.putInt("theme",theme);
        spUtils.editor.apply();
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SPUtils(this,true);
        loadTheme();
    }
}
