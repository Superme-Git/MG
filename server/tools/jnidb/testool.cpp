#include <iostream>
#include <sstream>
#include <cstdio>
#include <cstring>
#include <cassert>

#include "core/io/compress.h"
#include "core/common/octets.h"

//#include <string>
#include "dbhelper.hpp"


class _MyQueryData : public GNET::IQueryData
{
private:
	bool		_isCom;
	FILE* 		_file;

public:
	_MyQueryData(bool isCom, FILE* file) : _isCom(isCom), _file(file) { } 

	virtual bool update( const void *key, size_t key_len, const void *val, size_t val_len );
};

bool _MyQueryData::update( const void *key, size_t key_len, const void *data, size_t data_len )
{
	__xdb_helper::Value val;
	if (false == _isCom) {
		fprintf(stdout, "======    raw data    ======\n");
		fprintf(_file, "[key=");
		for (size_t i = 0; i < key_len; i++) 
			fprintf(_file, "%02x", *((const unsigned char*)key+i));
		fprintf(_file, ",");
		for (size_t i = 0; i < data_len; i++)
			fprintf(_file, "%c", *((const unsigned char*)data+i));
		fprintf(_file, "]\n");	

		return true;
	} else { 
		__xdb_helper::uncompress(data, data_len, val); 
		fprintf(stdout, "======    compress data    ======\n");
		fprintf(_file, "[key=");
		for (size_t i = 0; i < key_len; i++) 
			fprintf(_file, "%02x", *((const unsigned char*)key+i));
		fprintf(_file, ",");
		for (size_t i = 0; i < val.size(); i++)
			fprintf(_file, "%c", *((const unsigned char*)val.data()+i));
		fprintf(_file, "]\n");	

		return true;
	}

	// uncompress error? ignore
	fprintf(_file, "uncompress error!\n");
	return true;
}

// 测试压缩解压后与原数据比较
void testCorrect(const char* pTest, unsigned int testsize)
{
	__xdb_helper::Value v1;
	__xdb_helper::compress(pTest, testsize, v1);

	__xdb_helper::Value v2;
	__xdb_helper::uncompress(v1.data(), v1.size(), v2);

	assert(testsize == v2.size());
	assert(0 == memcmp(v2.data(), pTest, testsize));
}

// 测试压缩解压过程和io/compress.h压缩算法比较
void testCompare(const char* pTest, unsigned int testsize)
{
	__xdb_helper::Value v1;
	__xdb_helper::compress(pTest, testsize, v1);
	
	GNET::Octets data1(pTest, testsize);
	GNET::Octets result1;
	GNET::Compress(data1, result1);

	assert(v1.size() == result1.size()); // 压缩后的长度比较
	assert(0 == (memcmp(v1.data(), result1.begin(), v1.size()))); // 压缩后的字节比较

	__xdb_helper::Value v2;
	__xdb_helper::uncompress(v1.data(), v1.size(), v2);

	GNET::Octets result2;
	GNET::Uncompress(result1, result2);
	assert(v2.size() == testsize);
	assert(result2.size() == testsize);
	assert(0 == (memcmp(v2.data(), pTest, testsize)));
	assert(0 == (memcmp(result2.begin(), pTest, testsize)));
}


/**
 * 对标准输入的字符串序列进行压缩解压测试，以及和share/io/compress.h中的算法比较
 */
int main(int argc, char* argv[])
{
	std::stringstream ss;
	ss << std::cin.rdbuf();

	std::string s = ss.str();

	//std::cout << s << std::endl;

	testCorrect(s.c_str(), s.size());	
	testCompare(s.c_str(), s.size());
}


/*
int main(int argc, char* argv[])
{
	GNET::__db_helper::NullLogger log1;
	GNET::__db_helper::NullLogger log2;
	GNET::__db_helper::NullLogger log3;
	FILE* pf1 = fopen("content1.txt", "w");
	FILE* pf2 = fopen("content2.txt", "w");
	FILE* pf3 = fopen("content3.txt", "w");

	__xdb_helper::Storage s1(&log1, "./uncomdb/t3", 4096, 4096);
	_MyQueryData qd1(false, pf1);		
	s1.walk(&qd1);

	__xdb_helper::Storage s2(&log2, "./uncomdb_c/t3", 4096, 4096);
	_MyQueryData qd2(true, pf2);		
	s2.walk(&qd2);

	__xdb_helper::Storage s3(&log3, "./uncomdb_c_d/t3", 4096, 4096);
	_MyQueryData qd3(false, pf3);		
	s3.walk(&qd3);

	return 0;
}
*/









