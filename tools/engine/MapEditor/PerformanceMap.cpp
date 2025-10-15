#include "StdAfx.h"
#include "MapEditor.h"
#include "..\engine\world\lkotree.h"
#include "..\engine\world\distort.h"
#include "PerformanceMap.h"


int CPerformanceMap::GetAreaSize(Nuclear::CRECT &rect, const Nuclear::SubMap::WaterTileType *pWaterData)
{
	int minregioncol = rect.left / REGION_WIDTH - 1;
	if( minregioncol < 0 ) minregioncol = 0;

	int maxregioncol = (rect.right - 1 ) / REGION_WIDTH + 1;
	if( maxregioncol >= m_regioncol ) maxregioncol = m_regioncol - 1;

	int minregionrow = rect.top / REGION_HEIGHT -1;
	if( minregionrow < 0 ) minregionrow = 0;

	int maxregionrow = (rect.bottom -1 )/ REGION_HEIGHT + 1;
	if( maxregionrow >= m_regionrow ) maxregionrow = m_regionrow - 1;

	int regionPos, layer;
	int drawAreaSize = 0;



	{
		int x, y, i, j;
		int size = Nuclear::SubMap::PGround::WATER_WIDTH * Nuclear::SubMap::PGround::WATER_HEIGHT;
		x = static_cast<int>(rect.right / Nuclear::SubMap::PGround::WATER_WIDTH);
		if (x >= m_widthForWater)
		{
			x = m_widthForWater - 1;
		}
		y = static_cast<int>(rect.bottom  / Nuclear::SubMap::PGround::WATER_HEIGHT);
		if (y >= m_heightForWater)
		{
			y = m_heightForWater - 1;
		}

		const Nuclear::SubMap::WaterTileType *pTmpWater;
		for (j=rect.top / Nuclear::SubMap::PGround::WATER_HEIGHT; j<=y ; j++) 
		{
			i = rect.left / Nuclear::SubMap::PGround::WATER_WIDTH;
			pTmpWater =  pWaterData + (j * m_widthForWater + i);
			for ( ; i<=x ; i++, pTmpWater++)
			{
				if (pTmpWater->data > 0)
				{
					drawAreaSize += size;
				}
			}
		}
	}


	std::vector<Nuclear::Region::PicEle>::const_iterator itEle, ieEle;
	std::vector<Nuclear::LkoTree*>::const_iterator treeIter, treeIE;
	std::vector<Nuclear::Distort*>::const_iterator distIter, distIE;
	std::vector<Nuclear::Region::EffectInfo>::const_iterator effIter, effIE;
	Nuclear::LkoTree::DEList::const_iterator DEiter, DEiterEnd;
	Nuclear::CRECT objRect;
	Nuclear::CRECT wrect;
	Nuclear::FRECT tmpRect;
	Nuclear::FPOINT pt, ptOut;
	Nuclear::Region::LkoEle* pDE;
	Nuclear::FRECT fvp(0.0f, 0.0f, static_cast<float>(rect.Width()), static_cast<float>(rect.Height()));
	std::set<Nuclear::LkoTree*> hasDrawn;
	std::set<Nuclear::Distort*> hasDrawnDist;
	int pointCount;


	drawAreaSize += rect.Width() * rect.Height();


	for(layer=Nuclear::PIC_CATCH_LAYER_COUNT; layer<Nuclear::PIC_LAYER_COUNT; ++layer)
	{
		hasDrawn.clear();
		hasDrawnDist.clear();
		for(int row = minregionrow; row <= maxregionrow; ++row)
		{
			regionPos = row*m_regioncol + minregioncol;
			for(int col = minregioncol; col <= maxregioncol; ++col, ++regionPos)
			{

				treeIter = m_regions[regionPos].m_vpLkoTrees[layer-Nuclear::PIC_FIRST_ELEMENT_ID].begin();
				treeIE = m_regions[regionPos].m_vpLkoTrees[layer-Nuclear::PIC_FIRST_ELEMENT_ID].end();
				for (;treeIter!=treeIE;++treeIter)
				{
					if (hasDrawn.find(*treeIter) == hasDrawn.end())
					{
						const Nuclear::LkoTree::DEList &delist = (*treeIter)->GetLkoEleList();
						DEiter = delist.begin();
						DEiterEnd = delist.end();
						for (;DEiter!=DEiterEnd;++DEiter)
						{
							pDE = (*DEiter);
							pt.x = pDE->rect.left;
							pt.y = pDE->rect.top;
							pointCount = 0;
							XPVec3TransformCoord(&ptOut, &pt, &pDE->pTreeNode->mTransResult);
							if (fvp.PtInRect(ptOut))
								++pointCount;
							pt.y = pDE->rect.bottom;
							XPVec3TransformCoord(&ptOut, &pt, &pDE->pTreeNode->mTransResult);
							if (fvp.PtInRect(ptOut))
								++pointCount;
							pt.x = pDE->rect.right;
							XPVec3TransformCoord(&ptOut, &pt, &pDE->pTreeNode->mTransResult);
							if (fvp.PtInRect(ptOut))
								++pointCount;
							pt.y = pDE->rect.top;
							XPVec3TransformCoord(&ptOut, &pt, &pDE->pTreeNode->mTransResult);
							if (fvp.PtInRect(ptOut))
								++pointCount;
							drawAreaSize += static_cast<int>(pDE->rect.Width() * pDE->rect.Height() * pointCount / 4);
						}
						hasDrawn.insert(*treeIter);
					}
				}


				distIter = m_regions[regionPos].m_vpDistorts[layer-Nuclear::PIC_FIRST_ELEMENT_ID].begin();
				distIE = m_regions[regionPos].m_vpDistorts[layer-Nuclear::PIC_FIRST_ELEMENT_ID].end();
				for (;distIter!=distIE;++distIter)
				{
					if (hasDrawnDist.find(*distIter) == hasDrawnDist.end())
					{
						wrect = (*distIter)->GetRect();
						if( wrect.Cut(rect, objRect, tmpRect) )
						{
							drawAreaSize += objRect.Width() * objRect.Height();
						}
						hasDrawnDist.insert(*distIter);
					}
				}


				effIter = m_regions[regionPos].m_effects[layer-Nuclear::PIC_FIRST_ELEMENT_ID].begin();
				effIE = m_regions[regionPos].m_effects[layer-Nuclear::PIC_FIRST_ELEMENT_ID].end();
				for(;effIter != effIE; ++effIter)
				{
					wrect = effIter->rect;
					if( wrect.Cut(rect, objRect, tmpRect) )
					{
						drawAreaSize += objRect.Width() * objRect.Height() * 2;
					}
				}


				if( layer == Nuclear::PIC_MID_ELEMENT_LAYER_ID ) continue; 
				for(itEle = m_regions[regionPos].m_vPicEle[layer].begin(),
					ieEle = m_regions[regionPos].m_vPicEle[layer].end(); itEle != ieEle; ++itEle)
				{
					wrect = itEle->pos;
					if( wrect.Cut(rect, objRect, tmpRect) )
					{
						drawAreaSize += objRect.Width() * objRect.Height();
					}
				}


			}
		}
	}


	{

		std::vector<Nuclear::Region::PicEleWithBase>::const_iterator itEle, ieEle;
		for(int row = minregionrow; row <= maxregionrow; ++row)
		{
			regionPos = row*m_regioncol + minregioncol;
			for(int col = minregioncol; col <= maxregioncol; ++col, ++regionPos)
			{
				itEle = m_regions[regionPos].m_vPicEleWithBase.begin();
				ieEle = m_regions[regionPos].m_vPicEleWithBase.end();
				for (;itEle!=ieEle;++itEle)
				{
					wrect = itEle->pos;
					if( wrect.Cut(rect, objRect, tmpRect) )
					{
						drawAreaSize += objRect.Width() * objRect.Height();
					}
				}
			}
		}
	}

	return drawAreaSize;
}

