#ifndef __Nuclear_REGIONMAP_PTRANSOBJS_H__
#define __Nuclear_REGIONMAP_PTRANSOBJS_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"

namespace Nuclear
{
	namespace SubMap
	{
		struct sTransObjInfo
		{
			//std::wstring picName;//��/element��ʼ��������
			NuclearRect picRect; //ͼƬ��ʼλ�ã���ͼ����
			NuclearPoint scalPoint; //�Ŵ����ĵ㣬��ͼ����
			unsigned int time;	//��λ������
			int width;
			int height;
		};

		typedef std::map<unsigned int, sTransObjInfo> TransObjInfoMap;

		class NuclearTransObjs
		{
		public:
			NuclearTransObjs(void);
			~NuclearTransObjs(void);

			///////////////////////////////�������///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			unsigned int AddAnTransObj(const sTransObjInfo& obj);
			bool RemoveAnTransObj(const unsigned int TransId);
			void ModifyAnTransObj(const unsigned int TransId, const sTransObjInfo& obj);
			const TransObjInfoMap& GetTransInfos() { return m_mapTrans; }
			void SetTransObjInfoMap(const TransObjInfoMap& map) { m_mapTrans = map; }

		private:
			TransObjInfoMap m_mapTrans;
			unsigned int m_nMaxId;

		};
	}
}
#endif