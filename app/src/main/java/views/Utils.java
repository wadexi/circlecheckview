package views;

import android.content.*;

/**
 * Created by Administrator on 2017/11/6.
 */

public class Utils {

    public static int dip2px(Context context,int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(scale*dp + 0.5f);
    }
}
