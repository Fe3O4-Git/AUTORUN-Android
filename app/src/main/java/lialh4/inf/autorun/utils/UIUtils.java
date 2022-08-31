package lialh4.inf.autorun.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UIUtils {
    public static int dp2px(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static LinearLayout genAppInfoLL(Context context, Drawable icon, String name, String packageName){
        int dp16 = dp2px(16);
        ImageView img = new ImageView(context);
        img.setImageDrawable(icon);
        img.setLayoutParams(new RelativeLayout.LayoutParams(180, 180));

        TextView tv1 = new TextView(context);
        tv1.setText(name);

        TextView tv2 = new TextView(context);
        tv2.setText(packageName);

        LinearLayout llV = new LinearLayout(context);
        llV.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lpV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpV.setMargins(dp16,0,0,0);
        lpV.gravity = Gravity.CENTER_VERTICAL;
        llV.setLayoutParams(lpV);
        llV.addView(tv1);
        llV.addView(tv2);

        LinearLayout llH = new LinearLayout(context);
        LinearLayout.LayoutParams lpH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpH.setMargins(dp16,dp16,dp16,dp16);
        llH.setLayoutParams(lpH);
        llH.addView(img);
        llH.addView(llV);

        return llH;
    }
}
