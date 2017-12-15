package com.zhushou.weichat;

import com.lody.virtual.client.hook.delegate.PhoneInfoDelegate;


/**
 * Fake the Device ID.
 */
class MyPhoneInfoDelegate implements PhoneInfoDelegate {



    @Override
    public String getDeviceId(String oldDeviceId, int userId) {
        return null;
    }

    @Override
    public String getBluetoothAddress(String oldBluetoothAddress, int userId) {
        return null;
    }

    @Override
    public String getMacAddress(String oldMacAddress, int userId) {
        return null;
    }
}
