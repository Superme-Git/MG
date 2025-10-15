/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
#ifndef __CC_FILEUTILS_PLATFORM_H__
#define __CC_FILEUTILS_PLATFORM_H__

#include <string>
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
#include <vector>
#include <map>
#endif
#include "CCPlatformMacros.h"
#include "ccTypes.h"

#ifdef ANDROID
#include "android/asset_manager.h"
#endif

NS_CC_BEGIN

/**
 * @addtogroup platform
 * @{
 */

//! @brief  Helper class to handle file operations
class CC_DLL CCFileUtils
{
public:
    static CCFileUtils* sharedFileUtils();
    static void purgeFileUtils();

    void purgeCachedEntries();
    /**
    @brief Get resource file data
    @param[in]  pszFileName The resource file name which contain the path
    @param[in]  pszMode The read mode of the file
    @param[out] pSize If get the file data succeed the it will be the data size,or it will be 0
    @return if success,the pointer of data will be returned,or NULL is returned
    @warning If you get the file data succeed,you must delete[] it after used.
    */
    unsigned char* getFileData(const char* pszFileName, const char* pszMode, unsigned long * pSize);

    /**
    @brief Get resource file data from zip file
    @param[in]  pszFileName The resource file name which contain the relative path of zip file
    @param[out] pSize If get the file data succeed the it will be the data size,or it will be 0
    @return if success,the pointer of data will be returned,or NULL is returned
    @warning If you get the file data succeed,you must delete[] it after used.
    */
    unsigned char* getFileDataFromZip(const char* pszZipFilePath, const char* pszFileName, unsigned long * pSize);

    /** removes the suffix from a path
     * On RetinaDisplay it will remove the -hd suffix
     * On iPad it will remove the -ipad suffix
     * On iPhone it will remove the (empty) suffix
     Only valid on iOS. Not valid for OS X.
 
     @since v0.99.5
     */
    std::string& removeSuffixFromFile(std::string& path);

    /**
    @brief   Generate the absolute path of the file.
    @param   pszRelativePath     The relative path of the file.
    @return  The absolute path of the file.
    @warning We only add the ResourcePath before the relative path of the file.
    If you have not set the ResourcePath,the function add "/NEWPLUS/TDA_DATA/UserData/" as default.
    You can set ResourcePath by function void setResourcePath(const char *pszResourcePath);
    */
    const char* fullPathFromRelativePath(const char *pszRelativePath);

    /** Returns the fullpath of an filename including the resolution of the image.
 
        If in RetinaDisplay mode, and a RetinaDisplay file is found, it will return that path.
        If in iPad mode, and an iPad file is found, it will return that path.
 
        Examples:
 
        * In iPad mode: "image.png" -> "/full/path/image-ipad.png" (in case the -ipad file exists)
        * In RetinaDisplay mode: "image.png" -> "/full/path/image-hd.png" (in case the -hd file exists)
 
        If an iPad file is found, it will set resolution type to kCCResolutioniPad
        If a RetinaDisplay file is found, it will set resolution type to kCCResolutionRetinaDisplay
 
      */
    const char* fullPathFromRelativePath(const char *pszRelativePath, ccResolutionType *pResolutionType);

    /// @cond
    const char* fullPathFromRelativeFile(const char *pszFilename, const char *pszRelativeFile);
    /// @endcond

    /** Sets the iPhone RetinaDisplay suffix to load resources.
        By default it is "-hd".
        Only valid on iOS. Not valid for OS X.
 
        @since v1.1
     */
    void setiPhoneRetinaDisplaySuffix(const char *suffix);

    /** Sets the iPad suffix to load resources.
        By default it is "".
        Only valid on iOS. Not valid for OS X.
 

     */
    void setiPadSuffix(const char *suffix);

    /** Sets the iPad Retina Display suffix to load resources.
        By default it is "-ipadhd".
        Only valid on iOS. Not valid for OS X.
 
        @since v1.1
     */
    void setiPadRetinaDisplaySuffix(const char *suffix);

    /** Returns whether or not a given filename exists with the iPad suffix.
        Only available on iOS. Not supported on OS X.
        @since v1.1
    */
    bool iPadFileExistsAtPath(const char *filename);

    /** Returns whether or not a given filename exists with the iPad RetinaDisplay suffix.
     Only available on iOS. Not supported on OS X.
     @since v2.0
     */
    bool iPadRetinaDisplayFileExistsAtPath(const char *filename);

    /** Returns whether or not a given path exists with the iPhone RetinaDisplay suffix.
        Only available on iOS. Not supported on OS X.
        @since v1.1
    */
    bool iPhoneRetinaDisplayFileExistsAtPath(const char *filename);

