#include "stdafx.h"
#include "pobject.h"
#include "pfsutil.h"

namespace Nuclear
{

	bool PObject::SaveToMem(XBuffer &o) const
	{
		XOStream os;
		try {
			marshal(os);
		}
		catch(XMarshal::Exception)
		{
			return false;
		}
		o = os;
		return true;
	}

	bool PObject::LoadFromMem(const XBuffer &o)
	{
		XIStream os(o);
		try {
			unmarshal(os);
		}
		catch(XMarshal::Exception)
		{
			return false;
		}
		return true;
	}

	bool PObject::LoadFromMem(const void* buf, int size)
	{
		return LoadFromMem(XBuffer(buf, size));
	}

	bool PObject::Save(FILE* f) const
	{
		XBuffer os;
		if( !SaveToMem(os)) return false;
		return FWrite(f, os.constbegin(), os.size());
	}

	bool PObject::Load(FILE* f)
	{
		if( f == NULL || -1 == fseek(f, 0, SEEK_END) ) return false;
		int len = ftell(f);
		if( -1 == len || -1 == fseek(f, 0, 0) ) return false;

		XBuffer os;
		os.resize(len);
		if( 0 != FRead(f, os.normalbegin(), len) )
		{
			if( LoadFromMem(os) )
				return true;
		}
		return false;
	}

	bool PObject::Save(PFS::CBaseFile* cf) const
	{
		XBuffer os;
		if( !SaveToMem(os)) return false;

		return FWrite(cf, os.constbegin(), os.size());		
	}

	bool PObject::Load(PFS::CBaseFile* cf)
	{
		if( cf == NULL ) return false;
		PFS::CImageData imgdata = cf->GetImage();
		XBuffer tbuf;
		if( imgdata.GetSize() != 0 )
		{
			//tbuf.replace(imgdata.GetData(), (size_t)imgdata.GetSize());	
			tbuf.SetOctets(XFileOctets(XHardRef<PFS::CImageData>(new PFS::CImageData(imgdata))));
		}
		return LoadFromMem(tbuf);
	}

	bool PObject::Save(LPCTSTR fn) const
	{
		PFS::CFile cf;
		if( !cf.Open(std::wstring(fn), PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY ))
			return false;
		bool r = Save(&cf);
		cf.Close();
		return r;
	}

	bool PObject::Load(LPCTSTR fn)
	{
		PFS::CFile cf;
		if( !cf.Open(std::wstring(fn), PFS::FM_EXCL, PFS::FA_RDONLY ))
			return false;
		bool r = Load(&cf);
		cf.Close();
		return r;
	}
	
		// ´Ó native Â·¾¶´æÈ¡
	bool PObject::SaveToNativePath(LPCTSTR fn) const
	{
		FILE *f = FOpen(fn, L"wb");
		if( f == NULL )
			return false;
		bool r = Save(f);
		fclose(f);
		return r;
	}

	bool PObject::LoadFromNativePath(LPCTSTR fn)
	{
		FILE *f = FOpen(fn, L"rb");
		if( f == NULL )
			return false;
		bool r = Load(f);
		fclose(f);
		return r;
	}

}


