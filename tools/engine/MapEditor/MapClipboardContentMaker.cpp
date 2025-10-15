#include "StdAfx.h"
#include "..\engine\common\xmarshal.h"
#include "MapClipboardContentMaker.h"

bool CMapClipboardContentMaker::GetDataFromMapClipboardContent(HANDLE hData)
{
	int i, count, size;
	int *pData = static_cast<int*>(::GlobalLock(hData));
	size = *pData;
	pData += 1;
	Nuclear::XBuffer buffer(pData, size);
	Nuclear::XIStream os(buffer);
	MapClipboardCont mcc;
	m_Container.clear();
	char tmp;
	os >> tmp;
	m_Type = static_cast<MapClipboardContType>(tmp);
	os >> count;
	for (i=0;i<count;i++)
	{
		os >> mcc.name;
		os >> tmp;
		mcc.type = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(tmp);
		os >> mcc.rect;
		os >> mcc.point;
		os >> mcc.attachColor;
		m_Container.push_back(mcc);
	}
	::GlobalUnlock(hData);
	::GlobalFree(hData);
	return true;
}

HANDLE CMapClipboardContentMaker::MakeMapClipboardContent()
{
	Nuclear::XOStream os;
	VectorMapCBContainer::iterator iter = m_Container.begin();
	os << static_cast<char>(m_Type);
	os << static_cast<int>(m_Container.size());
	for (;iter!=m_Container.end();iter++)
	{
		os << iter->name;
		os << static_cast<char>(iter->type);
		os << iter->rect;
		os << iter->point;
		os << iter->attachColor;
	}
	int size = static_cast<int>(os.size());
	HANDLE hData = ::GlobalAlloc(GMEM_MOVEABLE, size + sizeof(int));
	if (hData == NULL)
		return NULL;
	int *pData = static_cast<int*>(::GlobalLock(hData));
	*(pData) = size;
	pData += 1;
	memcpy_s(pData, os.size(), os.begin(), os.size());
	::GlobalUnlock(hData);
	return hData;
}

void CMapClipboardContentMaker::AddObject(const std::wstring &name, Nuclear::PMap::MAP_OBJ_TYPE type, const CRect &rect, const CPoint &point, unsigned int attachColor)
{
	MapClipboardCont mcc;
	mcc.name = name;
	mcc.type = type;
	mcc.rect = rect;
	mcc.attachColor = attachColor;
	mcc.point = point;
	m_Container.push_back(mcc);
}

CMapClipboardContentMaker::~CMapClipboardContentMaker(void)
{
}

CMapClipboardContentMaker::CMapClipboardContentMaker(MapClipboardContType type)
{
	m_Type = type;
}

UINT const CMapClipboardContentMaker::ms_ClipboardFormatID = ::RegisterClipboardFormat(_T("ObjClipboardFormat"));