    /**
    @brief  Set the ResourcePath,we will find resource in this path
    @param pszResourcePath  The absolute resource path
    @warning Don't call this function in android and iOS, it has not effect.
    In android, if you want to read file other than apk, you shoud use invoke getFileData(), and pass the 
    absolute path.
    */
    void setResourcePath(const char *pszResourcePath);

#ifdef ANDROID
    void setAssetManager(AAssetManager* am);
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
	/** Returns the fullpath for a given filename.
     
     First it will try to get a new filename from the "filenameLookup" dictionary.
     If a new filename can't be found on the dictionary, it will use the original filename.
     Then it will try to obtain the full path of the filename using the CCFileUtils search rules: resolutions, and search paths.
     The file search is based on the array element order of search paths and resolution directories.
     
     For instance:

     	We set two elements("/mnt/sdcard/", "internal_dir/") to search paths vector by setSearchPaths,
     	and set three elements("resources-ipadhd/", "resources-ipad/", "resources-iphonehd")
     	to resolutions vector by setSearchResolutionsOrder. The "internal_dir" is relative to "Resources/".

		If we have a file named 'sprite.png', the mapping in fileLookup dictionary contains `key: sprite.png -> value: sprite.pvr.gz`.
     	Firstly, it will replace 'sprite.png' with 'sprite.pvr.gz', then searching the file sprite.pvr.gz as follows:

     	    /mnt/sdcard/resources-ipadhd/sprite.pvr.gz      (if not found, search next)
     	    /mnt/sdcard/resources-ipad/sprite.pvr.gz        (if not found, search next)
     	    /mnt/sdcard/resources-iphonehd/sprite.pvr.gz    (if not found, search next)
     	    /mnt/sdcard/sprite.pvr.gz                       (if not found, search next)
     	    internal_dir/resources-ipadhd/sprite.pvr.gz     (if not found, search next)
     	    internal_dir/resources-ipad/sprite.pvr.gz       (if not found, search next)
     	    internal_dir/resources-iphonehd/sprite.pvr.gz   (if not found, search next)
     	    internal_dir/sprite.pvr.gz                      (if not found, return "sprite.png")

        If the filename contains relative path like "gamescene/uilayer/sprite.png",
        and the mapping in fileLookup dictionary contains `key: gamescene/uilayer/sprite.png -> value: gamescene/uilayer/sprite.pvr.gz`.
        The file search order will be:

     	    /mnt/sdcard/gamescene/uilayer/resources-ipadhd/sprite.pvr.gz      (if not found, search next)
     	    /mnt/sdcard/gamescene/uilayer/resources-ipad/sprite.pvr.gz        (if not found, search next)
     	    /mnt/sdcard/gamescene/uilayer/resources-iphonehd/sprite.pvr.gz    (if not found, search next)
     	    /mnt/sdcard/gamescene/uilayer/sprite.pvr.gz                       (if not found, search next)
     	    internal_dir/gamescene/uilayer/resources-ipadhd/sprite.pvr.gz     (if not found, search next)
     	    internal_dir/gamescene/uilayer/resources-ipad/sprite.pvr.gz       (if not found, search next)
     	    internal_dir/gamescene/uilayer/resources-iphonehd/sprite.pvr.gz   (if not found, search next)
     	    internal_dir/gamescene/uilayer/sprite.pvr.gz                      (if not found, return "gamescene/uilayer/sprite.png")

     If the new file can't be found on the file system, it will return the parameter pszFileName directly.
     
     This method was added to simplify multiplatform support. Whether you are using cocos2d-js or any cross-compilation toolchain like StellaSDK or Apportable,
     you might need to load different resources for a given file in the different platforms.

     @since v2.1
     */
    virtual std::string fullPathForFilename(const char* pszFileName);
    
    /**
     * Loads the filenameLookup dictionary from the contents of a filename.
     * 
     * @note The plist file name should follow the format below:
     * 
     * @code
     * <?xml version="1.0" encoding="UTF-8"?>
     * <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
     * <plist version="1.0">
     * <dict>
     *     <key>filenames</key>
     *     <dict>
     *         <key>sounds/click.wav</key>
     *         <string>sounds/click.caf</string>
     *         <key>sounds/endgame.wav</key>
     *         <string>sounds/endgame.caf</string>
     *         <key>sounds/gem-0.wav</key>
     *         <string>sounds/gem-0.caf</string>
     *     </dict>
     *     <key>metadata</key>
     *     <dict>
     *         <key>version</key>
     *         <integer>1</integer>
     *     </dict>
     * </dict>
     * </plist>
     * @endcode
     * @param filename The plist file name.
     *
     * @since v2.1
     * @loadFilenameLookup
     */
    virtual void loadFilenameLookupDictionaryFromFile(const char* filename);
    
    /** 
     *  Sets the filenameLookup dictionary.
     *
     *  @param pFilenameLookupDict The dictionary for replacing filename.
     *  @since v2.1
     *  @lua NA
     */
    virtual void setFilenameLookupDictionary(CCDictionary* pFilenameLookupDict);

    /** 
     *  Sets the array that contains the search order of the resources.
     *
     *  @param searchResolutionsOrder The source array that contains the search order of the resources.
     *  @see getSearchResolutionsOrder(void), fullPathForFilename(const char*).
     *  @since v2.1
     *  @js NA
     *  @lua NA
     */
    virtual void setSearchResolutionsOrder(const std::vector<std::string>& searchResolutionsOrder);

