#ifndef __Nuclear_REGIONMAP_PTRIGGERSOBJS_H__
#define __Nuclear_REGIONMAP_PTRIGGERSOBJS_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"
#include "numapobjs.h"

//һ��������ЧӦ�ð����������ԣ�(С���������Եĵ�λ�����ͣ���������������Ե���Ϊ)
//1   ��������(һ�����Σ���������4������)��{��������Ч�����������Χ�ڲ��ţ�����Ǿ��鴥������ô���������뱾����Żᴥ��}
//2.1 ��������(�Ƿ��鴥��) {���鴥���Ļ�������������Զ�������������Ӱ�죬������Ϊ�����λ�ã��Ǿ��鴥��������Ļ�ϵ�ʵ���������һ��Tile�������Ҳ���ǽ����ڰ���tile�ĸ�������������n��ÿ��d���봥��n�Σ�����n���������}
//2.2 ��������(�Ƿ���Ϸʱ�䴥��) {��Ϸʱ�䴥��Ϊ����Ϸ��ʱ�����趨�õ�ʱ��(��3��)�ڲŴ���}
//3   ����������ʼʱ��s�ʹ�����������ʱ��e(����) {��2.2Ϊ����Ϸʱ�䴥��ʱ��Ч��s���Դ���e����ζ���ǵ�������Ϻ͵ڶ�����糿}
//4   ��������f(������������Ϊ[0,1])
//5   ���Դ����ļ��d(����)��{�ﵽ������������֮��ÿd�������һ����[0,1)�������pf����pf<f��ʱ��������ɹ�}
//6   �������ʱ����MaxD(����)��{����ϴδ�������MaxD����֮��û�д�������ô�ͺ��Դ������ʽ��д�����MaxD==0����ر����ʱ��������}
//
//09.07.22���䣺Ӧ�û��д���֮����Ч�Ĳ�Σ������κͲ߻��ǱߵĲ����һ�µģ��ֱ���
//		a) �ر�֮�ϣ�����֮�¡��������֮������֮ǰ����
//		b) ������ڵ��㣬������ݵ����Զ��ж��ڵ����������в�������
//		c) ����֮�ϡ���������֮�������֮ǰ��
//		d) ���ϲ㡣���ڹ���֮��
//
//09.08.03������
//2.1���޸�Ϊ�������ݣ�
//	��������(�Ƿ��鴥��) 
//  {
//		�����������£�
//			���������������ӿڵĽ�������rect��rect�����Ϊs��ÿ��������Ч���������������и�ʱ��b��
//			�Ǿ��鴥��: ÿ��tile�����Ϊts��ÿ��update���Դ���[s*b/ts/d]=n����Ч(dΪ�������)��Ҳ���ǲ���n���������һ�������Ƿ񴥷���֮��bҪ���ϱ��θ��µ�deltaֵ����b-=n*d*ts/s������bӦ���ǹ�0�ģ�������Ϊ���û�д����Ļ���deltaֵҪ�ۻ����´�update��
//          ���鴥��: ���Ŀǰ��rect����ľ��������a��ÿ��update���Դ���[a*b/d]=n����Ч(dΪ�������)��Ҳ���ǲ���n���������һ�������Ƿ񴥷���֮��bҪ���ϱ��θ��µ�deltaֵ����b-=n*d/a������bӦ���ǹ�0�ģ�������Ϊ���û�д����Ļ���deltaֵҪ�ۻ����´�update��
//6  ���޸�Ϊ�������ݣ�
//		�������ʱ����MaxD(����)��{����ϴδ�������MaxD����֮��û�д�������ô�ͺ��Դ������ʴ���һ�Σ�һ��������Чֻ����һ�Σ�������Ļ��������sprite�ж��٣���MaxD==0����ر����ʱ��������}
//
//Ŀǰ���еĴ���������������ڵ�ͼ�ϣ�ֻ����1�顣
//
//��ӡ�����������������޷��ô����ķ�ʽ����
//Ŀǰ���ڽ�ӡ��˼·�ǰ�һ�����ӷ������ھ������ϣ�����ÿ��һ����ԭ�ط���һ�����ٶ��޼��ٶȵ����ӡ�����Ŀǰ����û�з���
//
//10.01.26���䣺
//����һ���µ���Ч���ͣ�TRIGGER_NONRANDOM_AND_SINGLE���˴�����Ч����Ϊ���£�
//ֻ�ڴ����������ĵ㴥����������֮��������Ч���������ڲ��ٴ�����������TRIGGER_SPRITEͬ�á�
//�Ժ���Կ��ǽ�NONRANDOM��SINGLE�ֿ�����Ҫ��NONRANDOM��TRIGGER_SPRITE�г�ͻ��

