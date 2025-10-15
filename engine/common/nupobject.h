#ifndef __Nuclear_POBJECT_H__
#define __Nuclear_POBJECT_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuxmarshal.h"

namespace LJFM
{
	class LJFMBF;
}

namespace Nuclear
{
	class NuclearObject : public NuclearMarshal
	{
	public:
		NuclearObject() {}
		virtual ~NuclearObject() {}

	public:
	
		bool Save(FILE*) const ;
		bool Load(FILE*);
	
		bool Save(LJFM::LJFMBF*) const ;
		bool Load(LJFM::LJFMBF*);

		bool Save(wchar_t const * fn) const;
		bool Load(wchar_t const * fn);

		bool SaveToMem(NuclearBuffer&) const;
		bool LoadFromMem(const NuclearBuffer&);
		bool LoadFromMem(const void*, int size);

		bool SaveToNativePath(wchar_t const * fn) const;
		bool LoadFromNativePath(wchar_t const * fn);

	};
}


#endif