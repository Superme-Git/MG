/***********************************************************************
filename: 	IconManager.h
purpose:	游戏图标管理(技能及道具图标)
*************************************************************************/
#pragma once
#include "Singleton.hpp"

class IconManager : public CSingleton<IconManager>
{
public:
	IconManager();
	~IconManager();
	
	const CEGUI::String GetSkillIconPathByID(int id);   // 根据技能的id获得它的图标的标识文字 (imgeset+name),空则返回空字符串
	const CEGUI::String GetItemIconPathByID(int id);    // 根据道具的id获得它的图标的标识文字 (imgeset+name),空则返回空字符串
	const CEGUI::String GetImagePathByID(int id);

	const CEGUI::Image& GetTraceIcon();			//得到任务追踪标志
	const CEGUI::Image* GetBuffIconByID(int id);	//根据buff的id获得它的图标,空则返回NULL
	const CEGUI::Image* GetSkillIconByID(int id);	//根据技能的id获得它的图标,空则返回NULL
	const CEGUI::Image* GetItemIconByID(int id);	//根据道具的id获得它的图标,空则返回NULL
	const CEGUI::Image* GetImageByID(int id);		//根据图片的id获得图片

	const CEGUI::Image& getDefaultIcon();

	// 根据表情的获得它的的标识文字 (imgeset+name),空则返回空字符串 
	const CEGUI::Image* GetEmotionImageByFrame(int aniID, int frame);
	const CEGUI::String GetEmotionIconPathByFrame(int aniID, int frame);
private:

	//根据称谓ID得到它对应的图
	CEGUI::String GetTitleIconPathByID(int titleID);

	void DestroyImageSet(const CEGUI::String& name); //销毁一个imageset
	void CreateImageSet(const CEGUI::String& name);  //创建一个imageset

	CEGUI::String FindImageSetNameByID(int id);	//通过ID寻找所在的imageset的名字,如果找不到名字为空
	void InitIconInf();						//初始化图标信息

	void AddCardImageSet();
	void AddTitleImageSet();

	void InitImagesetSizeInfo(std::vector<int>& vec, CEGUI::String prefix, int fromNum, int maxNum);
	
	typedef std::vector<CEGUI::String> IconImageSetList;
	typedef std::vector<CEGUI::String>::iterator IconImageSetListIt;
	typedef std::map<CEGUI::String, CEGUI::String> IconInfMap;
	typedef std::map<CEGUI::String, CEGUI::String>::iterator IconInfMapIt;

protected:
private:
	IconImageSetList m_vecIconImageSet;
	IconInfMap m_mapIconInfMap;
	const static int m_nItemIconSize=64;	//每一个imageset的道具图片数
	const static int m_nSkillIconSize=64;	//每一个imageset的技能图片数
	const static int m_nBufferSize=16;		//每一个imageset的表情图片数
	const static int m_nRoleAndMonsterSize=128;
	const static int m_nRightTopRoleSize=84;
	const static int m_nRightTopPetSize=62;
	const static int m_nServentSize=72;
	const static int m_nRoleDlgHeadSize=153;
	const static int m_nSellPetIconSize=42; //售卖宠物图标大小
	
	const static int m_nImageSetSize=256;  //每一张imageSet的大
    
	std::vector<int> m_vecPetImagesetSize; //宠物每张imageset里有几个图标，序号从0开始 by lg
};

inline IconManager* gGetIconManager()
{
	return IconManager::GetInstance();
}
