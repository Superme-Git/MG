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
//		����typeָ�������ʹ���ShowRenderer����
//
// parameter:
//		ShowRenderer������
//
// returns:
//		ShowRenderer����ӿڣ�����NULL��ʾ��֧�ָ����ͻ����ڴ治�㵼�´�������ʧ��
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
