#pragma once
#include "afxext.h"
#include "Resource.h"
#include "afxcmn.h"
#include "afxwin.h"

#include "..\engine\particlesystem\ParticleSystem.h"

#include "..\engine\particlesystem\SpecialParticleSystem.h"
#include "..\engine\particlesystem\GeneralParticleSystem.h"

#include "DlgBasicEditorParam.h"
#include "DlgEmitterParam.h"
#include "DlgParticleParam.h"
#include "DlgSpecialParticleParam.h"

class CParticleParameterView : public CFormView
{
	DECLARE_DYNCREATE(CParticleParameterView)

protected:
	CParticleParameterView(); // protected constructor used by dynamic creation
	virtual ~CParticleParameterView();

public:
	enum { IDD = IDD_VIEW_PARTICLEPARAMETER };

public:
	void GetParameterPropertyVector(std::vector<PARAMETERPROPERTY>& paramPropertyVector);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

	virtual void OnInitialUpdate();

private:
	void InitCtrl(void);
	void SetCtrlPos(void);
	void FindPslTextures(const CString& pslPath);

public:
	DWORD GetBKColor() { return m_dwBKcolor; }
	void OnApplyEffectFile(LPCWSTR filePath); //CDlgSpecialParticleParam中会用到

public:
	Nuclear::CParticleSystem* GetParticleSys() { return m_pParticleSys; }
	void SetParticleSys(Nuclear::CParticleSystem* pParticleSys, Nuclear::ParticleSystemHandle handle)
	{
		m_pParticleSys = pParticleSys;
		m_ppsl = pParticleSys->GetPSL(); 
		m_hCurrentPShandle = handle;
		SetCtrlPos();
	}

	afx_msg void OnBnClickedButtonReplay();
	void OnSetStopFlag(bool bStop);

	afx_msg void OnBnClickedButtonSetshapetexture();

	Nuclear::ParticleSystemHandle GetCurrentPShandle() { return m_hCurrentPShandle; }

	std::wstring GetCurPSLName() { return m_strCurPSLName; }
	
	const std::vector<int>& GetVectorKeyPointIndex() { return m_vectorKeyPointIndex; }

private:
	int m_nScollFresh;
	std::vector<int> m_vectorKeyPointIndex; //关键帧路径的关键点索引

private:
	Nuclear::CParticleSystem* m_pParticleSys;
	Nuclear::XHardRef<Nuclear::PSL> m_ppsl;
	Nuclear::PSParameter m_psParameter;

	Nuclear::ParticleSystemHandle m_hCurrentPShandle;
	std::vector<std::wstring> m_vectorTextureName; //多纹理支持..

	int m_nHScrollPos;
	int m_nVScrollPos;	
	DWORD m_dwBKcolor;

	std::wstring m_strResPath; //指定访问资源的路径... WorkPath+L"res\\";
	std::wstring m_strSysPath; //指定访问资源的路径... WorkPath+L"res\\effect\\particle\\";
	std::wstring m_strCurPSLName; //用来保存修改用 ---相对路径

	std::wstring m_strBKpicFilePath; //完整路径名
	CBitmap m_bmpTexture; //绘制在设置旋转中心区域的图片...

private:
	std::vector<Nuclear::PATHPOINT> m_vectorPathPoint;
	std::vector<POINT> m_vectorFullPathPoint;
	bool m_bStop; //按o键盘暂停
	bool m_bSetShapeTexture; //设置了纹理之后才能做拆分--保存--加载等工作

private:
	int m_nVertexRow; //0-----40 //转场特效的前景拆分（四边形和三角形）
	int m_nVertexCol; //0-----40
	int m_nVertexType; //自定义、四边形、三角形、2pic--把图分成两块各自沿着分割线相向运动
	int m_nLineAngle;

public:
	BOOL OpenParticleSys(LPCTSTR lpszPathName);
	BOOL SaveParticleSys(LPCTSTR lpszPathName);

	int GetLineAngle() { return m_nLineAngle; }