    /**
      * Append search order of the resources.
      *
      * @see setSearchResolutionsOrder(), fullPathForFilename().
      * @since v2.1
      */
    virtual void addSearchResolutionsOrder(const char* order);
    
    /**
     *  Gets the array that contains the search order of the resources.
     *
     *  @see setSearchResolutionsOrder(const std::vector<std::string>&), fullPathForFilename(const char*).
     *  @since v2.1
     *  @js NA
     *  @lua NA
     */
    virtual const std::vector<std::string>& getSearchResolutionsOrder();
    
    /** 
     *  Sets the array of search paths.
     * 
     *  You can use this array to modify the search path of the resources.
     *  If you want to use "themes" or search resources in the "cache", you can do it easily by adding new entries in this array.
     *
     *  @note This method could access relative path and absolute path.
     *        If the relative path was passed to the vector, CCFileUtils will add the default resource directory before the relative path.
     *        For instance:
     *        	On Android, the default resource root path is "assets/".
     *        	If "/mnt/sdcard/" and "resources-large" were set to the search paths vector,
     *        	"resources-large" will be converted to "assets/resources-large" since it was a relative path.
     *
     *  @param searchPaths The array contains search paths.
     *  @see fullPathForFilename(const char*)
     *  @since v2.1
     *  @js NA
     *  @lua NA
     */
    virtual void setSearchPaths(const std::vector<std::string>& searchPaths);
    
    /**
      * Adds a path to search paths.
	  *
	  * @since v2.2
      */
     virtual void addSearchPath(const char* path);

    /**
      * Removes a path from search paths.
      *
      * @since v2.2
      * @lua NA
      */
	 virtual void removeSearchPath(const char *path);

    /**
      * Removes all paths.
      *
      * @since v2.2
      * @lua NA
      */
	 void removeAllPaths();
    
    /**
     *  Gets the array of search paths.
     *  
     *  @return The array of search paths.
     *  @see fullPathForFilename(const char*).
     *  @js NA
     *  @lua NA
     */
    virtual const std::vector<std::string>& getSearchPaths();

	CCDictionary* createCCDictionaryWithContentsOfFile(const std::string& filename);
	CCArray* createCCArrayWithContentsOfFile(const std::string& filename);
	bool writeToFile(cocos2d::CCDictionary *dict, const std::string &fullPath);

    CCFileUtils();
    /**
     *  The destructor of CCFileUtils.
     *  @js NA
     *  @lua NA
     */
    virtual ~CCFileUtils();
	virtual bool init();
	std::string getNewFilename(const char* pszFileName);
	std::string getPathForFilename(const std::string& filename, const std::string& resolutionDirectory, const std::string& searchPath);
	std::string getFullPathForDirectoryAndFilename(const std::string& strDirectory, const std::string& strFilename);
	bool isAbsolutePath(const std::string& strPath);
	/**
     *  Checks whether a file exists.
     *
     *  @note If a relative path was passed in, it will be inserted a default root path at the beginning.
     *  @param strFilePath The path of the file, it could be a relative or absolute path.
     *  @return true if the file exists, otherwise it will return false.
     *  @lua NA
     */
    virtual bool isFileExist(const std::string& strFilePath) = 0;
#endif
    /**
    @brief   Get the writeable path
    @return  The path that can write/read file
    */
    std::string getWriteablePath();

#if !(defined WIN7_32)
    std::string getCachePath();
    
    std::string getTempPath();
#endif

    /**
    @brief Set/Get whether pop-up a message box when the image load failed
    */
    void setPopupNotify(bool bNotify);
    bool isPopupNotify();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
protected:
	static CCFileUtils* s_sharedFileUtils;
	/** Dictionary used to lookup filenames based on a key.
     *  It is used internally by the following methods:
     *
     *  std::string fullPathForFilename(const char*);
     *
     *  @since v2.1
     */
    CCDictionary* m_pFilenameLookupDict;
	/** 
     *  The vector contains resolution folders.
     *  The lower index of the element in this vector, the higher priority for this resolution directory.
     */
    std::vector<std::string> m_searchResolutionsOrderArray;
    
    /**
     * The vector contains search paths.
     * The lower index of the element in this vector, the higher priority for this search path.
     */
    std::vector<std::string> m_searchPathArray;
    
    /**
     *  The default root path of resources.
     *  If the default root path of resources needs to be changed, do it in the `init` method of CCFileUtils's subclass.
     *  For instance:
     *  On Android, the default root path of resources will be assigned with "assets/" in CCFileUtilsAndroid::init().
     *  Similarly on Blackberry, we assign "app/native/Resources/" to this variable in CCFileUtilsBlackberry::init().
     */
    std::string m_strDefaultResRootPath;
    
    /**
     *  The full path cache. When a file is found, it will be added into this cache. 
     *  This variable is used for improving the performance of file search.
     */
    std::map<std::string, std::string> m_fullPathCache;
#endif
};

// end of platform group
/// @}

NS_CC_END

#endif    // __CC_FILEUTILS_PLATFORM_H__
