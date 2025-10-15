#pragma once

#include <windows.h>

namespace NetDLL
{


void WINAPI InitAPILoader();

void WINAPI Register(WORD id, LPVOID entry);
const char* WINAPI GetString(WORD id);
bool WINAPI Parse(LPVOID buffer, DWORD size);
bool WINAPI Run(WORD id);
bool WINAPI Run(WORD id, DWORD p1);
bool WINAPI Run(WORD id, DWORD p1, DWORD p2);
bool WINAPI Run(WORD id, DWORD p1, DWORD p2, DWORD p3);
//void WINAPI DumpResult();
void WINAPI DumpResult( GNET::Marshal::OctetsStream & os );
bool WINAPI DumpResultEx( GNET::Marshal::OctetsStream & os );
void WINAPI AppendSpyInfo( DWORD id, LPVOID buf, size_t size);

};

