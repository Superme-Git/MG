

#include "stdafx.h"
#include "MapEditor.h"

#include "MapEditorDoc.h"
#include "MapEditorView.h"

#include "RegionMapInfoDlg.h"
#include "..\engine\common\util.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

using Nuclear::SubMap::PtrTileTypeList;
using Nuclear::SubMap::PtrTileType;
using Nuclear::SubMap::PtrBiggerTileType;



IMPLEMENT_DYNCREATE(CMapEditorDoc, CDocument)

void CMapEditorDoc::ResetAllObjectRects()
{

	Nuclear::CFileIOManager *pIOMan = theApp.GetFileIOManager();
	if (!pIOMan)
		return;
	{
		const Nuclear::SubMap::ObjNameMap& onm = m_ToolsMap.m_objs[PMap::MAP_OBJ_ELEMENT].GetConstObjFileNameMap();
		std::map<Nuclear::SubMap::ObjMapKey, POINT> picSizeMap;
		Nuclear::SubMap::ObjNameMap::const_iterator onmIter = onm.begin();
		Nuclear::SubMap::ObjNameMap::const_iterator onmEnd = onm.end();
		POINT pt;
		for (;onmIter!=onmEnd;++onmIter)
		{
			CString fileName = onmIter->second.c_str();
			fileName = fileName + ImageSet_ExtName;
			Nuclear::XBuffer buffer;
			pIOMan->GetFileImage(fileName.GetBuffer(), buffer);
			fileName.ReleaseBuffer();
			if (buffer.size() > 0)
			{
				Nuclear::PImg tmpimg;
				tmpimg.LoadFromMem(buffer.constbegin(), (int)buffer.size());
				pt.x = tmpimg.m_nFileWidth;
				pt.y = tmpimg.m_nFileHeight;
				picSizeMap[onmIter->first] = pt;
			}
		}
		Nuclear::SubMap::ObjInfoVector::iterator picInfoIter;
		std::map<Nuclear::SubMap::ObjMapKey, POINT>::iterator picSizeIter;
		std::map<Nuclear::SubMap::ObjMapKey, POINT>::iterator picSizeEnd = picSizeMap.end();
		for (int i=0;i<Nuclear::MAP_LAYER_COUNT - 1;++i)
		{
			Nuclear::SubMap::ObjInfoVector& picInfos = m_ToolsMap.m_objs[PMap::MAP_OBJ_ELEMENT].GetObjInfos(i);
			picInfoIter = picInfos.begin();
			for(;picInfoIter!=picInfos.end();++picInfoIter)
			{
				picSizeIter = picSizeMap.find(picInfoIter->m_MapKey);
				if (picSizeIter!=picSizeEnd)
				{
					picInfoIter->m_Rect.right = picInfoIter->m_Rect.left + picSizeIter->second.x;
					picInfoIter->m_Rect.bottom = picInfoIter->m_Rect.top + picSizeIter->second.y;
				}
			}
		}

	}


	{
		const Nuclear::SubMap::ObjNameMap& onm = m_ToolsMap.m_objs[PMap::MAP_OBJ_LINKEDOBJ].GetConstObjFileNameMap();
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT> dobjSizeMap;
		Nuclear::SubMap::ObjNameMap::const_iterator onmIter = onm.begin();
		Nuclear::SubMap::ObjNameMap::const_iterator onmEnd = onm.end();
		for (;onmIter!=onmEnd;++onmIter)
		{
			Nuclear::XBuffer buffer;
			pIOMan->GetFileImage(onmIter->second, buffer);
			if (buffer.size() > 0)
			{
				Nuclear::PLinkedObject tmplko;
				tmplko.LoadFromMem(buffer.constbegin(), (int)buffer.size());
				dobjSizeMap[onmIter->first] = tmplko.m_rctMaxBox;
			}
		}
		Nuclear::SubMap::ObjInfoVector::iterator lkoInfoIter;
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT>::iterator dobjSizeIter;
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT>::iterator dobjSizeEnd = dobjSizeMap.end();
		for (int i=0;i<Nuclear::MAP_LAYER_COUNT - 1;++i)
		{
			Nuclear::SubMap::ObjInfoVector& lkoInfos = m_ToolsMap.m_objs[PMap::MAP_OBJ_LINKEDOBJ].GetObjInfos(i);
			lkoInfoIter = lkoInfos.begin();
			for(;lkoInfoIter!=lkoInfos.end();++lkoInfoIter)
			{
				dobjSizeIter = dobjSizeMap.find(lkoInfoIter->m_MapKey);
				if (dobjSizeIter!=dobjSizeEnd)
				{
					lkoInfoIter->m_Rect = dobjSizeIter->second;
					lkoInfoIter->m_Rect += lkoInfoIter->m_Position;
				}
			}
		}

	}


	{
		const Nuclear::SubMap::ObjNameMap& efnm = m_ToolsMap.m_objs[PMap::MAP_OBJ_EFFECT].GetConstObjFileNameMap();
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT> effectRectMap;
		Nuclear::SubMap::ObjNameMap::const_iterator efnmIter = efnm.begin();
		Nuclear::SubMap::ObjNameMap::const_iterator efnmEnd = efnm.end();
		POSITION pos = GetFirstViewPosition();
		CView *pView = NULL;
		CMapEditorView *pMapView = NULL;
		while (pView = GetNextView(pos))
		{
			pMapView = dynamic_cast<CMapEditorView*>(pView);
			if (pMapView)
				break;
		}
		if (!pMapView)
			return;
		Nuclear::EffectManager* pEMGR = pMapView->GetEffectManager();
		for (;efnmIter!=efnmEnd;++efnmIter)
		{
			Nuclear::Effect* pEffect = pEMGR->CreateEffect(efnmIter->second, false);
			if (pEffect)
			{
				pEffect->AssureResource(false);
				effectRectMap[efnmIter->first] = pEffect->GetRelBouningBox();
				delete pEffect;
			}
		}

		Nuclear::SubMap::ObjInfoVector::iterator effectInfoIter;
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT>::iterator erIter;
		std::map<Nuclear::SubMap::ObjMapKey, Nuclear::CRECT>::iterator erEnd = effectRectMap.end();
		for (int i=0;i<Nuclear::MAP_LAYER_COUNT - 1;++i)
		{
			Nuclear::SubMap::ObjInfoVector& efctInfos = m_ToolsMap.m_objs[PMap::MAP_OBJ_EFFECT].GetObjInfos(i);
			effectInfoIter = efctInfos.begin();
			for(;effectInfoIter!=efctInfos.end();++effectInfoIter)
			{
				erIter = effectRectMap.find(effectInfoIter->m_MapKey);
				if (erIter!=erEnd)
				{
					effectInfoIter->m_Rect = erIter->second + effectInfoIter->m_Position;
				}
			}
		}
	}

}

