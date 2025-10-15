#ifndef __ARTTEXTMANAGER_H
#define __ARTTEXTMANAGER_H

#include "Singleton.hpp"


enum enumHPMPChangeType
{
	eHPAdd,
	eHPCounhtSub,
	eMPAdd,
	eMPSub,
	eHPAddBang,	//暴击加血
	eHPSubBang,	//暴击扣血
	eExpAdd,	//加经验
	eHPCount,	//扣血累计
	eFanZhen,	//反震
	eFangYu,	//防御特效
	eHPMPMAX
};

class SceneObject;
class Effect;
class CArtText;

class NumberEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
public:
	CArtText* m_pArtText;
};

struct EffectNode
{
	NumberEffectNotify* m_pNotify;
	Nuclear::IEffect* m_pEffect;
};

class CArtText
{
private:
	SceneObject* m_pOwner;
	int m_DelayTime;
	int m_PlayTime;

	struct NumberTextOne
	{
		std::wstring m_EffectFileName;
		int m_X;
		int m_Y;
	};

	struct NumberData
	{
		enumHPMPChangeType m_eType;
		int m_iNum;
		int m_iFlagType;
	};
	std::list<NumberData> m_listNumberData;
	std::list<EffectNode> m_listEffect;
public:
	CArtText();
	~CArtText();
	void SetOwner(SceneObject* pOwner);
	SceneObject* GetOwner();
	void SetDelayTime(int iDelay);
	int GetDelayTime();
	void AddHPMPNumber(enumHPMPChangeType eType, int iNum, int iFlagType, bool bClearAll = false);
	void MakeNewText(NumberData& ND);
	void Run(int iDelta);
	void AddEffect(Nuclear::IEffect* pEffect);
	void RemoveEffect(Nuclear::IEffect* pEffect);
	static void InitArtText();
};

#define MAX_NUM_HIT	7	//最多7位数

#endif
