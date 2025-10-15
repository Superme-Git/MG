

#include "stdafx.h"
#include "MapEditor.h"
#include "..\engine\common\util.h"
#include "..\engine\common\xml.h"
#include "..\engine\common\log.h"
#include "..\engine\map\pdistortionobject.h"
#include "..\engine\effect\geffect.h"
#include "..\engine\effect\effectmanager.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\engine\configmanager.h"
#include "..\engine\engine\animanager.h"
#include "..\engine\engine\systemresourcemanager.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\world\picresrc.h"
#include "AllObjectResourceDlg.h"
#define XPLOG_RESINFO xwprint<sResInfo>
struct sResInfo 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"mapEditorResource.log"; } 
};

DWORD sResInfo::s_nProcessId = 0;



IMPLEMENT_DYNAMIC(CAllObjectResourceDlg, CDialog)

const static wchar_t* OBJ_TYPE_NAMES[Nuclear::PMap::MAP_OBJ_COUNT] = {L"物件", L"特效", L"动态物件", L"飘动物件"};

typedef struct _SortParam{
	CAllObjectResourceDlg::sSortInfo sortInfo;
	CAllObjectResourceDlg::ObjInfoVector* pVector;
} SortParam;

int CALLBACK SortFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort)
{
	SortParam *pParam = reinterpret_cast<SortParam*>(lParamSort);
	CAllObjectResourceDlg::sObjInfo& info1 = (*pParam->pVector)[lParam1];
	CAllObjectResourceDlg::sObjInfo& info2 = (*pParam->pVector)[lParam2];
	bool result = true;
	switch (pParam->sortInfo.m_Id)
	{
	case 0:
		result = info1.m_Uri > info2.m_Uri;
		break;
	case 1:
		result = info1.m_Type > info2.m_Type;
		break;
	case 2:
		result = info1.m_DirectRefCount > info2.m_DirectRefCount;
		break;
	case 3:
		result = info1.m_IndirectRefCount > info2.m_IndirectRefCount;
		break;
	case 4:
		result = info1.m_IndirectRefCount + info1.m_DirectRefCount > info2.m_IndirectRefCount + info2.m_DirectRefCount;
		break;
	case 5:
		result = info1.m_Size > info2.m_Size;
		break;
	case 6:
		result = info1.m_DirectMapNames > info2.m_DirectMapNames;
		break;
	case 7:
		result = info1.m_IndirectFilenames > info2.m_IndirectFilenames;
		break;
	default:
		break;
	}
	if (pParam->sortInfo.m_IsUp)
		return !result;
	return result;
}

int CALLBACK SortFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort);

void CAllObjectResourceDlg::OnHdnItemclickEleResList(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);
	SortParam param;
	if (phdr->iItem == m_SortInfo.m_Id)
	{
		m_SortInfo.m_IsUp=m_SortInfo.m_IsUp==0?1:0;
	} else {
		m_SortInfo.m_Id = phdr->iItem;
		m_SortInfo.m_IsUp = 0;
	}
	param.sortInfo = m_SortInfo;
	param.pVector = &m_ObjInfos;
	m_EleResList.SortItems(SortFunc, reinterpret_cast<DWORD_PTR>(&param));
	*pResult = 0;
}

void CAllObjectResourceDlg::OnTimer(UINT_PTR nIDEvent)
{
	
	if (nIDEvent == 1000)
	{
		Tick();
		InvalidateRect(&m_PreviewRect, FALSE);
	}

	__super::OnTimer(nIDEvent);
}

void CAllObjectResourceDlg::OnNMClickEleResList(NMHDR *pNMHDR, LRESULT *pResult)
{
	FreeAllResources();
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	sObjInfo &info = m_ObjInfos[m_EleResList.GetItemData(pNMLV->iItem)];
	if (!info.m_bDeleted)
	{
		bool ok = false;
		switch(info.m_Type)
		{
		case Nuclear::PMap::MAP_OBJ_ELEMENT:
			m_pImgInfo = CheckPictureLoad(info.m_Uri);
			if (m_pImgInfo)
			{
				m_nObjectWidth = m_pImgInfo->m_nFileWidth;
				m_nObjectHeight = m_pImgInfo->m_nFileHeight;
				ok = true;
			}
			break;
		case Nuclear::PMap::MAP_OBJ_EFFECT:
			m_pEffect = m_pEffectMgr->CreateEffect(info.m_Uri, true);
			if (m_pEffect)
			{
				m_pEffect->AssureResource(false);
				m_pEffect->Play();
				m_nObjectWidth = m_pEffect->GetRelBouningBox().Width();
				m_nObjectHeight = m_pEffect->GetRelBouningBox().Height();
				ok = true;
			}
			break;
		case Nuclear::PMap::MAP_OBJ_LINKEDOBJ:
			CheckLinkedObjectLoad(info.m_Uri);
			if (m_pLinkedObject)
			{
				m_nObjectWidth = m_pLinkedObject->m_rctMaxBox.Width();
				m_nObjectHeight = m_pLinkedObject->m_rctMaxBox.Height();
				ok = true;
			}
			break;
		case Nuclear::PMap::MAP_OBJ_DISTORT:
			CheckDistortObjectLoad(info.m_Uri);
			if (m_pDistort)
			{
				ok = true;
			}
			break;
		}
		if (!ok)
		{
			m_nObjectWidth = 0;
			m_nObjectHeight = 0;
		}
		if (m_nObjectWidth == 0 || m_nObjectHeight == 0)
		{
			m_nObjectWidth = m_PreviewRect.Width();
			m_nObjectHeight = m_PreviewRect.Height();
		}
		ResetPosition();
	}
	*pResult = 0;
}

void CAllObjectResourceDlg::ResetPosition()
{
	if (m_pDistort)
	{
		m_pDistort->setPos(Nuclear::CRECT(0, 0, m_nObjectWidth, m_nObjectHeight));
	}
	if (m_pEffect)
	{
		Nuclear::CRECT rect = m_pEffect->GetRelBouningBox();
		if (rect.left == rect.right || rect.top == rect.bottom)
		{
			rect.left = -m_PreviewRect.Width() / 2;
			rect.top = -m_PreviewRect.Height() / 2;
		}
		m_pEffect->SetLocation(-rect.left, -rect.top);
	}
	CheckRenderer();
	Nuclear::CRECT sorceRect(0, 0, m_nObjectWidth, m_nObjectHeight);
	int rH = m_PreviewRect.Height();
	int rW = m_PreviewRect.Width();
	if (m_nObjectWidth * rH > m_nObjectHeight * rW)
	{
		sorceRect.bottom = m_nObjectWidth * rH / rW;
	} else {
		sorceRect.right = rW * m_nObjectHeight / rH;
	}
	if (sorceRect.bottom < rH || sorceRect.right < rW)
	{
		sorceRect.bottom = rH;
		sorceRect.right = rW;
	}
	m_pRenderer->SetSourceRect(&sorceRect);
}

void CAllObjectResourceDlg::DrawEffect()
{
	if (!m_pEffect)
		return;
	m_pEffect->Render(NULL);
}

void CAllObjectResourceDlg::DrawElement()
{
	if (!m_pImgInfo)
		return;
	DrawElement(m_pImgInfo, 0.0f, 0.0f);
}

void CAllObjectResourceDlg::DrawDistort()
{
	if (!m_pDistort)
		return;
	m_pDistort->Render(NULL);
}

