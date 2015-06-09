package org.satorysoft.cotton.util;

import android.content.res.Resources;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class DpUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
