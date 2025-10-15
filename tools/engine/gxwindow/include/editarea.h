#pragma once

namespace GXWINDOW { 

namespace INLINECLASS {	

class CAnsiString;
class CEditAreaChangedNotifySet;
class CEditAreaChangingNotifySet;

} // namespace INLINECLASS {	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

class CEditAreaLimitQuery : virtual public GXWINDOW::CComponentNotify
{
	friend class CEditArea;
public:
	CEditAreaLimitQuery() {};
	virtual ~CEditAreaLimitQuery() {};
protected:
	// insert 是目前需要加入的字符串
	// 返回值是 insert中可以加入的个数
	virtual int QueryEditAreaLimit( GXWINDOW::CComponent& c, const wchar_t* insert, bool bRaw) = 0;
};

class CEditAreaChangedNotify : virtual public GXWINDOW::CComponentNotify
{
	friend class CEditArea;
public:
	CEditAreaChangedNotify() {};
	virtual ~CEditAreaChangedNotify() {};
protected:
	virtual void OnEditAreaChanged( GXWINDOW::CComponent& c) = 0;
};

class CEditAreaKeyInputHook : virtual public GXWINDOW::CComponentNotify
{
	friend class CEditArea;
public:
	CEditAreaKeyInputHook() {};
	virtual ~CEditAreaKeyInputHook() {};
protected:
	virtual bool OnEditAreaKeyInputHook( UINT uMsg, WPARAM wParam, LPARAM lParam, GXWINDOW::CComponent& c) = 0;
};

class GXWINDOW_EXPORTS_DLL CEditArea : public CTextArea
{
	INLINECLASS::CSimpleString*	m_strbuffer;
	INLINECLASS::CEditAreaChangedNotifySet* m_ceacs;
	CEditAreaLimitQuery*					m_LimitQuery;
	CEditAreaKeyInputHook*					m_InputHook;
	TIMERID		m_timerInput;
	int			m_nLimit;
	bool		m_bNumber;
	bool		m_bDefaultFocus;
	bool		m_bPastFromClipboard; /// 是否支持系统剪贴板粘贴
public:
	CEditArea( CAbstarctTextDecoder& decoder = CFormatTextDecoder());
	virtual ~CEditArea();
protected:
	virtual bool OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam);
	virtual void OnTimer( const TIMERID& nid);
	virtual void OnComponentShow();
public:
	virtual void AppendText( const wchar_t* szText);
	virtual void AppendText( const wchar_t* szText, int backgroundEffectId);
	virtual void AppendRawText( const wchar_t* szText);

	virtual void RemoveSelect();

	// virtual bool Encode( CAbstarctTextEncoder& fte);
public:
	bool CutToClipboard();
	bool PastFromClipboard();

	virtual void CharBackSpaceFromCursor();
	virtual void CharDeleteFromCursor();

	virtual void SetLimit( int n);
	virtual int GetLimit() const;

	virtual void SetNumber( bool b = true);
	virtual bool IsNumber() const;

	virtual void SetDefaultFocus( bool b = true);
	virtual bool IsDefaultFocus() const;

	virtual void EnablePastFromClipboard( bool b = true);
	virtual bool IsEnablePastFromClipboard() const;
private:
	void CheckInputTimer();
	void InsertBufferData();
public:
	void EditAreaLimitQueryAdd( CEditAreaLimitQuery& lq);
	void EditAreaLimitQueryRemove();

	void EditAreaChangedNotifyAdd( CEditAreaChangedNotify& meacn);
	void EditAreaChangedNotifyRemove( CEditAreaChangedNotify& meacn);

	void EditAreaKeyInputHookAdd( CEditAreaKeyInputHook& eakih);
	void EditAreaKeyInputHookRemove();
};

class GXWINDOW_EXPORTS_DLL CCombinationEditAreaAllocator : public CUncopyableObject
{
	CAbstarctTextDecoder*	m_Decoder;
public:
	CCombinationEditAreaAllocator( CAbstarctTextDecoder& decoder = CFormatTextDecoder());
	virtual ~CCombinationEditAreaAllocator();
public:
	virtual CScrollPanel* NewScrollPanel();
	virtual CEditArea* NewEditArea();
	virtual void DeleteComponent( CComponent* pc);
protected:
	CAbstarctTextDecoder& GetTextDecoder();
public:
	static CCombinationEditAreaAllocator& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CCombinationEditArea : public CUncopyableObject
{
	CCombinationEditAreaAllocator*	m_alloc;

	CScrollPanel*	m_sp;
	CEditArea*		m_ea;
public:
	CCombinationEditArea( CCombinationEditAreaAllocator& alloc = CCombinationEditAreaAllocator::GetInstance());
	virtual ~CCombinationEditArea();
public:
	CScrollPanel& GetScrollPanel();
	CEditArea& GetEditArea();
public:
	virtual void SetSize( int w, int h);
	virtual void SetCombinationSize( int w, int h);	// 避开ea的限制设置整个CombinationEditArea控件的大小(包括sp和ea)
	virtual void SetPos( int x, int y);
	virtual void ShowCursor( ImageParams& ip);
	virtual void AppendText( const wchar_t* szText);
	virtual void SetSelectAllow( bool b = true);
	virtual void SetSingleLine( bool b = true);
	virtual void SetViewOnly( bool bViewOnly = true);
	virtual bool IsViewOnly() const;
public:
	operator CComponent*();
	operator CComponent&();
	CComponent* operator&();

	void SetComponentName( const wchar_t* name);
	const wchar_t* GetComponentName() const;
};

class GXWINDOW_EXPORTS_DLL CCombinationSingleLineEditArea : public CCombinationEditArea
{
public:
	CCombinationSingleLineEditArea( CCombinationEditAreaAllocator& alloc = CCombinationEditAreaAllocator::GetInstance());
	virtual ~CCombinationSingleLineEditArea();
public:
	virtual void SetSize( int w, int h);
};

} // namespace GXWINDOW { 

