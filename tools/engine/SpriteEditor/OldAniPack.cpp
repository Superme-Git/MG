#include "stdafx.h"
#include "..\engine\engine\animanager.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\common\util.h"
#include "OldAniPack.h"

const Nuclear::XIStream& COldAniPack::unmarshal(const Nuclear::XIStream &os)
{
	int dummy;
	os >> dummy;

	m_nVersion = dummy;

	m_bOldVersion = true;
	int version = dummy;
	if (version > VERSION_13)
	{
		m_bOldVersion = false;
		throw Exception();
	}

	int i = 0, j = 0, k=0;

	os >> m_FileFormat;
	os >> m_TexFormat;
	os >> m_Blend;			
	os >> m_Time;
	os >> m_nRegion;
	os >> m_nFrame;
	int nOldDir;
	if( version < 7 )
		os >> nOldDir;
	else
		os >> m_DirMode;
	os >> m_Color;
	if (version > OLD_VERSION_NO_SYS_LEVEL)
	{
		os >> m_SystemLevel;
	} else {
		m_SystemLevel = Nuclear::XPCOMPUTERCONFIG_L;
	}
	if ( version > OLD_VERSION_NO_BIND_TYPE )
	{
		int bindtype;
		os >> bindtype;
		m_BindType = static_cast<Nuclear::XPEFFECT_BIND_TYPE>(bindtype);
	} else {
		m_BindType = Nuclear::XPEBT_ORIGIN;
	}

	Nuclear::CPOINT oldcenter(0, 0);
	if(version > VERSION)
	{
		if (dummy > VERSION_OLD_BASE)
		{
			os >> m_BaseVersion10.left;
			os >> m_BaseVersion10.right;
			if (dummy > VERSION_NO_CENTER)
			{
				os >> m_Center;
			}
		} else {
			Nuclear::CPOINT unuse;
			os >> oldcenter;
			m_Center = oldcenter;
			os >> m_BaseVersion10.left;
			os >> unuse;
			os >> m_BaseVersion10.right;
			os >> unuse;
		}
	}
	else
	{
		os >> m_Base.center;
		for(i=0; i<4; ++i)
			os >> m_Base.offset[i];
	}

	if( version > 5 )
	{
		os >> m_Border;
	}
	else
	{
		m_Border.Assign(-50,-50,50,50);
	}

	if( version < 7 )
	{
		switch( nOldDir )
		{
		case 3:
			m_DirMode = Nuclear::XPANIDM_3;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			break;
		case 4:
			m_DirMode = Nuclear::XPANIDM_4;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			break;
		case 8:
			m_DirMode = Nuclear::XPANIDM_8;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			os >> dummy;
			break;
		case -1:
			m_DirMode = Nuclear::XPANIDM_8USE1;
			break;
		default:
			m_DirMode = Nuclear::XPANIDM_NULL;
			break;
		}
	}

	m_Dirs.clear();
	switch( m_DirMode )
	{
	case Nuclear::XPANIDM_3:
		m_Dirs.push_back(3);
		m_Dirs.push_back(4);
		m_Dirs.push_back(5);
		break;
	case Nuclear::XPANIDM_4:
		m_Dirs.push_back(1);
		m_Dirs.push_back(3);
		m_Dirs.push_back(5);
		m_Dirs.push_back(7);
		break;
	case Nuclear::XPANIDM_8:
		m_Dirs.push_back(0);
		m_Dirs.push_back(1);
		m_Dirs.push_back(2);
		m_Dirs.push_back(3);
		m_Dirs.push_back(4);
		m_Dirs.push_back(5);
		m_Dirs.push_back(6);
		m_Dirs.push_back(7);
		break;
	case Nuclear::XPANIDM_8FROM5:
		m_Dirs.push_back(0);
		m_Dirs.push_back(1);
		m_Dirs.push_back(2);
		m_Dirs.push_back(3);
		m_Dirs.push_back(4);
		break;
	case Nuclear::XPANIDM_8FROM1:
		m_Dirs.push_back(2);
		break;
	case Nuclear::XPANIDM_8USE1:			
		m_Dirs.push_back(0);
		break;
	case Nuclear::XPANIDM_4FROM2:
		m_Dirs.push_back(3);
		m_Dirs.push_back(7);
		break;
	default:
		break;
	}

	m_Data.resize(m_Dirs.size());
	for(i=0; i<(int)m_Dirs.size(); ++i)
	{
		m_Data[i].resize(m_nFrame);
		for(j=0; j<m_nFrame; ++j)
		{
			m_Data[i][j].resize(m_nRegion);
			for(k=0; k<m_nRegion;++k)
			{
				if(version > VERSION)
				{
					os >> m_Data[i][j][k].m_OffsetVersion10;
					m_Data[i][j][k].m_OffsetVersion10 -= oldcenter;
					os >> m_Data[i][j][k].m_strPicPathVersion10;
					os >> m_Data[i][j][k].m_rctDataVersion10;

					m_Data[i][j][k].m_Handle = Nuclear::INVALID_PICTURE_HANDLE;
				}
				else
				{
					os >> m_Data[i][j][k].m_Offset;
					os >> m_Data[i][j][k].m_Data;
					m_Data[i][j][k].m_Handle = Nuclear::INVALID_PICTURE_HANDLE;		
				}
			}
		}
	}

	return os;
}

Nuclear::XOStream& COldAniPack::marshal(Nuclear::XOStream &os) const
{
	throw Exception();
	return os;
}

void COldAniPack::FreeOldAnimationPack()
{
	int i,j,k;
	for(i=0; i<(int)m_Dirs.size(); ++i)
	{
		for(j=0; j<m_nFrame; ++j)
		{
			for(k=0; k<m_nRegion;++k)
			{
				if (m_Data[i][j][k].m_Handle != Nuclear::INVALID_PICTURE_HANDLE)
				{
					m_pRenderer->FreePicture(m_Data[i][j][k].m_Handle);
				}
			}
		}
	}
}

COldAniPack::~COldAniPack(void)
{
	FreeOldAnimationPack();
}

