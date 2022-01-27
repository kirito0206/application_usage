#import "ApplicationUsagePlugin.h"
#if __has_include(<application_usage/application_usage-Swift.h>)
#import <application_usage/application_usage-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "application_usage-Swift.h"
#endif

@implementation ApplicationUsagePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftApplicationUsagePlugin registerWithRegistrar:registrar];
}
@end
