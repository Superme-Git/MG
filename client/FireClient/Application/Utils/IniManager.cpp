/******************************************************************************
 文件名称：IniManager.cpp
 文件描述：CIniManager类的实现，该类完成ini文件的读写，并且支持创建新的section
 ********************************************************************************/
#include "stdafx.h"
#include "IniManager.h"
#include <iostream>
#include <fstream>
#include <vector>
#include "utils/FileUtil.h"

bool IniManager::RemoveSection(const string& stringSectionName)
{
	std::string strDocuments = CFileUtil::GetDocDir();
	strDocuments += "/";
	strDocuments += m_strSettingPath;


	m_vecSettingContent.clear();
	m_strContentLine = "";

	try
	{
		string stringCurrentSection;//当前的section名称
		basic_string <char>::size_type IndexFirst;//[的位置
		basic_string <char>::size_type IndexEnd;//]的位置
		int iIndex = 0;//vector的循环变量
		int iCorrectIndex = 0;//符合特征的值所在的vector索引
		ifstream streamSetting(strDocuments.c_str());//配置文件的文件流
		//打开配置文件流
		if (!streamSetting.is_open()) {
			return false;
		}

		//读取一行数据，直到文件结尾
		int i = 0;
		bool bDel = false;
		while (!streamSetting.eof())
		{
			++i;
			if (i > 200) {
				break;
			}
			getline(streamSetting, m_strContentLine, '\n');//读取一行数据
			IndexFirst = m_strContentLine.find_first_of("[");
			IndexEnd = m_strContentLine.find_first_of("]");
			//寻找一下，当前读取的内容里面是否包含[符号，如果包含，说明是section，提取section，否则加入当前的section到行首
			if (IndexFirst != m_strContentLine.npos)
			{
				//包含"[", 提取当前的section
				stringCurrentSection = m_strContentLine.substr(IndexFirst + 1, IndexEnd - IndexFirst - 1);

				bDel = (stringSectionName == stringCurrentSection) ? true : false;
			}
			if (bDel)	continue;
			m_vecSettingContent.push_back(m_strContentLine);//将数据放入vector中
		}
		//关闭文件流
		streamSetting.close();

		vector <string>::iterator it;
		string strTemp;

		ofstream output(strDocuments.c_str(), ios::out);//用来输出的文件流

		if (output.is_open())
		{
			int idx6 = 0;
			//将vector写入文件
			for (it = m_vecSettingContent.begin(); it != m_vecSettingContent.end(); it++)
			{
				++idx6;
				if (idx6 > 1000) {
					break;
				}
				strTemp = *it + '\n';
				output.write(strTemp.c_str(), strTemp.length());
			}

			output.close();
			return true;
		}
		else
		{
			return false;//文件打开失败
		}

		return true;
	}
	catch (...)
	{
		//发生异常，返回false，通知上层程序
		return false;
	}
}

