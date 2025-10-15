/** gaoyong change for pcmt
浏览器使用
*/
#include "OleContainer.h"
#include "exdispid.h"
//#include "cocos/base/allocator/CCA"

/*
==================
|   构造和析构   |
==================
*/
COleContainer::COleContainer(void):
_refNum(0),
//_rcWebWnd(0),
_bInPlaced(false),
_bExternalPlace(false),
_bCalledCanInPlace(false),
_bWebWndInited(false),
_pOleObj(NULL), 
_pInPlaceObj(NULL), 
_pStorage(NULL),
//m_pDispatch(NULL), // yeqing
m_strUrl("")
, m_bShowScroll(true)
{
	USE_DO
    ::memset( (PVOID)&_rcWebWnd,0,sizeof(_rcWebWnd));
    HRTEST_SE( OleInitialize(0),L"Ole初始化错误");
    HRTEST_SE( StgCreateDocfile(0,STGM_READWRITE | STGM_SHARE_EXCLUSIVE | STGM_DIRECT | STGM_CREATE,0,&_pStorage),L"StgCreateDocfile错误");
    HRTEST_SE( OleCreate(CLSID_WebBrowser,IID_IOleObject,OLERENDER_DRAW,0,this,_pStorage,(void**)&_pOleObj),L"Ole创建失败");
    HRTEST_SE( _pOleObj->QueryInterface(IID_IOleInPlaceObject,(LPVOID*)&_pInPlaceObj),L"OleInPlaceObject创建失败");
	USE_WIHLE

	//m_pDispatch = new CExtenalDispatch();//CC_NEW CExtenalDispatch; // yeqing
}

COleContainer::~COleContainer(void)
{
	if (_pOleObj)
	{
		_pOleObj->Release();
		_pOleObj= nullptr;
	}

	if (_pInPlaceObj)
	{
		_pInPlaceObj->Release();
		_pInPlaceObj= nullptr;
	}

	//CC_SAFE_DELETE(m_pDispatch); // yeqing
}

/*
==================
|IUnknown methods|
==================
*/
STDMETHODIMP COleContainer::QueryInterface(REFIID iid,void**ppvObject)
{
    *ppvObject = 0;
    if ( iid == IID_IOleClientSite )
        *ppvObject = (IOleClientSite*)this;
    if ( iid == IID_IUnknown )
        *ppvObject = this;
    if ( iid == IID_IDispatch )
        *ppvObject = (IDispatch*)this;
    if ( _bExternalPlace == false)
    {
        if ( iid == IID_IOleInPlaceSite )
            *ppvObject = (IOleInPlaceSite*)this;
        if ( iid == IID_IOleInPlaceFrame )
            *ppvObject = (IOleInPlaceFrame*)this;
        if ( iid == IID_IOleInPlaceUIWindow )
            *ppvObject = (IOleInPlaceUIWindow*)this;
    }

	if ( iid == DIID_DWebBrowserEvents2 )
			*ppvObject = (DWebBrowserEvents2 *)this;
	if ( iid == IID_IDocHostUIHandler)
		*ppvObject = (IDocHostUIHandler*)this;

    if ( *ppvObject )
    {
        AddRef();
        return S_OK;
    }
    return E_NOINTERFACE;
}

STDMETHODIMP_(ULONG)  COleContainer::AddRef()
{
    return ::InterlockedIncrement( &_refNum );
}

STDMETHODIMP_(ULONG)  COleContainer::Release()
{
    return ::InterlockedDecrement( &_refNum );
}

/*
=====================
| IDispatch Methods |
=====================
*/
HRESULT _stdcall COleContainer::GetTypeInfoCount(
    unsigned int * pctinfo) 
{
    return E_NOTIMPL;
}

HRESULT _stdcall COleContainer::GetTypeInfo(
    unsigned int iTInfo,
    LCID lcid,
    ITypeInfo FAR* FAR* ppTInfo) 
{
    return E_NOTIMPL;
}

HRESULT _stdcall COleContainer::GetIDsOfNames(REFIID riid, 
  OLECHAR FAR* FAR* rgszNames, 
  unsigned int cNames, 
  LCID lcid, 
  DISPID FAR* rgDispId )
{
    return E_NOTIMPL;
}

