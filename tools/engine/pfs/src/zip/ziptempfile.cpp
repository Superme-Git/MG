#include "../common.h"
#include "zipfile.h"
#include "ziptempfile.h"

namespace PFS
{

enum 
{ 
	BUFFERSIZE = 65535,
	MAXCONTAINERSIZE = 300*1024*1024,	// ÿ����ʱ�ļ�Containerռ�õ������̿ռ�
};		

bool CZipTempFile::ReOpen()
{
	if( m_startpos != m_container.m_file.Seek( m_startpos, FSM_SET) )
	{
		CEnv::GetOutLogInstance()->Print(
			L"ERROR: - reopen the temp file failed( file size: %lu, offset: %lu, closed: %u).\n",
			m_size, m_offset, m_closed );

		return false;
	}
	
	m_offset = 0;
	m_closed = false;

	return true;
}

size_t CZipTempFile::Read( void* data, size_t size)
{
	if( m_offset > m_size)
		return 0;
	fsize_t res = Seek( m_offset, FSM_SET);
	if( FILE_SIZE_ERROR == res)
		return FILE_SIZE_ERROR;
	size_t left = (size_t)(m_size - m_offset);
	left = min( left, size);
	size_t sread = m_container.m_file.Read( data, left);
	if( FILE_SIZE_ERROR == sread)
	{	
		CEnv::GetOutLogInstance()->Print(
			L"ERROR: - read data failed from the temp file(data size:%u, file size: %lu, offset: %lu, left: %u).\n",
			size, m_size, m_offset, left );
		return FILE_SIZE_ERROR;
	}

	m_offset += sread;
	return sread;
}

size_t CZipTempFile::Write( const void* data, size_t size)
{
	long morewrite = (long)(size + m_offset - m_size);
	if( morewrite > 0)
	{
		// ������ǰ�ļ���Χ
		if( m_closed)
		{
			CEnv::GetOutLogInstance()->Print(
				L"ERROR: - write data failed to the closed temp file(data size:%u, file size: %lu, offset: %lu).\n",
				size, m_size, m_offset );
			return FILE_SIZE_ERROR;
		}
	}
	fsize_t res = Seek( m_offset, FSM_SET);
	if( FILE_SIZE_ERROR == res)
		return FILE_SIZE_ERROR;
	size_t swrite = m_container.m_file.Write( data, size);
	if( FILE_SIZE_ERROR == swrite)
	{
		CEnv::GetOutLogInstance()->Print(
			L"ERROR: - write data failed to the temp file(size:%u).\n", size );
		return FILE_SIZE_ERROR;
	}
	if( morewrite > 0)
		m_size += morewrite;
	m_offset += swrite;
	return swrite;
}

fsize_t CZipTempFile::Seek( foff_t pos, FILE_SEEK_METHOD fsm)
{
	fsize_t seekpos = 0;
	switch( fsm)
	{
	case FSM_SET:
		seekpos = (size_t)pos;
		break;
	case FSM_CUR:
		seekpos = m_offset + (size_t)pos;
		break;
	case FSM_END:
		seekpos = m_size + (size_t)pos;
		break;
	}
	if( seekpos > m_size && m_closed)
	{
		CEnv::GetOutLogInstance()->Print(
			L"ERROR: - failed to seek to position that out of range of the closed file length(seekpos: %lu, file size:%lu).\n", seekpos, m_size );
		return FILE_SIZE_ERROR;
	}

	if( m_offset == 0 || m_offset != seekpos )
	{
		if( (m_startpos + seekpos) != m_container.m_file.Seek( m_startpos + seekpos, FSM_SET) )
		{
			CEnv::GetOutLogInstance()->Print(
				L"ERROR: - failed to seek to position in the temp file(seekpos: %lu, file size:%lu).\n", m_startpos + seekpos, m_container.m_file.GetSize() );
			return FILE_SIZE_ERROR;
		}

		m_offset = seekpos;		
	}

	return m_offset;
}

bool CZipTempFile::ReadAll(void* data, fsize_t size )
{
	if ( m_size > size )
	{
		PFS::CEnv::SetLastError( PFS::EC_INSUFFICIENT_BUFFER );
		return false;
	}

	if( m_size > MAX_MEMORY_DATA_SIZE )
	{
		PFS::CEnv::SetLastError( PFS::EC_FILESIZE_TOO_LARGE);
		return false;
	}

	fsize_t old = m_offset;
	Seek( 0, PFS::FSM_SET);
	fsize_t res = Read( data, (size_t)size);
	Seek( old, PFS::FSM_SET);
	return ( res == size);
}

CZipTempFileContainer::~CZipTempFileContainer()
{
	// ���б���������CZipTempFile����������ڵ���Ϊֹ
	CZipTempFileMap::iterator iEnd = m_mapTempFile.end();
	for( CZipTempFileMap::iterator itor = m_mapTempFile.begin(); itor != iEnd; ++itor)
	{
		// ����ķ������ȼۣ����ԣ������໥����
		// CEnv::CloseFile( itor->second );
		CloseFile( itor->second );
	}
	m_mapTempFile.clear();
	m_curwritefile = NULL;
}

CZipTempFile* CZipTempFileContainer::GetTempFile( const std::wstring& filename )
{
	CZipTempFileMap::iterator itor = m_mapTempFile.find( filename );
	if( itor != m_mapTempFile.end())
	{
		return itor->second;
	}
	return NULL;
}

CZipTempFile* CZipTempFileContainer::CreateTempFile( const std::wstring& filename)
{
	if( m_mapTempFile.empty())
	{
		if( !m_file.Open())
			return NULL;
	}

	// �ر��ϴδ򿪵��ļ�
	if( m_curwritefile)
	{
		m_curwritefile->Close();
	}

	m_curwritefile = new CZipTempFile( *this, m_file.GetSize() );

	CZipTempFileMap::_Pairib pr = m_mapTempFile.insert( std::make_pair( filename, m_curwritefile));
	if( !pr.second )
	{
		if( pr.first->second != NULL )
			CloseFile( pr.first->second );

		CEnv::GetOutLogInstance()->Print(
			L"WARN: - temp file duplicated for %s, replace original one.\n", filename.c_str() );
		
		// ����Ѿ����ڣ��������������ļ�����
		pr.first->second = m_curwritefile;
	}

	return m_curwritefile;
}

void CZipTempFileContainer::DetachCentral( const std::wstring& filename )
{
	m_mapTempFile.erase( filename );
}

void CZipTempFileContainer::CloseFile( CZipTempFile*& rpFile )
{
	// ɾ���ļ�����
	delete rpFile; 
	rpFile = NULL;
}

// ������Ȼ�򿪵��ļ���Ŀ
size_t CZipTempFileContainer::Commit()
{
	for( CZipTempFileMap::iterator itor = m_mapTempFile.begin(); itor != m_mapTempFile.end(); )
	{
		// �ļ��Ѿ����Ϊ�رա�
		if( itor->second->IsClosed() )
		{
			if( m_curwritefile == itor->second )
				m_curwritefile = NULL;

			// ɾ���ļ�����
			CloseFile( itor->second );

			itor = m_mapTempFile.erase(itor);
		}
		else
		{
			++ itor;
		}
	}

	return m_mapTempFile.size();
}

CZipTempFileManager::~CZipTempFileManager()
{
	Destroy();
}

size_t CZipTempFileManager::Commit()
{
	size_t nOpen = 0;

	for( CZipTempFileContainerVector::iterator itor = m_containers.begin(); itor != m_containers.end(); )
	{
		// �ύ��������ʱ�ļ���������������δ�رյ��ļ���Ŀ
		nOpen += (*itor)->Commit();

		if( (*itor)->IsEmpty() )
		{
			// ����ļ����Ѿ���ȫ�ύ��ɾ��֮��
			if( m_curcontainer == (*itor ) )
				m_curcontainer = NULL;

			(*itor)->Delete();

			itor = m_containers.erase( itor );
		}
		else
		{
			++ itor;
		}
	}

	return nOpen;
}

void CZipTempFileManager::Destroy()
{
	CZipTempFileContainerVector::iterator iEnd = m_containers.end(); 
	for( CZipTempFileContainerVector::iterator itor = m_containers.begin(); itor != iEnd; ++itor)
	{
		(*itor)->Delete();
	}
	m_containers.clear();
	m_curcontainer = NULL;
}

CZipTempFile* CZipTempFileManager::GetTempFile( const std::wstring& filename )
{
	CZipTempFileContainerVector::iterator iEnd = m_containers.end(); 
	for( CZipTempFileContainerVector::iterator itor = m_containers.begin(); itor != iEnd; ++itor)
	{
		CZipTempFile* tempfile = (*itor)->GetTempFile( filename );
		if( tempfile)
		{
			return tempfile;
		}
	}
	return NULL;
}

CZipTempFileContainer* CZipTempFileManager::AddContainer()
{
	CZipTempFileContainer* container = new CZipTempFileContainer;
	m_containers.push_back( container);
	return container;
}

CZipTempFile* CZipTempFileManager::CreateTempFile( const std::wstring& filename, ZIPFILE::CFileCentralRead* centralread)
{
	CZipTempFile* oldfile = NULL;
	if( centralread != NULL )
	{
		oldfile = GetTempFile( filename );
		if( oldfile != NULL )
		{
			// ���ԭ������ʱ�ļ��Ѿ�����
			// ��ʱǿ�ƴ���һ��container����������ԭ����ʱ�ļ����ڵ�container
			// �Ա�֤oldfile��newfile�Կ�ʱƵ��seek�ļ�ָ�������
			m_curcontainer = AddContainer();
		}
	}

	if( oldfile == NULL )
	{
		if( NULL == m_curcontainer)
		{
			m_curcontainer = AddContainer();
		}
		else
		{
			if( m_curcontainer->m_file.GetSize() >= MAXCONTAINERSIZE)
			{
				// �������contianerռ�õĴ��̿ռ䳬�� MAXCONTAINERSIZE �򴴽����µ� container
				m_curcontainer = AddContainer();
			}		
			else if( m_curcontainer->m_curwritefile && !m_curcontainer->m_curwritefile->IsClosed())
			{
				// ��������ļ�û�йر�
				m_curcontainer = AddContainer();
			}
		}
	}
	
	CZipTempFile* newfile = m_curcontainer->CreateTempFile( filename);
	if( newfile != NULL )
	{
		if( centralread != NULL )
		{
			if( oldfile != NULL )
			{
				if( !WriteFileData( oldfile, newfile) )
				{
					CEnv::GetOutLogInstance()->Print(
						L"ERROR: - copy temp file to new temp file failed(%s).\n", filename.c_str() );
				}

				// ע�⣺
				// ��Ϊ CZipTempFile ��д�� Delete() ������
				// ��������ķ������ȼ��� CZipTempFileContainer::CloseFile(oldfile)
				//	CEnv::CloseFile( oldfile );
				oldfile->GetContainer().DetachCentral( filename );
				// ����ɾ������
				CZipTempFileContainer::CloseFile( oldfile );
			}
			else 
			{
				if( !WriteFileData( centralread, newfile) )
				{
					CEnv::GetOutLogInstance()->Print(
						L"ERROR: - copy central data to temp file failed(%s).\n", filename.c_str() );
				}
			}
			newfile->Seek( 0, PFS::FSM_SET);
		}
	}
	return newfile;
}

bool CZipTempFileManager::WriteFileData( CZipTempFile* pSrc, CZipTempFile* pDes)
{
	// ע�⣺
	// ���������pSrc��pDes�����ͬһ��container�У���Ȼ�������⣬�������!
	//
	pSrc->Seek( 0, PFS::FSM_SET);
	CDataBlock out;
	out.Reserve( BUFFERSIZE);
	while( size_t readlen = pSrc->Read( out.GetData(), BUFFERSIZE))
	{
		if( FILE_SIZE_ERROR == readlen)
			return false;
		if( FILE_SIZE_ERROR == pDes->Write( out.GetData(), readlen))
			return false;
	}
	return true;
}

bool CZipTempFileManager::WriteFileData( ZIPFILE::CFileCentralRead* centralread, CZipTempFile* pDes)
{
	centralread->Reset();
	CDataBlock out;
	out.Reserve( BUFFERSIZE);
	while( size_t readlen = centralread->Read( out, BUFFERSIZE))
	{
		if( FILE_SIZE_ERROR == readlen)
			return false;
		if( FILE_SIZE_ERROR == pDes->Write( out.GetData(), readlen))
			return false;
	}
	return true;
}

}	// namespace PFS