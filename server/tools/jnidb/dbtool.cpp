#include <fcntl.h>
#include <dirent.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>

#include <string>
#include <iostream>
#include <deque>
#include <memory>

#include "dbhelper.hpp"

namespace __xdb_helper {
	// note: After walk, also need to commit() !!!!!
	class RQueryData : public GNET::IQueryData {
	private:
		GNET::DB* _pd;
		GNET::Logger* _pl;
		bool _toCom;
		int _count;

		RQueryData(const RQueryData&);
		void operator=(const RQueryData&);
	public:
		RQueryData(GNET::DB* pd, GNET::Logger* pl, bool toCom) : _pd(pd), _pl(pl), _toCom(toCom), _count(0) { }	

		bool update(const void* key, size_t klen, const void* data, size_t dlen) {
			if (_toCom) {
				Value val; 
				compress(data, dlen, val);
				_pd->put(key, klen, val.data(), val.size(), false);
			} else {
				Value val;
				uncompress(data, dlen, val);
				_pd->put(key, klen, val.data(), val.size(), false);	
			}
			if (0 == ++_count%1000) {
				_count = 0;
				_pd->snapshot_create();
				_pl->commit(0);
				_pd->snapshot_release();
			}	
			return true;
		}
	};

}

#define Trace(s) std::cerr << #s << std::endl; s
static bool recover(const std::string& fromfile, const std::string& tofile, bool toCom) {
	try {
		
		//GNET::__db_helper::NullLogger log_from;
		GNET::__db_helper::NullLogger log_to;
		printf("%s\n", fromfile.c_str());

		//__xdb_helper::Storage sfrom(&flog, fromfile.c_str(), 4096, 8192);			
		//__xdb_helper::Storage sto(&tlog, tofile.c_str(), 4096, 8192);
		GNET::__db_helper::PageFile pagefile_from(fromfile.c_str()); 
		GNET::__db_helper::PageCache pagecache_from(&pagefile_from, 4096, 8192); 
		//GNET::__db_helper::DB db_from;
		//db_from.page_cache = pagecache_from;
		//log_from.init(pagefile_from, pagecache_from);

		GNET::__db_helper::PageFile pagefile_to(tofile.c_str()); 
		GNET::__db_helper::PageCache pagecache_to(&pagefile_to, 4096, 8192); 
		GNET::DB db_to;
		db_to.page_cache = &pagecache_to;
		log_to.init(&pagefile_to, &pagecache_to);

		__xdb_helper::RQueryData rqd(&db_to, &log_to, toCom);
		pagecache_from.walk(&rqd);

		db_to.snapshot_create();
		log_to.commit(0);
		db_to.snapshot_release();
	} catch (...) {
		fprintf(stderr, "Error: Catch Exception from DB\n");
		return false;		
	}
	
	return true;
}
#undef Trace


int main(int argc, char* argv[]) 
{
	DIR* pDirFrom = NULL;
	bool toCom = true;
	
	if (argc != 3) {
		fprintf(stderr, "Usage: dbtool [-d | -c] [DBdir] \n");	
		exit(1);
	} 

	if (0 == strcmp(argv[1], "-d")) {
		toCom = false;			
	} else if (0 == strcmp(argv[1], "-c")) {
		toCom = true;	
	} else {
		fprintf(stderr, "Usage: dbtool [-d | -c] [DBdir] \n");	
		exit(1);
	}

	pDirFrom = opendir(argv[2]);		
	if (NULL == pDirFrom) {
		fprintf(stderr, "Error: formDBdir is not exit!\n");
		closedir(pDirFrom);
		exit(1);
	}

	std::string fromdir;
	std::string todir;
	fromdir += argv[2];
	if ('/' != fromdir[fromdir.size() - 1]) { // add slash
		todir = toCom ? (fromdir + "_c/") : (fromdir + "_d/");
		fromdir += '/';	
	} else {
		todir = toCom ? (fromdir.substr(0, fromdir.size() - 1) + "_c/") : (fromdir.substr(0, fromdir.size() - 1) + "_d/");	
	}
	mkdir(todir.c_str(), S_IRWXU);

	dirent* de = readdir(pDirFrom);
	while (de != NULL) {
		std::string fromfile = fromdir + de->d_name;
		std::string tofile = todir + de->d_name;
		struct stat statbuf;
		de = readdir(pDirFrom);		
		
		if (0 == lstat(fromfile.c_str(), &statbuf) && 0 != S_ISREG(statbuf.st_mode)) { // regular file
			remove(tofile.c_str()); // overwrite
			if (true == recover(fromfile, tofile, toCom)) {
				printf("recover %-30s Success!  [to %s]\n", fromfile.c_str(), tofile.c_str());
			} else {
				printf("recover %-30s Failure!\n", fromfile.c_str());	
			}
			
			//std::cout << "recover " + fromfile 
			//		  << ( recover(fromfile, tofile) ? + " to " + tofile : " failure!" ) << std::endl;
		}
		
	}

#ifdef __STATISTIC
	if (0 != __xdb_helper::__total_uncom) {
		printf("compress percentum: %f%%\n", (double)__xdb_helper::__total_com * 100/(double)__xdb_helper::__total_uncom);
	}
#endif
		
	return 0;
}












