#include "../IImageEncoder.h"
#include "PNGEncoder.h"
#include "BMPEncoder.h"
#include "TGAEncoder.h"
#include "ImageEncoderHelper.h"

#include <string.h>

namespace MHZX
{
	template< class BaseClass >
	class CDefaultEncoderImpl : public BaseClass
	{
	public:
		CDefaultEncoderImpl()
		{
			m_policy = 0;
		}

		virtual bool Init( NEW_FILE_NAME_POLICY policy )
		{
			m_policy = policy;
			return true;
		}

		virtual bool GenerateNewFileName( 
			unsigned int fileid,
			const std::wstring& srcname,
			std::wstring& newname )
		{
			const wchar_t* pszExt = GetDefaultFileExtension();
			unsigned int ext_policy = ( m_policy & EXTENSION_POLICY_MASK );
		
			if( m_policy & NEW_FILE_NAME_BY_ID )
			{
				newname.resize(64);
				int len = swprintf( (wchar_t*)newname.data(), 64, L"%lu", fileid );
				newname.resize( len );

				unsigned int ext_policy = ( m_policy & EXTENSION_POLICY_MASK );
				if( ext_policy == TRIM_SRC_EXTENSION)
				{
					// do nothing!
				}
				else if( ext_policy == APPEND_NEW_EXTENSION )
				{
					// append old and new.
					const wchar_t* pszExt = GetDefaultFileExtension();
					std::wstring oldext;
					HELPER::CImageEncoderHelper::RetrieveFileExtension(srcname, oldext);
					newname += oldext + pszExt;
				}
				else if( ext_policy == REPLACE_SRC_EXTENSION )
				{
					// append new
					const wchar_t* pszExt = GetDefaultFileExtension();
					newname += pszExt;
				}
				else if( ext_policy == COPY_SRC_EXTENSION )
				{
					std::wstring oldext;
					HELPER::CImageEncoderHelper::RetrieveFileExtension(srcname, oldext);
					newname += oldext;
				}
			}
			else
			{
				if( ext_policy == TRIM_SRC_EXTENSION)
				{
					HELPER::CImageEncoderHelper::TrimFileExtension( srcname, newname );
				}
				else if( ext_policy == APPEND_NEW_EXTENSION )
				{
					newname = srcname + pszExt;
				}
				else if( ext_policy == REPLACE_SRC_EXTENSION )
				{
					HELPER::CImageEncoderHelper::ReplaceFileExtension( srcname, newname, pszExt );
				}
				else if( ext_policy == COPY_SRC_EXTENSION )
				{
					newname = srcname;
				}
			}

			return true;
		}

	private:
		NEW_FILE_NAME_POLICY m_policy;
	};

	template < class MyBaseClass >
	class CStaticSingletonTemplate : public MyBaseClass
	{
	public:
		typedef MyBaseClass __mybase;

		friend CImageEncoderFactory;

		static CStaticSingletonTemplate<__mybase> & GetInstance()
		{
			static CStaticSingletonTemplate<__mybase> instance;
			return instance;
		}
		virtual void Release()
		{
			// Do not need to delete static instance.
		}
	};

	IImageEncoder* _CreateImageEncoder(const wchar_t *image_type)
	{
		if( _wcsicmp( image_type, L"png" ) == 0 )
		{
			static CStaticSingletonTemplate< CDefaultEncoderImpl< CPNGEncoder > > instance;
			return &instance;
		}
		else if( _wcsicmp( image_type, L"bmp" ) == 0 )
		{
			static CStaticSingletonTemplate< CDefaultEncoderImpl< CBMPEncoder > > instance;
			return &instance;
		}
		else if( _wcsicmp( image_type, L"tga" ) == 0 )
		{
			static CStaticSingletonTemplate< CDefaultEncoderImpl < CTGAEncoder> > instance;
			return &instance;
		}

		return NULL;
	}

	IImageEncoder* CImageEncoderFactory::CreateImageEncoder(const wchar_t *image_type)
	{
		IImageEncoder* encoder = _CreateImageEncoder(image_type);
		if( encoder != NULL )
		{
			return encoder;
		}

		// Try again using multiplex extension. 
		// Just for supporting extensions like "pic.png". 
		const wchar_t *image_type2 = wcsrchr(image_type, L'.');
		if( image_type2 != NULL )
		{
			image_type2 ++;
			encoder = _CreateImageEncoder(image_type2);
			if( encoder != NULL )
			{
				if( image_type[0] == L'.' )
					encoder->SetDefaultFileExtension( image_type );
				else
				{
					std::wstring strExtension(L".");
					strExtension.append(image_type);
					encoder->SetDefaultFileExtension( strExtension.c_str() );
				}
			}
		}

		return encoder;
	}
}
