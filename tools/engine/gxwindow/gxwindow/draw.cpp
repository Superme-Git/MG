#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS{

class CFileNameImagesParam
{
public:
	CFileNameImagesParam() {}
	~CFileNameImagesParam() {}
public:
	typedef std::map<int,std::wstring> CIDToNameMap;
	typedef std::map<std::wstring,int> CNameToIDMap;

	CIDToNameMap		m_id2name;
	CNameToIDMap		m_name2id;
};

class CImageRefCountMap : public std::map<int, int>
{
public:
	CImageRefCountMap() {}
	virtual ~CImageRefCountMap() {}
};

static CFileNameImages		g_FileNameImagesDefaultInstance;
static CImageTextureManager	g_ImageTextureManager;

} // namespace INLINECLASS{

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFileImages::CFileImages()
{
}

CFileImages::~CFileImages()
{
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFileNameImages::CFileNameImages()
	: m_fnip( new INLINECLASS::CFileNameImagesParam()), m_nID( 0)
{
}

CFileNameImages::~CFileNameImages()
{
	// for dump used files
	//FILE*	f = fopen( "d:\\temp.txt", "w");
	//for( INLINECLASS::CFileNameImagesParam::CNameToIDMap::iterator it = m_fnip->m_name2id.begin(), ite = m_fnip->m_name2id.end(); it != ite; ++ it)
	//{
	//	const std::wstring&	filename = it->first;
	//	fwprintf( f, L"%s\n", filename.c_str());
	//}
	//fclose( f);

	delete m_fnip;
}

const wchar_t* CFileNameImages::QueryFileName( int n)
{
	INLINECLASS::CFileNameImagesParam::CIDToNameMap::iterator it = m_fnip->m_id2name.find( n);
	if( it == m_fnip->m_id2name.end())
		return NULL;
	const std::wstring& strName = it->second;
	return strName.c_str();
}

int CFileNameImages::GetNewID()
{
	return ++ m_nID;
}

int CFileNameImages::RegisterFileName( const wchar_t* szName)
{
	std::wstring		strFileName( szName);

	INLINECLASS::CFileNameImagesParam::CNameToIDMap::iterator it = m_fnip->m_name2id.find( strFileName);
	if( it == m_fnip->m_name2id.end())
	{
		int nid = GetNewID();
		m_fnip->m_name2id[strFileName] = nid;
		m_fnip->m_id2name[nid] = strFileName;
		return nid;
	}
	return it->second;
}

CFileNameImages& CFileNameImages::GetInstance()
{
	return INLINECLASS::g_FileNameImagesDefaultInstance;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageParams::CImageParams()
{
	nBitmap= -1;
	x = 0;
	y = 0;
	color = 0;
}

CImageParams::CImageParams( int bmp, int x, int y, COLOR clr)
{
	nBitmap= bmp;
	this->x = x;
	this->y = y;
	color = clr;
}

CImageParams::~CImageParams()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageTextureManager::CImageTextureManager()
: m_pircm( new INLINECLASS::CImageRefCountMap())
{

}

CImageTextureManager::~CImageTextureManager()
{
	delete m_pircm;
}

void CImageTextureManager::InsertImage( int nBitmap)
{
	if ( nBitmap >= 0)
	{
		INLINECLASS::CImageRefCountMap::iterator it = m_pircm->find( nBitmap);
		if ( it != m_pircm->end())
			it->second ++;
		else
			m_pircm->insert( std::make_pair( nBitmap, 1));
	}
}

void CImageTextureManager::RemoveImage( int nBitmap)
{
	if ( nBitmap >= 0)
	{
		INLINECLASS::CImageRefCountMap::iterator it = m_pircm->find( nBitmap);
		if ( it != m_pircm->end())
		{
			if ( --it->second > 0)
				return;
			m_pircm->erase( it);
		}
		CDesktop::GetDefault().GetDefaultRenderer()->FreePicture( nBitmap);
	}
}

CImageTextureManager& CImageTextureManager::GetInstance()
{
	return INLINECLASS::g_ImageTextureManager;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CDraw::CDraw()
{
}

CDraw::~CDraw()
{
}

#ifdef GXWINDOW_ENABLE_BATCH_RENDER
bool CDraw::IsNeedRedraw() const
{
	return GXWIsNeedRedraw();
}
#endif

} // namespace GXWINDOW { 