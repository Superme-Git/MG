#include "StdAfx.h"
#include "MapEditor.h"
#include "ReplaceObjs.h"

#include "..\engine\common\xml.h"

void CReplaceObjs::DoReplace()
{
	MapInfoArray::iterator mapIt = m_MapInfos.begin(), mapIe = m_MapInfos.end();
	CToolsMap* pMap = NULL;
	std::map<std::wstring, Nuclear::SubMap::ObjMapKey>::iterator n2kIt, n2kIe;
	for (;mapIt!=mapIe;++mapIt)
	{
		ReplaceMap::iterator rplsIt = m_LinkedObjectToImgReplaceMap.begin(), rplsIe = m_LinkedObjectToImgReplaceMap.end();
		bool changed = false;
		pMap = mapIt->m_pMap;
		n2kIe = pMap->m_mapNamesToObjMapKey[Nuclear::PMap::MAP_OBJ_LINKEDOBJ].end();
		for (;rplsIt!=rplsIe;++rplsIt)
		{

			n2kIt = pMap->m_mapNamesToObjMapKey[Nuclear::PMap::MAP_OBJ_LINKEDOBJ].find(rplsIt->first);
			if (n2kIt != n2kIe)
			{
				Nuclear::SubMap::ObjMapKey eleKey = pMap->GetObjMapKey(Nuclear::PMap::MAP_OBJ_ELEMENT, rplsIt->second);
				Nuclear::SubMap::ObjMapKey lkoKey = n2kIt->second;
				Nuclear::SubMap::ObjLayerVector& lkoVet = pMap->m_objs[Nuclear::PMap::MAP_OBJ_LINKEDOBJ].m_vectorLayers;
				Nuclear::SubMap::ObjLayerVector::iterator lLayerIt = lkoVet.begin(), lLayerIe = lkoVet.end();
				int layer = 0;
				Nuclear::PImg& img = m_ImgMap[rplsIt->second];
				for (;lLayerIt != lLayerIe; ++lLayerIt, ++layer)
				{
					Nuclear::SubMap::ObjInfoVector::iterator lIt = lLayerIt->vectorObjs.begin(), lIe = lLayerIt->vectorObjs.end();
					for (;lIt != lIe; ++lIt)
					{
						if (lIt->m_MapKey == lkoKey && (!lIt->m_bDeleted))
						{
							lIt->m_bDeleted = true;
							--lLayerIt->m_ObjectCount;
							Nuclear::CRECT rect;
							rect.left = lIt->m_Position.x - img.CenterPoint.x;
							rect.top = lIt->m_Position.y - img.CenterPoint.y;
							rect.right = rect.left + img.m_nFileWidth;
							rect.bottom = rect.top + img.m_nFileHeight;
							pMap->AddObj(Nuclear::PMap::MAP_OBJ_ELEMENT, layer, eleKey, rect, lIt->m_Position, lIt->m_AttachColor.data);
							changed = true;
						}
					}
				}
			}
		}

		rplsIt = m_ImgToLinkedObjectReplaceMap.begin();
		rplsIe = m_ImgToLinkedObjectReplaceMap.end();


		n2kIe = pMap->m_mapNamesToObjMapKey[Nuclear::PMap::MAP_OBJ_ELEMENT].end();
		for (;rplsIt!=rplsIe;++rplsIt)
		{
			n2kIt = pMap->m_mapNamesToObjMapKey[Nuclear::PMap::MAP_OBJ_ELEMENT].find(rplsIt->first);
			if (n2kIt != n2kIe)
			{
				Nuclear::SubMap::ObjMapKey lkoKey = pMap->GetObjMapKey(Nuclear::PMap::MAP_OBJ_LINKEDOBJ, rplsIt->second);
				Nuclear::SubMap::ObjMapKey eleKey = n2kIt->second;
				Nuclear::SubMap::ObjLayerVector& eleVet = pMap->m_objs[Nuclear::PMap::MAP_OBJ_ELEMENT].m_vectorLayers;
				Nuclear::SubMap::ObjLayerVector::iterator lLayerIt = eleVet.begin(), lLayerIe = eleVet.end();
				int layer = 0;
				Nuclear::PLinkedObject& lko = m_LinkedObjectMap[rplsIt->second];
				std::wstring imgname = lko.m_vectorLinkedObjectInfo[lko.GetMainNodeID()].m_strPicName;
				imgname.erase(imgname.length() - 4, 4);
				std::transform(imgname.begin(), imgname.end(), imgname.begin(), ::towlower);
				if (imgname != rplsIt->first)
				{
					CString err;
					err.Format(L"源img文件和要替换的lko的主节点的img文件不一致。\n  源img文件为：'%s'\n  lko主节点img文件为'%s'", rplsIt->first.c_str(), imgname.c_str());
					AfxMessageBox(err);
				}
				for (;lLayerIt != lLayerIe; ++lLayerIt, ++layer)
				{
					Nuclear::SubMap::ObjInfoVector::iterator lIt = lLayerIt->vectorObjs.begin(), lIe = lLayerIt->vectorObjs.end();
					for (;lIt != lIe; ++lIt)
					{
						if (lIt->m_MapKey == eleKey && (!lIt->m_bDeleted))
						{
							lIt->m_bDeleted = true;
							--lLayerIt->m_ObjectCount;
							Nuclear::CPOINT pos = lko.m_ptCentPos;
							pos.x += lIt->m_Rect.left;
							pos.y += lIt->m_Rect.top;
							Nuclear::CRECT rect = lko.m_rctMaxBox;
							rect += pos;
							pMap->AddObj(Nuclear::PMap::MAP_OBJ_LINKEDOBJ, layer, lkoKey, rect, pos, lIt->m_AttachColor.data);
							changed = true;
						}
					}
				}
			}
		}
		if (changed)
		{
			pMap->SaveToNativePath(mapIt->m_Uri.c_str());
		}
	}
}

