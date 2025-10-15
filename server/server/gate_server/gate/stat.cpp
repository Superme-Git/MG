#include "stat.hpp"
#include "LinkServer.hpp"

namespace glinkd
{
	void Stat::StatTask()
	{
		char tmp[32];
		ProtocolStat* ps = ProtocolStat::GetInstance();
		std::string result = "provider --> client:";
		for(ProtocolStat::Map::iterator itor = ps->g2c_map.begin();itor != ps->g2c_map.end();++itor)
		{
		   ps->sortmap.insert(make_pair(itor->second,itor->first));
		}

		ProtocolStat::SortMap::iterator it = ps->sortmap.begin();
		for(int i = 0;i < 10 && it!= ps->sortmap.end();++i)
		{
			sprintf(tmp,"%d-%d:%ld,",GNET::Protocol::typeHigh(it->second),GNET::Protocol::typeLow(it->second),it->first);
			result += tmp;
			it++;
		}
		GNET::Log::log(LOG_INFO,"StatTask: %s total size :%ld ",result.c_str(),ps->g2c_num);
		ps->sortmap.clear();
		ps->g2c_map.clear();
		ps->g2c_num = 0;


		result = "client --> provider:";
		for(ProtocolStat::Map::iterator itor = ps->c2g_map.begin();itor != ps->c2g_map.end();itor++)
                {
                        ps->sortmap.insert(make_pair(itor->second,itor->first));
                }
		it = ps->sortmap.begin();
                for(int i = 0;i < 10 && it!= ps->sortmap.end();++i)
                {
			sprintf(tmp,"%d-%d:%ld,",GNET::Protocol::typeHigh(it->second),GNET::Protocol::typeLow(it->second),it->first);
                        result += tmp;
                        it++;
                }
                GNET::Log::log(LOG_INFO,"StatTask: %s total size :%d ",result.c_str(),ps->c2g_num);
		ps->sortmap.clear();
		ps->c2g_map.clear();
		ps->c2g_num = 0;
	}
}
