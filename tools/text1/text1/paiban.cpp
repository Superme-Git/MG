#include "stdafx.h"
#include "filesystem.h"
#include <stack>
#include <vector>
#include <list>
#include <set>
#include <map>
/*
class ValueDefine
{
};

class Value
{

};

class FunctionDefine
{

};


class Function
{

};

class StructDef
{

};

class ClassDef
{

};

class IncludeFile
{

};

class NameSpace
{

};

class CPPFile
{
};
*/

/*
class CodeAttrib
{
public:
	std::vector<std::string> mComplicateSwite; //编译开关，可能受到好几个开源影响。
	std::vector<std::string> mNameSpace; //所在的命名空间,可能是好几层。
};

// 变量定义.
class ValueInfo : public CodeAttrib
{
public:
};

// 函数定义.
class FunctionInfo : public CodeAttrib
{
public:
	std::string mFunName;
	std::string mReturnClass; //返回类型.
	std::vector<ValueInfo> mParams;
	bool mIsConst; //函数是否是常量。
	bool mIsStatic;//函数是否是静态的。
	bool mIsVirtual;//函数是否是虚拟的。
	// SyntexBlock mBlock;//函数的语法块。
public:
	void process(const char* text);
};

// 类信息.
class ClassInfo : public CodeAttrib
{
public:
	std::string mNameSpace; // 所在的名字空间。
	std::string mClassName; // 类名字。
	std::vector<ClassInfo> mBaseClassDef;//所有的基类定义。
	std::vector<ValueInfo> mValues; //所有的变量。
	std::vector<FunctionInfo> mFunctions; //所有的函数定义。
};

class HFile
{
public:
	std::vector<std::string> mIncludes;
	std::vector<std::string> mPragmas;
	std::vector<ClassInfo> mClass;
	std::vector<FunctionInfo> mFunctions;
	std::vector<ValueInfo> mValues;
};

std::map<std::string, ClassInfo> ga_Class;


// 语法块,中间结构.
class SyntexBlock : public CodeAttrib
{
	std::string mTitle; //这个块前面的文本.
	std::vector<SyntexBlock*> maChild; // 所有的子块.
public:
	void AddBlock(SyntexBlock* sb)
	{
		maChild.push_back(sb);
	}
	void AddTitle(const std::string& text)
	{
		SyntexBlock* sb = new SyntexBlock;
		sb->mTitle = text;
	}
	void Parse()
	{

	}
};

SyntexBlock g_Blocks;

//
std::string GetBlockText(const char*& s)
{
	std::string blockText;
	bool quotation = false;
	std::string quotationText;
	int level = 0;
	while (*s)
	{
		if (*s == '"')
			quotation = !quotation;

		if (quotation)
			quotationText += *s;
		else if (*s == '{')
			++level;
		else if (*s == '}')
			--level;

		if (level == 0)
			return blockText;

		++s;
	}

	return blockText;
}

// 生成语法块.
SyntexBlock* MakeSyntexBlocks(SyntexBlock& sb, const char* s)
{
	SyntexBlock* retSb = new SyntexBlock;
	std::string titleText;
	bool quotation = false; // 是否处于引号之中.
	while (*s)
	{
		if (*s == '"')
			quotation = !quotation;

		if (quotation)
		{
			titleText += *s;
		}
		else
		{
			if (*s == '{')
			{
				std::string blockText = GetBlockText(s);
				retSb->AddTitle(titleText);
				titleText.clear();
				retSb->AddBlock(MakeSyntexBlocks(sb, blockText.c_str()));
			}
			else
				titleText += *s;
		}
		++s;
	}

	return retSb;
}
*/

// 删除注释.
std::string DeleteCommented(const std::string& inText)
{
	std::string ret;
	bool comment1 = false;
	bool comment2 = false;
	char lastChar = ' ';
	const char* s = inText.c_str();
	while (*s)
	{
		if (*s == '"')// 跳过引号.
		{
			++s;
			while (*s)
			{
				if (*s == '"')
				{
					++s;
					break;
				}
				++s;
			}
		}

		if (comment1)
		{
			if (*s == '\n'&&lastChar != '\\')
			{
				comment1 = false;
			}
			else
			{
				if (*s != ' '&&*s != '\t')
					lastChar = *s;
				++s;
				continue;
			}
		}

		if (comment2)
		{
			if (*s == '*' && *(s + 1) == '/')
			{
				++s;
				comment2 = false;
			}
			else
			{
				++s;
				continue;
			}
		}

		if (*s == '/' && *(s + 1) == '*')
		{
			++++s;
			comment2 = true;
			continue;
		}

		if (*s == '/' && *(s + 1) == '/')
		{
			++++s;
			comment1 = true;
			lastChar = 0;
			continue;
		}

		ret += *s;
		++s;
	}

	return ret;
}