void CReplaceObjs::LoadMapInfoFromXML(const std::wstring &name)
{
	XMLIO::CFileReader fr;
	int nError = fr.OpenNativeFile(name);

	if( XMLIO::EC_SUCCESS != nError)
	{
		fr.CloseFile();
		AfxMessageBox(L"打开文件错误\n");
		return;
	}

	XMLIO::CINode root;
	if (!fr.GetRootNode(root))
	{
		fr.CloseFile();
		AfxMessageBox(L"解释文件错误\n");
		return;
	}

	XMLIO::CNodeList nodeList;
	root.GetChildren(nodeList);
	bool r = true;

	for( XMLIO::CNodeList::iterator it = nodeList.begin(); it != nodeList.end(); ++it)
	{
		XMLIO::CINode& node = *it;
		if (node.GetType() != XMLIO::NT_ELEMENT)
			continue;
		std::wstring src;
		if (!node.GetAttribute(L"src", src))
		{
			AfxMessageBox(L"解释文件错误: 没有找到src字段\n");
			continue;
		}

		std::wstring dst;
		if (!node.GetAttribute(L"dst", dst))
		{
			AfxMessageBox(L"解释文件错误: 没有找到dst字段\n");
			continue;
		}
		std::transform(src.begin(), src.end(), src.begin(), ::towlower);
		std::transform(dst.begin(), dst.end(), dst.begin(), ::towlower);
		if (node.GetName() == L"img2lko")
		{
			if (!m_LinkedObjectMap[dst].Load(dst.c_str()))
			{
				CString err;
				err.Format(L"读取lko文件 %s 错误。\n", dst.c_str());
				AfxMessageBox(err);
				m_LinkedObjectMap.erase(dst);
			} else {
				m_ImgToLinkedObjectReplaceMap[src] = dst;
			}

		} else if (node.GetName() == L"lko2img")
		{
			std::wstring dsfFilename = dst + ImageSet_ExtName;
			if (!m_ImgMap[dst].Load(dsfFilename.c_str()))
			{
				CString err;
				err.Format(L"读取img文件 %s 错误。\n", dsfFilename.c_str());
				AfxMessageBox(err);
				m_ImgMap.erase(dst);
			} else {
				m_LinkedObjectToImgReplaceMap[src] = dst;
			}
		}

	}
}

void CReplaceObjs::ResetMapInfos()
{
	ReleaseMapInfos();
	CFileFind myFinder;
	CString FileName;
	CStringList FileList;
	BOOL working;
	MapInfo tmpMapInfo;
	FileName=theApp.GetResPath() + _T(".\\map");
	FileList.AddTail(FileName);
	while (!FileList.IsEmpty())  
	{
		FileName=FileList.GetHead()+_T("\\*.*");
		FileList.RemoveHead();
		working = myFinder.FindFile(FileName);
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			FileName = myFinder.GetFilePath().MakeLower();
			if (myFinder.IsDirectory()) 
			{
				if (FileName.Right(wcslen(L"maze")) == L"maze")
					continue;
				if (FileName.Right(wcslen(L"tiles")) == L"tiles")
					continue;
				if (FileName.Right(wcslen(L"elements")) == L"elements")
					continue;
				if (FileName.Right(wcslen(L"linkedobjects")) == L"linkedobjects")
					continue;
				if (FileName.Right(wcslen(L"distortionobjects")) == L"distortionobjects")
					continue;
				FileList.AddTail(FileName);
			} else {
				if (FileName.Right(wcslen(L".mrmp")) != L".mrmp")
					continue;
				tmpMapInfo.m_pMap = new CToolsMap();
				tmpMapInfo.m_pMap->LoadFromNativePath(FileName);
				tmpMapInfo.m_pMap->ResetMapNamesToObjMapKeys();
				tmpMapInfo.m_Uri = FileName.GetBuffer();
				FileName.ReleaseBuffer();
				m_MapInfos.push_back(tmpMapInfo);

			}
		}
	}
}

void CReplaceObjs::ReleaseMapInfos()
{
	MapInfoArray::iterator it, ie;
	it = m_MapInfos.begin();
	ie = m_MapInfos.end();
	for (;it!=ie;++it)
	{
		delete (it->m_pMap);
	}
	m_MapInfos.clear();
}

void CReplaceObjs::Do(const std::wstring &name)
{
	LoadMapInfoFromXML(name);
	ResetMapInfos();
	DoReplace();
}

CReplaceObjs::~CReplaceObjs(void)
{
	ReleaseMapInfos();
}

CReplaceObjs::CReplaceObjs(void)
{
	m_ImgToLinkedObjectReplaceMap.clear();
	m_LinkedObjectToImgReplaceMap.clear();
	m_MapInfos.clear();
}

