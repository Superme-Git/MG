#ifndef FireClient_IniManager_h
#define FireClient_IniManager_h

#pragma warning(disable:4786)

#include <string>

using namespace std;
class IniManager
{
public:
	bool RemoveSection(const string& stringSectionName);
	bool RemoveValueByName(const string& stringSectionName, const string& stringParameterName);
	bool GetValueByName(const wstring& stringSectionName, const wstring& stringParameterName, wstring& stringValue);

	/**********************************************************************
	函数名称：GetValueByName
	函数描述：通过section名称，变量名称来获取设置的值
	输入参数：stringSectionName,string常饮用类型，表示section名称。
	stringParameterName，string常饮用类型，表示变量的名称。
	输出参数：stringValue，string类型，用来带回设置的值。
	返回值：  true，布尔类型，表示获取成功。
	false,布尔类型，表示获取失败。
	************************************************************************/
	bool GetValueByName(const string& stringSectionName, const string& stringParameterName, string& stringValue);

	/**********************************************************************
	函数名称：CIniManager
	函数描述：CIniManager类的构造函数，进行初始化
	输入参数：SettingPath,string类型，ini文件的路径，带文件名
	输出参数：N/A
	返回值：N/A
	************************************************************************/
	IniManager(string SettingPath);

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
    bool WriteValueByName(const string& stringSectionName, const string& stringParameterName, const string& stringValue);
	bool WriteValueByName(const wstring& stringSectionName, const wstring& stringParameterName, const wstring& wstringValue);

private:
	/**********************************************************************
	函数名称：ConnectTwoString
	函数描述：连接section名字和某一行，使其成为"section_特定行内容"的形式。
	输入参数：stringSectionName,string常饮用类型，表示section名称。
	输出参数：stringContentLine,string饮用类型，表示处理后的特定行内容。
	返回值：  N/A
	************************************************************************/
	void ConnectTwoString(const string& stringSectionName, string& stringContentLine);

	string m_strContentLine;//配置文件中的某一行内容
	vector <string> m_vecSettingContent;//配置文件的全部内容
	string m_strSettingPath;//配置文件的路径
};
#endif
