
#include <stdarg.h>

#include <map>
#include <list>
#include <vector>
#include <string>
#include <assert.h>
#include <conio.h>

#include "../../pfs/pfs.h"
#include "../xmlio.h"

#include <windows.h>
#include <mmsystem.h>

#pragma comment( lib, "winmm.lib")

void PrintTab( int nTab, const wchar_t* info, ...)
{
	for( int i = 0; i < nTab; i ++)
		printf( "\t");

	va_list		vl;
	va_start( vl, info);
	vwprintf( info, vl);
	va_end( vl);
}

void DumpNode( const XMLIO::CINode& node, int nTab = 0)
{
	PrintTab( nTab, L" node name = %s\n", node.GetName().c_str());

	XMLIO::CAttributesNames	anames;
	node.GetAttributeNames(anames);
	std::wstring value;
	for( XMLIO::CAttributesNames::iterator it = anames.begin(); it != anames.end(); ++ it)
	{
		std::wstring& onename = *it;

		node.GetAttribute( onename, value);
		PrintTab( nTab, L" node attribute %s = %s\n", onename.c_str(), value.c_str());
	}
	std::wstring thistext = node.GetText();
	if( !thistext.empty())
		PrintTab( nTab, L" node text = %s\n", thistext.c_str());
	thistext = node.GetSubText();
	if( !thistext.empty())
		PrintTab( nTab, L" node subtext = %s\n", thistext.c_str());

	XMLIO::CNodeList nl;
	node.GetChildren(nl);
	for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		DumpNode( *it, nTab + 1);
}

int CheckXML( const std::wstring& filename)
{
	XMLIO::CFileReader		fr;
	int nError = fr.OpenFile( filename);
	if( XMLIO::EC_SUCCESS != nError)
		return nError;

	XMLIO::CINode	root;
	fr.GetRootNode(root);
	DumpNode( root);

	{
		PFS::CDataBlock	data;
		fr.SaveAsOctets( data);
		PFSX::CNativeFile		nf;
		nf.Open( L"d:\\temp\\outbin.xml", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);
		nf.Write( data.GetData(), data.GetSize());
	}
	{
		XMLIO::CFileReader		fr;
		int nError = fr.OpenNativeFile( L"d:\\temp\\outbin.xml");
		XMLIO::CINode	root;
		fr.GetRootNode(root);
		DumpNode( root );
	}
	fr.CloseFile();
	return XMLIO::EC_SUCCESS;
}

int CheckNativeXML( const std::wstring& filename)
{
	XMLIO::CFileReader		fr;
	int nError = fr.OpenNativeFile( filename);
	if( XMLIO::EC_SUCCESS != nError)
		return nError;

	XMLIO::CINode	root;
	fr.GetRootNode(root);
	
	DumpNode( root);

	fr.CloseFile();
	return XMLIO::EC_SUCCESS;
}

