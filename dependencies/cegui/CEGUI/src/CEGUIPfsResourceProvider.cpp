/********************************************************************
	created:	2011/03/08
	created:	8:3:2011   10:15
	filename: 	CEGUIPfsResourceProvider.cpp
	file path:	cegui\src
	file base:	CEGUIPfsResourceProvider
	file ext:	cpp
	purpose:	pfs\u63A5\u53E3
*********************************************************************/

#include "CEGUIPfsResourceProvider.h"
#include "CEGUIExceptions.h"
//#include "segmclient.h"
#include <algorithm>

#if defined(__WIN32__) || defined(_WIN32)
#   include <io.h>
#else
#   include <sys/types.h>
#   include <sys/stat.h>
#   include <dirent.h>
#   include <fnmatch.h>
#endif
// Start of CEGUI namespace section
namespace CEGUI
{
#if (defined PUBLISHED_VERSION) && !(defined FORCEGUIEDITOR)
	void PFSResourceProvider::loadRawDataContainer(const String& filename, LJFM::LJFMID& output, const String& resourceGroup)
	{
		std::string file(filename.c_str());
		if (filename.empty())
		{
			throw InvalidRequestException(
				"PFSResourceProvider::load - Filename supplied for data loading must be valid");
		}

		String final_filename(getFinalFilename(filename, resourceGroup));
		std::wstring pfsname = GUIStringToWString(final_filename);
		std::transform(pfsname.begin(),pfsname.end(),pfsname.begin(),::tolower);

		LJFM::LJFMF Afile;			
		if( !Afile.Open(pfsname, LJFM::FM_EXCL, LJFM::FA_RDONLY ))
			return ;
		int file_size = (int)Afile.GetSize();
		if( file_size == 0 ) return ;
		output = Afile.GetImage();
	}
#else
	void PFSResourceProvider::loadRawDataContainer(const String& filename, RawDataContainer& output, const String& resourceGroup)
    {
		std::string file(filename.c_str());
		if (filename.empty())
		{
			throw InvalidRequestException(
				"PFSResourceProvider::load - Filename supplied for data loading must be valid");
		}

		String final_filename(getFinalFilename(filename, resourceGroup));
		std::wstring pfsname = GUIStringToWString(final_filename);
		std::transform(pfsname.begin(), pfsname.end(), pfsname.begin(), ::tolower);

		LJFM::LJFMF Afile;
		if (!Afile.Open(pfsname, LJFM::FM_EXCL, LJFM::FA_RDONLY))
			return;
		int file_size = (int)Afile.GetSize();
		if (file_size == 0) return;
		//output = Afile.GetImage();
		CEGUI::uint8* data = new CEGUI::uint8[Afile.GetImage().GetSize()];
		memcpy(data, Afile.GetImage().GetData(), Afile.GetSize());
		output.setData(data);
		output.setSize(Afile.GetImage().GetSize());
    }
#endif
	void PFSResourceProvider::unloadRawDataContainer(RawDataContainer& data)
	{
		//uint8* ptr = data.getDataPtr();
		//delete ptr;
		//data.setData(0);
		//data.setSize(0);
	}

	std::wstring PFSResourceProvider::GUIStringToWString(const String& cs)
	{
		std::wstring ret(cs.length(),0);
		for(CEGUI::String::size_type i=0;i!=cs.length();++i){
			ret[i]=cs[i];
		}
		return ret;
	}

	CEGUI::String PFSResourceProvider::WStringToGUIString(const std::wstring& wstr)
	{
		CEGUI::String str(wstr);
		return str;
	}

    void PFSResourceProvider::setResourceGroupDirectory(const String& resourceGroup, const String& directory)
    {
        if (directory.length() == 0)
            return;

        #if defined(_WIN32) || defined(__WIN32__)
            // while we rarely use the unportable '\', the user may have
            const String separators("\\/");
        #else
            const String separators("/");
        #endif

        if (String::npos == separators.find(directory[directory.length() - 1]))
            d_resourceGroups[resourceGroup] = directory + '/';
        else
            d_resourceGroups[resourceGroup] = directory;
    }

