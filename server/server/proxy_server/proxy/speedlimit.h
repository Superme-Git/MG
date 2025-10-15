#pragma once

#include "rpcgen.hpp"
#include <queue>
#include <time.h>

namespace gdeliverd
{
	//精度考虑，deadline采用了秒，
	template <typename K>
	struct	Record
	{
		K key;
		int count;	
		time_t deadline;
		Record<K>(K _key, int window = 0) : key(_key),count(1),deadline(window + time(NULL)) {}
		bool isTimeout(time_t deadline)
		{
			return this->deadline <= deadline;
		}
		bool update(int high)
		{
			return count++ < high;
		}
		bool isLimit(int high)
		{
			return count < high;
		}
	};
	template<typename K>
	struct RecordCompare
	{
		bool operator() (const Record<K> & lh,const Record<K> & rh)
		{
			return lh.deadline > rh.deadline;
		}
	};

	template <typename TKey,typename Compare>
	class	SpeedLimit
	{
		int	HIGH;
		int	WINDOW;

		typedef	std::map< TKey,Record<TKey>,Compare>	MapType;
		typedef typename MapType::iterator 		Iterator;
		typedef std::priority_queue<Record<TKey>,std::vector<Record<TKey> >,RecordCompare<TKey> > PriorQueue;
		MapType	smap;
		PriorQueue priorQuque;
	public:
		SpeedLimit() { }	
		~SpeedLimit()
		{
			smap.clear();
		}

		void config(int window,int high)
		{
			HIGH	= high;
			WINDOW	= window;
		}
		//true:can add,flase:can not add 
		bool add(const TKey& key)
		{
			time_t now = time(NULL);
			while((!priorQuque.empty()) && ((Record<TKey>)(priorQuque.top())).isTimeout(now))
			{
				TKey i = priorQuque.top().key;
				priorQuque.pop();
				smap.erase(i);
			}
			Iterator iter = smap.find(key);
			if(iter != smap.end())
			{
				return iter->second.update(HIGH);
			}	

			Record<TKey> rd(key,WINDOW);
			priorQuque.push(rd);
			smap.insert(typename MapType::value_type(key,rd));
			return true;
		}
		bool ask(const TKey& key)
		{
			time_t now = time(NULL);
			while((!priorQuque.empty()) && ((Record<TKey>)(priorQuque.top())).isTimeout(now))
			{
				TKey i = priorQuque.top().key;
				priorQuque.pop();
				smap.erase(i);
			}
			Iterator iter = smap.find(key);
			if(iter != smap.end())
			{
				return iter->second.isLimit(HIGH);
			}	
			return	true;
		}

		void clear(const TKey& key)
		{
			smap.erase(key);
		}
	
		void dump()
		{
			while (!priorQuque.empty())
			{
				TKey i = priorQuque.top().key;
				priorQuque.pop();
				std::cout << i << " ";
			}
			std::cout << std::endl;

		}
	};
}
