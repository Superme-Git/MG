
#pragma once
#include <map>
#include "rpcgen.hpp"

namespace glinkd
{
	class ProtocolStat
	{
	public:	
		typedef std::map<int/*protocol type*/,int64_t/*protocol num*/>	Map;
		typedef std::multimap<int64_t,int,std::greater<int64_t> > 	SortMap;
		Map		g2c_map;
		Map		c2g_map;
		SortMap		sortmap;
		int64_t		g2c_num;
		int64_t		c2g_num;

		ProtocolStat() : g2c_num(0),c2g_num(0) { }

		static ProtocolStat * GetInstance()
		{
			static ProtocolStat instance;
			return &instance;
		}

		void reset()
		{
			g2c_num = 0;
			c2g_num = 0;
			g2c_map.clear();
			c2g_map.clear();
		}

		void update_g2c_map(int protocol_type,int times = 1)
		{
			g2c_map[protocol_type] += times;
		}
		
		void update_c2g_map(int protocol_type,int times = 1)
		{
			c2g_map[protocol_type] += times;
		}	
		
		void update_g2c_num(int data_size)
		{
			g2c_num += data_size;
		}
		
		void update_c2g_num(int data_size)
		{
			c2g_num += data_size;
		}
	};
	
	class Stat : public GNET::Thread::Runnable
	{
	   	int update_time;
	   	public:
		Stat(int _time,int _proir=1) : Runnable(_proir),update_time(_time) { }
		void StatTask();
		static Stat* GetInstance(int _time=60,int _proir=1) {
                        static Stat instance(_time,_proir);
                        return &instance;
                }
		void Run()
                {
                        StatTask();
                        GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
                }
	};

}
