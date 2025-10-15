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

	typedef std::map<int, std::wstring> XPSSGTypeMap;//Nuclear Step Sound Ground Type��key�ǵر����ͣ�value��˵��
	typedef std::map<int, NuclearColor> XPSSGColorMap;//Ground����ɫ��ֻ�ڱ༭����
	typedef std::map<std::wstring, std::wstring> XPSSATypeMap;//Nuclear Step Sound Actor Type��key�����֣�value��˵��
	typedef std::map<int, int> XPSSCountMap;	//key�ǵر����ͣ�value������ر���������������
	typedef std::map<std::wstring, XPSSCountMap> XPSSAllCountMap;//key��Actor�����֣�value��һ���ر����ͺ�������map
	typedef std::map<std::wstring, std::wstring> XPRideType;//key���������֣�value����������
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
	typedef std::map<std::wstring, SSModelType> XPSSModelMap;//key��ģ����

	//�Ų�����������
	//��ͬ��ɫ���ͣ������ˡ����ǵȣ��ڲ�ͬ�����򣨵ر����ͣ����в�ͬ�ĽŲ�����
	//ͬ���Ľ�ɫ������ͬ����������ܻ��ж��ֽŲ��������ʱ����Ҫ������š�
	//���ĳ����ɫ��ĳ����������Ų���������Ϊ0����ô�Ͳ���Ĭ������������idΪ0������������ɫû��Ĭ�������Ͳ�����
	class StepSoundType
	{
	private:
		XPSSGColorMap m_groundColor;//Ground����ɫ��ֻ�ڱ༭����
		XPSSGTypeMap m_groundMap;	//��¼������ʲô�ر�����
		XPSSATypeMap m_actorMap;	//��¼�������ļ��־���
		XPSSAllCountMap m_allSounds;
		XPSSModelMap m_modelMap;	//��¼�ĸ�ģ���������־���

	public:
		StepSoundType(void);
		~StepSoundType(void);

		bool Init(INuclearFileIO *pFIO);
		bool Save(const std::wstring &soundDir);//sounDir��/sound��Mount��������sound�����Ҫ�С�\��
		bool LoadSoundType(INuclearFileIO *pFIO);
		bool LoadModelMap(INuclearFileIO *pFIO);
		bool SaveSoundType(const std::wstring &soundDir);
		bool SaveModelMap(const std::wstring &soundDir);

		//��õ���ɫ������actname���ر�������groundType���������������������ֵ��false����������Ĭ������
		bool GetSoundCount(const std::wstring &actname, int groundType, int &count) const;
		const XPSSModelMap &GetModelMap() const { return m_modelMap; }


		//�༭��ר��
		void SetSoundCount(const std::wstring &actname, int groundType, int count) { m_allSounds[actname][groundType] = count; }
		void ClearSoundCount(const std::wstring &actname) { m_allSounds[actname].clear(); }

		void SetGroundType(int id, const std::wstring &des) { m_groundMap[id] = des; }//����޸Ķ�����
		void RemoveGroundType(int id) { m_groundMap.erase(id); }//������ȷ��id�Ƿ������

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