HRESULT _stdcall COleContainer::Invoke(
    DISPID dispIdMember,
    REFIID riid,
    LCID lcid,
    WORD wFlags,
    DISPPARAMS* pDispParams,
    VARIANT* pVarResult,
    EXCEPINFO* pExcepInfo,
    unsigned int* puArgErr)
{
	// DCOleContainerEvents
	if (dispIdMember == DISPID_DOCUMENTCOMPLETE)
	{
		DocumentComplete(pDispParams->rgvarg[1].pdispVal, pDispParams->rgvarg[0].pvarVal);
		return S_OK;
	}
	else if (dispIdMember == DISPID_BEFORENAVIGATE2)
	{
		BeforeNavigate2(pDispParams->rgvarg[6].pdispVal,
			pDispParams->rgvarg[5].pvarVal,
			pDispParams->rgvarg[4].pvarVal,
			pDispParams->rgvarg[3].pvarVal,
			pDispParams->rgvarg[2].pvarVal,
			pDispParams->rgvarg[1].pvarVal,
			pDispParams->rgvarg[0].pboolVal);
		return S_OK;
	}
    return E_NOTIMPL;
}

/*
========================
|IOleClientSite methods|
========================
*/
STDMETHODIMP COleContainer::SaveObject()
{
    return S_OK;
}

STDMETHODIMP COleContainer::GetMoniker(DWORD dwA,DWORD dwW,IMoniker**pm)
{
    *pm = 0;
    return E_NOTIMPL;
}

STDMETHODIMP COleContainer::GetContainer(IOleContainer**pc)
{
    *pc = 0;
    return E_FAIL;
}

STDMETHODIMP COleContainer::ShowObject()
{
    return S_OK;
}

STDMETHODIMP COleContainer::OnShowWindow(BOOL f)
{
    return S_OK;
}

STDMETHODIMP COleContainer::RequestNewObjectLayout()
{
    return S_OK;
}

/*
=========================
|IOleInPlaceSite methods|
=========================
*/
STDMETHODIMP COleContainer::GetWindow(HWND *p)
{
    *p = GetHWND();
    return S_OK;
}

STDMETHODIMP COleContainer::ContextSensitiveHelp(BOOL)
{
    return E_NOTIMPL;
}

STDMETHODIMP COleContainer::CanInPlaceActivate()//If this function return S_FALSE, AX cannot activate in place!
{
    if ( _bInPlaced )//Does COleContainer Control already in placed?
    {
        _bCalledCanInPlace = true;
        return S_OK;
    }
    return S_FALSE;
}

STDMETHODIMP COleContainer::OnInPlaceActivate()
{
    return S_OK;
}

STDMETHODIMP COleContainer::OnUIActivate()
{
    return S_OK;
}

STDMETHODIMP COleContainer::GetWindowContext(IOleInPlaceFrame** ppFrame,IOleInPlaceUIWindow **ppDoc,LPRECT r1,LPRECT r2,LPOLEINPLACEFRAMEINFO o)
{
    
    *ppFrame = (IOleInPlaceFrame*)this;
    AddRef();
    *ppDoc = NULL;

    ::GetClientRect(  GetHWND() ,&_rcWebWnd );
    *r1 = _rcWebWnd;
    *r2 = _rcWebWnd;

    o->cb = sizeof(OLEINPLACEFRAMEINFO);
    o->fMDIApp = false;
    o->hwndFrame = GetParent( GetHWND() );
    o->haccel = 0;
    o->cAccelEntries = 0;
    
    return S_OK;
}

STDMETHODIMP COleContainer::Scroll(SIZE s)
{
    return E_NOTIMPL;
}

STDMETHODIMP COleContainer::OnUIDeactivate(int)
{
    return S_OK;
}

STDMETHODIMP COleContainer::OnInPlaceDeactivate()
{
    return S_OK;
}

STDMETHODIMP COleContainer::DiscardUndoState()
{
    return S_OK;
}

STDMETHODIMP COleContainer::DeactivateAndUndo()
{
    return S_OK;
}

STDMETHODIMP COleContainer::OnPosRectChange(LPCRECT)
{
    return S_OK;
}

/*
==========================
|IOleInPlaceFrame methods|
==========================
*/
STDMETHODIMP COleContainer::GetBorder(LPRECT l)
{
    ::GetClientRect(  GetHWND() ,&_rcWebWnd );
    *l = _rcWebWnd;
    return S_OK;
}

STDMETHODIMP COleContainer::RequestBorderSpace(LPCBORDERWIDTHS b)
{
    return S_OK;
}

STDMETHODIMP COleContainer::SetBorderSpace(LPCBORDERWIDTHS b)
{
    return S_OK;
}