void CMapEditorDoc::ExportToFile(const CString& fileName)
{
	CToolsMap::MapFileState res;
	if (m_ToolsMap.ExportToFile(fileName, res))
	{
		CString logfn = fileName.Left(fileName.GetLength() - 7) + L"experr.log";
		if (CToolsMap::ExpRes(logfn, res))
		{
			CString errStr;
			errStr.Format(L"导出成功，但导出过程中找不到一些资源，\n这些资源已经从已导出的文件中去掉，具体请查看日志：\n%s", logfn.GetBuffer());
			AfxMessageBox(errStr);
		}
	} else {
		AfxMessageBox(L"导出失败！");
	}
}

bool CMapEditorDoc::ModifyCanvasForObjects(Nuclear::CRECT oldMap, Nuclear::CRECT newMap)
{
	unsigned int i, j;
	unsigned int layerCount = Nuclear::MAP_LAYER_COUNT - 1;
	int xAdj = newMap.left - oldMap.left;
	int yAdj = newMap.top - oldMap.top;
	Nuclear::SubMap::ObjInfoVector::iterator iter, iterEnd;
	PMap::MAP_OBJ_TYPE objType;
	for (j=0;j<PMap::MAP_OBJ_COUNT;++j)
	{
		objType = static_cast<PMap::MAP_OBJ_TYPE>(j);
		for (i=0;i<layerCount;i++)
		{
			Nuclear::SubMap::ObjInfoVector& objs = m_ToolsMap.m_objs[j].GetObjInfos(i);
			iter=objs.begin();
			iterEnd = objs.end();
			for (;iter!=iterEnd;iter++)
			{
				if (Nuclear::IsRectCross(oldMap, iter->m_Rect))
				{
					iter->m_Rect.left += xAdj;
					iter->m_Rect.top += yAdj;
					iter->m_Rect.right += xAdj;
					iter->m_Rect.bottom += yAdj;
					iter->m_Position.x += xAdj;
					iter->m_Position.y += yAdj;
				} else {
					m_ToolsMap.DeleteObj(objType, i, iter->m_ObjectId);
				}
			}

		}
	}

	return true;
}

bool ModifyCanvasForTriggers(Nuclear::CRECT oldMap, Nuclear::CRECT newMap);

bool CMapEditorDoc::ModifyCanvasForBackground(Nuclear::CRECT oldMap, Nuclear::CRECT newMap)
{
	int xAdj = newMap.left - oldMap.left;
	int yAdj = newMap.top - oldMap.top;
	Nuclear::SubMap::PBackGoundInfoVector::iterator it = m_ToolsMap.m_backGround.m_backGroundInfos.begin(), 
		ie = m_ToolsMap.m_backGround.m_backGroundInfos.end();
	for (;it!=ie;++it)
	{
		if (Nuclear::IsRectCross(oldMap, it->activeRect))
		{
			it->pos.x += xAdj;
			it->pos.y += yAdj;
			it->activeRect.left += xAdj;
			it->activeRect.right += xAdj;
			it->activeRect.top += yAdj;
			it->activeRect.bottom += yAdj;
		} else {
			it->m_bDeleted = true;
			--m_ToolsMap.m_backGround.m_backGroundInfoCount;
		}
	}
	return true;
}

Nuclear::SubMap::ObjectId CMapEditorDoc::AddObject(PMap::MAP_OBJ_TYPE type, int layerID, Nuclear::SubMap::ObjMapKey mapKey, const RECT& rect, const POINT& point, unsigned int attachColor)
{
	if (layerID >= m_ToolsMap.GetObjLayerCount(type))
		return false;	
	return m_ToolsMap.AddObj(type, layerID, mapKey, rect, point, attachColor);
}

bool CMapEditorDoc::GetAvailableRectInMaps(int newWidth, int newHeight, int modifyMode, Nuclear::CRECT& inOldMap, Nuclear::CRECT& inNewMap)
{
	newWidth /= PMap::TILE_WIDTH;
	newHeight /= PMap::TILE_HEIGHT;

	int oldWidth = m_ToolsMap.GetWidthForSmlTiles();
	int oldHeight = m_ToolsMap.GetHeightForSmlTiles();
	switch (modifyMode)
	{
	case 0:
	case 3:
	case 6:

		inNewMap.left = 0;
		inOldMap.left = 0;
		inNewMap.right = (newWidth<oldWidth?newWidth:oldWidth);
		inOldMap.right = inNewMap.right;
		break;
	case 1:
	case 4:
	case 7:
		if (oldWidth > newWidth)
		{

			inNewMap.left = 0;
			inOldMap.left = (oldWidth - newWidth) / 2;
			inNewMap.right = newWidth;
			inOldMap.right = inOldMap.left + newWidth;
		} else {

			inNewMap.left = (newWidth - oldWidth) / 2;
			inOldMap.left = 0;
			inNewMap.right = inNewMap.left + oldWidth;
			inOldMap.right = oldWidth;
		}
		break;
	case 2:
	case 5:
	case 8:

		if (oldWidth > newWidth)
		{
			inNewMap.left = 0;
			inOldMap.left = oldWidth - newWidth;
		} else {
			inNewMap.left = newWidth - oldWidth;
			inOldMap.left = 0;
		}
		inNewMap.right = newWidth;
		inOldMap.right = oldWidth;
		break;
	}
	switch (modifyMode)
	{
	case 0:
	case 1:
	case 2:

		inNewMap.top = 0;
		inOldMap.top = 0;
		inNewMap.bottom = (newHeight<oldHeight?newHeight:oldHeight);
		inOldMap.bottom = inNewMap.bottom;
		break;
	case 3:
	case 4:
	case 5:
		if (oldHeight > newHeight)
		{

			inNewMap.top = 0;
			inOldMap.top = (oldHeight - newHeight) / 2;
			inNewMap.bottom = newHeight;
			inOldMap.bottom = inOldMap.top + newHeight;
		} else {

			inNewMap.top = (newHeight - oldHeight) / 2;
			inOldMap.top = 0;
			inNewMap.bottom = inNewMap.top + oldHeight;
			inOldMap.bottom = oldHeight;
		}
		break;
	case 6:
	case 7:
	case 8:

		if (oldHeight > newHeight)
		{
			inNewMap.top = 0;
			inOldMap.top = oldHeight - newHeight;
		} else {
			inNewMap.top = newHeight - oldHeight;
			inOldMap.top = 0;
		}
		inNewMap.bottom = newHeight;
		inOldMap.bottom = oldHeight;
		break;
	}
	return true;
}

