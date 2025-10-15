/********************************************************************
	created:	2011/03/07
	created:	7:3:2011   20:44
	filename: 	CEGUIPfsResourceProvider.h
	file path:	cegui\projects\premake\BaseSystem
	file base:	CEGUIPfsResourceProvider
	file ext:	h
	purpose:	\u7528Pfs\u8BFB\u53D6cegui\u8D44\u6E90
*********************************************************************/


#ifndef _CEGUIFPSRESOURCEPROVIDER_H_
#define _CEGUIFPSRESOURCEPROVIDER_H_

#include "CEGUIBase.h"
#include "CEGUIResourceProvider.h"
#include "Nuclear.h"
#include <map>

// Start of CEGUI namespace section
namespace CEGUI
{
class CEGUIEXPORT PFSResourceProvider : public ResourceProvider
{
public:
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	PFSResourceProvider(){}
	~PFSResourceProvider(void) {}

    /*!
    \brief
        Set the directory associated with a given resource group identifier.

    \param resourceGroup
        The resource group identifier whose directory is to be set.

    \param directory
        The directory to be associated with resource group identifier
        \a resourceGroup

    \return
        Nothing.
    */
    void setResourceGroupDirectory(const String& resourceGroup, const String& directory);

    /*!
    \brief
        Return the directory associated with the specified resource group
        identifier.

    \param resourceGroup
        The resource group identifier for which the associated directory is to
        be returned.

    \return
        String object describing the directory currently associated with resource
        group identifier \a resourceGroup.

    \note
        This member is not defined as being const because it may cause
        creation of an 'empty' directory specification for the resourceGroup
        if the resourceGroup has not previously been accessed.
    */
    const String& getResourceGroupDirectory(const String& resourceGroup);

    /*!
    \brief
        clears any currently set directory for the specified resource group
        identifier.

    \param resourceGroup
        The resource group identifier for which the associated directory is to
        be cleared.
    */
    void clearResourceGroupDirectory(const String& resourceGroup);

	//\u4E0D\u7528\u8FD9\u4E2A\u63A5\u53E3\u4E86\uFF0C\u53EA\u7528Xbufer
#if (defined PUBLISHED_VERSION) && !(defined FORCEGUIEDITOR)
	void loadRawDataContainer(const String& filename, LJFM::LJFMID& output, const String& resourceGroup);
#else
	void loadRawDataContainer(const String& filename, RawDataContainer& output, const String& resourceGroup);
#endif
    void unloadRawDataContainer(RawDataContainer& data);
	//void unloadRawDataContainer(RawDataContainer& data);
    size_t getResourceGroupFileNames(std::vector<String>& out_vec,
                                     const String& file_pattern,
                                     const String& resource_group);

public: 
	std::wstring GetPFSFileName(const String& filename, const String& resourceGroup);

protected:
    /*!
    \brief
        Return the final path and filename, taking into account the given
        resource group identifier that should be used when attempting to
        load the data.
    */
    String getFinalFilename(const String& filename, const String& resourceGroup) const;

    typedef std::map<String, String, String::FastLessCompare> ResourceGroupMap;
    ResourceGroupMap    d_resourceGroups;

public:
	static std::wstring GUIStringToWString(const String& cs);

	static CEGUI::String WStringToGUIString(const std::wstring& wstr);

	static bool IsValidateString(CEGUI::String string, size_t validateLen)
	{
		return GUIStringToWString(string).length() <= validateLen;
	}

	static bool IsValidateStringLength(CEGUI::String string, size_t maxlength, size_t minlength)
	{
		return GUIStringToWString(string).length() <= maxlength && GUIStringToWString(string).length() >= minlength;
	}
};

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIPFSResourceProvider_h_
