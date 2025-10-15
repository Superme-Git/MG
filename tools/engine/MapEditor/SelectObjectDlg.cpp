

#include "stdafx.h"
#include <list>

#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "SelectObjectDlg.h"

#include "..\engine\map\pimg.h"
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CSelectObjectDlg, CDialog)

void CSelectObjectDlg::OnBnClickedSelectBackgroundColor()
{

	CColorDialog dlg;  
	if (dlg.DoModal() == IDOK) 
	{
		unsigned int cGroundColor = dlg.GetColor();
		m_pMapEditorView->SetAttachColor(ReverseColor(cGroundColor) | 0xFF000000);
		InvalidateRect(&m_ColorRect, FALSE);
	}
}

void CSelectObjectDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);

	if (m_bSizing)
	{
		RenewSize();
		m_bSizing = false;
	}

}

void CSelectObjectDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	int width = rectCtrl.Width();


	ResizeRect(width-10, rectCtrl.Height() / 4);

	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 30;
	rectCtrl.top += m_nPictureHeight + 10;



	m_Tree.MoveWindow(&rectCtrl);

	rectCtrl.right = rectCtrl.left + 60;
	rectCtrl.top = rectCtrl.bottom + 9;
	rectCtrl.bottom = rectCtrl.top + 12;
	GetDlgItem(IDC_COLOR)->MoveWindow(&rectCtrl);

	m_ColorRect.top = rectCtrl.top;
	m_ColorRect.left = rectCtrl.right + 5;
	m_ColorRect.right = m_ColorRect.left + 50;
	m_ColorRect.bottom = m_ColorRect.top + 13;

	rectCtrl.left += 120;
	rectCtrl.right = rectCtrl.left + 25;
	rectCtrl.top -= 1;
	rectCtrl.bottom += 2;
	GetDlgItem(IDC_SELECT_BACKGROUND_COLOR)->MoveWindow(&rectCtrl);
	InvalidateRect(&m_ColorRect, FALSE);

	rectCtrl.left = 0;
	rectCtrl.right = width;
	rectCtrl.top = m_nPictureHeight + 5;
	rectCtrl.bottom = rectCtrl.top + 10;
	InvalidateRect(&rectCtrl);
	rectCtrl.left = rectCtrl.right - 5;
	rectCtrl.top = 0;
	InvalidateRect(&rectCtrl);
}
void CSelectObjectDlg::OnSizing(UINT fwSide, LPRECT pRect)
{
	if (pRect->right - pRect->left < m_nMinWidth)
	{
		pRect->right = pRect->left + m_nMinWidth;
	}

	if (pRect->bottom - pRect->top < m_nMinHeight)
	{
		pRect->bottom = pRect->top + m_nMinHeight;
	}
	CDialog::OnSizing(fwSide, pRect);

	RenewSize();
	m_bSizing = true;
}

BOOL CSelectObjectDlg::PreTranslateMessage(MSG* pMsg)
{

	if(pMsg->wParam==VK_SHIFT)
	{
		m_pMapEditorView->ResetMagnetAdjust();
	}
	return CDialog::PreTranslateMessage(pMsg);
}

void CSelectObjectDlg::ResetBrushType()
{
	HTREEITEM item;
	item = m_Tree.GetSelectedItem();
	if (item)
	{
		m_nBrushType = static_cast<unsigned int>(m_Tree.GetItemData(item));
	} else {
		m_nBrushType = 0;
	}
	ResetDisplay();
	m_pMapEditorView->SetBrushType(m_nBrushType);
}

void CSelectObjectDlg::OnShowWindow(BOOL bShow, UINT nStatus)
{
	CDialog::OnShowWindow(bShow, nStatus);


	m_pMapEditorView->SetEditMode(EMT_OBJECT_NORMAL);

	m_pMapEditorView->SetBrushType(m_nBrushType);
}
void CSelectObjectDlg::OnOK()
{

}

void CSelectObjectDlg::OnPaint()
{
	CPaintDC dc(this); 

	DrawElement();

	CBrush brushGround(ReverseColor(m_pMapEditorView->GetAttachColor()) & 0x00FFFFFF);
	dc.FillRect(&m_ColorRect, &brushGround);

	ReleaseDC(&dc);

}