bool IniManager::RemoveValueByName(const string& stringSectionName, const string& stringParameterName)
{
	m_vecSettingContent.clear();
	m_strContentLine = "";
	try
	{
		string stringLine;//读取出来一行
		string stringSection;//存储当前的section名称
		string stringTemp;//临时存放从iterator获取的字串
		vector <string> vectorContent;//用来存储内容的vector

		vector <string>::iterator iteratorString;//用于存储内容vector的迭代器
		vector <string>::iterator iteratorSection;//用于检查section的迭代器
		bool bExistTheSection = false;//默认当前的文件不存在指定的section

		string stringCurrentSection;//存储当期提取到的section名称

		//全部读出文件

		std::string strDocuments = CFileUtil::GetDocDir();
		strDocuments += "/";
		strDocuments += m_strSettingPath;


		ifstream streamSetting(strDocuments.c_str());
		bool bExistFile = streamSetting.is_open();
		if (bExistFile)
		{
			int idx = 0;
			while (!streamSetting.eof())
			{
				++idx;
				if (idx > 200)
				{
					break;
				}
				getline(streamSetting, stringLine, '\n');
				if (stringLine != "") //patch 1.1消除空行
				{
					vectorContent.push_back(stringLine);
				}
			}
			streamSetting.close();//关闭文件流
		}


		//使用迭代器对vector进行遍历，定位section，并寻找插入点
		bool findsection = false;
		int idx4 = 0;
		for (iteratorString = vectorContent.begin();
			iteratorString != vectorContent.end(); iteratorString++)
		{
			++idx4;
			if (idx4 > 1000)
			{
				break;
			}
			//取出一行，查看当前行是否包含"["符号，包含则提取section名称，和给定的名称对比，否则继续下一行

			stringTemp = *iteratorString;
			if (!findsection)
			{
				if (stringTemp.find_first_of("[") != string::npos)
				{
					//找到了[符号,提取section名称
					stringCurrentSection = stringTemp.substr(stringTemp.find_first_of("[") + 1,
						stringTemp.find_first_of("]") - 1);

					//判断section是否和制定的section相同,相同则继续，否则进行下一个
					if (stringCurrentSection == stringSectionName)
					{
						findsection = true;
					}
				}
			}
			else
			{
				basic_string <char>::size_type pos = stringTemp.find_first_of("=");
				if (pos != string::npos)
				{
					stringTemp = stringTemp.substr(0, pos);
					if (stringTemp == stringParameterName)
					{
						vectorContent.erase(iteratorString);
						break;
					}
				}
			}

		}
		ofstream output(strDocuments.c_str(), ios::out);//用来输出的文件流

		if (output.is_open())
		{
			int idx6 = 0;
			//将vector写入文件
			for (iteratorString = vectorContent.begin(); iteratorString != vectorContent.end(); iteratorString++)
			{
				++idx6;
				if (idx6 > 1000) {
					break;
				}
				stringTemp = *iteratorString + '\n';
				output.write(stringTemp.c_str(), stringTemp.length());
			}

			output.close();
			return true;
		}
		else
		{
			return false;//文件打开失败
		}
	}
	catch (...)
	{
		return false;
	}
}

bool IniManager::WriteValueByName(const wstring& stringSectionName, const wstring& stringParameterName,
	const wstring& wstringValue)
{
	std::string strSection = StringCover::to_string(stringSectionName);
	std::string strPara = StringCover::to_string(stringParameterName);
	std::string strValue = StringCover::to_string(wstringValue);

	bool bReture = WriteValueByName(strSection, strPara, strValue);
	return bReture;


}

bool IniManager::GetValueByName(const wstring& stringSectionName, const wstring& stringParameterName, wstring& stringValue)
{
	std::string strSection = StringCover::to_string(stringSectionName);
	std::string strPara = StringCover::to_string(stringParameterName);
	std::string strValue("");
	bool bReture = GetValueByName(strSection, strPara, strValue);
	stringValue = StringCover::to_wstring(strValue);
	return bReture;

}