// 获取括号中的文本.
bool GetKuoHaoText(std::string& ret,const char*& s)
{
	std::stack<char> signStack;
	while (*s)
	{
		if (*s == '{')
			signStack.push('}');
		else if (*s == '[')
			signStack.push(']');
		else if (*s == '(')
			signStack.push(')');
		else if (*s == '<')
			signStack.push('>');

		if (signStack.top() == *s)
		{
			signStack.pop();
		}
		if (signStack.size() == 0)
		{
			return true;
		}
		ret += *s;
		++s;
	}

	return false;
}

class IType;
class CSyntexBlock
{
public:
	std::map<std::string, CSyntexBlock*> mChilds;
public:
	bool MakeBlocks(const std::vector<std::string>& words, int& index)
	{
		while (index < words.size())
		{
			std::string word = words[index];
			if (word == "#") // 首先识别预处理.
			{
				//mChilds.insert()
			}
			else if (word == "namespace")//
			{
				std::string name = words[index + 1];
				//mChilds.insert(std::pair<std::string,CSyntexBlock*>(name));
			}
			else if (word == "template")
			{

			}
			else if (word == "class")//
			{

			}
			else if (word == "struct")
			{

			}
			else if (word == "union")
			{

			}
			else if (word == "public:")
			{

			}
			else if (word == "protected:")
			{

			}
			else if (word == "private:")
			{

			}
		}
		return true;
	}
};

enum AuthType
{
	AT_PUBLIC,
	AT_PROTECTED,
	AT_PRIVATE
};

class IType
{
protected:
	AuthType mAt;
public:
	virtual bool IsEnum(){ return false; }
	virtual bool IsClass(){ return false; }
	virtual bool IsStruct(){ return false; }
	virtual bool IsNameSpace(){ return false; }
	virtual bool IsFunction(){ return false; }
	virtual bool IsValue(){ return false; }
	virtual bool IsTypeDef(){ return false;  }
};

class CTypeDef : public IType
{
public:
	bool IsTypeDef(){ return true; }
	CTypeDef(AuthType at, const CSyntexBlock& block)
	{
	}
};

class CClass : public IType
{
public:
	CClass(AuthType at, const CSyntexBlock& block){}
};

class CStruct :public IType
{
public:
	CStruct(AuthType at, const CSyntexBlock& block){}
};

class CUnion :public IType
{
public:
	CUnion(AuthType at, const CSyntexBlock& block){}
};

class CEnum :public IType
{
public:
	CEnum(AuthType at, const CSyntexBlock& block){}
};

class CNameSpace : public IType
{
public:
	class std::map<std::string, IType*> mDeclars;//所有的定义.
public:
	bool IsNameSpace(){ return true; }
	void AddContext(CSyntexBlock* block) // 增加内容.
	{
		/*
		AuthType at = AT_PRIVATE;
		for (int i = 0; i < block->GetBlocks().size(); ++i)
		{
			const CSyntexBlock& childBlock = block->GetBlocks()[i];
			const std::vector<std::string>& words = childBlock.GetWords();
			if (words[0] == "public:"){	at = AT_PUBLIC;	}
			else if (words[0] == "protected:"){	at = AT_PROTECTED;	}
			else if (words[0] == "private:"){	at = AT_PRIVATE;	}
			else if (words[0] == "enum"){	mDeclars.insert(std::pair<std::string,IType*>(words[1], new CEnum(at, childBlock)));	}
			else if (words[0] == "typedef"){	mDeclars.insert(std::pair<std::string,IType*>(words[1], new CTypeDef(at, childBlock)));	}
			else if (words[0] == "struct"){	mDeclars.insert(std::pair<std::string,IType*>(words[1], new CStruct(at, childBlock)));	}
			else if (words[0] == "class"){	mDeclars.insert(std::pair<std::string,IType*>(words[1], new CClass(at, childBlock)));	}

			// 剩下的下个阶段，所有定义读入成功再处理。
		}*/
	}
};

std::vector<std::string> gCppTexts; //所有的CPP原始文件.
std::vector<std::string> gNoCommentedCppTexts; //所有的无注释的CPP文件.
CSyntexBlock gRootBlock; //根语法块.
class std::map<std::string, IType*> gDeclars;

// 解析定义文件.
void ParseText(std::string& inText)
{
	// 第一阶段，删除注释.
	std::string parseText = DeleteCommented(inText);
	/*
	// 第二阶段，解析小括号，中括号，大括号，书括号,#和标识符。
	CSyntexBlock rootWord;
	if (false == rootWord.Parse(parseText.c_str()))
		return;
		*/
	//gBlocks.push_back(rootWord);
}

// 加载所有的文件文本.
void LoadCPPFile(dir& dirFiles)
{
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(L"h");
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		std::string inText;
		CStdioFile file;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			char c;
			while (file.Read(&c, 1))
			{
				inText.push_back(c);
			}
			file.Close();
		}
		gCppTexts.push_back(inText);
	}
}

