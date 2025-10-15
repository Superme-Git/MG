#ifndef UpdateManagerEx_Win_H
#define UpdateManagerEx_Win_H

#include <string>
#include "Singleton.hpp"
#include "ljfmext.h"

class UpdateManagerEx : public CSingleton <UpdateManagerEx>
{
public:

	std::wstring m_RootPath;
	std::wstring m_RootResPath;
	std::wstring m_CacheResPath;

	void* m_pVerRoot;
	void* m_pVerCache;
    
    unsigned int m_VersionOld;
    std::wstring m_VersionOldCaption;
    unsigned int m_Version;
    std::wstring m_VersionCaption;
	unsigned int m_VersionBase;
	std::wstring m_VersionBaseCaption;
	unsigned int m_Channel;
	std::wstring m_ChannelCaption;

	std::map<std::wstring, std::wstring> m_ExtendMap;

	UpdateManagerEx();
	~UpdateManagerEx();

	int DelOldResFile();

	int StepInit();
	int StepInitPath();
	int StepLoadVersion();
	virtual bool Run();
	
	int StepLoadVersionNoPack();
	virtual bool RunNoPack();
    
    virtual void Continue(int iResult);

	void CloneExtendMap(std::map<std::wstring, std::wstring>& ExtendMap);
};


#endif //UpdateManagerEx_Win_H
