#ifndef __Nuclear_GUOBJ_H__
#define __Nuclear_GUOBJ_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	/*
	1.����Ķ���Ӧ����һ�������ڼ�Ψһ��ID���ڸ����෶Χ�ڣ�
	2.�����ʹ��GenGUID�����Լ���ΨһID�Ļ��ɱ�֤�������������ΨһID
	todo û�����ôǿ��ʵ�ֻ��߸ò���ǿ��ʵ��2
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