#pragma once
#include "..\engine\soundarea\xpenvsoundcirclearea.h"
#include "..\engine\soundarea\xpenvsoundpolygonarea.h"
#include "..\engine\soundarea\xpbkgsoundarea.h"
#include "..\engine\soundarea\xpbkgvolarea.h"
class CSoundEditorDoc;
class CActionList;
class CAllResDlg;
class CToolsSoundArea
{
protected:
	const static DWORD NON_SELECTED_INNER_COLOR = 0x2000FF00;
	const static DWORD NON_SELECTED_OUTTER_COLOR = 0x20FF0000;
	const static DWORD SELECTED_INNER_COLOR = 0xA000FF00;
	const static DWORD SELECTED_OUTTER_COLOR = 0xA0FF0000;
	const static DWORD SELECTED_DIV_COLOR = 0xFFFFFFFF;
	friend class CSoundEditorDoc;
	friend class CActionList;
	friend class CAllResDlg;
public:
	const static int CHECK_CLICK_RADIUS = 3;
	typedef std::list<Nuclear::XPPolygon> PolygonCont;
	typedef std::list<Nuclear::CPOINT> CutPairCont;
	typedef std::vector<DWORD_PTR> HandleVet;
	typedef std::vector<HandleVet> HandleVetVet;

//只能由Doc、OperatorList来修改Area
protected:
	int nSoundListBeginID;//（含）
	int nSoundListEndID;//（不含），例如这个区域有3个声音，从5开始，分别是5 6 7，那么Begin是5，End是8
	int nOrders;
private:
	virtual int Load(XMLIO::CINode& node, int flag) = 0;
	virtual int Save(XMLIO::CONode& node, int flag) = 0;
	virtual void ResetAreaRegion(const POINT &mapSize) = 0;
	virtual void Move(const POINT& pt) = 0;
	virtual bool SetShape(const Nuclear::XPIShape *pShape) = 0;

public:
	CToolsSoundArea() : nSoundListBeginID(0), nSoundListEndID(0), nOrders(0) {}
	virtual ~CToolsSoundArea() {}
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const = 0;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const = 0;
	virtual Nuclear::CPOINT GetPos() const = 0;
	virtual Nuclear::XPAREA_TYPE GetType() const = 0;
	virtual int CheckClickPoint(const POINT &pt) const = 0;
	virtual int CheckClickLinePoint(const POINT &pt) const { return -1; }
	virtual void GetShape(Nuclear::XPIShape* pShape) const = 0;
	int GetBeginID() const { return nSoundListBeginID; }
	int GetEndID() const { return nSoundListEndID; }
	int GetOrders() const { return nOrders; }
	virtual void ClearSoundHandle() = 0;

	static int LoadPolygonToolsData(XMLIO::CINode& node, CutPairCont& toolsData);
	static int SavePolygonToolsData(XMLIO::CONode& node, const CutPairCont& toolsData);
};

//====================================================背景音音量区域===================================================
class CToolsBkgVolumeArea : public CToolsSoundArea
{
protected:
	friend class CSoundEditorDoc;
	friend class CActionList;

private://实现
	virtual void ClearSoundHandle() {}

	//只能由Doc、OperatorList来修改Area
private:
	virtual void SetVol(float v) = 0;
	virtual void SetTransition(int nTransition) = 0;
	
public:
	CToolsBkgVolumeArea(void) {}
	virtual ~CToolsBkgVolumeArea(void) {}
	virtual float GetVol() const = 0;
	virtual int GetTransition() const = 0;
};

class CToolsBkgVolCircleArea : 
	public Nuclear::XPBkgVolCircleArea, public CToolsBkgVolumeArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	Nuclear::XPCircle *m_pShape;
private:
	//自有方法
	void ResetShape();
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { int result = Nuclear::XPBkgVolCircleArea::Load(node, flag); ResetShape(); return result; }
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPBkgVolCircleArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	virtual void SetVol(float v) { m_fVol = v; }
	virtual void SetTransition(int nTransition);
	
public:
	//构造析构
	CToolsBkgVolCircleArea(void) : m_pShape(NULL) {}
	virtual ~CToolsBkgVolCircleArea(void) { if (m_pShape) delete m_pShape; }

	//方法们
	
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const { return m_circle.m_pos; }
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPBkgVolCircleArea::GetType(); }
	virtual int GetTransition() const { return Nuclear::XPBkgVolCircleArea::GetTransition(); }
	virtual float GetVol() const { return Nuclear::XPBkgVolCircleArea::GetVol(); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	virtual CToolsBkgVolumeArea* Clone() const;
};

