//////////////////////////////////////////////////////////////////////////
//
// ShowRenderer.cpp
//
// author : yanghaibo
// date :	2008-11-06
//
//////////////////////////////////////////////////////////////////////////

#include "StdAfx.h"
#include "ShowRenderer.h"
#include "SvgShowRenderer.h"


//
// summary:
//		根据type指定的类型创建ShowRenderer对象
//
// parameter:
//		ShowRenderer的类型
//
// returns:
//		ShowRenderer对象接口，返回NULL表示不支持该类型或者内存不足导致创建对象失败
//
IShowRenderer* CreateShowRenderer( int type/* = ST_DEFAULT*/ )
{
	IShowRenderer* pRenderer = NULL;
	switch( type )
	{
	case ST_SVG:
		pRenderer = new CSvgShowRenderer();
		break;
	default:
		break;
	}

	return pRenderer;
}