bool CSelectObjectDlg::DrawElement(void)
{
	checkRenderer();
	Nuclear::FRECT rect;
	m_pRenderer->Begin();
	if (m_nPictureId != 0)
	{
		LPImgInfo pImgInfo = m_ImgInfoMap[m_nPictureId];
		float fRatio = static_cast<float>(m_PictureRect.right - m_PictureRect.left) / pImgInfo->m_nFileWidth;
		std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
		std::vector<Nuclear::CRECT>::iterator rectEnd = pImgInfo->m_RectArray.end();
		std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
		for (;rectIter!=rectEnd;rectIter++,handleIter++)
		{
			rect.left = m_PictureRect.left + rectIter->left * fRatio;
			rect.top = m_PictureRect.top + rectIter->top * fRatio;
			rect.right = m_PictureRect.left + rectIter->right * fRatio;
			rect.bottom = m_PictureRect.top + rectIter->bottom * fRatio;
			m_pRenderer->DrawPicture(*handleIter, rect, 0xffffffff);
		}
	}
	m_pRenderer->End();
	return true;
}

void CSelectObjectDlg::ResizeRect(int width, int height)
{
	checkRenderer();
	m_nPictureWidth = width;
	m_nPictureHeight = height;

	m_DestRect.top = 5;
	m_DestRect.left = 5;
	m_DestRect.bottom = m_nPictureHeight + m_DestRect.top;
	m_DestRect.right = m_nPictureWidth + m_DestRect.left;
	Nuclear::CRECT sourceRect(0, 0, m_nPictureWidth, m_nPictureHeight);
	m_pRenderer->SetDestRect(&m_DestRect);
	m_pRenderer->SetSourceRect(&sourceRect);
	ResizePicture();
}

void CSelectObjectDlg::ResizePicture()
{
	if (m_nPictureId != 0)
	{
		LPImgInfo pImgInfo = m_ImgInfoMap[m_nPictureId];
		int pWidth = pImgInfo->m_nFileWidth;
		int pHeight = pImgInfo->m_nFileHeight;
		float scal = static_cast<float>(m_nPictureWidth) / pWidth;
		if (static_cast<float>(m_nPictureHeight) / pHeight < scal)
		{
			scal = static_cast<float>(m_nPictureHeight) / pHeight;
		}

		float width = pWidth * scal;
		float height = pHeight * scal;
		if (width < m_nPictureWidth)
		{
			m_PictureRect.left = (m_nPictureWidth - width) / 2;
			m_PictureRect.right = m_PictureRect.left + width;
		} else {
			m_PictureRect.left = 0;
			m_PictureRect.right = static_cast<float>(m_nPictureWidth);
		}
		if (height < m_nPictureHeight)
		{
			m_PictureRect.top = (m_nPictureHeight - height) / 2;
			m_PictureRect.bottom = m_PictureRect.top + height;
		} else {
			m_PictureRect.top = 0;
			m_PictureRect.bottom = static_cast<float>(m_nPictureHeight);
		}
	}
}

void CSelectObjectDlg::SelectSelection()
{
	m_Tree.SelectItem(m_ItemSelect);
}

void CSelectObjectDlg::OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);

	m_nBrushType = static_cast<unsigned int>(m_Tree.GetItemData(pNMTreeView->itemNew.hItem));	
	m_pMapEditorView->SetBrushType(m_nBrushType);
	ResetDisplay();
	*pResult = 0;
}

void CSelectObjectDlg::ResetDistortDisplay()
{
	const CString &filename = m_ObjectFilenameArr.at(m_nBrushType);
	m_nPictureId = 0;
	this->InvalidateRect(&m_DestRect, FALSE);
}

void CSelectObjectDlg::ResetLinkedObjectDisplay()
{
	const CString &filename = m_ObjectFilenameArr.at(m_nBrushType);
	m_nPictureId = 0;
	this->InvalidateRect(&m_DestRect, FALSE);
}

void CSelectObjectDlg::ResetEffectDisplay()
{
	const CString &filename = m_ObjectFilenameArr.at(m_nBrushType);	
	m_nPictureId = 0;
}