int CreateXML()
{
	XMLIO::CFileWriter		fw;

	XMLIO::CONode node;
	fw.GetRootNode(node);
	
	XMLIO::CONode subnode;
	fw.CreateElement( node, L"test", subnode);
	subnode.SetAttribute( L"test1", L"ou<t1");
	subnode.SetAttribute( L"test2", L"ou>t2");
	subnode.SetAttribute( L"test3", L"");

	std::wstring		strtest = L"asf1as;d&f.,.a,sf<>?..,\\/";
	fw.InsertCData( subnode, strtest);
	fw.InsertText( subnode, strtest);

	XMLIO::CONode subnode11;
	fw.CreateElement( subnode, L"test11", subnode11);
	subnode11.SetAttribute( L"test11", L"a11");
	subnode11.SetAttribute( L"haha", L"hehe");
	subnode11.SetAttribute( L"test3", L"a33");
	subnode11.InsertText( L"testtext");

	XMLIO::CONode subnode1;
	fw.CreateElement( node, L"test1", subnode1);
	subnode1.SetAttribute( L"haha1", L"hehe1");

	XMLIO::CONode subnode2;
	fw.CreateElement( node, L"test2", subnode2);
	subnode2.SetAttribute( L"haha2", L"hehe2");

	PFS::CDataBlock data;
	if( fw.SaveDataWithBOM(data) != 0 )
	{
		return 1;
	}

	XMLIO::CFileReader fr;
	if( !fw.GetAsReader( fr ) )
	{
		return 2;
	}

	XMLIO::CINode inode;
	if( !fr.GetRootNode( inode ) )
	{
		return 3;
	}

	DumpNode( inode );

	XMLIO::CFileWriter		fw2;
	fr.GetAsWriter( fw2 );
	
	XMLIO::CONode subnode3;
	fw2.CreateElement( node, L"test3", subnode3);
	subnode3.SetAttribute( L"haha3", L"hehe3");

	std::wstring xmlString;
	fw2.SaveAsString( xmlString );
	
	_putws( L"result xml:" );
	_putws( xmlString.c_str() );

	fw2.SaveNative( L"D:\\temp\\fw2.xml");

	PFS::CDataBlock	data2;
	fw2.SaveAsOctets( data2);
	PFSX::CNativeFile nf;
	nf.Open( L"d:\\temp\\fw2.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);
	nf.Write( data2.GetData(), data2.GetSize());

	return 0;
}

int CreateNativeXML()
{
	XMLIO::CFileWriter		fw;

	XMLIO::CONode node;
	fw.GetRootNode( node );

	XMLIO::CONode subnode;
	fw.CreateElement( node, L"test", subnode );
	subnode.SetAttribute( L"test", L"out");
	subnode.SetAttribute( L"test2", L"out1");

	std::wstring		strtest = L"asf1asdf.,.a,sf<>?..,\\/";
	fw.InsertCData( subnode, strtest);
	fw.InsertText( subnode, strtest);

	return fw.SaveNative( L"d:\\temp\\out.xml");
}

int HugeNativeXMLSave()
{
	XMLIO::CFileReader		fr;

	int nError = fr.OpenNativeFile( L"d:\\temp\\item_armor.xml");
//	int nError = fr.OpenNativeFile( L"d:\\temp\\out.xml");
	if( nError)
		return nError;
	{
		PFS::CDataBlock	data;
		fr.SaveAsOctets( data);
		PFSX::CNativeFile		nf;
		if( !nf.Open( L"d:\\temp\\outbin.xml", PFS::FM_CREAT, PFS::FA_WRONLY))
			return PFS::CEnv::GetLastError();
		if( data.GetSize() != nf.Write( data.GetData(), data.GetSize()))
			return PFS::CEnv::GetLastError();
	}
	return 0;
}

int HugeNativeXMLTest()
{
	DWORD	dwTime = timeGetTime();
	if( 1)
	{
		XMLIO::CFileReader		fr;

		DWORD	dwTime = timeGetTime();
		int nError = fr.OpenNativeFile( L"d:\\temp\\item_armor.xml");
		if( nError)
			return nError;
		dwTime = timeGetTime() - dwTime;
		printf( "OpenNativeFile == %d ==\n", dwTime);

		dwTime = timeGetTime();
		XMLIO::CINode root;
		fr.GetRootNode( root );
		size_t scount = root.GetChildrenCount();
		XMLIO::CINode sub;
		for( size_t i = 0; i < scount; i ++)
		{
			root.GetChildAt( i, sub );
		}
		dwTime = timeGetTime() - dwTime;
		printf( "GetChildAt == %d ==\n", dwTime);
		
		dwTime = timeGetTime();
		XMLIO::CNodeList nl;
		root.GetChildren( nl);
		dwTime = timeGetTime() - dwTime;
		printf( "GetChildren == %d ==\n", dwTime);
	}
	dwTime = timeGetTime() - dwTime;
	printf( "item_armor.xml %d\n", dwTime);
	dwTime = timeGetTime();
	if( 1)
	{
		XMLIO::CFileReader		fr;

		DWORD	dwTime = timeGetTime();

		int nError = fr.OpenNativeFile( L"d:\\temp\\outbin.xml");
		if( nError)
			return nError;
		dwTime = timeGetTime() - dwTime;
		printf( "OpenNativeFile == %d ==\n", dwTime);

		dwTime = timeGetTime();
		XMLIO::CINode root;
		fr.GetRootNode( root );
		size_t scount = root.GetChildrenCount();
		XMLIO::CINode sub;
		for( size_t i = 0; i < scount; i ++)
		{
			root.GetChildAt( i, sub );
		}
		dwTime = timeGetTime() - dwTime;
		printf( "GetChildAt == %d ==\n", dwTime);

		dwTime = timeGetTime();
		XMLIO::CNodeList nl;
		root.GetChildren( nl);
		dwTime = timeGetTime() - dwTime;
		printf( "GetChildren == %d ==\n", dwTime);
	}
	dwTime = timeGetTime() - dwTime;
	printf( "outbin.xml %d\n", dwTime);
	return 0;
}

int TestTranslatorX2O()
{
	int iRet = CreateXML();
	if ( iRet != 0 )
	{
		return iRet;
	}

	XMLIO::CFileReader fr;
	if( (iRet = fr.OpenFile( L"D:\\temp\\fw2.xml" ) ) != 0 )
	{
		return iRet;
	}

	PFS::CDataBlock	data;
	fr.SaveAsOctets( data);
	PFSX::CNativeFile nf;
	nf.Open( L"d:\\temp\\fw2.xml.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);
	nf.Write( data.GetData(), data.GetSize());

	return 0;
}

int TestTranslatorO2X()
{
	int iRet = 0;
	XMLIO::CFileReader fr;
	if( (iRet = fr.OpenFile( L"D:\\temp\\fw2.xml.bin" ) ) != 0 )
	{
		return iRet;
	}

	std::wstring str;
	fr.SaveAsString( str );
	PFSX::CNativeFile nf;
	nf.Open( L"d:\\temp\\fw2.xml.bin.xml", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);
	wchar_t BOM = 0xFEFF;
	nf.Write( &BOM, 2 );
	nf.Write( str.c_str(), str.length()*2 );
	nf.Close();

	return 0;
}

int main1( int argc, char* argv[])
{
	if( PFS::CEnv::Mount( L"/run", L"d:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE))
	{
		int nError = CheckXML( L"/run/out.xml");
		if( XMLIO::EC_SUCCESS != nError)
			printf( "CheckXML = %d\n", nError);
		nError = CreateXML();
		if( XMLIO::EC_SUCCESS != nError)
			printf( "CheckXML = %d\n", nError);

		PFS::CEnv::Unmount( L"/run");
	}

	if( 1 )
	{
		int nError = CheckNativeXML( L"d:\\temp\\outbin.xml");
		if( XMLIO::EC_SUCCESS != nError)
			printf( "CheckNativeXML = %d\n", nError);
		nError = CreateNativeXML();
		if( XMLIO::EC_SUCCESS != nError)
			printf( "CreateNativeXML = %d\n", nError);
	}

	if( 1 )
	{
		int nError = HugeNativeXMLSave();
		if( XMLIO::EC_SUCCESS != nError)
			printf( "HugeNativeXMLSave = %d\n", nError);
	}

	if( 1 )
	{
		int nError = HugeNativeXMLTest();
		if( XMLIO::EC_SUCCESS != nError)
			printf( "HugeNativeXMLTest = %d\n", nError);
	}

	printf( "finish!\n");
	_getch();
	return 0;
}

int main2( int argc, char* argv[])
{
	int iRet = 0;
	if( PFS::CEnv::Mount( L"/run", L"d:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE))
	{
		iRet = CreateXML();

		PFS::CEnv::Unmount( L"/run");
	}

	return iRet;
}

int main( int argc, char* argv[])
{
	int iRet = 0;
	if( PFS::CEnv::Mount( L"/run", L"d:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE))
	{
		iRet = TestTranslatorX2O();

		if(iRet==0)
			iRet = TestTranslatorO2X();

		PFS::CEnv::Unmount( L"/run");
	}

	return iRet;
}