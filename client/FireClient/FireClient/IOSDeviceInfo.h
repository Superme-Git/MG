//  IOSDeviceInfo.h
//  FireClient

@interface  IOSDeviceInfo

+ (NSString *) getSysInfoByName:(const char *)typeSpecifier;
+ (NSUInteger) getSysInfo: (uint) typeSpecifier;
+ (NSUInteger) getCPUFrequency;
+ (NSUInteger) getCPUCount;
+ (NSUInteger) getTotalMemorySize;
+ (float) getProcessCpuRate;

@end
