#ifndef __Nuclear_QUERY_H
#define __Nuclear_QUERY_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\iquery.h"

namespace Nuclear
{

class Engine;
class Query : public IQuery
{
public:
	Query(Engine *pEngine) : m_pEngine(pEngine) { }
	virtual ~Query() { }
	virtual bool GetActionInfo(const std::wstring &modelname, const std::wstring &actname, IQuery::ActionInfo &info) const;

	// 返回vector的size就是应用的层数， 此数据是引擎根据 res/role/layerdef.xml 获得的
	virtual std::vector<SpriteLayerInfo> GetSpriteLayerInfo(const std::wstring &modelname) const;

private:
	Engine* m_pEngine;
};

}

#endif