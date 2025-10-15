// 
// 2010-11-17
//
#include "..\common.h"
#include "DeviceFactory.h"
//#include "PackageDirectoryDevice.h"
//#include "WindowsDirectoryDevice.h"
//#include "PackageFileDevice.h"
#include "PFSBaseDevice.h"

namespace PFS
{
	IAbstractDevice* CDeviceFactory::CreateDeviceObject(std::wstring& device, MOUNT_TYPE mt)
	{
		// TODO : supper device?
		if( device.empty() )
		{
			CEnv::SetLastError(EC_DEVICE_INVALID_DEVNAME);
			return NULL;
		}

		bool IsPFSPath = (device[0] == L'/');
		if( IsPFSPath ) 
		{
			// ������Ҫд������Device����ʹ�ܹ�ӳ��ΪNativeFileName������Ҳ������ô����
			// ��Ϊ���ǵ�PFS��֧�ֹ���д��ͬ��PFS���ļ���
			if( IsMountReadOnly( mt ) ) 
			{
				// ����ֻ��ϵͳ����PFSPath���б�Ҫ��ת�����Լ�PFSϵͳ�Ĺ����ԡ�
				IsPFSPath = !CEnv::PFSFileNameToNativeFileName( std::wstring(device), device );
			}
		}

		if( IsMountSetFSType(mt) )
		{
			// ���ж�FSType֮ǰ��PFSPath�Ѿ������˱�Ҫ��ת��������ת��������ָ�����͵�FSSystemҲ��������ġ�

			FS_TYPE fst = FSTypeFromMountType( mt ); // ע�⣬0 - FST_NATIVE������Чֵ��
			return new CPFSBaseDevice(fst);
		}

		if( IsPFSPath )
		{
			// device ���ܼ򵥵�ת����Windows·����
			// ��ʱ��device��������豸������һ�����Device��ֻ��ʹ��MountDirectoryDevice��
			return new CMountDirectoryDevice();
		}

		// device �Ѿ���PFS·��ת������OS·����
		// �������ø�·��������Device����PFS�޹أ����Լ򻯶�������ԡ�

		// TODO: ʵ��һ��HttpInetFSϵͳ����֧��
		// if ( 0 == _wcsicmp(L"http://", device.substr( 0, wcslen(L"http://")-1 ) )
		// {
		//	 return new CInetDirectoryDevice();
		// }

		if( PFS::CFile::IsFileExisting( device ) )
		{
			return new CPackageFileDevice();
		}

		if( PFS::CFileIO::IsDirectoryExisting( device ) )
		{
			return new CWindowsDirectoryDevice();
		}

		if( PFS::IsMountReadOnly(mt) )
		{
			CEnv::SetLastError(EC_DEVICE_MISSING_STORAGE);
			return NULL;
		}

		// �Ƿ����ȴ���Ŀ¼ϵͳ��������ǣ������Ǹ����ļ����Ƿ���к�׺ѡ�񴴽�Ŀ¼���Ǵ�������
		if( !IsMountPreferDir( mt ) )
		{
			// ����ļ������к�׺����ôѡ�񴴽���
			std::wstring::size_type dotPos = device.find_last_of(L'.');
			if( dotPos != std::wstring::npos )
			{
				std::wstring::size_type slashPos = device.find_last_of(L'/');
				std::wstring::size_type backslashPos = device.find_last_of(L'\\');
				std::wstring::size_type sepPos = max( (int)slashPos, (int)backslashPos );
				if( sepPos == std::wstring::npos || dotPos > sepPos )
				{
					// like a/b/c.d or a\b\c.d
					return new CPackageFileDevice();
				}
			}
		}

		// TODO:
		return new CWindowsDirectoryDevice();
	}

}