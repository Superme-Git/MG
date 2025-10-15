#include "StdAfx.h"
#include "ResumeBrokenDownload.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "TextManager.h"
#include "TextID.h"
#include <wininet.h>

const wchar_t CFG[] = L".cfg";	// �����ļ����ؽ��ȵ������ļ���׺��

CResumeBrokenDownload::CResumeBrokenDownload(const std::wstring& file, const std::wstring& url, 
											 const std::wstring& localPath, unsigned long long hash)
: m_file( file)
, m_url( url)
, m_localPath( localPath)
, m_hash( hash)
{
}

CResumeBrokenDownload::~CResumeBrokenDownload(void)
{
}

bool CResumeBrokenDownload::Download( HANDLE hBreakEvent)
{
	std::wstring src = m_url + m_file;
	std::wstring des = m_localPath + m_file;
	std::wstring cfg = des + CFG;
	size_t offset = 0;
	PFS::fsize_t total = -1;
	PFS::FILE_DATA_HASH hash = 0;
	bool bCfgOK = false;

	// �������ļ��������
	if( m_cfgFile.Open( cfg, PFS::FM_CREAT, PFS::FA_RDWR ) )
	{
		bCfgOK = ReadCfgFile( offset, total, hash );
		if( bCfgOK )
		{
			if( hash == m_hash )
			{
				// ����ļ�������ϣ���ִ��HashУ�飬����û�б�Ҫ��
				if( offset == total && m_hash != 0 )
				{
					if( CheckFile() )
						return true;
					else
					{
						// ���������ص��ļ�
						offset = 0;
						total = -1;
					}
				}
				// else trust the cached file.
			}
			else
			{
				// ���������ص��ļ�
				offset = 0;
				total = -1;
			}
		}
	}

	PFS::fsize_t sizeNeedDownload = 0;
	PFSX::CInetFile inetFile;
	if( !bCfgOK || total != offset || hash != m_hash )
	{
		if( !inetFile.Open( src, (long)offset, -1) )
		{
			// �޷���ȡ�������ļ�(%1!s!)!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_INET_FILE_OPEN_FAILED, src.c_str() ) );
			return false;
		}

		sizeNeedDownload = inetFile.GetSize();
	}

	PFSX::CNativeFile theFile;
	if( !theFile.Open( des, PFS::FM_CREAT, PFS::FA_WRONLY))
	{
		// ���������ļ�ʧ��(%1!s!)!
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_NATIVE_FILE_CREATE_FAILED, des.c_str() ) );
		return false;
	}

	if( theFile.Seek( offset, PFS::FSM_SET ) != offset )
	{
		if( theFile.GetSize() < offset )
		{
			// ɾ�������ļ�
			m_cfgFile.Close();
			PFS::CFileIO::DeleteFile( cfg );

			// ���ػ����ļ��ѱ��ƻ�(%1!s!)������������!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_NATIVE_FILE_CACHE_CORRUPT, des.c_str() ) );
		
			return false;
		}
		else
		{
			// �����ļ��ض�λʧ��(%1!s!)!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_NATIVE_FILE_SEEK_FAILED, des.c_str() ) );
			return false;
		}
	}

	// BUG:
	// ��ĳ����������£��ϵ��������ƻ���Ϊ����ķ����ļ�����û�б���ȷ������޷��������Զ���Ч��
	// ��������ͽ���������£�
	// ����Ч������Ϊoffset����������ݶ��ضϣ�����
	// �����ʱOpen���ļ���ԭ���ѷ������ļ������ң��������ص����ݲ�����ȫ���Ƿ����ļ�offset֮������ݵĻ���
	// ��Щû�и��ǵķ������ݽ��ᵼ�¶ϵ�����֮����ļ�У��ʧ�ܡ�
	theFile.SetEnd();

	if( sizeNeedDownload == 0 )
	{
		// �ļ��Ѿ��������(%1!s!)!
		GlobalNotify( GlobalFormatText( TIP_INET_FILE_DOWNLOADED_ALREADY, src.c_str() ).c_str() );
	}

	PFS::fsize_t szTotal = sizeNeedDownload + offset;
	const size_t BUFSIZE = 40960;

	GlobalNotifyStart( ((int)szTotal+BUFSIZE-1)/BUFSIZE);
	
	size_t szTotalRead = offset;
	if( szTotalRead )
	{
		// ���ý��������ϴ����ص�λ��
		// �����أ�(%1!lu!)
		GlobalNotifyStep(
			GlobalFormatText( TIP_INET_FILE_DOWNLOADED_PERCENT, int( szTotalRead*100.0/szTotal) ),
			0, (int)szTotalRead/BUFSIZE);
	}

	size_t szRead = 0;
	BYTE buffer[ BUFSIZE ] = {0};
	int nTriedTimes1 = 0;
	int nTriedTimes2 = 0;
	bool bNetworkErrorOccurred = false;
	if ( sizeNeedDownload )
	{
		while( szTotalRead < szTotal )
		{
			if( DetectBreakEvent( hBreakEvent) )
			{
				// �û�Ҫ�˳��˹��̣���ʱ����Ϊ����ʧ�ܣ�
				break;
			}

			szRead = inetFile.Read( buffer, BUFSIZE);
			if ( PFS::FILE_SIZE_ERROR != szRead && szRead > 0 )
			{
				nTriedTimes1 = 0; // ���سɹ�,�����Դ�������Ϊ0
				nTriedTimes2 = 0; 
				theFile.Write( buffer, szRead );
				szTotalRead += szRead;

				// ���������ļ�
				WriteCfgFile( szTotalRead );

				// �����أ�(%1!lu!)		
				GlobalNotifyStep( 
					GlobalFormatText( TIP_INET_FILE_DOWNLOADED_PERCENT, int( szTotalRead*100.0/szTotal) ) );
			}
			else
			{
				int nErrorCode = PFS::CEnv::GetLastError();
				if( nErrorCode == ERROR_INTERNET_TIMEOUT || 
					nErrorCode == ERROR_INTERNET_FORCE_RETRY )
				{
					// ��ʱ����,����30��,ÿ��1s,��30s
					if( nTriedTimes1 ++ > 30 )
					{
						bNetworkErrorOccurred = true;
						break;
					}
					Sleep( 1000 );
				}
				else if (
					nErrorCode == ERROR_INTERNET_INTERNAL_ERROR ||
					nErrorCode == ERROR_INTERNET_INCORRECT_HANDLE_STATE )
				{
					// �ڲ�������������
					bNetworkErrorOccurred = true;
					break;
				}
				else
				{
					// ����������10��,ÿ��0.1s,��1s
					if( nTriedTimes2 ++ > 10 )
					{
						bNetworkErrorOccurred = true;
						break;
					}
					Sleep( 100 );
				}
			}
		}

		// д�ܴ�С
		WriteCfgFile( szTotalRead, szTotal, m_hash );

		inetFile.Close();
	}

	GlobalNotifyEnd();

	theFile.SetEnd();
	theFile.Close();

	if( szTotal == szTotalRead )
	{
		// ɾ�������ļ�
		m_cfgFile.Close();
		
		if( sizeNeedDownload == 0 )
		{
			return true;
		}

		if( CheckFile() )
		{
			// �ļ��������(%1!s!)!
			GlobalNotify( GlobalFormatText( TIP_INET_FILE_DOWNLOADED_JUST_NOW, src.c_str() ).c_str() );
			return true;
		}

		// ֻ��У��ʧ�ܣ������ɾ���ϵ����������ļ���У��ɹ���ɾ��
		// ��ʱ��δɾ�������Ļ����ļ���
		PFS::CFileIO::DeleteFile( cfg );

		// �������ļ������ѷ����ı�(%1!s!)!
		upd::SetLastError( PFS::CEnv::GetLastError(),
			GlobalFormatText( ERR_INET_FILE_DATA_CHANGED, src.c_str() ) );
	}
	else
	{
		if( !DetectBreakEvent( hBreakEvent))
		{
			std::wostringstream woss;
			if( bNetworkErrorOccurred )
			{
				// ������������ѶϿ������� %1!s! ʧ��!
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_INET_FILE_DISCONNECT_FROM_SERVER, src.c_str() ) );
			}
			else
			{
				// �������ļ���С�ѷ����ı�(%1!s!)!
				woss << L"�������ļ� " << src << L" ��С�ѷ����ı䣡";
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_INET_FILE_SIZE_CHANGED, src.c_str() ) );

				// ���⣺�����Ƿ��б�Ҫɾ��cfg�ļ���
				// ��𣺼�ʹ���ﲻɾ�����´���������ʱ��hashֵӦ�ûᷢ���仯��cfg���Զ�ʧЧ��
			}
		}
	}

	return false;
}

