#include "stdafx.h"

#include "../../src/common.h"
#include "../../src/zip/zipfile.h"
#include "FileCentralCache.h"

class CSortFileCentral
{
	int		m_type;
	bool	m_bAsc;

	bool Sort( ZIPFILE::CFileCentral* fcLeft, ZIPFILE::CFileCentral* fcRight)
	{
		// debug 下比较算法会有 assert 对话框，先忽略
		switch( m_type)
		{
		case NAME:
			return fcLeft->m_name < fcRight->m_name;
		case ORIGINALSIZE:
			return fcLeft->GetOrginalSize() < fcRight->GetOrginalSize();
		case COMPRESSSIZE:
			return fcLeft->GetCompressedSize() < fcRight->GetCompressedSize();
		case OFFSET:
			return fcLeft->GetDataOffset() < fcRight->GetDataOffset();
		case MODIFYTIME:
			return (((unsigned int)(fcLeft->m_usModifyDate) << 16) | fcLeft->m_usModifyTime) 
				< (((unsigned int)(fcRight->m_usModifyDate) << 16) | fcRight->m_usModifyTime);
		case CRC32:
			return fcLeft->m_uiCRC32 < fcRight->m_uiCRC32;

		}
		return false;
	}

public:
	CSortFileCentral( int type, bool bAsc) : m_type( type), m_bAsc( bAsc) {}

	bool operator()( DWORD_PTR left, DWORD_PTR right)
	{
		ZIPFILE::CFileCentral* fcLeft = (ZIPFILE::CFileCentral*)left;
		ZIPFILE::CFileCentral* fcRight = (ZIPFILE::CFileCentral*)right;
		
		if( fcLeft->IsDirectory() != fcRight->IsDirectory())
		{
			return fcLeft->IsDirectory();
		}

		return m_bAsc ? Sort( fcLeft, fcRight) : !Sort( fcLeft, fcRight);
	}
};

void CFileCentralCache::Sort( int type, bool bAsc)
{
	std::sort( begin(), end(), CSortFileCentral( type, bAsc));
}
