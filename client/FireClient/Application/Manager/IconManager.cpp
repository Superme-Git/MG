#include "stdafx.h"
#include "IconManager.h"
#include "EmotionManager.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIPropertyHelper.h"
#include "GameTable/common/CCommon.h"

IconManager::IconManager()
{
	m_vecIconImageSet.resize(0);
}

IconManager::~IconManager()
{

}

void IconManager::InitImagesetSizeInfo(std::vector<int>& vec, CEGUI::String prefix, int fromNum, int maxNum)
{
	for (int i = fromNum; i <= maxNum; i++)
	{
		CEGUI::String strImageSetName = prefix + CEGUI::PropertyHelper::intToString(i);
		
		if (!CEGUI::ImagesetManager::getSingleton().isDefined(strImageSetName))
		{
			CreateImageSet(strImageSetName);
		}
		const CEGUI::Imageset& imageSet = CEGUI::ImagesetManager::getSingleton().get(strImageSetName);
		vec.push_back(imageSet.getImageCount());
	}
}

const CEGUI::Image* IconManager::GetImageByID(int id)  //根据图片的id获得图片
{
	CEGUI::String strImageSetName = FindImageSetNameByID(id);
	if (strImageSetName.empty())
	{
		return &getDefaultIcon();
	}
	if (std::find(m_vecIconImageSet.begin(), m_vecIconImageSet.end(), strImageSetName) != m_vecIconImageSet.end())
	{
		if (CEGUI::ImagesetManager::getSingleton().isDefined(strImageSetName))
		{
			const CEGUI::Imageset& imageSet = CEGUI::ImagesetManager::getSingleton().get(strImageSetName);
			if (imageSet.isImageDefined(CEGUI::PropertyHelper::intToString(id)))
			{
				return &imageSet.getImage(CEGUI::PropertyHelper::intToString(id));
			}
		}
	}
	else
	{
		if (CEGUI::ImagesetManager::getSingleton().isDefined(strImageSetName))
		{
			m_vecIconImageSet.push_back(strImageSetName);
		}
		else
		{
			try {
				CreateImageSet(strImageSetName);
			}
			catch (...) {
				return &getDefaultIcon();
			}
		}

		return GetImageByID(id);

	}

	return &getDefaultIcon();
}

const CEGUI::String IconManager::GetImagePathByID(int id)
{
	const CEGUI::Image* pImage = GetImageByID(id);
	if (pImage != NULL)
	{
		return CEGUI::PropertyHelper::imageToString(pImage);
	}
	else
	{
		return "";
	}
}

const CEGUI::Image* IconManager::GetBuffIconByID(int id)	//根据buff的id获得它的图标
{
	return GetImageByID(id);
}

const CEGUI::Image* IconManager::GetItemIconByID(int id)  //根据道具的id获得它的图标
{
	return GetImageByID(id);

}

const CEGUI::Image* IconManager::GetSkillIconByID(int id)  //根据技能的id获得它的图标
{
	return GetImageByID(id);
}

const CEGUI::String IconManager::GetItemIconPathByID(int id)    // 根据道具的id获得它的图标的标识文字 (imgeset+name)
{
	CEGUI::String path = GetImagePathByID(id);
	if (path == "")
		return GetImagePathByID(1433);
	return path;
}

const CEGUI::String IconManager::GetSkillIconPathByID(int id)   // 根据技能的id获得它的图标的标识文字 (imgeset+name)
{
	return GetImagePathByID(id);
}

void IconManager::CreateImageSet(const CEGUI::String& name)  //创建一个imageset
{
	if (std::find(m_vecIconImageSet.begin(), m_vecIconImageSet.end(), name) == m_vecIconImageSet.end())
	{
		CEGUI::ImagesetManager::getSingleton().create(name + ".imageset");
		m_vecIconImageSet.push_back(name);
	}

}

void IconManager::DestroyImageSet(const CEGUI::String& name) //销毁一个imageset
{
	CEGUI::ImagesetManager::getSingleton().destroy(name);
	IconImageSetListIt it = std::find(m_vecIconImageSet.begin(), m_vecIconImageSet.end(), name);
	if (it != m_vecIconImageSet.end())
	{
		m_vecIconImageSet.erase(it);
	}

}

