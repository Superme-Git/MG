#pragma once
#include "..\engine\world\distortbase.h"
#include "..\engine\world\picresrc.h"

class CToolsDistort :
	public Nuclear::DistortBase
{
public:

	CToolsDistort(Nuclear::EngineBase* pEB, const Nuclear::DistortionEffectParam& effectParam) : Nuclear::DistortBase(pEB, effectParam)
	{
	}

	virtual ~CToolsDistort(void)
	{
		ResetMainImgInfo();
		ResetLightImgInfo();
	}

public:
	//±à¼­Æ÷×¨ÓÃ
	void ResetMainImgInfo()
	{
		Nuclear::DistortImgInfoVetor::iterator iter = m_vectorImgInfo.begin();
		for(; iter!=m_vectorImgInfo.end(); ++iter)
		{
			if(iter->pPicResrc != NULL)
			{
				delete(iter->pPicResrc);
				iter->pPicResrc = NULL;
			}
		}
		m_vectorImgInfo.clear();
	}

	void ResetLightImgInfo()
	{
		Nuclear::DistortImgInfoVetor::iterator iter = m_vectorLightImgInfo.begin();
		for(; iter!=m_vectorLightImgInfo.end(); ++iter)
		{
			if(iter->pPicResrc != NULL)
			{
				delete(iter->pPicResrc);
				iter->pPicResrc = NULL;
			}
		}
		m_vectorLightImgInfo.clear();
	}

	const Nuclear::DistortionEffectParam& GetParam() const { return m_effectParam; }
	Nuclear::DistortionEffectParam& GetParam() { return m_effectParam; }
	void SetParam(const Nuclear::DistortionEffectParam& param) { m_effectParam = param; }
};