std::vector<std::string> FormatCppText(const char* s)
{
	// 在重点符号左右插入空格，以便形成单独的词.
	std::string text;
	while (*s)
	{
		switch (*s)
		{
		case '+':
		case '-':
		case '*':
		case '/':
		case '=':
		case '&':
		case ':':
		case '#':
		case '<':
		case '>':
		case '{':
		case '}':
		case '[':
		case ']':
		case '(':
		case ')':
			text += " ";
			text += *s;
			text += " ";
			break;
		default:
			text += *s;
			break;
		}
		++s;
	}

	// 生成单词表.
	std::vector<std::string> words = stovs(text.c_str(), ' ','\n','\r','\t');

	// 删除空行.
	std::vector<std::string>::iterator it = words.begin();
	while (it != words.end())
	{
		if (!it->size())
			it = words.erase(it);
		else
			++it;
	}

	// 返回.
	return words;
}

std::string GetFormatWord(const char* line)
{
	//如果发现空格，则调整为1个.
	std::string ret;
	bool enterSpace = false;
	while (*line)
	{
		if (*line == ' ' || *line == '\t')
		{
			if (enterSpace)
			{
				++line;
				continue;
			}
			else
				enterSpace = true;
		}
		else
		{
			enterSpace = false;
		}
		ret += *line;
	}

	// 如果第一个字符为空格，或者最后一个字符为空格，则删除.
	if (ret[0] == ' ' || ret[0] == '\t')
		ret.erase(ret.begin());
	if (ret.size()&&ret[ret.size() - 1] == ' ' || ret[ret.size() - 1] == '\t')
	{
		ret.resize(ret.size() - 1);
	}

	// 返回.
	return ret;
}

// 生成语法块.
bool CreateSyntexBlocks(CSyntexBlock& parentSyntexBlock, const std::string& text)
{
	// 首先格式化文本.
	std::vector<std::string> vsWord = FormatCppText(text.c_str());

	//
	int index = 0;
	parentSyntexBlock.MakeBlocks(vsWord,index);

	//
	return true;
}

// 提取宏定义，并且进行预处理.
std::map<std::string,std::string> gDefines;
std::vector<std::string> PreProcessCPPFiles()
{
	// 解析成句子.
	std::vector<std::string> words;/*
	std::string word;
	for (int i = 0; i < gCppTexts.size(); ++i)
	{
		const char* s = gCppTexts[i].c_str();
		while (*s)
		{
			if (*s == '\n')
			{
				words.push_back(word);
				word = "";
			}
			else
				word += *s;
			++s;
		}
	}
	if (word.size())
		words.push_back(word);

	// 提取#打头的句子.
	for (int i = 0; i < words.size(); ++i)
	{
		for (int j = 0; j < words[i].size(); ++j)
		{
			if (words[i][j] == ' ' || words[i][j] == '\t')
				continue;
			if (words[i][j] != '#')
				continue;
			std::string formatWord = GetFormatWord(words[i].c_str());
			if (!memcmp(formatWord.c_str(), "#define", 7))
			{
				char defName[256];
				char defText[256];
				sscanf_s(formatWord.c_str(), "%s %s", defName, defText);
				gDefines.insert(std::pair<std::string,std::string>(defName,defText));
			}
		}
	}

	//扫描文本，进行预处理替换.
	for (int i = 0; i < gCppTexts.size(); ++i)
	{
		std::string newCppText;
		const char* s = gCppTexts[i].c_str();
		std::string word;
		while (*s)
		{
			if ((*s >= 'a'&&*s <= 'z') || (*s >= 'A'&&*s <= 'Z') || (*s >= '0'&&*s <= '9'))
			{
				word.push_back(*s);
			}
			else
			{
				if (word.size())
				{
					std::map<std::string, std::string>::iterator it = gDefines.find(word);
					if (it != gDefines.end())
					{
						// 取代码中的参数.
						if (word[0] == '(')
						{
							std::string params;
							const char* wordStr = word.c_str();
							GetKuoHaoText(params, wordStr);
							std::vector<std::string> vsParams = stovs(params.c_str(),',');
						}

						// 取定义文本.？？？？？？？
						std::string& defineText = it->second;

						newCppText += it->second;
					}
				}
			}
			newCppText += *s;
			++s;
		}
	}
	*/
	//
	return words;
}

