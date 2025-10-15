// test.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <string>
#include <vector>
#include "../include/xmlconfig.h"

#include "beans/knight/gsp/battle/CBattleMagicConfig.h"

int _tmain(int argc, _TCHAR* argv[])
{
	XMLCONFIG::Initialize( L"D:\\projects\\gameproj\\doc\\data\\clientxml", L"d:\\temp");

	knight::gsp::battle::CBattleMagicConfigTable& table = knight::gsp::battle::GetCBattleMagicConfigTableInstance();
	const knight::gsp::battle::CBattleMagicConfig& recorder = table.getRecorder( 2);
	XMLCONFIG::Uninitialize();
	return 0;
}