	afx_msg void OnBnClickedButtonOpenparticlesys();
	afx_msg void OnBnClickedButtonSavemodifypsl();
	afx_msg void OnBnClickedButtonSaveparticlesys();

private:
	afx_msg void OnOpenPsl();
	afx_msg void OnSavePsl();
	afx_msg void OnSaveasPsl();
	afx_msg void OnBnClickedButtonLoadtexture();

	afx_msg void OnBnClickedButtonResetparticlesys();
	afx_msg void OnBnClickedButtonAllPSLtoPTC();
	void FindPSL(std::wstring RootPathName, std::wstring PathName);
	
	afx_msg void OnBnClickedButtonSample1();
	afx_msg void OnBnClickedButtonSample2();
	afx_msg void OnBnClickedButtonSample3();
	afx_msg void OnBnClickedButtonSample4();
	afx_msg void OnBnClickedButtonSample5();
	afx_msg void OnBnClickedButtonSample6();
	afx_msg void OnBnClickedButtonSample7();
	afx_msg void OnBnClickedButtonSample8();
	afx_msg void OnBnClickedButtonSample9();
	afx_msg void OnBnClickedButtonSample10();

	afx_msg void OnClose();
	afx_msg void OnBnClickedButtonSetbkcolor();
	afx_msg void OnBnClickedButtonSetcustompath();

	afx_msg void OnBnClickedButtonTestmanager();
	afx_msg void OnBnClickedButtonSetbkpic();

	afx_msg void OnBnClickedButtonpsparamcolor();
		
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);

	afx_msg void OnBnClickedButtonFindpsltextures();
	afx_msg void OnBnClickedButtonLoadvertex();
	afx_msg void OnBnClickedButtonSavevertex();
	Nuclear::PSL LoadPSL(LPCWSTR strFilename);

	afx_msg void OnBnClickedButtonmnpictest();//大图片切分成小图片---(美术切素材用的小工具)
	afx_msg void OnBnClickedButtonPrintpsani();
	afx_msg void OnBnClickedButtonStrtani();
	afx_msg void OnBnClickedButtonEndani();

	afx_msg void OnBnClickedButtonFromtexturelistfindpsl();

	int m_nFramePerSecond;
	int m_nRadioPrintScreenArea;
	Nuclear::XPIMAGE_FILEFORMAT m_nFileFormat;
	CString m_strFilePath;//保存的文件夹，具体文件名通过粒子名加帧序号得到
	Nuclear::CRECT m_RctPrintFile;

	afx_msg void OnBnClickedButtonPslfileinfo();
	void CollectPSLInfo(CString pslPath); //收集粒子信息..
	void CollectPSLUseTheTextureList(std::wstring pslPath, const std::vector<std::wstring>& TextureFileList);

	int m_nMaxParticles; //统计最大粒子数超过这个数的粒子文件
	BOOL m_bMaxParticles;
	BOOL m_bFullScreen;
	BOOL m_bSpecialPS;
	BOOL m_bMultTexture;
	BOOL m_bCustomPath;
	BOOL m_bAlphaADD;

public:
	CTabCtrl m_tabParticleParam;
	CDlgBasicEditorParam m_dlgEditorParam;
	CDlgEmitterParam m_dlgEmitterParam;
	CDlgParticleParam m_dlgParticleParam;
	CDlgSpecialParticleParam m_dlgSpecialParticleParam;

	void ProcessMsg(MSG* pMsg);

	virtual BOOL PreTranslateMessage(MSG* pMsg);

	afx_msg void OnTcnSelchangeTabParticleparams(NMHDR *pNMHDR, LRESULT *pResult);

public:
	void SetParticleSysCycle(bool bCycle);
	void SetEmitterangle(float fEmitterSpinAngle);
	void UpdateBaseCtrlState(bool bMoveMap, bool bSetParticleHoleFlag, bool bSetEmitterSpinCentFlag);
	void DrawParticleTexture(CDC* pDC, CRect rctSetRotatePos);

	bool Openparamfile();
	bool Saveparamfile();
};