STDMETHODIMP COleContainer::SetActiveObject(IOleInPlaceActiveObject*pV,LPCOLESTR s)
{
    return S_OK;
}

STDMETHODIMP COleContainer::SetStatusText(LPCOLESTR t)
{
    return E_NOTIMPL;
}

STDMETHODIMP COleContainer::EnableModeless(BOOL f)
{
    return E_NOTIMPL;
}

STDMETHODIMP COleContainer::TranslateAccelerator(LPMSG,WORD)
{
    return E_NOTIMPL;
}

HRESULT _stdcall COleContainer::RemoveMenus(HMENU h)
{
    return E_NOTIMPL;
}

HRESULT _stdcall COleContainer::InsertMenus(HMENU h,LPOLEMENUGROUPWIDTHS x)
{
    return E_NOTIMPL;
}
HRESULT _stdcall COleContainer::SetMenu(HMENU h,HOLEMENU hO,HWND hw)
{
    return E_NOTIMPL;
}

/*
====================
|DCOleContainerEvents2|
====================
*/
void 
COleContainer::DocumentComplete( IDispatch *pDisp,VARIANT *URL)
{
    return;
}

void COleContainer::BeforeNavigate2(LPDISPATCH pDisp, VARIANT* URL,VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData,VARIANT* Headers, VARIANT_BOOL* Cancel)
{
	size_t st= wcslen(URL->bstrVal);
	char* szPath = new char[st+ 1];//CC_NEW char[st+ 1];
	memset(szPath, 0, st+ 1);
	WideCharToMultiByte(CP_ACP, 0, URL->bstrVal, wcslen(URL->bstrVal), szPath , MAX_PATH, NULL, NULL);
	szPath[st]= 0;
	m_strUrl= szPath;
	delete szPath;
	//CC_DELETE[] szPath;
}

/*
=====================
| IDocHostUIHandler |
=====================
*/
HRESULT COleContainer:: ShowContextMenu( 
	DWORD dwID,
	POINT *ppt,
	IUnknown *pcmdtReserved,
	IDispatch *pdispReserved)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: GetHostInfo( 
	DOCHOSTUIINFO *pInfo)
{
	if (pInfo != NULL)
	{
		pInfo->dwFlags |= DOCHOSTUIFLAG_NO3DBORDER;
		if (!m_bShowScroll)
			pInfo->dwFlags |= DOCHOSTUIFLAG_SCROLL_NO;
	}
	return S_OK;
}

HRESULT COleContainer:: ShowUI( 
	DWORD dwID,
	IOleInPlaceActiveObject *pActiveObject,
	IOleCommandTarget *pCommandTarget,
	IOleInPlaceFrame *pFrame,
	IOleInPlaceUIWindow *pDoc)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: HideUI( void)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: UpdateUI( void)
{
	return E_NOTIMPL;
}

//HRESULT WebBrowser:: EnableModeless( 
//  BOOL fEnable){return E_NOTIMPL;}

HRESULT COleContainer:: OnDocWindowActivate( 
	BOOL fActivate)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: OnFrameWindowActivate( 
	BOOL fActivate)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: ResizeBorder( 
	LPCRECT prcBorder,
	IOleInPlaceUIWindow *pUIWindow,
	BOOL fRameWindow)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: TranslateAccelerator( 
	LPMSG lpMsg,
	const GUID *pguidCmdGroup,
	DWORD nCmdID)
{
	HRESULT hr= S_FALSE;
	return hr;
}

HRESULT COleContainer:: GetOptionKeyPath( 
	LPOLESTR *pchKey,
	DWORD dw)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: GetDropTarget( 
	IDropTarget *pDropTarget,
	IDropTarget **ppDropTarget)
{
	return E_NOTIMPL;//使用默认拖拽
}

HRESULT COleContainer:: GetExternal( IDispatch **ppDispatch)
{
	// yeqing
	//m_pDispatch->AddRef();
	//*ppDispatch = m_pDispatch;
	return S_OK;
}

HRESULT COleContainer:: TranslateUrl( 
	DWORD dwTranslate,
	OLECHAR *pchURLIn,
	OLECHAR **ppchURLOut)
{
	return E_NOTIMPL;
}

HRESULT COleContainer:: FilterDataObject( 
	IDataObject *pDO,
	IDataObject **ppDORet)
{
	return E_NOTIMPL;
}
/* end */