class CToolsBkgVolPolygonArea :
	public Nuclear::XPBkgVolPolygonArea, public CToolsBkgVolumeArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	PolygonCont m_ToolsInnerPolygons;
	PolygonCont m_ToolsOutterPolygons;
	CutPairCont m_ToolsCutPairs;

private:
	void ClearSPolygons();
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { int result = Nuclear::XPBkgVolPolygonArea::Load(node, flag); ResetToolsPolygon(); return result;}
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPBkgVolPolygonArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual void SetTransition(int nTransition);
	virtual void SetVol(float v) { m_fVol = v; }
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	void ResetOutter();
	void ResetToolsPolygon();
	void ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys);
protected:
	virtual int LoadToolsData(XMLIO::CINode& node) { return LoadPolygonToolsData(node, m_ToolsCutPairs); }
	virtual int SaveToolsData(XMLIO::CONode& node) const { return SavePolygonToolsData(node, m_ToolsCutPairs); }

public:
	//构造析构
	CToolsBkgVolPolygonArea(void) {}
	virtual ~CToolsBkgVolPolygonArea(void) {}

	//方法们
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const;
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPBkgVolPolygonArea::GetType(); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual int CheckClickLinePoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	virtual CToolsBkgVolumeArea* Clone() const;
	Nuclear::CPOINT GetPoint(int ptID) const { return m_innerPolygon.m_points[ptID]; }
	virtual int GetTransition() const { return Nuclear::XPBkgVolPolygonArea::GetTransition(); }
	virtual float GetVol() const { return Nuclear::XPBkgVolPolygonArea::GetVol(); }
};

//====================================================背景音乐区域===================================================
class CToolsBkgSoundArea : public CToolsSoundArea
{
protected:
	friend class CSoundEditorDoc;
	friend class CActionList;
	HandleVet soundHandleVet;

//只能由Doc、OperatorList来修改Area
private:
	virtual void SetVol(int soundID, int v) = 0;
	virtual void SetSoundType(int soundID, Nuclear::XPSA_SOUND_TYPE type) = 0;
	virtual int AddSound(const std::wstring &name) = 0;
	virtual void InsertSound(int soundID, const Nuclear::XPBkgSoundArea::SoundParam &param) = 0;//用来undo delete
	virtual void DeleteSound(int soundID) = 0;
	virtual void SetMinInterval(int v) = 0;
	virtual void SetMaxInterval(int v) = 0;
	virtual void SetFadeInTime(unsigned short t) = 0;
	virtual void SetFadeOutTime(unsigned short t) = 0;
	virtual void SetWaitPrevAreaPlayEnd(bool b) = 0;
	virtual void SetPlayMode(Nuclear::XPBGM_PLAY_TYPE mode) = 0;

public:
	CToolsBkgSoundArea(void) {}
	virtual ~CToolsBkgSoundArea(void) {}
	virtual void GetSoundList(std::vector<std::wstring> &slist) const = 0;
	virtual int GetVol(int soundID) const = 0;
	virtual Nuclear::XPSA_SOUND_TYPE GetSoundType(int soundID) const = 0;
	virtual const Nuclear::XPBkgSoundArea::SoundParam& GetSoundParam(int soundID) const = 0;
	virtual int GetMinInterval() const = 0;
	virtual int GetMaxInterval() const = 0;
	virtual unsigned short GetFadeInTime() const = 0;
	virtual unsigned short GetFadeOutTime() const = 0;
	virtual bool IsWaitPrevAreaPlayEnd() const = 0;
	virtual Nuclear::XPBGM_PLAY_TYPE GetPlayMode() const = 0;
	DWORD_PTR GetHandle(int soundID) const
	{
		assert((int)soundHandleVet.size() > soundID);
		return soundHandleVet[soundID];
	}
	virtual void ClearSoundHandle() {
		nSoundListBeginID = 0;
		nSoundListEndID = 0;
		nOrders = 0;
		soundHandleVet.clear(); 
	}
};

