//IniFile.h
#ifndef _share_IniFile_H_
#define _share_IniFile_H_

#include <string>

class IniFile
{
public:    

	static int read_profile_string(const char *section, const char *key, char *value, int size, const char *default_value, const char *file);
	static int read_profile_int(const char *section, const char *key, int default_value, const char *file);
	static int read_profile_float(const char *section, const char *key, float default_value, const char *file);
	static int write_profile_string(const char *section, const char *key, const char *value, const char *file);

	static std::string getCfgFilename();
};


#endif
