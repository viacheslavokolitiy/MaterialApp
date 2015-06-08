package org.satorysoft.cotton.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.satorysoft.cotton.util.TypefaceCreator;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class RobotoTextView extends TextView {
    public RobotoTextView(Context context){
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(new TypefaceCreator(context).createCustomTypeface("font/RobotoCondensed-Regular.ttf"));
    }
}