void CSelectObjectDlg::ResetElementDisplay()
{
	LPImgInfo pImgInfo;
	const CString &filename = m_ObjectFilenameArr.at(m_nBrushType);
	m_nPictureId = m_nBrushType;
	checkRenderer();
	if (m_ImgInfoMap.find(m_nBrushType) == m_ImgInfoMap.end())
	{
		CString name = filename + ImageSet_ExtName;
		Nuclear::XBuffer buffer;
		if (!theApp.GetFileIOManager()->GetFileImage(name.GetBuffer(), buffer))
		{
			name.ReleaseBuffer();
			return;
		}
		name.ReleaseBuffer();
		Nuclear::PictureHandle handle;
		Nuclear::PImg tmpimg;
		int col, row, i;
		CString nameFormat;
		nameFormat = filename + _T("_%d");
		tmpimg.LoadFromMem(buffer.constbegin(), (int)buffer.size());
		pImgInfo = new sImgInfo();
		*pImgInfo = tmpimg;
		i = 0;
		for (row=0;row<pImgInfo->m_nRow;row++)
		{
			for (col=0;col<pImgInfo->m_nCol;col++,i++)
			{
				name.Format(nameFormat, i);
				handle = m_pRenderer->LoadPicture(std::wstring(name));
				pImgInfo->m_HandleArray.push_back(handle);
			}
		}
		m_ImgInfoMap[m_nBrushType] = pImgInfo;
	} else {
		pImgInfo = m_ImgInfoMap[m_nBrushType];
	}
	ResizePicture();
	this->InvalidateRect(&m_DestRect, FALSE);
}

void CSelectObjectDlg::ResetDisplay()
{
	static void (CSelectObjectDlg::*pReset[PMap::MAP_OBJ_COUNT])() =
	{
		&CSelectObjectDlg::ResetElementDisplay, &CSelectObjectDlg::ResetEffectDisplay,&CSelectObjectDlg::ResetLinkedObjectDisplay, &CSelectObjectDlg::ResetDistortDisplay
	};
	if (m_nBrushType != 0)
	{
		for (int i=PMap::MAP_OBJ_COUNT-1;i>=0;--i)
		{
			if (m_nBrushType >= m_FirstObjectIds[i])
			{
				(this->*pReset[i])();
				break;
			}
		}
	} else {
		m_nPictureId = 0;
	}
	this->InvalidateRect(&m_DestRect, FALSE);
}

