#include "nupobject.h"
#include "nuljfmutil.h"
#include "utils/StringUtil.h"
#include "utils/FileUtil.h"


namespace Nuclear
{

	bool NuclearObject::Save(FILE* f) const
	{
		NuclearBuffer os;
		if( !SaveToMem(os)) return false;
		return FWrite(f, os.constbegin(), os.size());
	}

	bool NuclearObject::Load(FILE* f)
	{
		if( f == NULL || -1 == fseek(f, 0, SEEK_END) ) return false;
		int len = ftell(f);
		if( -1 == len || -1 == fseek(f, 0, 0) ) return false;

		NuclearBuffer os;
		os.resize(len);
		if( 0 != FRead(f, os.normalbegin(), len) )
		{
			if( LoadFromMem(os) )
				return true;
		}
		return false;
	}

	bool NuclearObject::Save(LJFM::LJFMBF* cf) const
	{
        return false;
	}

	bool NuclearObject::Load(LJFM::LJFMBF* cf)
	{
		if( cf == NULL ) return false;
		LJFM::LJFMID imgdata = cf->GetImage();
		NuclearBuffer tbuf;
		if( imgdata.GetSize() != 0 )
		{
			tbuf.SetOctets(NuclearFileOctets(NuclearHardRef<LJFM::LJFMID>(new LJFM::LJFMID(imgdata))));
		}
		return LoadFromMem(tbuf);
	}

	bool NuclearObject::Save(wchar_t const * fn) const
	{
        return false;
	}

	bool NuclearObject::Load(wchar_t const * fn)
	{
		std::wstring strPath(fn);
		LJFM::LJFMF cf;
		if (!cf.Open(strPath, LJFM::FM_EXCL, LJFM::FA_RDONLY))
			return false;
		bool r = Load(&cf);
		cf.Close();
		return r;
	}

	bool NuclearObject::SaveToMem(NuclearBuffer &o) const
	{
		NuclearOStream os;
		try {
			marshal(os);
		}
		catch(NuclearMarshal::Exception)
		{
			return false;
		}
		o = os;
		return true;
	}

	bool NuclearObject::LoadFromMem(const NuclearBuffer &o)
	{
		INuclearStream os(o);
		try {
			unmarshal(os);
		}
		catch(NuclearMarshal::Exception)
		{
			return false;
		}
		return true;
	}

	bool NuclearObject::LoadFromMem(const void* buf, int size)
	{
		return LoadFromMem(NuclearBuffer(buf, size));
	}
	
	bool NuclearObject::SaveToNativePath(wchar_t const * fn) const
	{
        assert(false);
        return false;
	}

	bool NuclearObject::LoadFromNativePath(wchar_t const * fn)
	{
		std::wstring strPath(fn);
		FILE *f = FOpen((char*)CFileUtil::MakePath(CFileUtil::GetCacheDir().c_str(), ws2s(strPath).c_str()).c_str(), (char*)L"rb");
		if( f == NULL )
			return false;
		bool r = Load(f);
		fclose(f);
		return r;
	}

}


