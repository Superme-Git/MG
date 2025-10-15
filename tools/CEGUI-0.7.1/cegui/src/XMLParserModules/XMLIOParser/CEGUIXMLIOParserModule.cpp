/********************************************************************
	created:	2011/04/20
	created:	20:4:2011   15:43
	filename: 	CEGUIXMLIOParserModule.cpp
	file path:	cegui\src\XMLParserModules\XMLIOParser
	file base:	CEGUIXMLIOParserModule
	file ext:	cpp
	purpose:	
*********************************************************************/

#include "CEGUIXMLIOParserModule.h"
#include "CEGUIXMLIOParser.h"
#include "Nuclear.h"
CEGUI::XMLParser* createParser(void)
{
#ifdef FORCEGUIEDITOR

	bool bWritableDefault = false;
	bool bApplictionInBuild = true;
	{
		wchar_t workdir[MAX_PATH];

		// yangyinyu change begin!
		int nchar = GetCurrentDirectoryW(MAX_PATH, workdir);

		if (0 == nchar)
			return NULL;
		int sign = bApplictionInBuild ? 1 : 0;
		for (int i = nchar - 1; i >= 0; --i)
		{
			if (workdir[i] == L'/' || workdir[i] == L'\\')
			{
				if (!sign)
				{
					sign = 1;
				}
				else {
					break;
				}
			}
		}
		// yangyinyu change end!

		if (!PFS::CEnv::Mount( L"/root", workdir, PFS::FST_NATIVE, bWritableDefault ? PFS::MT_WRITABLE : PFS::MT_RUNTIME))
		{
			return NULL;
		}

		XMLIO::CFileReader fr;
		std::wstring wdir = workdir;
		wdir = wdir.substr(0, wdir.find_last_of(L"\\"));
		std::wstring xmlpath = wdir + L"\\res\\cfg\\mount.xml";
		int nError = fr.OpenNativeFile(xmlpath);

		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			return NULL;
		}

		XMLIO::CINode	root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			return NULL;
		}

		XMLIO::CNodeList mountnl;
		root.GetChildren(mountnl);
		bool r = true;

		for( XMLIO::CNodeList::iterator mountIt = mountnl.begin(); mountIt != mountnl.end(); ++mountIt)
		{
			XMLIO::CNodeList itemNl;
			XMLIO::CINode& mountNode = *mountIt;
			if (mountNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			std::wstring rootName;
			if (!mountNode.GetAttribute(L"root", rootName))
			{
				r = false;
				continue;
			}

			std::wstring dir;
			std::wstring pfs;
			bool usepfs = false;
			bool dbg = (mountNode.GetAttributeInteger(L"dbg") == 1);
			PFS::MOUNT_TYPE mountType = bWritableDefault ? PFS::MT_WRITABLE : mountNode.GetAttributeInteger(L"mt");
			if (mountNode.GetAttribute(L"pfs", pfs))
			{
				if (PFS::CFile::IsFileExisting(pfs))
				{
					usepfs = PFS::CEnv::Mount( rootName, pfs, bWritableDefault ? PFS::FST_ZIP : PFS::FST_RUNZIP, mountType);
				}
			}
			if (!usepfs)
			{
				if (!mountNode.GetAttribute(L"dir", dir))
				{
					r = false;
					continue;
				}
				if (!PFS::CEnv::Mount( rootName, dir, PFS::FST_NATIVE, mountType))
				{
					if (!dbg)
					{
						r = false;
					}
					continue;
				}
			}
		}
		fr.CloseFile();
		return new CEGUI::XMLIOParser();
	}
#else
    return new CEGUI::XMLIOParser();
#endif
}

void destroyParser(CEGUI::XMLParser* parser)
{
    delete parser;
}
