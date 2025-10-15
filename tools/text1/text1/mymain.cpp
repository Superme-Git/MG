#include "stdafx.h"
#include "filesystem.h"
#include <string>
#include <set>
#include <map>
#include <assert.h>
#include <atlenc.h>

std::set<CString> words;

wchar_t getNextChar(const CString& line, int i)
{
	for (; i < line.GetLength(); ++i)
	{
		if (line[i] == ' ')
			continue;
		if (line[i] == '\t')
			continue;
		return line[i];
	}
	return ' ';
}

bool isChar(char c)
{
	return (c >= '0'&& c <= '9') || (c >= 'a'&& c <= 'z') || (c >= 'A'&& c <= 'Z') || c == '_';
}

bool isFirstChar(char c)
{
	return (c >= 'a'&& c <= 'z') || (c >= 'A'&& c <= 'Z') || c == '_';
}

std::string getNextWord(std::string& beginString, const char*& s)
{
	std::string ret;

	// 寻找第一字符.
	if (!isFirstChar(*s))
	{
		while (*s)
		{
			if (isFirstChar(*s))
				break;
			beginString += *s;
			++s;
		}
	}

	// 移动到非字符.
	while (*s)
	{
		if (!isChar(*s))
		{
			return ret;
		}
		ret += *s;
		++s;
	}

	return ret;
}

void wstovws(const CString& line)
{
	CString s;
	for (int i = 0; i < line.GetLength(); ++i)
	{
		if ((line[i] >= L'0'&& line[i] <= L'9')||
			(line[i] >= L'a'&& line[i] <= L'z')||
			(line[i] >= L'A'&& line[i] <= L'Z')||
			line[i] == L'_'
			)
		{
			s += line[i];
		}
		else
		{
			if (s.GetLength() &&
				(s[0]<L'0' || s[0]>L'9')
				)
			{
				words.insert(s);
				s.Empty();
			}
		}
	}
}

void getWords(dir& dirFiles, const wchar_t* type)
{
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(type);
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		CStdioFile file;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			//读取内容。
			CString s;
			while (file.ReadString(s))
			{
				wstovws(s);
			}
			file.Close();
		}
	}
}

// 
void myGet(CString& path, std::vector<CString>& vsTypes)
{
	dir dirFiles;
	dirFiles.push_files(path);
	for (int i = 0; i < vsTypes.size(); ++i)
	{
		getWords(dirFiles, vsTypes[i]);
	}

	// 创建忽略表.
	std::set<CString> hulueLists;
	CStdioFile hulueFile;
	if (hulueFile.Open(L"hulue.txt", CFile::modeRead | CFile::typeText))
	{
		CString s;
		while (hulueFile.ReadString(s))
		{
			hulueLists.insert(s);
		}
		hulueFile.Close();
	}

	CStdioFile outputFile;
	if (outputFile.Open(L"E:/MT_G/tools/text1/words.txt", CFile::modeCreate|CFile::modeWrite | CFile::typeText))
	{
		std::set<CString>::iterator it = words.begin();
		while (it != words.end())
		{
			if (hulueLists.find(*it) == hulueLists.end())
			{
				outputFile.WriteString(*it);
				outputFile.WriteString(L"\n");
			}
			++it;
		}
	}

}

//
void myGetDir(CString& path, std::vector<CString>& vsTypes)
{
	dir dirFiles;
	dirFiles.push_dir_fires(path);

	CStdioFile outputFile;
	if (outputFile.Open(L"names.txt", CFile::modeCreate | CFile::modeWrite | CFile::typeText))
	{
		for (int i = 0; i < vsTypes.size(); ++i)
		{
			std::vector<std::wstring> vsFiles = dirFiles.get_files_by_type(vsTypes[i].GetString());
			for (int j = 0; j < vsFiles.size(); ++j)
			{
			}
		}
		/*
		std::set<CString>::iterator it = words.begin();
		while (it != words.end())
		{
			outputFile.WriteString(*it);
			outputFile.WriteString(L"\n");
			++it;
		}
		*/
		outputFile.Close();
	}
}

std::map<std::string, std::string> ansiDists;
std::map<CString, CString> dists;