void CAllObjectResourceDlg::DrawLinkedObject()
{
	if (!m_pLinkedObject)
		return;
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator iter = m_pLinkedObject->m_vectorLinkedObjectInfo.begin() + m_pLinkedObject->GetMainNodeID();
	std::wstring imgFilename;
	float mainCenterX, mainCenterY;
	mainCenterX = static_cast<float>(iter->m_nWidth) / 2 - m_pLinkedObject->m_rctMaxBox.left - m_pLinkedObject->m_ptCentPos.x;
	mainCenterY = static_cast<float>(iter->m_nHeight) / 2 - m_pLinkedObject->m_rctMaxBox.top - m_pLinkedObject->m_ptCentPos.y;
	iter = m_pLinkedObject->m_vectorLinkedObjectInfo.begin();
	for (;iter!=m_pLinkedObject->m_vectorLinkedObjectInfo.end();iter++)
	{
		imgFilename = iter->m_strPicName;
		imgFilename.erase(imgFilename.size()-4, 4);
		LPImgInfo pImgInfo = CheckPictureLoad(imgFilename);
		if (!pImgInfo)
			continue;
		if (iter->m_bMainNode)
		{
			DrawElement(pImgInfo, mainCenterX - (iter->m_nWidth / 2),
				mainCenterY - (iter->m_nHeight / 2));
		} else {
			DrawElement(pImgInfo, mainCenterX + (iter->m_rotateCent_2.x - iter->m_rotateCent.x - iter->m_nWidth / 2),
				mainCenterY + (iter->m_rotateCent_2.y - iter->m_rotateCent.y - iter->m_nHeight / 2));
		}
	}
}

void CAllObjectResourceDlg::Tick()
{
	if (m_pDistort)
	{
		m_pDistort->Update(0.02f);
	}
	if (m_pEffect)
	{
		m_pEffect->Update(20);
	}
}

void CAllObjectResourceDlg::DrawElement(LPImgInfo pImgInfo, float x, float y)
{
	Nuclear::FRECT rect;
	rect.left = x;
	rect.top = y;
	rect.right = x + pImgInfo->m_nFileWidth;
	rect.bottom = y + pImgInfo->m_nFileHeight;

	std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
	std::vector<Nuclear::CRECT>::iterator rectEnd = pImgInfo->m_RectArray.end();
	std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
	Nuclear::FRECT tmpRect;
	for (;rectIter!=rectEnd;rectIter++,handleIter++)
	{
		tmpRect.left = rect.left + rectIter->left;
		tmpRect.top = rect.top + rectIter->top;
		tmpRect.right = rect.left + rectIter->right;
		tmpRect.bottom = rect.top + rectIter->bottom;
		m_pRenderer->DrawPicture(*handleIter, tmpRect, 0xFFFFFFFF);
	}
}

void CAllObjectResourceDlg::CheckDistortObjectLoad(const std::wstring& name)
{
	if (!m_pDistort)
	{
		Nuclear::PDistortionObject obj;
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(name, buffer);
		if (buffer.size() > 0)
		{
			obj.LoadFromMem(buffer.constbegin(), (int)buffer.size());
			Nuclear::DistortionEffectParam effectParam;
			effectParam.nType = obj.m_distortionObj.nType;
			effectParam.fCycNum = obj.m_distortionObj.fCycNum;
			effectParam.fAmplitudes = obj.m_distortionObj.fAmplitudes;
			effectParam.fSpeed = obj.m_distortionObj.fSpeed;
			effectParam.fPos = 0.0f;
			m_pDistort = new Nuclear::DistortBase(this, effectParam);
			std::wstring imgFilename = obj.m_distortionObj.strImgFileName;
			std::wstring imgLightFileName = obj.m_distortionObj.strLightFile;
			imgFilename.erase(imgFilename.size()-4, 4);
			LPImgInfo pLightImgInfo = NULL;
			if (obj.m_bLightFlag)
			{
				imgLightFileName.erase(imgLightFileName.size()-4, 4);
				pLightImgInfo = CheckPictureLoad(imgLightFileName);
			}
			LPImgInfo pImgInfo = CheckPictureLoad(imgFilename);
			if (!pImgInfo)
				return;
			if (pLightImgInfo)
			{
				if ((pLightImgInfo->m_nFileWidth != pImgInfo->m_nFileWidth) ||
					(pLightImgInfo->m_nFileHeight != pImgInfo->m_nFileHeight))
				{
					std::wstring err = L"飘动物件\n" + name + L"\n的灯光节点和主节点大小不一致";
					AfxMessageBox(err.c_str());
					delete pLightImgInfo;
					pLightImgInfo = NULL;
				}
					
			}
			m_nObjectWidth = pImgInfo->m_nFileWidth + 50;
			m_nObjectHeight = pImgInfo->m_nFileHeight + 50;
			std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
			std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
			std::vector<Nuclear::PictureHandle>::iterator handleIE = pImgInfo->m_HandleArray.end();

			std::vector<Nuclear::PictureHandle>::iterator lightHandleIter;
			if (pLightImgInfo)
			{
				lightHandleIter = pLightImgInfo->m_HandleArray.begin();
			}
			

			Nuclear::PicResrc* pPic, *pLightPic;
			for (;handleIter!=handleIE;++handleIter,++rectIter)
			{
				if (m_PicHandleToResMap.find(*handleIter) == m_PicHandleToResMap.end())
				{
					pPic = new Nuclear::PicResrc();
					m_PicHandleToResMap[*handleIter] = pPic;
					pPic->handle = *handleIter;
				} else {
					pPic = m_PicHandleToResMap[*handleIter];
				}
				m_pDistort->PushAnPic(pPic, *rectIter);
				if (pLightImgInfo)
				{
					if (m_PicHandleToResMap.find(*lightHandleIter) == m_PicHandleToResMap.end())
					{
						pLightPic = new Nuclear::PicResrc();
						m_PicHandleToResMap[*lightHandleIter] = pLightPic;
						pLightPic->handle = *lightHandleIter;
					} else {
						pLightPic = m_PicHandleToResMap[*lightHandleIter];
					}
					m_pDistort->PushLightPic(pLightPic, *rectIter);
					++lightHandleIter;
				}
				
				
			
			}
		} else {
			CString error = _T("飘动物件加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_RESINFO(error);
		}
	}

}

void CAllObjectResourceDlg::CheckLinkedObjectLoad(const std::wstring& name)
{
	if (!m_pLinkedObject)
	{
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(name, buffer);
		if (buffer.size() > 0)
		{
			m_pLinkedObject = new Nuclear::PLinkedObject();
			m_pLinkedObject->LoadFromMem(buffer.constbegin(), (int)buffer.size());
		} else {
			CString error = _T("动态物件加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_RESINFO(error);
		}
	}
}

LPImgInfo CAllObjectResourceDlg::CheckPictureLoad(const std::wstring& name)
{
	LPImgInfo pImgInfo = NULL;
	if (m_ImgageResources.find(name) == m_ImgageResources.end())
	{
		CString fileName;
		fileName = name.data();
		fileName = fileName + ImageSet_ExtName;
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(fileName.GetBuffer(), buffer);
		fileName.ReleaseBuffer();
		if (buffer.size() > 0)
		{
			Nuclear::PictureHandle handle;
			Nuclear::PImg tmpimg;
			int col, row, i;
			CString nameFormat;
			nameFormat = name.data();
			nameFormat = nameFormat + _T("_%d");
			tmpimg.LoadFromMem(buffer.constbegin(), (int)buffer.size());
			pImgInfo = new sImgInfo();
			*pImgInfo = tmpimg;
			i = 0;
			for (row=0;row<pImgInfo->m_nRow;row++)
			{
				for (col=0;col<pImgInfo->m_nCol;col++,i++)
				{
					fileName.Format(nameFormat, i);
					handle = m_pRenderer->LoadPicture(std::wstring(fileName));
					pImgInfo->m_HandleArray.push_back(handle);
				}
			}
		} else {
			CString error = _T("图片加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_RESINFO(error);
		}
		m_ImgageResources[name] = pImgInfo;
		return pImgInfo;
	}
	return m_ImgageResources[name];
}

void CAllObjectResourceDlg::OnPaint()
{
	CPaintDC dc(this); 
	CheckRenderer();
	m_pEffectMgr->OnTick();
	m_pRenderer->Begin();
	DrawElement();
	DrawEffect();
	DrawLinkedObject();
	DrawDistort();
	m_pRenderer->End();
}

void CAllObjectResourceDlg::OnSizing(UINT fwSide, LPRECT pRect)
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

	m_bSizing = true;
	RenewSize();
}

void CAllObjectResourceDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	if (m_bSizing)
	{
		RenewSize();
		m_bSizing = false;
	}
}

void CAllObjectResourceDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	rectCtrl.DeflateRect(0, 10);
	m_PreviewRect.top = rectCtrl.top;
	m_PreviewRect.right = rectCtrl.right - 10;
	m_PreviewRect.bottom = rectCtrl.bottom;
	m_PreviewRect.left = m_PreviewRect.right - m_PreviewRect.Height();
	if (m_PreviewRect.left < (rectCtrl.right * 4 / 7))
		m_PreviewRect.left = (rectCtrl.right * 4 / 7);
	rectCtrl.right = m_PreviewRect.left - 10;
	rectCtrl.left += 10;
	rectCtrl.bottom -= 30;
	m_EleResList.MoveWindow(&rectCtrl);
	rectCtrl.top = rectCtrl.bottom + 10;
	rectCtrl.bottom = rectCtrl.top + 20;
	rectCtrl.left = rectCtrl.right - 70;
	GetDlgItem(IDC_BUTTON_CLEAN)->MoveWindow(&rectCtrl, FALSE);
	rectCtrl.MoveToX(rectCtrl.left - 80);
	GetDlgItem(IDC_BUTTON_COUNT_REF)->MoveWindow(&rectCtrl, FALSE);
	rectCtrl.MoveToX(rectCtrl.left - 80);
	GetDlgItem(IDC_REFRESH)->MoveWindow(&rectCtrl, FALSE);
	rectCtrl.MoveToX(rectCtrl.left - 80);
	GetDlgItem(IDC_BUTTON1)->MoveWindow(&rectCtrl, FALSE);
	rectCtrl.MoveToX(rectCtrl.left - 80);
	GetDlgItem(IDC_BUTTON2)->MoveWindow(&rectCtrl, FALSE);

	CheckRenderer();
	/*m_nPictureWidth = width;
	m_nPictureHeight = height;*/
	Nuclear::CRECT sourceRect(0, 0, m_PreviewRect.Width(), m_PreviewRect.Height());
	m_pRenderer->SetDestRect(&m_PreviewRect);
	if (m_nObjectWidth == 0 || m_nObjectHeight == 0)
	{
		m_nObjectWidth = m_PreviewRect.Width();
		m_nObjectHeight = m_PreviewRect.Height();
	}
	ResetPosition();
	rectCtrl.left = 0;
	rectCtrl.top = 0;
	rectCtrl.right = m_PreviewRect.right + 10;
	rectCtrl.bottom = m_PreviewRect.top;
	InvalidateRect(&rectCtrl);
	rectCtrl.top = m_PreviewRect.bottom;
	rectCtrl.bottom = rectCtrl.top + 10;
	InvalidateRect(&rectCtrl);
	rectCtrl.top = 0;
	rectCtrl.left = m_PreviewRect.right;
	InvalidateRect(&rectCtrl);
	rectCtrl.top = 0;
	rectCtrl.left = m_PreviewRect.left-10;
	rectCtrl.right = m_PreviewRect.left;
	InvalidateRect(&rectCtrl);
	rectCtrl.top = m_PreviewRect.bottom - 30;
	rectCtrl.left = 0;
	rectCtrl.right = m_PreviewRect.left;
	rectCtrl.bottom = rectCtrl.top + 30;
	InvalidateRect(&rectCtrl);
}