void CPerformanceMap::UpdateLinkedObjectTree(Nuclear::CRECT &rect)
{
	std::set<Nuclear::LkoTree*> hasUpdated;
	int minregioncol = rect.left / REGION_WIDTH - 1;
	if( minregioncol < 0 ) minregioncol = 0;

	int maxregioncol = (rect.right - 1 ) / REGION_WIDTH + 1;
	if( maxregioncol >= m_regioncol ) maxregioncol = m_regioncol - 1;

	int minregionrow = rect.top / REGION_HEIGHT -1;
	if( minregionrow < 0 ) minregionrow = 0;

	int maxregionrow = (rect.bottom -1 )/ REGION_HEIGHT + 1;
	if( maxregionrow >= m_regionrow ) maxregionrow = m_regionrow - 1;

	std::vector<Nuclear::LkoTree*>::const_iterator iter, iterEnd;

	int regionPos;
	for (int i=0;i<Nuclear::MAP_LAYER_COUNT-1;++i)
	{
		hasUpdated.clear();
		for(int row = minregionrow; row <= maxregionrow; ++row)
		{
			regionPos = row*m_regioncol + minregioncol;
			for(int col = minregioncol; col <= maxregioncol; ++col, ++regionPos)
			{
				iter = m_regions[regionPos].m_vpLkoTrees[i].begin();
				iterEnd = m_regions[regionPos].m_vpLkoTrees[i].end();
				for (;iter!=iterEnd;++iter)
				{
					if (hasUpdated.find((*iter)) == hasUpdated.end())
					{
						(*iter)->Update(0, rect.left, rect.top, 1.0f);
						hasUpdated.insert(*iter);
					}
				}
			}
		}
	}
}

