package com.meyouhealth.myhandroid.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.meyouhealth.myhandroid.R;

import java.util.HashMap;
import java.util.UUID;

public class DeviceDetailsHelper {

    public static final String PROPERTY_REG_ID = CoreConstants.PREF_GCM_DEVICE_ID;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static String getUserAgentString(Context context) {
        StringBuilder agentString = new StringBuilder();
        //formatted as: projectname/version (device; os version)

        String appName = getAppName(context);
        String appVersion = String.valueOf(getAppVersion(context));
        String phoneType = getPhoneType();
        String osVersion = getOsVersion();

        agentString.append(appName+"/"+appVersion);

        if(!TextUtils.isEmpty(phoneType) || !TextUtils.isEmpty(osVersion)) {
            agentString.append("(");

            if(!TextUtils.isEmpty(phoneType)) {
                agentString.append(phoneType + "; ");
            }

            if(!TextUtils.isEmpty(osVersion)) {
                agentString.append(osVersion + "");
            }

            agentString.append(")");
        }

        return agentString.toString();
    }

    public static String getAppName(Context context) {
        return context.getString(R.string.user_agent_app_name);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getPhoneType() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE + " " + String.valueOf(Build.VERSION.SDK_INT);
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(CoreConstants.SHARED_PREFS,
                Context.MODE_PRIVATE);
    }

    private static String getDeviceFriendlyName(Context context) {
        BluetoothAdapter bluetoothAdapter = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            BluetoothManager manager  = (BluetoothManager)context.getSystemService(
                    Context.BLUETOOTH_SERVICE);
            if (manager != null) {
                bluetoothAdapter = manager.getAdapter();
            }
        }
        String friendlyName = Build.MODEL;
        if (bluetoothAdapter != null) {
            // Will only reach here if the device supports Bluetooth
            friendlyName = bluetoothAdapter.getName();
        }

        return friendlyName;
    }

    public static String getIdentifierFromMap(HashMap<String, Boolean> map) {
        Object[] objects = map.keySet().toArray();
        if (objects.length > 0) {
            return (String) objects[0];
        }

        return "";
    }

    public static HashMap<String, Boolean> getDeviceIdentifier(Context context) {

        final TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(
                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String deviceId = deviceUuid.toString();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }

        SharedPreferences prefs = context.getSharedPreferences(CoreConstants.SHARED_PREFS, 0);
        String prevDeviceId = prefs.getString(CoreConstants.PROPERTY_DEVICE_IDENTIFIER, "");
        boolean isNewIdentifier = false;
        if (!prevDeviceId.equals(deviceId)) {
            // Replace  with new identifier
            SharedPreferences.Editor editor = context.getSharedPreferences(CoreConstants.SHARED_PREFS, 0).edit();
            editor.putString(CoreConstants.PROPERTY_DEVICE_IDENTIFIER, deviceId);
            editor.commit();
            isNewIdentifier = true;
        }

        HashMap<String, Boolean> result = new HashMap<>();
        result.put(deviceId, isNewIdentifier);
        return result;
    }
}
