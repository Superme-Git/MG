#pragma once
#include "..\engine\common\xptypes.h"
#include "..\engine\common\pobject.h"
namespace Nuclear
{
	class Renderer;
};
class COldAniPack :
	public Nuclear::PObject
{
public:
	struct sFileSec
	{
		Nuclear::CPOINT m_Offset;
		Nuclear::XBuffer m_Data;
		Nuclear::PictureHandle m_Handle;

		Nuclear::CRECT m_OffsetVersion10; 
		std::wstring m_strPicPathVersion10; 
		Nuclear::FRECT m_rctDataVersion10; 
	};
	virtual ~COldAniPack(void);

	COldAniPack(Nuclear::Renderer *pRenderer, std::wstring oldAniPath) : m_pRenderer(pRenderer)
	{
		m_strOldAniPath = oldAniPath;
	}


private:


	friend class CSpriteEditorDoc;
	

	

	Nuclear::CPOINT m_Center;
	Nuclear::XPBase m_BaseVersion10;
	std::vector<std::vector<std::vector<sFileSec> > > m_Data;


	std::vector<int> m_Dirs;
	Nuclear::CRECT m_Border;		
	Nuclear::XBase m_Base;
	Nuclear::XPEFFECT_BIND_TYPE m_BindType;		
	int m_SystemLevel;	
	int m_Color;
	int m_DirMode;		
	int m_nFrame;
	int m_nRegion;		
	int m_Time;			
	int m_Blend;		
	int m_TexFormat;		
	int m_FileFormat;		
	int m_nVersion;

	bool m_bOldVersion;

	Nuclear::Renderer *m_pRenderer;

	std::wstring m_strOldAniPath; 
	enum { 
		VERSION_13 = 13, 
		VERSION_12 = 12,
		VERSION_NO_CENTER = 11,
		VERSION_OLD_BASE = 10,
		VERSION = 9,
		OLD_VERSION_NO_SYS_LEVEL = 8,
		OLD_VERSION_NO_BIND_TYPE = 7,
	};



public:
	int GetVersion() const { return m_nVersion; }
	bool IsOldVersion() const { return m_bOldVersion; }

	void SetSystemLevel(int syslevel) { m_SystemLevel = syslevel; }
	void SetEffectBindType(Nuclear::XPEFFECT_BIND_TYPE t) { m_BindType = t; }
	void SetTime(int time) { m_Time = time; }
	void SetBlend(Nuclear::XPTEXTURE_BLENDMODE blend) { m_Blend = blend; }
	Nuclear::XPTEXTURE_FORMAT GetTexFormat() const { return (Nuclear::XPTEXTURE_FORMAT)m_TexFormat; }

	Nuclear::XPIMAGE_FILEFORMAT GetFileFormat() const { return (Nuclear::XPIMAGE_FILEFORMAT)m_FileFormat; }
	void SetBorder(const Nuclear::CRECT &border) { m_Border = border; } 
	bool GetBorder(Nuclear::CRECT &border) const { border = m_Border; return true; }
	void SetBase(const Nuclear::XBase &base) { m_Base = base; } 
	bool GetBase(Nuclear::XBase &base) const { base = m_Base; return true; }
	const std::vector<std::vector<std::vector<sFileSec> > >& GetData() const { return m_Data; }

	const std::vector<int>& GetDirs() const { return m_Dirs; }
	int GetDirMode() const { return m_DirMode; }
	int GetRegionCount() const { return m_nRegion; }
	int GetFrameCount() const { return m_nFrame; }
	void FreeOldAnimationPack();
	virtual const Nuclear::XIStream& unmarshal(const Nuclear::XIStream &);

	virtual Nuclear::XOStream& marshal(Nuclear::XOStream &) const;
};
