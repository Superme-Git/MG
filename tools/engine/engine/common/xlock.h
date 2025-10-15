#ifndef __Nuclear_XLOCK_H
#define __Nuclear_XLOCK_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{

class XCSLock
{
	// ��������
public:
	XCSLock();
	~XCSLock();

public:
	void Lock();
	void Unlock();
	void SetSpinCount(int count);

private:
	CRITICAL_SECTION m_cs;
	// ռ��
private:
	XCSLock(const XCSLock&);
	XCSLock& operator=(const XCSLock&);

};

};

#endif
