package com.intrepid.thirdpartylibs.models;

import android.content.Context;

import com.intrepid.thirdpartylibs.R;

public enum LibDemo {
    RETROFIT(R.string.lib_demo_github);

    private int titleResId;

    LibDemo(int titleResId) {
        this.titleResId = titleResId;
    }

    public String getTitle(Context context) {
        return context.getString(titleResId);
    }
}
