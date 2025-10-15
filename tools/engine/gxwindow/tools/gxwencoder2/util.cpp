#include "stdafx.h"
#include "util.h"

std::string wstr2str(const std::wstring& wstr)
{
	std::string curLocale = setlocale(LC_ALL, NULL); // "C"
	setlocale(LC_ALL, "chs");

	char *buff = (char*) malloc(sizeof(char) * (wstr.size() * 2 + 1));
	wcstombs(buff, wstr.c_str(), wstr.size() * 2 + 1);
	std::string str(buff);
	free(buff);

	setlocale(LC_ALL, curLocale.c_str());
	return str;
}

std::wstring str2wstr(const std::string& str)
{
	std::string curLocale = setlocale(LC_ALL, NULL);
	setlocale(LC_ALL, "chs");

	wchar_t *buff = (wchar_t*) malloc(sizeof(wchar_t) * (str.size() + 1));
	mbstowcs(buff, str.c_str(), str.size() + 1);
	std::wstring wstr(buff);
	free(buff);

	setlocale(LC_ALL, curLocale.c_str());
	return wstr;
}

std::string ARGB2RGBA(const std::string& ARGB)
{
	regex::match_results results;
	regex::rpattern pat( std::wstring(L","));
	regex::split_results sr;
	pat.split( str2wstr( ARGB), sr);
	//assert( sr.size() == 4);
	if ( sr.size() != 4)
		return "255,255,255,255";

	std::string RGBA = wstr2str( sr[1]) + ",";
	RGBA += wstr2str( sr[2]) + ",";
	RGBA += wstr2str( sr[3]) + ",";
	RGBA += wstr2str( sr[0]);

	return RGBA;
}

std::string ARGB2RGBA( const std::string& ARGB, std::vector<int>& clrs)
{
	regex::match_results results;
	regex::rpattern pat( std::wstring(L","));
	regex::split_results sr;
	pat.split( str2wstr( ARGB), sr);
	if ( sr.size() != 4)
		return ARGB2RGBA( "255,255,255,255", clrs);

	clrs.clear();
	clrs.resize( 4);
	clrs[0] = atoi( wstr2str( sr[1]).c_str());
	clrs[1] = atoi( wstr2str( sr[2]).c_str());
	clrs[2] = atoi( wstr2str( sr[3]).c_str());
	clrs[3] = atoi( wstr2str( sr[0]).c_str());

	std::string RGBA = wstr2str( sr[1]) + ",";
	RGBA += wstr2str( sr[2]) + ",";
	RGBA += wstr2str( sr[3]) + ",";
	RGBA += wstr2str( sr[0]);

	return RGBA;
}

std::vector<std::string> SplitString( const std::string& text, const std::string& separator)
{
	std::wstring wseparator = str2wstr( separator);

	regex::match_results results;
	regex::rpattern pat( std::wstring( wseparator.c_str()));
	regex::split_results sr;
	pat.split( str2wstr( text), sr);

	std::vector<std::string> splited;
	splited.reserve( sr.size());
	for ( regex::split_results::iterator it = sr.begin(); it != sr.end(); ++ it)
	{
		if ( !(*it).empty())
			splited.push_back( wstr2str( *it));
	}

	return splited;
}

std::string Replace( const std::string& text, const std::string& replace, const std::string& bereplaced)
{
	regex::rpattern rep( str2wstr( replace), str2wstr( bereplaced), regex::NOCASE | regex::GLOBAL );
	regex::subst_results rs;
	std::wstring wtmp = str2wstr( text);
	rep.substitute( wtmp, rs);

	return wstr2str( wtmp);
}

void CreateDirectories( const std::string& targetpath, const std::string& createpath)
{
	std::string path = Replace( createpath, "\\", "/");
	std::vector<std::string> splited = SplitString( path, "/");

	std::string target = targetpath;
	for ( std::vector<std::string>::iterator it = splited.begin(); it != splited.end(); ++ it)
	{
		target += "\\" + *it;
		CreateDirectory( target.c_str(), NULL);
	}
}