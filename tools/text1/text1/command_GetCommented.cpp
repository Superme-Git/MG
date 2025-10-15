#include "stdafx.h"

void GetCommenteds(FILE* fp, dir& dirFiles, CString& type)
{
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
				text += " ";
			}
			file.Close();
		}

		wchar_t* s = text.GetBuffer();
		while (*s)
		{
			if (*s == '"')
			{
				while (*s&&*(s + 1) != '"')
				{
					++s;
				}
			}
			if (*s == '/'&&*(s + 1) == '/')
			{
				while (*s&&*s!='\n')
				{
					fputc(*s, fp);
					++s;
				}
				fputc(*s, fp);
				++s;
			}
			if (*s == '/'&&*(s + 1) == '*')
			{
				while (*s)
				{
					if (*s == '*'&&*(s + 1) == '/')
					{
						fputc(*s, fp);
						++s;
						fputc(*s, fp);
						break;
					}
					fputc(*s, fp);
					charCount++;
					++s;
				}
				fprintf(fp, "\r\n");
			}
			++s;
		}
	}
}

void Command_GetCommented(CString& path, std::vector<CString>& vsTypes)
{
	FILE* fp;
	fopen_s(&fp, "d:/commenteds.txt", "w+");
	if (fp)
	{
		//整体替换.
		dir dirFiles;
		dirFiles.push_files(path);
		for (int i = 0; i < vsTypes.size(); ++i)
		{
			GetCommenteds(fp, dirFiles, vsTypes[i]);
		}
		fclose(fp);
	}
}
