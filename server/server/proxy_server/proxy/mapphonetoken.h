#pragma once

namespace gdeliverd 
{

class PhoneTokenInfo
{
public:
        GNET::Octets	seed;
        int		rtime;
public:
        PhoneTokenInfo() { }
        PhoneTokenInfo(const GNET::Octets& s,int r) : seed(s),rtime(r)  {}
        PhoneTokenInfo(const PhoneTokenInfo& r) : seed(r.seed),rtime(r.rtime)  {}
	void GetKeyInfo(GNET::Octets& s, int& r)
	{
		s  = seed;
		r = rtime;
	}
};


class PhoneTokenCache
{
	private:
		typedef	GNET::simplelru<int,PhoneTokenInfo,std::less<int> >	CacheLRUMap;
		typedef GNET::simplelru<int,GNET::Octets,std::less<int> >	Int2OctetsLRUMap;
		CacheLRUMap							phonemap;
		Int2OctetsLRUMap						usedelecmap;
	public:
		static 	int time_diff;//au.currenttime - deliver.time(NULL).ignore network delay
		PhoneTokenCache():phonemap(200000),usedelecmap(200000) { }
		static PhoneTokenCache* GetInstance()
		{
			static PhoneTokenCache instance;
			return &instance;
		}

		void SetMaxSize(size_t size) 
		{
			if(size <= 200000)
			{
				phonemap.setMaxSize(200000);
				usedelecmap.setMaxSize(200000);
			}
			else 
			{
				phonemap.setMaxSize(size); 
				usedelecmap.setMaxSize(size);	
			}
		}

		bool GetPhoneTokenInfo(int userid,GNET::Octets& seed,int& rtime,int& currenttime )
		{
			PhoneTokenInfo info;
			if(phonemap.find(userid,info))
			{
				info.GetKeyInfo(seed,rtime);
				currenttime = time_diff + (int)time(NULL);//time_t is int64_t in x64
				return true;
			}
			return false;
		}

		void SetPhoneTokenInfo(int userid,GNET::Octets& seed,int rtime,int currenttime)
		{
			time_diff = currenttime - (int)time(NULL);
			phonemap.put(userid,PhoneTokenInfo(seed,rtime));
		}

		void SetUsedElecNumber(int userid,GNET::Octets& used_elec_number)
		{
			usedelecmap.put(userid,used_elec_number);	
		}
	
		bool GetUsedElecNumber(int userid,GNET::Octets& elec_number)
		{
			if(usedelecmap.find(userid,elec_number))
				return true;
			return false;
		}
};

}


