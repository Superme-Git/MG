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
	// 构造析构
public:
	PObject() {}
	virtual ~PObject() {  }
public:

	// 基本操作
	bool SaveToMem(XBuffer&) const;
	bool LoadFromMem(const XBuffer&);
	bool LoadFromMem(const void*, int size);
	
	bool Save(FILE*) const ;
	bool Load(FILE*);

	
	bool Save(PFS::CBaseFile*) const ;
	bool Load(PFS::CBaseFile*);

	// 从 pfs 路径存取
	bool Save(LPCTSTR fn) const ;
	bool Load(LPCTSTR fn);

	// 从 native 路径存取
	bool SaveToNativePath(LPCTSTR fn) const ;
	bool LoadFromNativePath(LPCTSTR fn);
	

};


}


#endif