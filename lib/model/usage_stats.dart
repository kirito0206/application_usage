class UsageStats {
  String? applicationName;
  String? packageName;
  int? firstTimeStamp;
  int? lastTimeStamp;
  int? lastTimeUsed;
  int? totalTimeInForeground;
  int? describeContents;
  int? lastTimeVisible;
  int? totalTimeVisible;
  int? lastTimeForegroundServiceUsed;
  int? totalTimeForegroundServiceUsed;

  UsageStats.fromMap(Map<dynamic, dynamic> map) {
    applicationName = map['applicationName'];
    packageName = map['packageName'];
    firstTimeStamp = map['firstTimeStamp'];
    lastTimeStamp = map['lastTimeStamp'];
    lastTimeUsed = map['lastTimeUsed'];
    totalTimeInForeground = map['totalTimeInForeground'];
    describeContents = map['describeContents'];
    lastTimeVisible = map['lastTimeVisible'];
    totalTimeVisible = map['totalTimeVisible'];
    lastTimeForegroundServiceUsed = map['lastTimeForegroundServiceUsed'];
    totalTimeForegroundServiceUsed = map['totalTimeForegroundServiceUsed'];
  }
}