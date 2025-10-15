//
//  LocationDetector_ios.m
//  FireClient
//
//  Created by user on 16/5/11.
//
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

#include "LocationDetector.h"
#include "CCCommon.h"

static void appendLocationName(std::string& name, const char* sz)
{
    if(!name.empty())
    {
        name += " . ";
    }
    name += sz;
}

@interface LocationManagerWraper : NSObject<CLLocationManagerDelegate>

@property(nonatomic,assign) CLLocationManager* locationMgr;

-(LocationManagerWraper*)init;

-(CLAuthorizationStatus)getAuthorizationStatus;
-(void)requestAuthorization;
-(void)startDetect;

@end

@implementation LocationManagerWraper

-(LocationManagerWraper*)init
{
    if(self.locationMgr == nil)
    {
        self.locationMgr = [[CLLocationManager alloc] init];
        self.locationMgr.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        if ([self.locationMgr respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
            [self.locationMgr requestWhenInUseAuthorization];
        }
    }
    
    return self;
}

-(CLAuthorizationStatus)getAuthorizationStatus
{
    return [CLLocationManager authorizationStatus];
}

-(void)requestAuthorization
{
    if(self.locationMgr != nil)
    {
        if ([self.locationMgr respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
            [self.locationMgr requestWhenInUseAuthorization];
        }
    }
}

-(void)startDetect
{
    if(self.locationMgr != nil)
    {
        self.locationMgr.delegate = self;
        [self.locationMgr startUpdatingLocation];
    }
}

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations
{
    self.locationMgr.delegate = nil;
    [self.locationMgr stopUpdatingLocation];  // 已经获取到了位置，停止获取位置
    
    LocationDetector* pLocDetector = LocationDetector::GetInstance();
    if(pLocDetector)
    {
        pLocDetector->clear();
        
        CLGeocoder* pGeoCoder = [[CLGeocoder alloc] init];
        
        for(CLLocation* pLoc in locations)
        {
            [pGeoCoder reverseGeocodeLocation:pLoc completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error)
            {
                if(error == nil)
                {
                    bool bGetPlace = false;
                    for (CLPlacemark* pPlace in placemarks)
                    {
                        LocationDetector::Location loc;
                        
                        loc.mLongitude = pPlace.location.coordinate.longitude;
                        loc.mLatitude = pPlace.location.coordinate.latitude;
                        
                        loc.mName = "";
                        NSString* adminArea = @"";
                        if(pPlace.administrativeArea != nil && ![pPlace.administrativeArea isEqual: @""])
                        {
                            appendLocationName(loc.mName, [pPlace.administrativeArea UTF8String]);
                            adminArea = pPlace.administrativeArea;
                        }
                        
                        if(pPlace.subAdministrativeArea != nil && ![pPlace.subAdministrativeArea isEqual:@""])
                        {
                            appendLocationName(loc.mName, [pPlace.subAdministrativeArea UTF8String]);
                        }
                        
                        if(pPlace.locality != nil && ![pPlace.locality isEqual:@""])
                        {
                            if(![pPlace.locality isEqual:adminArea])
                            {
                                appendLocationName(loc.mName, [pPlace.locality UTF8String]);
                            }
                        }
                        
                        if(pPlace.subLocality != nil && ![pPlace.subLocality isEqual:@""])
                        {
                            appendLocationName(loc.mName, [pPlace.subLocality UTF8String]);
                        }
                        
                        pLocDetector->addLocation(loc);
                        bGetPlace = true;
                    }
                    
                    pLocDetector->onFinishDetect(bGetPlace);
                }
                else
                {
                    pLocDetector->onFinishDetect(false);
                }
            }];
        }
    }
}

@end

//////////////////////////////////////////////////////////////////////////////////////////////////

static LocationManagerWraper* sWraper = NULL;

LocationDetector::LocationDetector()
{
    if(!sWraper)
    {
        sWraper = [[LocationManagerWraper alloc]init];
    }
}

LocationDetector::~LocationDetector()
{
    if(sWraper)
    {
        [sWraper release];
        sWraper = NULL;
    }
}

bool LocationDetector::isAvailable() const
{
    return [CLLocationManager locationServicesEnabled] == YES;
}

bool LocationDetector::isAllowed() const
{
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    return status == kCLAuthorizationStatusAuthorized || status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways;
}

void LocationDetector::startDetect()
{
    if(!sWraper)  // 未知错误
    {
        onFinishDetect(false);
        return;
    }
    
    CLAuthorizationStatus status = [sWraper getAuthorizationStatus];
    if(status == kCLAuthorizationStatusDenied || status == kCLAuthorizationStatusRestricted)  // 用户不允许本app使用定位服务
    {
        cocos2d::CCMessageBox("您未授权本游戏使用定位功能", "提醒");
        onFinishDetect(false);
        return;
    }
    else if(status == kCLAuthorizationStatusNotDetermined)  // 用户还没决定本app是否可以使用定位服务，那么请求一下
    {
        [sWraper requestAuthorization];
        onFinishDetect(false);
        return;
    }
    
    if(isAllowed())
    {
        [sWraper startDetect];
    }
}