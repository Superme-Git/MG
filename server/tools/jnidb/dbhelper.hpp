#pragma once

#include <stdlib.h>
#include <string.h>

#include "core/io/mppc.h"
#include "core/common/marshal.h"
#ifdef WIN32
#include "core/common/db_win32.h"
#else
#include "core/common/db.h"
#endif

// helper, like smart pointer
//  _____________________________ 
// | offset | length | not cared |
//
namespace __xdb_helper {

class Value
{
private:
	char * value;
	size_t length;
	size_t offset;

	Value(const Value&);
	void operator=(const Value&);
public:
	Value() : value(NULL), length(0), offset(0) { }
	~Value() { if (NULL != value) free(value); }

	void setV(void* v, size_t s, size_t o) {
		if (NULL != value) { // Value control the life of pointer
			free(value);	
		}
		value  = static_cast<char*>(v);
		length = s;
		offset = o;
	}

	char * data() { return value + offset; }
	size_t size() { return length; }

	void dump() {
		if (value != NULL) {
			fprintf(stderr, "[Value Dump]: total len = %lu, off = %lu\n", length + offset, offset );
			fprintf(stderr, "[Value Dump]:[off -- len + off) \"");
			for (size_t i = offset; i < length + offset; i++) {
				fprintf(stderr, "%c", (unsigned char)*(value + i));		
			}
			fprintf(stderr, "\"\n");
		}
	}
};

#define __STATISTIC
#ifdef __STATISTIC
static long int __total_uncom = 0;
static long int __total_com = 0;
#endif

inline void compress(const void* src, size_t src_len, Value& out) 
{
	// max_compactUint = 5 && 10 = 2 * max_compactUint_size
	size_t calculate = GNET::mppc::compressBound(src_len);
	int com_len = calculate > src_len ? calculate : src_len; // in|out
	unsigned char * com = (unsigned char *)malloc(com_len + 10); 

	bool success;
	if (src_len <= 8192)
	{
		success = ( 0 == GNET::mppc::compress(com + 10, &com_len, (const unsigned char*)src, src_len) 
			&& com_len < (int)src_len ); 
	} 
	else
	{
		success = ( 0 == GNET::mppc::compress2(com + 10, &com_len, (const unsigned char*)src, src_len) 
			&& com_len < (int)src_len ); 
	}

	GNET::Marshal::OctetsStream header;
	if (success) 
	{
		header << GNET::CompactUINT(src_len) << GNET::CompactUINT(com_len);
		int offset = 10 - header.size();
		memcpy((char*)com + offset, header.begin(), header.size());
		out.setV(com, header.size() + com_len, offset );
	}
	else 
	{
		header << GNET::CompactUINT(src_len) << GNET::CompactUINT(src_len);
		int offset = 10 - header.size();
		memcpy((char*)com + offset, header.begin(), header.size());
		memcpy((char*)com + 10, (const char*)src, src_len);
		out.setV(com, header.size() + src_len, offset );
	}
#ifdef __STATISTIC
	__total_uncom += src_len;
	__total_com += out.size() - header.size();
#endif
}


inline void uncompress(const void* com, size_t com_len, Value& out) 
{
	// com_len:      包含头信息的待解压数据长度
	// com_real_len: 真正的压缩长度
	// header_len:   头信息的长度 com_len == com_real_len + header_len
	// src_len:      真正的数据长度
	unsigned int com_real_len, src_len; 
	unsigned int header_len = 0;
	out.setV(NULL, 0, 0);

	{ // os仅仅用来获取header中的 com_real_len 和 src_len 值
		GNET::Marshal::OctetsStream os;
		os.push_byte((const char*)com, (com_len < 10 ? com_len : 10)); 
		os >> GNET::CompactUINT(src_len) >> GNET::CompactUINT(com_real_len);
		header_len = os.position();
		if (header_len + com_real_len != com_len) 
			return;
	}
	
	void* src = malloc(src_len);	
	if ( com_real_len < src_len )
	{
		bool success;
		if (src_len <= 8192)
		{
			success = (0 == GNET::mppc::uncompress((unsigned char*)src, (int*)&src_len, 
						(const unsigned char*)com + header_len, (int)com_real_len));
		}
		else
		{
			success = (0 == GNET::mppc::uncompress2((unsigned char*)src, (int*)&src_len, 
						(const unsigned char*)com + header_len, (int)com_real_len));
		}

		if (success)
		{
			out.setV(src, src_len, 0);			
		}
		else // 失败的话，把压缩的失败数据返回上层
		{
			fprintf(stdout, "unmarshal failed ! send raw data !\n");
			fflush(stdout);
			memcpy((char*)src, (const char*)com + header_len, com_real_len);	
			out.setV(src, com_real_len, 0);
		}	
	} 
	else if ( com_real_len == src_len )
	{
		memcpy((char*)src, (const char*)com + header_len, (int)src_len);	
		out.setV(src, src_len, 0);
	} // else --->  Error, current Out = (NULL, 0, 0) 
}

class Storage
{
	char* fullfilename;
	GNET::PageFile * page_file;
	GNET::PageCache * page_cache;

public:
	Storage(GNET::Logger * logger, const char * fullfilename, size_t cache_high, size_t cache_low) {
		this->fullfilename = new char[strlen(fullfilename) + 1];
		strcpy(this->fullfilename, fullfilename);
		page_file  = new GNET::PageFile(fullfilename);
		page_cache = new GNET::PageCache(page_file, cache_high, cache_low);
		logger->init(page_file, page_cache);
	}

