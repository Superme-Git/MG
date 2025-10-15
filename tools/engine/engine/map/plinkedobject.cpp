#include "stdafx.h"
#include "plinkedobject.h"

namespace Nuclear
{

PLinkedObject::PLinkedObject(void)
{
	m_ptCentPos = CPOINT(); //plant的重心,(相对于主节点的左上角..)
	for(int i=0; i<4; i++)
		m_ptBasePos[i] = CPOINT();//plant的底座,----位置关系同上	
	m_vectorLinkedObjectInfo.clear();

	m_dwMainNodeID = 0;

	m_rctMaxBox = CRECT(-500, -500, 500, 500);

	m_bLKOEnableZoom = true; //是否允许被干扰
	m_fLKOzoom = 1.0f;//干扰最激烈程度
	m_fLKOTime = 0.0f;//恢复所需时间

	m_bStatic = false;//是否为静止不动物件
}

PLinkedObject::~PLinkedObject(void)
{
}

XOStream& PLinkedObject::marshal(XOStream &os) const
{ //save
	os << (int)CURVER;

	os << m_ptCentPos;
	for(int i=0; i<4; ++i)
		os << m_ptBasePos[i];

	os << m_rctMaxBox;

	os << m_bLKOEnableZoom; //7
	os << m_fLKOzoom; //7
	os << m_fLKOTime; //7
	os << m_bStatic;

	unsigned long count = (unsigned long)m_vectorLinkedObjectInfo.size();
	os << count;

	VectorLinkedObjectInfo::const_iterator myIterator = m_vectorLinkedObjectInfo.begin();
	unsigned int nameSize = 0;
	for ( ; myIterator != m_vectorLinkedObjectInfo.end(); ++myIterator)
	{
		//nameSize = static_cast<unsigned int>(myIterator->m_strPicName.size() * sizeof(wchar_t));
		//os<<nameSize;
		//os.push_byte(myIterator->m_strPicName.data(), nameSize);
		os << myIterator->m_strPicName;			//修正上面的bug 采用标准方式

		os<<myIterator->m_bMainNode;
	//	os<<myIterator->m_bMoveFollow; //版本2增加的  ---  版本5删除的
		os<<myIterator->m_nWidth;
		os<<myIterator->m_nHeight;
		os<<myIterator->m_LinkedObjectMoveParam.m_dwFlag;

		os<<myIterator->m_ptOffset;
 
//  		if (myIterator->m_LinkedObjectMoveParam.m_nDistortionType == XPPLANTSKEW)	//错切
//  		{
//  			CPOINT tmpPoint;
//  			int adjX, adjY;
//  			adjX = myIterator->m_rotateCent.x + myIterator->m_nWidth / 2;
//  			adjY = myIterator->m_rotateCent.y + myIterator->m_nHeight / 2;
//  			tmpPoint.x = myIterator->m_rotateCent.x - adjX;
//  			tmpPoint.y = myIterator->m_rotateCent.y - adjY;
//  			os<<tmpPoint;
//  			tmpPoint.x = myIterator->m_rotateCent_2.x - adjX;
//  			tmpPoint.y = myIterator->m_rotateCent_2.y - adjY;
//  			os<<tmpPoint;
//  		}
//  		else
//		{
			os<<myIterator->m_rotateCent;
			os<<myIterator->m_rotateCent_2;
//		}

		os<<myIterator->m_LinkedObjectMoveParam.m_fAngle1;
		os<<myIterator->m_LinkedObjectMoveParam.m_fAngle2;
		os<<myIterator->m_LinkedObjectMoveParam.m_fSpeed;
		os<<(int)(myIterator->m_LinkedObjectMoveParam.m_nDistortionType);
		os<<(int)(myIterator->m_LinkedObjectMoveParam.m_nSkewType);
		os<<(int)(myIterator->m_LinkedObjectMoveParam.m_nMoveFollowType); //版本5增加的..
		os<<myIterator->m_LinkedObjectMoveParam.m_nSkewPixelX;
		os<<myIterator->m_LinkedObjectMoveParam.m_nSkewSpeedX;

		//版本8增加的 ---平移参数
		os<<myIterator->m_LinkedObjectMoveParam.m_fSizeLeft;
		os<<myIterator->m_LinkedObjectMoveParam.m_fSizeRight;
		os<<myIterator->m_LinkedObjectMoveParam.m_fSizeTop;
		os<<myIterator->m_LinkedObjectMoveParam.m_fSizeDown;
		os<<myIterator->m_LinkedObjectMoveParam.m_fTranslationSpeedx;
		os<<myIterator->m_LinkedObjectMoveParam.m_fTranslationSpeedy;
	}

	return os;
}

const XIStream& PLinkedObject::unmarshal(const XIStream &os)
{ //Load
	int nVer;
	os >> nVer;

	os >> m_ptCentPos;
	for(int i=0; i<4; i++)
		os >> m_ptBasePos[i];

	if(nVer > 2)
		os >> m_rctMaxBox;

	if(nVer > 6)
	{
		os >> m_bLKOEnableZoom;
		os >> m_fLKOzoom;
		os >> m_fLKOTime;
		os >> m_bStatic;
	}

	unsigned long count = 0;
	os >> count;

	unsigned int str_size = 0;
	wchar_t *str = NULL;

	m_vectorLinkedObjectInfo.clear();
	for(unsigned long i=0; i<count; i++)
	{
		PLANTPICINFO plantInfo;

		/*os >> str_size;
		str = static_cast<wchar_t*>(XAlloc(str_size*2+2));
		ZeroMemory(str, str_size*2+2);
		os.pop_byte(str, str_size*2);
		plantInfo.m_strPicName = str;
		XFree(str);*/
		os >> plantInfo.m_strPicName;	//采用标准方式实现上面的代码

		os>>plantInfo.m_bMainNode;
		if( (nVer > 1) && (nVer < Version_MoveFollowType) )
		{
			bool bMoveFollow;
			os>>bMoveFollow;
			if(bMoveFollow)
				plantInfo.m_LinkedObjectMoveParam.m_nMoveFollowType = XPMOVEFOLLOWFLEXIBLE;
			else
				plantInfo.m_LinkedObjectMoveParam.m_nMoveFollowType = XPMOVEFOLLOWNO;
		}

		os>>plantInfo.m_nWidth;
		os>>plantInfo.m_nHeight;

		if(nVer < Version_dwFlag)
		{
			plantInfo.m_LinkedObjectMoveParam.m_dwFlag = 0x00000000;
		}
		else
			os>>plantInfo.m_LinkedObjectMoveParam.m_dwFlag;

		os>>plantInfo.m_ptOffset;
		os>>plantInfo.m_rotateCent;
		os>>plantInfo.m_rotateCent_2;
		os>>plantInfo.m_LinkedObjectMoveParam.m_fAngle1;
		os>>plantInfo.m_LinkedObjectMoveParam.m_fAngle2;
		os>>plantInfo.m_LinkedObjectMoveParam.m_fSpeed;
		int nDistortionType = 0;
		os>>nDistortionType;
		plantInfo.m_LinkedObjectMoveParam.m_nDistortionType = (XPPLANTDISTORTIONTYPE)nDistortionType;

		int nSkewType = 0;
		os>>nSkewType;
		plantInfo.m_LinkedObjectMoveParam.m_nSkewType = (XPPLANTSKEWTYPE)nSkewType;

		if(plantInfo.m_LinkedObjectMoveParam.m_nDistortionType == XPPLANTSKEW)
		{
			if(nVer < 4)//以前的版本错切分上切和下切，旋转中心没用
			{
				CPOINT pt = plantInfo.m_rotateCent;
				if(plantInfo.m_LinkedObjectMoveParam.m_nSkewType == 0) //上切
				{
					plantInfo.m_rotateCent = CPOINT(0, plantInfo.m_nHeight/2);
				}
				else //下切
				{
					plantInfo.m_rotateCent = CPOINT(0, -plantInfo.m_nHeight/2);
				}
				plantInfo.m_LinkedObjectMoveParam.m_nSkewType = XPSKEWHORIZONTAL; //改为水平切..
				pt = pt - plantInfo.m_rotateCent;
				plantInfo.m_rotateCent_2 = plantInfo.m_rotateCent_2 -pt;
			}
		}

		if(nVer > Version_SkewType) //从版本5开始增加的
		{
			int nMoveFollowType = 0;
			os>>nMoveFollowType;
			plantInfo.m_LinkedObjectMoveParam.m_nMoveFollowType = (XPMOVEFOLLOWTYPE)nMoveFollowType;
		}

		os>>plantInfo.m_LinkedObjectMoveParam.m_nSkewPixelX;
		os>>plantInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX;

		if(nVer > Version_ZOOMPARAM) //版本8增加的
		{
			os>>plantInfo.m_LinkedObjectMoveParam.m_fSizeLeft;;
			os>>plantInfo.m_LinkedObjectMoveParam.m_fSizeRight;
			os>>plantInfo.m_LinkedObjectMoveParam.m_fSizeTop;
			os>>plantInfo.m_LinkedObjectMoveParam.m_fSizeDown;
			os>>plantInfo.m_LinkedObjectMoveParam.m_fTranslationSpeedx;
			os>>plantInfo.m_LinkedObjectMoveParam.m_fTranslationSpeedy;
		}

		m_vectorLinkedObjectInfo.push_back( plantInfo );
		if(plantInfo.m_bMainNode)
			m_dwMainNodeID = i;
	}

	return os;
}


}
