#include "stdafx.h"
#include "..\sprite\spritemanager.h"
#include "engine.h"
#include "query.h"
namespace Nuclear
{

	bool Query::GetActionInfo(const std::wstring &modelname, const std::wstring &actname, IQuery::ActionInfo &info) const
	{
		const SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		const Action *pAct = pSprMan->GetAction(modelname, actname);
		if( pAct == NULL ) return false;
		info.nFrame = pAct->GetFrameCount();
		info.nTime = pAct->GetTime();
		info.nStride = pAct->GetStride();
		if( info.nStride < 1 ) info.nStride = 80; //todo ���ڻ�û�༭�������ٵ���
		return true;
	}

	// ����vector��size����Ӧ�õĲ����� ��������������� res/role/layerdef.xml ��õ�
	std::vector<IQuery::SpriteLayerInfo> Query::GetSpriteLayerInfo(const std::wstring &modelname) const
	{
		const SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		int n = pSprMan->GetLayerCount(modelname);
		std::vector<IQuery::SpriteLayerInfo> v(n);
		for(int i=0; i<n; ++i)
		{
			v[i].name = pSprMan->GetLayerName(modelname, i);
			v[i].des = pSprMan->GetLayerDes(modelname, i);
		}
		return v;
	}

}