bool CMapEditorDoc::ModifyCanvasForTile(int newWidth, int newHeight, int modifyMode, Nuclear::CRECT oldMap, Nuclear::CRECT newMap)
{

	int oldWidth = m_ToolsMap.GetWidthForSmlTiles();
	int oldHeight = m_ToolsMap.GetHeightForSmlTiles();
	int oldColorWidth = m_ToolsMap.GetWidthForColor();
	int oldWaterWidth = m_ToolsMap.m_Ground.GetWidthForWater();
	int oldMidTileWidth = m_ToolsMap.GetWidthForMidTiles();
	int oldBigTileWidth = m_ToolsMap.GetWidthForBigTiles();

	int i, j, y;
	unsigned int tile, tileCount;
	PtrTileTypeList::iterator iter = m_ToolsMap.m_GroundLayersArray.begin();

	TileType *oldPtrTiles, *newPtrTiles, *oldPtrTilesTemp, *newPtrTilesTemp;

	m_ToolsMap.m_nWidth = newWidth;
	m_ToolsMap.m_nHeight = newHeight;



	int oldWaterCount;
	Nuclear::SubMap::WaterTileType *pWaterData, *pOldTmpWaterData, *pNewTmpWaterData;
	oldWaterCount = m_ToolsMap.m_Ground.GetWaterCount();
	pWaterData = new Nuclear::SubMap::WaterTileType[oldWaterCount];
	memcpy(pWaterData, m_ToolsMap.GetWaterData(), oldWaterCount * sizeof(unsigned char));

	int oldMidTileCount;
	PtrBiggerTileType pMidTileData[Nuclear::TILES_MID_LAYER_COUNT];
	PtrBiggerTileType pOldTmpMidTileData[Nuclear::TILES_MID_LAYER_COUNT];
	PtrBiggerTileType pNewTmpMidTileData[Nuclear::TILES_MID_LAYER_COUNT];
	oldMidTileCount = m_ToolsMap.GetMidTileCount();
	for (i=0;i<Nuclear::TILES_MID_LAYER_COUNT;++i)
	{
		pMidTileData[i] = new BiggerTileType[oldMidTileCount];
		memcpy(pMidTileData[i], m_ToolsMap.GetMidTileData(i), oldMidTileCount * sizeof(BiggerTileType));
	}

	int oldBigTileCount;
	PtrBiggerTileType pBigTileData, pOldTmpBigTileData, pNewTmpBigTileData;
	oldBigTileCount = m_ToolsMap.GetBigTileCount();
	pBigTileData = new BiggerTileType[oldBigTileCount];
	memcpy(pBigTileData, m_ToolsMap.GetBigTileData(), oldBigTileCount * sizeof(BiggerTileType));


	int oldColorSize = m_ToolsMap.GetHeightForColor() * m_ToolsMap.GetWidthForColor() * sizeof(unsigned int);
	unsigned int *pColorData = (unsigned int *)Nuclear::XAlloc(oldColorSize);
	memcpy(pColorData, m_ToolsMap.GetColorData(), oldColorSize);

	if (!m_ToolsMap.m_Ground.Init(newWidth, newHeight))
		return false;


	{
		unsigned int *pOldColorData, *pNewTmpColorData;
		int newColorWidth = m_ToolsMap.m_Ground.GetWidthForColor();
		int newMapTop = newMap.top;
		int newMapBottom = newMap.bottom;
		int oldMapTop = oldMap.top;
		for (j=newMapTop,y=oldMapTop;j<=newMapBottom;++j,++y)
		{
			pOldColorData = pColorData + y * oldColorWidth + oldMap.left;
			pNewTmpColorData = m_ToolsMap.GetColorData() + j * newColorWidth + newMap.left;
			for (i=newMap.left;i<=newMap.right;++i)
			{
				*(pNewTmpColorData++) = *(pOldColorData++);
			}
		}
		Nuclear::XFree(pColorData);
	}


	{
		int newWaterWidth = m_ToolsMap.m_Ground.GetWidthForWater();
		int newMapTop = newMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::WATER_HEIGHT;
		int newMapBottom = newMap.bottom * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::WATER_HEIGHT;
		int oldMapTop = oldMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::WATER_HEIGHT;
		int newMapLeft = newMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::WATER_WIDTH;
		int oldMapLeft = oldMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::WATER_WIDTH;
		int newMapRight = newMap.right * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::WATER_WIDTH;
		for (j=newMapTop,y=oldMapTop;j<newMapBottom;j++,y++)
		{
			pOldTmpWaterData = pWaterData + y * oldWaterWidth + oldMapLeft;
			pNewTmpWaterData = m_ToolsMap.GetWaterData() + j * newWaterWidth + newMapLeft;
			for (i=newMapLeft;i<newMapRight;i++)
			{
				*(pNewTmpWaterData++) = *(pOldTmpWaterData++);
			}
		}
		delete [] pWaterData;
	}

	{
		int newMidTileWidth = m_ToolsMap.GetWidthForMidTiles();
		int newMapTop = newMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		int newMapBottom = newMap.bottom * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		int oldMapTop = oldMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		int newMapLeft = newMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_MID_WIDTH;
		int oldMapLeft = oldMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_MID_WIDTH;
		int newMapRight = newMap.right * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_MID_WIDTH;
		for (j=newMapTop,y=oldMapTop;j<newMapBottom;j++,y++)
		{
			pOldTmpMidTileData[0] = pMidTileData[0] + y * oldMidTileWidth + oldMapLeft;
			pNewTmpMidTileData[0] = m_ToolsMap.GetMidTileData(0) + j * newMidTileWidth + newMapLeft;
			pOldTmpMidTileData[1] = pMidTileData[1] + y * oldMidTileWidth + oldMapLeft;
			pNewTmpMidTileData[1] = m_ToolsMap.GetMidTileData(1) + j * newMidTileWidth + newMapLeft;
			for (i=newMapLeft;i<newMapRight;i++)
			{
				*(pNewTmpMidTileData[0]++) = *(pOldTmpMidTileData[0]++);
				*(pNewTmpMidTileData[1]++) = *(pOldTmpMidTileData[1]++);
			}
		}
		delete [] pMidTileData[0];
		delete [] pMidTileData[1];
	}

	{
		int newBigTileWidth = m_ToolsMap.GetWidthForBigTiles();
		int newMapTop = newMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		int newMapBottom = newMap.bottom * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		int oldMapTop = oldMap.top * Nuclear::PMap::TILE_HEIGHT / Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		int newMapLeft = newMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
		int oldMapLeft = oldMap.left * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
		int newMapRight = newMap.right * Nuclear::PMap::TILE_WIDTH / Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
		for (j=newMapTop,y=oldMapTop;j<newMapBottom;j++,y++)
		{
			pOldTmpBigTileData = pBigTileData + y * oldBigTileWidth + oldMapLeft;
			pNewTmpBigTileData = m_ToolsMap.GetBigTileData() + j * newBigTileWidth + newMapLeft;
			for (i=newMapLeft;i<newMapRight;i++)
			{
				*(pNewTmpBigTileData++) = *(pOldTmpBigTileData++);
			}
		}
		delete [] pBigTileData;
	}

	newWidth /= PMap::TILE_WIDTH;
	newHeight /= PMap::TILE_HEIGHT;

	tileCount = m_ToolsMap.GetSmlTileCount();
	for(;iter!=m_ToolsMap.m_GroundLayersArray.end();iter++)
	{
		oldPtrTiles = *iter;
		newPtrTiles = new TileType[tileCount];
		newPtrTilesTemp = newPtrTiles;
		for (tile=0;tile<tileCount;++tile)
		{
			*(newPtrTilesTemp++) = Nuclear::SubMap::PGround::TILE_NULL;
		}
		for (j=newMap.top,y=oldMap.top;j<newMap.bottom;j++,y++)
		{
			oldPtrTilesTemp = oldPtrTiles + y * oldWidth + oldMap.left;
			newPtrTilesTemp = newPtrTiles + j * newWidth + newMap.left;
			for (i=newMap.left;i<newMap.right;i++)
			{
				*(newPtrTilesTemp++) = *(oldPtrTilesTemp++);
			}
		}
		delete [] oldPtrTiles;
		*iter = newPtrTiles;
	}
	return true;
}

