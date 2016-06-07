package com.intrepid.thirdpartylibs;

import android.app.Application;

import com.intrepid.thirdpartylibs.net.ServiceManager;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class ThirdPartyLibsApplication extends Application {
    public static EventBus bus;

    public ThirdPartyLibsApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ServiceManager.init();

        bus = new EventBus();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
