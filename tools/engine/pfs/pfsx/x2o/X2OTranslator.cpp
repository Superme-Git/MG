///=============================================================================
/// Product   : PFS
/// Project   : pfsx-x2o
/// Module    : X2OTranslator.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML/Octets Translator
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-1-21 Created.
///=============================================================================

#include "X2OTranslator.h"
#include "../../pfs2.h"
#include "../../../xmlio/xmlio.h"

namespace PFSX {

	namespace X2O {

		class CX2OEncoder : public CEncoder
		{
		public:
			CX2OEncoder() 
				: CEncoder( MakeTypeIDByName('X', '2', 'O') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CX2OEncoder();
			}

			virtual void Delete() { delete this; }
		};


		class CX2ODecoder : public CDecoder 
		{
		public:
			CX2ODecoder ()
				: CDecoder( MakeTypeIDByName('X', '2', 'O') )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CX2ODecoder();
			}

			virtual void Delete() { delete this; }
		};

		class _CX2OTranslatorAutoRegister
		{
		public:
			_CX2OTranslatorAutoRegister()
			{
				CTranslateManager::GetInstance().RegisterEncoder( CX2OEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CX2ODecoder() );
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CX2OTranslatorAutoRegister _autoRegister;
			return PFS::EC_SUCCESS;
		}

		long GetEncoderCount()
		{
			return 1;
		}

		void* GetEncoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CX2OEncoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		long GetDecoderCount()
		{
			return 1;
		}

		void* GetDecoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CX2ODecoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		using PFS::CBaseFile;

		static int _PerformEncode( CBaseFile& source, CBaseFile& destination )
		{
			XMLIO::CFileReader xFileReader;
			int xRet = xFileReader.OpenFile(source);
			if( PFS::EC_SUCCESS != xRet )
			{
				return xRet;
			}

			PFS::CDataBlock	data;
			// �����ԣ�ѹ������Ϊ1/4�ȽϺ������ڴ��xml�ļ������Ա����ڴ�����ͷŲ������ȶ�
			// �����Ƕ���/run/cfg/item.xml
			data.Reserve((size_t)(source.GetSize() >> 2));
			xFileReader.SaveAsOctets( data);
			size_t nRemaining = data.GetSize();
			if ( nRemaining == 0 )
			{
				return PFS::ECX_BADFORMAT;
			}

			size_t nCurrent = 0;
			const unsigned char * pData = (const unsigned char *)data.GetData();
			while ( nRemaining > 0 )
			{
				nCurrent = destination.Write(pData, nRemaining);
				if ( nCurrent == PFS::FILE_SIZE_ERROR )
				{
					break;
				}
			
				nRemaining -= nCurrent;
				pData += nCurrent;
			}
			
			return nRemaining == 0 ? PFS::EC_SUCCESS : PFS::CEnv::GetLastError();
		}

		static int _PerformDecode( CBaseFile& source, CBaseFile& destination )
		{
			XMLIO::CFileReader xFileReader;
			int xRet = xFileReader.OpenFile(source);
			if( PFS::EC_SUCCESS != xRet )
			{
				return xRet;
			}

			std::wstring strData;
			xFileReader.SaveAsString( strData);
			std::wstring::size_type nRemaining = strData.size()*sizeof(std::wstring::value_type);
			if ( nRemaining == 0 )
			{
				// SaveAsString failed!
				return PFS::ECX_BADFORMAT;
			}

			//////////////////////////////////////////////////////////////////////////
			//
			// ˵��: ��ǰ�������е�XMLIO::CFileReaderʵ����ʵ����SaveAsString������
			// ֻ�����XML��ʽ��CFileReader֧��SaveAsString()������
			// ��ˣ������CX2ODecoderʵ�ʲ��޷����OctetsToXML��ת������
			//
			// [1/21/2009 yanghaibo]
			//
			//------------------------------------------------------------------------
			// ��ǰ��XMLIO::CFileReader�Ѿ�ʵ������Զ����Ƹ�ʽ��SaveAsString()������
			//
			// [2010-04-01 yanghaibo] 
			//

			// Write BOM bytes
			destination.Write("\xFF\xFE", 2);
			std::wstring::size_type nCurrent = 0;
			const unsigned char* pData = (const unsigned char*)strData.data();
			while ( nRemaining > 0 )
			{
				nCurrent = destination.Write(pData, nRemaining);
				if(nCurrent == PFS::FILE_SIZE_ERROR)
				{
					break;
				}

				nRemaining -= nCurrent;
				pData += nCurrent;
			}

			return nRemaining == 0 ? PFS::EC_SUCCESS : PFS::CEnv::GetLastError();
		}

		static bool _CopyBaseFile( CBaseFile& source, CBaseFile& dest )
		{
			CBaseFile*	fsrc = &source;
			CBaseFile*	fdst = &dest;
			const int	nBufferSize = 4096;
			unsigned char byteBuffer[nBufferSize];
			bool		bResult = true;
			size_t		s = 0;
			while( s = fsrc->Read( byteBuffer, nBufferSize ) )
			{
				if( PFS::FILE_SIZE_ERROR == s )
					return false;
				s = fdst->Write( byteBuffer, s);
				if( PFS::FILE_SIZE_ERROR == s )
					return false;
			}

			return true;
		}

		int CX2OEncoder::DoEncode()
		{
			try
			{
				return _PerformEncode( GetSource(), GetDestination() );
			}
			catch (int /*ec*/)
			{
				return PFS::ECX_BADFORMAT;
			}			
		}

		int CX2ODecoder::DoDecode()
		{
			try
			{
				if( _CopyBaseFile( GetSource(), GetDestination() ) )
				{
					return PFS::EC_SUCCESS;
				}
				else
				{
					return PFS::CEnv::GetLastError();
				}
			}
			catch (int /*ec*/)
			{
				return PFS::ECX_BADFORMAT;
			}
		}
	}
}