// 排版功能.
void myPaiBan(CString& path, std::vector<CString>& vsTypes)
{
	// 加载所有的源代码.
	dir dirFiles;
	dirFiles.push_files(path);
	for (int i = 0; i < vsTypes.size(); ++i)
	{
		if (vsTypes[i] == "h")
		{
			LoadCPPFile(dirFiles);
		}
		if (vsTypes[i] == "cpp")
		{
			LoadCPPFile(dirFiles);
		}
	}
	// 删除注释.
	for (int i = 0; i < gCppTexts.size(); ++i)
	{
		gNoCommentedCppTexts.push_back(DeleteCommented(gCppTexts[i]));
	}
	// 生成语法块.
	for (int i = 0; i < gNoCommentedCppTexts.size(); ++i)
	{
		if (!CreateSyntexBlocks(gRootBlock,gNoCommentedCppTexts[i]))
		{
			printf("make syntex failed!\n");
			return;
		}
	}
	// 预处理.
	/*
	for (int i = 0; i < gBlocks.size(); ++i)
	{
		gBlocks[i]
	}
	*/
	// 识别名字空间，枚举，联合，类型，结构，函数，变量。
	/*
	for (int i = 0; i < gBlocks.size(); ++i)
	{
		gBlocks[i].MakeDeclars();
	}
*/

	/*
	std::vector<std::wstring>& vwsFiles = dirFiles.get_files_by_type(type);
	for (int i = 0; i < vwsFiles.size(); ++i)
	{
		std::string inText;
		CStdioFile file;
		if (file.Open(vwsFiles[i].c_str(), CFile::modeRead | CFile::typeText))
		{
			char c;
			while (file.Read(&c, 1))
			{
				inText.push_back(c);
			}
			file.Close();
		}

		DeleteFile(vwsFiles[i].c_str());

		int pos = MultiByteToWideChar(CP_UTF8, 0, inText.c_str(), inText.length(), 0, 0);
		wchar_t* wstr = (wchar_t*)malloc(pos * 2 + 2);
		MultiByteToWideChar(CP_UTF8, 0, inText.c_str(), inText.length(), wstr, pos + 1);

		CStdioFile file2;
		if (file2.Open(vwsFiles[i].c_str(), CFile::modeCreate | CFile::modeWrite | CFile::typeText))
		{
	*/
}

/*
//////////////////////////// cpp
// 取一个完整的标识符，或者取括号中完整的文本.
bool CSyntexBlock::MakeBlocks()
{
	const char* s = mText.c_str();
	std::string word;
	bool enterSign = false;
	bool enterDefine = false;
	while (*s)
	{
		// 记录预处理.
		if (*s == '#')
		{
			enterDefine = true;
		}
		if (enterDefine)
		{
			if (*s == '\r' || *s == '\n')
			{
				mBlockTexts.push_back(word);
				word = "";
				enterDefine = false;
			}
		}

		// 跳过字符串.
		if (*s == '"')
		{
			enterSign = !enterSign;
		}

		// 获取括号中的文本.
		if (*s == '<' || *s == '{' || *s == '[' || *s == '(')
		{
			std::string ret;
			if (GetKuoHaoText(ret, s))
			{
				return false;
			}
			mBlockTexts.push_back(ret);
		}
		else if ((*s >= 'a'&&*s <= 'z') || (*s >= 'A'&&*s <= 'Z') || (*s >= '0'&&*s <= '9'))
		{
			word += *s;
		}
		else
		{
			mBlockTexts.push_back(word);
			word = "";
		}
	}

	return true;
}
// 是否是一段括号中的文本.
bool CSyntexBlock::IsKuoHaoBlock()
{
	return mText[0] == '<' || mText[0] == '{' || mText[0] == '[' || mText[0] == '('; 
}
// 是否是预处理所在行.
bool CSyntexBlock::IsPreProcess()
{
	return mText[0] == '#'; 
}
// 解析文本.
bool CSyntexBlock::Parse(const char* s)
{
	mText = s;
	MakeBlocks();
	for (int i = 0; i < mBlockTexts.size(); i++)
	{
		CSyntexBlock w;
		w.mText = mBlockTexts[i];
		if (IsKuoHaoBlock())
		{
			if (!w.Parse(mBlockTexts[i].c_str()))
			{
				return false;
			}
		}
	}
	return true;
}
const std::vector<std::string>& CSyntexBlock::GetWords()const 
{
	return mWords; 
}
const std::vector<CSyntexBlock>& CSyntexBlock::GetBlocks()const 
{
	return mBlocks; 
}
bool CSyntexBlock::MakeDeclars()
{
	if (mWords[0] == "namespace")
	{
		std::string& name = mWords[1];
		std::map<std::string, IType*>::iterator it = gDeclars.find(name);
		if (it != gDeclars.end())
		{
			if (it->second->IsNameSpace())
			{
				CNameSpace* nameSpace = (CNameSpace*)it->second;
				nameSpace->AddContext(this);
			}
		}
		else
		{
			CNameSpace* nameSpace = new CNameSpace();
			nameSpace->AddContext(this);
		}
	}
	else if ("class")
	{

	}

	return true;
}
*/
