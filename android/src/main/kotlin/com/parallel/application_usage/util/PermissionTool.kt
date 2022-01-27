package com.parallel.application_usage.util

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.provider.Settings


class PermissionTool private constructor() {

    companion object {
        private var instance: PermissionTool? = null
            get() {
                if (field == null) {
                    field = PermissionTool()
                }
                return field
            }
        @Synchronized
        fun get(): PermissionTool {
            return instance!!
        }
    }

    fun isGranted(context: Context): Boolean {
        return !(!isStatAccessPermissionSet(context) && isNoOption(context))
    }

    private fun isStatAccessPermissionSet(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                val packageManager = context.packageManager
                val info = packageManager.getApplicationInfo(context.packageName, 0)
                val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                //                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            true
        }
    }

    private fun isNoOption(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val packageManager = context.packageManager
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            val list: MutableList<ResolveInfo> = ArrayList()
            //进程间通讯有可能异常
            try {
                list.addAll(packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return list.size > 0
        }
        return false
    }
}