/**********************************************************************
函数名称：WriteValueByName
函数描述：通过section名称，变量名称，设置的值，写入ini文件。
输入参数：stringSectionName,string常饮用类型，表示section名称。
stringParameterName，string常饮用类型，表示变量的名称。
stringValue，string常饮用类型，表示设置的值。
输出参数：N/A
返回值：  true，布尔类型，表示写入成功。
false,布尔类型，表示写入失败。
************************************************************************/
bool IniManager::WriteValueByName(const string& stringSectionName, const string& stringParameterName,
	const string& stringValue)
{
	m_vecSettingContent.clear();
	m_strContentLine = "";
	try
	{


		string stringLine;//读取出来一行
		string stringSection;//存储当前的section名称
		string stringTemp;//临时存放从iterator获取的字串
		vector <string> vectorContent;//用来存储内容的vector

		vector <string>::iterator iteratorString;//用于存储内容vector的迭代器
		vector <string>::iterator iteratorSection;//用于检查section的迭代器
		bool bExistTheSection = false;//默认当前的文件不存在指定的section

		string stringWanted = stringParameterName + "=" + stringValue;//要写入的字串，如：AnalyzeData=%ProgramPath%\AnalyzeData
		string stringCurrentSection;//存储当期提取到的section名称

		//全部读出文件

		std::string strDocuments = CFileUtil::GetDocDir();
		strDocuments += "/";
		strDocuments += m_strSettingPath;


		ifstream streamSetting(strDocuments.c_str());
		bool bExistFile = streamSetting.is_open();
		if (bExistFile)
		{
			int idx = 0;
			while (!streamSetting.eof())
			{
				++idx;
				if (idx>200)
				{
					break;
				}
				getline(streamSetting, stringLine, '\n');
				if (stringLine != "") //patch 1.1消除空行
				{
					vectorContent.push_back(stringLine);
				}
			}
			streamSetting.close();//关闭文件流
		}

		//在进一步处理之前，首先对section进行检查，如果当前不存在，则创建它
		int idx2 = 0;
		for (iteratorSection = vectorContent.begin();
			iteratorSection != vectorContent.end(); iteratorSection++)
		{
			++idx2;
			if (idx2>1000)
			{
				break;
			}
			if (*iteratorSection == ("[" + stringSectionName + "]"))
			{
				bExistTheSection = true;
				break;
			}
		}
		//经过上面的遍历，无法找到指定的section，则创建它
		if (!bExistTheSection)
		{
			vectorContent.push_back("[" + stringSectionName + "]");
		}

		int idx3 = 0;
		//patch 1.1 修复设置重复的bug
		for (iteratorString = vectorContent.begin();
			iteratorString != vectorContent.end(); iteratorString++)
		{
			//对每一行进行遍历，如果是section，则记录当前的section，否则，就查找指定的字串，
			//找到后判断当前的section是否是指定的section，是的话，删除，否则记录下一个
			//取出来一行
			++idx3;
			if (idx3>1000) {
				break;
			}
			stringTemp = *iteratorString;
			//判断是否是section,然后更改stringSection的值
			if (stringTemp.find_first_of("[") != string::npos)
			{
				//找到了[符号，提取section名称
				stringSection = stringTemp.substr(stringTemp.find_first_of("[") + 1,
					stringTemp.find_first_of("]") - 1);
			}
			if (stringTemp.find(stringParameterName + "=") != string::npos)
			{
				//找到了特征字符串,确保当前section是指定的section，否则不删除，主要是保证不同的section可以有相同的parameter
				if (stringSection == stringSectionName)
				{
					vectorContent.erase(iteratorString);
					break;//找到后就不再继续
				}
			}
		}
		//end patch 1.1
		//使用迭代器对vector进行遍历，定位section，并寻找插入点
		int idx4 = 0;
		for (iteratorString = vectorContent.begin();
			iteratorString != vectorContent.end(); iteratorString++)
		{
			++idx4;
			if (idx4>1000)
			{
				break;
			}
			//取出一行，查看当前行是否包含"["符号，包含则提取section名称，和给定的名称对比，否则继续下一行

			stringTemp = *iteratorString;
			if (stringTemp.find_first_of("[") != string::npos)
			{
				//找到了[符号,提取section名称
				stringCurrentSection = stringTemp.substr(stringTemp.find_first_of("[") + 1,
					stringTemp.find_first_of("]") - 1);

				//判断section是否和制定的section相同,相同则继续，否则进行下一个
				if (stringCurrentSection == stringSectionName)
				{
					//相同，开始寻找合适的位置，进行插入
					//迭代器没有到达文件结束并且没有找到
					int idx5 = 0;
					while ((stringTemp.find_first_of("[") == string::npos) && (iteratorString
						!= vectorContent.end()))
					{
						++idx5;
						if (idx5>1000)
						{
							break;
						}
						iteratorString++;
					}

					//直接添加
					vectorContent.insert(iteratorString + 1, stringWanted);
					break;
					//}
				}
			}
		}
		ofstream output(strDocuments.c_str(), ios::out);//用来输出的文件流

		if (output.is_open())
		{
			int idx6 = 0;
			//将vector写入文件
			for (iteratorString = vectorContent.begin(); iteratorString != vectorContent.end(); iteratorString++)
			{
				++idx6;
				if (idx6>1000) {
					break;
				}
				stringTemp = *iteratorString + '\n';
				output.write(stringTemp.c_str(), stringTemp.length());
			}

			output.close();
			return true;
		}
		else
		{
			return false;//文件打开失败
		}
	}
	catch (...)
	{
		return false;
	}
}

/**********************************************************************
函数名称：ConnectTwoString
函数描述：连接section名字和某一行，使其成为"section_特定行内容"的形式。
输入参数：stringSectionName,string常饮用类型，表示section名称。
输出参数：stringContentLine,string饮用类型，表示处理后的特定行内容。
返回值：  N/A
************************************************************************/
void IniManager::ConnectTwoString(const string& stringSectionName, string& stringContentLine)
{
	stringContentLine = stringSectionName + "_" + stringContentLine;
}

