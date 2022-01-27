
import 'dart:async';

import 'package:flutter/services.dart';

import 'model/usage_stats.dart';

class ApplicationUsage {
  static const MethodChannel _channel = MethodChannel('application_usage');

  static Future<List<UsageStats>?> getUsageList(int startTime, int endTime) async {
    final usageStatusList = <UsageStats>[];
    final sharedData = await _channel.invokeMethod('getUsageList', {'startTime': startTime, 'endTime': endTime});
    for (var element in sharedData) {
      usageStatusList.add(UsageStats.fromMap(element));
    }
    return usageStatusList;
  }
}