void CAllObjectResourceDlg::OnBnClickedButton2()
{
	CFileDialog dlg(false, L".csv", L"output.csv", OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, L"CSV (逗号分割) (*.csv)|*.csv||");
	if (dlg.DoModal() == IDOK)
	{
		setlocale(LC_CTYPE, "chinese_china");;
		FILE *f = Nuclear::FOpen(dlg.GetPathName(), L"w");
		if (f)
		{
			fwprintf(f, L"%s", L"资源路径,物件类型,直接引用数,间接引用数,引用总数,大小(KB),直接引用地图,间接引用文件\n");
			ObjInfoVector::iterator iter, iterEnd;
			iter = m_ObjInfos.begin();
			iterEnd = m_ObjInfos.end();
			for (;iter!=iterEnd;++iter)
			{
				if (!iter->m_bDeleted)
				{
					fwprintf(f, L"\"%s\",%s,%d,%d,%d,%0.2f,\"%s\",\"%s\"\n", iter->m_Uri.c_str(), OBJ_TYPE_NAMES[iter->m_Type], iter->m_DirectRefCount, iter->m_IndirectRefCount, iter->m_DirectRefCount+iter->m_IndirectRefCount, iter->m_Size, iter->m_DirectMapNames.c_str(), iter->m_IndirectFilenames.c_str());
				}
			}
			fclose(f);
		} else {
			AfxMessageBox(L"打开文件失败");
		}
		
	}
	
}

void CAllObjectResourceDlg::OnBnClickedButtonClean()
{
	
	static float (CAllObjectResourceDlg::*pGetFileSize[Nuclear::PMap::MAP_OBJ_COUNT])(const std::wstring& name) =
	{
		&CAllObjectResourceDlg::GetImageSize, &CAllObjectResourceDlg::GetEffectSize, &CAllObjectResourceDlg::GetOtherFileSize, &CAllObjectResourceDlg::GetOtherFileSize
	};
	ObjInfoVector::iterator iter, iterEnd;
	iter = m_ObjInfos.begin();
	iterEnd = m_ObjInfos.end();
	for (;iter!=iterEnd;++iter)
	{
		if (!iter->m_bDeleted)
		{
			iter->m_Size = ((this->*pGetFileSize[iter->m_Type])(iter->m_Uri));
		}
	}
	ShowSizes();
}

float CAllObjectResourceDlg::GetOtherFileSize(const std::wstring& name)
{
	float result = 0.0f;
	PFS::CFile file;
	if (file.Open(name, PFS::FM_EXCL, PFS::FA_RDONLY ))
	{
		result += file.GetSize();
		file.Close();
	} else {
		CString error = _T("文件加载错误：");
		error = error + name.data();
		error = error + _T("\n");
		XPLOG_RESINFO(error);
	}
	return result / 1024;
}

float CAllObjectResourceDlg::GetEffectSize(const std::wstring& name)
{
	float result = 0.0f;
	PFS::CFile file;
	std::wstring tmpname;
	if( 0 == name.find(L"ani") )
	{
		
		tmpname = L"/effect/" + name + SFI_ExtName;
	}
	else if( 0 == name.find(L"par") )
	{
		
		tmpname = L"/effect/" + name + L"." + FNExt_ParticleWS;
	}
	else if ( 0 == name.find(L"geffect") )
	{
		
		tmpname = L"/effect/" + name + Effect_ExtName;
	}
	if (file.Open(tmpname, PFS::FM_EXCL, PFS::FA_RDONLY ))
	{
		result += file.GetSize();
		file.Close();
	} else {
		CString error = _T("文件加载错误：");
		error = error + name.data();
		error = error + _T("\n");
		XPLOG_RESINFO(error);
	}
	return result / 1024;
}

