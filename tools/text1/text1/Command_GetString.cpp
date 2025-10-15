#include "stdafx.h"
#include <set>

void GetStrings(FILE* fp, dir& dirFiles, CString& type)
{
	std::set<std::string> texts;
	int charCount = 0;
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(type.GetString());
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		wprintf(L"file %s\r\n", vwsFiles[i].c_str());

		// 加载文件.
		CStdioFile file;
		CString text;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			CString s;
			while (file.ReadString(s))
			{
				text += s;
				text += "\r";
			}
			file.Close();
		}

		int line = 1;
		wchar_t* s = text.GetBuffer();
		while (*s)
		{
			if (*s == '\r'){ line++; ++s; continue; }
			// 注释中的字符串无效.
			if (*s == '/'&&*(s + 1) == '/')
			{
				char lastChar = *s;
				while (*s&&*s != '\r'&&*s != '\n'&&lastChar != '\\')
				{
					if (*s == '\r'){ line++; ++s; continue; }
					if (*s != ' '&&*s != '/t')
					{
						lastChar = *s;
					}
					++s;
				}
			}
			if (*s == '/'&&*(s + 1) == '*')
			{
				++++s;
				while (*s)
				{
					if (*s == '*'&&*(s + 1) == '/')
						break;
					if (*s == '\r'){ line++; ++s; continue; }
					charCount++;
					++s;
				}
			}

			// 统计字符串.
			std::string text;
			if (*s == '"')
			{
				++s;
				while (*s&&*s!='"')
				{
					text += *s;
					++s;
				}
			}

			++s;

			//
			if (text.size() > 2)
			{
				//fwprintf(fp, L"file %s line %d ", vwsFiles[i].c_str(), line);
				//fwrite(text.c_str(), text.length(), 1, fp);
				//fwrite(text.c_str(), text.length(), 1, fp);
				texts.insert(text);
			}
		}
	}

	std::set<std::string>::iterator it = texts.begin();
	while (it != texts.end())
	{
		fwrite(it->c_str(), it->length(), 1, fp);
		fprintf(fp, "\n");
		++it;
	}
}

void Command_GetString(CString& path, std::vector<CString>& vsTypes)
{
	//
	FILE* fp;
	fopen_s(&fp, "d:/strings.txt", "w+");
	if (fp)
	{
		//整体替换.
		dir dirFiles;
		dirFiles.push_files(path);
		unsigned char type1 = 0xef;
		unsigned char type2 = 0xbb;
		unsigned char type3 = 0xbf;
		fwrite(&type1, 1, 1, fp);
		fwrite(&type2, 1, 1, fp);
		fwrite(&type3, 1, 1, fp);
		for (int i = 0; i < vsTypes.size(); ++i)
		{
			GetStrings(fp, dirFiles, vsTypes[i]);
		}
		fclose(fp);
	}
}
