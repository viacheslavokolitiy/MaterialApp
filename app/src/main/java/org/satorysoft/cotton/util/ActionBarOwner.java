package org.satorysoft.cotton.util;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.ui.widget.RobotoTextView;

/**
 * Created by viacheslavokolitiy on 15.06.2015.
 */
public class ActionBarOwner {
    private final Context context;

    public ActionBarOwner(Context context){
        this.context = context;
    }

    public void setCustomActionBarTitle(ActionBar actionBar, String title) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_action_bar_title, null);
        ((RobotoTextView)v.findViewById(R.id.text_custom_action_bar_title)).setText(title);
        actionBar.setCustomView(v);
    }
}
