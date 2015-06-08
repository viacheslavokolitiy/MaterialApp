package org.satorysoft.cotton.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class DrawableConverter {
    public byte[] convertDrawable(Drawable applicationIcon) {
        Bitmap bitmap = ((BitmapDrawable)applicationIcon).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
