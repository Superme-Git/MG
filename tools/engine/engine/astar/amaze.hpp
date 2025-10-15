#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <malloc.h>
#include "astar.hpp"

namespace astar
{

/*
简单迷宫编辑管理。目前仅用于测试。
*/

class AMaze
{
public:
	typedef unsigned char value_t; // 可达图格子数据类型。
	typedef unsigned int  len_t;   // 可达图长度类型。

	enum
	{
		eMaskAll = 0xffffffff,  // 检查所有障碍
		eMaskAny = 0,           // 忽略所有障碍
	};

	// 传递可达图参数给PathFinder
	astar::AMap amap(unsigned mask = eMaskAll) const { return astar::AMap(width(), height(), mask, data()); }

	// 简单编辑功能。
	len_t width() const          { return this->_width; }
	len_t height() const         { return this->_height; }
	const value_t * data() const { return this->_data; }
	len_t size() const           { return this->width() * this->height(); }
	len_t bytes() const          { return this->size() * sizeof(value_t); }

	bool set(int x, int y, value_t val)
	{
		if (x < 0 || (len_t)x >= this->width())  return false;
		if (y < 0 || (len_t)y >= this->height()) return false;

		this->_data[x + y * this->width()] = val;
		return true;
	}

	bool walkable(int x, int y, unsigned mask = eMaskAll) const
	{
		if (x < 0 || (len_t)x >= this->width())  return false;
		if (y < 0 || (len_t)y >= this->height()) return false;
		return 0 == (this->_data[x + y * this->width()] & mask);
	}

	AMaze(len_t width = 0, len_t height = 0)
	{
		this->_width  = width;
		this->_height = height;
		this->_data   = (value_t *)::calloc(size(), sizeof(value_t));
	}

	~AMaze()
	{
		::free(this->_data);
	}

	void swap(AMaze & maze)
	{
		std::swap(this->_width,  maze._width);
		std::swap(this->_height, maze._height);
		std::swap(this->_data,   maze._data);
	}

	AMaze(const AMaze & maze)
	{
		this->_width  = maze._width;
		this->_height = maze._height;
		this->_data   = (value_t *)::malloc(bytes());
		::memcpy(this->_data, maze._data, bytes());
	}

	AMaze & operator=(const AMaze & maze)
	{
		::free(this->_data);
		this->_width  = maze._width;
		this->_height = maze._height;
		this->_data   = (value_t *)::malloc(bytes());
		::memcpy(this->_data, maze._data, bytes());
		return *this;
	}

	void paste(len_t tx, len_t ty, const AMaze & src, len_t sx, len_t sy, len_t width, len_t height)
	{
		if (this == &src) return; // 不允许自己粘贴
		if (src._width  <= sx || src._height  <= sy) return;    // 源区域为空
		if (this->_width <= tx || this->_height <= ty) return;  // 目标区域为空

		using std::min; // 在windows下面有个min宏，这样写不会冲突。

		// 修正源区域
		width = min(width, src._width - sx);
		height = min(height, src._height - sy);

		value_t * dstData    = this->_data + ty * this->_width;
		value_t * dstDataEnd = this->_data + this->size();
		value_t * srcData    = src._data  + src._width * sy;
		value_t * srcDataEnd = src._data  + src._width * (sy + height);

		len_t copybytes = sizeof(value_t) * min(this->_width - tx, width);
		while (dstData < dstDataEnd && srcData < srcDataEnd)
		{
			::memcpy(dstData + tx, srcData + sx, copybytes);
			dstData += this->_width;
			srcData += src._width;
		}
	}

	AMaze copy(len_t x, len_t y, len_t width, len_t height) const
	{
		AMaze _copy(width, height);
		_copy.paste(0, 0, *this, x, y, width, height);
		return _copy;
	}

	void paste(len_t x, len_t y, const AMaze & src)
	{
		this->paste(x, y, src, 0, 0, src.width(), src.height());
	}

	#ifdef WIN32
	// TODO windows的文件读改成用pfs。
	class File
	{
		FILE * fp;
	public:
		File(const char * filename, const char * mode) { if (::fopen_s(&fp, filename, mode)) fp = NULL; }
		bool read(void * buffer, size_t size)          { if (fp) return size == ::fread(buffer, 1, size, fp);  return false; }
		bool write(const void * buffer, size_t size)   { if (fp) return size == ::fwrite(buffer, 1, size, fp); return false; }
		~File() { if (fp) ::fclose(fp); }
	};
	#else
	class File
	{
		FILE * fp;
	public:
		File(const char * filename, const char * mode) { fp = ::fopen(filename, mode); }
		bool read(void * buffer, size_t size)          { if (fp) return size == ::fread(buffer, 1, size, fp);  return false; }
		bool write(const void * buffer, size_t size)   { if (fp) return size == ::fwrite(buffer, 1, size, fp); return false; }
		~File() { if (fp) ::fclose(fp); }
	};
	#endif
	struct Header
	{
		enum { eMinSize = 12 };
		len_t hsize;
		len_t width;
		len_t height;
		Header() { hsize = eMinSize; }
	};

	bool load(const char * mazeFile)
	{
		File file(mazeFile, "rb");

		// read header
		Header header;
		if (!file.read(&header, sizeof(Header))) return false;
		if (header.hsize < Header::eMinSize)     return false;
		// skip unkown header
		for (len_t i = 0; i < header.hsize - Header::eMinSize; ++i)
		{
			char c;
			if (!file.read(&c, 1)) return false;
		}

		len_t size = header.width * header.height;
		value_t * data = (value_t *)::malloc(size);
		if (!file.read(data, size))
		{
			::free(data);
			return false;
		}

		::free(this->_data);
		this->_data   = data;
		this->_width  = header.width;
		this->_height = header.height;
		return true;
	}

	bool save(const char * mazeFile)
	{
		File file(mazeFile, "wb");

		Header header;
		header.width  = this->width();
		header.height = this->height();

		if (!file.write(&header, sizeof(Header)))     return false;
		if (!file.write(this->data(), this->bytes())) return false;

		return true;
	}

private:
	len_t     _width;
	len_t     _height;
	value_t * _data;
};

} // namespace astar

