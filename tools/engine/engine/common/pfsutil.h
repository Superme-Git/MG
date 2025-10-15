#ifndef __Nuclear_PFSUTIL_H
#define __Nuclear_PFSUTIL_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\..\pfs\pfs2.h"
#include "..\common\xref.h"
#include "xbuffer.h"

#define FNExt_ParticleWS L"ptc"
#define FNExt_ParticleS "ptc"


namespace Nuclear
{

	bool FRead(PFS::CBaseFile *cf, void *buf, size_t size);
	bool FWrite(PFS::CBaseFile *cf, const void *buf, size_t size);

	class PFSLog : public PFS::COutLog
	{
	public:
		virtual ~PFSLog() { }
		virtual void VPrint( const wchar_t* info, va_list vl);
	};

	class XFileOctets : public XOctets
	{
	private:
		XHardRef<PFS::CImageData> m_pImgData;
		
	public:
		XFileOctets(XHardRef<PFS::CImageData> pImgData) : m_pImgData(pImgData) {}
		virtual XOctets& reserve(size_t size);
		virtual XOctets& replace(const void *data, size_t size);
		virtual ~XFileOctets() {}
		
		virtual void *begin();
		virtual void *end();
		virtual const void *constbegin() const { return m_pImgData->GetData(); }
		virtual const void *constend() const { return ((unsigned char*)m_pImgData->GetData()) + size(); }
		virtual size_t size() const { return (size_t)m_pImgData->GetSize(); }
		virtual size_t capacity() const { return (size_t)m_pImgData->GetSize(); }
		virtual XOctets& clear();
		virtual XOctets& erase(void *x, void *y);

		// x 不可以 在begin()和end()之间
		virtual XOctets& insert(void *pos, const void *x, size_t len);
		virtual XOctets& insert(void *pos, const void *x, const void *y);
		virtual XOctets& resize(size_t size);
		virtual XOctets *clone() const { return new XFileOctets(m_pImgData); }
		virtual int GetRef() const { return m_pImgData.GetHardRefCount(); }
		virtual void SetTick(int tick) { m_pImgData.SetTick(tick); }
		virtual int GetTick() const { return m_pImgData.GetTick(); }
	};

};

#endif