int CPerformanceMap::GetStorageSize(Nuclear::CRECT &rect)
{
	int result = 0;
	PictureResourceSet picSet;

	int minregioncol = rect.left / REGION_WIDTH - 1;
	if( minregioncol < 0 ) minregioncol = 0;

	int maxregioncol = (rect.right - 1 ) / REGION_WIDTH + 1;
	if( maxregioncol >= m_regioncol ) maxregioncol = m_regioncol - 1;

	int minregionrow = rect.top / REGION_HEIGHT -1;
	if( minregionrow < 0 ) minregionrow = 0;

	int maxregionrow = (rect.bottom -1 )/ REGION_HEIGHT + 1;
	if( maxregionrow >= m_regionrow ) maxregionrow = m_regionrow - 1;

	for(int row = minregionrow; row <= maxregionrow; ++row)
	{
		for(int col = minregioncol; col <= maxregioncol; ++col)
		{				
			result += GetRegionStorageSize(m_regions[col+row*m_regioncol], picSet);
		}
	}
	return result;
}

int CPerformanceMap::GetMapStorageSize()
{
	int result = 0;
	std::vector<RegionInfo>::iterator regIter, regEnd;
	regIter = m_regions.begin();
	regEnd = m_regions.end();
	PictureResourceSet picSet;
	for(;regIter!=regEnd;++regIter)
	{
		result += GetRegionStorageSize(*regIter, picSet);
	}
	return result;
}

