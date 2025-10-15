#include "stdafx.h"
#include "xmlmetafileio.h"

#include <atlbase.h>
#include "../../../xmlio/xmlio.h"

namespace XMLMETAFILE {

void CMetaFileRead::DoParseData( const XMLIO::CINode& rn, PFS::CMetaInfoMap& mm)
{
	size_t count = rn.GetChildrenCount();
	XMLIO::CINode child;
	for( size_t i=0; i<count; i++)
	{
		rn.GetChildAt( i, child);
		DoParseOneData( child, mm);
	}
}

void CMetaFileRead::DoParseOneData( const XMLIO::CINode& rn, PFS::CMetaInfoMap& mm)
{
	PFS::CMetaInfo meta;
	meta.SetFileHash( CTransfer::String2Hash( rn.GetAttribute( FILEHASH)));
	meta.SetFileDownloadURI( rn.GetAttribute( DOWNLOADURL));
	PFS::CDataBlock data = CTransfer::String2Data( rn.GetAttribute( ATTRIBUTES));
	PFS::FILEPFSXATTRIB attr;
	memcpy( &attr, data.GetData(), sizeof( PFS::FILEPFSXATTRIB));
	meta.SetFilePfsxAttributes( attr);
	meta.SetFileSize( CTransfer::String2FileSize( rn.GetAttribute( FILESIZE)));
	meta.SetMethod( CTransfer::String2Int( rn.GetAttribute( METHOD)));
	mm.insert( std::make_pair( rn.GetAttribute( FILENAME), meta));
}

bool CMetaFileRead::DoRead( PFS::CBaseFile& basefile, PFS::CMetaInfoMap& mm)
{
	//MSXMLDOM::CReader reader;
	//if( EC_SUCCESS == reader.OpenFile( basefile))
	PFS::CDataBlock data;
	data.Resize( (size_t)basefile.GetSize());
	if( basefile.Read( data.GetData(), data.GetSize()) != data.GetSize())
		return false;
	return DoRead( data, mm);
}

bool CMetaFileRead::DoRead( PFS::CDataBlock& data, PFS::CMetaInfoMap& mm)
{
	XMLIO::CFileReader reader;
	if( PFS::EC_SUCCESS == reader.OpenData( data))
	{
		XMLIO::CINode root;
		reader.GetRootNode(root);
		DoParseData( root, mm);
		return true;
	}
	return false;
}

void CMetaFileWrite::DoDumpFile( XMLIO::CONode& wn, const PFS::CMetaInfoMap& mm)
{
	XMLIO::CONode child;
	for( PFS::CMetaInfoMap::const_iterator it = mm.begin(); it != mm.end(); ++ it)
	{
		wn.CreateSubElement( L"f", child);
		DoDumpOneMetaInfo( child, it->first, it->second);
	}
}

void CMetaFileWrite::DoDumpOneMetaInfo( XMLIO::CONode& wn, const std::wstring& filename, const PFS::CMetaInfo& mi)
{
	wn.SetAttribute( FILENAME, filename);
	std::wstring temp;
	CTransfer::FileSize2String( mi.GetFileSize(), temp);
	wn.SetAttribute( FILESIZE, temp);
	CTransfer::Int2String( mi.GetMethod(), temp);
	wn.SetAttribute( METHOD, temp);
	PFS::FILEPFSXATTRIB attr = mi.GetFilePfsxAttributes();
	PFS::CDataBlock attribute( &attr, sizeof( PFS::FILEPFSXATTRIB));
	CTransfer::Data2String( attribute, temp);
	wn.SetAttribute( ATTRIBUTES, temp);
	wn.SetAttribute( DOWNLOADURL, mi.GetFileDownloadURI());
	CTransfer::Hash2String( mi.GetFileHash(), temp);
	wn.SetAttribute( FILEHASH, temp);
}

bool CMetaFileWrite::DoWrite( PFS::CBaseFile& basefile, const PFS::CMetaInfoMap& mm)
{
	XMLIO::CFileWriter write;
	XMLIO::CONode root;
	write.GetRootNode(root);
	DoDumpFile(root, mm);
	return write.Save( basefile) == PFS::EC_SUCCESS;
}

bool CMetaFileWrite::DoWriteData( PFS::CDataBlock& data, const PFS::CMetaInfoMap& mm)
{
	XMLIO::CFileWriter write;
	XMLIO::CONode root;
	write.GetRootNode(root);
	DoDumpFile(root, mm);
	return write.SaveData( data) == PFS::EC_SUCCESS;
}
}