class ToolsPMap;

namespace Nuclear
{
	namespace SubMap
	{
		enum eTRIGGER_TYPE
		{
			TRIGGER_SPRITE = 1,
			TRIGGER_TIME = 1 << 1, //2,
			TRIGGER_NONRANDOM_AND_SINGLE = 1 << 2, //4,
			TRIGGER_DELETE = 1 << 15
		};

		typedef unsigned short TriggerInfoID;
		typedef unsigned short TriggerSmallID;

		typedef std::map<TriggerSmallID, NuclearRect> TriggerAreaMap;

		struct sTriggerInfo
		{
			ObjMapKey effectMapKey;				//������Ҫ���ŵ���Ч
			unsigned short triggerTypeFlag;		//eTRIGGER_TYPE
			ActionTypeFlag actionsFlag;			//�����Ķ�������
			DWORD beginTime;
			DWORD endTime;
			float frequency;
			DWORD triDelta;
			DWORD maxDelta;
			NuclearPoint excursion;					//����ƫ�ƣ�ֻ����Sprite��������
			Nuclear_EffectLayer layer;
		};

		typedef std::vector<sTriggerInfo> TriggerInfoVet;

		struct sTriggerObjInfo
		{
			std::wstring name;
			TriggerInfoVet triggerInfos;
			unsigned int nextSamllID;
			TriggerAreaMap areas;
			sTriggerObjInfo () : nextSamllID(0) {}
		};

		union sTriggerID
		{
			struct {
				TriggerInfoID infoID;
				TriggerSmallID areaID;
			};
			unsigned int id;
			sTriggerID(TriggerInfoID _info, TriggerSmallID _area):infoID(_info), areaID(_area) {}
			sTriggerID(unsigned int _id = 0):id(_id) {}
		};

		typedef std::map<TriggerInfoID, sTriggerObjInfo> TriggerInfoMap;

		class NuclearTriggerObjs
		{
			friend class ToolsPMap;
		public:
			NuclearTriggerObjs(void);
			~NuclearTriggerObjs(void);

			///////////////////////////////�������///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			TriggerInfoID AddAnTriggerInfoVet(const std::wstring &name, const TriggerInfoVet& obj);
			bool RemoveAnTriggerInfoVet(TriggerInfoID infoID);
			void ModifyAnTriggerInfoVet(TriggerInfoID infoID, const std::wstring &name, const TriggerInfoVet& obj);

			const TriggerInfoMap& GetTriggerInfos() const { return m_TriggerInfos; }
			void SetTriggerInfos(const TriggerInfoMap& pmap) { m_TriggerInfos = pmap; }

			sTriggerID AddAnTriggerArea(TriggerInfoID infoID, const NuclearRect& area);
			void RemoveAnTriggerArea(sTriggerID triggerID);
			void ModifyAnTriggerArea(sTriggerID triggerID, const NuclearRect& newArea);

		private:
			TriggerInfoMap m_TriggerInfos;
			TriggerInfoID m_nNextTriggerID;

		};
	}
}
#endif