float CAllObjectResourceDlg::GetImageSize(const std::wstring& name)
{
	float result = 0.0f;
	PFS::CFile file;
	std::wstring filename = name + ImageSet_ExtName;
	wchar_t fnbuf[MAX_PATH];
	if (file.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY ))
	{
		int index = 0;
		result += file.GetSize();
		file.Close();
		Nuclear::PImg img;
		img.Load(filename.c_str());
		for(std::vector<Nuclear::CRECT>::const_iterator it2 = img.m_vectorRct.begin(), ie2 = img.m_vectorRct.end(); it2 != ie2; ++it2, ++index)
		{
			wsprintf(fnbuf,L"_%d",index);
			filename = name + fnbuf;
			if (!file.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY ))
			{
				CString error = _T("图片分片加载错误：");
				error = error + filename.data();
				error = error + _T("\n");
				XPLOG_RESINFO(error);
				continue;
			}
			result += file.GetSize();
			file.Close();
		}
	} else {
		CString error = _T("图片加载错误：");
		error = error + filename.data();
		error = error + _T("\n");
		XPLOG_RESINFO(error);
	}
	return result / 1024;
}

void CAllObjectResourceDlg::DeleteDistort(std::wstring& name)
{
	std::wstring navname;
	PFS::CEnv::PFSFileNameToNativeFileName( name, navname);
	DeleteFile(navname.c_str());
}

void CAllObjectResourceDlg::DeleteLinkedObject(std::wstring& name)
{
	std::wstring navname;
	PFS::CEnv::PFSFileNameToNativeFileName( name, navname);
	DeleteFile(navname.c_str());
}

void CAllObjectResourceDlg::DeleteEffect(std::wstring& name)
{
	std::wstring pfsname = L"/effect/" + name;
	if( 0 == name.find(L"ani") )
	{
		pfsname += SFI_ExtName;
	}
	else if( 0 == name.find(L"par") )
	{
		pfsname += L".";
		pfsname += FNExt_ParticleWS;
	}
	else if ( 0 == name.find(L"geffect") )
	{
		pfsname += Effect_ExtName;
	}
	std::wstring navname;
	PFS::CEnv::PFSFileNameToNativeFileName( pfsname, navname);
	DeleteFile(navname.c_str());
}

void CAllObjectResourceDlg::DeleteElement(std::wstring& name)
{
	CString fileName;
	fileName = name.data();
	fileName = fileName + ImageSet_ExtName;
	Nuclear::XBuffer buffer;
	{
		Nuclear::XBuffer tmpBuffer;
		theApp.GetFileIOManager()->GetFileImage(fileName.GetBuffer(), tmpBuffer);
		buffer.resize(tmpBuffer.size());
		memcpy_s(buffer.normalbegin(), buffer.size(), tmpBuffer.constbegin(), tmpBuffer.size());
	}
	fileName.ReleaseBuffer();
	if (buffer.size() > 0)
	{
		Nuclear::PImg tmpimg;
		int col, row, i;
		CString nameFormat;
		nameFormat = name.data();
		nameFormat = nameFormat + _T("_%d");
		tmpimg.LoadFromMem(buffer.constbegin(), (int)buffer.size());
		std::wstring pfsname = fileName;
		std::wstring navname;
		PFS::CEnv::PFSFileNameToNativeFileName( pfsname, navname);
		DeleteFile(navname.c_str());
		i = 0;
		for (row=0;row<tmpimg.m_nRow;row++)
		{
			for (col=0;col<tmpimg.m_nCol;col++,i++)
			{
				fileName.Format(nameFormat, i);
				pfsname = fileName;
				PFS::CEnv::PFSFileNameToNativeFileName( pfsname, navname);
				DeleteFile(navname.c_str());
			}
		}
	}
}

void CAllObjectResourceDlg::DeleteObjectFile(sObjInfo& objInfo)
{
	static void (CAllObjectResourceDlg::*pDelFile[Nuclear::PMap::MAP_OBJ_COUNT])(std::wstring& name) =
	{
		&CAllObjectResourceDlg::DeleteElement, &CAllObjectResourceDlg::DeleteEffect, &CAllObjectResourceDlg::DeleteLinkedObject, &CAllObjectResourceDlg::DeleteDistort
	};
	(this->*pDelFile[objInfo.m_Type])(objInfo.m_Uri);
}

void CAllObjectResourceDlg::OnBnClickedButton1()	
{
	if (!m_bHasRefCount)
	{
		AfxMessageBox(L"请先计算引用！\n");
		return;
	}
	POSITION pos = m_EleResList.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox(L"请先选中1项物件");
		return;
	}
	Nuclear::PMap* pPMap = NULL;
	Nuclear::SubMap::ObjLayerVector::iterator layerIter;
	Nuclear::SubMap::ObjInfoVector::iterator objInfoIter, objInfoEnd;
	std::set<int> setitems;
	while (pos)
	{
		int nItem = m_EleResList.GetNextSelectedItem(pos);
		size_t i = (size_t)m_EleResList.GetItemData(nItem);
		sObjInfo &info = m_ObjInfos[i];
		bool ok = true;
		if (info.m_IndirectRefCount > 0)
		{
			if (AfxMessageBox(L"这个物件的间接引用不为0，可能会影响别的物件，无论如何都要继续么？\n", MB_YESNO) == IDNO)
			{
				ok = false;
			}
		}
		if (ok)
		{
			info.m_bDeleted = true;
			MapKeySet::iterator it = info.m_MapKeys.begin(), ie = info.m_MapKeys.end();
			for (;it!=ie;++it)
			{
				bool hasDel = false;
				sMapInfo &mapInfo = m_MapInfos[it->m_MapID];
				pPMap = mapInfo.m_pMap;
				pPMap->m_objs[info.m_Type].m_mapObjNames.erase(it->m_MapKey);
				Nuclear::SubMap::ObjLayerVector &layerVector = pPMap->m_objs[info.m_Type].m_vectorLayers;
				layerIter = layerVector.begin();
				for (;layerIter!=layerVector.end();++layerIter)
				{
					objInfoEnd = layerIter->vectorObjs.end();
					for (objInfoIter=layerIter->vectorObjs.begin();objInfoIter!=objInfoEnd;++objInfoIter)
					{
						if (objInfoIter->m_bDeleted)
							continue;
						if (objInfoIter->m_MapKey == it->m_MapKey)
						{
							hasDel = true;
							objInfoIter->m_bDeleted = true;
							--(layerIter->m_ObjectCount);
						}
					}
				}
				if (hasDel)
					pPMap->SaveToNativePath(mapInfo.m_Uri.c_str());
			}
			std::vector<unsigned int>::iterator indIt, indIe;
			indIt = m_IndirectRefMap[i].begin();
			indIe = m_IndirectRefMap[i].end();
			for (;indIt!=indIe;++indIt)
			{
				m_ObjInfos[*indIt].m_IndirectRefCount -= info.m_DirectRefCount;
			}
			setitems.insert(nItem);
			DeleteObjectFile(info);
		}
		
	}
	std::set<int>::reverse_iterator setIt = setitems.rbegin(), setIe = setitems.rend();
	if (setIt != setIe)
	{
		for (;setIt!=setIe;++setIt)
		{
			m_EleResList.DeleteItem(*setIt);
		}
		ShowCounts();
	}
}

void CAllObjectResourceDlg::OnBnClickedRefresh()
{
	ResetLists();
	ResetMaps();
}

