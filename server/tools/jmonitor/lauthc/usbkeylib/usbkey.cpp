#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <list>
#include <string>

#include "hidapi.h"
#include "usbkey.h"

CUsbHidKeyFinder::CUsbHidKeyFinder() {}
CUsbHidKeyFinder::~CUsbHidKeyFinder() {}

void CUsbHidKeyFinder::FindKeys()
{
	struct hid_device_info* devs = hid_enumerate( VENDOR_ID, PRODUCT_ID);
	struct hid_device_info* cur_dev = devs;
	while (cur_dev)
	{
		if( 0 == wcscmp( cur_dev->product_string, L"www.locojoy.com"))
			m_Paths.push_back( cur_dev->path);
		cur_dev = cur_dev->next;
	}
	hid_free_enumeration(devs);
}

int CUsbHidKeyFinder::GetKeyCount() const
{
	return (int)m_Paths.size();
}

const char* CUsbHidKeyFinder::GetFirstKeyPath() const
{
	if( m_Paths.empty())
		return NULL;
	const std::string& strPath = *m_Paths.begin();
	return strPath.c_str();
}

CUsbHidKey::CUsbHidKey()
	: m_handle( NULL)
{}

CUsbHidKey::~CUsbHidKey()
{
	if( !!m_handle)
		hid_close( m_handle);
}

bool CUsbHidKey::HidFeatureIO( int nSleep)
{
	if( -1 == hid_send_feature_report( m_handle, m_pBuffer, SIZE_FEATURE))
		return false;
	usleep( nSleep * 1000);
	while( -1 != hid_get_feature_report( m_handle, m_pBuffer, SIZE_FEATURE))
	{
		switch( m_pBuffer[SIZE_FEATURE - 1])
		{
		case FEATURE_IO_ENDOUT:
			break;
		case FEATURE_IO_ENDIN:
			return true;
		default:
			return false;
		}
		usleep( 50 * 1000);
	}
	return false;
}

void CUsbHidKey::SetBufferCommand( unsigned char bCmd, unsigned char nDataCount, const unsigned char* pData)
{
	m_pBuffer[0] = 0;
	m_pBuffer[1] = bCmd;
	m_pBuffer[2] = nDataCount;
	if( nDataCount && pData)
		memcpy( &m_pBuffer[3], pData, nDataCount);
	m_pBuffer[SIZE_FEATURE - 1] = FEATURE_IO_ENDOUT;
}

unsigned char CUsbHidKey::GetDataCount()
{
	return m_pBuffer[2];
}

unsigned char* CUsbHidKey::GetData()
{
	return &m_pBuffer[3];
}

bool CUsbHidKey::OpenKey( const char* szPath)
{
	m_handle = hid_open_path( szPath);
	if( NULL == m_handle)
		return false;

	SetBufferCommand( FTDATA_CMD_INFO, 0, NULL);

	if( !HidFeatureIO())
		return false;

	if( GetDataCount() != (13 + SIZE_SERIALNUMBER))
		return false;
	if( 0 != strcmp( (const char*)GetData(), "locojoy"))
		return false;
	memcpy( m_SerialNumber, GetData() + 13, SIZE_SERIALNUMBER);
	return true;
}

const unsigned char* CUsbHidKey::GetSerialNumber() const
{
	return &m_SerialNumber[0];
}

bool CUsbHidKey::Verify( unsigned char* pKey)
{
	SetBufferCommand( FTDATA_CMD_VERIFY, SIZE_HMAC_KEY, pKey);

	if( !HidFeatureIO())
		return false;
	return 0 != GetData()[0];
}

bool CUsbHidKey::HMAC_MD5( const unsigned char* pData, int nSize, unsigned char* pOutput)
{
	SetBufferCommand( FTDATA_CMD_HMAC_MD5, nSize, pData);
	if( !HidFeatureIO())
		return false;
	if( SIZE_HMAC_KEY != GetDataCount())
		return false;
	memcpy( pOutput, GetData(), SIZE_HMAC_KEY);
	return true;
}

bool CUsbHidKey::Reset()
{
	SetBufferCommand( FTDATA_CMD_RESET, 0, NULL);
	return HidFeatureIO();
}
