package org.satorysoft.cotton.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class TypefaceCreator {
    private Typeface mTypeface;
    private final Context context;

    public TypefaceCreator(Context context){
        this.context = context;
    }

    public Typeface createCustomTypeface(String typefaceName){
        mTypeface = Typeface.createFromAsset(context.getAssets(), typefaceName);

        return mTypeface;
    }
}