	~Storage() {
		delete page_cache;
		delete page_file;
		delete []fullfilename;
	}

	// data access
	bool exist(const void* key, size_t keysize) {
		return page_cache->exist(key, keysize);
	}

	bool remove(const void* key, size_t keysize) {
		return page_cache->del(key, keysize);
	}

	void walk(GNET::IQueryData* data) {
		page_cache->walk(data);
	}

	void browse(GNET::IQueryData* data, size_t hashsize) {
		GNET::__db_helper::PageBrowser pb(fullfilename, hashsize);	
		// XXX pb.action will return corrupt numbers, not care ???
		pb.action(data);
	}

	void find(const void* key, size_t keysize, __xdb_helper::Value& val) {
		size_t data_len = 0;
		void * data = page_cache->find(key, keysize, data_len);
		if (NULL != data) {
			/*
			// lich begin
			GNET::Octets datacopy(data, data_len);
			GNET::Octets result;
			GNET::Uncompress(datacopy, result);
			void * valcopy = malloc(result.size());
			memcpy(valcopy, result.begin(), result.size());
			val.setV(valcopy, result.size(), 0);
			// lich end
			*/
			uncompress(data, data_len, val);
			free(data);
		} else {
			val.setV(NULL, 0, 0);
		}
	}

	bool insert(const void* key, size_t keysize, const void *data, size_t data_len) {
		/*
		// lich begin
		GNET::Octets datacopy(data, data_len);
		GNET::Octets result;
		GNET::Compress(datacopy, result);
		return page_cache->put(key, keysize, result.begin(), result.size(), false);
		// lich end
		*/
		Value val;
		compress(data, data_len, val);
		return page_cache->put(key, keysize, val.data(), val.size(), false);
	}
	
	bool replace(const void* key, size_t keysize, const void *data , size_t data_len) {
		/*
		// lich begin
		GNET::Octets datacopy(data, data_len);
		GNET::Octets result;
		GNET::Compress(datacopy, result);
		return page_cache->put(key, keysize, result.begin(), result.size(), true);
		// lich end
		*/
		Value val;
		compress(data, data_len, val);
		return page_cache->put(key, keysize, val.data(), val.size(), true);
	}

	void _find(const void* key, size_t keysize, __xdb_helper::Value& val) {
		size_t data_len = 0;
		void * data = page_cache->find(key, keysize, data_len);
		val.setV(data, data_len, 0);
	}

	bool _insert(const void* key, size_t keysize, const void *data, size_t data_len) {
		return page_cache->put(key, keysize, data, data_len, false);
	}
	
	bool _replace(const void* key, size_t keysize, const void *data , size_t data_len) {
		return page_cache->put(key, keysize, data, data_len, true);
	}

	// management
	void snapshot_create()  { page_cache->snapshot_create(); }
	void snapshot_release() { page_cache->snapshot_release(); }

	bool firstKey(__xdb_helper::Value& val) {
		size_t keysize = 0;
		void * first = page_cache->first_key( keysize );
		val.setV(first, keysize, 0);
		return first != NULL;
	}

	bool nextKey(const void * key, size_t keysize, __xdb_helper::Value& val) {
		void * next = page_cache->greater_key( key, keysize );
		val.setV(next, keysize, 0);
		return next != NULL;
	}
};

}

