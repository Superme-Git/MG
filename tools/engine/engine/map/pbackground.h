#ifndef __Nuclear_PBACKGROUND_H
#define __Nuclear_PBACKGROUND_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "mapobjs.h"

namespace Nuclear
{
	namespace SubMap
	{
		typedef struct _PBackGoundInfo 
		{
			std::wstring name;	//����
			CPOINT pos;			//ͼƬ���ĵ�λ�ã���ͼ����
			FPOINT relSpeed;	//�ٶȱȣ�[0~1]�����ٶȱ�->(1, 1)ʱ����ζ�ż���ȫ��ͼ�ɼ���������Զ����viewport�ܣ����ٶȱ�->(0, 0)ʱ�����˻�Ϊ��ͨ������
			CRECT activeRect;	//����������ͼ����
			int girdWidth;		//���ӵĿ��
			int girdHeight;		//���ӵĸ߶�
			int width;			//ͼƬ�������ӿ�
			int height;			//ͼƬ�������Ӹ�
			ObjMapKey* pPics;	//ͼƬ����������
			bool wrapHorz;		//�Ƿ�ˮƽ�������	//����Ժ�Ҫ��ʲô����֮��ľ��ñ�־λ������
			bool wrapVert;		//�Ƿ���ֱ�������
			bool m_bDeleted;		//�Ƿ�ɾ����
			_PBackGoundInfo() : name(L""), pos(0, 0), relSpeed(0.0f, 0.0f), activeRect(0, 0, 0, 0), girdWidth(0), 
				girdHeight(0), width(0), height(0), pPics(NULL), wrapHorz(false), wrapVert(false), m_bDeleted(false) {}
		} PBackGoundInfo;

		typedef std::vector<PBackGoundInfo> PBackGoundInfoVector;

		class PBackGround
		{
		public:
			PBackGoundInfoVector m_backGroundInfos;
			int					 m_backGroundInfoCount;

		private:
			void ClearAll();

		public:
			PBackGround(void);
			~PBackGround(void);

			PBackGround& operator= (const PBackGround &x);
			///////////////////////////////�������
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);
		};
	}
}

#endif