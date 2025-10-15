#pragma once
#include <string>

class CTextLoader
{
public:

	static std::wstring	m_null;

	virtual ~CTextLoader() {}
	virtual bool Load( const std::wstring& file) = 0;
	virtual const std::wstring& GetText( int id)  = 0;
};


class CTextManager
{
	std::wstring	m_buffer;
	CTextLoader*	m_loader;
	
	CTextManager( );
	~CTextManager(void);

public:

	enum FILE_TYPE
	{
		FILE_XML,	// �ı���Դ������xml�ļ�
		FILE_DLL,	// �ı���Դ������dll�ļ�
		FILE_TXT	// �ı���Դ������txt�ļ�
	};

	bool Init( FILE_TYPE type, const std::wstring& file);

	const std::wstring& GetText( int id);

	const std::wstring& FormatText( const wchar_t* pszFormat, va_list* vl);

	std::wstring GetText2( int id);

	static CTextManager& GetInstance()
	{
		static CTextManager s_instance;
		return s_instance;
	}
};

extern const std::wstring& GlobalGetText( int id);
extern std::wstring GlobalGetText2( int id);
extern const std::wstring& __cdecl GlobalFormatText( int id, ...);
