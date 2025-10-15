#ifndef __Nuclear_REGIONMAP_PTRANSOBJS_H
#define __Nuclear_REGIONMAP_PTRANSOBJS_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"

namespace Nuclear
{
	namespace SubMap
	{
		struct TransObjInfo
		{
			//std::wstring picName;//��/element��ʼ��������
			CRECT picRect; //ͼƬ��ʼλ�ã���ͼ����
			CPOINT scalPoint; //�Ŵ����ĵ㣬��ͼ����
			unsigned int time;	//��λ������
			int width;
			int height;
		};

		typedef std::map<unsigned int, TransObjInfo> TransObjInfoMap;

		class PTransObjs
		{
		public:
			//��Ա������
		private:
			TransObjInfoMap m_mapTrans;
			unsigned int m_nMaxId;

			//��Ա������
		private:

		public:
			///////////////////////////////��ʼ��������������
			PTransObjs(void);
			~PTransObjs(void);

			///////////////////////////////�������
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////����������
			unsigned int AddAnTransObj(const TransObjInfo& obj);
			bool RemoveAnTransObj(const unsigned int TransId);
			void ModifyAnTransObj(const unsigned int TransId, const TransObjInfo& obj);
			const TransObjInfoMap& GetTransInfos()
			{
				return m_mapTrans;
			}
			void SetTransObjInfoMap(const TransObjInfoMap& map)
			{
				m_mapTrans = map;
			}

		};
	}
}
#endif