int CPerformanceMap::GetRegionStorageSize(const RegionInfo &region, PictureResourceSet &picset)
{
	int result = 0;
	std::vector<Nuclear::Region::PicEle>::const_iterator picEleIter, picEleEnd;
	std::vector<Nuclear::Region::PicEleWithBase>::const_iterator picBaseIter, picBaseEnd;
	std::vector<const Nuclear::Region::LkoEle*>::const_iterator dynEleIter, dynEleEnd;
	std::vector<Nuclear::PicResrc*>::const_iterator picIter, picIterEnd; 
	int layer;
	for (layer=0;layer<Nuclear::PIC_LAYER_COUNT;++layer)
	{
		picEleIter = region.m_vPicEle[layer].begin();
		picEleEnd = region.m_vPicEle[layer].end();
		for (;picEleIter!=picEleEnd;++picEleIter)
		{
			if (picset.find(picEleIter->pPicResrc) == picset.end())
			{
				picset.insert(picEleIter->pPicResrc);
				result += m_PictureStorageSize[picEleIter->pPicResrc];
			}

		}
	}

	picBaseIter = region.m_vPicEleWithBase.begin();
	picBaseEnd = region.m_vPicEleWithBase.end();
	for (;picBaseIter!=picBaseEnd;++picBaseIter)
	{
		if (picset.find(picBaseIter->pPicResrc) == picset.end())
		{
			picset.insert(picBaseIter->pPicResrc);
			result += m_PictureStorageSize[picBaseIter->pPicResrc];
		}
	}

	for (layer=0;layer<Nuclear::PIC_ELEMENT_LAYER_COUNT;++layer)
	{
		dynEleIter = region.m_vLkoEle[layer].begin();
		dynEleEnd = region.m_vLkoEle[layer].end();
		for (;dynEleIter!=dynEleEnd;++dynEleIter)
		{
			if (picset.find((*dynEleIter)->pPicResrc) == picset.end())
			{
				picset.insert((*dynEleIter)->pPicResrc);
				result += m_PictureStorageSize[(*dynEleIter)->pPicResrc];
			}

		}
	}

	picIter = region.m_vpDistortsPic.begin();
	picIterEnd = region.m_vpDistortsPic.end();
	for (;picIter!=picIterEnd;++picIter)
	{
		if (picset.find(*picIter) == picset.end())
		{
			picset.insert(*picIter);
			result += m_PictureStorageSize[*picIter];
		}
	}
	return result;
}

bool CPerformanceMap::LoadMap(Nuclear::PMap& pmap)
{
	bool result = true;
	m_PictureAreaSize.clear();
	if (!LoadBaseInfo(pmap))
	{
		AfxMessageBox(L"LoadBaseInfo Faild");
		result = false;
	}
	if (!LoadGround(pmap, theApp.GetPathMap()))
	{
		AfxMessageBox(L"LoadGround Faild");
		result = false;
	}
	Nuclear::RegionMap::LoadingPicResrcMap picMap;
	Nuclear::RegionMap::LoadingLinkedObjMap lkoMap;
	Nuclear::RegionMap::LoadingDistortObjMap disMap;

	Nuclear::RegionMap::ElementNames names;
	if (!LoadElementPicMap(pmap, picMap, lkoMap, disMap, names))
	{
		AfxMessageBox(L"LoadElementPicMap Faild");
		result = false;
	}
	if (!LoadElements(pmap, picMap))
	{
		AfxMessageBox(L"LoadElements Faild");
		result = false;
	}
	if (!LoadEffects(pmap))
	{
		AfxMessageBox(L"LoadEffects Faild");
		result = false;
	}

	if (!LoadLinkedObjs(pmap, picMap, lkoMap, names))
	{
		AfxMessageBox(L"LoadLinkedObjs Faild");
		result = false;
	}
	if (!LoadDistortObjs(pmap, picMap, disMap, names))
	{
		AfxMessageBox(L"LoadDistortObjs Faild");
		result = false;
	}
	CalculateSize();
	return result;
}

void CPerformanceMap::CalculateSize()
{
	m_PictureStorageSize.clear();
	Nuclear::PicResrcMap::iterator iter, iterEnd;
	iter = m_pAllPicResrcs->begin();
	iterEnd = m_pAllPicResrcs->end();
	for (;iter!=iterEnd;++iter)
	{
		PFS::CFile file;
		if( !file.Open(iter->first, PFS::FM_EXCL, PFS::FA_RDONLY ))
		{
			continue;
		}
		m_PictureStorageSize[iter->second]	= static_cast<int>(file.GetSize());
		file.Close();
	}
}

CPerformanceMap::~CPerformanceMap(void)
{
}

CPerformanceMap::CPerformanceMap(Nuclear::EngineBase *pEB, Nuclear::PicResrcMap* pPicResrcMap) : Nuclear::RegionMap(pEB, NULL, pPicResrcMap)
{
}