class CToolsBkgDefaultSoundArea : 
	public Nuclear::XPBkgDefaultSoundArea, public CToolsBkgSoundArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { return Nuclear::XPBkgDefaultSoundArea::Load(node, flag); }
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPBkgDefaultSoundArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize) {}
	virtual void Move(const POINT& pt) {}
	virtual bool SetShape(const Nuclear::XPIShape *pShape) { return false; }
	virtual void SetVol(int soundID, int v) { Nuclear::XPBkgSoundArea::SetVol(soundID, v); }
	virtual void SetSoundType(int soundID, Nuclear::XPSA_SOUND_TYPE type) { Nuclear::XPBkgSoundArea::SetSoundType(soundID, type); }
	virtual int AddSound(const std::wstring &name) { return Nuclear::XPBkgSoundArea::AddSound(name); }
	virtual void InsertSound(int soundID, const Nuclear::XPBkgSoundArea::SoundParam &param) { Nuclear::XPBkgSoundArea::InsertSound(soundID, param); }
	virtual void DeleteSound(int soundID) { return Nuclear::XPBkgSoundArea::DeleteSound(soundID); }
	virtual void SetMinInterval(int v) { Nuclear::XPBkgSoundArea::SetMinInterval(v); }
	virtual void SetMaxInterval(int v) { Nuclear::XPBkgSoundArea::SetMaxInterval(v); }
	virtual void SetFadeInTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeInTime(t); }
	virtual void SetFadeOutTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeOutTime(t); }
	virtual void SetWaitPrevAreaPlayEnd(bool b) { Nuclear::XPBkgSoundArea::SetWaitPrevAreaPlayEnd(b); }
	virtual void SetPlayMode(Nuclear::XPBGM_PLAY_TYPE mode) { Nuclear::XPBkgSoundArea::SetPlayMode(mode); }

public:
	//构造析构
	CToolsBkgDefaultSoundArea() : Nuclear::XPBkgDefaultSoundArea(NULL) {}
	virtual ~CToolsBkgDefaultSoundArea(void) {}

	//方法们	
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const {};
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const {};
	virtual Nuclear::CPOINT GetPos() const { return Nuclear::CPOINT(0, 0); }
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPBkgDefaultSoundArea::GetType(); }
	virtual void GetSoundList(std::vector<std::wstring> &slist) const { return Nuclear::XPBkgSoundArea::GetSoundList(slist);}
	virtual int GetVol(int soundID) const { return Nuclear::XPBkgSoundArea::GetVol(soundID); }
	virtual Nuclear::XPSA_SOUND_TYPE GetSoundType(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundType(soundID); }
	virtual const Nuclear::XPBkgSoundArea::SoundParam& GetSoundParam(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundParam(soundID); }
	virtual int CheckClickPoint(const POINT &pt) const { return 0; }
	virtual void GetShape(Nuclear::XPIShape* pShape) const {}
	virtual int GetMinInterval() const { return Nuclear::XPBkgSoundArea::GetMinInterval(); }
	virtual int GetMaxInterval() const { return Nuclear::XPBkgSoundArea::GetMaxInterval(); }
	virtual unsigned short GetFadeInTime() const { return Nuclear::XPBkgSoundArea::GetFadeInTime(); }
	virtual unsigned short GetFadeOutTime() const { return Nuclear::XPBkgSoundArea::GetFadeOutTime(); }
	virtual bool IsWaitPrevAreaPlayEnd() const { return Nuclear::XPBkgSoundArea::IsWaitPrevAreaPlayEnd(); }
	virtual Nuclear::XPBGM_PLAY_TYPE GetPlayMode() const { return Nuclear::XPBkgSoundArea::GetPlayMode(); }
};

class CToolsBkgSoundCircleArea : 
	public Nuclear::XPBkgSoundCircleArea, public CToolsBkgSoundArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
