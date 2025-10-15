#ifndef LJFMUTIL_H
#define LJFMUTIL_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <ljfmext.h>
#include "../common/nuxref.h"
#include "nuxbuffer.h"

namespace Nuclear
{

	bool FRead(LJFM::LJFMBF *cf, void *buf, size_t size);
	bool FWrite(LJFM::LJFMBF *cf, const void *buf, size_t size);

	class LJFMLog : public LJFM::COutLog
	{
	public:
		virtual ~LJFMLog() { }
		virtual void VPrint( const wchar_t* info, va_list vl);
	};

	class NuclearFileOctets : public NuclearOctets
	{
	private:
		NuclearHardRef<LJFM::LJFMID> m_pImgData;
		
	public:
		NuclearFileOctets(NuclearHardRef<LJFM::LJFMID> pImgData) : m_pImgData(pImgData) {}
		virtual NuclearOctets& reserve(size_t size);
		virtual NuclearOctets& replace(const void *data, size_t size);
		virtual ~NuclearFileOctets() {}

		virtual NuclearOctets& insert(void *pos, const void *x, size_t len);
		virtual NuclearOctets& insert(void *pos, const void *x, const void *y);
		virtual NuclearOctets& resize(size_t size);
		virtual NuclearOctets *clone() const { return new NuclearFileOctets(m_pImgData); }
		virtual int GetRef() const { return m_pImgData.GetHardRefCount(); }
		virtual void SetTick(int tick) { m_pImgData.SetTick(tick); }
		virtual int GetTick() const { return m_pImgData.GetTick(); }
		
		virtual void *begin();
		virtual void *end();
		virtual const void *constbegin() const { return m_pImgData->GetData(); }
		virtual const void *constend() const { return ((unsigned char*)m_pImgData->GetData()) + size(); }
		virtual size_t size() const { return (size_t)m_pImgData->GetSize(); }
		virtual size_t capacity() const { return (size_t)m_pImgData->GetSize(); }
		virtual NuclearOctets& clear();
		virtual NuclearOctets& erase(void *x, void *y);
	};

}

#endif