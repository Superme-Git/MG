#ifndef MAP_CLIPBOARD_CONTAINER_MAKER_H
#define MAP_CLIPBOARD_CONTAINER_MAKER_H
#pragma once

#include <vector>
#include "..\engine\map\pmap.h"

enum MapClipboardContType
{
	MCCT_OBJ = 0,
};

typedef struct
{
	std::wstring name;
	Nuclear::PMap::MAP_OBJ_TYPE type;
	CRect rect;
	CPoint point;
	unsigned int attachColor;
} MapClipboardCont;

typedef std::vector<MapClipboardCont> VectorMapCBContainer;

class CMapClipboardContentMaker
{
	VectorMapCBContainer m_Container;
	MapClipboardContType m_Type;
	
public:
	bool GetDataFromMapClipboardContent(HANDLE hData);
	HANDLE MakeMapClipboardContent();
	void AddObject(const std::wstring &name, Nuclear::PMap::MAP_OBJ_TYPE type, const CRect &rect, const CPoint &point, unsigned int attachColor);
	const VectorMapCBContainer& GetContainer() const {return m_Container;}
	MapClipboardContType GetType() const {return m_Type;}
	~CMapClipboardContentMaker(void);
	CMapClipboardContentMaker() {}
	CMapClipboardContentMaker(MapClipboardContType type);
	static const UINT ms_ClipboardFormatID;

};

#endif 