#include "StdAfx.h"
#include "FilesMetaFileViewer.h"
#include "../../filesmetafile.h"
#include "../../stringhelper.h"

CFilesMetaFileViewer::CFilesMetaFileViewer(void)
{
	m_bValidMetaFile = true;
}

CFilesMetaFileViewer::~CFilesMetaFileViewer(void)
{
}

BEGIN_MESSAGE_MAP( CFilesMetaFileViewer, CListItemsFileViewer )
	ON_NOTIFY( LVN_GETDISPINFOW, AFX_IDW_PANE_FIRST, &OnLvnGetdispinfoList1)
END_MESSAGE_MAP()

bool CFilesMetaFileViewer::InitColumns() 
{	
	m_list.InsertColumn( 0, L"name", LVCFMT_LEFT, 250);
	m_list.InsertColumn( 1, L"size", LVCFMT_LEFT, 60);
	m_list.InsertColumn( 2, L"method", LVCFMT_LEFT, 60);
	m_list.InsertColumn( 3, L"hash", LVCFMT_LEFT, 120);
	m_list.InsertColumn( 4, L"TSID", LVCFMT_LEFT, 50);
	m_list.InsertColumn( 5, L"CRC32", LVCFMT_LEFT, 60);
	m_list.InsertColumn( 6, L"size2", LVCFMT_LEFT, 60);
	m_list.InsertColumn( 7, L"hash2", LVCFMT_LEFT, 120);
	m_list.SetExtendedStyle( LVS_EX_FULLROWSELECT);

	return true;
}

bool CFilesMetaFileViewer::SetData( const void* pData, size_t len )
{
	PFS::CDataBlock db(pData, len);
	PFS::CImageFile mf( db );

	m_list.SetItemCount(0);
	m_mim.clear();
	m_cache.clear();

	if( !PFS::CMetaFileRead::DoRead( mf, m_mim ) )
	{
		// 不是合法的.files.meta文件。
		m_bValidMetaFile = false;

		m_list.SetItemCount( 1 );
		return false;
	}

	m_bValidMetaFile = true;
	size_t size = m_mim.size();
	m_list.SetItemCount( size);
	m_cache.reserve(size);

	CMetaInfoPtr temp;

	for( PFS::CMetaInfoMap::iterator itor = m_mim.begin();
		itor != m_mim.end(); ++itor)
	{
		temp.m_name = itor->first.c_str();
		temp.m_meta = &itor->second;
		m_cache.push_back( temp);
	}

	return true;
}

void CFilesMetaFileViewer::OnLvnGetdispinfoList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);
	
	LV_ITEM* pItem= &(pDispInfo->item);
	size_t index = pItem->iItem;
	*pResult = 0;


	if( (pItem->mask & LVIF_TEXT) != LVIF_TEXT )
	{
		return;
	}

	if( !m_bValidMetaFile )
	{
		switch( pItem->iSubItem)
		{
		case 0:		// name
			wcscpy_s( pItem->pszText, pItem->cchTextMax, L"不是合法的FilesMeta文件。");
			break;
		default:
			wcscpy_s( pItem->pszText, pItem->cchTextMax, L"" );
			break;
		}
		return;
	}

	if( index >= m_cache.size())
	{
		return;
	}

	// 字符串缓冲区有效

	const size_t size = 64;
	wchar_t	str[size] = {0};
	switch( pItem->iSubItem)
	{
	case 0:		// name
		wcscpy_s( pItem->pszText, pItem->cchTextMax, m_cache[index].m_name);
		break;
	case 1:		// size
		_i64tow_s( m_cache[index].m_meta->GetFileSize(), str, size, 10);
		wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
		break;
	case 2:		// method
		_itow_s( m_cache[index].m_meta->GetMethod(), str, size, 10);
		wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
		break;
	case 3:
		{	// half MD5
			const PFS::FILE_DATA_HASH& dataHash = m_cache[index].m_meta->GetFileHash();
			if( dataHash == 0 )
			{
				wcscpy_s( pItem->pszText, pItem->cchTextMax, L"" );
			}
			else
			{
				std::wstring strHash = PFSX::CStringHelper::Hash2String( dataHash );
				wcscpy_s( pItem->pszText, pItem->cchTextMax, strHash.c_str() );
			}
		}
		break;
	case 4:
		{	// TSID
			PFSX::TSID tsid = m_cache[index].m_meta->GetTSID();
			str[4] = 0;
			str[0] = tsid&0xFF;
			str[1] = (tsid>>8)&0xFF;
			str[2] = (tsid>>16)&0xFF;
			str[3] = (tsid>>24)&0xFF;
			wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
		}
		break;
	case 5:
		{	// CRC32
			PFSX::CRC32 crc32 = m_cache[index].m_meta->GetCRC32();
			_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", crc32 );
		}
		break;
	case 6:	// size2
		{
			_i64tow_s( m_cache[index].m_meta->GetFileSize2(), str, size, 10);
			wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
		}
		break;
	case 7:
		{	// half hash2
			const PFS::FILE_DATA_HASH& dataHash = m_cache[index].m_meta->GetFileHash2();
			if( dataHash == 0 )
			{
				wcscpy_s( pItem->pszText, pItem->cchTextMax, L"" );
			}
			else
			{
				std::wstring strHash = PFSX::CStringHelper::Hash2String( dataHash );
				wcscpy_s( pItem->pszText, pItem->cchTextMax, strHash.c_str() );
			}
		}
		break;

	default :
		return;
	}

	*pResult = 1;
}