private:
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { return Nuclear::XPBkgSoundCircleArea::Load(node, flag); }
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPBkgSoundCircleArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	virtual void SetVol(int soundID, int v) { Nuclear::XPBkgSoundArea::SetVol(soundID, v); }
	virtual void SetSoundType(int soundID, Nuclear::XPSA_SOUND_TYPE type) { Nuclear::XPBkgSoundArea::SetSoundType(soundID, type); }
	virtual int AddSound(const std::wstring &name) { return Nuclear::XPBkgSoundArea::AddSound(name); }
	virtual void InsertSound(int soundID, const Nuclear::XPBkgSoundArea::SoundParam &param) { Nuclear::XPBkgSoundArea::InsertSound(soundID, param); }
	virtual void DeleteSound(int soundID) { return Nuclear::XPBkgSoundArea::DeleteSound(soundID); }
	virtual void SetMinInterval(int v) { Nuclear::XPBkgSoundArea::SetMinInterval(v); }
	virtual void SetMaxInterval(int v) { Nuclear::XPBkgSoundArea::SetMaxInterval(v); }
	virtual void SetFadeInTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeInTime(t); }
	virtual void SetFadeOutTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeOutTime(t); }
	virtual void SetWaitPrevAreaPlayEnd(bool b) { Nuclear::XPBkgSoundArea::SetWaitPrevAreaPlayEnd(b); }
	virtual void SetPlayMode(Nuclear::XPBGM_PLAY_TYPE mode) { Nuclear::XPBkgSoundArea::SetPlayMode(mode); }

public:
	//构造析构
	CToolsBkgSoundCircleArea(void) : Nuclear::XPBkgSoundCircleArea(NULL) {}
	virtual ~CToolsBkgSoundCircleArea(void) {}

	//方法们	
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const { return m_circle.m_pos; }
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPBkgSoundCircleArea::GetType(); }
	virtual void GetSoundList(std::vector<std::wstring> &slist) const { return Nuclear::XPBkgSoundArea::GetSoundList(slist);}
	virtual int GetVol(int soundID) const { return Nuclear::XPBkgSoundArea::GetVol(soundID); }
	virtual Nuclear::XPSA_SOUND_TYPE GetSoundType(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundType(soundID); }
	virtual const Nuclear::XPBkgSoundArea::SoundParam& GetSoundParam(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundParam(soundID); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	virtual int GetMinInterval() const { return Nuclear::XPBkgSoundArea::GetMinInterval(); }
	virtual int GetMaxInterval() const { return Nuclear::XPBkgSoundArea::GetMaxInterval(); }
	virtual unsigned short GetFadeInTime() const { return Nuclear::XPBkgSoundArea::GetFadeInTime(); }
	virtual unsigned short GetFadeOutTime() const { return Nuclear::XPBkgSoundArea::GetFadeOutTime(); }
	virtual bool IsWaitPrevAreaPlayEnd() const { return Nuclear::XPBkgSoundArea::IsWaitPrevAreaPlayEnd(); }
	virtual Nuclear::XPBGM_PLAY_TYPE GetPlayMode() const { return Nuclear::XPBkgSoundArea::GetPlayMode(); }
};

class CToolsBkgSoundPolygonArea :
	public Nuclear::XPBkgSoundPolygonArea, public CToolsBkgSoundArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	PolygonCont m_ToolsPolygons;
	CutPairCont m_ToolsCutPairs;

private:
	void ClearSPolygons();
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { int result = Nuclear::XPBkgSoundPolygonArea::Load(node, flag); ResetToolsPolygon(); return result;}
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPBkgSoundPolygonArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	virtual void SetVol(int soundID, int v) { Nuclear::XPBkgSoundArea::SetVol(soundID, v); }
	virtual void SetSoundType(int soundID, Nuclear::XPSA_SOUND_TYPE type) { Nuclear::XPBkgSoundArea::SetSoundType(soundID, type); }
	virtual int AddSound(const std::wstring &name) { return Nuclear::XPBkgSoundArea::AddSound(name); }
	virtual void InsertSound(int soundID, const Nuclear::XPBkgSoundArea::SoundParam &param) { Nuclear::XPBkgSoundArea::InsertSound(soundID, param); }
	virtual void DeleteSound(int soundID) { return Nuclear::XPBkgSoundArea::DeleteSound(soundID); }
	void ResetToolsPolygon();
	void ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys);
	virtual void SetMinInterval(int v) { Nuclear::XPBkgSoundArea::SetMinInterval(v); }
	virtual void SetMaxInterval(int v) { Nuclear::XPBkgSoundArea::SetMaxInterval(v); }
	virtual void SetFadeInTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeInTime(t); }
	virtual void SetFadeOutTime(unsigned short t) { Nuclear::XPBkgSoundArea::SetFadeOutTime(t); }
	virtual void SetWaitPrevAreaPlayEnd(bool b) { Nuclear::XPBkgSoundArea::SetWaitPrevAreaPlayEnd(b); }
	virtual void SetPlayMode(Nuclear::XPBGM_PLAY_TYPE mode) { Nuclear::XPBkgSoundArea::SetPlayMode(mode); }