/**********************************************************************
函数名称：GetValueByName
函数描述：通过section名称，变量名称来获取设置的值
输入参数：stringSectionName,string常饮用类型，表示section名称。
stringParameterName，string常饮用类型，表示变量的名称。
输出参数：stringValue，string类型，用来带回设置的值。
返回值：  true，布尔类型，表示获取成功。
false,布尔类型，表示获取失败。
************************************************************************/
bool IniManager::GetValueByName(const string& stringSectionName, const string& stringParameterName, string& stringValue)
{
	std::string strDocuments = CFileUtil::GetDocDir();
	strDocuments += "/";
	strDocuments += m_strSettingPath;

	m_vecSettingContent.clear();
	m_strContentLine = "";

	try
	{
		string stringWanted = stringSectionName + "_" + stringParameterName;//存储要查找的字串

		string stringCurrentSection;//当前的section名称
		basic_string <char>::size_type IndexFirst;//[的位置
		basic_string <char>::size_type IndexEnd;//]的位置
		unsigned int iIndex = 0;//vector的循环变量
		int iCorrectIndex = 0;//符合特征的值所在的vector索引
		ifstream streamSetting(strDocuments.c_str());//配置文件的文件流
		//打开配置文件流
		if (!streamSetting.is_open()) {
			return false;
		}

		//读取一行数据，直到文件结尾
		int i = 0;
		while (!streamSetting.eof())
		{
			++i;
			if (i>200) {
				break;
			}
			getline(streamSetting, m_strContentLine, '\n');//读取一行数据
			IndexFirst = m_strContentLine.find_first_of("[");
			IndexEnd = m_strContentLine.find_first_of("]");
			//寻找一下，当前读取的内容里面是否包含[符号，如果包含，说明是section，提取section，否则加入当前的section到行首
			if (IndexFirst != m_strContentLine.npos)
			{
				//包含"[", 提取当前的section
				stringCurrentSection = m_strContentLine.substr(IndexFirst + 1, IndexEnd - IndexFirst - 1);
			}
			else if (m_strContentLine != "")
			{
				//加入当前的section到行首
				ConnectTwoString(stringCurrentSection, m_strContentLine);//为了支持不同section下可以使用同名的parameter，因此在parameter前，加上section名字
			}
			m_vecSettingContent.push_back(m_strContentLine);//将数据放入vector中
		}//end  while (!streamSetting.eof())
		//关闭文件流
		streamSetting.close();
		//在vector中查找stringWanted
		int iTempIndex = 0;
		string strTemp;
		for (iIndex = 0; iIndex < m_vecSettingContent.size(); iIndex++)
		{
			if (iIndex>1000) {
				break;
			}
			strTemp = m_vecSettingContent[iIndex];
			iTempIndex = strTemp.find(stringWanted.c_str());

			//当成功找到特定字符串时，记录当前的索引
			size_t leftIdx = strTemp.find_first_of("=");
			std::string subTemp("");
			if (leftIdx != strTemp.npos)
			{
				subTemp = strTemp.substr(0, leftIdx);
			}

			if (subTemp == stringWanted)
			{
				iCorrectIndex = iIndex;
				//分离出值，并返回
				iTempIndex = m_vecSettingContent[iCorrectIndex].find_first_of("=");
				if (iTempIndex != m_vecSettingContent[iCorrectIndex].npos)
				{
					stringValue = m_vecSettingContent[iCorrectIndex].substr(
						iTempIndex + 1, m_vecSettingContent[iCorrectIndex].size() - iTempIndex);
					return true;
				}
			}
		}

		return false;
	}
	catch (...)
	{
		//发生异常，返回false，通知上层程序
		stringValue = "";
		return false;
	}
}

/**********************************************************************
函数名称：CIniManager
函数描述：CIniManager类的构造函数，进行初始化
输入参数：SettingPath,string类型，ini文件的路径，带文件名
输出参数：N/A
返回值：N/A
************************************************************************/
IniManager::IniManager(string SettingPath)
{
	m_strSettingPath = SettingPath;
}
