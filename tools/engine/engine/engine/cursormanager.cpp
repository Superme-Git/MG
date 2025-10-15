#include "stdafx.h"
#include "..\common\fileiomanager.h"
#include "engine.h"
#include "cursormanager.h"

namespace Nuclear
{
	CursorManager::CursorManager(Engine *pEngine)
		: m_pEngine(pEngine), m_curCursor(NULL), m_hOldCursor(NULL)
	{
	}

	CursorManager::~CursorManager()
	{
	}

	XPCursorHandle CursorManager::RegisterCursor(const std::wstring& resource)
	{
		return ::LoadCursorFromFile(resource.c_str());
	}

	CursorHandle CursorManager::RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center)
	{
		Renderer* pRenderer = m_pEngine->GetRenderer();
		XBuffer srcdata;
		if( !m_pEngine->GetFileIOManager()->GetFileImage(resource, srcdata) )
			return INVALID_CURSOR_HANDLE;

		Nuclear::PictureInfo picInfo;
		PictureHandle hPic = pRenderer->LoadPictureFromMem(srcdata.constbegin(), (int)srcdata.size(), &picInfo, XPTEXFMT_A8R8G8B8);
		if( hPic == INVALID_PICTURE_HANDLE || picInfo.m_texfmt != XPTEXFMT_A8R8G8B8 )
			return INVALID_CURSOR_HANDLE;
		Nuclear::PictureInfo picInfoNew;
		PictureHandle hNewPic = pRenderer->NewPicture(32,32, &picInfoNew, picInfo.m_texfmt);
		if( hNewPic == INVALID_PICTURE_HANDLE)
		{
			pRenderer->FreePicture(hPic);
			return INVALID_CURSOR_HANDLE;
		}
		if( picInfo.m_texfmt != picInfoNew.m_texfmt )
		{
			pRenderer->FreePicture(hPic);
			pRenderer->FreePicture(hNewPic);
			return INVALID_CURSOR_HANDLE;
		}
		
		CRECT rect(offset, 32, 32);
		BYTE data[32 * 32 * 4];
		if( pRenderer->GetPictureData(hPic, data, sizeof(data), &rect) &&
			pRenderer->SetPictureData(hNewPic, data, sizeof(data)) )
		{
			m_mapCursor[hNewPic] = center;
		}
		else
		{
			pRenderer->FreePicture(hNewPic);
			hNewPic = INVALID_CURSOR_HANDLE;
		}

		pRenderer->FreePicture(hPic);
		return hNewPic;
	}

	bool CursorManager::ChangeCursor(CursorHandle hCursor)
	{
		std::map<CursorHandle, CPOINT>::const_iterator it = m_mapCursor.find(hCursor);
		if( it == m_mapCursor.end() ) return false;
		if (m_pEngine->GetRenderer()->SetCursorPicture(hCursor, it->second.x, it->second.y) == S_OK)
		{
			ShowCursor(true);
			return true;
		}
		return false;
	}

	bool CursorManager::ChangeCursor(XPCursorHandle hCursor)
	{
		ShowCursor(false);
		::SetCursor(hCursor);
		m_curCursor = hCursor;
		return true;
	}

	bool CursorManager::ShowCursor(bool b)
	{
		if( !m_pEngine->GetRenderer()->ShowCursor(b) ) 
			return false;
		m_bUseHardCursor = b;
		return true;
	}

	bool CursorManager::SetCursorPosition(const CPOINT &point)
	{
		return m_pEngine->GetRenderer()->SetCursorPosition(point.x, point.y) == S_OK;
	}

	void CursorManager::GetCursorPosition(CPOINT &point) const
	{
		::GetCursorPos( &point);
		::ScreenToClient( m_pEngine->GetWindow(), &point);
		m_pEngine->WindowToClient( point );
	}

	void CursorManager::OnSetCursor()
	{
		if( m_bUseHardCursor)
		{
			if( m_hOldCursor == NULL )
				m_hOldCursor = ::SetCursor(NULL);
			m_pEngine->GetRenderer()->ShowCursor(true);
		}
		else
		{
			if( m_hOldCursor != NULL )
			{
				::SetCursor(m_hOldCursor);
			}
			m_hOldCursor = NULL;
			m_pEngine->GetRenderer()->ShowCursor(false);
			//ÐÂCursor
			if (m_curCursor != NULL)
			{
				::SetCursor(m_curCursor);
			}
		}
	}
};