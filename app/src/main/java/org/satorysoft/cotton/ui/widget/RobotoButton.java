package org.satorysoft.cotton.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import org.satorysoft.cotton.util.TypefaceCreator;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class RobotoButton extends Button {
    public RobotoButton(Context context){
        super(context);
    }

    public RobotoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(new TypefaceCreator(context).createCustomTypeface("font/RobotoCondensed-Regular.ttf"));
    }
}