    const String& PFSResourceProvider::getResourceGroupDirectory(const String& resourceGroup)
    {
        return d_resourceGroups[resourceGroup];
    }

    void PFSResourceProvider::clearResourceGroupDirectory(const String& resourceGroup)
    {
        ResourceGroupMap::iterator iter = d_resourceGroups.find(resourceGroup);

        if (iter != d_resourceGroups.end())
            d_resourceGroups.erase(iter);
    }

    String PFSResourceProvider::getFinalFilename(const String& filename, const String& resourceGroup) const
    {
        String final_filename;

        // look up resource group directory
        ResourceGroupMap::const_iterator iter =
            d_resourceGroups.find(resourceGroup.empty() ? d_defaultResourceGroup : resourceGroup);

        // if there was an entry for this group, use it's directory as the
        // first part of the filename
        if (iter != d_resourceGroups.end())
            final_filename = (*iter).second;

        // append the filename part that we were passed
        final_filename += filename;
#ifdef _EDITOR_
		return final_filename;
#endif        

        // return result
        return final_filename;         
    }

	std::wstring PFSResourceProvider::GetPFSFileName(const String& filename, const String& resourceGroup)
	{
		String final_filename;

		// look up resource group directory
		ResourceGroupMap::const_iterator iter =
			d_resourceGroups.find(resourceGroup.empty() ? d_defaultResourceGroup : resourceGroup);

		// if there was an entry for this group, use it's directory as the
		// first part of the filename
		if (iter != d_resourceGroups.end())
			final_filename = (*iter).second;

		// append the filename part that we were passed
		
		final_filename += filename;
		
#ifdef _EDITOR_

		std::wstring wstrfilenameEditor = GUIStringToWString(final_filename);
		std::transform(wstrfilenameEditor.begin(),wstrfilenameEditor.end(),wstrfilenameEditor.begin(),::tolower);
		return wstrfilenameEditor;
        
#endif		

		std::wstring wstrfilename = GUIStringToWString(final_filename);
		std::transform(wstrfilename.begin(),wstrfilename.end(),wstrfilename.begin(),::tolower);

		return wstrfilename;
	}

size_t PFSResourceProvider::getResourceGroupFileNames(
    std::vector<String>& out_vec,
    const String& file_pattern,
    const String& resource_group)
{
    // look-up resource group name
    ResourceGroupMap::const_iterator iter =
        d_resourceGroups.find(resource_group.empty() ? d_defaultResourceGroup :
                                                       resource_group);
    // get directory that's set for the resource group
    const String dir_name(iter != d_resourceGroups.end() ? (*iter).second : "./");

    size_t entries = 0;

// Win32 code.
#if defined(__WIN32__) || defined(_WIN32)
    intptr_t f;
    struct _finddata_t fd;
    if ((f = _findfirst((dir_name + file_pattern).c_str(), &fd)) != -1)
    {
        do
        {
            if ((fd.attrib & _A_SUBDIR))
                continue;

            out_vec.push_back(fd.name);
            ++entries;
        } while (_findnext(f, &fd) == 0);

        _findclose(f);
    }
// Everybody else
#else
    DIR* dirp;
    if ((dirp = opendir(dir_name.c_str())))
    {
        struct dirent* dp;
        while ((dp = readdir(dirp)))
        {
            const String filename(dir_name + dp->d_name);
            struct stat s;

            if ((stat(filename.c_str(), &s) == 0) &&
                S_ISREG(s.st_mode) &&
                (fnmatch(file_pattern.c_str(), dp->d_name, 0) == 0))
            {
                out_vec.push_back(dp->d_name);
                ++entries;
            }
        }

        closedir(dirp);
    }
#endif
    return entries;
}

} // End of  CEGUI namespace section