BOOL CSelectObjectDlg::Create(CMapEditorView* pMapEditorView/* = NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

bool CSelectObjectDlg::RenewDistortTrees()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	HTREEITEM fatherItem,nowItem;
	std::list<HTREEITEM> treeItemList;
	std::vector<std::pair<CString, CString>> fileNames;	
	std::vector<CString> folderNames;
	std::vector<std::pair<CString, CString>>::iterator fileIter;
	std::vector<CString>::iterator folderIter;
	wchar_t value[256];
	BOOL working;
	FileName=theApp.GetResPath() + _T(".\\map\\distortionobjects");
	FileList.AddTail(FileName);
	nowItem = m_Tree.InsertItem(L"飘动物件",0,0,NULL);
	m_Tree.SetItemData(nowItem, 0);
	treeItemList.push_back(nowItem);
	while (!FileList.IsEmpty())  
	{  
		FileName=FileList.GetHead()+_T("\\*.*");
		FileList.RemoveHead();
		fatherItem = treeItemList.front();
		treeItemList.pop_front();
		working = myFinder.FindFile(FileName);
		folderNames.clear();
		fileNames.clear();
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			tmpName = myFinder.GetFileName();
			if (tmpName.Right(_tcslen(_T(".dis"))).MakeLower() != _T(".dis") && !myFinder.IsDirectory())
				continue;
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(myFinder.GetFilePath());
				GetPrivateProfileString(L"NAME", L"name", L"", value, 255, myFinder.GetFilePath() + L"\\name.ini");
				if (value[0] > 0)
				{
					tmpName += L"(";
					tmpName += value;
					tmpName += L")";
				}
				folderNames.push_back(tmpName);
			} else {
				fileNames.push_back(std::make_pair(tmpName, myFinder.GetFilePath()));
			}
		}
		for (folderIter=folderNames.begin();folderIter!=folderNames.end();++folderIter)
		{
			nowItem = m_Tree.InsertItem(*folderIter, 0, 0, fatherItem);
			treeItemList.push_back(nowItem);
			m_Tree.SetItemData(nowItem, 0);
		}
		for (fileIter=fileNames.begin();fileIter!=fileNames.end();++fileIter)
		{
			nowItem = m_Tree.InsertItem(fileIter->first, 0, 0, fatherItem);
			const std::wstring oldfilename = fileIter->second;
			std::wstring newname;
			PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
			m_Tree.SetItemData(nowItem, m_ObjectFilenameArr.size());
			m_ObjectFilenameArr.push_back(newname.c_str());
		}
	}
	return true;
}

bool CSelectObjectDlg::RenewLinkedObjectTrees()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	HTREEITEM fatherItem,nowItem;
	std::list<HTREEITEM> treeItemList;
	std::vector<std::pair<CString, CString>> fileNames;	
	std::vector<CString> folderNames;
	std::vector<std::pair<CString, CString>>::iterator fileIter;
	std::vector<CString>::iterator folderIter;
	wchar_t value[256];
	BOOL working;
	FileName=theApp.GetResPath() + _T(".\\map\\linkedobjects");
	FileList.AddTail(FileName);
	nowItem = m_Tree.InsertItem(L"动态物件",0,0,NULL);
	m_Tree.SetItemData(nowItem, 0);
	treeItemList.push_back(nowItem);
	while (!FileList.IsEmpty())  
	{  
		FileName=FileList.GetHead()+_T("\\*.*");
		FileList.RemoveHead();
		fatherItem = treeItemList.front();
		treeItemList.pop_front();
		working = myFinder.FindFile(FileName);
		folderNames.clear();
		fileNames.clear();
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			tmpName = myFinder.GetFileName();
			if (tmpName.Right(_tcslen(_T(".lko"))).MakeLower() != _T(".lko") && !myFinder.IsDirectory())
				continue;
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(myFinder.GetFilePath());
				GetPrivateProfileString(L"NAME", L"name", L"", value, 255, myFinder.GetFilePath() + L"\\name.ini");
				if (value[0] > 0)
				{
					tmpName += L"(";
					tmpName += value;
					tmpName += L")";
				}
				folderNames.push_back(tmpName);
			} else {
				fileNames.push_back(std::make_pair(tmpName, myFinder.GetFilePath()));
			}
		}
		for (folderIter=folderNames.begin();folderIter!=folderNames.end();++folderIter)
		{
			nowItem = m_Tree.InsertItem(*folderIter, 0, 0, fatherItem);
			treeItemList.push_back(nowItem);
			m_Tree.SetItemData(nowItem, 0);
		}
		for (fileIter=fileNames.begin();fileIter!=fileNames.end();++fileIter)
		{
			nowItem = m_Tree.InsertItem(fileIter->first, 0, 0, fatherItem);
			const std::wstring oldfilename = fileIter->second;
			std::wstring newname;
			PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
			m_Tree.SetItemData(nowItem, m_ObjectFilenameArr.size());
			m_ObjectFilenameArr.push_back(newname.c_str());
		}
	}
	return true;
}

bool CSelectObjectDlg::RenewEffectTrees()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CString filePath;
	CStringList FileList;
	HTREEITEM fatherItem,nowItem;
	std::list<HTREEITEM> treeItemList;
	std::vector<std::pair<CString, CString>> fileNames;	
	std::vector<CString> folderNames;
	std::vector<std::pair<CString, CString>>::iterator fileIter;
	std::vector<CString>::iterator folderIter;
	wchar_t value[256];
	BOOL working;
	HTREEITEM effItem = m_Tree.InsertItem(L"特效",0,0,NULL);
	m_Tree.SetItemData(effItem, 0);

	int j = 0;
	for (j=0;j<4;j++)
	{
		int extLen = 0;

		switch (j)
		{
		case 0:
			FileName=theApp.GetResPath() + _T(".\\effect\\animation");
			nowItem = m_Tree.InsertItem(_T("序列帧动画"),0,0,effItem);
			extLen = wcslen(SFI_ExtName);
			break;
		case 1:
			FileName=theApp.GetResPath() + _T(".\\effect\\particle\\psl");
			nowItem = m_Tree.InsertItem(_T("粒子"),0,0,effItem);
			extLen = 1+ wcslen(FNExt_ParticleWS);
			break;
		case 2:
			FileName=theApp.GetResPath() + _T(".\\effect\\geffect");
			nowItem = m_Tree.InsertItem(_T("特效包"),0,0,effItem);
			extLen = wcslen(Effect_ExtName);
			break;
		case 3:
			FileName=theApp.GetResPath() + _T(".\\effect\\spine");
			nowItem = m_Tree.InsertItem(_T("骨骼动画"),0,0,effItem);
			extLen = wcslen(SPINE_ExtName);
			break;
		}
		m_Tree.SetItemData(nowItem, 0);

		FileList.AddTail(FileName);
		treeItemList.push_back(nowItem);
		while (!FileList.IsEmpty())  
		{  
			FileName=FileList.GetHead()+_T("\\*.*");
			FileList.RemoveHead();
			fatherItem = treeItemList.front();
			treeItemList.pop_front();
			working = myFinder.FindFile(FileName);
			fileNames.clear();
			folderNames.clear();
			while (working)
			{
				working = myFinder.FindNextFile();
				if (myFinder.IsDots())   
					continue;
				if (myFinder.IsHidden())
					continue;
				tmpName = myFinder.GetFileName();
				if (myFinder.IsDirectory()) 
				{
					FileList.AddTail(myFinder.GetFilePath());
					GetPrivateProfileString(L"NAME", L"name", L"", value, 255, myFinder.GetFilePath() + L"\\name.ini");
					if (value[0] > 0)
					{
						tmpName += L"(";
						tmpName += value;
						tmpName += L")";
					}
					folderNames.push_back(tmpName);
				} else {
					tmpName = tmpName.MakeLower();
					filePath = myFinder.GetFilePath();
					switch (j)
					{
					case 0:
						if (tmpName.Right(extLen) != SFI_ExtName)
							continue;
						tmpName.Delete(tmpName.GetLength()-extLen, extLen);
						filePath.Delete(filePath.GetLength()-extLen, extLen);
						break;
					case 1:
						{
							std::wstring wsTmp = FNExt_ParticleWS;
							if (tmpName.Right(extLen) != (_T(".") + wsTmp).c_str())
								continue;
							tmpName.Delete(tmpName.GetLength()-extLen, extLen);
							filePath.Delete(filePath.GetLength()-extLen, extLen);
							break;
						}
					case 2:
						if (tmpName.Right(extLen) != Effect_ExtName)
							continue;
						tmpName.Delete(tmpName.GetLength()-extLen, extLen);
						filePath.Delete(filePath.GetLength()-extLen, extLen);
						break;
					case 3:
						if (tmpName.Right(extLen) != SPINE_ExtName)
							continue;
						tmpName.Delete(tmpName.GetLength()-extLen, extLen);
						filePath.Delete(filePath.GetLength()-extLen, extLen);
						break;
					}
					filePath = filePath.MakeLower();
					fileNames.push_back(std::make_pair(tmpName, filePath));
				}
			}
			for (folderIter=folderNames.begin();folderIter!=folderNames.end();++folderIter)
			{
				nowItem = m_Tree.InsertItem(*folderIter, 0, 0, fatherItem);
				treeItemList.push_back(nowItem);
				m_Tree.SetItemData(nowItem, 0);
			}
			for (fileIter=fileNames.begin();fileIter!=fileNames.end();++fileIter)
			{
				nowItem = m_Tree.InsertItem(fileIter->first, 0, 0, fatherItem);
				const std::wstring oldfilename = fileIter->second;
				std::wstring newname;
				PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
				assert(newname[1] == 'e');
				assert(newname[2] == 'f');
				assert(newname[3] == 'f');
				newname.erase(0, 8);
				m_Tree.SetItemData(nowItem, m_ObjectFilenameArr.size());
				m_ObjectFilenameArr.push_back(newname.c_str());
			}
		}
	}
	return true;
}

bool CSelectObjectDlg::RenewElementTrees()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	HTREEITEM fatherItem,nowItem;
	std::list<HTREEITEM> treeItemList;
	BOOL working;
	wchar_t value[256];
	std::vector<std::pair<CString, CString>> fileNames;	
	std::vector<CString> folderNames;
	std::vector<std::pair<CString, CString>>::iterator fileIter;
	std::vector<CString>::iterator folderIter;
	FileName=theApp.GetResPath() + _T(".\\map\\elements");

	FileList.AddTail(FileName);

	nowItem = m_Tree.InsertItem(L"物件",0,0,NULL);
	m_Tree.SetItemData(nowItem, 0);
	treeItemList.push_back(nowItem);

	while (!FileList.IsEmpty())  
	{  
		FileName=FileList.GetHead()+_T("\\*.*");
		FileList.RemoveHead();
		fatherItem = treeItemList.front();
		treeItemList.pop_front();
		working = myFinder.FindFile(FileName);
		fileNames.clear();
		folderNames.clear();
		while (working)
		{
			working = myFinder.FindNextFile();
			if (myFinder.IsDots())   
				continue;
			if (myFinder.IsHidden())
				continue;
			tmpName = myFinder.GetFileName();
			int extLen = wcslen(ImageSet_ExtName);
			if (tmpName.Right(extLen).MakeLower() != ImageSet_ExtName && !myFinder.IsDirectory())
				continue;
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(myFinder.GetFilePath());				
				GetPrivateProfileString(L"NAME", L"name", L"", value, 255, myFinder.GetFilePath() + L"\\name.ini");
				if (value[0] > 0)
				{
					tmpName += L"(";
					tmpName += value;
					tmpName += L")";
				}
				folderNames.push_back(tmpName);
			} else {
				fileNames.push_back(std::make_pair(tmpName, myFinder.GetFilePath()));
			}
		}
		for (folderIter=folderNames.begin();folderIter!=folderNames.end();++folderIter)
		{
			nowItem = m_Tree.InsertItem(*folderIter, 0, 0, fatherItem);
			treeItemList.push_back(nowItem);
			m_Tree.SetItemData(nowItem, 0);
		}
		for (fileIter=fileNames.begin();fileIter!=fileNames.end();++fileIter)
		{
			nowItem = m_Tree.InsertItem(fileIter->first, 0, 0, fatherItem);
			const std::wstring oldfilename = fileIter->second;
			std::wstring newname;
			PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
			newname.erase(newname.size()-4, 4);
			m_Tree.SetItemData(nowItem, m_ObjectFilenameArr.size());
			m_ObjectFilenameArr.push_back(newname.c_str());
		}  
	}
	return true;
}


bool CSelectObjectDlg::RenewTrees(void)
{
	HTREEITEM item = m_Tree.InsertItem(L"选择",0,0,NULL);
	m_ItemSelect = item;
	m_Tree.SetItemData(item, 0);
	bool result, tmpResult;
	m_ObjectFilenameArr.clear();
	m_ObjectFilenameArr.push_back(L"");
	m_FirstObjectIds[0] = static_cast<unsigned int>(m_ObjectFilenameArr.size());
	result = RenewElementTrees();
	m_FirstObjectIds[1] = static_cast<unsigned int>(m_ObjectFilenameArr.size());
	tmpResult = RenewEffectTrees();
	result = result && tmpResult;
	m_FirstObjectIds[2] = static_cast<unsigned int>(m_ObjectFilenameArr.size());
	tmpResult = RenewLinkedObjectTrees();
	result = result && tmpResult;
	m_FirstObjectIds[3] = static_cast<unsigned int>(m_ObjectFilenameArr.size());
	tmpResult = RenewDistortTrees();

	return result && tmpResult;
}

BOOL CSelectObjectDlg::OnInitDialog()
{
	CDialog::OnInitDialog();


	m_nPathLength = theApp.GetResPath().GetLength();
	SetWindowText(_T("地图物件列表"));
	m_Tree.BringWindowToTop();

	RenewTrees();

	checkRenderer();
	CRect rect;
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();

	RenewSize();
	return TRUE;  

}

BEGIN_MESSAGE_MAP(CSelectObjectDlg, CDialog)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE1, &CSelectObjectDlg::OnTvnSelchangedTree1)
	ON_WM_PAINT()
	ON_WM_SHOWWINDOW()
	ON_WM_KEYDOWN()
	ON_WM_KEYUP()
	ON_WM_SIZING()
	ON_WM_SIZE()
	ON_BN_CLICKED(IDC_SELECT_BACKGROUND_COLOR, &CSelectObjectDlg::OnBnClickedSelectBackgroundColor)
	ON_WM_TIMER()
END_MESSAGE_MAP()




void CSelectObjectDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TREE1, m_Tree);
}


CSelectObjectDlg::~CSelectObjectDlg()
{
	ImgInfoMap::iterator iter = m_ImgInfoMap.begin();
	for (;iter!=m_ImgInfoMap.end();iter++)
	{
		delete(iter->second);
	}
	DestroyRenderer(m_pRenderer);
}

CSelectObjectDlg::CSelectObjectDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectObjectDlg::IDD, pParent), m_pMapEditorView(NULL), m_nBrushType(0), m_pRenderer(NULL), m_nPictureId(0), 
m_nPictureHeight(102), m_nPictureWidth(134), m_Color(0xFFFFFFFF), m_bSizing(false)
{
	m_ObjectFilenameArr.reserve(100);

}

