#include "ljfmfsmanager.h"

namespace LJFM
{
    LJFMFSManager::LJFMFSManager() {}
	LJFMFSManager::~LJFMFSManager(){}
    
    LJFMFSManager* LJFMFSManager::GetLJFMFSManager()
	{
        if (!instance)
		{
            instance = new LJFMFSManager();
            int temp = 0x01;
            uint8_t *t = (uint8_t*)&temp;
            if (*t != 0x01)
			{
                isLE = false;
            }
        }
        return instance;
    }

	void LJFMFSManager::ReleaseLJFMFSManager()
	{
		if (instance)
		{
			delete instance;
			instance = NULL;
		}
	}

    LJFMFS* LJFMFSManager::GetFSPtr(LJFM::LJFMBF& file)
	{
        LJFMFS *stream = NULL;
        if(isLE)
		{
            stream = new LJFMFSLE(file);
        }
		else
		{
            stream = new LJFMFSBE(file);
        }
        return stream;
    }
    
    bool LJFMFSManager::ReleaseFSPtr(LJFM::LJFMFS *fs)
	{
		if (fs) delete fs;
        return true;
    }
    
    LJFMFSManager* LJFMFSManager::instance = NULL;
    bool LJFMFSManager::isLE = true;

}