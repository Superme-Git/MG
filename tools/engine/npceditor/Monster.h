#pragma once

struct stMonsterBaseData
{
	int BaseID;
	std::wstring strMonsterColor;
	std::wstring strMonsterDescribe;
	
	stMonsterBaseData()
	{
		BaseID=-1;
		strMonsterColor=L"";
		strMonsterDescribe=L"";
	}

	
	stMonsterBaseData(const stMonsterBaseData& rhs)
	{
		BaseID = rhs.BaseID;
		strMonsterColor = rhs.strMonsterColor;
		strMonsterDescribe = rhs.strMonsterDescribe;
	}
	
	stMonsterBaseData& operator=(const stMonsterBaseData& rhs)
	{
		BaseID = rhs.BaseID;
		strMonsterColor = rhs.strMonsterColor;
		strMonsterDescribe = rhs.strMonsterDescribe;
	}

};