bool CResumeBrokenDownload::CheckFile()
{
	if ( m_hash == 0 )
	{
		// ���hashֵΪ0������У��
		return true;
	}

	PFSX::CNativeFile nf;
	if( nf.Open( m_localPath + m_file, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		PFS::FILE_DATA_HASH hash;
		PFS::helper::CMD5FileHashMethod md5Hash;
		md5Hash.MakeFileHash( nf, hash);

		if( hash == m_hash)
		{
			return true;
		}
	}

	// У��ʧ�ܣ����ô�����
	PFS::CEnv::SetLastError( PFS::EC_INVALID_PATCH );

	return false;
}

// �������ļ�
bool CResumeBrokenDownload::ReadCfgFile( size_t& rOffset, PFS::fsize_t& rTotal, PFS::FILE_DATA_HASH& rHash )
{
	size_t offset = 0;
	if( 0 != m_cfgFile.Seek( 0, PFS::FSM_SET) )
	{
		return false;
	}

	if( sizeof( size_t) != m_cfgFile.Read( &offset, sizeof( offset)) )
	{
		return false;
	}
	
	if( sizeof( PFS::fsize_t) != m_cfgFile.Read( &rTotal, sizeof( PFS::fsize_t) ) )
	{
		rTotal = -1;
		return false;
	}

	if ( sizeof( PFS::FILE_DATA_HASH) != m_cfgFile.Read( &rHash,sizeof( PFS::FILE_DATA_HASH) ) )
	{
		rHash = 0;
		return false;
	}

	rOffset = offset;

	return true;
}

// д�����ļ�
bool CResumeBrokenDownload::WriteCfgFile( size_t offset, PFS::fsize_t total, const PFS::FILE_DATA_HASH& hash  )
{
	m_cfgFile.Seek( 0, PFS::FSM_SET);
	if( sizeof( size_t) != m_cfgFile.Write( &offset, sizeof( size_t)))
	{
		return false;
	}

	if ( total != (PFS::fsize_t)-1 )
	{
		if( sizeof( PFS::fsize_t) != m_cfgFile.Write( &total, sizeof( PFS::fsize_t)))
		{
			return false;
		}
	}

	if ( hash != 0 )
	{
		if( sizeof( PFS::FILE_DATA_HASH) != m_cfgFile.Write( &hash,sizeof( PFS::FILE_DATA_HASH) ) )
		{
			return false;
		}
	}

	return true;
}