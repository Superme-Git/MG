#ifndef __Nuclear_amaze_H__
#define __Nuclear_amaze_H__

#include <stdlib.h>
#include <stdio.h>
#include <malloc.h>
#include "astar.hpp"

namespace astar
{

/*
简单迷宫编辑管理。
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
	len_t width() const          { return this->m_width; }
	len_t height() const         { return this->m_height; }
	const value_t * data() const { return this->m_data; }
	len_t size() const           { return this->width() * this->height(); }
	len_t bytes() const          { return this->size() * sizeof(value_t); }

	bool set(int x, int y, value_t val)
	{
		if (x < 0 || (len_t)x >= this->width())  return false;
		if (y < 0 || (len_t)y >= this->height()) return false;

		this->m_data[x + y * this->width()] = val;
		return true;
	}

	bool walkable(int x, int y, unsigned mask = eMaskAll) const
	{
		if (x < 0 || (len_t)x >= this->width())  return false;
		if (y < 0 || (len_t)y >= this->height()) return false;
		return 0 == (this->m_data[x + y * this->width()] & mask);
	}

	AMaze(len_t width = 0, len_t height = 0)
	{
		this->m_width = width;
		this->m_height = height;
		this->m_data = (value_t *)::calloc(size(), sizeof(value_t));
	}

	~AMaze()
	{
		::free(this->m_data);
	}

	void swap(AMaze & maze)
	{
		std::swap(this->m_width, maze.m_width);
		std::swap(this->m_height, maze.m_height);
		std::swap(this->m_data, maze.m_data);
	}

	AMaze(const AMaze & maze)
	{
		this->m_width = maze.m_width;
		this->m_height = maze.m_height;
		this->m_data = (value_t *)::malloc(bytes());
		::memcpy(this->m_data, maze.m_data, bytes());
	}

	AMaze & operator=(const AMaze & maze)
	{
		::free(this->m_data);
		this->m_width = maze.m_width;
		this->m_height = maze.m_height;
		this->m_data = (value_t *)::malloc(bytes());
		::memcpy(this->m_data, maze.m_data, bytes());
		return *this;
	}

	void paste(len_t tx, len_t ty, const AMaze & src, len_t sx, len_t sy, len_t width, len_t height)
	{
		if (this == &src) return; // 不允许自己粘贴
		if (src.m_width  <= sx || src.m_height  <= sy) return;    // 源区域为空
		if (this->m_width <= tx || this->m_height <= ty) return;  // 目标区域为空

		using std::min; // 在windows下面有个min宏，这样写不会冲突。

		// 修正源区域
		width = min(width, src.m_width - sx);
		height = min(height, src.m_height - sy);

		value_t * dstData    = this->m_data + ty * this->m_width;
		value_t * dstDataEnd = this->m_data + this->size();
		value_t * srcData    = src.m_data  + src.m_width * sy;
		value_t * srcDataEnd = src.m_data  + src.m_width * (sy + height);

		len_t copybytes = sizeof(value_t) * min(this->m_width - tx, width);
		while (dstData < dstDataEnd && srcData < srcDataEnd)
		{
			::memcpy(dstData + tx, srcData + sx, copybytes);
			dstData += this->m_width;
			srcData += src.m_width;
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

		::free(this->m_data);
		this->m_data   = data;
		this->m_width  = header.width;
		this->m_height = header.height;
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
	len_t     m_width;
	len_t     m_height;
	value_t * m_data;
};

} // namespace astar

#endif