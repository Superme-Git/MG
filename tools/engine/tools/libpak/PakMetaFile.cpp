#include "../../pfs/pfs2.h"
#include "../../xmlio/xmlio.h"
#include "PakMetaFile.h"
#include <iostream>
#include <sstream>

namespace MHZX
{

	CPakMetaFile::CPakMetaFile(void)
	{
	}

	CPakMetaFile::~CPakMetaFile(void)
	{
	}

	bool CPakMetaFile::Open( PFS::CDevice* pDevice )
	{
		PFS::CDeviceFile file;
		if( !file.Open( pDevice, L"pak.meta.xml", PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			return false;
		}
		
		XMLIO::CFileReader reader;
		reader.OpenFile( file );

		XMLIO::CINode iNode;
		if( !reader.GetRootNode( iNode ) )
		{
			return false;
		}


		XMLIO::CNodeList xNodeList;
		iNode.GetChildren( xNodeList );

		//<picture width="192" height="64" x_offset="0" y_offset="0" format="3" storge="0" file="0.b03">
		//	<block width="128" height="64" x_offset="0" y_offset="0" size="32768"/>
		//	<block width="64" height="64" x_offset="128" y_offset="0" size="16384"/>
		//</picture>

		size_t count = iNode.GetChildrenCount();

		for( size_t i = 0; i < count; i ++ )
		{
			XMLIO::CINode xNodePicture;
			iNode.GetChildAt( i, xNodePicture );

			int width = xNodePicture.GetAttributeInteger( L"width", 0 );
			int height = xNodePicture.GetAttributeInteger( L"height", 0 );
			int x_offset = xNodePicture.GetAttributeInteger( L"x_offset", 0 );
			int y_offset = xNodePicture.GetAttributeInteger( L"y_offset", 0 );
			int format = xNodePicture.GetAttributeInteger( L"format", 0 );
			int storage = xNodePicture.GetAttributeInteger( L"storage", 0 );
			std::wstring filename;
			xNodePicture.GetAttribute(L"file", filename);

			std::wstringstream wss;
			wss << L"<picture width=\"" << width << L"\" height=\"" << height
				<< L"\" x_offset=\"" << x_offset << L"\" y_offset=\"" << y_offset 
				<< L"\" format=\"" << format << L"\" storage=\"" << storage
				<< L"\" file=\"" << filename <<L"\">" << std::endl;

			size_t blockcount = xNodePicture.GetChildrenCount();

			for( size_t j = 0; j < blockcount ; j ++ )
			{
				XMLIO::CINode xNodeBlock;
				xNodePicture.GetChildAt( j, xNodeBlock );
				int width = xNodeBlock.GetAttributeInteger( L"width", 0 );
				int height = xNodeBlock.GetAttributeInteger( L"height", 0 );
				int x_offset = xNodeBlock.GetAttributeInteger( L"x_offset", 0 );
				int y_offset = xNodeBlock.GetAttributeInteger( L"y_offset", 0 );
				int size = xNodeBlock.GetAttributeInteger( L"size", 0 );


				wss << L"  <block width=\"" << width
					<< L"\" height=\"" << height
					<< L"\" x_offset=\"" << x_offset 
					<< L"\" y_offset=\"" << y_offset 
					<< L"\" size=\"" << size << L"\"/>" << std::endl;
			}
			wss << L"</block>" << std::endl;

			std::wcout<< wss.str();
		}

		return true;
	}
}