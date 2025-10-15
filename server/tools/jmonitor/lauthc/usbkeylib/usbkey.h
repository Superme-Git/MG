#pragma once

typedef struct hid_device_ hid_device;

class CUsbHidKeyFinder
{
	enum
	{
		VENDOR_ID 	= 0x0111,
		PRODUCT_ID 	= 0x0111,
	};

	std::list<std::string>	m_Paths;
public:
	CUsbHidKeyFinder();
	~CUsbHidKeyFinder();
public:
	void FindKeys();
	int GetKeyCount() const;
	const char* GetFirstKeyPath() const;
};

class CUsbHidKey
{
	enum
	{
		SIZE_FEATURE		= 0x41,
		SIZE_SERIALNUMBER	= 0x08,
		SIZE_HMAC_KEY		= 0x10,

		FEATURE_IO_ENDOUT	= 0x2E,
		FEATURE_IO_ENDIN	= 0xD3,
		FEATURE_IO_UNKNOW	= 0x00,

		FTDATA_CMD_INFO		= 0x01,
		FTDATA_CMD_VERIFY	= 0x02,
		FTDATA_CMD_RESET	= 0x03,
		FTDATA_CMD_HMAC_MD5	= 0x20,
	};

	unsigned char			m_SerialNumber[SIZE_SERIALNUMBER];
	unsigned char			m_pBuffer[SIZE_FEATURE];
	hid_device*				m_handle;
public:
	CUsbHidKey();
	~CUsbHidKey();
private:
	bool HidFeatureIO( int nSleep = 50);
	void SetBufferCommand( unsigned char bCmd, unsigned char nDataCount, const unsigned char* pData);
	unsigned char GetDataCount();
	unsigned char* GetData();
public:
	bool OpenKey( const char* szPath);
	const unsigned char* GetSerialNumber() const;
	bool Verify( unsigned char* pKey);
	bool HMAC_MD5( const unsigned char* pData, int nSize, unsigned char* pOutput);
	bool Reset();
};


