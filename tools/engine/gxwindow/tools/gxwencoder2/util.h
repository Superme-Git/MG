#pragma once


#pragma push_macro("UNICODE")
#define UNICODE
#include "./greta/regexpr2.h"
#pragma pop_macro("UNICODE")

#include <vector>
#include <string>
#include <locale.h>
#include <iostream>

#pragma warning( disable: 4996)

std::string wstr2str(const std::wstring& wstr);
std::wstring str2wstr(const std::string& str);

std::string ARGB2RGBA( const std::string& ARGB);	// A,R,G,B Из: 255,255,255,255
std::string ARGB2RGBA( const std::string& ARGB, std::vector<int>& clrs);

std::vector<std::string> SplitString( const std::string& text, const std::string& separator);

std::string Replace( const std::string& text, const std::string& replace, const std::string& bereplaced);

void CreateDirectories( const std::string& targetpath, const std::string& createpath);