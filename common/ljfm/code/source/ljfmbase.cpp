#include "../include/common.h"
#include "../include/ljfmbase.h"

namespace LJFM
{
	typedef std::map<FS_TYPE, LJFMBase*> LJFMBaseMap;
	static LJFMBaseMap g_mapBases;

	LJFMBase::LJFMBase(FS_TYPE type)
		: m_BaseLJFM(NULL)
		, m_bMetaChanged(false)
		, m_usFilesMetaVersion(0x0100)
	{
		g_mapBases.insert(std::make_pair(type, this));
	}

	LJFMBase* LJFMBase::Create(FS_TYPE type)
	{
		LJFMBaseMap::const_iterator it = g_mapBases.find(type);
		if (it == g_mapBases.end())
		{
			return NULL;
		}
		const LJFMBase* ps = it->second;
		return ps->Clone();
	}

	bool LJFMBase::Destroy()
	{
		return true;
	}
}