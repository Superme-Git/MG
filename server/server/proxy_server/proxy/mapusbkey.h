#pragma once

namespace gdeliverd 
{

class UsbKeyInfo
{
public:
        GNET::Octets	seed;
        GNET::Octets	pin;
        int		rtime;
public:
        UsbKeyInfo() { }
        UsbKeyInfo(const GNET::Octets& s,const GNET::Octets& p,int r) : seed(s),pin(p),rtime(r)  {}
        UsbKeyInfo(const UsbKeyInfo& r) : seed(r.seed),pin(r.pin),rtime(r.rtime)  {}
	void GetKeyInfo(GNET::Octets& s, GNET::Octets& p, int& r)
	{
		s  = seed;
		p = pin;
		r = rtime;
	}

};


class UsbKeyCache
{
	private:
		typedef	GNET::simplelru<int,UsbKeyInfo,std::less<int> >		CacheLRUMap;
		typedef GNET::simplelru<int,GNET::Octets,std::less<int> >	Int2OctetsLRUMap;
		CacheLRUMap							usbmap;
		Int2OctetsLRUMap						usedelecmap;
	public:
		static 	int time_diff;//au.currenttime - deliver.time(NULL).ignore network delay
		UsbKeyCache():usbmap(200000),usedelecmap(200000) { }
		static UsbKeyCache* GetInstance()
		{
			static UsbKeyCache instance;
			return &instance;
		}

	//	size_t Size() { return usbmap.size(); }
	//	size_t GetCacheLimit() { return usbmap.getMaxSize(); }
		void SetMaxSize(size_t size) 
		{
			if(size <= 200000)
			{
				usbmap.setMaxSize(200000);
				usedelecmap.setMaxSize(200000);
			}
			else 
			{
				usbmap.setMaxSize(size); 
				usedelecmap.setMaxSize(size);	
			}
		}

		bool GetUsbKeyInfo(int userid,GNET::Octets& seed,GNET::Octets& pin,int& rtime,int& currenttime )
		{
			UsbKeyInfo info;
			if(usbmap.find(userid,info))
			{
				info.GetKeyInfo(seed,pin,rtime);
				currenttime = time_diff + (int)time(NULL);//time_t is int64_t in x64
				return true;
			}
			else
				return false;
		}

		void SetUsbKeyInfo(int userid,GNET::Octets& seed,GNET::Octets& pin,int rtime,int currenttime)
		{
			time_diff = currenttime - (int)time(NULL);
			usbmap.put(userid,UsbKeyInfo(seed,pin,rtime));
		}

		void SetUsedElecNumber(int userid,GNET::Octets& used_elec_number)
		{
			usedelecmap.put(userid,used_elec_number);	
		}
	
		bool GetUsedElecNumber(int userid,GNET::Octets& elec_number)
		{
			if(usedelecmap.find(userid,elec_number))
			{
				return true;
			}
			else
				return false;
		}
};

}


