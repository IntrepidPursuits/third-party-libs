package com.intrepid.thirdpartylibs.events;

public class ToastErrorEvent {
    private CharSequence errorMsg;

    public ToastErrorEvent(CharSequence errorMsg) {
        this.errorMsg = errorMsg;
    }

    public CharSequence getErrorMsg() {
        return errorMsg;
    }
}