protected:
	virtual int LoadToolsData(XMLIO::CINode& node) { return LoadPolygonToolsData(node, m_ToolsCutPairs); }
	virtual int SaveToolsData(XMLIO::CONode& node) const { return SavePolygonToolsData(node, m_ToolsCutPairs); }

public:
	//构造析构
	CToolsBkgSoundPolygonArea(void) : Nuclear::XPBkgSoundPolygonArea(NULL) {}
	virtual ~CToolsBkgSoundPolygonArea(void) {}

	//方法们
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const;
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPBkgSoundPolygonArea::GetType(); }
	virtual void GetSoundList(std::vector<std::wstring> &slist) const { return Nuclear::XPBkgSoundArea::GetSoundList(slist);}
	virtual int GetVol(int soundID) const { return Nuclear::XPBkgSoundArea::GetVol(soundID); }
	virtual Nuclear::XPSA_SOUND_TYPE GetSoundType(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundType(soundID); }
	virtual const Nuclear::XPBkgSoundArea::SoundParam& GetSoundParam(int soundID) const { return Nuclear::XPBkgSoundArea::GetSoundParam(soundID); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual int CheckClickLinePoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	Nuclear::CPOINT GetPoint(int ptID) const { return m_polygon.m_points[ptID]; }
	virtual int GetMinInterval() const { return Nuclear::XPBkgSoundArea::GetMinInterval(); }
	virtual int GetMaxInterval() const { return Nuclear::XPBkgSoundArea::GetMaxInterval(); }
	virtual unsigned short GetFadeInTime() const { return Nuclear::XPBkgSoundArea::GetFadeInTime(); }
	virtual unsigned short GetFadeOutTime() const { return Nuclear::XPBkgSoundArea::GetFadeOutTime(); }
	virtual bool IsWaitPrevAreaPlayEnd() const { return Nuclear::XPBkgSoundArea::IsWaitPrevAreaPlayEnd(); }
	virtual Nuclear::XPBGM_PLAY_TYPE GetPlayMode() const { return Nuclear::XPBkgSoundArea::GetPlayMode(); }
};

//====================================================环境音效区域===================================================
class CToolsEnvSoundArea : public CToolsSoundArea
{
protected:
	friend class CSoundEditorDoc;
	friend class CActionList;
	HandleVetVet soundHandleVet;

//只能由Doc、OperatorList来修改Area
private:
	virtual void SetTransition(int nTransition) = 0;
	virtual void SetMaxVol(int typeID, int soundID, unsigned char v) = 0;
	virtual void SetMinVol(int typeID, int soundID, unsigned char v) = 0;
	virtual void SetMaxFreq(int typeID, int soundID, unsigned char v) = 0;
	virtual void SetMinFreq(int typeID, int soundID, unsigned char v) = 0;
	virtual void SetTime(int typeID, int soundID, int type, int time) = 0;
	virtual void SetLooping(int typeID, int soundID, bool loop) = 0;
	virtual void SetSoundType(int typeID, int soundID, unsigned char type) = 0;
	virtual void SetMinInterval(int typeID, int nInterval) = 0;
	virtual void SetMaxInterval(int typeID, int nInterval) = 0;
	virtual void SetPriority(int typeID, short priority) = 0;
	virtual int AddSound(int typeID, const std::wstring &name) = 0;
	virtual void InsertSound(int typeID, int soundID, const Nuclear::XPEnvSoundArea::SoundParam &param) = 0;//用来undo delete
	virtual void DeleteSound(int typeID, int soundID) = 0;
	virtual int AddSoundType() = 0;
	virtual void InsertSoundType(int typeID, const Nuclear::XPEnvSoundArea::SoundTypeParam &param) = 0;//用来undo delete
	virtual void DeleteSoundType(int typeID) = 0;

public:
	CToolsEnvSoundArea(void) {}
	virtual ~CToolsEnvSoundArea(void) {}
	virtual int GetTransition() const = 0;
	virtual int GetTypeCount() const = 0;
	virtual void GetSoundList(int typeID, std::vector<std::wstring> &slist) const = 0;
	virtual unsigned char GetMaxVol(int typeID, int soundID) const = 0;
	virtual unsigned char GetMinVol(int typeID, int soundID) const = 0;
	virtual unsigned char GetMaxFreq(int typeID, int soundID) const = 0;
	virtual unsigned char GetMinFreq(int typeID, int soundID) const = 0;
	virtual int GetTime(int typeID, int soundID, int type) const = 0;
	virtual bool IsLooping(int typeID, int soundID) const = 0;
	virtual unsigned char GetSoundType(int typeID, int soundID) const = 0;
	virtual int GetMinInterval(int typeID) const = 0;
	virtual int GetMaxInterval(int typeID) const = 0;
	virtual short GetPriority(int typeID) const = 0;
	virtual const Nuclear::XPEnvSoundArea::SoundParam& GetSoundParam(int typeID, int soundID) const = 0;
	virtual const Nuclear::XPEnvSoundArea::SoundTypeParam& GetSoundTypeParam(int typeID) const = 0;
	virtual CToolsEnvSoundArea* Clone() const = 0;
	DWORD_PTR GetHandle(int typeID, int soundID) const
	{
		assert((int)soundHandleVet.size() > typeID);
		assert((int)soundHandleVet[typeID].size() > soundID);
		return soundHandleVet[typeID][soundID];
	}
	virtual void ClearSoundHandle() 
	{ 
		nSoundListBeginID = 0;
		nSoundListEndID = 0;
		nOrders = 0;
		soundHandleVet.clear(); 
	}
	
};

