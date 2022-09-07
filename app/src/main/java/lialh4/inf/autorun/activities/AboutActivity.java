package lialh4.inf.autorun.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicInteger;

import lialh4.inf.autorun.BuildConfig;
import lialh4.inf.autorun.R;

public class AboutActivity extends BaseActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AtomicInteger versionClickTimes = new AtomicInteger();
        ConstraintLayout rootCl = findViewById(R.id.about_root_cl);
        TextView tv = findViewById(R.id.version_tv);
        tv.setText(BuildConfig.VERSION_NAME);
        findViewById(R.id.version_card).setOnClickListener(v -> {
            if (versionClickTimes.get() == 4) {
                openUrl("https://mtf.wiki");
                versionClickTimes.set(0);
            } else
                versionClickTimes.getAndIncrement();
        });
        findViewById(R.id.author_card).setOnClickListener(v1 -> {
            if (Math.random() < 0.1)
                Snackbar.make(rootCl, R.string.tcaretni4hlail, Snackbar.LENGTH_LONG)
                        .setAction(R.string.caretni4hlail, v2 -> Snackbar.make(rootCl, getString(R.string.hrtcaretni4hlail) + String.format("%.2f", 1 + Math.random() * 2), Snackbar.LENGTH_LONG).show())
                        .show();
        });
        findViewById(R.id.github_card).setOnClickListener(v -> openUrl("http://github.com/Fe3O4-Git/Android-Autorun"));
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
