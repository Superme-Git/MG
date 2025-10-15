#ifndef __Nuclear_PBACKGROUND_H__
#define __Nuclear_PBACKGROUND_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "numapobjs.h"

namespace Nuclear
{
	namespace SubMap
	{
		typedef struct _PBackGoundInfo 
		{
			std::wstring name;	//����
			NuclearPoint pos;			//ͼƬ���ĵ�λ�ã���ͼ����
			NuclearFPoint relSpeed;	//�ٶȱȣ�[0~1]�����ٶȱ�->(1, 1)ʱ����ζ�ż���ȫ��ͼ�ɼ���������Զ����viewport�ܣ����ٶȱ�->(0, 0)ʱ�����˻�Ϊ��ͨ������
			NuclearRect activeRect;	//����������ͼ����
			int girdWidth;		//���ӵĿ��
			int girdHeight;		//���ӵĸ߶�
			int width;			//ͼƬ�������ӿ�
			int height;			//ͼƬ�������Ӹ�
			ObjMapKey* pPics;	//ͼƬ����������
			bool wrapHorz;		//�Ƿ�ˮƽ�������	//����Ժ�Ҫ��ʲô����֮��ľ��ñ�־λ������
			bool wrapVert;		//�Ƿ���ֱ�������
			bool deleted;		//�Ƿ�ɾ����
			_PBackGoundInfo() : name(L""), pos(0, 0), relSpeed(0.0f, 0.0f), activeRect(0, 0, 0, 0), girdWidth(0), girdHeight(0), width(0), height(0), pPics(NULL), wrapHorz(false), wrapVert(false), deleted(false) {}
		} NuclearBackGoundInfo;

		typedef std::vector<NuclearBackGoundInfo> NuclearBackGoundInfoVector;

		class NuclearBackGround
		{
		public:
			NuclearBackGoundInfoVector m_BackGroundInfos;
			int						m_BackGroundInfoCount;

		public:
			NuclearBackGround(void);
			~NuclearBackGround(void);

			NuclearBackGround& operator= (const NuclearBackGround &x);
			///////////////////////////////�������///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

		private:
			void clearAll();
		};
	}
}

#endif