#pragma once
#include "..\engine\world\regionmap.h"

class CPerformanceMap : public Nuclear::RegionMap
{
private:
	typedef std::set<Nuclear::PicResrc*> PictureResourceSet;
public:
	typedef std::map<Nuclear::PicResrc*, int> PictureSizeMap;

private:
	PictureSizeMap m_PictureStorageSize;	
	PictureSizeMap m_PictureAreaSize;		

public:
	virtual ~CPerformanceMap(void);
	CPerformanceMap(Nuclear::EngineBase *pEB, Nuclear::PicResrcMap* pPicResrcMap);

private:
	int GetRegionStorageSize(const RegionInfo &region, PictureResourceSet &picset);
	void CalculateSize();
	
protected:
	virtual void AddAreaSize(Nuclear::PicResrc* pic, const Nuclear::CRECT& rect)
	{
		m_PictureAreaSize[pic] = rect.Width() * rect.Height();
	}

public:
	int GetAreaSize(Nuclear::CRECT &rect, const Nuclear::SubMap::WaterTileType *pWaterData);
	void UpdateLinkedObjectTree(Nuclear::CRECT &rect);
	int GetStorageSize(Nuclear::CRECT &rect);
	int GetMapStorageSize();
	bool LoadMap(Nuclear::PMap& pmap);

};
