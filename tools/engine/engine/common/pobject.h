#ifndef __Nuclear_POBJECT_H
#define __Nuclear_POBJECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "xmarshal.h"

namespace PFS
{
	class CBaseFile;
}

namespace Nuclear
{

class PObject : public XMarshal
{
	// ��������
public:
	PObject() {}
	virtual ~PObject() {  }
public:

	// ��������
	bool SaveToMem(XBuffer&) const;
	bool LoadFromMem(const XBuffer&);
	bool LoadFromMem(const void*, int size);
	
	bool Save(FILE*) const ;
	bool Load(FILE*);

	
	bool Save(PFS::CBaseFile*) const ;
	bool Load(PFS::CBaseFile*);

	// �� pfs ·����ȡ
	bool Save(LPCTSTR fn) const ;
	bool Load(LPCTSTR fn);

	// �� native ·����ȡ
	bool SaveToNativePath(LPCTSTR fn) const ;
	bool LoadFromNativePath(LPCTSTR fn);
	

};


}


#endif