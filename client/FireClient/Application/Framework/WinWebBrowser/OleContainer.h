/** gaoyong change for pcmt
浏览器使用
*/
#pragma once

#ifdef _WIN32
#include <WinSock2.h>
#include <Windows.h>
#endif

#include <oleidl.h>
#include <ExDisp.h>
#include <MsHTML.h>
#include <string>
//#include "ExtenalDispatch.h" // yeqing
#include "cocos2d.h"

#include <MsHtmHst.h>

#define USE_DO_WIHLE
#ifdef USE_DO_WIHLE
#define USE_DO \
	do\
{

#define DO_BREAK(isBreak) \
	if(isBreak)\
	break;

#define HRTEST_SE(isBreak, outputStr)\
	if (FAILED(isBreak))\
	{\
	OutputDebugStringW(outputStr);\
	break;\
	}

#define NULLTEST_SE(isBreak, outputStr)\
	if (!(isBreak))\
	{\
	OutputDebugStringW(outputStr);\
	break;\
	}

#define NULLTEST(isBreak)\
	if (!(isBreak))\
	break;

#define USE_WIHLE \
} while (0);

#endif // USE_DO_WIHLE


class /*CC_DLL*/ COleContainer :
	public IDispatch,
	public IOleClientSite,
	public IOleInPlaceSite,
	public IOleInPlaceFrame,
	public IDocHostUIHandler
{
public:
	COleContainer();
	~COleContainer();

	// IUnknown methods
	virtual STDMETHODIMP QueryInterface(REFIID iid,void**ppvObject);
	virtual STDMETHODIMP_(ULONG) AddRef();
	virtual STDMETHODIMP_(ULONG) Release();

	// IDispatch Methods
	HRESULT _stdcall GetTypeInfoCount(unsigned int * pctinfo);
	HRESULT _stdcall GetTypeInfo(unsigned int iTInfo,LCID lcid,ITypeInfo FAR* FAR* ppTInfo);
	HRESULT _stdcall GetIDsOfNames(REFIID riid,OLECHAR FAR* FAR* rgszNames,unsigned int cNames,LCID lcid,DISPID FAR* rgDispId);
	HRESULT _stdcall Invoke(DISPID dispIdMember,REFIID riid,LCID lcid,WORD wFlags,DISPPARAMS FAR* pDispParams,VARIANT FAR* pVarResult,EXCEPINFO FAR* pExcepInfo,unsigned int FAR* puArgErr);


	// IOleClientSite methods
	virtual STDMETHODIMP SaveObject();
	virtual STDMETHODIMP GetMoniker(DWORD dwA,DWORD dwW,IMoniker**pm);
	virtual STDMETHODIMP GetContainer(IOleContainer**pc);
	virtual STDMETHODIMP ShowObject();
	virtual STDMETHODIMP OnShowWindow(BOOL f);
	virtual STDMETHODIMP RequestNewObjectLayout();


	// IOleInPlaceSite methods
	virtual STDMETHODIMP GetWindow(HWND *p);
	virtual STDMETHODIMP ContextSensitiveHelp(BOOL);
	virtual STDMETHODIMP CanInPlaceActivate();
	virtual STDMETHODIMP OnInPlaceActivate();
	virtual STDMETHODIMP OnUIActivate();
	virtual STDMETHODIMP GetWindowContext(IOleInPlaceFrame** ppFrame,IOleInPlaceUIWindow **ppDoc,LPRECT r1,LPRECT r2,LPOLEINPLACEFRAMEINFO o);
	virtual STDMETHODIMP Scroll(SIZE s);
	virtual STDMETHODIMP OnUIDeactivate(int);
	virtual STDMETHODIMP OnInPlaceDeactivate();
	virtual STDMETHODIMP DiscardUndoState();
	virtual STDMETHODIMP DeactivateAndUndo();
	virtual STDMETHODIMP OnPosRectChange(LPCRECT);

	// IOleInPlaceFrame methods
	virtual STDMETHODIMP GetBorder(LPRECT l);
	virtual STDMETHODIMP RequestBorderSpace(LPCBORDERWIDTHS);
	virtual STDMETHODIMP SetBorderSpace(LPCBORDERWIDTHS w);
	virtual STDMETHODIMP SetActiveObject(IOleInPlaceActiveObject*pV,LPCOLESTR s);
	virtual STDMETHODIMP InsertMenus(HMENU h,LPOLEMENUGROUPWIDTHS x);
	virtual STDMETHODIMP SetMenu(HMENU h,HOLEMENU hO,HWND hw);
	virtual STDMETHODIMP RemoveMenus(HMENU h);
	virtual STDMETHODIMP SetStatusText(LPCOLESTR t);
	virtual STDMETHODIMP EnableModeless(BOOL f);
	virtual STDMETHODIMP TranslateAccelerator(LPMSG,WORD);

	//IDocHostUIHandler
	virtual HRESULT STDMETHODCALLTYPE ShowContextMenu( 
		DWORD dwID,
		POINT *ppt,
		IUnknown *pcmdtReserved,
		IDispatch *pdispReserved);
	virtual HRESULT STDMETHODCALLTYPE GetHostInfo( 
		DOCHOSTUIINFO *pInfo);
	virtual HRESULT STDMETHODCALLTYPE ShowUI( 
		DWORD dwID,
		IOleInPlaceActiveObject *pActiveObject,
		IOleCommandTarget *pCommandTarget,
		IOleInPlaceFrame *pFrame,
		IOleInPlaceUIWindow *pDoc);
	virtual HRESULT STDMETHODCALLTYPE HideUI( void);
	virtual HRESULT STDMETHODCALLTYPE UpdateUI( void);
	//IOleInPlaceFrame已经包含了这个方法 , IOleInPlaceFrame接口在WebBrowser中实现,所以不必要再次实现
	//virtual HRESULT STDMETHODCALLTYPE EnableModeless( 
	//   BOOL fEnable);
	virtual HRESULT STDMETHODCALLTYPE OnDocWindowActivate( 
		BOOL fActivate);
	virtual HRESULT STDMETHODCALLTYPE OnFrameWindowActivate( 
		BOOL fActivate);
	virtual HRESULT STDMETHODCALLTYPE ResizeBorder( 
		LPCRECT prcBorder,
		IOleInPlaceUIWindow *pUIWindow,
		BOOL fRameWindow);
	virtual HRESULT STDMETHODCALLTYPE TranslateAccelerator( 
		LPMSG lpMsg,
		const GUID *pguidCmdGroup,
		DWORD nCmdID);
	virtual HRESULT STDMETHODCALLTYPE GetOptionKeyPath( 
		LPOLESTR *pchKey,
		DWORD dw);
	virtual HRESULT STDMETHODCALLTYPE GetDropTarget( 
		IDropTarget *pDropTarget,
		IDropTarget **ppDropTarget);
	virtual HRESULT STDMETHODCALLTYPE GetExternal(
		IDispatch **ppDispatch);
	virtual HRESULT STDMETHODCALLTYPE TranslateUrl( 
		DWORD dwTranslate,
		OLECHAR *pchURLIn,
		OLECHAR **ppchURLOut);
	virtual HRESULT STDMETHODCALLTYPE FilterDataObject( 
		IDataObject *pDO,
		IDataObject **ppDORet);

	//IDocHostUIHandler
	virtual STDMETHODIMP_(void) DocumentComplete( IDispatch *pDisp,VARIANT *URL);//其实是: virtual void __stdcall DocumentComplete(...)
	virtual void BeforeNavigate2(LPDISPATCH pDisp, VARIANT* URL,VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData,VARIANT* Headers, VARIANT_BOOL* Cancel);

	//CExtenalDispatch* getExtenalDispatch(){return m_pDispatch;} // yeqing
protected:
	virtual HWND GetHWND(){return NULL;};//继承的类应该实现这个方法,告诉WebBrowser,到底用哪一个HWND放置WebBrowser
	inline IOleObject* _GetOleObject(){return _pOleObj;};
	inline IOleInPlaceObject* _GetInPlaceObject(){return _pInPlaceObj;};
	// 内部数据
protected:
	long   _refNum;
	//CExtenalDispatch * m_pDispatch; // yeqing
	RECT  _rcWebWnd;
	bool  _bInPlaced;
	bool  _bExternalPlace;
	bool  _bCalledCanInPlace;
	bool  _bWebWndInited;

	//指针
	IOleObject*                 _pOleObj; 
	IOleInPlaceObject*          _pInPlaceObj;
	IStorage*                   _pStorage;

	std::string					m_strUrl;
	bool						m_bShowScroll;
};
/* end */