CEGUI::String IconManager::FindImageSetNameByID(int id)  //通过ID寻找所在的imageset的名字,如果找不到名字为空
{
	CEGUI::String ImageSetName("");

	int index = -1;
	if (id > 0 && id < 900)
	{
		id--;
		index = id / ((m_nImageSetSize / m_nSkillIconSize)*(m_nImageSetSize / m_nSkillIconSize));

		return "skillicon" + CEGUI::PropertyHelper::intToString(index);
	}

	if (id >= 900 && id < 1000)
	{
		index = (id - 900) / ((m_nImageSetSize / m_nBufferSize)*(m_nImageSetSize / m_nBufferSize));
		return "buffer" + CEGUI::PropertyHelper::intToString(index);
	}

	if (id >= 1000 && id < 9000)
	{
		index = (id - 1000) / ((m_nImageSetSize / m_nItemIconSize)*(m_nImageSetSize / m_nItemIconSize));
		return "itemicon" + CEGUI::PropertyHelper::intToString(index);
	}

	if (id >= 9000 && id < 10000)
	{
		index = id - 9000;
		return "roleandmonster" + CEGUI::PropertyHelper::intToString(index);
	}


	if (id >= 30000 && id < 30100)
	{
		index = (id - 30010) / ((m_nImageSetSize / m_nRightTopRoleSize)*(m_nImageSetSize / m_nRightTopRoleSize));
		return "righttoprole" + CEGUI::PropertyHelper::intToString(index);
	}

	if (id >= 30100 && id < 30200)
	{
		index = (id - 30100) / ((m_nImageSetSize / m_nServentSize)*(m_nImageSetSize / m_nServentSize));
		return "servent" + CEGUI::PropertyHelper::intToString(index);
	}
	//30000+ is nomal icon 40000+ is grey icon 
	if ((id >= 30200 && id < 31000))
	{
		//the image count in each imageset is not same any more. change by liugeng
		if (m_vecPetImagesetSize.empty())
		{
			int maxImagesetCount = CEGUI::PropertyHelper::stringToInt(GameTable::common::GetCCommonTableInstance().getRecorder(177).value);
			InitImagesetSizeInfo(m_vecPetImagesetSize, "righttoppet", 0, maxImagesetCount);
		}
		int n = 0;
		for (size_t i = 0; i < m_vecPetImagesetSize.size(); i++)
		{
			if (id - 30200 + 1 <= n + m_vecPetImagesetSize[i])
			{
				index = i;
				break;
			}
			n += m_vecPetImagesetSize[i];
		}
		return "righttoppet" + CEGUI::PropertyHelper::intToString(index);
	}
	// 40000+ is grey icon 
	if ((id >= 40200 && id < 41000))
	{
		//the image count in each imageset is not same any more. change by liugeng
		if (m_vecPetImagesetSize.empty())
		{
			int maxImagesetCount = CEGUI::PropertyHelper::stringToInt(GameTable::common::GetCCommonTableInstance().getRecorder(177).value);
			InitImagesetSizeInfo(m_vecPetImagesetSize, "righttoppet", 0, maxImagesetCount);
		}
		int n = 0;
		for (size_t i = 0; i < m_vecPetImagesetSize.size(); i++)
		{
			if (id - 40200 + 1 <= n + m_vecPetImagesetSize[i])
			{
				index = i;
				break;
			}
			n += m_vecPetImagesetSize[i];
		}
		return "righttoppet" + CEGUI::PropertyHelper::intToString(index + CEGUI::PropertyHelper::stringToInt(GameTable::common::GetCCommonTableInstance().getRecorder(242).value));
	}
	if (id >= 31000 && id < 31010)
	{
		index = 0;
		return "roledlghead" + CEGUI::PropertyHelper::intToString(index);
	}

	if (id >= 32000 && id < 33000)
	{
		index = (id - 32000) / ((m_nImageSetSize / m_nSellPetIconSize)*(m_nImageSetSize / m_nSellPetIconSize));
		return "sellpeticon" + CEGUI::PropertyHelper::intToString(index);
	}
	return "";
}

const CEGUI::String IconManager::GetEmotionIconPathByFrame(int aniID, int frame)
{
	const CEGUI::Image* pImage = GetEmotionImageByFrame(aniID, frame);
	if (pImage != NULL)
	{
		return CEGUI::PropertyHelper::imageToString(pImage);
	}
	else
	{
		return "";
	}
}

const CEGUI::Image* IconManager::GetEmotionImageByFrame(int aniID, int frame)
{
	int imagesetIdx = gGetEmotionManager()->GetImageSetIndexByIDAndFrame(aniID, frame);

	CEGUI::String strEmotionImageSetName("emotionicon");

	strEmotionImageSetName += CEGUI::PropertyHelper::intToString(imagesetIdx);

	if (std::find(m_vecIconImageSet.begin(), m_vecIconImageSet.end(), strEmotionImageSetName) != m_vecIconImageSet.end())
	{
		if (CEGUI::ImagesetManager::getSingleton().isDefined(strEmotionImageSetName))
		{
			const CEGUI::Imageset& imageSet = CEGUI::ImagesetManager::getSingleton().get(strEmotionImageSetName);
			CEGUI::String strImageName("");
			int imageID = 90000 + aniID * 100 + frame;
			strImageName += CEGUI::PropertyHelper::intToString(imageID);

			if (imageSet.isImageDefined(strImageName))
			{
				return &imageSet.getImage(strImageName);
			}
		}
	}
	else
	{
		CreateImageSet(strEmotionImageSetName);

		return GetEmotionImageByFrame(aniID, frame);

	}
	return NULL;
}

void IconManager::AddTitleImageSet()
{
	CreateImageSet("title");
}

void IconManager::AddCardImageSet()
{
	CreateImageSet("Card");
}

const CEGUI::Image& IconManager::GetTraceIcon()
{
	return CEGUI::ImagesetManager::getSingleton().get("common").getImage("common_biaoshi_cc");
}

const CEGUI::Image& IconManager::getDefaultIcon()
{
	return CEGUI::ImagesetManager::getSingleton().get("common").getImage("common_biaoshi_cc");
}
