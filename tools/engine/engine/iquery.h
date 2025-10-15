#ifndef __Nuclear_IQUERY_H
#define __Nuclear_IQUERY_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <string>
#include <vector>

namespace Nuclear
{

// �����������¶һЩ������ڲ���Ϣ

class IQuery
{
public:
	struct ActionInfo
	{
		int nTime;	 // ����������Ҫ��ʱ�䣬��λ����
		int nFrame;  // ֡��
		int nStride; // ��������λ����(�������ƶ���������������Ϊ0)
	};

	struct SpriteLayerInfo
	{
		std::wstring name; // ͬʱ��Ϊ directory name
		std::wstring des;  // ˫���ֵ�����
	};

	// ��������
public:
	IQuery() {}
	virtual ~IQuery() {  }

	// �ӿ�
public:
	// ���ض��������Ϣ
	virtual bool GetActionInfo(const std::wstring &modelname, const std::wstring &actname, IQuery::ActionInfo &info) const = 0;

	// ����vector��size����Ӧ�õĲ����� ��������������� res/role/layerdef.xml ��õ�
	// �������ڳ��������ڼ䲻��仯������ֻӦ����һ��
	virtual std::vector<SpriteLayerInfo> GetSpriteLayerInfo(const std::wstring &modelname) const = 0;

	// ռ��
private:
	IQuery(const IQuery&);
	IQuery& operator=(const IQuery&);

};


}


#endif