class CToolsEnvSoundCircleArea : 
	public Nuclear::XPEnvSoundCircleArea, public CToolsEnvSoundArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	Nuclear::XPCircle *m_pShape;
private:
	//自有方法
	void ResetShape();
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { int result = Nuclear::XPEnvSoundCircleArea::Load(node, flag); ResetShape(); return result; }
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPEnvSoundCircleArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual void SetTransition(int nTransition);
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	virtual void SetMaxVol(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMaxVol(typeID, soundID, v); }
	virtual void SetMinVol(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMinVol(typeID, soundID, v); }
	virtual void SetMaxFreq(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMaxFreq(typeID, soundID, v); }
	virtual void SetMinFreq(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMinFreq(typeID, soundID, v); }
	virtual void SetTime(int typeID, int soundID, int type, int time) { Nuclear::XPEnvSoundArea::SetTime(typeID, soundID, type, time); }
	virtual void SetLooping(int typeID, int soundID, bool loop)  { Nuclear::XPEnvSoundArea::SetLooping(typeID, soundID, loop); }
	virtual void SetSoundType(int typeID, int soundID, unsigned char type) { Nuclear::XPEnvSoundArea::SetSoundType(typeID, soundID, type); }
	virtual void SetMinInterval(int typeID, int nInterval) { Nuclear::XPEnvSoundArea::SetMinInterval(typeID, nInterval); }
	virtual void SetMaxInterval(int typeID, int nInterval) { Nuclear::XPEnvSoundArea::SetMaxInterval(typeID, nInterval); }
	virtual void SetPriority(int typeID, short priority) { Nuclear::XPEnvSoundArea::SetPriority(typeID, priority); }
	virtual int AddSound(int typeID, const std::wstring &name) { return Nuclear::XPEnvSoundArea::AddSound(typeID, name); }
	virtual void InsertSound(int typeID, int soundID, const Nuclear::XPEnvSoundArea::SoundParam &param) { return Nuclear::XPEnvSoundArea::InsertSound(typeID, soundID, param); }
	virtual void DeleteSound(int typeID, int soundID) { return Nuclear::XPEnvSoundArea::DeleteSound(typeID, soundID); }
	virtual int AddSoundType() { return Nuclear::XPEnvSoundArea::AddSoundType(); }
	virtual void InsertSoundType(int typeID, const Nuclear::XPEnvSoundArea::SoundTypeParam &param) { return Nuclear::XPEnvSoundArea::InsertSoundType(typeID, param); }
	virtual void DeleteSoundType(int typeID) { return Nuclear::XPEnvSoundArea::DeleteSoundType(typeID); }