void CAllObjectResourceDlg::OnBnClickedButtonCountRef()
{
	sMapKeys tmpMapKey;
	Nuclear::SubMap::ObjLayerVector::iterator layerIter;
	Nuclear::SubMap::ObjInfoVector::iterator objInfoIter, objInfoEnd;
	Nuclear::SubMap::ObjNameMap::iterator nameIter;
	ObjectNameToInfoMap::iterator infoIter, infoEnd;
	std::vector<unsigned int>::iterator refVetIter, refVetIterEnd;
	MapInfoArray::iterator it, ie;
	tmpMapKey.m_MapID = 0;
	it = m_MapInfos.begin();
	ie = m_MapInfos.end();
	int objType;
	infoEnd = m_ObjInfoMap.end();
	for (;it!=ie;++it,++tmpMapKey.m_MapID)
	{
		sMapInfo &tmpMapInfo = *it;
		Nuclear::SubMap::ObjNameMap &nameMap = tmpMapInfo.m_pMap->m_objs[Nuclear::PMap::MAP_OBJ_EFFECT].m_mapObjNames;
		const Nuclear::SubMap::TriggerInfoMap& triggerInfos = tmpMapInfo.m_pMap->GetTriggerInfos();
		Nuclear::SubMap::TriggerInfoMap::const_iterator triggerInfoIter = triggerInfos.begin();
		for (;triggerInfoIter!=triggerInfos.end();++triggerInfoIter)
		{
			const Nuclear::SubMap::TriggerInfoVet &triggerInfoVet = triggerInfoIter->second.triggerInfos;
			Nuclear::SubMap::TriggerInfoVet::const_iterator vetIter = triggerInfoVet.begin();
			for (;vetIter!=triggerInfoVet.end();++vetIter)
			{
				nameIter = nameMap.find(vetIter->effectMapKey);
				if (nameIter != nameMap.end())
				{
					infoIter = m_ObjInfoMap.find(nameIter->second);
					if (infoIter != infoEnd)
					{
						sObjInfo &info = m_ObjInfos[infoIter->second];
						tmpMapKey.m_MapKey = vetIter->effectMapKey;
						info.m_MapKeys.insert(tmpMapKey);
						++info.m_DirectRefCount;
						refVetIter = m_IndirectRefMap[infoIter->second].begin();
						refVetIterEnd = m_IndirectRefMap[infoIter->second].end();
						for (;refVetIter!=refVetIterEnd;++refVetIter)
						{
							++m_ObjInfos[*refVetIter].m_IndirectRefCount;	
						}

					} else {
						CString errorStr;
						errorStr.Format(L"找不到 %s 地图里面 使用的 %s 文件\n", tmpMapInfo.m_Uri.c_str(), nameIter->second.c_str());
						if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
						{
							m_ErrorStringSet.insert(errorStr);
							XPLOG_RESINFO(errorStr.GetBuffer());
							errorStr.ReleaseBuffer();
						}
					}
				} else {
					CString errorStr;
					errorStr.Format(L"找不到 %s 地图里面 ID 为 %d 的特效\n", tmpMapInfo.m_Uri.c_str(), vetIter->effectMapKey);
					if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
					{
						m_ErrorStringSet.insert(errorStr);
						XPLOG_RESINFO(errorStr.GetBuffer());
						errorStr.ReleaseBuffer();
					}
				}
			}
		}
		for (objType=0;objType<Nuclear::PMap::MAP_OBJ_COUNT;++objType)
		{
			Nuclear::SubMap::ObjNameMap &nameMap = tmpMapInfo.m_pMap->m_objs[objType].m_mapObjNames;
			Nuclear::SubMap::ObjLayerVector &layerVector = tmpMapInfo.m_pMap->m_objs[objType].m_vectorLayers;
			layerIter = layerVector.begin();
			for (;layerIter!=layerVector.end();++layerIter)
			{
				objInfoEnd = layerIter->vectorObjs.end();
				for (objInfoIter=layerIter->vectorObjs.begin();objInfoIter!=objInfoEnd;++objInfoIter)
				{
					if (objInfoIter->m_bDeleted)
						continue;
					nameIter = nameMap.find(objInfoIter->m_MapKey);
					if (nameIter != nameMap.end())
					{
						infoIter = m_ObjInfoMap.find(nameIter->second);
						if (infoIter != infoEnd)
						{
							sObjInfo &info = m_ObjInfos[infoIter->second];
							tmpMapKey.m_MapKey = objInfoIter->m_MapKey;
							info.m_MapKeys.insert(tmpMapKey);
							++info.m_DirectRefCount;
							refVetIter = m_IndirectRefMap[infoIter->second].begin();
							refVetIterEnd = m_IndirectRefMap[infoIter->second].end();
							for (;refVetIter!=refVetIterEnd;++refVetIter)
							{
								++m_ObjInfos[*refVetIter].m_IndirectRefCount;	
							}

						} else {
							CString errorStr;
							errorStr.Format(L"找不到 %s 地图里面 使用的 %s 文件\n", tmpMapInfo.m_Uri.c_str(), nameIter->second.c_str());
							if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
							{
								m_ErrorStringSet.insert(errorStr);
								XPLOG_RESINFO(errorStr.GetBuffer());
								errorStr.ReleaseBuffer();
							}
						}
					} else {
						CString errorStr;
						errorStr.Format(L"找不到 %s 地图里面 ID 为 %d 的 %s\n", tmpMapInfo.m_Uri.c_str(), objInfoIter->m_MapKey, OBJ_TYPE_NAMES[objType]);
						if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
						{
							m_ErrorStringSet.insert(errorStr);
							XPLOG_RESINFO(errorStr.GetBuffer());
							errorStr.ReleaseBuffer();
						}
					}
				}
			}
		}
		
		{
			Nuclear::SubMap::ObjNameMap &nameMap = tmpMapInfo.m_pMap->m_objs[Nuclear::PMap::MAP_OBJ_ELEMENT].m_mapObjNames;
			Nuclear::SubMap::PBackGoundInfoVector::iterator bgIt = tmpMapInfo.m_pMap->m_backGround.m_backGroundInfos.begin(), 
				bgIe = tmpMapInfo.m_pMap->m_backGround.m_backGroundInfos.end();
			for (;bgIt!=bgIe;++bgIt)
			{
				int count = bgIt->width * bgIt->height;
				Nuclear::SubMap::ObjMapKey *pKeys = bgIt->pPics;
				for (int i=0;i<count;++i,++pKeys)
				{
					nameIter = nameMap.find(*pKeys);
					if (nameIter != nameMap.end())
					{
						infoIter = m_ObjInfoMap.find(nameIter->second);
						if (infoIter != infoEnd)
						{
							sObjInfo &info = m_ObjInfos[infoIter->second];
							tmpMapKey.m_MapKey = *pKeys;
							info.m_MapKeys.insert(tmpMapKey);
							++info.m_DirectRefCount;
							refVetIter = m_IndirectRefMap[infoIter->second].begin();
							refVetIterEnd = m_IndirectRefMap[infoIter->second].end();
							for (;refVetIter!=refVetIterEnd;++refVetIter)
							{
								++m_ObjInfos[*refVetIter].m_IndirectRefCount;	
							}
						} else {
							CString errorStr;
							errorStr.Format(L"找不到 %s 地图里面 使用的 %s 文件\n", tmpMapInfo.m_Uri.c_str(), nameIter->second.c_str());
							if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
							{
								m_ErrorStringSet.insert(errorStr);
								XPLOG_RESINFO(errorStr.GetBuffer());
								errorStr.ReleaseBuffer();
							}
						}
					} else {
						CString errorStr;
						errorStr.Format(L"找不到 %s 地图里面 ID 为 %d 的物件\n", tmpMapInfo.m_Uri.c_str(), *pKeys);
						if (m_ErrorStringSet.find(errorStr) == m_ErrorStringSet.end())
						{
							m_ErrorStringSet.insert(errorStr);
							XPLOG_RESINFO(errorStr.GetBuffer());
							errorStr.ReleaseBuffer();
						}
					}
				}
			}
		}
		
	}
	ObjInfoVector::iterator infoIt = m_ObjInfos.begin(), infoIe = m_ObjInfos.end();
	MapKeySet::iterator mapKeyIt, mapKeyIe;
	for(;infoIt!=infoIe;++infoIt)
	{
		sObjInfo &info = *infoIt;
		if (info.m_MapKeys.size() > 0)
		{
			mapKeyIt = info.m_MapKeys.begin(), mapKeyIe = info.m_MapKeys.end();
			info.m_DirectMapNames = m_MapInfos[mapKeyIt->m_MapID].m_Names;
			++mapKeyIt;
			for (;mapKeyIt!=mapKeyIe;++mapKeyIt)
			{
				info.m_DirectMapNames += L", " + m_MapInfos[mapKeyIt->m_MapID].m_Names;
			}
		}
	}
	ShowCounts();
	m_bHasRefCount = true;
}

