///=============================================================================
/// Product   : Nuclear
/// Project   : xmliolib
/// Module    : XMLIOHeapManager.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	A separated heap manager of the XMLIO library
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-8-27 Created.
///=============================================================================
#pragma once

class CXMLIOHeapManager
{
protected:
	CXMLIOHeapManager(void);
	~CXMLIOHeapManager(void);
	HANDLE m_hHeap;

public:
	static CXMLIOHeapManager& GetInstance();

public:
	BOOL Create(SIZE_T dwInitialSize,SIZE_T dwMaximumSize);

	BOOL Destroy();

	LPVOID Alloc(SIZE_T dwBytes);

	BOOL Free(LPVOID lpMem);

	SIZE_T Size(LPCVOID lpMem);
};