void change(CString& rets,CString& s)
{
	std::map<CString, CString>::iterator it = dists.find(s);
				
	if (it != dists.end()&&it->second.GetLength())
	{
		rets += it->second.GetString();
	}
	else
		rets += s;

	s = "";
}

std::wstring GetChangeText(const std::wstring line)
{
	std::wstring rets;
	std::wstring s;
	std::wstring commented;
	wchar_t firstChar = L' ';
	for (int i = 0; i < line.length(); ++i)
	{
		if (line[i] == '"')//处于引号之间的不处理.
		{
			rets += s.c_str();
			s = L"";
			rets += line[i];
			++i;
			for (; i < line.length(); ++i)
			{
				if (line[i] != L'"')
				{
					rets += line[i];
				}
				else
				{
					break;
				}
			}
		}

		if ((line[i] >= L'0'&& line[i] <= L'9') ||
			(line[i] >= L'a'&& line[i] <= L'z') ||
			(line[i] >= L'A'&& line[i] <= L'Z') ||
			line[i] == L'_'
			)
		{
			s += line[i];
		}
		else
		{
			if (s.length())
			{
				CString s2;
				change(s2,CString(s.c_str()));
				rets += s2;
				s = L"";
			}

			rets += line[i];
		}
	}

	if (s.length())
	{
		CString s2;
		change(s2, CString(s.c_str()));
		rets += s2;
		s = L"";
	}

	return rets;
}

void GetCommented(std::string& code, std::string& commented, std::string& text)
{
	const char* s = text.c_str();
	while (*s)
	{
		if (*s == '/')
		{
			if (*++s == '/')
			{
				commented += '/';

				while (*s)
				{
					commented += (*s);
					++s;
				}
				return;
			}
			code += '/';
		}
		code += *s;
		++s;
	}
}

std::string GetAnsiChangeText(const std::string& line)
{
	std::string ret;
	const char* s = line.c_str();
	const char* send = s + line.size();
	while (*s)
	{
		std::string word = getNextWord(ret,s);
		std::map<std::string, std::string>::iterator it = ansiDists.find(word);
		if (it != ansiDists.end())
		{
			ret += it->second;
		}
		else
			ret += word;
		assert(s <= send);
		if (s >= send)
			break;
		ret += *s;
		++s;
	}
	return ret;
}

std::string GetUtf8ChangeText(const std::string& line)
{
	static char* buf = (char*)malloc(40960);
	UTF8ToANSI(line.c_str(), buf, CP_UTF8, CP_ACP);
	std::string ret = GetAnsiChangeText(buf);
	UTF8ToANSI(ret.c_str(), buf, CP_ACP, CP_UTF8);
	return buf;
}

void WriteLine(std::string& line, FILE* fp)
{
	int len = line.length();
	if (len)
	{
		fwrite(line.c_str(), 1, len, fp);
	}
}

void WriteParseLine(std::string& line,bool isUtf8,FILE* fp)
{
	std::string buffer;
	if (isUtf8)
	{
		buffer = GetUtf8ChangeText(line);
	}
	else
		buffer = GetAnsiChangeText(line);
	int len = buffer.size();
	if (len)
	{
		fwrite(buffer.c_str(), 1, len, fp);
	}
}

