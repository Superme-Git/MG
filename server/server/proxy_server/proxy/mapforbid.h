#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/RoleForbid.hpp"

namespace gdeliverd
{

	class ForbidLogin
	{
		private:	
			typedef std::map<int/*userid*/,gnet::RoleForbid> ForbidMap;
			ForbidMap map;
		public:
			static ForbidLogin * GetInstance()
			{
				static ForbidLogin instance;
				return &instance;
			}
			ForbidLogin() {}

			size_t Size() 
			{
				 return map.size();
			}
			void Update( int update_time )
			{
				for (ForbidMap::iterator it = map.begin(); it != map.end();)
				{
					it->second.time -= update_time;
					if(it->second.time <= 0)
						map.erase(it++);	
					else
						++it;
				}
			}
			bool GetForbid( int userid, gnet::RoleForbid& forbid )
			{
				ForbidMap::iterator it = map.find( userid );
				if (it != map.end())
				{
					forbid = it->second;
					return true;
				}
				return false;
			}
			void PutForbid( int userid, gnet::RoleForbid& forbid )
			{
				map[userid] = forbid;
			}
			void RmvForbid( int userid )
			{
				map.erase(userid);
			}
	};

	/* Check forbidlogin user map periodically, and remove timeout items
	*/  
	class CheckForbidTimer : public GNET::Thread::Runnable
	{
		int update_time;
		CheckForbidTimer(int _time,int _proir=1) : Runnable(_proir),update_time(_time) { }
		public:
		static CheckForbidTimer* GetInstance(int _time=60,int _proir=1) {
			static CheckForbidTimer instance(_time,_proir);
			return &instance;
		}	

		void Run()
		{
			ForbidLogin::GetInstance()->Update( update_time );
			GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
		}
	};

};


