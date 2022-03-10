#import "BekenenginePlugin.h"
#if __has_include(<bekenengine/bekenengine-Swift.h>)
#import <bekenengine/bekenengine-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "bekenengine-Swift.h"
#endif

@implementation BekenenginePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBekenenginePlugin registerWithRegistrar:registrar];
}
@end