public:
//构造析构
	CToolsEnvSoundCircleArea(void) : Nuclear::XPEnvSoundCircleArea(NULL), m_pShape(NULL) {}
	virtual ~CToolsEnvSoundCircleArea(void) { if (m_pShape) delete m_pShape; }

//方法们	
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const { return m_circle.m_pos; }
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPEnvSoundCircleArea::GetType(); }
	virtual int GetTransition() const { return m_nTransition; }
	virtual int GetTypeCount() const { return Nuclear::XPEnvSoundArea::GetTypeCount(); }
	virtual void GetSoundList(int typeID, std::vector<std::wstring> &slist) const { return Nuclear::XPEnvSoundArea::GetSoundList(typeID, slist);}
	virtual unsigned char GetMaxVol(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMaxVol(typeID, soundID); }
	virtual unsigned char GetMinVol(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMinVol(typeID, soundID); }
	virtual unsigned char GetMaxFreq(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMaxFreq(typeID, soundID); }
	virtual unsigned char GetMinFreq(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMinFreq(typeID, soundID); }
	virtual int GetTime(int typeID, int soundID, int type) const  { return Nuclear::XPEnvSoundArea::GetTime(typeID, soundID, type); }
	virtual bool IsLooping(int typeID, int soundID) const  { return Nuclear::XPEnvSoundArea::IsLooping(typeID, soundID); }
	virtual unsigned char GetSoundType(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetSoundType(typeID, soundID); }
	virtual int GetMinInterval(int typeID) const { return Nuclear::XPEnvSoundArea::GetMinInterval(typeID); }
	virtual int GetMaxInterval(int typeID) const { return Nuclear::XPEnvSoundArea::GetMaxInterval(typeID); }
	virtual short GetPriority(int typeID) const { return Nuclear::XPEnvSoundArea::GetPriority(typeID); }
	virtual const Nuclear::XPEnvSoundArea::SoundParam& GetSoundParam(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetSoundParam(typeID, soundID); }
	virtual const Nuclear::XPEnvSoundArea::SoundTypeParam& GetSoundTypeParam(int typeID) const { return Nuclear::XPEnvSoundArea::GetSoundTypeParam(typeID); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	virtual CToolsEnvSoundArea* Clone() const;
};

class CToolsEnvSoundPolygonArea :
	public Nuclear::XPEnvSoundPolygonArea, public CToolsEnvSoundArea
{
	friend class CSoundEditorDoc;
	friend class CActionList;
private:
	PolygonCont m_ToolsInnerPolygons;
	PolygonCont m_ToolsOutterPolygons;
	CutPairCont m_ToolsCutPairs;

private:
	void ClearSPolygons();
	//实现
	virtual int Load(XMLIO::CINode& node, int flag) { int result = Nuclear::XPEnvSoundPolygonArea::Load(node, flag); ResetToolsPolygon(); return result;}
	virtual int Save(XMLIO::CONode& node, int flag) { return Nuclear::XPEnvSoundPolygonArea::Save(node, flag); }
	virtual void ResetAreaRegion(const POINT &mapSize);
	virtual void Move(const POINT& pt);
	virtual void SetTransition(int nTransition);
	virtual bool SetShape(const Nuclear::XPIShape *pShape);
	virtual void SetMaxVol(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMaxVol(typeID, soundID, v); }
	virtual void SetMinVol(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMinVol(typeID, soundID, v); }
	virtual void SetMaxFreq(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMaxFreq(typeID, soundID, v); }
	virtual void SetMinFreq(int typeID, int soundID, unsigned char v) { Nuclear::XPEnvSoundArea::SetMinFreq(typeID, soundID, v); }
	virtual void SetTime(int typeID, int soundID, int type, int time) { Nuclear::XPEnvSoundArea::SetTime(typeID, soundID, type, time); }
	virtual void SetLooping(int typeID, int soundID, bool loop)  { Nuclear::XPEnvSoundArea::SetLooping(typeID, soundID, loop); }
	virtual void SetSoundType(int typeID, int soundID, unsigned char type) { Nuclear::XPEnvSoundArea::SetSoundType(typeID, soundID, type); }
	virtual void SetMinInterval(int typeID, int nInterval) { Nuclear::XPEnvSoundArea::SetMinInterval(typeID, nInterval); }
	virtual void SetMaxInterval(int typeID, int nInterval) { Nuclear::XPEnvSoundArea::SetMaxInterval(typeID, nInterval); }
	virtual void SetPriority(int typeID, short priority) { Nuclear::XPEnvSoundArea::SetPriority(typeID, priority); }
	virtual int AddSound(int typeID, const std::wstring &name) { return Nuclear::XPEnvSoundArea::AddSound(typeID, name); }
	virtual void InsertSound(int typeID, int soundID, const Nuclear::XPEnvSoundArea::SoundParam &param) { return Nuclear::XPEnvSoundArea::InsertSound(typeID, soundID, param); }
	virtual void DeleteSound(int typeID, int soundID) { return Nuclear::XPEnvSoundArea::DeleteSound(typeID, soundID); }
	virtual int AddSoundType() { return Nuclear::XPEnvSoundArea::AddSoundType(); }
	virtual void InsertSoundType(int typeID, const Nuclear::XPEnvSoundArea::SoundTypeParam &param) { return Nuclear::XPEnvSoundArea::InsertSoundType(typeID, param); }
	virtual void DeleteSoundType(int typeID) { return Nuclear::XPEnvSoundArea::DeleteSoundType(typeID); }
	void ResetOutter();
	void ResetToolsPolygon();
	void ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys);
