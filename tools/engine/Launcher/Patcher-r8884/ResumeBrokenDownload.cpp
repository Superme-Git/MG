#include "StdAfx.h"
#include "ResumeBrokenDownload.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "TextManager.h"
#include "TextID.h"
#include <wininet.h>

const wchar_t CFG[] = L".cfg";	// 保存文件下载进度的配置文件后缀名

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

	// 打开配置文件允许出错
	if( m_cfgFile.Open( cfg, PFS::FM_CREAT, PFS::FA_RDWR ) )
	{
		bCfgOK = ReadCfgFile( offset, total, hash );
		if( bCfgOK )
		{
			if( hash == m_hash )
			{
				// 如果文件下载完毕，方执行Hash校验，否则没有必要。
				if( offset == total && m_hash != 0 )
				{
					if( CheckFile() )
						return true;
					else
					{
						// 废弃已下载的文件
						offset = 0;
						total = -1;
					}
				}
				// else trust the cached file.
			}
			else
			{
				// 废弃已下载的文件
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
			// 无法读取服务器文件(%1!s!)!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_INET_FILE_OPEN_FAILED, src.c_str() ) );
			return false;
		}

		sizeNeedDownload = inetFile.GetSize();
	}

	PFSX::CNativeFile theFile;
	if( !theFile.Open( des, PFS::FM_CREAT, PFS::FA_WRONLY))
	{
		// 创建本地文件失败(%1!s!)!
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_NATIVE_FILE_CREATE_FAILED, des.c_str() ) );
		return false;
	}

	if( theFile.Seek( offset, PFS::FSM_SET ) != offset )
	{
		if( theFile.GetSize() < offset )
		{
			// 删除配置文件
			m_cfgFile.Close();
			PFS::CFileIO::DeleteFile( cfg );

			// 本地缓存文件已被破坏(%1!s!)，请重新下载!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_NATIVE_FILE_CACHE_CORRUPT, des.c_str() ) );
		
			return false;
		}
		else
		{
			// 本地文件重定位失败(%1!s!)!
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_NATIVE_FILE_SEEK_FAILED, des.c_str() ) );
			return false;
		}
	}

	// BUG:
	// 在某种特殊情况下，断点续传机制会因为缓存的废弃文件数据没有被正确处理而无法处理，重试都无效。
	// 问题分析和解决方案如下：
	// 将有效长度设为offset，其余的数据都截断，否则
	// 如果此时Open的文件是原来已废弃的文件，而且，继续下载的数据不能完全覆盖废弃文件offset之后的数据的话，
	// 这些没有覆盖的废弃数据将会导致断点续传之后的文件校验失败。
	theFile.SetEnd();

	if( sizeNeedDownload == 0 )
	{
		// 文件已经下载完毕(%1!s!)!
		GlobalNotify( GlobalFormatText( TIP_INET_FILE_DOWNLOADED_ALREADY, src.c_str() ).c_str() );
	}

	PFS::fsize_t szTotal = sizeNeedDownload + offset;
	const size_t BUFSIZE = 40960;

	GlobalNotifyStart( ((int)szTotal+BUFSIZE-1)/BUFSIZE);
	
	size_t szTotalRead = offset;
	if( szTotalRead )
	{
		// 设置进度条到上次下载的位置
		// 已下载：(%1!lu!)
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
				// 用户要退出此过程（此时不视为下载失败）
				break;
			}

			szRead = inetFile.Read( buffer, BUFSIZE);
			if ( PFS::FILE_SIZE_ERROR != szRead && szRead > 0 )
			{
				nTriedTimes1 = 0; // 下载成功,将尝试次数重置为0
				nTriedTimes2 = 0; 
				theFile.Write( buffer, szRead );
				szTotalRead += szRead;

				// 保存配置文件
				WriteCfgFile( szTotalRead );

				// 已下载：(%1!lu!)		
				GlobalNotifyStep( 
					GlobalFormatText( TIP_INET_FILE_DOWNLOADED_PERCENT, int( szTotalRead*100.0/szTotal) ) );
			}
			else
			{
				int nErrorCode = PFS::CEnv::GetLastError();
				if( nErrorCode == ERROR_INTERNET_TIMEOUT || 
					nErrorCode == ERROR_INTERNET_FORCE_RETRY )
				{
					// 超时错误,尝试30次,每次1s,共30s
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
					// 内部错误，无需重试
					bNetworkErrorOccurred = true;
					break;
				}
				else
				{
					// 其它错误尝试10次,每次0.1s,共1s
					if( nTriedTimes2 ++ > 10 )
					{
						bNetworkErrorOccurred = true;
						break;
					}
					Sleep( 100 );
				}
			}
		}

		// 写总大小
		WriteCfgFile( szTotalRead, szTotal, m_hash );

		inetFile.Close();
	}

	GlobalNotifyEnd();

	theFile.SetEnd();
	theFile.Close();

	if( szTotal == szTotalRead )
	{
		// 删除配置文件
		m_cfgFile.Close();
		
		if( sizeNeedDownload == 0 )
		{
			return true;
		}

		if( CheckFile() )
		{
			// 文件下载完毕(%1!s!)!
			GlobalNotify( GlobalFormatText( TIP_INET_FILE_DOWNLOADED_JUST_NOW, src.c_str() ).c_str() );
			return true;
		}

		// 只有校验失败，这里才删除断点续传配置文件，校验成功不删除
		// 此时并未删除废弃的缓存文件。
		PFS::CFileIO::DeleteFile( cfg );

		// 服务器文件数据已发生改变(%1!s!)!
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
				// 与服务器连接已断开，下载 %1!s! 失败!
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_INET_FILE_DISCONNECT_FROM_SERVER, src.c_str() ) );
			}
			else
			{
				// 服务器文件大小已发生改变(%1!s!)!
				woss << L"服务器文件 " << src << L" 大小已发生改变！";
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_INET_FILE_SIZE_CHANGED, src.c_str() ) );

				// 问题：这里是否有必要删除cfg文件吗？
				// 解答：即使这里不删除，下次重新下载时，hash值应该会发生变化，cfg会自动失效。
			}
		}
	}

	return false;
}

bool CResumeBrokenDownload::CheckFile()
{
	if ( m_hash == 0 )
	{
		// 如果hash值为0，不做校验
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

	// 校验失败，重置错误码
	PFS::CEnv::SetLastError( PFS::EC_INVALID_PATCH );

	return false;
}

// 读配置文件
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

// 写配置文件
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