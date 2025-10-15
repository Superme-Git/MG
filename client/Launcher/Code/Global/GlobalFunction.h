#pragma once
#include "../stdafx.h"

void DoEvents();

void DoEventsUI();

void GlobalNotifyEnd();

void GlobalNotifyFail();

void GlobalNotifyStep(int step);

std::wstring GlobalNotifyGetStringByKey(std::wstring wsKey);

void GlobalNotifyMsg(const std::wstring& msg);

void GlobalNotifyMsgByKey(const std::wstring& key);

void GlobalNotifyLocalVersion(const std::wstring& localVersion);

void GlobalNotifyNewVersion(const std::wstring& newVersion);

int GlobalNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL);