#include "PicPak.h"
#include "../../pfs/pfs2.h"
#include "../../pfs/device.h"
#include "../../xmlio/xmlio.h"
#include "../libdds/include/libdds.h"

#include "PakMetaFile.h"
#include "Decoder/PictureDecoder.h"
#include <iostream>
#include <sstream>

#ifndef MAKELONG
#define MAKELONG(a, b) \
			((LONG)(((WORD)((DWORD_PTR)(a) & 0xffff)) | \
			((DWORD)((WORD)((DWORD_PTR)(b) & 0xffff))) << 16))
#endif

namespace MHZX
{

	CPicPak::CPicPak(void)
	{
		m_bPictureChanged = false;
	}

	CPicPak::~CPicPak(void)
	{
		Close();
	}

	bool CPicPak::Open(const wchar_t * lpszPakPath, bool bCreateNew  )
	{
		if( lpszPakPath == NULL || lpszPakPath[0] == L'\0' )
		{
			// TODO: 
			return false;
		}

		m_strPakPath.assign( lpszPakPath );

		if( !m_device.Mount( m_strPakPath, bCreateNew ? PFS::MT_TRUNCATE|PFS::MT_PREFERDIR : PFS::MT_READONLY ) )
		{
			std::wcerr << L"Invalid Pak file: "<< m_strPakPath << std::endl;
			return false;
		}

		if( bCreateNew )
		{
			return true;
		}

		PFS::CDeviceFile file;
		if( !file.Open( &m_device, L"pak.meta.xml", PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcerr << L"Missing pak.meta.xml! Pak file: "<< m_strPakPath << std::endl;
			return false;
		}

		XMLIO::CFileReader reader;
		reader.OpenFile( file );

		XMLIO::CINode rootNode;
		if( !reader.GetRootNode( rootNode ) )
		{
			std::wcerr << L"Dummy pak.meta.xml! Pak file: "<< m_strPakPath << std::endl;
			return false;
		}

		int version = rootNode.GetAttributeInteger( L"version");
		if( version != 1 && version != 2 )
		{
			// 09.03.16 升级一次版本号,统一图片后缀,去掉图片内的描述信息
			std::wcerr << L"Unsupported version! Pak file: "<< m_strPakPath << L" version : " << version << std::endl;
			return false;
		}

		XMLIO::CNodeList xNodeList;
		rootNode.GetChildren( xNodeList );

		//<picture width="192" height="64" x_offset="0" y_offset="0" format="3" storge="0" file="0.b03">
		//	<block width="128" height="64" x_offset="0" y_offset="0" size="32768"/>
		//	<block width="64" height="64" x_offset="128" y_offset="0" size="16384"/>
		//</picture>

		size_t pictureCount = rootNode.GetChildrenCount();
		m_vPictures.resize( pictureCount, NULL );

		for( size_t i = 0; i < pictureCount; i ++ )
		{
			XMLIO::CINode xNodePicture;
			rootNode.GetChildAt( i, xNodePicture );

			CPicture*& pPic = m_vPictures[i];
			pPic = new CPicture;
			PictureDesc& rPictureDesc = pPic->m_PictureDesc;
			rPictureDesc.version = version; // 1 or 2
			rPictureDesc.width = xNodePicture.GetAttributeInteger( L"width", 0 );
			rPictureDesc.height = xNodePicture.GetAttributeInteger( L"height", 0 );
			rPictureDesc.x_offset = xNodePicture.GetAttributeInteger( L"x_offset", 0 );
			rPictureDesc.y_offset = xNodePicture.GetAttributeInteger( L"y_offset", 0 );
			rPictureDesc.format = xNodePicture.GetAttributeInteger( L"format", -1 );
			
			// BAD attribute name: 'storge' should be 'storage'. From MIX engine!
			rPictureDesc.storage = xNodePicture.GetAttributeInteger( L"storge", 0 );

			xNodePicture.GetAttribute(L"file", rPictureDesc.filename);

			size_t blockcount = xNodePicture.GetChildrenCount();
			pPic->m_vBlocks.resize( blockcount );
			for( size_t j = 0; j < blockcount ; j ++ )
			{
				XMLIO::CINode xNodeBlock;
				xNodePicture.GetChildAt( j, xNodeBlock );

				BlockDesc & rBlockDesc = pPic->m_vBlocks[j];
				rBlockDesc.width = xNodeBlock.GetAttributeInteger( L"width", 0 );
				rBlockDesc.height = xNodeBlock.GetAttributeInteger( L"height", 0 );
				rBlockDesc.x_offset = xNodeBlock.GetAttributeInteger( L"x_offset", 0 );
				rBlockDesc.y_offset = xNodeBlock.GetAttributeInteger( L"y_offset", 0 );
				rBlockDesc.size = xNodeBlock.GetAttributeInteger( L"size", 0 );
			}
		}

		return true;
	}

	bool CPicPak::InitPicture( size_t index )
	{
		CPicture* pPic = m_vPictures[index];
		
		if( pPic->m_PictureDesc.filename.empty() )
		{
			return false;
		}

		PFS::CDeviceFile file;
		if( !file.Open( &m_device, pPic->m_PictureDesc.filename, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcerr << L" Open picture file failed! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			return false;
		}

		IPictureDecoder * pDecoder = CPictureDecoderFactory::CreatePictureDecoder(
			MAKELONG( pPic->m_PictureDesc.format, pPic->m_PictureDesc.storage ) );
		if( pDecoder == NULL )
		{
			std::wcerr << L" Missing decoder for ficture(format: " << pPic->m_PictureDesc.format 
				<< L", storage:" << pPic->m_PictureDesc.storage 
				<< ")! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			return false;
		}
		
		if( !pDecoder->Init(this))
		{
			std::wcerr << L" Init Decoder for file failed! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			return false;
		}

		if( !pDecoder->Decode( file, *pPic ) )
		{
			std::wcerr << L" Decode picture failed! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			pDecoder->Release();
			return false;
		}

		pDecoder->Release();
		return true;
	}

	CPicture* CPicPak::GetPicture( size_t index )
	{
		return m_vPictures[index];
	}

	bool CPicPak::AddPicture( CPicture* pPicture )
	{
		m_bPictureChanged = true;
		m_vPictures.push_back( pPicture );
		return true;
	}

	bool CPicPak::ReplacePicture( size_t index, CPicture* pPicture )
	{
		if( m_vPictures.size() <= index )
		{
			return false;
		}

		CPicture* pPictureOld = m_vPictures[ index ];
		m_vPictures[ index ] = pPicture;

		if( pPictureOld != NULL && pPictureOld != pPicture )
		{
			delete pPictureOld;
		}

		m_bPictureChanged = true;

		return true;
	}

	bool CPicPak::CommitMeta()
	{
		// update pak.meta.xml
		PFS::CDeviceFile file;
		if( !file.Open( &m_device, L"pak.meta.xml", PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY  ) )
		{
			std::wcerr << L"Create pak.meta.xml failed! Pak file: "<< m_strPakPath << std::endl;
			return false;
		}

		XMLIO::CFileWriter writer;

		XMLIO::CONode xRootNode;
		if( !writer.GetRootNode(xRootNode) )
		{
			return false;
		}

		xRootNode.SetAttribute( L"version", L"2" );
		
		//<picture width="192" height="64" x_offset="0" y_offset="0" format="3" storge="0" file="0.b03">
		//	<block width="128" height="64" x_offset="0" y_offset="0" size="32768"/>
		//	<block width="64" height="64" x_offset="128" y_offset="0" size="16384"/>
		//</picture>

		size_t pictureCount = GetPictureCount();
				
		for( size_t i = 0; i < pictureCount; i ++ )
		{
			XMLIO::CONode xPictureNode;
			if( !writer.CreateElement( xRootNode, L"picture", xPictureNode ) )
			{
				return false;
			}
			
			CPicture*& pPic = m_vPictures[i];
			PictureDesc& rPictureDesc = pPic->m_PictureDesc;
			xPictureNode.SetAttribute( L"width", rPictureDesc.width );
			
			xPictureNode.SetAttribute( L"height", rPictureDesc.height );

			xPictureNode.SetAttribute( L"x_offset", rPictureDesc.x_offset );

			xPictureNode.SetAttribute( L"y_offset", rPictureDesc.y_offset );

			xPictureNode.SetAttribute( L"format", rPictureDesc.format );

			// BAD attribute name: 'storge' should be 'storage'. From MIX engine!
			xPictureNode.SetAttribute( L"storge", rPictureDesc.storage ); 

			if( !rPictureDesc.filename.empty() )
			{
				xPictureNode.SetAttribute( L"file", rPictureDesc.filename );
			}

			size_t blockcount = pPic->m_vBlocks.size();
			
			for( size_t j = 0; j < blockcount ; j ++ )
			{
				XMLIO::CONode xBlockNode;
				if( !writer.CreateElement( xPictureNode, L"block", xBlockNode ) )
				{
					return false;
				}
				
				BlockDesc & rBlockDesc = pPic->m_vBlocks[j];
				xBlockNode.SetAttribute( L"width", rBlockDesc.width );
				xBlockNode.SetAttribute( L"height", rBlockDesc.height );
				xBlockNode.SetAttribute( L"x_offset", rBlockDesc.x_offset );
				xBlockNode.SetAttribute( L"y_offset", rBlockDesc.y_offset );
				xBlockNode.SetAttribute( L"size", rBlockDesc.size );
			}
		}

		return !writer.Save( file );
	}

	void CPicPak::Close()
	{

		if( m_bPictureChanged )
		{
			CommitMeta();
		}

		size_t pictureCount = GetPictureCount();
		for( size_t i = 0; i < pictureCount; i ++ )
		{
			delete m_vPictures[i];
		}

		m_vPictures.clear();

		m_device.Unmount();
	}

}