int CMapEditorDoc::CalculateTileNum(unsigned char type, unsigned char transType)
{
	CString fileName;
	CFileFind myFinder;
	int count = 0;
	fileName.Format(theApp.GetResPath() + _T(".\\map\\tiles\\smalltile\\%d-*"), type);
	if (myFinder.FindFile(LPCTSTR(fileName))) {
		myFinder.FindNextFile();

		if (myFinder.IsDirectory()) 
		{
			fileName.Format(myFinder.GetFilePath() + _T("\\%d-*") + TILE_TYPE, transType);
			count = CalculateFileCountFromPath(fileName);
		}
	}
	myFinder.Close();
	return count;
}

int CMapEditorDoc::CalculateBiggerTileNum(Nuclear::TILE_SIZE_TYPE tileSize, unsigned char type)
{
	CString fileName;
	CFileFind myFinder;
	int count = 0;
	switch (tileSize)
	{
	case Nuclear::TST_BIG:
		fileName.Format(theApp.GetResPath() + _T(".\\map\\tiles\\bigtile\\%d-*"), type);
		break;
	case Nuclear::TST_MIDDLE:
		fileName.Format(theApp.GetResPath() + _T(".\\map\\tiles\\midtile\\%d-*"), type);
		break;
	case Nuclear::TST_SMALL:
		return -1;
		break;
	}
	if (myFinder.FindFile(LPCTSTR(fileName))) {
		myFinder.FindNextFile();
		if (myFinder.IsDirectory()) 
		{
			fileName = myFinder.GetFilePath() + L"\\*" + TILE_TYPE;
			BOOL working;
			CFileFind finder;
			working = finder.FindFile(LPCTSTR(fileName));
			while (working)
			{  
				working = finder.FindNextFile();
				if (finder.IsDots())
					continue;
				if (!finder.IsDirectory() && !(finder.IsHidden()) && _wtoi(finder.GetFileTitle().GetString()) >= 15) 
				{
					count++;
				}
			}
			finder.Close();
		}
	}
	myFinder.Close();
	return count;
}

int CMapEditorDoc::CalculateFileCountFromPath(CString fileName)
{
	BOOL working;
	CFileFind myFinder;
	int count = 0;
	working = myFinder.FindFile(LPCTSTR(fileName));
	while (working)
	{  
		working = myFinder.FindNextFile();
		if (myFinder.IsDots())
			continue;
		if (!myFinder.IsDirectory() && !(myFinder.IsHidden())) 
		{
			count++;
		}
	}
	myFinder.Close();
	return count;
}

bool CMapEditorDoc::FillRect(int layer, Nuclear::CRECT rect, unsigned char type)
{
	int i,j,count;
	TileType *ptrTiles;
	TileType typeMsk = Nuclear::SubMap::PGround::TILE_NULL;
	int width = m_ToolsMap.GetWidthForSmlTiles();
	count = CalculateTileNum(type);
	typeMsk = Nuclear::SubMap::PGround::SetGroundType(typeMsk, type);
	typeMsk = Nuclear::SubMap::PGround::SetTileType(typeMsk, Nuclear::SubMap::PGround::TILE_SOLID);
	for (j = rect.top;j<rect.bottom;j++)
	{
		ptrTiles = m_ToolsMap.m_GroundLayersArray[layer] + j * width + rect.left;
		for (i = rect.left;i<rect.right;i++)
		{
			*(ptrTiles++) = Nuclear::SubMap::PGround::SetTileNumber(typeMsk, (rand() % count));
		}
	}
	return true;
}

bool CMapEditorDoc::ModifyCanvas(const sModifyCanvasParam& param)
{

	if (param.m_NewWidth == 0 || param.m_NewHeight == 0)
		return false;
	if (param.m_NewWidth % PMap::TILE_WIDTH != 0)
		return false;
	if (param.m_NewHeight % PMap::TILE_HEIGHT != 0)
		return false;


	if (param.m_ModifyMode < 0 || param.m_ModifyMode > 8)
		return false;
	int oldWidth = m_ToolsMap.m_nWidth;
	Nuclear::CRECT oldMap, newMap;
	if (!GetAvailableRectInMaps(param.m_NewWidth, param.m_NewHeight, param.m_ModifyMode, oldMap, newMap))
		return false;
	if (!ModifyCanvasForTile(param.m_NewWidth, param.m_NewHeight, param.m_ModifyMode, oldMap, newMap))
		return false;
	oldMap.top *= PMap::TILE_HEIGHT;
	oldMap.bottom *= PMap::TILE_HEIGHT;
	oldMap.left *= PMap::TILE_WIDTH;
	oldMap.right *= PMap::TILE_WIDTH;
	newMap.top *= PMap::TILE_HEIGHT;
	newMap.bottom *= PMap::TILE_HEIGHT;
	newMap.left *= PMap::TILE_WIDTH;
	newMap.right *= PMap::TILE_WIDTH;
	if (!ModifyCanvasForObjects(oldMap, newMap))
		return false;

	ModifyCanvasForBackground(oldMap, newMap);
	m_ToolsMap.ModifyTriggerCanvas(oldMap, newMap);
	m_ToolsMap.ModifyWaterAreaCanvas(oldMap, newMap);
	int todo;

	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		m_ToolsMap.SetWaterName(i, param.m_EffectName[i]);
	}

	m_DocSize.cx = m_ToolsMap.m_nWidth;
	m_DocSize.cy = m_ToolsMap.m_nHeight;
	m_bInit = true;
	return true;
}

