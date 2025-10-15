#!/bin/bash
sudo cp /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/Library/PrivateFrameworks/DTDeviceKit.framework/Versions/A/Resources/symbolicatecrash /usr/local/bin 

export DEVELOPER_DIR="/Applications/Xcode.app/Contents/Developer/"

sudo xcode-select -switch /Applications/Xcode.app/Contents/Developer

#symbolicate [crash file]
