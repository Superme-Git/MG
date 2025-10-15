#pragma once
#include <map>
#include <string>
#include <vector>
#include <fstream>
#include <algorithm>
#include <comutil.h>

class CIniFile
{
public:
	CIniFile(void){}
	~CIniFile(void){}

	bool open(const wchar_t* path)
	{
		std::wifstream fin(path);
		fin.imbue(std::locale(""));
		if (!fin.is_open())
			return false;
		std::vector<std::wstring> strvect;
		while (!fin.eof())
		{
			std::wstring inbuf;
			std::getline(fin, inbuf);
			strvect.push_back(inbuf);
		}
		fin.close();
		if (strvect.empty())
			return false;
		std::for_each(strvect.begin(), strvect.end(), analyzeini(m_mapValue));
		m_path = path;
		return !m_mapValue.empty();
	}

	bool save(const std::wstring& path=L"")
	{
		std::wstring file(path);
		if (file.empty())
			file = m_path;
		std::wofstream fon(file.c_str());
		if (!fon.is_open())
			return false;
		IniValueMap::const_iterator itor = m_mapValue.begin();
		for (; itor != m_mapValue.end(); ++itor)
		{
			fon << L'[' << itor->first << L']' << std::endl;
			IniKeyMap::const_iterator itorKey = itor->second.begin();
			for (; itorKey != itor->second.end(); ++itorKey)
			{
				fon << itorKey->first << L" = " << itorKey->second << std::endl;
			}
			fon << std::endl;
		}
		fon.close();
		return true;
	}

	std::wstring GetValue(const wchar_t* psect, const wchar_t* pkey)
	{
		IniValueMap::const_iterator itor = m_mapValue.find(psect);
		if (itor == m_mapValue.end())
			return L"";
		IniKeyMap::const_iterator itorValue = itor->second.find(pkey);
		if (itorValue == itor->second.end())
			return L"";
		return itorValue->second;
	}

	bool SetValue(const wchar_t* psect, const wchar_t* pkey, const std::wstring& value)
	{
		(m_mapValue[psect])[pkey] = value;
		return true;
	}

	bool ClearSection(const wchar_t* psect)
	{
		IniValueMap::iterator itor = m_mapValue.find(psect);
		if (itor == m_mapValue.end())
			return false;
		itor->second.clear();
		return true;
	}

	typedef std::map<std::wstring, std::wstring>		IniKeyMap;
	typedef std::map<std::wstring, IniKeyMap>			IniValueMap;

	// 得到section下所有的key
	const IniKeyMap* GetAllKey(const wchar_t* psect)
	{
		IniValueMap::const_iterator itor = m_mapValue.find(psect);
		if (itor == m_mapValue.end())
			return NULL;
		return &itor->second;
	}

private:
	struct analyzeini
	{
		std::wstring strsect;
		IniValueMap *pmap;
		analyzeini(IniValueMap& strmap):pmap(&strmap){}

		void operator()( const std::wstring& strini)
		{
			std::wstring::size_type first = strini.find(L'[');
			std::wstring::size_type last = strini.rfind(L']');
			if (first != std::wstring::npos && last != std::wstring::npos && first != last+1)
			{
				strsect = strini.substr(first+1, last-first-1);
				return ;
			}
			if (strsect.empty())
				return ;
			if ((first=strini.find('=')) == std::wstring::npos)
				return ;
			std::wstring strtmp1 = strini.substr(0,first);
			std::wstring strtmp2 = strini.substr(first+1, std::wstring::npos);
			first = strtmp1.find_first_not_of(L" \t");
			last = strtmp1.find_last_not_of(L" \t");
			if (first == std::wstring::npos || last == std::wstring::npos)
				return;
			std::wstring strkey = strtmp1.substr(first, last-first+1);
			first = strtmp2.find_first_not_of(L" \t");
			if (((last = strtmp2.find(L"\t#", first )) != std::wstring::npos) ||
				((last = strtmp2.find(L" #", first )) != std::wstring::npos) ||
				((last = strtmp2.find(L"\t//", first )) != std::wstring::npos)||
				((last = strtmp2.find(L" //", first )) != std::wstring::npos))
			{
				strtmp2 = strtmp2.substr(0, last-first);
			}
			last = strtmp2.find_last_not_of(L" \t");
			if(first == std::wstring::npos || last == std::wstring::npos)
				return ;
			std::wstring value = strtmp2.substr(first, last-first+1);
			//string mapkey = strsect + MIDDLESTRING;
			//mapkey += strkey;
			//(*pmap)[mapkey]=value;
			(*pmap)[strsect].insert(std::make_pair(strkey, value));
		}
	};

private:
	IniValueMap		m_mapValue;
	std::wstring	m_path;
};
