package lialh4.inf.autorun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AtomicInteger clickTimes = new AtomicInteger();
        TextView tv = findViewById(R.id.version_tv);
        tv.setText(BuildConfig.VERSION_NAME);
        findViewById(R.id.version_card).setOnClickListener(view -> {
            if(clickTimes.get() == 4){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mtf.wiki")));
                clickTimes.set(0);
            }else
                clickTimes.getAndIncrement();
        });
    }

}