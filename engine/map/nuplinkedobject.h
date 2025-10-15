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
		XPSKEWHORIZONTAL = 0, //水平切
		XPSKEWVERTICAL = 1, //垂直切
	};

	enum XPMOVEFOLLOWTYPE
	{
		XPMOVEFOLLOWNO = 0, //不连接
		XPMOVEFOLLOWFLEXIBLE = 1, //柔性连接
		XPMOVEFOLLOWRIGID = 2, //刚性连接
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
		XPMOVEFOLLOWTYPE m_nMoveFollowType; //版本5开始用..不连接、软连接、硬连接
		DWORD m_dwFlag; //目前仅仅用最低位是否为1来判断该组件是否是灯光... ---版本6增加的
		int m_nSkewPixelX;
		int m_nSkewSpeedX;

		float m_fSizeLeft; //版本8增加的 ---平移参数
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
		std::wstring m_strPicName; //IMG文件名...   "/map/elements/药王居/药王居 遮挡3.set"
		bool m_bMainNode; //一个连接对象中有且只有一个主节点...
		int m_nWidth; //该组件的宽高
		int m_nHeight;

		NuclearPoint m_ptOffset; //左上角相对主节点的左上角的坐标..（主节点左上角为0,0）--水平右x+ 竖直下y+
		NuclearPoint m_rotateCent; //相对自己图像中心（0，0）的相对坐标---水平右x+ 竖直下y+
		NuclearPoint m_rotateCent_2; //相对主节点图像中心（0，0）的相对位置..水平右x+ 竖直下y+..		
		sPLANTMOVEPARAM m_LinkedObjectMoveParam; //美术设定值...

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
			Version_MoveFollow = 2, //次节点是否跟随主节点运动可选.. 版本1中是一定跟随的
			Version_MaxBox = 3, //增加了包围盒 m_rctMaxBox
			Version_SkewType = 4, //错切改为水平切和垂直切两种..以前版本的上切对应旋转点m_rotateCent为图像下边缘上的点，下切对应..
			Version_MoveFollowType = 5, //m_nMoveFollowType
			Version_dwFlag = 6, //做灯光物件用
			Version_ZOOMPARAM = 7, //动态物件可能被扰动导致激烈运动等..
			Version_TransformType = 8, //除旋转错切之外，增加平移参数..
			Version_NotMountElement = 9,	//不mount elements了
			CURVER = 9,
		};	

	public:
		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);
		DWORD GetMainNodeID() const { return m_MainNodeID; }

	public:
		NuclearPoint m_ptCentPos; //plant的重心,(相对于主节点的左上角..)
		NuclearPoint m_ptBasePos[4];//plant的底座,----位置关系同上
		NuclearRect m_rctMaxBox; //以重心为坐标原点，水平右x+，垂直下y+

		//版本7增加
		bool m_bLKOEnableZoom; //是否允许被干扰
		float m_fLKOzoom;//干扰最激烈程度
		float m_fLKOTime;//恢复所需时间
		bool  m_bStatic;//是否为静止不动物件
		//以上..版本7
	
		typedef std::vector<sPLANTPICINFO> VectorLinkedObjectInfo;
		VectorLinkedObjectInfo m_vectorLinkedObjectInfo; //ID--读文件时建立...不写磁盘..

	private:
		DWORD m_MainNodeID; //主节点ID

	};

}

#endif