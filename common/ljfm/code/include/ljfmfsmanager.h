#pragma once
#ifndef LJFMFSManager_H
#define LJFMFSManager_H

#include "ljfmfs.h"

namespace LJFM
{
	class LJFMFSManager
	{
	private:
		static LJFMFSManager *instance;
		static bool isLE;
    
	private:
		LJFMFSManager();
		~LJFMFSManager();
    
	public:
		static LJFMFSManager* GetLJFMFSManager();
		static void ReleaseLJFMFSManager();
    
	public:
		LJFMFS* GetFSPtr(LJFM::LJFMBF& );
		bool ReleaseFSPtr(LJFMFS* );
	};
}

#endif