protected:
	virtual int LoadToolsData(XMLIO::CINode& node) { return LoadPolygonToolsData(node, m_ToolsCutPairs); }
	virtual int SaveToolsData(XMLIO::CONode& node) const { return SavePolygonToolsData(node, m_ToolsCutPairs); }

public:
//构造析构
	CToolsEnvSoundPolygonArea(void) : Nuclear::XPEnvSoundPolygonArea(NULL) {}
	virtual ~CToolsEnvSoundPolygonArea(void) {}

//方法们
	virtual void DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const;
	virtual void DrawArea(CDC* pDC, const CPoint& lt, float ratio) const;
	virtual Nuclear::CPOINT GetPos() const;
	virtual Nuclear::XPAREA_TYPE GetType() const { return Nuclear::XPEnvSoundPolygonArea::GetType(); }
	virtual int GetTransition() const { return m_nTransition; }
	virtual int GetTypeCount() const { return Nuclear::XPEnvSoundArea::GetTypeCount(); }
	virtual void GetSoundList(int typeID, std::vector<std::wstring> &slist) const { return Nuclear::XPEnvSoundArea::GetSoundList(typeID, slist);}
	virtual unsigned char GetMaxVol(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMaxVol(typeID, soundID); }
	virtual unsigned char GetMinVol(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMinVol(typeID, soundID); }
	virtual unsigned char GetMaxFreq(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMaxFreq(typeID, soundID); }
	virtual unsigned char GetMinFreq(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetMinFreq(typeID, soundID); }
	virtual int GetTime(int typeID, int soundID, int type) const  { return Nuclear::XPEnvSoundArea::GetTime(typeID, soundID, type); }
	virtual bool IsLooping(int typeID, int soundID) const  { return Nuclear::XPEnvSoundArea::IsLooping(typeID, soundID); }
	virtual unsigned char GetSoundType(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetSoundType(typeID, soundID); }
	virtual int GetMinInterval(int typeID) const { return Nuclear::XPEnvSoundArea::GetMinInterval(typeID); }
	virtual int GetMaxInterval(int typeID) const { return Nuclear::XPEnvSoundArea::GetMaxInterval(typeID); }
	virtual short GetPriority(int typeID) const { return Nuclear::XPEnvSoundArea::GetPriority(typeID); }
	virtual const Nuclear::XPEnvSoundArea::SoundParam& GetSoundParam(int typeID, int soundID) const { return Nuclear::XPEnvSoundArea::GetSoundParam(typeID, soundID); }
	virtual const Nuclear::XPEnvSoundArea::SoundTypeParam& GetSoundTypeParam(int typeID) const { return Nuclear::XPEnvSoundArea::GetSoundTypeParam(typeID); }
	virtual int CheckClickPoint(const POINT &pt) const;
	virtual int CheckClickLinePoint(const POINT &pt) const;
	virtual void GetShape(Nuclear::XPIShape* pShape) const;
	virtual CToolsEnvSoundArea* Clone() const;
	Nuclear::CPOINT GetPoint(int ptID) const { return m_innerPolygon.m_points[ptID]; }
};


