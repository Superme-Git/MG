#ifndef __Nuclear_IENTITATIVE_TITLE_MANAGER_H
#define __Nuclear_IENTITATIVE_TITLE_MANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "common\xptypes.h"


namespace Nuclear
{
	class IEntitativeTitleManager
	{

	public:
		IEntitativeTitleManager(void) {}
		virtual ~IEntitativeTitleManager(void) {}

		//�ڻ���ʵ�壨Sprite��Immovableobj����ʱ���������������
		//�߼��Ǳ߿����ڵ��������ʱ������ϻ��ƣ����ʱ������м�㣬
		//Ҳ����������������ռ���֮���ڻ��������棬����������UI֮ǰ����
		//posΪ��Ļ��������
		virtual void RenderTitle(EntitativeTitleHandle title, const CPOINT &pos) = 0;
		
		//���Location�������������꣬��ʰȡ����ʱ�򣬵����ּ���������Title�ķ�Χ�ڵ�ʱ��
		//�ͻ�������������һ����⡣��Ȼ�����Ǳ��뾫ȷ������
		virtual bool TestTitleAlpha(EntitativeTitleHandle title, const Location &testLoc) = 0;
	};
}

#endif