void CMapEditorDoc::SetAutoSave()
{
	int time = GetPrivateProfileInt(L"SAVE", L"AutoSave", 0, theApp.GetResPath() + _T("\\MapEditorCfg.ini"));
	if (time > 0)
	{
		POSITION pos = GetFirstViewPosition();
		CView* pView = GetNextView(pos);
		pView->SetTimer(1001, time*60*1000, NULL);
	}
}

void CMapEditorDoc::AutoSave()
{
	CString fileName = m_strPathName;
	if (fileName != L"")
	{
		setlocale(LC_CTYPE, "chinese_china");
		time_t now = time(NULL);
		struct tm t;
		localtime_s(&t, &now);
		CString timeStr;
		timeStr.Format(L" - %4d%02d%02d%02d%02d%02d", t.tm_year+1900, t.tm_mon+1, t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec);
		fileName.Insert(fileName.GetLength()-5, timeStr);
		BOOL oldModify = IsModified();
		OnSaveDocument(fileName);
		SetModifiedFlag(oldModify);
	}
}

BOOL CMapEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	bool ret = m_ToolsMap.SaveToNativePath(lpszPathName)	;
	if (!ret)
	{
		AfxMessageBox(L"保存文件失败了！！请停止编辑！");
	}

	POSITION pos = GetFirstViewPosition();
	CView *pView;
	CMapEditorView* pMapEditorView;
	CString className = _T("CMapEditorView");
	int i;
	LPImgInfo pImgInfo;
	Nuclear::PLinkedObject *pLinkObj;
	CString imgName;
	while (pView = GetNextView(pos))
	{
		if (className == pView->GetRuntimeClass()->m_lpszClassName)
		{
			pMapEditorView = reinterpret_cast<CMapEditorView*>(pView);

			{
				std::map<Nuclear::SubMap::ObjMapKey, int>::iterator iter = pMapEditorView->m_ChangedImages.begin();
				for (;iter!=pMapEditorView->m_ChangedImages.end();iter++)
				{
					if (iter->second > 0)
					{
						Nuclear::PImg imgFile;
						const std::wstring& picName = m_ToolsMap.GetObjFileName(PMap::MAP_OBJ_ELEMENT, iter->first);
						imgName = picName.data();
						imgName = imgName + ImageSet_ExtName;
						if (imgFile.Load(imgName))
						{
							pImgInfo = pMapEditorView->m_ImgInfoMap[picName];
							for (i=0;i<4;i++)
							{
								imgFile.pos[i] = pImgInfo->m_Positions[i];
							}
							imgFile.Save(imgName);
						}
					}
				}
			}



			{
				std::map<Nuclear::SubMap::ObjMapKey, int>::iterator iter = pMapEditorView->m_ChangedLinkedObjects.begin();
				for (;iter!=pMapEditorView->m_ChangedLinkedObjects.end();iter++)
				{
					if (iter->second > 0)
					{						
						const std::wstring& lkoName = m_ToolsMap.GetObjFileName(PMap::MAP_OBJ_LINKEDOBJ, iter->first);
						pLinkObj = pMapEditorView->m_LinkedObjectMap[lkoName];
						if (pLinkObj)
							pLinkObj->Save(lkoName.c_str());
					}
				}
			}

			break;
		}
	}


	SetModifiedFlag(0);
	if (m_strPathName == L"")
	{

		SetAutoSave();
	}
	return ret;

}

BOOL CMapEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	bool ret = m_ToolsMap.LoadFromNativePath(lpszPathName);
	m_ToolsMap.ResetMapNamesToObjMapKeys();
	m_DocSize.cx = m_ToolsMap.GetWidth();
	m_DocSize.cy = m_ToolsMap.GetHeight();

	if (m_ToolsMap.GetVersion() != Nuclear::PMap::MAP_VERSION)
	{
		SetModifiedFlag();
	}

	m_bInit = true;
	SetAutoSave();
	return ret;
}

bool CMapEditorDoc::SetTile(int layer, CPoint point, TileType tile)
{

	int pos = m_ToolsMap.m_Ground.GetSmlTilePos(point.x, point.y);
	if (pos == -1)
		return false;

	TileType tempTile = m_ToolsMap.m_GroundLayersArray[layer][pos];
	if (tempTile == tile)
		return false;

	if ((tempTile & (~Nuclear::SubMap::PGround::TILE_MSK_TILE_NUMBER)) != (tile & (~Nuclear::SubMap::PGround::TILE_MSK_TILE_NUMBER)))
		return false;
	m_ToolsMap.m_GroundLayersArray[layer][pos] = tile;
	return true;
}

bool CMapEditorDoc::SetTile(Nuclear::SubMap::GroundLayers layer, CPoint point, BiggerTileType tile)
{
	return SetTileForcibly(layer, point.x, point.y, tile);

}

bool CMapEditorDoc::SetTileForcibly(int layer, int x, int y, TileType tile)
{

	int pos = m_ToolsMap.m_Ground.GetSmlTilePos(x, y);
	if (pos == -1)
		return false;

	TileType tempTile = m_ToolsMap.m_GroundLayersArray[layer][pos];
	if (tempTile == tile)
		return false;
	m_ToolsMap.m_GroundLayersArray[layer][pos] = tile;
	return true;
}

bool CMapEditorDoc::SetTileForcibly(Nuclear::SubMap::GroundLayers layer, int x, int y, BiggerTileType tile)
{

	int pos;
	PtrBiggerTileType pTile;
	switch (layer)
	{
	case Nuclear::SubMap::GL_SMALL:
		return false;
		break;
	case Nuclear::SubMap::GL_MID1:
		pos = m_ToolsMap.m_Ground.GetMidTilePos(x, y);
		pTile = m_ToolsMap.GetMidTileData(0);
		break;
	case Nuclear::SubMap::GL_MID2:
		pos = m_ToolsMap.m_Ground.GetMidTilePos(x, y);
		pTile = m_ToolsMap.GetMidTileData(1);
		break;
	case Nuclear::SubMap::GL_BIG:
		pos = m_ToolsMap.m_Ground.GetBigTilePos(x, y);
		pTile = m_ToolsMap.GetBigTileData();
		break;
	}
	if (pos == -1)
		return false;

	BiggerTileType tempTile = pTile[pos];
	if (tempTile.data == tile.data)
		return false;
	pTile[pos] = tile;
	return true;
}

