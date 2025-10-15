#pragma once
#include <afxole.h>
#include <vector>
#include <string>

class CDropDataSource : public COleDataSource
{
	enum { BUFFERSIZE = 40960 };

	std::vector<std::wstring>		m_files;
	PFS::CDataBlock					m_buffer;
	CWinPFSDoc*						m_doc;

	BOOL OnRenderFileData( LPFORMATETC lpFormatEtc,CFile* pFile )
	{
		if( RegisterClipboardFormat( CFSTR_FILECONTENTS) == lpFormatEtc->cfFormat)
		{
			int index = lpFormatEtc->lindex;
			if( index >= 0 && index < (int)m_files.size())
			{
				std::wstring& file = m_files[index];

				ZIPFILE::CFileEditableReader& fer = m_doc->m_filereader;
				const ZIPFILE::CFileCentral* fc = fer.GetFileCentral( file);
				if( fc)
				{
					ZIPFILE::CFileCentralRead fcr( fer.GetFileIO(), *fc);
					while( size_t read = fcr.Read( m_buffer, BUFFERSIZE))
					{
						if( PFS::FILE_SIZE_ERROR == read )
						{
							return FALSE;
						}
						m_buffer.Resize( read);
						pFile->Write( m_buffer.GetData(), m_buffer.GetSize());	
					}
					return TRUE;
				}
			}
			
		}
		return COleDataSource::OnRenderFileData( lpFormatEtc, pFile);
	}

public:

	std::vector<std::wstring>& GetFile( ) { return m_files; }

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }

	CDropDataSource() { m_buffer.Reserve( BUFFERSIZE); }
};