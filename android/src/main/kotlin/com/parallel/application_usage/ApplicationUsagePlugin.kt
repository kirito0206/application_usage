package com.parallel.application_usage

import android.app.Activity
import android.app.usage.UsageStatsManager
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivity
import com.parallel.application_usage.util.PermissionTool
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** ApplicationUsagePlugin */
class ApplicationUsagePlugin: ActivityAware, FlutterPlugin, MethodCallHandler {

  private lateinit var channel : MethodChannel

  private var activity: Activity? = null

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "application_usage")
    channel.setMethodCallHandler(this)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getUsageList") {
      if (activity == null) {
        result.error("414", "绑定的activity为空", null)
      }
      if (!PermissionTool.get().isGranted(activity!!)) {
        startActivity(activity!!, Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), null)
          result.error("414", "权限未开启", null)
      } else {
        val startTime = call.argument<Long>("startTime")
        val endTime = call.argument<Long>("endTime")
        if (startTime == null || endTime == null)
          result.error("414", "startTime或endTime为空", null)
        else
          result.success(getUsageList(startTime, endTime))
      }
    } else {
      result.notImplemented()
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
  fun getUsageList(startTime: Long, endTime: Long): ArrayList<Map<String, Any?>> {
    val res: ArrayList<Map<String, Any?>> = ArrayList()
    val usm = activity!!.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
    val map = usm.queryAndAggregateUsageStats(startTime, endTime)
    for ((_, stats) in map) {
      if (stats.totalTimeInForeground > 0) {
        val statsMap = HashMap<String, Any?>()
        statsMap["applicationName"] = getApplicationNameByPackageName(stats.packageName)
        statsMap["packageName"] = stats.packageName
        statsMap["firstTimeStamp"] = stats.firstTimeStamp
        statsMap["lastTimeStamp"] = stats.lastTimeStamp
        statsMap["lastTimeUsed"] = stats.lastTimeUsed
        statsMap["totalTimeInForeground"] = stats.totalTimeInForeground
        statsMap["describeContents"] = stats.describeContents()
        // api版本限制
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          statsMap["lastTimeVisible"] = stats.lastTimeVisible
          statsMap["totalTimeVisible"] = stats.totalTimeVisible
          statsMap["lastTimeForegroundServiceUsed"] = stats.lastTimeForegroundServiceUsed
          statsMap["totalTimeForegroundServiceUsed"] = stats.totalTimeForegroundServiceUsed
        }
        res.add(statsMap)
      }
    }
    return res
  }

  /**
   * 根据包名获取应用名
   */
  private fun getApplicationNameByPackageName(packageName: String?): String {
    return try {
      activity!!.packageManager.getApplicationLabel(activity!!.packageManager.getApplicationInfo(packageName!!, PackageManager.GET_META_DATA)).toString()
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
      ""
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
