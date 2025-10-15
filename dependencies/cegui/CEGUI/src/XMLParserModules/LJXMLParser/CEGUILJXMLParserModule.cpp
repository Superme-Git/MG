#include "CEGUILJXMLParserModule.h"
#include "CEGUILJXMLParser.h"
#include "Nuclear.h"
CEGUI::XMLParser* createParser(void)
{
//#ifdef FORCEGUIEDITOR
//	bool bWritableDefault = false;
//	bool bApplictionInBuild = true;
//	{
//		wchar_t workdir[MAX_PATH];
//		
//		int nchar = GetCurrentDirectoryW(MAX_PATH, workdir);
//
//		if( 0 ==  nchar )
//			return NULL;
//		int sign = bApplictionInBuild?1:0;
//		for(int i=nchar-1; i>=0; --i)
//		{
//			if( workdir[i] == L'/' || workdir[i] == L'\\' )
//			{
//				if (!sign)
//				{
//					sign = 1;
//				} else {
//					break;
//				}
//			}
//		}
//
//		if (!LJFM::LJFMOpen::Mount( L"/root", workdir, LJFM::FST_NATIVE, bWritableDefault ? LJFM::MT_WRITABLE : LJFM::MT_RUNTIME))
//		{
//			return NULL;
//		}
//
//		LJ_XML::CFileReader fr;
//		std::wstring wdir = workdir;
//		wdir = wdir.substr(0, wdir.find_last_of(L"\\"));
//		std::wstring xmlpath = wdir + L"\\res\\cfg\\mount.xml";
//		int nError = fr.OpenNativeFile(xmlpath);
//
//		if( LJ_XML::EC_SUCCESS != nError)
//		{
//			fr.CloseFile();
//			return NULL;
//		}
//
//		LJ_XML::CINode	root;
//		if (!fr.GetRootNode(root))
//		{
//			fr.CloseFile();
//			return NULL;
//		}
//
//		LJ_XML::CNodeList mountnl;
//		root.GetChildren(mountnl);
//		bool r = true;
//
//		for( LJ_XML::CNodeList::iterator mountIt = mountnl.begin(); mountIt != mountnl.end(); ++mountIt)
//		{
//			LJ_XML::CNodeList itemNl;
//			LJ_XML::CINode& mountNode = *mountIt;
//			if (mountNode.GetType() != LJ_XML::NT_ELEMENT)
//				continue;
//			std::wstring rootName;
//			if (!mountNode.GetAttribute(L"root", rootName))
//			{
//				r = false;
//				continue;
//			}
//
//			std::wstring dir;
//			std::wstring pfs;
//			bool usepfs = false;
//			bool dbg = (mountNode.GetAttributeInteger(L"dbg") == 1);
//			LJFM::MOUNT_TYPE mountType = bWritableDefault ? LJFM::MT_WRITABLE : mountNode.GetAttributeInteger(L"mt");
//			if (mountNode.GetAttribute(L"pfs", pfs))
//			{
//				if (LJFM::LJFMF::IsFileExisting(pfs))
//				{
//					usepfs = LJFM::LJFMOpen::Mount( rootName, pfs, bWritableDefault ? LJFM::FST_ZIP : LJFM::FST_RUNZIP, mountType);
//				}
//			}
//			if (!usepfs)
//			{
//				if (!mountNode.GetAttribute(L"dir", dir))
//				{
//					r = false;
//					continue;
//				}
//				if (!LJFM::LJFMOpen::Mount( rootName, dir, LJFM::FST_NATIVE, mountType))
//				{
//					if (!dbg)
//					{
//						r = false;
//					}
//					continue;
//				}
//			}
//		}
//		fr.CloseFile();
//		return new CEGUI::LJXMLParser();
//	}
//#else
    return new CEGUI::LJXMLParser();
//#endif
}

void destroyParser(CEGUI::XMLParser* parser)
{
    delete parser;
}
