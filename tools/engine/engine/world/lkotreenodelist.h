#ifndef __Nuclear_LKOTREENODELIST_H
#define __Nuclear_LKOTREENODELIST_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <list>
#include "..\map\plinkedobject.h"

namespace Nuclear
{

	//���桢��ͼ�༭������ʱ�ṹ�壬���漰��ͼƬ��Դ
	struct LkoTreeNode
	{
		LkoTreeNode *pParent;			//���ڵ�ָ��
		LkoTreeNode *pRoot;				//���ڵ�ָ��
		XPVECTOR2 fromMainRotateCent;		//�Ӹ��ڵ���ת���ĵ��Լ�����ת���ĵ�����������Լ��Ǹ��ڵ㣬��ô��������ڵ�ͼ�ϵ����꣨�������꣩��
	//	XPMOVEFOLLOWTYPE moveFollowType;	//�Ƿ����������ڵ��ϣ�Ŀǰ���е����Ӷ��������ӣ�
		int height;							//�ڵ�ĸ߶ȣ�ֻ�д��е�ʱ����
		int width;							//�ڵ�Ŀ�ȣ�ֻ�д��е�ʱ����
		PLANTMOVEPARAM LinkedObjectMoveParam;	//�����趨ֵ...
		CPOINT rotateCent;					//��ת���ģ��Ե�ǰ�ڵ����Ͻ�Ϊԭ��

		///////////���²�����Ҫÿ֡Update
		float fTranslationx; //x����ƽ��ֵ
		float fTranslationy; //y����ƽ��ֵ

		int nBeginBrightTime;				//����ʱ��
		int nEndBrightTime;					//���ʱ�䣨��LinkedObjectMoveParam.m_dwFlagû��XP_LINKEDOBJ_LIGHT��־λ��ʱ��������������Ч��
		float fAngle;						//��ǰ��ת�Ƕ�
		float fSkewx;						//��ǰб�в���
		XPMATRIX3 mTransfer;				//ƽ�ƾ��������Ӳ�����ڸ��ڵ��ϣ���������ڵ�Ĵ��к���ת���Σ�
											//������������ڸ��ڵ��ϣ���������ڵ�����к���ת������ɵ�ƽ�ƣ�
		XPMATRIX3 mTransAll;				//�任���� >> Update������ ������ξ��󣨰������С���ת��* mTransfer
		//����������ڸ��ڵ��ϣ�����ƽ�ƾ���Ӧ���� fromMainRotateCent * pParent->mTransfer �����������ƽ�ƾ���
		//�����Ӳ�����ڸ��ڵ��ϣ�����ƽ�ƾ����� fromMainRotateCent ת��Ϊƽ�ƾ���� ���� pParent->mTransAll
		//������������ڸ��ڵ��ϣ�����ƽ�ƾ���Ӧ���� fromMainRotateCent * pParent->mTransAll �����������ƽ�ƾ���
		//Ӳ���Ӻ������ӵ������������ҵ�ƽ�ƾ��󲻰���ƽ������ı任����
		XPMATRIX3 mTransResult;				//�任���󣨻�ͼ��ʱ������վ��󣬰���ԭ�����ת���ĵ�ͼƬ���Ͻǵı任��
		bool CheckLightTime(int time) const
		{
			if (nBeginBrightTime > nEndBrightTime)
			{
				if (time > nEndBrightTime && time < nBeginBrightTime)
					return false;
			} else {
				if (time > nEndBrightTime || time < nBeginBrightTime)
					return false;
			}
			return true;
		}
	};

	typedef std::vector<LkoTreeNode*> DTList;

	class LkoTreeNodeList
	{
	protected:
		DTList m_Lists;

	private:
		bool m_bTouchFlag;
		float m_fLkoLife; //���Ŷ�����֮��ĵ���ʱ..

	public:
		void SetZoomParamFlag(bool bflag) //������Ч..״̬�ı�ʱ��������
		{
			m_bTouchFlag = bflag;
			if(bflag)
				m_fLkoLife = m_fLKOTime;
		}

	protected: //��������Ϊ��������---
		bool m_bLKOEnableZoom; //�Ƿ���������
		float m_fLKOzoom;//������ҳ̶�
		float m_fLKOTime;//�ָ�����ʱ��
		bool  m_Static;//�Ƿ�ֹ

	public:
		LkoTreeNodeList(void);
		virtual ~LkoTreeNodeList(void);

		const DTList &GetList() const { return m_Lists; }
		void Update(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio);
		void SimpleUpdate(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio);
		void AddANode(LkoTreeNode *node);
		void RandomBrightTime();
		void SetStaticFlag(bool bStatic) { m_Static = bStatic; }
		//void RemoveATree(LkoTreeNode *root);
	};
}


#endif