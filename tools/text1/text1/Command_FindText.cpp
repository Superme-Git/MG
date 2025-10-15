#include "stdafx.h"
#include <assert.h>
#include <set>

//
class FindText_CharNode
{
public:
	bool mTail;//是否是结尾字符.
	BYTE mChar;//当前节点。
	FindText_CharNode* mChild[256]; // 下一步有可能存在的节点.
	FindText_CharNode()
	{
		for (int i = 0; i < 256; ++i)
		{
			mChild[i] = 0;
		}
	}
	static void CreateKey(const BYTE* word, FindText_CharNode*& node)
	{
		if (!node)
		{
			node = new FindText_CharNode;
			node->mChar = *word;
		}

		++word;

		if (*word)
		{
			CreateKey(word, node->mChild[*word]);
			node->mTail = false;
		}
		else
			node->mTail = true;
	}
	static bool FindKeyWord(CString& keyword,const BYTE* s,FindText_CharNode*& node)
	{
		assert(node);
		keyword += *s;
		++s;
		FindText_CharNode* nextNode = node->mChild[*s];
		if (nextNode == NULL)
		{
			return node->mTail;
		}
		return FindKeyWord(keyword, s, nextNode);
	}
};

//在资源和代码中寻找指定的文本.
void Command_FindText(CString& path, std::vector<CString>& vsTypes)
{
	// 建立关键字字典.
	FindText_CharNode* keys[256];
	for (int i = 0; i < 256; ++i)
	{
		keys[i] = NULL;
	}

	FILE* fpKey;
	_wfopen_s(&fpKey, vsTypes[0].GetString(), L"rb");

	std::string strKeyText;
	while (!feof(fpKey))
	{
		strKeyText += getc(fpKey);
	}

	fclose(fpKey);

	std::vector<std::string> vsKeyWord;
	if (strKeyText.size() >= 3)
	{
		BYTE b1 = *(BYTE*)&(strKeyText[0]);
		BYTE b2 = *(BYTE*)&(strKeyText[1]);
		BYTE b3 = *(BYTE*)&(strKeyText[2]);
		if (b1 == 0xef && b2 == 0xbb && b3 == 0xbf)
		{
			strKeyText.erase(strKeyText.begin());
			strKeyText.erase(strKeyText.begin());
			strKeyText.erase(strKeyText.begin());

			strKeyText.resize(strKeyText.size() - 1); // 删除最后一个字符。

			BYTE* utf8Buffer = UTF8ToANSI2(strKeyText.c_str(), CP_UTF8, CP_ACP);
			vsKeyWord = stovs(strKeyText.c_str(), '\n', '\n', '\n', '\r');
			free(utf8Buffer);
		}
		else
		{
			strKeyText.resize(strKeyText.size() - 1); // 删除最后一个字符。
			vsKeyWord = stovs(strKeyText.c_str(), '\n', '\n', '\n', '\r');
		}
	}
	else
	{
		printf("key file error!\n");
		return;
	}

	for (int i = 0; i < vsKeyWord.size(); ++i)
	{
		if (vsKeyWord[i].size())
		{
			const BYTE* word = (const BYTE*)vsKeyWord[i].c_str();
			FindText_CharNode::CreateKey(word, keys[*word]);
		}
	}

	// 整体替换.
	FILE* fpOutput;
	_wfopen_s(&fpOutput, vsTypes[1].GetString(), L"w+");
	if (!fpOutput)
	{
		fprintf(fpOutput, "create output file %s failed!",vsTypes[1].GetString());
		return;
	}

	std::set<CString> exts;
	std::set<CString> exts2;//后缀名和文件名是一个的情况.
	dir dirFiles;
	dirFiles.push_files(path);
	for (int i = 0; i < dirFiles.m_items.size(); ++i)
	{
		CString fileName = dirFiles.m_items[i].fullPath.c_str();
		CString fileExt = dirFiles.m_items[i].ext.c_str();
		
		if (dirFiles.m_items[i].fileName==L"FindTextKeyWord.txt"||
			dirFiles.m_items[i].fileName == L"FindTextKeyWord.txt.bak"
			)
			continue;

		//
		if (fileExt == "bin")
		{
			// 读文件内容.
			FILE* fp;
			_wfopen_s(&fp, fileName.GetString(), L"rb");
			std::string fileText;
			while (!feof(fp))
			{
				fileText.push_back(getc(fp));
			}
			fclose(fp);

			std::vector<std::string> vsWord;
			std::string word;
			for (int i = 0; i < fileText.size(); ++i)
			{
				if (fileText[i])
					word += fileText[i];
				else
				{
					vsWord.push_back(word);
					word = "";
				}
			}
			if (word.size())
				vsWord.push_back(word);

			for (int i = 0; i < vsWord.size(); ++i)
			{
				std::string& word = vsWord[i];
				BYTE* utf8Text = UTF8ToANSI2(word.c_str(), CP_UTF8, CP_ACP);
				BYTE* s = utf8Text;
				while (*s)
				{
					CString keyword;
					FindText_CharNode* node = keys[*(BYTE*)s];
					if (node != NULL)
					{
						if (FindText_CharNode::FindKeyWord(keyword, s, node))
						{
							fwprintf(fpOutput, L"[%s] %s \n", keyword.GetString(), fileName.GetString());
							wprintf_s(L"[%s] %s\n", keyword.GetString(), fileName.GetString());
						}
					}
					++s;
				}
				free(utf8Text);
			}
		}/*
		if (fileExt == "c" || fileExt == "cpp" || fileExt == "cxx" || fileExt == "cc" || fileExt == "tli" || fileExt == "tlh" || fileExt == "h" ||
			fileExt == "hh" || fileExt == "hpp" || fileExt == "hxx" || fileExt == "inl" || fileExt == "rc" || fileExt == "resx" || fileExt == "asm" || 
			fileExt == "inc" || fileExt == "xml" || fileExt == "txt" || fileExt == "ini" || fileExt == "Readme" || fileExt == ""
			)*/
		else
		{
			// 读文件内容.
			FILE* fp;
			if (errno_t t = _wfopen_s(&fp, fileName.GetString(), L"rb"))
			{
				fwprintf(fpOutput, L"!open file %s failed!\n", fileName.GetString());
				continue;
			}
			std::string fileText;
			while (!feof(fp))
			{
				fileText.push_back(getc(fp));
			}
			fclose(fp);

			bool bIsUtf8 = false;
			if (fileText.size() >= 3)
			{
				unsigned char m1 = *(unsigned char*)&(fileText[0]);
				unsigned char m2 = *(unsigned char*)&(fileText[1]);
				unsigned char m3 = *(unsigned char*)&(fileText[2]);

				if (m1 == 0xef && m2 == 0xbb && m3 == 0xbf)
				{
					fileText.erase(fileText.begin());
					fileText.erase(fileText.begin());
					fileText.erase(fileText.begin());
					bIsUtf8 = true;
				}
			}
			fileText.resize(fileText.size() - 1);//删除最后的-1

			if (bIsUtf8)
			{
				BYTE* utf8Text = UTF8ToANSI2(fileText.c_str(), CP_UTF8, CP_ACP);
				BYTE* s = utf8Text;
				int line = 1;
				while (*s)
				{
					if (*s == '\n')
						++line;
					CString keyword;
					FindText_CharNode* node = keys[*(BYTE*)s];
					if (node != NULL)
					{
						if (FindText_CharNode::FindKeyWord(keyword, s, node))
						{
							fwprintf(fpOutput, L"[%s] %s line %d\n", keyword.GetString(), fileName.GetString(), line);
							wprintf_s(L"[%s] %s line %d\n", keyword.GetString(), fileName.GetString(), line);
						}
					}
					++s;
				}
				free(utf8Text);
			}
			else
			{
				const BYTE* s = (BYTE*)fileText.c_str();
				int line = 1;
				while (*s)
				{
					if (*s == '\n')
						++line;
					CString keyword;
					FindText_CharNode* node = keys[*(BYTE*)s];
					if (node != NULL)
					{
						if (FindText_CharNode::FindKeyWord(keyword, s, node))
						{
							fwprintf(fpOutput, L"[%s] %s line %d\n", keyword.GetString(), fileName.GetString(), line);
							wprintf_s(L"[%s] %s line %d\n", keyword.GetString(), fileName.GetString(), line);
						}
					}
					++s;
				}
			}
		}/*
		else
		{
			if (fileExt == fileName)
				exts2.insert(fileExt);
			else
				exts.insert(fileExt);
		}*/
	}
	
	CString msg = L"seek files:\n";
	std::set<CString>::iterator it = exts.begin();
	while (it != exts.end())
	{
		msg += it->GetString();
		msg += ";\n";
		++it;
	}
	fwprintf(fpOutput, L"%s", msg.GetString());

	msg = L"seek files2: \n";
	it = exts2.begin();
	while (it != exts2.end())
	{
		msg += it->GetString();
		msg += ";\n";
		++it;
	}
	fwprintf(fpOutput, L"%s", msg.GetString());

	fclose(fpOutput);
}