BOOL CAllObjectResourceDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	SetWindowText(L"硬盘物件资源表");
	m_EleResList.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_EleResList.InsertColumn(0, L"资源路径", LVCFMT_LEFT, 250);
	m_EleResList.InsertColumn(1, L"物件类型", LVCFMT_LEFT, 70);
	m_EleResList.InsertColumn(2, L"直接引用数", LVCFMT_LEFT, 80);
	m_EleResList.InsertColumn(3, L"间接引用数", LVCFMT_LEFT, 80);
	m_EleResList.InsertColumn(4, L"引用总数", LVCFMT_LEFT, 80);
	m_EleResList.InsertColumn(5, L"大小(KB)", LVCFMT_LEFT, 80);
	m_EleResList.InsertColumn(6, L"直接引用地图", LVCFMT_LEFT, 100);
	m_EleResList.InsertColumn(7, L"间接引用文件", LVCFMT_LEFT, 100);
	ResetLists();
	ResetMaps();
	SetCurrentDirectory(theApp.GetResPath());
	CRect rect;
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();
	RenewSize();
	SetTimer(1000, 20, NULL);
	GetDlgItem(IDC_BUTTON_CLEAN)->SetWindowText(L"计算大小");
	GetDlgItem(IDC_BUTTON2)->SetWindowText(L"导出到Excel");
	return TRUE;  
	
}

void CAllObjectResourceDlg::ResetMaps()
{
	ReleaseMaps();
	CFileFind myFinder;
	CString FileName;
	CStringList FileList;
	BOOL working;
	sMapInfo tmpMapInfo;
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
				tmpMapInfo.m_Uri = FileName.GetBuffer();
				PFS::CEnv::NativeFileNameToPFSFileName(tmpMapInfo.m_Uri, tmpMapInfo.m_Names);
				FileName.ReleaseBuffer();
				m_MapInfos.push_back(tmpMapInfo);

			}
		}
	}
}

void CAllObjectResourceDlg::ReleaseMaps()
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

void CAllObjectResourceDlg::AddDistortObjects()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	BOOL working;
	Nuclear::PDistortionObject pdistObj;
	ObjectNameToInfoMap::iterator mapIter;
	unsigned int id;
	FileName=theApp.GetResPath() + _T(".\\map\\distortionobjects");
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
			tmpName = myFinder.GetFilePath();
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(tmpName);
			} else {
				if (tmpName.Right(wcslen(L".dis")).MakeLower() != L".dis")
					continue;
				const std::wstring oldfilename = tmpName;
				std::wstring newname;
				PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
				AddItem(newname, Nuclear::PMap::MAP_OBJ_DISTORT);
				if (pdistObj.LoadFromNativePath(tmpName))
				{
					id = m_ObjInfoMap[newname];
					pdistObj.m_distortionObj.strImgFileName.erase(pdistObj.m_distortionObj.strImgFileName.size()-4, 4);		
					std::transform(pdistObj.m_distortionObj.strImgFileName.begin(), pdistObj.m_distortionObj.strImgFileName.end(), pdistObj.m_distortionObj.strImgFileName.begin(), ::towlower);
					mapIter = m_ObjInfoMap.find(pdistObj.m_distortionObj.strImgFileName);
					if (mapIter == m_ObjInfoMap.end())
					{
						XPLOG_RESINFO(L"找不到飘动物件 %s 中的img文件 %s\n", newname.c_str(), pdistObj.m_distortionObj.strImgFileName.c_str());
					} else {
						m_IndirectRefMap[id].push_back(mapIter->second);
						AddRefMap(newname, mapIter->second);
					}
				} else {
					XPLOG_RESINFO(L"加载 %s 文件失败\n", newname.c_str());
				}
			}
		}
	}
}

void CAllObjectResourceDlg::AddLinkedObjects()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	BOOL working;
	FileName=theApp.GetResPath() + _T(".\\map\\linkedobjects");
	FileList.AddTail(FileName);
	Nuclear::PLinkedObject lko;
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator iter, iterEnd;
	ObjectNameToInfoMap::iterator mapIter;
	unsigned int id;
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
			tmpName = myFinder.GetFilePath();
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(tmpName);
			} else {
				if (tmpName.Right(_tcslen(_T(".lko"))).MakeLower() != _T(".lko"))
					continue;
				const std::wstring oldfilename = tmpName;
				std::wstring newname;
				PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
				AddItem(newname, Nuclear::PMap::MAP_OBJ_LINKEDOBJ);
				
				if (lko.LoadFromNativePath(tmpName))
				{
					id = m_ObjInfoMap[newname];
					iter = lko.m_vectorLinkedObjectInfo.begin();
					iterEnd = lko.m_vectorLinkedObjectInfo.end();
					for (;iter!=iterEnd;++iter)
					{
						iter->m_strPicName.erase(iter->m_strPicName.size()-4, 4);
						std::transform(iter->m_strPicName.begin(), iter->m_strPicName.end(), iter->m_strPicName.begin(), ::towlower);
						mapIter = m_ObjInfoMap.find(iter->m_strPicName);
						if (mapIter == m_ObjInfoMap.end())
						{
							XPLOG_RESINFO(L"找不到动态物件 %s 中的img文件 %s\n", newname.c_str(), iter->m_strPicName.c_str());
						} else {
							m_IndirectRefMap[id].push_back(mapIter->second);
							AddRefMap(newname, mapIter->second);
						}
					}
				} else {
					XPLOG_RESINFO(L"加载 %s 文件失败\n", newname.c_str());
				}
				
			}
		}
	}

}

void CAllObjectResourceDlg::AddEffects()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	BOOL working;
	int j = 0;
	
	std::map<std::wstring, std::map<std::wstring, int>> refMap;
	
	std::map<std::wstring, std::set<std::wstring>> notifyMap;
	
	for (j=0;j<3;j++)
	{
		int extLen = 0;
		switch (j)
		{
		case 0:
			FileName=theApp.GetResPath() + _T(".\\effect\\animation");
			extLen = wcslen(SFI_ExtName);
			break;
		case 1:
			FileName=theApp.GetResPath() + _T(".\\effect\\particle\\psl");
			extLen = 1 + wcslen(FNExt_ParticleWS);
			break;
		case 2:
			FileName=theApp.GetResPath() + _T(".\\effect\\geffect");
			extLen = wcslen(Effect_ExtName);
			break;
		}
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
				tmpName = myFinder.GetFilePath();
				if (myFinder.IsDirectory()) 
				{
					FileList.AddTail(tmpName);
				} else {
					tmpName = tmpName.MakeLower();
					switch (j)
					{
					case 0:
						if (tmpName.Right(extLen) != SFI_ExtName)
							continue;
						tmpName.Delete(tmpName.GetLength()-extLen, extLen);
						break;
					case 1:
						{
							std::wstring wsTmp = FNExt_ParticleWS;
							if (tmpName.Right(extLen) != (_T(".") + wsTmp).c_str())
								continue;
							tmpName.Delete(tmpName.GetLength()-extLen, extLen);
							break;
						}
					case 2:
						if (tmpName.Right(extLen) != Effect_ExtName)
							continue;
						tmpName.Delete(tmpName.GetLength()-extLen, extLen);
						break;
					}
					const std::wstring oldfilename = tmpName;
					std::wstring newname;
					PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
					assert(newname[1] == 'e');
					assert(newname[2] == 'f');
					assert(newname[3] == 'f');
					newname.erase(0,8);
					AddItem(newname, Nuclear::PMap::MAP_OBJ_EFFECT);
					if (j == 2)
					{
						CalEffectRefCount(newname, refMap, notifyMap);
					}
				}
			}
		}
	}
}