void changeWords(dir& dirFiles, const wchar_t* type)
{
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(type);
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		FILE* fp;
		std::string data;

		// 读入内容。
		_wfopen_s(&fp, vwsFiles[i].c_str(), L"rb");
		while (!feof(fp))
		{
			data += fgetc(fp);
		}
		fclose(fp);
		data.resize(data.size()-1);// 删除最后一个-1.

		// 删除文件。
		DeleteFile(vwsFiles[i].c_str());
		bool isUtf8 = false;
		if (data.size() >= 4)
		{
			unsigned char	code1 = data[0];
			unsigned char	code2 = data[1];
			unsigned char	code3 = data[2];
			if (code1 == 0xef && code2 == 0xbb && code3 == 0xbf)
			{
				isUtf8 = true;
				data.erase(data.begin());
				data.erase(data.begin());
				data.erase(data.begin());
			}
		}

		// 修改。
		_wfopen_s(&fp, vwsFiles[i].c_str(), L"wb+");
		if (isUtf8)
		{
			unsigned char code1 = 0xef;
			unsigned char code2 = 0xbb;
			unsigned char code3 = 0xbf;
			fputc(code1, fp);
			fputc(code2, fp);
			fputc(code3, fp);
		}
		std::string line;
		bool commentedState1 = false;
		bool commentedState2 = false;
		bool yinhao = false;
		const char* s = data.c_str();
		while (*s)
		{
			if (s[0] == '"'&&yinhao==false)
			{
				WriteParseLine(line, isUtf8, fp);
				line = s[0];
				yinhao = true;
				++s;
			}
			if (s[0] == '"'&&yinhao == true)
			{
				WriteLine(line, fp);
				line = s[0];
				yinhao = false;
				++s;
			}
			if (s[0] == '/'&&s[1]=='*')
			{
				WriteParseLine(line, isUtf8, fp);
				line = s[0];
				commentedState1 = true;
				++s;
				continue;
			}
			if (s[0] == '*'&&s[1] == '/')
			{
				commentedState1 = false;
				WriteLine(line, fp);
				line = "";
				fputc(*s,fp);
				fputc(*++s, fp);
				++s;
				continue;
			}
			if (!commentedState1)
			{
				if (s[0] == '/'&&s[1] == '/')
				{
					if (line.size())
					{
						WriteParseLine(line, isUtf8, fp);
						line = s[0];
						++s;
						continue;
					}
					commentedState2 = true;
				}
				if (commentedState2&&(s[0] == '\r' || *s == '\n'))
				{
					WriteLine(line, fp);
					fputc(*s, fp);
					line = "";
					commentedState2 = false;
					++s;
					continue;
				}
				if (!commentedState2 && (s[0] == '\r' || *s == '\n'))
				{
					WriteParseLine(line, isUtf8, fp);
					fputc(*s, fp);
					line = "";
					++s;
					continue;
				}
			}
			line += *s;

			++s;
		}
		if (line.size())
			WriteParseLine(line, isUtf8, fp);

		fclose(fp);

		/*
		CStdioFile file;
		std::vector<CString> cs;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			CString s;
			while (file.ReadString(s))
			{
				cs.push_back(s);
			}
			file.Close();
		}

		DeleteFile(vwsFiles[i].c_str());

		if (cs.size()&&cs[0].GetLength()>3 && cs[0][0] == 0xEF && cs[0][1] == 0xBB && cs[0][2] == 0xBF)
		{
			cs[0].Delete(0, 3);
		}

		CStdioFile file2;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeCreate | CFile::modeWrite | CFile::typeText))
		{
			for (int j = 0; j < cs.size(); j++)
			{
				std::wstring line = GetChangeText(cs[j].GetString());
				file.WriteString(line.c_str());
				file.WriteString(L"\r\n");
			}
			file.Flush();
			file.Close();
		}
		*/
	}
}

void makeDist(const wchar_t* fileName)
{
	wprintf(L"file %s\n", fileName);

	FILE* fp;
	_wfopen_s(&fp, fileName, L"rb");
	std::vector<std::string> lines;
	std::string line;
	while (!feof(fp))
	{
		char c = fgetc(fp);
		if (c == '\r' || c == '\n')
		{
			if (line.size())
			{
				lines.push_back(line);
			}
			line = "";
		}
		else
			line += c;
	}
	fclose(fp);

	for (int i = 0; i < lines.size(); ++i)
	{
		std::string s1, s2;
		std::string& line = lines[i];
		for (int j = 0; j < line.size(); ++j)
		{
			if (s2.size())
			{
				s2 += line[j];
			}
			else if (line[j]!=' ')
			{
				s1 += line[j];
			}
			else
			{
				s2 += line[j];
			}
		}
		if (s2.size()>1)
		{
			s2.erase(s2.begin());
			ansiDists.insert(std::pair<std::string, std::string>(s1, s2));
		}
	}
}

void myChange(CString& path, std::vector<CString>& vsTypes)
{
	makeDist(L"E:/MT_G/tools/text1/words.txt");

	//整体替换.
	dir dirFiles;
	dirFiles.push_files(path);
	for (int i = 0; i < vsTypes.size(); ++i)
	{
		changeWords(dirFiles, vsTypes[i]);
	}
}

