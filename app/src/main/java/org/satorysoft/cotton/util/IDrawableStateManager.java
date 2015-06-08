package org.satorysoft.cotton.util;

import android.graphics.drawable.Drawable;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public interface IDrawableStateManager {
    byte[] convertToBytes(Drawable drawable);
    public Drawable restoreDrawable(byte[] bytes);
}
