/*
 * Copyright (c) 2022.
 * Created by Ameer Hamza on 15/3/2022
 * Author Ameer Hamza (ameerhamza7171@gmail.com)
 */

package com.ameer.awesome.utility;

import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


/**
 * Check either a device is rooted or not
 * To Use this class simply call RootUtil.isDeviceRooted()
 */
public class RootUtil {

    /**
     * This method will return true if device is rooted, otherwise return false.
     */
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4("su");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private static boolean checkRootMethod4(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }


/* public static boolean checkRootMethod1() {
// check if /system/app/Superuser.apk is present
try {
File file = new File("/system/app/Superuser.apk");
if (file.exists()) {
return true;
}
} catch (Exception e1) {
// ignore
}

// try executing commands
return canExecuteCommand("/system/xbin/which su")
*//*|| canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su")*//*;
}*/

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    // Root check on base of android version check
    public static boolean checkRootMethod1() {
// check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
// ignore
        }

        boolean check1 = canExecuteCommand("/system/xbin/which su");
        boolean check2 = canExecuteCommand("/system/bin/which su");
        boolean check3 = canExecuteCommand("which su");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return check1;
        } else {
            return check1 || check2 || check3;
        }

    }
}