bool CMapEditorDoc::SetType(int layer, Nuclear::CRECT rect, unsigned char type, bool isDelete)
{

	if (GetFactTileRect(rect, Nuclear::TST_SMALL) == 0)
		return false;
	int row, col, width, height, pos;
	TileType tempTile;
	TileType *pTile;
	unsigned char toSetTileType;
	bool changed = false;
	unsigned char count;

	width = m_ToolsMap.GetWidthForSmlTiles();
	height = m_ToolsMap.GetHeightForSmlTiles();
	tempTile = Nuclear::SubMap::PGround::TILE_NULL;
	if (isDelete)
	{
		count = 0;
		type = 0;
	} else {
		count = CalculateTileNum(type, Nuclear::SubMap::PGround::TILE_SOLID);
		tempTile = Nuclear::SubMap::PGround::SetGroundType(Nuclear::SubMap::PGround::TILE_NULL, type);
		tempTile = Nuclear::SubMap::PGround::SetTileType(tempTile, Nuclear::SubMap::PGround::TILE_SOLID);
	}

	for (row=rect.top+1;row<rect.bottom;row++)
	{

		pTile = m_ToolsMap.m_GroundLayersArray[layer] + row * width + rect.left+1;
		for (col=rect.left+1;col<rect.right;col++,pTile++)
		{
			if (((*pTile) & (~Nuclear::SubMap::PGround::TILE_MSK_TILE_NUMBER)) == tempTile)
				continue;
			changed = true;
			*pTile = GetRandomTile(tempTile, count);
		}

	}

	if (rect.top >= 0 && rect.top < height)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + rect.top * width + rect.left + 1;

		toSetTileType = Nuclear::SubMap::PGround::TILE_BOTTOM;
		if (SetTileForLine(pTile, rect.right - rect.left - 1, 1, toSetTileType, type, isDelete))
			changed = true;
	}



	if (rect.bottom >= 0 && rect.bottom < height)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + rect.bottom * width + rect.left + 1;

		toSetTileType = Nuclear::SubMap::PGround::TILE_TOP;
		if (SetTileForLine(pTile, rect.right - rect.left - 1, 1, toSetTileType, type, isDelete))
			changed = true;
	}


	if (rect.left >= 0 && rect.left < width)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + (rect.top + 1) * width + rect.left;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT;
		if (SetTileForLine(pTile, rect.bottom - rect.top - 1, width, toSetTileType, type, isDelete))
			changed = true;
	}

	if (rect.right >= 0 && rect.right < width)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + (rect.top + 1) * width + rect.right;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT;
		if (SetTileForLine(pTile, rect.bottom - rect.top - 1, width, toSetTileType, type, isDelete))
			changed = true;
	}


	pos = m_ToolsMap.m_Ground.GetSmlTilePos(rect.left, rect.top);
	if (pos >= 0)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + pos;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT_BOTTOM_S;
		if (SetTileForLine(pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	pos = m_ToolsMap.m_Ground.GetSmlTilePos(rect.right, rect.top);
	if (pos >= 0)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + pos;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT_BOTTOM_S;
		if (SetTileForLine(pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	pos = m_ToolsMap.m_Ground.GetSmlTilePos(rect.right, rect.bottom);
	if (pos >= 0)
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + pos;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT_TOP_S;
		if (SetTileForLine(pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	pos = m_ToolsMap.m_Ground.GetSmlTilePos(rect.left, rect.bottom);
	if (pos >=0 )
	{
		pTile = m_ToolsMap.m_GroundLayersArray[layer] + pos;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT_TOP_S;
		if (SetTileForLine(pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}
	return changed;
}

bool CMapEditorDoc::SetType(Nuclear::SubMap::GroundLayers layer, Nuclear::CRECT rect, unsigned char type, bool isDelete)
{
	Nuclear::TILE_SIZE_TYPE tileSize;
	int width, height;
	switch(layer)
	{
	case Nuclear::SubMap::GL_SMALL:
		return false;
		break;
	case Nuclear::SubMap::GL_MID1:
	case Nuclear::SubMap::GL_MID2:
		tileSize = Nuclear::TST_MIDDLE;
		width = m_ToolsMap.GetWidthForMidTiles();
		height = m_ToolsMap.GetHeightForMidTiles();
		break;
	case Nuclear::SubMap::GL_BIG:
		tileSize = Nuclear::TST_BIG;
		width = m_ToolsMap.GetWidthForBigTiles();
		height = m_ToolsMap.GetHeightForBigTiles();
		break;
	}

	if (GetFactTileRect(rect, tileSize) == 0)
		return false;
	int row, col, posLT, posLB, posRT, posRB;
	BiggerTileType tempTile;
	BiggerTileType *pTile, *pTiles;
	switch(layer)
	{
	case Nuclear::SubMap::GL_MID1:
		pTiles = m_ToolsMap.GetMidTileData(0);
		break;
	case Nuclear::SubMap::GL_MID2:
		pTiles = m_ToolsMap.GetMidTileData(1);
		break;
	case Nuclear::SubMap::GL_BIG:
		pTiles = m_ToolsMap.GetBigTileData();
		break;
	}
	unsigned char toSetTileType;
	bool changed = false;
	unsigned char count;

	if (isDelete)
	{
		count = 0;
		type = 0;
	} else {
		count = CalculateBiggerTileNum(tileSize, type);
		tempTile.tileType = type;
	}

	for (row=rect.top+1;row<rect.bottom;row++)
	{
		pTile = pTiles + row * width + rect.left+1;
		for (col=rect.left+1;col<rect.right;col++,pTile++)
		{
			if ((pTile->tileType == tempTile.tileType) && (pTile->tileID >= 15))
				continue;
			changed = true;
			*pTile = GetRendomBiggerTile(tempTile, count);
		}

	}

	if (rect.top >= 0 && rect.top < height)
	{
		pTile = pTiles + rect.top * width + rect.left + 1;

		toSetTileType = Nuclear::SubMap::PGround::TILE_BOTTOM;
		if (SetTileForLine(tileSize, pTile, rect.right - rect.left - 1, 1, toSetTileType, type, isDelete))
			changed = true;
	}



	if (rect.bottom >= 0 && rect.bottom < height)
	{
		pTile = pTiles + rect.bottom * width + rect.left + 1;

		toSetTileType = Nuclear::SubMap::PGround::TILE_TOP;
		if (SetTileForLine(tileSize, pTile, rect.right - rect.left - 1, 1, toSetTileType, type, isDelete))
			changed = true;
	}


	if (rect.left >= 0 && rect.left < width)
	{
		pTile = pTiles + (rect.top + 1) * width + rect.left;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT;
		if (SetTileForLine(tileSize, pTile, rect.bottom - rect.top - 1, width, toSetTileType, type, isDelete))
			changed = true;
	}

	if (rect.right >= 0 && rect.right < width)
	{
		pTile = pTiles + (rect.top + 1) * width + rect.right;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT;
		if (SetTileForLine(tileSize, pTile, rect.bottom - rect.top - 1, width, toSetTileType, type, isDelete))
			changed = true;
	}


	switch (tileSize)
	{
	case Nuclear::TST_BIG:
		posLT = m_ToolsMap.m_Ground.GetBigTilePos(rect.left, rect.top);
		posRT = m_ToolsMap.m_Ground.GetBigTilePos(rect.right, rect.top);
		posRB = m_ToolsMap.m_Ground.GetBigTilePos(rect.right, rect.bottom);
		posLB = m_ToolsMap.m_Ground.GetBigTilePos(rect.left, rect.bottom);
		break;
	case Nuclear::TST_MIDDLE:
		posLT = m_ToolsMap.m_Ground.GetMidTilePos(rect.left, rect.top);
		posRT = m_ToolsMap.m_Ground.GetMidTilePos(rect.right, rect.top);
		posRB = m_ToolsMap.m_Ground.GetMidTilePos(rect.right, rect.bottom);
		posLB = m_ToolsMap.m_Ground.GetMidTilePos(rect.left, rect.bottom);
		break;
	}
	if (posLT >= 0)
	{
		pTile = pTiles + posLT;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT_BOTTOM_S;
		if (SetTileForLine(tileSize, pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	if (posRT >= 0)
	{
		pTile = pTiles + posRT;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT_BOTTOM_S;
		if (SetTileForLine(tileSize, pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	if (posRB >= 0)
	{
		pTile = pTiles + posRB;

		toSetTileType = Nuclear::SubMap::PGround::TILE_LEFT_TOP_S;
		if (SetTileForLine(tileSize, pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}

	if (posLB >=0 )
	{
		pTile = pTiles + posLB;

		toSetTileType = Nuclear::SubMap::PGround::TILE_RIGHT_TOP_S;
		if (SetTileForLine(tileSize, pTile, 1, 0, toSetTileType, type, isDelete))
			changed = true;
	}
	return changed;
}

bool CMapEditorDoc::SetTileForLine(Nuclear::TILE_SIZE_TYPE tileSize, PtrBiggerTileType pTile, int tileCount, int step, unsigned char toSetTileType, unsigned char type, bool isDelete)
{
	bool changed = false;
	int i;
	BiggerTileType tempTile;
	unsigned char tileType;
	for (i=0;i<tileCount;i++,pTile += step)
	{
		tempTile = *pTile;
		if (isDelete) {	
			type = tempTile.tileType;
		}
		if (tempTile.tileType != type &&	
			tempTile.tileID != 0)
		{

			tempTile = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
		}
		tileType = tempTile.tileID;
		if (tileType > 15)
			tileType = 15;
		if (tileType == 0)
		{

			tempTile.tileType = type;
		}
		if (isDelete) 
		{
			if (tileType & toSetTileType)
			{
				tileType &= ~toSetTileType;
			} else {
				continue;
			}
		} else {
			if ((tileType & toSetTileType) == toSetTileType)
				continue;
			tileType |= toSetTileType;
		}
		changed = true;
		if (tileType == 0)
		{
			tempTile = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
		} else {
			if (tileType == 15)
			{
				if (tempTile.tileID < 15)
				{
					tempTile = GetRendomBiggerTile(tempTile, CalculateBiggerTileNum(tileSize, type));
				}
			} else {
				tempTile.tileID = tileType;
			}
		}
		*pTile = tempTile;
	}
	return changed;
}

bool CMapEditorDoc::SetTileForLine(PtrTileType pTile, int tileCount, int step, unsigned char toSetTileType, unsigned char type, bool isDelete)
{
	bool changed = false;
	int i;
	TileType tempTile;
	unsigned char tileType;
	unsigned char count;
	for (i=0;i<tileCount;i++,pTile += step)
	{
		tempTile = *pTile;
		if (isDelete) {	
			type = Nuclear::SubMap::PGround::GetGroundType(tempTile);
		}
		if (Nuclear::SubMap::PGround::GetGroundType(tempTile) != type &&	
			Nuclear::SubMap::PGround::GetTileType(tempTile) != Nuclear::SubMap::PGround::TILE_EMPTY)
		{

			tempTile = Nuclear::SubMap::PGround::TILE_NULL;
		}
		count = 0;
		tileType = Nuclear::SubMap::PGround::GetTileType(tempTile);
		if (tileType == Nuclear::SubMap::PGround::TILE_EMPTY)
		{

			tempTile = Nuclear::SubMap::PGround::SetGroundType(tempTile, type);
		}
		if (isDelete) 
		{
			if (tileType & toSetTileType)
			{
				tileType &= ~toSetTileType;
			} else {
				continue;
			}
		} else {
			if ((tileType & toSetTileType) == toSetTileType)
				continue;
			tileType |= toSetTileType;
		}
		changed = true;
		if (tileType == Nuclear::SubMap::PGround::TILE_EMPTY)
		{
			tempTile = Nuclear::SubMap::PGround::TILE_NULL;
		} else {
			count = CalculateTileNum(type, tileType);
			tempTile = Nuclear::SubMap::PGround::SetTileType(tempTile, tileType);
			tempTile = GetRandomTile(tempTile, count);
		}
		*pTile = tempTile;
	}
	return changed;
}

unsigned int CMapEditorDoc::GetFactTileRect(Nuclear::CRECT& rect, Nuclear::TILE_SIZE_TYPE tileSize, bool isCloseRange)
{
	int min, max, width, height;
	min = isCloseRange?0:-1;
	switch (tileSize)
	{
	case Nuclear::TST_SMALL:
		width = m_ToolsMap.GetWidthForSmlTiles();
		height = m_ToolsMap.GetHeightForSmlTiles();
		break;
	case Nuclear::TST_MIDDLE:
		width = m_ToolsMap.GetWidthForMidTiles();
		height = m_ToolsMap.GetHeightForMidTiles();
		break;
	case Nuclear::TST_BIG:
		width = m_ToolsMap.GetWidthForBigTiles();
		height = m_ToolsMap.GetHeightForBigTiles();
		break;
	}
	max = width;
	max = isCloseRange?max-1:max;
	if (rect.left < min)
		rect.left = min;
	if (rect.right > max)
		rect.right = max;
	if (rect.top < min)
		rect.top = min;
	max = height;
	max = isCloseRange?max-1:max;
	if (rect.bottom > max)
		rect.bottom = max;
	if (rect.left > rect.right)
		return 0;
	if (rect.top > rect.bottom)
		return 0;
	return (rect.right - rect.left + 1) * (rect.bottom - rect.top + 1);

}

#ifdef _DEBUG
void CMapEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CMapEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif 

void CMapEditorDoc::Serialize(CArchive& ar)
{

}

BOOL CMapEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;



	BOOL ret = TRUE;
	CToolsMap::TileLayerInfo tmpInfo;
	if (theApp.IsImport())
	{
		CString fileName;
		CFileDialog fileDlg(true, NULL, NULL, OFN_HIDEREADONLY|OFN_OVERWRITEPROMPT, _T("RMP文件 (*.rmp)|*.rmp||"));
		fileDlg.m_ofn.lpstrFile = fileName.GetBuffer(_MAX_PATH);
		INT_PTR nResult = fileDlg.DoModal();
		fileName.ReleaseBuffer();
		if (nResult != IDOK)
			return FALSE;
		ret = m_ToolsMap.ImportFromFile(fileName);
		m_DocSize.cx = m_ToolsMap.GetWidth();
		m_DocSize.cy = m_ToolsMap.GetHeight();

	} else if (theApp.IsMergeImport())	
	{
		CString fullPath, folderName;
		if (theApp.SelectExportFolder(fullPath, folderName, theApp.GetMainWnd()->GetSafeHwnd(), _T("请选择导入路径"),  BIF_RETURNFSANCESTORS | BIF_NEWDIALOGSTYLE | BIF_NONEWFOLDERBUTTON))
		{
			CFileFind myFinder;
			BOOL working;
			working = myFinder.FindFile(fullPath + _T("*.mrmp"));
			FILE *f = NULL;
			bool isFirst = true;
			while (working)
			{
				working = myFinder.FindNextFile();
				if (myFinder.IsDots() || myFinder.IsDirectory() || myFinder.IsHidden()) 
				{
					continue;
				}
				if (isFirst)
				{
					if (m_ToolsMap.LoadFromNativePath(myFinder.GetFilePath()))
					{
						m_ToolsMap.ResetMapNamesToObjMapKeys();
						isFirst = false;
					}
				} else {
					CToolsMap tmpMap;
					if (tmpMap.LoadFromNativePath(myFinder.GetFilePath()))
					{
						m_ToolsMap.MargeMap(tmpMap);
					}
				}
			}
			m_DocSize.cx = m_ToolsMap.GetWidth();
			m_DocSize.cy = m_ToolsMap.GetHeight();
			myFinder.Close();
			SetModifiedFlag();
		} else {
			return FALSE;
		}
	} else {
		CRegionMapInfoDlg dlg;
		if( IDOK != dlg.DoModal() )
			return FALSE;

		if( !m_ToolsMap.Init(dlg.m_nWidth, dlg.m_nHeight) ) return FALSE;
		m_DocSize.cx = dlg.m_nWidth;
		m_DocSize.cy = dlg.m_nHeight;
		m_ToolsMap.ClearSign();
		if (dlg.m_bIsUndergroundPalace)
		{
			m_ToolsMap.AddSign(Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE);
		} else {
			m_ToolsMap.RemoveSign(Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE);
		}
		for (int i=0;i<3;++i)
		{
			m_ToolsMap.SetWaterName(i, dlg.m_EffectName[i].GetBuffer());
			dlg.m_EffectName[i].ReleaseBuffer();
		}
		m_ToolsMap.NewGroundLayer();
		m_ToolsMap.SetGroundLayerName(0, L"图层 1");

		if (dlg.m_bHyalineGroundType)
		{
			m_ToolsMap.FillSmallGroundLayer(0, Nuclear::SubMap::PGround::TILE_NULL);
		} else {
			m_ToolsMap.RandomTiles(0, dlg.m_nGroundType, CalculateTileNum(dlg.m_nGroundType));
		}
	}

	m_bInit = true;
	return ret;
}

CMapEditorDoc::~CMapEditorDoc() {}

CMapEditorDoc::CMapEditorDoc() : m_bInit(false), m_bPictureInit(false)
{
}

BEGIN_MESSAGE_MAP(CMapEditorDoc, CDocument)
END_MESSAGE_MAP()

void CMapEditorDoc::ModifyTriggerArea(Nuclear::SubMap::TriggerID triggerID, const Nuclear::CRECT& newArea)
{
	return m_ToolsMap.ModifyAnTriggerArea(triggerID, newArea);
}

void CMapEditorDoc::RemoveTriggerArea(Nuclear::SubMap::TriggerID triggerID)
{
	return m_ToolsMap.RemoveAnTriggerArea(triggerID);
}

Nuclear::SubMap::TriggerID CMapEditorDoc::AddTriggerArea(Nuclear::SubMap::TriggerInfoID infoID, const Nuclear::CRECT& area)
{
	return m_ToolsMap.AddAnTriggerArea(infoID, area);
}

void CMapEditorDoc::SetTriggerInfos(const Nuclear::SubMap::TriggerInfoMap& pmap)
{
	return m_ToolsMap.SetTriggerInfos(pmap);
}

const Nuclear::SubMap::TriggerInfoMap& CMapEditorDoc::GetTriggerInfos() const
{
	return m_ToolsMap.GetTriggerInfos();
}

void CMapEditorDoc::ModifyTriggerInfoArray(Nuclear::SubMap::TriggerInfoID infoID, const std::wstring &name, const Nuclear::SubMap::TriggerInfoVet& obj)
{
	return m_ToolsMap.ModifyAnTriggerInfoVet(infoID, name, obj);
}

bool CMapEditorDoc::RemoveTriggerInfoArray(Nuclear::SubMap::TriggerInfoID infoID)
{
	return m_ToolsMap.RemoveAnTriggerInfoVet(infoID);
}

Nuclear::SubMap::TriggerInfoID CMapEditorDoc::AddTriggerInfoArray(const std::wstring &name, const Nuclear::SubMap::TriggerInfoVet& obj)
{
	return m_ToolsMap.AddAnTriggerInfoVet(name, obj);
}

void CMapEditorDoc::SetObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjNameMap& nameMap)
{
	m_ToolsMap.m_objs[type].GetObjFileNameMap() = nameMap;
}

void CMapEditorDoc::SetObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjMapKey key, const std::wstring &name)
{
	m_ToolsMap.m_objs[type].SetObjFileName(key, name);
}

void CMapEditorDoc::DeleteObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjMapKey key)
{
	m_ToolsMap.m_objs[type].GetObjFileNameMap().erase(key);
}

