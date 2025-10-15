#ifndef __Nuclear_GUOBJ_H__
#define __Nuclear_GUOBJ_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	/*
	1.子类的对象都应该有一个运行期间唯一的ID（在该子类范围内）
	2.如果都使用GenGUID生成自己的唯一ID的话可保证所有子类对象都有唯一ID
	todo 没想好怎么强制实现或者该不该强制实现2
	*/
	class GUObject
	{
		static unsigned int s_guidseed;
		unsigned int m_nGuid;
	public:
		GUObject() {m_nGuid = s_guidseed++;}
		unsigned int GetGUID() const { return m_nGuid; }
		virtual ~GUObject() { }
	private:
		GUObject(const GUObject&);
		GUObject& operator= (const GUObject&);
	};

}

#endif