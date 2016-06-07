package com.intrepid.thirdpartylibs;

import android.app.Application;
import android.widget.Toast;

import com.intrepid.thirdpartylibs.events.ToastErrorEvent;
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
        bus.register(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ToastErrorEvent errorEvent) {
        Toast.makeText(ThirdPartyLibsApplication.this, errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }
}