bool CAllObjectResourceDlg::CalEffectRefCount(const std::wstring& name, std::map<std::wstring, std::map<std::wstring, int>>&refMap, std::map<std::wstring, std::set<std::wstring>>& notifyMap)
{
	std::wstring uri = L"/effect/" + name + Effect_ExtName;
	Nuclear::XBuffer xmlfiledata;
	if( !GetFileIOManager()->GetFileImage(uri, xmlfiledata))
	{
		XPLOG_RESINFO(L"找不到特效包 %s \n", name.c_str());
		return false;
	}
	XMLIO::CFileReader fr;
	int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
	if( XMLIO::EC_SUCCESS != nError)
	{
		fr.CloseFile();
		XPLOG_RESINFO(L"打开特效包 %s 错误\n", name.c_str());
		return false;
	}

	XMLIO::CINode root;
	if (!fr.GetRootNode(root))
	{
		fr.CloseFile();
		XPLOG_RESINFO(L"解析XML特效包 %s 错误\n", name.c_str());
		return false;
	}
	
	XMLIO::CNodeList clipsNodeList;
	root.GetChildren( clipsNodeList);
	ObjectNameToInfoMap::iterator mapInfoIter;
	std::map<std::wstring, int> &refList = refMap[name];
	unsigned int thisID = m_ObjInfoMap[name];
	std::vector<unsigned int>& vetIndirects = m_IndirectRefMap[thisID];
	
	for ( XMLIO::CNodeList::iterator i = clipsNodeList.begin(), e = clipsNodeList.end(); i != e; ++i)
	{
		XMLIO::CINode& node = *i;
		if (node.GetType() != XMLIO::NT_ELEMENT)
			continue;

		XPASSERT (node.GetName() == L"clip");	
		std::wstring strRes;
		if (! node.GetAttribute(L"r_f", strRes) )
		{
			XPLOG_RESINFO(L"特效包 %s 有个clip没有res_file字段 \n", name.c_str());
			continue;
		}

		if (strRes[0] == 'g')
		{
			
			
			std::map<std::wstring, std::map<std::wstring, int>>::iterator refIter = refMap.find(strRes);
			if (refIter == refMap.end()) 
			{
				notifyMap[strRes].insert(name);
				refList[strRes] += 1;
			} else {
				ObjectNameToInfoMap::iterator iter = m_ObjInfoMap.find(strRes);
				XPASSERT(iter != m_ObjInfoMap.end());
				vetIndirects.push_back(iter->second);
				AddRefMap(name, iter->second);
				std::vector<unsigned int>& otherIndirects = m_IndirectRefMap[iter->second];
				std::vector<unsigned int>::iterator oIt = otherIndirects.begin(), oIe = otherIndirects.end();
				vetIndirects.insert(vetIndirects.end(), oIt, oIe);
				for (;oIt!=oIe;++oIt)				
				{
					AddRefMap(name, *oIt);
				}

				
				std::map<std::wstring, int>::iterator it = refIter->second.begin(), ie = refIter->second.end();
				for (;it!=ie;++it)
				{
					refList[it->first] += it->second;
					notifyMap[it->first].insert(name);
				}
			}
		} else { 
			XPASSERT(strRes[0] != 'G');
			ObjectNameToInfoMap::iterator iter = m_ObjInfoMap.find(strRes);
			if (iter == m_ObjInfoMap.end())
			{
				XPLOG_RESINFO(L"特效包 %s 引用了不存在的特效 %s \n", name.c_str(), strRes.c_str());
				continue;
			}
			vetIndirects.push_back(iter->second);
			AddRefMap(name, iter->second);
		}
	}
	fr.CloseFile();

	std::set<std::wstring>::iterator notiIT = notifyMap[name].begin(), notiIE = notifyMap[name].end();
	for (;notiIT != notiIE; ++notiIT)
	{
		
		std::map<std::wstring, int> &ref = refMap[*notiIT];
		std::map<std::wstring, int>::iterator refIter = ref.find(name);
		XPASSERT(refIter != ref.end());
		int count = refIter->second;
		
		std::vector<unsigned int> &indirects = m_IndirectRefMap[m_ObjInfoMap[*notiIT]];
		std::vector<unsigned int>::iterator vIt = vetIndirects.begin(), vIe = vetIndirects.end();
		for (int i=0;i<count;++i)
		{
			indirects.insert(indirects.end(), vIt, vIe);
			indirects.push_back(thisID);
		}
		for (;vIt!=vIe;++vIt)				
		{
			AddRefMap(*notiIT, *vIt);
		}
		AddRefMap(*notiIT, thisID);
		
		std::map<std::wstring, int>::iterator tmpRefIT = refList.begin(), tmpRefIE = refList.end();
		for (;tmpRefIT!=tmpRefIE;++tmpRefIT)
		{
			ref[tmpRefIT->first] += tmpRefIT->second * count;
		}
		ref.erase(refIter);
	}
	notifyMap.erase(name);

	return true;
	
}

void CAllObjectResourceDlg::AddRefMap(const std::wstring &name, unsigned int id)
{
	sObjInfo& objInfo = m_ObjInfos[id];
	if (objInfo.m_IndirectFileSet.find(name) == objInfo.m_IndirectFileSet.end())
	{
		if (objInfo.m_IndirectFilenames.size() > 0)
		{
			objInfo.m_IndirectFilenames += L", ";
		}
		objInfo.m_IndirectFilenames += name;
		objInfo.m_IndirectFileSet.insert(name);
	}
}

void CAllObjectResourceDlg::AddElements()
{
	CFileFind myFinder;
	CString FileName;
	CString tmpName;
	CStringList FileList;
	BOOL working;	
	FileName=theApp.GetResPath() + _T(".\\map\\elements");
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
			tmpName = myFinder.GetFilePath();
			if (myFinder.IsDirectory()) 
			{
				FileList.AddTail(tmpName);
			} else {
				int extLen = wcslen(ImageSet_ExtName);
				if (tmpName.Right(extLen).MakeLower() != ImageSet_ExtName)
					continue;
				const std::wstring oldfilename = tmpName;
				std::wstring newname;
				PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
				newname.erase(newname.size()-extLen, extLen);
				AddItem(newname, Nuclear::PMap::MAP_OBJ_ELEMENT);
			}
		}
	}
}

void CAllObjectResourceDlg::ResetLists()
{
	m_bHasRefCount = false;
	m_EleResList.DeleteAllItems();
	m_ObjInfos.clear();
	m_ObjInfoMap.clear();
	AddElements();
	AddEffects();
	AddLinkedObjects();
	AddDistortObjects();
}

void CAllObjectResourceDlg::AddItem(const std::wstring& name, Nuclear::PMap::MAP_OBJ_TYPE type)
{
	LV_ITEM lvItem;
	ZeroMemory(&lvItem, sizeof(LV_ITEM));
	sObjInfo tmpinfo;
	unsigned int i = static_cast<unsigned int>(m_ObjInfos.size());
	m_ObjInfoMap[name] = i;
	m_ObjInfos.push_back(tmpinfo);
	sObjInfo &info = m_ObjInfos[i];
	info.m_Type = type;
	info.m_DirectRefCount = 0;
	info.m_IndirectRefCount = 0;
	info.m_Uri = name;
	m_EleResList.InsertItem((int)i, name.data());
	m_EleResList.SetItemText((int)i, 1, OBJ_TYPE_NAMES[type]);
	m_EleResList.SetItemData((int)i, i);	
	lvItem.iItem = (int)i;
	lvItem.mask = LVIF_TEXT;
	lvItem.pszText = L"未知";
	lvItem.iSubItem = 2;
	m_EleResList.SetItem(&lvItem);
	lvItem.iSubItem = 3;
	m_EleResList.SetItem(&lvItem);
	lvItem.iSubItem = 4;
	m_EleResList.SetItem(&lvItem);
	lvItem.iSubItem = 5;
	m_EleResList.SetItem(&lvItem);
	lvItem.iSubItem = 6;
	m_EleResList.SetItem(&lvItem);
	lvItem.iSubItem = 7;
	m_EleResList.SetItem(&lvItem);
}

