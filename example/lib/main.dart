import 'package:application_usage/application_usage.dart';
import 'package:application_usage/model/usage_stats.dart';
import 'package:flutter/material.dart';
import 'dart:async';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'UsageStats Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: '访问应用使用情况demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  final usageStatusList = <UsageStats>[];

  @override
  void initState() {
    super.initState();
    _refresh();
  }

  Future<void> _refresh() async {
    var startTime = DateTime.now().millisecondsSinceEpoch - 24*60*60*1000;
    var endTime = DateTime.now().millisecondsSinceEpoch;
    var sharedData = await ApplicationUsage.getUsageList(startTime, endTime);
    if (sharedData != null) {
      setState(() {
        usageStatusList.clear();
        usageStatusList.addAll(sharedData);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
          child: ListView.separated(
            itemCount: usageStatusList.length,
            itemBuilder: (context, index) {
              return Column(
                children: [
                  Text('应用名: ${usageStatusList[index].applicationName}'),
                  Text('包名: ${usageStatusList[index].packageName}'),
                  Text('前台使用时间: ${usageStatusList[index].totalTimeInForeground}'),
                ],
              );
            },
            separatorBuilder: (context, index) => const Divider(height: .0),
          )
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _refresh,
        tooltip: 'refresh',
        child: const Icon(Icons.refresh),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}