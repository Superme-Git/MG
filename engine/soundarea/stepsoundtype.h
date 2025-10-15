#ifndef __Nuclear_STEPSOUNDTYPE_H
#define __Nuclear_STEPSOUNDTYPE_H

#if _MSC_VER > 1000
#pragma once
#endif
#include <map>
#include "nuxptypes.h"

namespace Nuclear
{
	class INuclearFileIO;

	typedef std::map<int, std::wstring> XPSSGTypeMap;//Nuclear Step Sound Ground Type，key是地表类型，value是说明
	typedef std::map<int, NuclearColor> XPSSGColorMap;//Ground的颜色，只在编辑器用
	typedef std::map<std::wstring, std::wstring> XPSSATypeMap;//Nuclear Step Sound Actor Type，key是名字，value是说明
	typedef std::map<int, int> XPSSCountMap;	//key是地表类型，value是这个地表类型声音的数量
	typedef std::map<std::wstring, XPSSCountMap> XPSSAllCountMap;//key是Actor的名字，value是一个地表类型和数量的map
	typedef std::map<std::wstring, std::wstring> XPRideType;//key是骑宠的名字，value是骑宠的类型
	struct StepNotifieV
	{
		std::wstring actname;
		int frame;
		bool operator== (const StepNotifieV& o) { return frame == o.frame && actname == o.actname; }
	};
	typedef std::vector<StepNotifieV> XPStepNotifies;
	struct SSModelType	
	{
		std::wstring defaultType;
		XPRideType	ridetype;
		XPStepNotifies notifies;
	};
	typedef std::map<std::wstring, SSModelType> XPSSModelMap;//key是模型名

	//脚步声音的类型
	//不同角色类型（例如人、马、狼等）在不同的区域（地表类型）会有不同的脚步声；
	//同样的角色类型在同样的区域可能会有多种脚步声，这个时候需要随机播放。
	//如果某个角色在某种区域里面脚步声的数量为0，那么就播放默认声音（区域id为0），如果这个角色没有默认声音就不播放
	class StepSoundType
	{
	private:
		XPSSGColorMap m_groundColor;//Ground的颜色，只在编辑器用
		XPSSGTypeMap m_groundMap;	//记录到底有什么地表类型
		XPSSATypeMap m_actorMap;	//记录到底有哪几种精灵
		XPSSAllCountMap m_allSounds;
		XPSSModelMap m_modelMap;	//记录哪个模型属于哪种精灵

	public:
		StepSoundType(void);
		~StepSoundType(void);

		bool Init(INuclearFileIO *pFIO);
		bool Save(const std::wstring &soundDir);//sounDir是/sound的Mount根，包括sound，最后要有“\”
		bool LoadSoundType(INuclearFileIO *pFIO);
		bool LoadModelMap(INuclearFileIO *pFIO);
		bool SaveSoundType(const std::wstring &soundDir);
		bool SaveModelMap(const std::wstring &soundDir);

		//获得当角色类型是actname，地表类型是groundType的声音的数量，如果返回值是false，代表是用默认声音
		bool GetSoundCount(const std::wstring &actname, int groundType, int &count) const;
		const XPSSModelMap &GetModelMap() const { return m_modelMap; }


		//编辑器专用
		void SetSoundCount(const std::wstring &actname, int groundType, int count) { m_allSounds[actname][groundType] = count; }
		void ClearSoundCount(const std::wstring &actname) { m_allSounds[actname].clear(); }

		void SetGroundType(int id, const std::wstring &des) { m_groundMap[id] = des; }//添加修改都用它
		void RemoveGroundType(int id) { m_groundMap.erase(id); }//不用先确定id是否存在了

		void SetActorType(const std::wstring &name, const std::wstring &des) { m_actorMap[name] = des; }
		void RemoveActorType(const std::wstring &name) { m_actorMap.erase(name); }

		bool SetModelDefaultType(const std::wstring &modelname, const std::wstring &actorname);
		bool SetModelRideType(const std::wstring &modelname, const std::wstring &ridename, const std::wstring &actorname);

		bool InsertModelStepNotify(const std::wstring &modelname, const StepNotifieV &notify);
		bool RemoveModelStepNotify(const std::wstring &modelname, size_t id);
		bool ModifyModelStepNotify(const std::wstring &modelname, size_t id, const StepNotifieV &notify);

		const XPSSGTypeMap &GetGroundMap() const { return m_groundMap; }
		const XPSSATypeMap &GetActorMap() const { return m_actorMap; }

		XPSSGColorMap &GetGroundColor() { return m_groundColor; }
		
	};

}

#endif	//#ifndef __Nuclear_STEPSOUNDTYPE_H