void CAllObjectResourceDlg::ShowSizes()
{
	CString tmp;
	int itemCount = m_EleResList.GetItemCount();
	int i;
	LV_ITEM lvItem;
	for (i=0;i<itemCount;i++)
	{
		sObjInfo &info = m_ObjInfos[m_EleResList.GetItemData(i)];
		ZeroMemory(&lvItem, sizeof(LV_ITEM));
		lvItem.mask = LVIF_TEXT;
		lvItem.iItem = i;
		lvItem.iSubItem = 5;
		tmp.Format(L"%0.2f", info.m_Size);
		lvItem.pszText = tmp.GetBuffer();
		m_EleResList.SetItem(&lvItem);
		tmp.ReleaseBuffer();
	}
}

void CAllObjectResourceDlg::ShowCounts()
{
	int itemCount = m_EleResList.GetItemCount();
	int i;
	wchar_t strCount[10];
	LV_ITEM lvItem;
	for (i=0;i<itemCount;i++)
	{
		sObjInfo &info = m_ObjInfos[m_EleResList.GetItemData(i)];
		ZeroMemory(&lvItem, sizeof(LV_ITEM));
		ZeroMemory(strCount, 10 * sizeof(wchar_t));
		lvItem.mask = LVIF_TEXT;
		lvItem.pszText = strCount;
		lvItem.iItem = i;
		_itow_s(info.m_DirectRefCount, strCount, 10, 10);
		lvItem.iSubItem = 2;
		m_EleResList.SetItem(&lvItem);
		_itow_s(info.m_IndirectRefCount, strCount, 10, 10);
		lvItem.iSubItem = 3;
		m_EleResList.SetItem(&lvItem);
		_itow_s(info.m_DirectRefCount + info.m_IndirectRefCount, strCount, 10, 10);
		lvItem.iSubItem = 4;
		m_EleResList.SetItem(&lvItem);
		lvItem.iSubItem = 6;
		CString tmpMapStr = info.m_DirectMapNames.c_str();
		lvItem.pszText = tmpMapStr.GetBuffer();
		m_EleResList.SetItem(&lvItem);
		tmpMapStr.ReleaseBuffer();
		lvItem.iSubItem = 7;
		CString tmpFileStr = info.m_IndirectFilenames.c_str();
		lvItem.pszText = tmpFileStr.GetBuffer();
		m_EleResList.SetItem(&lvItem);
		tmpFileStr.ReleaseBuffer();
	}
}

bool CAllObjectResourceDlg::CheckRenderer()
{
	if( m_pRenderer == NULL ) 
	{
		Nuclear::CRECT sourceRect(0, 0, m_PreviewRect.Width(), m_PreviewRect.Height());
		CreateRenderer(&m_pRenderer, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), theApp.GetFileIOManager());
		m_pRenderer->SetSourceRect(&sourceRect);
		m_pRenderer->SetDestRect(&m_PreviewRect);
		m_pSystemResourceMgr = new Nuclear::SystemResourceManager(this);
		m_pSystemResourceMgr->Init();
		return true;
	}
	return false;
}

void CAllObjectResourceDlg::FreeAllResources()
{
	m_pImgInfo = NULL;
	if (m_pEffect)
		delete m_pEffect;
	m_pEffect = NULL;
	if (m_pLinkedObject)
		delete m_pLinkedObject;
	m_pLinkedObject = NULL;
	if (m_pDistort)
		delete m_pDistort;
	m_pDistort = NULL;
	ImageResourceMap::iterator iter = m_ImgageResources.begin();
	std::vector<Nuclear::PictureHandle>::iterator picIter;
	for (;iter!=m_ImgageResources.end();++iter)
	{
		if (!iter->second)
			continue;
		for (picIter = iter->second->m_HandleArray.begin();picIter != iter->second->m_HandleArray.end();++picIter)
		{
			m_pRenderer->FreePicture(*picIter);
		}
		delete iter->second;
	}
	m_ImgageResources.clear();
	std::map<Nuclear::PictureHandle, Nuclear::PicResrc*>::iterator resIter, resEnd;
	resIter = m_PicHandleToResMap.begin();
	resEnd = m_PicHandleToResMap.end();
	for (;resIter!=resEnd;++resIter)
	{
		delete resIter->second;
	}
	m_PicHandleToResMap.clear();
}

BEGIN_MESSAGE_MAP(CAllObjectResourceDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_COUNT_REF, &CAllObjectResourceDlg::OnBnClickedButtonCountRef)
	ON_BN_CLICKED(IDC_REFRESH, &CAllObjectResourceDlg::OnBnClickedRefresh)
	ON_BN_CLICKED(IDC_BUTTON1, &CAllObjectResourceDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON_CLEAN, &CAllObjectResourceDlg::OnBnClickedButtonClean)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_PAINT()
	ON_NOTIFY(NM_CLICK, IDC_ELE_RES_LIST, &CAllObjectResourceDlg::OnNMClickEleResList)
	ON_WM_TIMER()
	ON_NOTIFY(HDN_ITEMCLICK, 0, &CAllObjectResourceDlg::OnHdnItemclickEleResList)
	ON_BN_CLICKED(IDC_BUTTON2, &CAllObjectResourceDlg::OnBnClickedButton2)
END_MESSAGE_MAP()

void CAllObjectResourceDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_ELE_RES_LIST, m_EleResList);
}

CAllObjectResourceDlg::~CAllObjectResourceDlg()
{
	FreeAllResources();
	ReleaseMaps();
	if (m_pSystemResourceMgr)
		delete m_pSystemResourceMgr;
	delete m_pConfigMgr;
	delete m_pEffectMgr;
	delete m_pSpriteMgr;
	delete m_pAniMgr;
	DestroyRenderer(m_pRenderer);
}

CAllObjectResourceDlg::CAllObjectResourceDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAllObjectResourceDlg::IDD, pParent), m_bHasRefCount(false), m_bSizing(false), m_pImgInfo(NULL), m_pEffect(NULL),
	m_pLinkedObject(NULL), m_pDistort(NULL), m_pRenderer(NULL), m_pSystemResourceMgr(NULL), m_pConfigMgr(NULL), m_pEffectMgr(NULL), 
	m_pSpriteMgr(NULL), m_pAniMgr(NULL), m_nObjectWidth(0), m_nObjectHeight(0)
{
	m_SortInfo.m_Id = -1;
	m_SortInfo.m_IsUp = 0;
	m_pConfigMgr = new Nuclear::ConfigManager(this);
	m_pConfigMgr->Init();
	m_pEffectMgr = new Nuclear::EffectManager(this);
	m_pSpriteMgr = new Nuclear::SpriteManager(this);
	sResInfo::s_nProcessId = GetCurrentProcessId();

	if (!m_pSpriteMgr->PreInit())
	{
		AfxMessageBox(L"精灵管理器初始化整体失败");
		return;
	}
	size_t SprCount = m_pSpriteMgr->GetInitCount();
	bool succ = true;
	for (size_t i=0;i<SprCount;++i)
	{
		if( m_pSpriteMgr->Init(i, 1) )
		{	
			XPLOG_ERROR(L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
			succ = false;
		}
	}
	if (!succ)
	{
		AfxMessageBox(L"精灵管理器初始化部分失败，请查看日志并且联系美术～");
	}
	if (!m_pSpriteMgr->PostInit())
	{
		AfxMessageBox(L"精灵管理器初始化失败");
		return;
	}

	m_pAniMgr = new Nuclear::AniManager(this);
	if( !m_pAniMgr->Init() ) 
	{
		
		XPLOG_INFO(L"Init Faild.\n");
		return;
	}
}

