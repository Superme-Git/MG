#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CTimerNotifyMap; }

class GXWINDOW_EXPORTS_DLL CComponentNotify
{
public:
	CComponentNotify();
	virtual ~CComponentNotify();
private:
	CComponentNotify( const CComponentNotify& src);
	CComponentNotify& operator=( const CComponentNotify& src);
public:
	bool IsSameNotify( const CComponentNotify& cn) const;
	bool IsSameNotify( const CComponentNotify* cn) const;
};

class CTimerID;
typedef CTimerID TIMERID;

class GXWINDOW_EXPORTS_DLL CTimerNotify : public CComponentNotify
{
	friend class CTimer;
public:
	CTimerNotify();
	virtual ~CTimerNotify();
protected:
	virtual void OnTimer( const TIMERID& nid) = 0;
};

class GXWINDOW_EXPORTS_DLL CTimer : public CUncopyableObject
{
	friend GXWINDOW_EXPORTS_DLL void GXTimerUpdate();
	static unsigned int	s_uiCurrentTime;

	INLINECLASS::CTimerNotifyMap*	m_map;
private:
	CTimer();
	static CTimer			g_Instance;
public:
	virtual ~CTimer();
public:
	//
	// 新创建Timer或ResetTimer
	// 当tid为-1时创建Timer,若tid为已经创建的TimerID,则执行ResetTimer
	//
	static void NewTimer( CTimerNotify& tn, unsigned int nDelay, TIMERID& tid);

	//
	// 重置Timer
	//
	static void ResetTimer( CTimerNotify& tn, const TIMERID& tid, unsigned int nDelay);

	//
	// 删除已创建的Timer
	//
	static void CancelTimer( const TIMERID& nid);

	//
	// 返回最近一次GXTimerUpdate的时间值(毫秒)
	//
	static unsigned int GetCurrentTime();
private:
	void DoCheckTimer();
};

class GXWINDOW_EXPORTS_DLL CTimerID : public CUncopyableObject
{
	friend CTimer;

	__int64		m_id;
public:
	CTimerID();
	~CTimerID();
public:
	void CancelTimer();
	void ResetTimer( CTimerNotify& tn, unsigned int nDelay);
	void NewTimer( CTimerNotify& tn, unsigned int nDelay);
public:
	bool operator==( const CTimerID& src) const;
public:
	bool IsNull() const;
	__int64 GetID() const;
	void ResetID( __int64 nid = -1);
};

} // namespace GXWINDOW { 


