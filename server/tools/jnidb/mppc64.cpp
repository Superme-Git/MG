
#include "dbhelper.hpp"
#include <assert.h>
#include <time.h>

// this file deprecated
int main(int argc, char * argv[])
{
	char origin[12383];
	int count = 0;
	for (int i = 0; i < sizeof(origin); ++i) {
		std::cout << (int)origin[i] << ",";
		++count;
		if (count == 16)
			count = 0;
	}
	/*
	FILE * fmppc = fopen("mppc.dat", "r");
	if (NULL != fmppc) {
		fread(origin, 1, 12383, fmppc);
		fclose(fmppc);
	} else {
		FILE * fmppc = fopen("mppc.dat", "w");
		fwrite(origin, 1, 12383, fmppc);
		fclose(fmppc);
	}
	*/

	__xdb_helper::Value compressed;
	__xdb_helper::compress(origin, sizeof(origin), compressed);

	__xdb_helper::Value uncompressed;
	__xdb_helper::uncompress(compressed.data(), compressed.size(), uncompressed);

	std::cout << "uncompress.size=" << uncompressed.size() << " origin.size=" << sizeof(origin) << std::endl;

	//assert(uncompressed.size() == sizeof(origin));
	//assert(memcmp(uncompressed.data(), origin, sizeof(origin)) == 0);

	//inline void __xdb_helper::compress(const void* src, size_t src_len, Value& out) 
	//inline void __xdb_helper::uncompress(const void* com, size_t com_len, Value& out) 
	return 0;
}
