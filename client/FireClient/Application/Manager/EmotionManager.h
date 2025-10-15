/***********************************************************************
filename: 	IconManager.h
purpose:	表情管理()
*************************************************************************/
#pragma once
#include "Singleton.hpp"


struct stEmotionInf 
{
	int id;
	std::wstring accesskey;		//快捷方式
	std::wstring tips;			//描述
	float duration;				//持续时间  即多少秒播放完一轮
	int toalFrame;				//总帧数
};

struct stImageSetInf 
{
	int id;
	int endImage;				//imageset结束的image
	int startImage;				//imageset开始的image

	stImageSetInf(int _id, int _startImage, int _endImage);
};

class EmotionManager : public CSingleton<EmotionManager>
{
public: 
	EmotionManager();
	~EmotionManager();

	const static int s_iPerImageSetEmotioIconNum = 144;		//每一个imageset包含的表情图标数

	bool isNextCharEmotionExist(int id);					//是否存在下一位的表情，比如说当id=20时存不存在id为210-219的表情
	bool isEmotionExist(int id);							//某个表情是否存，返回true表明该iD的表情存在
	void ClearEmotionPackInf();								//返回登陆界面时清表情包信息
	bool isEmotionCanUse(int id);							//某个表情是否可用
	bool CheckRichEditBoxEmotionCanUse(CEGUI::RichEditbox* pBox);	//检查输入框里的所有表情是否可以
	int GetPackIDByEmotionID(int id);						//根据表情id获得对应的包
	bool isEmotionPackCanUse(int id);
	bool isEmotionPackFree(int id);
	const std::map<int, int64_t>& GetEmotionPackInf();
	int64_t GetEmotionPackTime(int packID);					//得到某个表情包的剩余时间，单位毫秒数
	void RefreshEmotionPackTime(int elapse);
	void RefreshEmotionPackInf(int packID, int64_t time);	//刷新表情包信息
	std::wstring GetEmotionTips(int id);
	int GetImageSetIndexByIDAndFrame(int id, int frame);
	int GetEmotionsCount();
	const std::vector<stEmotionInf>& GetAllEmotion();
	int GetFrameByElapseTime(int id, float fElapse);			//  pos>0.0f pos<1.0f  1.0表示播放最后一帧
	float GetEmotionDuration(int id);
	int GetEmotionTotalFrame(int id);
	void AddEmotion(int id, int totalFrame, float duration, const std::wstring& strTips, const std::wstring& strAccessKey);
	void LoadAllEmotionInf();
	void InitEmitionImagesetIdxInf();						//初始化表情对应的imageset信息

private:
	int m_maxEmotionID;
	std::map<int, int64_t> m_mapEmotionPackInf; //表情包信息
	std::vector<stImageSetInf> m_vecImageSetInf;
	std::vector<stEmotionInf> m_vecEmotionInfs;


};

inline EmotionManager* gGetEmotionManager()
{
	return EmotionManager::GetInstance();
}

