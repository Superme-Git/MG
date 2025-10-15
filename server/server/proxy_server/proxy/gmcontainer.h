
#pragma once

#include "rpcgen.hpp"
#include <vector>

namespace gdeliverd
{
	class GMContainer
	{
		private:
			typedef std::map<int,std::vector<int> > GMMap;
			GMMap gmmap;
		public:
			static GMContainer * GetInstance()
			{
				static GMContainer instance;
				return &instance;
			}

			std::vector<int>* GetGMInfo(int userid)
			{
				GMMap::iterator it = gmmap.find(userid);
				if(it != gmmap.end())
				{
					return &(it->second);
				}
				return NULL;
			}

			void InsertGMInfo(int userid,std::vector<int>& auth)
			{
				gmmap[userid] = auth;
			}

			void RemoveGMInfo(int userid)
			{
				gmmap.erase(userid);	
			}
	};
}
