#ifndef __Nuclear_PLINKEDOBJECT_H__
#define __Nuclear_PLINKEDOBJECT_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nupobject.h"
#include "../common/nuxptypes.h"

namespace Nuclear
{
	enum XPPLANTDISTORTIONTYPE
	{
		XPPLANTROTATE = 0,
		XPPLANTSKEW = 1,
		XPPLANTTRANSLATION = 2,
	};

	enum XPPLANTSKEWTYPE
	{
		XPSKEWHORIZONTAL = 0, //ˮƽ��
		XPSKEWVERTICAL = 1, //��ֱ��
	};

	enum XPMOVEFOLLOWTYPE
	{
		XPMOVEFOLLOWNO = 0, //������
		XPMOVEFOLLOWFLEXIBLE = 1, //��������
		XPMOVEFOLLOWRIGID = 2, //��������
	};

	enum XP_LINKEDOBJ_FLAG
	{
		XP_LINKEDOBJ_NULL = 0x00000000,
		FIRE_LINKEDOBJ_LIGHT = 0x00000001,
	};

	struct sPLANTMOVEPARAM 
	{
		float m_fAngle1;
		float m_fAngle2;
		float m_fSpeed;
		XPPLANTDISTORTIONTYPE m_nDistortionType;
		XPPLANTSKEWTYPE m_nSkewType;	
		XPMOVEFOLLOWTYPE m_nMoveFollowType; //�汾5��ʼ��..�����ӡ������ӡ�Ӳ����
		DWORD m_dwFlag; //Ŀǰ���������λ�Ƿ�Ϊ1���жϸ�����Ƿ��ǵƹ�... ---�汾6���ӵ�
		int m_nSkewPixelX;
		int m_nSkewSpeedX;

		float m_fSizeLeft; //�汾8���ӵ� ---ƽ�Ʋ���
		float m_fSizeRight;
		float m_fSizeTop; 
		float m_fSizeDown;
		float m_fTranslationSpeedx;
		float m_fTranslationSpeedy;

		sPLANTMOVEPARAM()
		{
			m_fAngle1 = -1.0f;
			m_fAngle2 = 1.0f;
			m_fSpeed = 1.0f;
			m_nDistortionType = XPPLANTROTATE;
			m_nSkewType = XPSKEWHORIZONTAL;
			m_nMoveFollowType = XPMOVEFOLLOWNO;
			m_nSkewPixelX = 5;
			m_nSkewSpeedX = 5;
			m_dwFlag = XP_LINKEDOBJ_NULL;

			m_fSizeLeft = -2.0f;
			m_fSizeRight = 2.0f;
			m_fSizeTop = 0.0f;
			m_fSizeDown = 0.0f;
			m_fTranslationSpeedx = 2.0f;
			m_fTranslationSpeedy = 2.0f;
		}
	};

	struct sPLANTPICINFO 
	{
		std::wstring m_strPicName; //IMG�ļ���...   "/map/elements/ҩ����/ҩ���� �ڵ�3.set"
		bool m_bMainNode; //һ�����Ӷ���������ֻ��һ�����ڵ�...
		int m_nWidth; //������Ŀ��
		int m_nHeight;

		NuclearPoint m_ptOffset; //���Ͻ�������ڵ�����Ͻǵ�����..�����ڵ����Ͻ�Ϊ0,0��--ˮƽ��x+ ��ֱ��y+
		NuclearPoint m_rotateCent; //����Լ�ͼ�����ģ�0��0�����������---ˮƽ��x+ ��ֱ��y+
		NuclearPoint m_rotateCent_2; //������ڵ�ͼ�����ģ�0��0�������λ��..ˮƽ��x+ ��ֱ��y+..		
		sPLANTMOVEPARAM m_LinkedObjectMoveParam; //�����趨ֵ...

		sPLANTPICINFO()
		{
			m_strPicName = L"";
			m_bMainNode = false;
			m_nWidth = 0;
			m_nHeight = 0;
			m_ptOffset = NuclearPoint();
			m_rotateCent = NuclearPoint();
			m_rotateCent_2 = NuclearPoint();
		}
	};

	class NuclearLinkedObject :	public NuclearObject
	{
	public:
		NuclearLinkedObject(void);
		~NuclearLinkedObject(void);

	public:
		enum PLANTVER
		{
			VERSION = 1,
			Version_MoveFollow = 2, //�νڵ��Ƿ�������ڵ��˶���ѡ.. �汾1����һ�������
			Version_MaxBox = 3, //�����˰�Χ�� m_rctMaxBox
			Version_SkewType = 4, //���и�Ϊˮƽ�кʹ�ֱ������..��ǰ�汾�����ж�Ӧ��ת��m_rotateCentΪͼ���±�Ե�ϵĵ㣬���ж�Ӧ..
			Version_MoveFollowType = 5, //m_nMoveFollowType
			Version_dwFlag = 6, //���ƹ������
			Version_ZOOMPARAM = 7, //��̬������ܱ��Ŷ����¼����˶���..
			Version_TransformType = 8, //����ת����֮�⣬����ƽ�Ʋ���..
			Version_NotMountElement = 9,	//��mount elements��
			CURVER = 9,
		};	

	public:
		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);
		DWORD GetMainNodeID() const { return m_MainNodeID; }

	public:
		NuclearPoint m_ptCentPos; //plant������,(��������ڵ�����Ͻ�..)
		NuclearPoint m_ptBasePos[4];//plant�ĵ���,----λ�ù�ϵͬ��
		NuclearRect m_rctMaxBox; //������Ϊ����ԭ�㣬ˮƽ��x+����ֱ��y+

		//�汾7����
		bool m_bLKOEnableZoom; //�Ƿ���������
		float m_fLKOzoom;//������ҳ̶�
		float m_fLKOTime;//�ָ�����ʱ��
		bool  m_bStatic;//�Ƿ�Ϊ��ֹ�������
		//����..�汾7
	
		typedef std::vector<sPLANTPICINFO> VectorLinkedObjectInfo;
		VectorLinkedObjectInfo m_vectorLinkedObjectInfo; //ID--���ļ�ʱ����...��д����..

	private:
		DWORD m_MainNodeID; //���ڵ�ID

	};

}

#endif