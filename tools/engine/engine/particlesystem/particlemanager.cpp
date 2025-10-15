#include "stdafx.h"
#include "particlemanager.h"
#include "commonfuncs.h"
#include <Shlwapi.h>
//#include <float.h>

#include "..\common\log.h"
#include "..\common\pfsutil.h"
#include "..\common\xmarshal.h"
#include "..\common\fileiomanager.h"

#include "SpecialParticleSystem.h"
#include "GeneralParticleSystem.h"

namespace Nuclear
{
	DWORD MHZX_PSLOG::s_nProcessId = 0;

	class ParticleSysResourceReadTask : public CFileIOManager::AsyncReadTask
	{
		ParticleManager* m_pPSManager;
		int m_tick;
		std::wstring m_strTextureFileName; //从粒子文件中来的相对路径文件名
	public:
		ParticleSysResourceReadTask(const std::wstring filename, const std::wstring strTextureFileName, CFileIOManager *pFileIOMan, ParticleManager* pPSManager)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0)
		{
			m_pPSManager = pPSManager;
			m_strTextureFileName = strTextureFileName;
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			int tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load PSresource failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load PSresource failed!, path is %s\n", m_filename.c_str());
				m_pPSManager->NotifyLoadingUrl(m_strTextureFileName, false);
			} else {
				m_pFileIOMan->OnReady(tick-m_tick, m_data.size());
				XPTRACE(L"load PSresource ok!, path is %s\n", m_filename.c_str());
				m_pPSManager->SaveAsyncReadTextureResource(m_data, m_strTextureFileName);
				m_pPSManager->NotifyLoadingUrl(m_strTextureFileName, true);
			}
			delete this;
		}

		virtual void OnDiscard()
		{
			delete this;
		}

		virtual ~ParticleSysResourceReadTask() { }
	};
	
	std::wstring ParticleManager::m_logName = L"";

	ParticleManager::ParticleManager()
	{
		m_pRenderer = NULL;
		m_pFileIOMan = NULL;
		m_psHandleSeed = 1;
		m_nLastError = PSLLOADERROR_SUCC;
		m_nComputerConfig = XPCOMPUTERCONFIG_H;
		m_fCurResourceSize = 0.0f;
		
		m_nBitsPixel = 32;

		m_ViewPort = CRECT(0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));

		MHZX_PSLOG::s_nProcessId = GetCurrentProcessId();
	}

	ParticleManager::~ParticleManager()
	{
		Destroy();
	}

	ParticleManager::ParticleManager(IBaseRenderer *pRenderer, IFileIO* pFileIOMan, bool bAutoGCFlag)
	{
		m_pRenderer = pRenderer;
		XDisplayMode dismode = m_pRenderer->GetDisplayMode();
		m_nBitsPixel = dismode.bitperpix;

		m_pFileIOMan = pFileIOMan;
		m_psHandleSeed = 1;
		m_nLastError = PSLLOADERROR_SUCC;
		m_nComputerConfig = XPCOMPUTERCONFIG_H;
		m_fCurResourceSize = 0.0f;

		m_ViewPort = CRECT(0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));

		m_fMaxSize = 10.0f; //10M
		m_fLastTime = 3.0f; //3分钟
		m_bAutoGCFlag = bAutoGCFlag;
		m_fgcTime = 0.0f;

		//降低浮点运算精度提高计算速度
//		_control87(_PC_24, _MCW_PC);
//		_control87(_DN_FLUSH, _MCW_DN);
	}

	ParticleSystemHandle ParticleManager::CreateParticleSystem(const std::wstring& pslNameSrc, const PSParameter& param, bool bLoadRes)
	{
		std::wstring pslName;
		std::wstring wsTmp;
		wsTmp = FNExt_ParticleWS;
		if (pslNameSrc.length() < 5 || pslNameSrc.substr(pslNameSrc.length() - 4) != L"." + wsTmp)
		{
			pslName = pslNameSrc + L"." + FNExt_ParticleWS;
		}
		else
		{
			pslName = pslNameSrc;
		}

		std::map<std::wstring, XHardRef<PSL>*>::const_iterator it = m_mapPSL.find(pslName);
		
		XHardRef<PSL>* pRefPsl = NULL;

		if( m_mapPSL.end() == it)
		{//load Psl 
			PSL* ppsl = new PSL();
			bool bres = LoadPSLFromFile2(pslName, ppsl);
			if(!bres)
			{
				delete ppsl;
				m_nLastError = PSLLOADERROR_FILE;
				if( !(m_logName.empty()) )
					XPLOG_PSMHZX(L"ParticleManager::CreateParticleSystem()-- LoadPSLFromFile()-FAILED, fileName: %s\n\n", pslName.c_str());
				return INVALID_PS_HANDLE;
			}

			pRefPsl = new XHardRef<PSL>(ppsl);
			m_mapPSL.insert( std::make_pair(pslName, pRefPsl) );
			m_fCurResourceSize += (float)sizeof(PSL)/(float)(1024*1024);
		}
		else
		{
			pRefPsl = it->second;
		}

		//读路径... 路径数据也可以后加载....
		std::vector<POINT> vectorFullPathPoint;
		std::wstring strPathFileName;

		if((*pRefPsl)->m_nPosType == PSLTYPE_CUSTOMPATH)
		{
			strPathFileName = (*pRefPsl)->m_strFullPathFile;
			std::map<std::wstring, std::vector<POINT>>::const_iterator iter = m_mapPath.find(strPathFileName);
			if( m_mapPath.end() == iter)
			{//load path 
				bool bres = LoadPathFromFile(pRefPsl->operator->(), vectorFullPathPoint);
				if(!bres)
				{
					m_nLastError = PSLLOADERROR_PATH;
					if( !(m_logName.empty()) )
						XPLOG_PSMHZX(L"ParticleManager::CreateParticleSystem()-- LoadPathFromFile()-FAILED, fileName: %s\n", strPathFileName.c_str());
					return INVALID_PS_HANDLE;
				}
				m_mapPath.insert( std::make_pair(strPathFileName, vectorFullPathPoint) );

				int nTotalSize = (int)(vectorFullPathPoint.size()) *sizeof(POINT);
				m_fCurResourceSize += (float)nTotalSize/(float)(1024*1024);
			}
			else
			{
				vectorFullPathPoint = iter->second;
			}

			TransformPoint(vectorFullPathPoint, (*pRefPsl)->m_fPathZoom, (*pRefPsl)->m_fPathSpin, (*pRefPsl)->m_nPathMirrorType);
		}

		PSParameter paramTemp = param;
		if((*pRefPsl)->m_nMoveType == PSLMOVETYPE_FULLSCREEN)
		{
			paramTemp.m_bScreenCoord = false;
			paramTemp.m_bVertexRHWflag = true;
		}
		if( ( !paramTemp.m_bScreenCoord ) && (paramTemp.m_bVertexRHWflag) )	//m_bVertexRHWflag等于false的情况由d3d来做以下坐标转换	
			AdjustPSParameter(paramTemp.m_psModifyParam);
		
		if((*pRefPsl)->m_nInterfacePsl) //界面特效
			paramTemp.m_psModifyParam.m_pointCent += (*pRefPsl)->m_ptInterfacialCenter; //加相对界面的偏移值

		if(paramTemp.m_bModifyLineEmitter)
		{
			paramTemp.m_psModifyParam.m_fpoint0 = paramTemp.m_ptLineEmitterStrt;
			paramTemp.m_psModifyParam.m_fpoint1 = paramTemp.m_ptLineEmitterEnd;

			paramTemp.m_psModifyParam.m_pointCent = (paramTemp.m_ptLineEmitterStrt + paramTemp.m_ptLineEmitterEnd)/2.0f;
		}

		CParticleSystem* pParticleSys = NULL;
		if( (*pRefPsl)->m_nAbsrtactPsl == 1 )
			pParticleSys = new CSpecialParticleSystem(*pRefPsl, paramTemp, m_pRenderer, pslName);
		else
		{
			pParticleSys = new CGeneralParticleSystem(*pRefPsl, paramTemp, m_pRenderer, vectorFullPathPoint, pslName);
		}

		pParticleSys->m_pointCent0 = param.m_psModifyParam.m_pointCent;

		m_mapParticleSystem.insert( std::make_pair(m_psHandleSeed, pParticleSys) );
		if(bLoadRes)
		{
			if(EnsureParticleSystemResouce(m_psHandleSeed) != XPEARR_RESREADY)
			{
				m_mapParticleSystem.erase(m_psHandleSeed);
				delete pParticleSys;
				return INVALID_PS_HANDLE;
			}
		}
		
		//
		if((*pRefPsl)->m_nAbsrtactPsl == 1)
		{
			SHAPE_LIST shapelist;
			std::wstring strShapeListPath = (*pRefPsl)->m_strVtxsObjectPath;
			std::map<std::wstring, SHAPE_LIST>::const_iterator iter = m_mapShapeList.find(strShapeListPath);
			if( m_mapShapeList.end() == iter)
			{//load shapeData 
				bool bres = LoadShapeListFromFile(strShapeListPath, shapelist);
				if(!bres)
				{
					m_nLastError = PSLLOADERROR_SHAPELIST;
					if( !(m_logName.empty()) )
						XPLOG_PSMHZX(L"ParticleManager::CreateParticleSystem()-- LoadShapeListFromFile()-FAILED, fileName: %s\n", strShapeListPath.c_str());

					m_mapParticleSystem.erase(m_psHandleSeed);
					delete pParticleSys;
					return INVALID_PS_HANDLE;
				}
				m_mapShapeList.insert( std::make_pair(strShapeListPath, shapelist) );

				//在这里假设平均一个shape占用5个顶点的空间
				int nTotalSize = (int)(shapelist.size()) *5 *sizeof(Vertex);
				m_fCurResourceSize += (float)nTotalSize/(float)(1024*1024);
			}
			else
			{
				shapelist = iter->second;
			}
			SetSpecialPsVertexs(m_psHandleSeed, shapelist);
		}

		if( !(m_logName.empty()) )
		{
			XPLOG_PSMHZX(L"-----------------------------------------------\n");
			std::wstring str;
			str = paramTemp.m_bVertexRHWflag ? L"---Create: m_bVertexRHWflag: true\n" : L"---Create: m_bVertexRHWflag: false\n";
			str = pslName +str;
			XPLOG_PSMHZX(str.c_str());
		}

		return m_psHandleSeed++;
	}

	bool ParticleManager::GenPSTextureInfo(std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture, PSTEXTUREINFOSTRUCT* ppslTexture)
	{
		if(m_pRenderer==NULL)
			return false;
		
		int cnt = (int)vectorD3DTexture.size();
		if(cnt<1 || cnt>32)
			return false;
		PictureInfo picinfo;
		if( !m_pRenderer->GetPictureInfo(vectorD3DTexture[0]->handle, picinfo) )
			return false;

		int nWidth = picinfo.m_nPicWidth;
		int nHeight = picinfo.m_nPicHeight;
		
		int nFlipIndex = 0;
		for(int i=0; i<6; i++)
		{
			int maxCntPow2Min = 1<<i;
			if(maxCntPow2Min>=cnt)
			{
				nFlipIndex = i;
				break;
			}
		}
		//列大于等于行...
		//纹理数据按行优先填充
		int nCol = 0;
		int nRow = 0;
		if(nFlipIndex%2) //奇数
		{
			nCol = 1<<((nFlipIndex/2)+1);
			nRow = 1<<(nFlipIndex/2);
		}
		else
		{
			nCol = 1<<(nFlipIndex/2);
			nRow = 1<<(nFlipIndex/2);
		}

		XDisplayMode xpMode = m_pRenderer->GetDisplayMode();
		bool bmode16 = (xpMode.bitperpix == 16);

		PictureHandle newhandle = m_pRenderer->NewPicture(nCol*nWidth, nRow*nHeight, NULL, bmode16?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8);

		if( newhandle == INVALID_PICTURE_HANDLE ) return false;
		ppslTexture->PsTextureInfo.handle = newhandle;
		ppslTexture->dataSize = (float)(nCol*nWidth * nRow*nHeight *32)/(float)(1024*1024);
		if(bmode16)
		{
			ppslTexture->dataSize = (float)(nCol*nWidth * nRow*nHeight *16)/(float)(1024*1024);
		}

		ppslTexture->PsTextureInfo.m_nTexWidth = nWidth;
		ppslTexture->PsTextureInfo.m_nTexHeight = nHeight;
		ppslTexture->PsTextureInfo.m_nTexNum = cnt;
		ppslTexture->PsTextureInfo.m_nPslTexCol = nCol;
		ppslTexture->PsTextureInfo.m_nPslTexRow = nRow;

		CRECT srcrect(0, 0, nWidth, nHeight);
		int buffersize = nWidth*nHeight*4;
		if(bmode16)
		{
			buffersize = nWidth*nHeight*2;
		}
		void *buffer = malloc(buffersize);
		if( buffer == NULL ) return false;
		for(int i=0; i<cnt; i++)
		{
			m_pRenderer->GetPictureData(vectorD3DTexture[i]->handle, buffer, buffersize, &srcrect);
			int row = i/ppslTexture->PsTextureInfo.m_nPslTexCol;
			int col = i%ppslTexture->PsTextureInfo.m_nPslTexCol;
			CRECT dstrect(col*nWidth,row*nHeight,(col+1)*nWidth,(row+1)*nHeight);
			m_pRenderer->SetPictureData(newhandle, buffer, buffersize, &dstrect);
		}
		free(buffer);
		return true;
	}

	bool ParticleManager::LoadShapeListFromFile(const std::wstring& fileName, SHAPE_LIST& shapelist)
	{
		PSVertexsObject vertexObj;

		XBuffer data;
		std::wstring newname;
		bool bres = false;
		newname = fileName;
		std::transform( newname.begin(), newname.end(), newname.begin(), ::towlower);

		std::wstring subStr = L"\\";
		std::wstring ReplaceStr = L"/";
		std::wstring inStr = newname;

		newname = replace( inStr, subStr, ReplaceStr);

		newname =  m_strPath + L"vertex/" + newname;
		bres = m_pFileIOMan->GetFileImage(newname, data);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadShapeListFromFile()--ReadPFSFile failed, fileName: %s\n", newname.c_str());
			return false;
		}

		bres = vertexObj.LoadFromMem(data);
		if(!bres)
			return false;

		shapelist = vertexObj.GetShapeList();

		return true;
	}

	void ParticleManager::AdjustPSParameter(PSModifyParam& param)
	{
		//世界坐标转为屏幕坐标
		//已经是屏幕坐标了的特效不会流进这里，比如目前的界面特效	
		//绘制的时候就是需要世界坐标的也不会流进这里.. 比如梦诛中的飞行特效等情况...
		param.m_pointCent.x = param.m_pointCent.x -m_ViewPort.left;
		param.m_pointCent.y = param.m_pointCent.y -m_ViewPort.top;
		param.m_pointObject.x = param.m_pointObject.x -m_ViewPort.left;
		param.m_pointObject.y = param.m_pointObject.y -m_ViewPort.top;

	}

	bool ParticleManager::LoadPathFromFile(PSL* ppsl, std::vector<POINT>& vectorFullPathPoint)
	{
		CPslPathObject PslPathObject;

		XBuffer data;
		std::wstring newname;
		bool bres = false;
		newname = ppsl->m_strFullPathFile;
		std::transform( newname.begin(), newname.end(), newname.begin(), ::towlower);

		std::wstring subStr = L"\\";
		std::wstring ReplaceStr = L"/";
		std::wstring inStr = newname;

		newname = replace( inStr, subStr, ReplaceStr);

		newname =  m_strPath + L"path/" + newname;
		bres = m_pFileIOMan->GetFileImage(newname, data);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPathFromFile()--ReadPFSFile failed, fileName: %s\n", newname.c_str());
			return false;
		}

		bres = PslPathObject.LoadFromMem(data);
		if(!bres)
			return false;

		BOOL bVectorCharPath = PslPathObject.GetVectorCharPointFlag();
		BOOL bCurveSample = PslPathObject.GetCurveSampleFlag();
		if(bVectorCharPath)
		{
			vectorFullPathPoint = PslPathObject.GetFullPathPointVector();
		}
		else
		{
			std::vector<PATHPOINT> vectorPathPoint = PslPathObject.GetVectorPathPoint();

			//PATHPOINT转换为point---自定义路径点解码
			int cnt = (int)vectorPathPoint.size();
			if(bCurveSample)
			{
				for(int i=0; i<cnt; i++)
					vectorFullPathPoint.push_back(vectorPathPoint[i].pt);
			}
			else
			{
				if(cnt==1)
				{
					vectorFullPathPoint.push_back(vectorPathPoint[0].pt);
				}
				for(int i=0; (i<cnt-1)&&(cnt>1); i++)
				{
					POINT pt0 = vectorPathPoint[i].pt;
					POINT pt1 = vectorPathPoint[i].next_ControlPt;
					POINT pt2 = vectorPathPoint[i +1].pre_ControlPt;
					POINT pt3 = vectorPathPoint[i +1].pt;
					float dis = ComputerDistance(pt0, pt3);

					float step = 3.0f/dis; //点的疏密控制...以后要改变精度就直接修改这里就行了
					int nStep = (int)(dis/3.0f);
					if(ppsl->m_nNumPointFullPath == -1)
					{ //8.0f永远不要改
						step = 8.0f/dis;
						nStep = (int)(dis/8.0f);
					}

					for(int j=0; j<nStep; j++)
					{
						float u = j*step;
						POINT point;
						point = Computer3BezierCurvePoint(u, pt0, pt1, pt2, pt3);
						vectorFullPathPoint.push_back(point);
					}
					if(nStep*step < 1.0f)
						vectorFullPathPoint.push_back(pt3);
				}

				if(ppsl->m_nNumPointFullPath == -1)
				{
				}
				else
				{
					cnt = (int)vectorFullPathPoint.size();
					float coef = (float)cnt/(float)(ppsl->m_nNumPointFullPath);

					ppsl->m_nEmitterSpeed = (int)(ppsl->m_nEmitterSpeed *coef);
					if(ppsl->m_nMotionToPath == 1)
					{
						ppsl->m_fSpeedMin *= coef;
						ppsl->m_fSpeedMax *= coef;
					}

					ppsl->m_nNumPointFullPath = cnt;
				}
			}
		}

		return true;
	}

	bool ParticleManager::LoadTextureFromFile(const std::wstring& strTextureFileName, PSPICHANDLESTRUCT* picHandleStruct)
	{
		//处理文件夹问题
		XBuffer data;
		std::wstring newname;
		newname = strTextureFileName;

		bool bres = false;
		bres = m_pFileIOMan->GetFileImage(newname, data);
		if(!bres)
		{
			XPLOG_ERROR(L"ParticleManager::LoadTextureFromFile()--GetFileImage failed, fileName: %s\n", newname.c_str());
			return bres;
		}

		picHandleStruct->handle = m_pRenderer->LoadPictureFromMem(data.constbegin(), static_cast<int>(data.size()));
		if( picHandleStruct->handle == INVALID_PICTURE_HANDLE )
			return false;
		picHandleStruct->dataSize = (float)(data.size())/(float)(1024*1024);
		return true;
	}

	bool ParticleManager::SaveAsyncReadTextureResource(const XBuffer& data, std::wstring strTextureFileName)
	{
		if (m_mapD3d9Texture.find(strTextureFileName) != m_mapD3d9Texture.end())
		{
			return true;
		}
		PSPICHANDLESTRUCT* PicHandle = new PSPICHANDLESTRUCT(m_pRenderer);

		PicHandle->handle = m_pRenderer->LoadPictureFromMem(data.constbegin(), static_cast<int>(data.size()));
		if( PicHandle->handle == INVALID_PICTURE_HANDLE )
		{
			delete PicHandle;
			PicHandle = NULL;
			return false;
		}
		PicHandle->dataSize = (float)(data.size())/(float)(1024*1024);

		PicHandle->dwTime = GetTickCount()/1000;

		XHardRef<PSPICHANDLESTRUCT>* RefHandle = new XHardRef<PSPICHANDLESTRUCT>(PicHandle);
		m_mapD3d9Texture.insert( std::make_pair(strTextureFileName, RefHandle) );

		m_fCurResourceSize += PicHandle->dataSize;

		return true;
	}

	bool ParticleManager::LoadPSLFromFile(const std::wstring& fileName, PSL* ppsl)
	{
		//m_pFileIOMan
		XBuffer data;
		std::wstring newname;
		bool bres = false;
		newname = fileName;
		std::transform( newname.begin(), newname.end(), newname.begin(), ::towlower);

		std::wstring subStr = L"\\";
		std::wstring ReplaceStr = L"/";
		std::wstring inStr = newname;

		newname = replace( inStr, subStr, ReplaceStr);
		newname =  m_strPath + L"psl/" + newname;

		bres = m_pFileIOMan->GetFileImage(newname, data);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile()--ReadPFSFile failed, fileName: %s\n", newname.c_str());
			return false;
		}
		//

		bres = LoadPSLFromMem(data, ppsl);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile()--LoadPSLFromMem failed, fileName: %s\n", newname.c_str());
			return false;
		}
		return true;
	}

	bool ParticleManager::LoadPSLFromMem(const XBuffer &data, PSL* ppsl)
	{
		XIStream os(data);
		TCHAR* pBuffer = NULL;
		int nSize = (int)os.size();
		pBuffer = reinterpret_cast<TCHAR*>(XAlloc(nSize));
		os.pop_byte(reinterpret_cast<char*>(pBuffer), nSize);

		//分析pBuffer 分行读出数据...
		TCHAR buf[256];
		TCHAR leftside[256];
		TCHAR rightside[256];
		std::map<std::wstring, std::wstring> valuemap;

		// 第一个非空行...必须是版本..[ParticleSystem 1.00] or [ParticleSystem 2.00]
		int nVer = -1;
		int nFirstRow = 0;
		memset(buf, 0, sizeof(TCHAR)*256);
		int j = 0;
		int count = 0;
		for(int i=0; ( i<(int)(nSize/sizeof(TCHAR)) ) && (count<1000); i++)
		{
			TCHAR ch = *(pBuffer+i);
			if(ch != '\n')
				buf[j++] = ch;
			else
			{
				//一行读完了..
				if( wcslen(buf) >= 2 )
				{//丢弃空行..
					nFirstRow++;
					if(nFirstRow==1) //第一个非空行确定版本号
					{
						if( wcsstr(buf, L"[ParticleSystem 1.00]") != NULL )
						{
							if( !(m_logName.empty()) )
								XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--版本1.00 \n");
							nVer = 1;
						}
						else if( wcsstr(buf, L"[ParticleSystem 2.00]") != NULL )
						{
							if( !(m_logName.empty()) )
								XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--版本2.00 \n"); //3个加速度的改变...
							nVer = 2;
						}
						else if( wcsstr(buf, L"[ParticleSystem 3.00]") != NULL )
						{ //RandAngle为true时恢复SpinRand的作用
							if( !(m_logName.empty()) )
								XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--版本3.00 \n");
							nVer = 3;
						}
						else if( wcsstr(buf, L"[ParticleSystem 4.00]") != NULL )
						{ //RandAngle为true时恢复SpinRand的作用
							if( !(m_logName.empty()) )
								XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--版本4.00 \n");
							nVer = 4;
						}
						else
						{
							if( !(m_logName.empty()) )
								XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--版本不可识别\n");
							return false;
						}
					}

					ppsl->m_nVersion = nVer;

					memset(leftside, 0, sizeof(leftside));
					memset(rightside, 0, sizeof(rightside));

					PluckFirstField(buf, leftside, sizeof(leftside), L"=");
					PluckFirstField(buf, rightside, sizeof(rightside), L"\n");

					//是否重复...
					if(valuemap.find(leftside) != valuemap.end())
					{
						XFree(pBuffer);
						if( !(m_logName.empty()) )
							XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--参数重复\n");
						return false;
					}

					//空格...
					LPCTSTR pszTrimChars = L" ";
					BOOL bRes = StrTrim(leftside, pszTrimChars);
					bRes = StrTrim(rightside, pszTrimChars);
					valuemap.insert(std::make_pair(std::wstring(leftside), std::wstring(rightside)));

				}

				j = 0;
				memset(buf, 0, sizeof(TCHAR)*256);
				count++;
			}
		}
		if(count>1000) //粒子文件不可能那么大..
		{
			XFree(pBuffer);
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromMem()--文件行数大于 %d\n", count);
			return false;
		}
		XFree(pBuffer);

		ReadProperty(valuemap, L"nAbsrtactPsl", ppsl->m_nAbsrtactPsl);

		WCHAR strVtxsObjectPath[256];
		if( ppsl->m_nAbsrtactPsl==1 )
		{
			memset(strVtxsObjectPath, 0, 256*sizeof(WCHAR));
			ReadProperty(valuemap, L"strVtxsObjectPath", strVtxsObjectPath);
			ppsl->m_strVtxsObjectPath = strVtxsObjectPath;
		}

		ReadProperty(valuemap, L"nDirMotion", ppsl->m_nDirMotion);
		ReadProperty(valuemap, L"nPSConfigReq", ppsl->m_nPSConfigReq);
		ReadProperty(valuemap, L"bPathOrder", ppsl->m_nPathOrder);
		ReadProperty(valuemap, L"bUVmirrorh", ppsl->m_nUVmirrorh);
		ReadProperty(valuemap, L"bUVmirrorv", ppsl->m_nUVmirrorv);
		ReadProperty(valuemap, L"bParticleColorChange", ppsl->m_nParticleColorChange);
		ReadProperty(valuemap, L"bParticleColorRand", ppsl->m_nParticleColorRand);
		ReadProperty(valuemap, L"bTrackFx", ppsl->m_nTrackFx);

		ReadProperty(valuemap, L"nMoveType", ppsl->m_nMoveType);
		ReadProperty(valuemap, L"nPosType", ppsl->m_nPosType);
		ReadProperty(valuemap, L"nParticlePathInitPosType", ppsl->m_nParticlePathInitPosType);
		ReadProperty(valuemap, L"nParticleSizeRandType", ppsl->m_nParticleSizeRandType);

		ReadProperty(valuemap, L"nSysPosRadiusX", ppsl->m_nSysPosRadiusX);
		ReadProperty(valuemap, L"nSysPosRadiusY", ppsl->m_nSysPosRadiusY);

		ReadProperty(valuemap, L"nRctLeft", ppsl->m_rct.left);
		ReadProperty(valuemap, L"nRctTop", ppsl->m_rct.top);
		ReadProperty(valuemap, L"nRctRight", ppsl->m_rct.right);
		ReadProperty(valuemap, L"nRctBottom", ppsl->m_rct.bottom);

		WCHAR pTexturePath[256];
		if(ppsl->m_nPosType==PSLTYPE_CUSTOMPATH)
		{
			memset(pTexturePath, 0, 256*sizeof(WCHAR));
			ReadProperty(valuemap, L"strFullPathFile", pTexturePath);
			ppsl->m_strFullPathFile = pTexturePath;
		}

		ReadProperty(valuemap, L"bEmitterType", ppsl->m_nEmitterType);
		ReadProperty(valuemap, L"bSingleParticleAni", ppsl->m_nSingleParticleAni);
		ReadProperty(valuemap, L"bParticleAniTextureIndex1", ppsl->m_nParticleAniTextureIndex1);
		ReadProperty(valuemap, L"nAniPlaySpeed", ppsl->m_nAniPlaySpeed);
		ReadProperty(valuemap, L"fAniSpeedRand", ppsl->m_fAniSpeedRand);
		ReadProperty(valuemap, L"bParFillMode", ppsl->m_nParFillMode);
		ReadProperty(valuemap, L"fPointX0", ppsl->m_point0.x);
		ReadProperty(valuemap, L"fPointY0", ppsl->m_point0.y);
		ReadProperty(valuemap, L"fPointX1", ppsl->m_point1.x);
		ReadProperty(valuemap, L"fPointY1", ppsl->m_point1.y);
		ReadProperty(valuemap, L"pointEmitterSpinCentX", ppsl->m_pointEmitterSpinCent.x);
		ReadProperty(valuemap, L"pointEmitterSpinCentY", ppsl->m_pointEmitterSpinCent.y);
		ReadProperty(valuemap, L"pointParticleHoleX", ppsl->m_pointParticleHole.x);
		ReadProperty(valuemap, L"pointParticleHoleY", ppsl->m_pointParticleHole.y);

		ReadProperty(valuemap, L"nSysMaxParticles", ppsl->m_nSysMaxParticles);

		ReadProperty(valuemap, L"fPathZoom", ppsl->m_fPathZoom);
		ReadProperty(valuemap, L"fPathSpin", ppsl->m_fPathSpin);
		ReadProperty(valuemap, L"nPathMirrorType", ppsl->m_nPathMirrorType);
		ReadProperty(valuemap, L"nNumPointFullPath", ppsl->m_nNumPointFullPath);

		ReadProperty(valuemap, L"fSysLife", ppsl->m_fSysLife);
		ReadProperty(valuemap, L"nEmissionRate", ppsl->m_nEmissionRate);
		ReadProperty(valuemap, L"nEmitterSpeed", ppsl->m_nEmitterSpeed);
		ReadProperty(valuemap, L"fParticleLifeMin", ppsl->m_fParticleLifeMin);
		ReadProperty(valuemap, L"fParticleLifeMax", ppsl->m_fParticleLifeMax);
		ReadProperty(valuemap, L"fEmissionDirection", ppsl->m_fEmissionDirection);
		ReadProperty(valuemap, L"fEmissionSpread", ppsl->m_fEmissionSpread);
		ReadProperty(valuemap, L"fSpeedMin", ppsl->m_fSpeedMin);
		ReadProperty(valuemap, L"fSpeedMax", ppsl->m_fSpeedMax);

		if(nVer==1)
		{
			float fMin = 0.0f;
			float fMax = 0.0f;
			ReadProperty(valuemap, L"fGravityMin", fMin);
			ReadProperty(valuemap, L"fGravityMax", fMax);
			ppsl->m_fGravity = (fMin+fMax)/2.0f;
			ppsl->m_fGravityRand = 0.0f;

			ReadProperty(valuemap, L"fRadialAccMin", fMin);
			ReadProperty(valuemap, L"fRadialAccMax", fMax);
			ppsl->m_fRadialAcc = (fMin+fMax)/2.0f;
			ppsl->m_fRadialAccRand = 0.0f;

			ReadProperty(valuemap, L"fTangAccMin", fMin);
			ReadProperty(valuemap, L"fTangAccMax", fMax);
			ppsl->m_fTangAcc = (fMin+fMax)/2.0f;
			ppsl->m_fTangAccRand = 0.0f;
		}
		else
		{
			ReadProperty(valuemap, L"fGravity", ppsl->m_fGravity);
			ReadProperty(valuemap, L"fGravityRand", ppsl->m_fGravityRand);
			ReadProperty(valuemap, L"fRadialAcc", ppsl->m_fRadialAcc);
			ReadProperty(valuemap, L"fRadialAccRand", ppsl->m_fRadialAccRand);
			ReadProperty(valuemap, L"fTangAcc", ppsl->m_fTangAcc);
			ReadProperty(valuemap, L"fTangAccRand", ppsl->m_fTangAccRand);
		}

		ReadProperty(valuemap, L"fGravity", ppsl->m_fGravity);
		ReadProperty(valuemap, L"fGravityRand", ppsl->m_fGravityRand);
		ReadProperty(valuemap, L"fRadialAcc", ppsl->m_fRadialAcc);
		ReadProperty(valuemap, L"fRadialAccRand", ppsl->m_fRadialAccRand);
		ReadProperty(valuemap, L"fTangAcc", ppsl->m_fTangAcc);
		ReadProperty(valuemap, L"fTangAccRand", ppsl->m_fTangAccRand);
		ReadProperty(valuemap, L"fParSizeStrtX", ppsl->m_fParSizeStrtX);
		ReadProperty(valuemap, L"fParSizeStrtY", ppsl->m_fParSizeStrtY);
		ReadProperty(valuemap, L"fParSizeEndX", ppsl->m_fParSizeEndX);
		ReadProperty(valuemap, L"fParSizeEndY", ppsl->m_fParSizeEndY);
		ReadProperty(valuemap, L"fParSizeVar", ppsl->m_fParSizeVar);
		ReadProperty(valuemap, L"fParSpinStrt", ppsl->m_fParSpinStrt);
		ReadProperty(valuemap, L"fParSpinEnd", ppsl->m_fParSpinEnd);
		ReadProperty(valuemap, L"fParSpinVar", ppsl->m_fParSpinVar);

		ReadProperty(valuemap, L"fRotateCentX", ppsl->m_fRotateCentX);
		ReadProperty(valuemap, L"fRotateCentY", ppsl->m_fRotateCentY);

		ReadProperty(valuemap, L"fParAngleStrt", ppsl->m_fParAngleStrt);
		ReadProperty(valuemap, L"fParAngleEnd", ppsl->m_fParAngleEnd);
		ReadProperty(valuemap, L"fParAngleVar", ppsl->m_fParAngleVar);

		ReadProperty(valuemap, L"nParAlphaStrt", ppsl->m_nParAlphaStrt);
		ReadProperty(valuemap, L"nParAlphaEnd", ppsl->m_nParAlphaEnd);
		ReadProperty(valuemap, L"fParAlphaVar", ppsl->m_fParAlphaVar);
		ReadProperty(valuemap, L"nParAlphaMid", ppsl->m_nParAlphaMid);
		ReadProperty(valuemap, L"fParAlphaMidTime", ppsl->m_fParAlphaMidTime);

		ReadProperty(valuemap, L"fSizeRandom", ppsl->m_fSizeRandom);
		ReadProperty(valuemap, L"fSpinRandom", ppsl->m_fSpinRandom);
		ReadProperty(valuemap, L"fAngleRandom", ppsl->m_fAngleRandom);
		ReadProperty(valuemap, L"fAlignToMotionAngle", ppsl->m_fAlignToMotionAngle);
		ReadProperty(valuemap, L"nAlignToMotion", ppsl->m_nAlignToMotion);
		ReadProperty(valuemap, L"nAlignToPath", ppsl->m_nAlignToPath);
		ReadProperty(valuemap, L"nMotionToPath", ppsl->m_nMotionToPath);
		ReadProperty(valuemap, L"nAlignToEmitter", ppsl->m_nAlignToEmitter);
		ReadProperty(valuemap, L"nAlignToHole", ppsl->m_nAlignToHole);
		ReadProperty(valuemap, L"nParticleRandAngle", ppsl->m_nParticleRandAngle);

		if(nVer<3 && ppsl->m_nParticleRandAngle==1)
		{
			ppsl->m_fSpinRandom = 0.0f;
		}
		ReadProperty(valuemap, L"nParticleRandAngleSelf", ppsl->m_nParticleRandAngleSelf);

		ReadProperty(valuemap, L"nParColorStrtR", ppsl->m_nParColorStrtR);
		ReadProperty(valuemap, L"nParColorStrtG", ppsl->m_nParColorStrtG);
		ReadProperty(valuemap, L"nParColorStrtB", ppsl->m_nParColorStrtB);
		ReadProperty(valuemap, L"nParColorEndR", ppsl->m_nParColorEndR);
		ReadProperty(valuemap, L"nParColorEndG", ppsl->m_nParColorEndG);
		ReadProperty(valuemap, L"nParColorEndB", ppsl->m_nParColorEndB);
		ReadProperty(valuemap, L"fParColorVar", ppsl->m_fParColorVar);
		ReadProperty(valuemap, L"fPSLzoom", ppsl->m_fPSLzoom);
		ReadProperty(valuemap, L"fEmitterSpin", ppsl->m_fEmitterSpin);

		ReadProperty(valuemap, L"nDestBlendMode", ppsl->m_nDestBlendMode);
		ReadBlendModeProperty(valuemap, L"BlendMode", ppsl->m_nBlendMode);

		int nTextureNum = 0;
		ReadProperty(valuemap, L"TextureNum", nTextureNum);

		ppsl->m_vectorTexturePath.clear();
		//读纹理数组...
		std::wstring str;
		WCHAR lpstr[3]; //最多支持100个纹理 0-99

		for(int i=0; i<nTextureNum; i++)
		{
			memset(lpstr, 0, 3*sizeof(WCHAR));
			_itow_s(i, lpstr, 3, 10); //*sizeof(WCHAR)
			str = (std::wstring)(L"Texture") + (std::wstring)lpstr;
			memset(pTexturePath, 0, 256*sizeof(WCHAR));
			ReadProperty(valuemap, str.c_str(), pTexturePath);
			std::wstring  tmpStr =  pTexturePath;
			tmpStr = L"/effect/particle/texture/" + tmpStr;
			ppsl->m_vectorTexturePath.push_back(tmpStr);
		}

		if(nVer<4)
		{
			ppsl->m_nRowNum = 1;
			ppsl->m_nColNum = 1;
			ppsl->m_nTextureNum = 1;
		}
		else
		{
			ReadProperty(valuemap, L"nRowNum", ppsl->m_nRowNum);
			ReadProperty(valuemap, L"nColNum", ppsl->m_nColNum);
			ReadProperty(valuemap, L"nTextureNum", ppsl->m_nTextureNum);
		}

		ReadProperty(valuemap, L"rctMaxBoxL", ppsl->m_rctMaxBox.left);
		ReadProperty(valuemap, L"rctMaxBoxT", ppsl->m_rctMaxBox.top);
		ReadProperty(valuemap, L"rctMaxBoxR", ppsl->m_rctMaxBox.right);
		ReadProperty(valuemap, L"rctMaxBoxB", ppsl->m_rctMaxBox.bottom);

		ReadProperty(valuemap, L"nInterfacePsl", ppsl->m_nInterfacePsl);
		ReadProperty(valuemap, L"ptInterfacialCenterX", ppsl->m_ptInterfacialCenter.x);
		ReadProperty(valuemap, L"ptInterfacialCenterY", ppsl->m_ptInterfacialCenter.y);

		ReadProperty(valuemap, L"nSkillEffectSpritePoint", ppsl->m_nSkillEffectSpritePoint);

		return true;
	}

	bool ParticleManager::LoadPSLFromFile2(const std::wstring& fileName, PSL* ppsl)
	{
		//m_pFileIOMan
		XBuffer data;
		std::wstring newname;
		bool bres = false;
		newname = fileName;
		std::transform( newname.begin(), newname.end(), newname.begin(), ::towlower);

		std::wstring subStr = L"\\";
		std::wstring ReplaceStr = L"/";
		std::wstring inStr = newname;

		newname = replace( inStr, subStr, ReplaceStr);
		newname =  m_strPath + L"psl/" + newname;

		bres = m_pFileIOMan->GetFileImage(newname, data);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile2()--GetFileImage failed, fileName: %s\n", newname.c_str());
			return false;
		}
		//

		bres = LoadPSLFromMemEx(data, ppsl);
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile2()--LoadPSLFromMemEx failed, fileName: %s\n", newname.c_str());
			return false;
		}
		return true;
	}

	bool ParticleManager::LoadPSLFromMemEx(const XBuffer &data, PSL* ppsl)
	{
		XIStream FS(data);
		std::string TmpStr;
		ReadData2(FS, ppsl->m_nVersion);
		ReadData2(FS, ppsl->m_nAbsrtactPsl);//WriteProperty(file, L"nAbsrtactPsl", m_ppsl->m_nAbsrtactPsl);
		ReadData2(FS, TmpStr);//WriteProperty(file, L"strVtxsObjectPath", m_ppsl->m_strVtxsObjectPath.c_str());
		ppsl->m_strVtxsObjectPath = s2ws(TmpStr);
		ReadData2(FS, ppsl->m_nDirMotion);//WriteProperty(file, L"nDirMotion", m_ppsl->m_nDirMotion);
		ReadData2(FS, ppsl->m_nPSConfigReq);//WriteProperty(file, L"nPSConfigReq", m_ppsl->m_nPSConfigReq);
		ReadData2(FS, ppsl->m_nPathOrder);//WriteProperty(file, L"bPathOrder", m_ppsl->m_nPathOrder);
		ReadData2(FS, ppsl->m_nUVmirrorh);//WriteProperty(file, L"bUVmirrorh", m_ppsl->m_nUVmirrorh);
		ReadData2(FS, ppsl->m_nUVmirrorv);//WriteProperty(file, L"bUVmirrorv", m_ppsl->m_nUVmirrorv);
		ReadData2(FS, ppsl->m_nParticleColorChange);//WriteProperty(file, L"bParticleColorChange", m_ppsl->m_nParticleColorChange);
		ReadData2(FS, ppsl->m_nParticleColorRand);//WriteProperty(file, L"bParticleColorRand", m_ppsl->m_nParticleColorRand);
		ReadData2(FS, ppsl->m_nTrackFx);//WriteProperty(file, L"bTrackFx", m_ppsl->m_nTrackFx);
		ReadData2(FS, ppsl->m_nEmitterType);//WriteProperty(file, L"bEmitterType", m_ppsl->m_nEmitterType);
		ReadData2(FS, ppsl->m_nSingleParticleAni);//WriteProperty(file, L"bSingleParticleAni", m_ppsl->m_nSingleParticleAni);
		ReadData2(FS, ppsl->m_nParticleAniTextureIndex1);//WriteProperty(file, L"bParticleAniTextureIndex1", m_ppsl->m_nParticleAniTextureIndex1);
		ReadData2(FS, ppsl->m_nAniPlaySpeed);//WriteProperty(file, L"nAniPlaySpeed", m_ppsl->m_nAniPlaySpeed);
		ReadData2(FS, ppsl->m_fAniSpeedRand);//WriteProperty(file, L"fAniSpeedRand", m_ppsl->m_fAniSpeedRand);
		ReadData2(FS, ppsl->m_nParFillMode);//WriteProperty(file, L"bParFillMode", m_ppsl->m_nParFillMode);
		ReadData2(FS, ppsl->m_nPosType);//WriteProperty(file, L"nPosType", m_ppsl->m_nPosType);
		if (ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)
		{
			ReadData2(FS, TmpStr);//WriteProperty( file, L"strFullPathFile", m_ppsl->m_strFullPathFile.c_str());
			ppsl->m_strFullPathFile = s2ws(TmpStr);
		}
		ReadData2(FS, ppsl->m_nParticlePathInitPosType);//WriteProperty(file, L"nParticlePathInitPosType", ppsl->m_nParticlePathInitPosType);
		ReadData2(FS, ppsl->m_nParticleSizeRandType);//WriteProperty(file, L"nParticleSizeRandType", ppsl->m_nParticleSizeRandType);
		ReadData2(FS, ppsl->m_nMoveType);//WriteProperty(file, L"nMoveType", ppsl->m_nMoveType);
		ReadData2(FS, ppsl->m_point0.x);//WriteProperty(file, L"fPointX0", ppsl->m_point0.x);
		ReadData2(FS, ppsl->m_point0.y);//WriteProperty(file, L"fPointY0", ppsl->m_point0.y);
		ReadData2(FS, ppsl->m_point1.x);//WriteProperty(file, L"fPointX1", ppsl->m_point1.x);
		ReadData2(FS, ppsl->m_point1.y);//WriteProperty(file, L"fPointY1", ppsl->m_point1.y);
		ReadData2(FS, ppsl->m_pointEmitterSpinCent.x);//WriteProperty(file, L"pointEmitterSpinCentX", ppsl->m_pointEmitterSpinCent.x);
		ReadData2(FS, ppsl->m_pointEmitterSpinCent.y);//WriteProperty(file, L"pointEmitterSpinCentY", ppsl->m_pointEmitterSpinCent.y);
		ReadData2(FS, ppsl->m_pointParticleHole.x);//WriteProperty(file, L"pointParticleHoleX", ppsl->m_pointParticleHole.x);
		ReadData2(FS, ppsl->m_pointParticleHole.y);//WriteProperty(file, L"pointParticleHoleY", ppsl->m_pointParticleHole.y);
		ReadData2(FS, ppsl->m_nSysPosRadiusX);//WriteProperty(file, L"nSysPosRadiusX", ppsl->m_nSysPosRadiusX);
		ReadData2(FS, ppsl->m_nSysPosRadiusY);//WriteProperty(file, L"nSysPosRadiusY", ppsl->m_nSysPosRadiusY);
		ReadData2(FS, (int&)(ppsl->m_rct.left));//WriteProperty(file, L"nRctLeft", ppsl->m_rct.left);
		ReadData2(FS, (int&)(ppsl->m_rct.top));//WriteProperty(file, L"nRctTop", ppsl->m_rct.top);
		ReadData2(FS, (int&)(ppsl->m_rct.right));//WriteProperty(file, L"nRctRight", ppsl->m_rct.right);
		ReadData2(FS, (int&)(ppsl->m_rct.bottom));//WriteProperty(file, L"nRctBottom", ppsl->m_rct.bottom);
		ReadData2(FS, ppsl->m_nSysMaxParticles);//WriteProperty(file, L"nSysMaxParticles", ppsl->m_nSysMaxParticles);
		ReadData2(FS, ppsl->m_fPathZoom);//WriteProperty(file, L"fPathZoom", ppsl->m_fPathZoom);
		ReadData2(FS, ppsl->m_fPathSpin);//WriteProperty(file, L"fPathSpin", ppsl->m_fPathSpin);
		ReadData2(FS, ppsl->m_nPathMirrorType);//WriteProperty(file, L"nPathMirrorType", ppsl->m_nPathMirrorType);
		ReadData2(FS, ppsl->m_nNumPointFullPath);//WriteProperty(file, L"nNumPointFullPath", ppsl->m_nNumPointFullPath);
		ReadData2(FS, ppsl->m_fSysLife);//WriteProperty(file, L"fSysLife", ppsl->m_fSysLife);
		ReadData2(FS, ppsl->m_nEmissionRate);//WriteProperty(file, L"nEmissionRate", ppsl->m_nEmissionRate);
		ReadData2(FS, ppsl->m_nEmitterSpeed);//WriteProperty(file, L"nEmitterSpeed", ppsl->m_nEmitterSpeed);
		ReadData2(FS, ppsl->m_fParticleLifeMin);//WriteProperty(file, L"fParticleLifeMin", ppsl->m_fParticleLifeMin);
		ReadData2(FS, ppsl->m_fParticleLifeMax);//WriteProperty(file, L"fParticleLifeMax", ppsl->m_fParticleLifeMax);
		ReadData2(FS, ppsl->m_fEmissionDirection);//WriteProperty(file, L"fEmissionDirection", ppsl->m_fEmissionDirection);
		ReadData2(FS, ppsl->m_fEmissionSpread);//WriteProperty(file, L"fEmissionSpread", ppsl->m_fEmissionSpread);
		ReadData2(FS, ppsl->m_fSpeedMin);//WriteProperty(file, L"fSpeedMin", ppsl->m_fSpeedMin);
		ReadData2(FS, ppsl->m_fSpeedMax);//WriteProperty(file, L"fSpeedMax", ppsl->m_fSpeedMax);
		ReadData2(FS, ppsl->m_fGravity);//WriteProperty(file, L"fGravity", ppsl->m_fGravity);
		ReadData2(FS, ppsl->m_fGravityRand);//WriteProperty(file, L"fGravityRand", ppsl->m_fGravityRand);
		ReadData2(FS, ppsl->m_fRadialAcc);//WriteProperty(file, L"fRadialAcc", ppsl->m_fRadialAcc);
		ReadData2(FS, ppsl->m_fRadialAccRand);//WriteProperty(file, L"fRadialAccRand", ppsl->m_fRadialAccRand);
		ReadData2(FS, ppsl->m_fTangAcc);//WriteProperty(file, L"fTangAcc", ppsl->m_fTangAcc);
		ReadData2(FS, ppsl->m_fTangAccRand);//WriteProperty(file, L"fTangAccRand", ppsl->m_fTangAccRand);
		ReadData2(FS, ppsl->m_fParSizeStrtX);//WriteProperty(file, L"fParSizeStrtX", ppsl->m_fParSizeStrtX);
		ReadData2(FS, ppsl->m_fParSizeStrtY);//WriteProperty(file, L"fParSizeStrtY", ppsl->m_fParSizeStrtY);
		ReadData2(FS, ppsl->m_fParSizeEndX);//WriteProperty(file, L"fParSizeEndX", ppsl->m_fParSizeEndX);
		ReadData2(FS, ppsl->m_fParSizeEndY);//WriteProperty(file, L"fParSizeEndY", ppsl->m_fParSizeEndY);
		ReadData2(FS, ppsl->m_fParSizeVar);//WriteProperty(file, L"fParSizeVar", ppsl->m_fParSizeVar);
		ReadData2(FS, ppsl->m_fParSpinStrt);//WriteProperty(file, L"fParSpinStrt", ppsl->m_fParSpinStrt);
		ReadData2(FS, ppsl->m_fParSpinEnd);//WriteProperty(file, L"fParSpinEnd", ppsl->m_fParSpinEnd);
		ReadData2(FS, ppsl->m_fParSpinVar);//WriteProperty(file, L"fParSpinVar", ppsl->m_fParSpinVar);
		ReadData2(FS, ppsl->m_fRotateCentX);//WriteProperty(file, L"fRotateCentX", ppsl->m_fRotateCentX);
		ReadData2(FS, ppsl->m_fRotateCentY);//WriteProperty(file, L"fRotateCentY", ppsl->m_fRotateCentY);
		ReadData2(FS, ppsl->m_fParAngleStrt);//WriteProperty(file, L"fParAngleStrt", ppsl->m_fParAngleStrt);
		ReadData2(FS, ppsl->m_fParAngleEnd);//WriteProperty(file, L"fParAngleEnd", ppsl->m_fParAngleEnd);
		ReadData2(FS, ppsl->m_fParAngleVar);//WriteProperty(file, L"fParAngleVar", ppsl->m_fParAngleVar);
		ReadData2(FS, ppsl->m_nParAlphaStrt);//WriteProperty(file, L"nParAlphaStrt", ppsl->m_nParAlphaStrt);
		ReadData2(FS, ppsl->m_nParAlphaEnd);//WriteProperty(file, L"nParAlphaEnd", ppsl->m_nParAlphaEnd);
		ReadData2(FS, ppsl->m_fParAlphaVar);//WriteProperty(file, L"fParAlphaVar", ppsl->m_fParAlphaVar);
		ReadData2(FS, ppsl->m_nParAlphaMid);//WriteProperty(file, L"nParAlphaMid", ppsl->m_nParAlphaMid);
		ReadData2(FS, ppsl->m_fParAlphaMidTime);//WriteProperty(file, L"fParAlphaMidTime", ppsl->m_fParAlphaMidTime);
		ReadData2(FS, ppsl->m_fSizeRandom);//WriteProperty(file, L"fSizeRandom", ppsl->m_fSizeRandom);
		ReadData2(FS, ppsl->m_fSpinRandom);//WriteProperty(file, L"fSpinRandom", ppsl->m_fSpinRandom);
		ReadData2(FS, ppsl->m_fAngleRandom);//WriteProperty(file, L"fAngleRandom", ppsl->m_fAngleRandom);
		ReadData2(FS, ppsl->m_fAlignToMotionAngle);//WriteProperty(file, L"fAlignToMotionAngle", ppsl->m_fAlignToMotionAngle);
		ReadData2(FS, ppsl->m_nAlignToMotion);//WriteProperty(file, L"nAlignToMotion", ppsl->m_nAlignToMotion);
		ReadData2(FS, ppsl->m_nAlignToPath);//WriteProperty(file, L"nAlignToPath", ppsl->m_nAlignToPath);
		ReadData2(FS, ppsl->m_nMotionToPath);//WriteProperty(file, L"nMotionToPath", ppsl->m_nMotionToPath);
		ReadData2(FS, ppsl->m_nAlignToEmitter);//WriteProperty(file, L"nAlignToEmitter", ppsl->m_nAlignToEmitter);
		ReadData2(FS, ppsl->m_nAlignToHole);//WriteProperty(file, L"nAlignToHole", ppsl->m_nAlignToHole);
		ReadData2(FS, ppsl->m_nParticleRandAngle);//WriteProperty(file, L"nParticleRandAngle", ppsl->m_nParticleRandAngle);
		ReadData2(FS, ppsl->m_nParticleRandAngleSelf);//WriteProperty(file, L"nParticleRandAngleSelf", ppsl->m_nParticleRandAngleSelf);
		ReadData2(FS, ppsl->m_nParColorStrtR);//WriteProperty(file, L"nParColorStrtR", ppsl->m_nParColorStrtR);
		ReadData2(FS, ppsl->m_nParColorStrtG);//WriteProperty(file, L"nParColorStrtG", ppsl->m_nParColorStrtG);
		ReadData2(FS, ppsl->m_nParColorStrtB);//WriteProperty(file, L"nParColorStrtB", ppsl->m_nParColorStrtB);
		ReadData2(FS, ppsl->m_nParColorEndR);//WriteProperty(file, L"nParColorEndR", ppsl->m_nParColorEndR);
		ReadData2(FS, ppsl->m_nParColorEndG);//WriteProperty(file, L"nParColorEndG", ppsl->m_nParColorEndG);
		ReadData2(FS, ppsl->m_nParColorEndB);//WriteProperty(file, L"nParColorEndB", ppsl->m_nParColorEndB);
		ReadData2(FS, ppsl->m_fParColorVar);//WriteProperty(file, L"fParColorVar", ppsl->m_fParColorVar);
		ReadData2(FS, ppsl->m_fPSLzoom);//WriteProperty(file, L"fPSLzoom", ppsl->m_fPSLzoom);
		ReadData2(FS, ppsl->m_fEmitterSpin);//WriteProperty(file, L"fEmitterSpin", ppsl->m_fEmitterSpin);
		ReadData2(FS, ppsl->m_nDestBlendMode);//WriteProperty(file, L"nDestBlendMode", ppsl->m_nDestBlendMode);
		ReadDataBlendMode2(FS, ppsl->m_nBlendMode);//WriteBlendModeProperty(file, L"BlendMode", ppsl->m_nBlendMode);	

		int nTextureNum = 0;
		ReadData2(FS, nTextureNum);//WriteProperty(file, L"TextureNum", cnt);

		ppsl->m_vectorTexturePath.clear();
		//读纹理数组...
		std::wstring pTexturePath;
		for (int i = 0; i < nTextureNum; i++)
		{
			ReadData2(FS, TmpStr);
			pTexturePath = s2ws(TmpStr);
			pTexturePath = L"/effect/particle/texture/" + pTexturePath;
			ppsl->m_vectorTexturePath.push_back(pTexturePath);
		}

		ReadData2(FS, ppsl->m_nRowNum);//WriteProperty(file, L"nRowNum", ppsl->m_nRowNum);
		ReadData2(FS, ppsl->m_nColNum);//WriteProperty(file, L"nColNum", ppsl->m_nColNum);
		ReadData2(FS, ppsl->m_nTextureNum);//WriteProperty(file, L"nTextureNum", ppsl->m_nTextureNum);
		ReadData2(FS, (int&)(ppsl->m_rctMaxBox.left));//WriteProperty(file, L"rctMaxBoxL", ppsl->m_rctMaxBox.left);
		ReadData2(FS, (int&)(ppsl->m_rctMaxBox.top));//WriteProperty(file, L"rctMaxBoxT", ppsl->m_rctMaxBox.top);
		ReadData2(FS, (int&)(ppsl->m_rctMaxBox.right));//WriteProperty(file, L"rctMaxBoxR", ppsl->m_rctMaxBox.right);
		ReadData2(FS, (int&)(ppsl->m_rctMaxBox.bottom));//WriteProperty(file, L"rctMaxBoxB", ppsl->m_rctMaxBox.bottom);
		ReadData2(FS, ppsl->m_nInterfacePsl);//WriteProperty(file, L"nInterfacePsl", ppsl->m_nInterfacePsl);
		ReadData2(FS, ppsl->m_ptInterfacialCenter.x);//WriteProperty(file, L"ptInterfacialCenterX", ppsl->m_ptInterfacialCenter.x);
		ReadData2(FS, ppsl->m_ptInterfacialCenter.y);//WriteProperty(file, L"ptInterfacialCenterY", ppsl->m_ptInterfacialCenter.y);
		ReadData2(FS, ppsl->m_nSkillEffectSpritePoint);//WriteProperty(file, L"nSkillEffectSpritePoint", ppsl->m_nSkillEffectSpritePoint);

		return true;
	}

	bool ParticleManager::LoadPSLFromFileEx(std::wstring& fileName, PSL* ppsl)
	{
		std::wstring newname;
		bool bres = false;
		newname = fileName;
		std::transform(newname.begin(), newname.end(), newname.begin(), ::towlower);

		std::wstring subStr = L"\\";
		std::wstring ReplaceStr = L"/";
		std::wstring inStr = newname;

		newname = replace(inStr, subStr, ReplaceStr);

		std::ifstream FS(ws2s(newname).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile()--LoadPSLFromFileEx failed, fileName: %s\n", newname.c_str());
			return false;
		}
		FS.seekg(0, std::ios_base::beg);
		bres = LoadPSLFromStream(FS, ppsl);
		FS.close();
		if(!bres)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::LoadPSLFromFile()--LoadPSLFromStream failed, fileName: %s\n", newname.c_str());
			return false;
		}
		return true;
	}

	bool ParticleManager::LoadPSLFromStream(std::ifstream& FS, PSL* ppsl)
	{
		std::string TmpStr;
		ReadData(FS, ppsl->m_nVersion);
		ReadData(FS, ppsl->m_nAbsrtactPsl);//WriteProperty(file, L"nAbsrtactPsl", m_ppsl->m_nAbsrtactPsl);
		ReadData(FS, TmpStr);//WriteProperty(file, L"strVtxsObjectPath", m_ppsl->m_strVtxsObjectPath.c_str());
		ppsl->m_strVtxsObjectPath = s2ws(TmpStr);
		ReadData(FS, ppsl->m_nDirMotion);//WriteProperty(file, L"nDirMotion", m_ppsl->m_nDirMotion);
		ReadData(FS, ppsl->m_nPSConfigReq);//WriteProperty(file, L"nPSConfigReq", m_ppsl->m_nPSConfigReq);
		ReadData(FS, ppsl->m_nPathOrder);//WriteProperty(file, L"bPathOrder", m_ppsl->m_nPathOrder);
		ReadData(FS, ppsl->m_nUVmirrorh);//WriteProperty(file, L"bUVmirrorh", m_ppsl->m_nUVmirrorh);
		ReadData(FS, ppsl->m_nUVmirrorv);//WriteProperty(file, L"bUVmirrorv", m_ppsl->m_nUVmirrorv);
		ReadData(FS, ppsl->m_nParticleColorChange);//WriteProperty(file, L"bParticleColorChange", m_ppsl->m_nParticleColorChange);
		ReadData(FS, ppsl->m_nParticleColorRand);//WriteProperty(file, L"bParticleColorRand", m_ppsl->m_nParticleColorRand);
		ReadData(FS, ppsl->m_nTrackFx);//WriteProperty(file, L"bTrackFx", m_ppsl->m_nTrackFx);
		ReadData(FS, ppsl->m_nEmitterType);//WriteProperty(file, L"bEmitterType", m_ppsl->m_nEmitterType);
		ReadData(FS, ppsl->m_nSingleParticleAni);//WriteProperty(file, L"bSingleParticleAni", m_ppsl->m_nSingleParticleAni);
		ReadData(FS, ppsl->m_nParticleAniTextureIndex1);//WriteProperty(file, L"bParticleAniTextureIndex1", m_ppsl->m_nParticleAniTextureIndex1);
		ReadData(FS, ppsl->m_nAniPlaySpeed);//WriteProperty(file, L"nAniPlaySpeed", m_ppsl->m_nAniPlaySpeed);
		ReadData(FS, ppsl->m_fAniSpeedRand);//WriteProperty(file, L"fAniSpeedRand", m_ppsl->m_fAniSpeedRand);
		ReadData(FS, ppsl->m_nParFillMode);//WriteProperty(file, L"bParFillMode", m_ppsl->m_nParFillMode);
		ReadData(FS, ppsl->m_nPosType);//WriteProperty(file, L"nPosType", m_ppsl->m_nPosType);
		if(ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)
		{
			ReadData(FS, TmpStr);//WriteProperty( file, L"strFullPathFile", m_ppsl->m_strFullPathFile.c_str());
			ppsl->m_strFullPathFile = s2ws(TmpStr);
		}
		ReadData(FS, ppsl->m_nParticlePathInitPosType);//WriteProperty(file, L"nParticlePathInitPosType", ppsl->m_nParticlePathInitPosType);
		ReadData(FS, ppsl->m_nParticleSizeRandType);//WriteProperty(file, L"nParticleSizeRandType", ppsl->m_nParticleSizeRandType);
		ReadData(FS, ppsl->m_nMoveType);//WriteProperty(file, L"nMoveType", ppsl->m_nMoveType);
		ReadData(FS, ppsl->m_point0.x);//WriteProperty(file, L"fPointX0", ppsl->m_point0.x);
		ReadData(FS, ppsl->m_point0.y);//WriteProperty(file, L"fPointY0", ppsl->m_point0.y);
		ReadData(FS, ppsl->m_point1.x);//WriteProperty(file, L"fPointX1", ppsl->m_point1.x);
		ReadData(FS, ppsl->m_point1.y);//WriteProperty(file, L"fPointY1", ppsl->m_point1.y);
		ReadData(FS, ppsl->m_pointEmitterSpinCent.x);//WriteProperty(file, L"pointEmitterSpinCentX", ppsl->m_pointEmitterSpinCent.x);
		ReadData(FS, ppsl->m_pointEmitterSpinCent.y);//WriteProperty(file, L"pointEmitterSpinCentY", ppsl->m_pointEmitterSpinCent.y);
		ReadData(FS, ppsl->m_pointParticleHole.x);//WriteProperty(file, L"pointParticleHoleX", ppsl->m_pointParticleHole.x);
		ReadData(FS, ppsl->m_pointParticleHole.y);//WriteProperty(file, L"pointParticleHoleY", ppsl->m_pointParticleHole.y);
		ReadData(FS, ppsl->m_nSysPosRadiusX);//WriteProperty(file, L"nSysPosRadiusX", ppsl->m_nSysPosRadiusX);
		ReadData(FS, ppsl->m_nSysPosRadiusY);//WriteProperty(file, L"nSysPosRadiusY", ppsl->m_nSysPosRadiusY);
		ReadData(FS, (int&)(ppsl->m_rct.left));//WriteProperty(file, L"nRctLeft", ppsl->m_rct.left);
		ReadData(FS, (int&)(ppsl->m_rct.top));//WriteProperty(file, L"nRctTop", ppsl->m_rct.top);
		ReadData(FS, (int&)(ppsl->m_rct.right));//WriteProperty(file, L"nRctRight", ppsl->m_rct.right);
		ReadData(FS, (int&)(ppsl->m_rct.bottom));//WriteProperty(file, L"nRctBottom", ppsl->m_rct.bottom);
		ReadData(FS, ppsl->m_nSysMaxParticles);//WriteProperty(file, L"nSysMaxParticles", ppsl->m_nSysMaxParticles);
		ReadData(FS, ppsl->m_fPathZoom);//WriteProperty(file, L"fPathZoom", ppsl->m_fPathZoom);
		ReadData(FS, ppsl->m_fPathSpin);//WriteProperty(file, L"fPathSpin", ppsl->m_fPathSpin);
		ReadData(FS, ppsl->m_nPathMirrorType);//WriteProperty(file, L"nPathMirrorType", ppsl->m_nPathMirrorType);
		ReadData(FS, ppsl->m_nNumPointFullPath);//WriteProperty(file, L"nNumPointFullPath", ppsl->m_nNumPointFullPath);
		ReadData(FS, ppsl->m_fSysLife);//WriteProperty(file, L"fSysLife", ppsl->m_fSysLife);
		ReadData(FS, ppsl->m_nEmissionRate);//WriteProperty(file, L"nEmissionRate", ppsl->m_nEmissionRate);
		ReadData(FS, ppsl->m_nEmitterSpeed);//WriteProperty(file, L"nEmitterSpeed", ppsl->m_nEmitterSpeed);
		ReadData(FS, ppsl->m_fParticleLifeMin);//WriteProperty(file, L"fParticleLifeMin", ppsl->m_fParticleLifeMin);
		ReadData(FS, ppsl->m_fParticleLifeMax);//WriteProperty(file, L"fParticleLifeMax", ppsl->m_fParticleLifeMax);
		ReadData(FS, ppsl->m_fEmissionDirection);//WriteProperty(file, L"fEmissionDirection", ppsl->m_fEmissionDirection);
		ReadData(FS, ppsl->m_fEmissionSpread);//WriteProperty(file, L"fEmissionSpread", ppsl->m_fEmissionSpread);
		ReadData(FS, ppsl->m_fSpeedMin);//WriteProperty(file, L"fSpeedMin", ppsl->m_fSpeedMin);
		ReadData(FS, ppsl->m_fSpeedMax);//WriteProperty(file, L"fSpeedMax", ppsl->m_fSpeedMax);
		ReadData(FS, ppsl->m_fGravity);//WriteProperty(file, L"fGravity", ppsl->m_fGravity);
		ReadData(FS, ppsl->m_fGravityRand);//WriteProperty(file, L"fGravityRand", ppsl->m_fGravityRand);
		ReadData(FS, ppsl->m_fRadialAcc);//WriteProperty(file, L"fRadialAcc", ppsl->m_fRadialAcc);
		ReadData(FS, ppsl->m_fRadialAccRand);//WriteProperty(file, L"fRadialAccRand", ppsl->m_fRadialAccRand);
		ReadData(FS, ppsl->m_fTangAcc);//WriteProperty(file, L"fTangAcc", ppsl->m_fTangAcc);
		ReadData(FS, ppsl->m_fTangAccRand);//WriteProperty(file, L"fTangAccRand", ppsl->m_fTangAccRand);
		ReadData(FS, ppsl->m_fParSizeStrtX);//WriteProperty(file, L"fParSizeStrtX", ppsl->m_fParSizeStrtX);
		ReadData(FS, ppsl->m_fParSizeStrtY);//WriteProperty(file, L"fParSizeStrtY", ppsl->m_fParSizeStrtY);
		ReadData(FS, ppsl->m_fParSizeEndX);//WriteProperty(file, L"fParSizeEndX", ppsl->m_fParSizeEndX);
		ReadData(FS, ppsl->m_fParSizeEndY);//WriteProperty(file, L"fParSizeEndY", ppsl->m_fParSizeEndY);
		ReadData(FS, ppsl->m_fParSizeVar);//WriteProperty(file, L"fParSizeVar", ppsl->m_fParSizeVar);
		ReadData(FS, ppsl->m_fParSpinStrt);//WriteProperty(file, L"fParSpinStrt", ppsl->m_fParSpinStrt);
		ReadData(FS, ppsl->m_fParSpinEnd);//WriteProperty(file, L"fParSpinEnd", ppsl->m_fParSpinEnd);
		ReadData(FS, ppsl->m_fParSpinVar);//WriteProperty(file, L"fParSpinVar", ppsl->m_fParSpinVar);
		ReadData(FS, ppsl->m_fRotateCentX);//WriteProperty(file, L"fRotateCentX", ppsl->m_fRotateCentX);
		ReadData(FS, ppsl->m_fRotateCentY);//WriteProperty(file, L"fRotateCentY", ppsl->m_fRotateCentY);
		ReadData(FS, ppsl->m_fParAngleStrt);//WriteProperty(file, L"fParAngleStrt", ppsl->m_fParAngleStrt);
		ReadData(FS, ppsl->m_fParAngleEnd);//WriteProperty(file, L"fParAngleEnd", ppsl->m_fParAngleEnd);
		ReadData(FS, ppsl->m_fParAngleVar);//WriteProperty(file, L"fParAngleVar", ppsl->m_fParAngleVar);
		ReadData(FS, ppsl->m_nParAlphaStrt);//WriteProperty(file, L"nParAlphaStrt", ppsl->m_nParAlphaStrt);
		ReadData(FS, ppsl->m_nParAlphaEnd);//WriteProperty(file, L"nParAlphaEnd", ppsl->m_nParAlphaEnd);
		ReadData(FS, ppsl->m_fParAlphaVar);//WriteProperty(file, L"fParAlphaVar", ppsl->m_fParAlphaVar);
		ReadData(FS, ppsl->m_nParAlphaMid);//WriteProperty(file, L"nParAlphaMid", ppsl->m_nParAlphaMid);
		ReadData(FS, ppsl->m_fParAlphaMidTime);//WriteProperty(file, L"fParAlphaMidTime", ppsl->m_fParAlphaMidTime);
		ReadData(FS, ppsl->m_fSizeRandom);//WriteProperty(file, L"fSizeRandom", ppsl->m_fSizeRandom);
		ReadData(FS, ppsl->m_fSpinRandom);//WriteProperty(file, L"fSpinRandom", ppsl->m_fSpinRandom);
		ReadData(FS, ppsl->m_fAngleRandom);//WriteProperty(file, L"fAngleRandom", ppsl->m_fAngleRandom);
		ReadData(FS, ppsl->m_fAlignToMotionAngle);//WriteProperty(file, L"fAlignToMotionAngle", ppsl->m_fAlignToMotionAngle);
		ReadData(FS, ppsl->m_nAlignToMotion);//WriteProperty(file, L"nAlignToMotion", ppsl->m_nAlignToMotion);
		ReadData(FS, ppsl->m_nAlignToPath);//WriteProperty(file, L"nAlignToPath", ppsl->m_nAlignToPath);
		ReadData(FS, ppsl->m_nMotionToPath);//WriteProperty(file, L"nMotionToPath", ppsl->m_nMotionToPath);
		ReadData(FS, ppsl->m_nAlignToEmitter);//WriteProperty(file, L"nAlignToEmitter", ppsl->m_nAlignToEmitter);
		ReadData(FS, ppsl->m_nAlignToHole);//WriteProperty(file, L"nAlignToHole", ppsl->m_nAlignToHole);
		ReadData(FS, ppsl->m_nParticleRandAngle);//WriteProperty(file, L"nParticleRandAngle", ppsl->m_nParticleRandAngle);
		ReadData(FS, ppsl->m_nParticleRandAngleSelf);//WriteProperty(file, L"nParticleRandAngleSelf", ppsl->m_nParticleRandAngleSelf);
		ReadData(FS, ppsl->m_nParColorStrtR);//WriteProperty(file, L"nParColorStrtR", ppsl->m_nParColorStrtR);
		ReadData(FS, ppsl->m_nParColorStrtG);//WriteProperty(file, L"nParColorStrtG", ppsl->m_nParColorStrtG);
		ReadData(FS, ppsl->m_nParColorStrtB);//WriteProperty(file, L"nParColorStrtB", ppsl->m_nParColorStrtB);
		ReadData(FS, ppsl->m_nParColorEndR);//WriteProperty(file, L"nParColorEndR", ppsl->m_nParColorEndR);
		ReadData(FS, ppsl->m_nParColorEndG);//WriteProperty(file, L"nParColorEndG", ppsl->m_nParColorEndG);
		ReadData(FS, ppsl->m_nParColorEndB);//WriteProperty(file, L"nParColorEndB", ppsl->m_nParColorEndB);
		ReadData(FS, ppsl->m_fParColorVar);//WriteProperty(file, L"fParColorVar", ppsl->m_fParColorVar);
		ReadData(FS, ppsl->m_fPSLzoom);//WriteProperty(file, L"fPSLzoom", ppsl->m_fPSLzoom);
		ReadData(FS, ppsl->m_fEmitterSpin);//WriteProperty(file, L"fEmitterSpin", ppsl->m_fEmitterSpin);
		ReadData(FS, ppsl->m_nDestBlendMode);//WriteProperty(file, L"nDestBlendMode", ppsl->m_nDestBlendMode);
		ReadDataBlendMode(FS, ppsl->m_nBlendMode);//WriteBlendModeProperty(file, L"BlendMode", ppsl->m_nBlendMode);	

		int nTextureNum = 0;
		ReadData(FS, nTextureNum);//WriteProperty(file, L"TextureNum", cnt);

		ppsl->m_vectorTexturePath.clear();
		//读纹理数组...
		std::wstring pTexturePath;
		for(int i=0; i<nTextureNum; i++)
		{
			ReadData(FS, TmpStr);
			pTexturePath = s2ws(TmpStr);
			pTexturePath = L"/effect/particle/texture/" + pTexturePath;
			ppsl->m_vectorTexturePath.push_back(pTexturePath);
		}

		ReadData(FS, ppsl->m_nRowNum);//WriteProperty(file, L"nRowNum", ppsl->m_nRowNum);
		ReadData(FS, ppsl->m_nColNum);//WriteProperty(file, L"nColNum", ppsl->m_nColNum);
		ReadData(FS, ppsl->m_nTextureNum);//WriteProperty(file, L"nTextureNum", ppsl->m_nTextureNum);
		ReadData(FS, (int&)(ppsl->m_rctMaxBox.left));//WriteProperty(file, L"rctMaxBoxL", ppsl->m_rctMaxBox.left);
		ReadData(FS, (int&)(ppsl->m_rctMaxBox.top));//WriteProperty(file, L"rctMaxBoxT", ppsl->m_rctMaxBox.top);
		ReadData(FS, (int&)(ppsl->m_rctMaxBox.right));//WriteProperty(file, L"rctMaxBoxR", ppsl->m_rctMaxBox.right);
		ReadData(FS, (int&)(ppsl->m_rctMaxBox.bottom));//WriteProperty(file, L"rctMaxBoxB", ppsl->m_rctMaxBox.bottom);
		ReadData(FS, ppsl->m_nInterfacePsl);//WriteProperty(file, L"nInterfacePsl", ppsl->m_nInterfacePsl);
		ReadData(FS, ppsl->m_ptInterfacialCenter.x);//WriteProperty(file, L"ptInterfacialCenterX", ppsl->m_ptInterfacialCenter.x);
		ReadData(FS, ppsl->m_ptInterfacialCenter.y);//WriteProperty(file, L"ptInterfacialCenterY", ppsl->m_ptInterfacialCenter.y);
		ReadData(FS, ppsl->m_nSkillEffectSpritePoint);//WriteProperty(file, L"nSkillEffectSpritePoint", ppsl->m_nSkillEffectSpritePoint);

		return true;
	}

	bool ParticleManager::DeleteParticleSystem(ParticleSystemHandle handle)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
			return false;

		CGeneralParticleSystem* pPS = dynamic_cast<CGeneralParticleSystem*>(it->second);
		if (pPS)
		{
			if (pPS->m_restTexRes)//貌似还没有加载完成呢
			{
				XHardRef<PSL> ppsl = pPS->GetPSL();
				int cnt = (int)ppsl->m_vectorTexturePath.size();
				std::map<std::wstring, std::set<CGeneralParticleSystem*>>::iterator loadingIt;
				for(int i=0; i<cnt; i++)
				{
					loadingIt = m_LoadingNotify.find(ppsl->m_vectorTexturePath[i]);
					if (loadingIt != m_LoadingNotify.end())
					{
						loadingIt->second.erase(pPS);
					}
				}
			}
		}

		delete it->second;
		m_mapParticleSystem.erase(it);

		return true;
	}

	bool ParticleManager::ReleaseParticleSystemRes(ParticleSystemHandle handle)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
			return false;

		CGeneralParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CGeneralParticleSystem*>(it->second) )
		{
			return pPS->ReleaseResource();
		}
		return false;
	}

	bool ParticleManager::ResetParticleSystem(ParticleSystemHandle handle)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if ( m_mapParticleSystem.end() == it)
			return false;

		it->second->Reset(); //以前的粒子都删除..重新产生新的

		//vertex-buffer texture PSParameter重置？
		return true;
	}

	bool ParticleManager::ModifyParticleSystem(ParticleSystemHandle handle, const PSModifyParam& param)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
		{//INVALID_PS_HANDLE
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::ModifyParticleSystem()--ParticleSystemHandle %d\n", handle);
			return false;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();
		PSParameter& psParameter = particleSys->GetPSParameter();

		PSModifyParam paramTemp = param; //这个是可以考虑去掉的...

		if(psParameter.m_bModifyLineEmitter)
		{
			paramTemp.m_pointCent = (paramTemp.m_fpoint0 + paramTemp.m_fpoint1)/2.0f;
		}

		if( !(psParameter.m_bScreenCoord) && (psParameter.m_bVertexRHWflag) )
			AdjustPSParameter(paramTemp); //世界坐标转换..

		if(ppsl->m_nInterfacePsl) //界面特效
			paramTemp.m_pointCent += ppsl->m_ptInterfacialCenter; //加相对界面的偏移值

		FPOINT fpt = FPOINT(0.0f, 0.0f);
		if(ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
		{
			FPOINT pt0 = paramTemp.m_pointCent;
			if(param.m_scaleType != XPEST_NOSCALE)
			{
				pt0 *= param.m_fParticlePosCoef;
			}
			fpt = pt0 - psParameter.m_psModifyParam.m_pointCent;

			particleSys->AddPOINTdxdy( fpt );
			float fangle = param.m_fEmitterSpinAngle - psParameter.m_psModifyParam.m_fEmitterSpinAngle;
			fangle = fangle>=0.0f ? fangle:(2.0f*XP_PI + fangle);
			particleSys->SetParticleAngle( fangle );
		}

		particleSys->m_pointCent0 = param.m_pointCent;

		if(ppsl->m_nMoveType != PSLMOVETYPE_FULLSCREEN)
		{
			//全屏特效自己计算的..m_pointCent
			//it->second->m_psParameter.m_pointCent来自粒子文件？..
			psParameter.m_psModifyParam.m_pointCent = paramTemp.m_pointCent;
		}
		if(param.m_scaleType != XPEST_NOSCALE)
		{
			//已经产生的粒子没有移到正确位置上。。。。
			//这种情况需要特殊处理一下---等以后根据实际需求来做吧
			psParameter.m_psModifyParam.m_pointCent *= param.m_fParticlePosCoef;
		}

		psParameter.m_psModifyParam.m_scaleType = paramTemp.m_scaleType;
		psParameter.m_psModifyParam.m_fParticleSizeCoef = paramTemp.m_fParticleSizeCoef;
		psParameter.m_psModifyParam.m_fParticlePosCoef = paramTemp.m_fParticlePosCoef;

		psParameter.m_psModifyParam.m_fEmissionScale = paramTemp.m_fEmissionScale;
		psParameter.m_psModifyParam.m_fEmitterSpinAngle = paramTemp.m_fEmitterSpinAngle;
		
		if(psParameter.m_bModifyLineEmitter)
		{
			FPOINT fpt((float)(m_ViewPort.left), (float)(m_ViewPort.top));

			//
	//最开始的发射器线段世界坐标psParameter.m_ptLineEmitterStrt  psParameter.m_ptLineEmitterEnd
	//上一次的发射器线段世界坐标psParameter.m_psModifyParam.m_fpoint0+fpt psParameter.m_psModifyParam.m_fpoint1+fpt
	//现在的发射器线段世界坐标paramTemp.m_fpoint0  paramTemp.m_fpoint1
			float fdis0 = distance(psParameter.m_ptLineEmitterStrt, psParameter.m_ptLineEmitterEnd);
			float fdis1 = distance(paramTemp.m_fpoint0, paramTemp.m_fpoint1);
			float fzoom = fdis1/fdis0; //缩放比
			psParameter.m_psModifyParam.m_fEmissionScale = fzoom;

			float fx = psParameter.m_ptLineEmitterEnd.x - psParameter.m_ptLineEmitterStrt.x;
			float fy = psParameter.m_ptLineEmitterEnd.y - psParameter.m_ptLineEmitterStrt.y;
			XPVECTOR2 v1(fx, fy);

			fx = paramTemp.m_fpoint1.x - paramTemp.m_fpoint0.x;
			fy = paramTemp.m_fpoint1.y - paramTemp.m_fpoint0.y;
			XPVECTOR2 v2(fx, fy);

			float fAngle = Computer2VectorAngle(v1, v2);
			float fdir = v1.x*v2.y - v1.y*v2.x;
			if(fdir<0)
			{
				fAngle *= -1.0f;
			}

			psParameter.m_psModifyParam.m_fLineEmitterRotate = fAngle;
			//

			psParameter.m_psModifyParam.m_fpoint0 = paramTemp.m_fpoint0 - fpt;
			psParameter.m_psModifyParam.m_fpoint1 = paramTemp.m_fpoint1 - fpt;
		}

		if( ppsl->m_nPosType == PSLTYPE_LINE &&
			paramTemp.m_fLineLength>0 &&
			!IsEqualFloat(psParameter.m_psModifyParam.m_fLineLength, paramTemp.m_fLineLength) )
		{
			float x = fabs(ppsl->m_point1.x - ppsl->m_point0.x);
			float y = fabs(ppsl->m_point1.y - ppsl->m_point0.y);
			if(x>10 && y<3) //视为水平方向的线
			{
				//修改粒子数目
				CGeneralParticleSystem* pPS = NULL;
				if( pPS = dynamic_cast<CGeneralParticleSystem*>(particleSys) )
				{
					float fzoom = paramTemp.m_fLineLength /x;

					int nParticleSize = (int)(ppsl->m_nSysMaxParticles *fzoom);
					int nEmissionRate = (int)(ppsl->m_nEmissionRate *fzoom);
					nParticleSize = nParticleSize>2000 ? 2000:nParticleSize;
					nEmissionRate = nEmissionRate>1000 ? 1000:nEmissionRate;
					pPS->ResizeParticleArry(nParticleSize, nEmissionRate);
				}
			}
		}

		
		if( ppsl->m_nPosType == PSLTYPE_RECT && 
			paramTemp.m_fRctWidth > 0 &&
			paramTemp.m_fRctHeight > 0 &&
			(!IsEqualFloat(psParameter.m_psModifyParam.m_fRctWidth, paramTemp.m_fRctWidth) || 
			 !IsEqualFloat(psParameter.m_psModifyParam.m_fRctHeight, paramTemp.m_fRctHeight)) )
		{
			//修改粒子数目
			float fzoom = 1.0f;
			if(ppsl->m_nMotionToPath == 1)
			{
				//粒子数与周长的倍数成正比
				fzoom = (paramTemp.m_fRctWidth + paramTemp.m_fRctHeight)/(float)(ppsl->m_rct.Width() + ppsl->m_rct.Height());					    
			}
			else
			{
				float fx = paramTemp.m_fRctWidth/(float)(ppsl->m_rct.Width());
				float fy = paramTemp.m_fRctHeight/(float)(ppsl->m_rct.Height());
				fzoom = fx*fy; //粒子数与面积的倍数成正比
			}

			CGeneralParticleSystem* pPS = NULL;
			if( pPS = dynamic_cast<CGeneralParticleSystem*>(particleSys) )
			{
				int nParticleSize = (int)(ppsl->m_nSysMaxParticles *fzoom); 
				int nEmissionRate = (int)(ppsl->m_nEmissionRate *fzoom);
				nParticleSize = nParticleSize>2000 ? 2000:nParticleSize;
				nEmissionRate = nEmissionRate>1000 ? 1000:nEmissionRate;
				pPS->ResizeParticleArry(nParticleSize, nEmissionRate);
			}
		}
		psParameter.m_psModifyParam.m_fLineLength = paramTemp.m_fLineLength;
		psParameter.m_psModifyParam.m_fRctWidth = paramTemp.m_fRctWidth;
		psParameter.m_psModifyParam.m_fRctHeight = paramTemp.m_fRctHeight;
		psParameter.m_psModifyParam.m_vectorRctBlindArea = paramTemp.m_vectorRctBlindArea;

		psParameter.m_psModifyParam.m_vectorBlockLine = paramTemp.m_vectorBlockLine;
		psParameter.m_psModifyParam.m_vectorReflectLine = paramTemp.m_vectorReflectLine;

	//	if(paramTemp.m_bModifyLineEmitter)
	//		particleSys->Reset();

		return true;
	}

	bool ParticleManager::ModifyParticleSystem(ParticleSystemHandle handle, const PSParameter& param)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
		{//INVALID_PS_HANDLE
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::ModifyParticleSystem()--ParticleSystemHandle %d\n", handle);

			return false;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();

		PSParameter& pa = particleSys->GetPSParameter();
		pa.color = param.color;
		pa.time = param.time;
		
		if( fabs(param.time - PSDEFAULTTIME) > XP_EPSILON )
			particleSys->SetSysLife(param.time);
		else
			particleSys->SetSysLife(ppsl->m_fSysLife);

		pa.layer = param.layer;
		pa.cycle = param.cycle;
		pa.autorender = param.autorender;

		pa.m_ptLineEmitterStrt = param.m_ptLineEmitterStrt;
		pa.m_ptLineEmitterEnd = param.m_ptLineEmitterEnd;
		pa.m_bModifyLineEmitter = param.m_bModifyLineEmitter;
		
		if(ppsl->m_nMoveType != PSLMOVETYPE_FULLSCREEN)
		{
			if(pa.m_bVertexRHWflag != param.m_bVertexRHWflag) //2009--9--15
			{
				if( !(m_logName.empty()) )
				{
					std::wstring str = pa.m_bVertexRHWflag ? L"---Modify: m_bVertexRHWflag: true\n":L"---Modify: m_bVertexRHWflag: false\n";
					std::wstring pslName = particleSys->GetPslName();
					str = pslName +str;
					XPLOG_PSMHZX(str.c_str());
				}

				pa.m_bVertexRHWflag = param.m_bVertexRHWflag; 
				//在ps的构造函数里面是根据这个值来分配顶点空间的, 当这个值修改了需要重置顶点空间
				particleSys->ResetVerticesVector();//m_vertices_TCT---m_vertices_UTCT
			}

			pa.m_bScreenCoord = param.m_bScreenCoord;
		}

		ModifyParticleSystem(handle, param.m_psModifyParam); //这里面要做相应的坐标调整

		return true;
	}

	void ParticleManager::OnViewportUpdate(const CRECT &viewport)
	{
		m_ViewPort = viewport;
	}

	bool ParticleManager::RenderParticleSystem(ParticleSystemHandle handle, bool bUpdate, bool bEndScene) 
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
			return false;

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();

		if(ppsl->m_nPSConfigReq > m_nComputerConfig)
			return false; //配置不符合要求..跳过该特效

		if(!bUpdate)
		{
			particleSys->SetViewPort(m_ViewPort);
		}

		if( !bEndScene )
		{
			return particleSys->Render(bUpdate, bEndScene);
		}

		static XPTEXCOLORBLEND_TYPE shaderColorOPMap[12] = 
		{
			XPTOP_DEFAULT, //0
			XPTOP_DEFAULT, //1
			XPTOP_SELECTARG1, //2
			XPTOP_SELECTARG2, //3
			XPTOP_MODULATE, //4
			XPTOP_MODULATE2X, //5
			XPTOP_DEFAULT, //6
			XPTOP_ADD, //7
			XPTOP_ADDSIGNED, //8
			XPTOP_DEFAULT, //9
			XPTOP_SUBTRACT, //10
			XPTOP_ADDSMOOTH, //11
		};

		RendererStateAutoRecover stateAutoRecover(m_pRenderer);
		
		m_pRenderer->SetTextureColorBlendType(shaderColorOPMap[ppsl->m_nBlendMode]);
		
		switch(ppsl->m_nDestBlendMode)
		{
		case PSLRENDSTATE_ALPHA:
			m_pRenderer->SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			m_pRenderer->SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		case PSLRENDSTATE_LIGHTING:
			m_pRenderer->SetRenderEffect(XPRE_NULL);
			m_pRenderer->SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			m_pRenderer->SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case PSLRENDSTATE_ADD:
			m_pRenderer->SetRenderEffect(XPRE_NULL);
			m_pRenderer->SetAlphaBlendSrcType(XPBLEND_ONE);
			m_pRenderer->SetAlphaBlendDstType(XPBLEND_ONE);
		    break;
		default:
		    break;
		}

		bool result = particleSys->Render(bUpdate, bEndScene);
		return result;
	}

	bool ParticleManager::RenderParticleSystemByLayer(int layer)
	{
		for(std::map<ParticleSystemHandle, CParticleSystem*>::iterator it = m_mapParticleSystem.begin(), ie = m_mapParticleSystem.end(); it != ie; ++it)
		{
			if(it->second->GetPSParameter().layer == layer)
			{
				if(it->second->GetPSParameter().autorender)
				{
					it->second->SetViewPort(m_ViewPort);
					it->second->Render();
				}
			}
		}

		return true;
	}

	int ParticleManager::GetParticleSystemCount() const
	{
		return (int)m_mapParticleSystem.size();
	}

	int ParticleManager::GetTotalNumActiveParticles() const
	{
		int totalNum = 0;
		for(std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.begin(), ie = m_mapParticleSystem.end(); it != ie; ++it)
		{
			totalNum += it->second->GetNumActiveParticles();
		}

		return totalNum;
	}

	int ParticleManager::GetNumActiveParticles(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetNumActiveParticles(%d)--Failed\n", handle);
			return -1;
		}

		return it->second->GetNumActiveParticles();
	}

	PSINFO ParticleManager::GetParticleSystemInfo(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetParticleSystemInfo(%d)--Failed\n", handle);
			return PSINFO();
		}

		CParticleSystem* particleSys = it->second;//GetParticleSysByHandle(handle);
		XHardRef<PSL> ppsl = particleSys->GetPSL();

		PSINFO psInfo;
	//	psInfo.m_rctMaxBox = it->second->GetPSMaxBox();
		CRECT rctMaxBox = ppsl->m_rctMaxBox;
		if(ppsl->m_nMoveType == PSLMOVETYPE_FULLSCREEN)
			rctMaxBox = CRECT(-1000, -1000, 1000, 1000);
		psInfo.m_rctMaxBox = rctMaxBox;
		psInfo.m_pointCent = it->second->GetPSParameter().m_psModifyParam.m_pointCent;
		psInfo.m_moveType = ppsl->m_nMoveType;
		psInfo.m_emisType = ppsl->m_nPosType;
		psInfo.m_bCycle = it->second->GetPSParameter().cycle;

		psInfo.m_nPSLConfigReq = ppsl->m_nPSConfigReq;

		return psInfo;
	}

	const PSParameter* ParticleManager::GetParticleSystemParameter(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if(m_mapParticleSystem.end() == it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetParticleSystemInfo(%d)--Failed\n", handle);
			return NULL;
		}
		return &(it->second->GetPSParameter());
	}

	float ParticleManager::GetSysLife(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetSysLife(%d)--Failed\n", handle);
			return 0;
		}
		return it->second->GetSysLife();
	}

	float ParticleManager::GetParticleMaxLife(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetParticleMaxLife(%d)--Failed\n", handle);
			return 0;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();

		return ppsl->m_fParticleLifeMax;
	}

	static void SetParticleSysResouce(CGeneralParticleSystem *pGPS, XHardRef<PSTEXTUREINFOSTRUCT> *pRefTextureInfo)
	{
		(*pRefTextureInfo)->dwTime = GetTickCount()/1000;
		//设置纹理资源。。。分配粒子存储空间等
		pGPS->SetParticleSysResouce(*pRefTextureInfo);
		pGPS->SetLoadedResFlag(true);
	}

	void ParticleManager::NotifyLoadingUrl(const std::wstring &url, bool ok)
	{
		std::map<std::wstring, std::set<CGeneralParticleSystem*>>::iterator it = m_LoadingNotify.find(url);
		if (it != m_LoadingNotify.end())
		{
			CGeneralParticleSystem* pPS;
			std::set<CGeneralParticleSystem*>::iterator psIt = it->second.begin(), psIe = it->second.end();
			if (ok)
			{
				DWORD time = GetTickCount() / 1000;
				XHardRef<PSPICHANDLESTRUCT>* pRefHandle;
				std::map<std::wstring, std::vector<XHardRef<PSPICHANDLESTRUCT>>> m_okPSL;
				std::map<std::wstring, std::vector<XHardRef<PSPICHANDLESTRUCT>>>::iterator okpslIt;
				for (;psIt!=psIe;++psIt)
				{
					pPS = *psIt;
					--(pPS->m_restTexRes);
					if (pPS->m_restTexRes == 0)
					{//所有的资源都全了
						if (pPS->IsLoadFaild())
						{
							XPASSERT(pPS->GetParticleLoadingNotify() && L"//加载失败了，通知ParticleEffect吧");
							pPS->GetParticleLoadingNotify()->OnReady(false);
							//这时pPS已经被delete了。。。
							pPS = NULL;
							continue;
						}
						XHardRef<PSL> ppsl = pPS->GetPSL();
						std::wstring pslName = pPS->GetPslName();
						okpslIt = m_okPSL.find(pslName);
						if (okpslIt != m_okPSL.end())//这个粒子已经好了
						{
							pPS->SetPicHandleRefResource(okpslIt->second);
						} else {
							int cnt = (int)ppsl->m_vectorTexturePath.size();
							std::vector<XHardRef<PSPICHANDLESTRUCT>> &vectorD3DTexture = m_okPSL[pslName];
							for(int i=0; i<cnt; i++)
							{
								std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::iterator pit = m_mapD3d9Texture.find(ppsl->m_vectorTexturePath[i]);
								XPASSERT(pit != m_mapD3d9Texture.end() && L"pPS->m_restTexRes == 0了，资源应该全了");
								pRefHandle = pit->second;
								(*pRefHandle)->dwTime = time;
								vectorD3DTexture.push_back(*pRefHandle);
							}
							if (!InsertPslTexture(vectorD3DTexture, pslName))
							{
								vectorD3DTexture.clear();
								//加载失败了，通知ParticleEffect吧
								XPASSERT(pPS->GetParticleLoadingNotify() && L"//加载失败了，通知ParticleEffect吧");
								pPS->GetParticleLoadingNotify()->OnReady(false);
								//这时pPS已经被delete了。。。
								pPS = NULL;
								continue;
							}
							pPS->SetPicHandleRefResource(vectorD3DTexture);
						}
						SetParticleSysResouce(pPS, m_mapPslTexture[pslName]);
						//加载成功了，通知ParticleEffect吧
						XPASSERT(pPS->GetParticleLoadingNotify() && L"//加载成功了，通知ParticleEffect吧");
						pPS->GetParticleLoadingNotify()->OnReady(true);
						pPS->SetParticleLoadingNotify(NULL);
					}

				}
			} else {
				//加载失败了，通知ParticleEffect吧
				for (;psIt!=psIe;++psIt)
				{
					pPS = *psIt;
					--(pPS->m_restTexRes);
					pPS->SetLoadFaild();
					if (pPS->m_restTexRes == 0)
					{
						XPASSERT(pPS->GetParticleLoadingNotify() && L"//加载失败了，通知ParticleEffect吧");
						pPS->GetParticleLoadingNotify()->OnReady(false);
						//这时pPS已经被delete了。。。
						pPS = NULL;
					}
				}
			}
			m_LoadingNotify.erase(it);
		}
	}

	void ParticleManager::SetStopFlag(ParticleSystemHandle handle, bool bStop)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetStopFlag(%d)--Failed\n", handle);
			return ;
		}
		it->second->SetStopFlag(bStop);
	}

	bool ParticleManager::InsertPslTexture(std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture, const std::wstring &pslName)
	{
		PSTEXTUREINFOSTRUCT *ppslTextureInfo = new PSTEXTUREINFOSTRUCT(m_pRenderer);
		int cnt = vectorD3DTexture.size();
		if(cnt<1)
			return false;

		int nVersion = (*(m_mapPSL[pslName]))->m_nVersion;

		if( cnt>1 && nVersion>=4 )
		{//大于等于版本4 && cnt>1 这种情况属于逻辑错误...  
			XPLOG_PSMHZX(L"ParticleManager::InsertPslTexture(textureNum:%d, nVesion:%d)--Failed\n", cnt, nVersion);
			return false;
		}

		if( cnt>1 && nVersion<4 ) 
		{
			if( !GenPSTextureInfo(vectorD3DTexture, ppslTextureInfo) )
			{
				delete ppslTextureInfo;
				m_nLastError = PSLLOADERROR_TEXTURE;

				if( !(m_logName.empty()) )
					XPLOG_PSMHZX(L"ParticleManager::EnsureParticleSystemResouce()-- GenPSTextureInfo()-FAILED \n");
				return false;
			}
		}
		else
		{
			PictureInfo picinfo;
			if( !m_pRenderer->GetPictureInfo(vectorD3DTexture[0]->handle, picinfo) )
				return false;

			ppslTextureInfo->PsTextureInfo.handle = vectorD3DTexture[0]->handle;
			ppslTextureInfo->dataSize = vectorD3DTexture[0]->dataSize;

			ppslTextureInfo->PsTextureInfo.m_nTexWidth = picinfo.m_nPicWidth;
			ppslTextureInfo->PsTextureInfo.m_nTexHeight = picinfo.m_nPicHeight;

			if(nVersion<4)
			{
				ppslTextureInfo->PsTextureInfo.m_nTexNum = 1;
				ppslTextureInfo->PsTextureInfo.m_nPslTexCol = 1;
				ppslTextureInfo->PsTextureInfo.m_nPslTexRow = 1;
			}
			else
			{
				ppslTextureInfo->PsTextureInfo.m_nTexNum = (*(m_mapPSL[pslName]))->m_nTextureNum;
				ppslTextureInfo->PsTextureInfo.m_nPslTexCol = (*(m_mapPSL[pslName]))->m_nColNum;
				ppslTextureInfo->PsTextureInfo.m_nPslTexRow = (*(m_mapPSL[pslName]))->m_nRowNum;
				int nCol = (*(m_mapPSL[pslName]))->m_nColNum;
				int nRow = (*(m_mapPSL[pslName]))->m_nRowNum;
				ppslTextureInfo->PsTextureInfo.m_nTexWidth /= nCol;
				ppslTextureInfo->PsTextureInfo.m_nTexHeight /= nRow;
			}
		}

		XHardRef<PSTEXTUREINFOSTRUCT> *pRefTextureInfo = new XHardRef<PSTEXTUREINFOSTRUCT>(ppslTextureInfo);
		m_fCurResourceSize += ppslTextureInfo->dataSize;
		m_mapPslTexture[pslName] = pRefTextureInfo;
		return true;
	}

	XPEffectAssureResResult ParticleManager::EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async, ParticleLoadingNotify* pNotify)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::EnsureParticleSystemResouce(%d)--Failed\n", handle);
			return XPEARR_LOAD_ERROR;
		}

		CParticleSystem* particleSys = it->second;
		CGeneralParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CGeneralParticleSystem*>(particleSys) )
		{
			if(pPS->GetLoadedResFlag())
			{
				return XPEARR_RESREADY;
			}
			else
			{
				std::vector<XHardRef<PSPICHANDLESTRUCT>> vectorD3DTexture;
				XHardRef<PSL> ppsl = particleSys->GetPSL();
				std::wstring strTextureFileName;
				int cnt = (int)ppsl->m_vectorTexturePath.size();
				XHardRef<PSPICHANDLESTRUCT>* RefHandle = NULL;

				//加载资源
				std::wstring pslName = particleSys->GetPslName();
				std::map<std::wstring, XHardRef<PSTEXTUREINFOSTRUCT>*>::const_iterator it = m_mapPslTexture.find(pslName);
				if(it == m_mapPslTexture.end())
				{ 
					if(!async) //同步读取资源
					{
						PSPICHANDLESTRUCT* PicHandle = NULL;
						for(int i=0; i<cnt; i++)
						{
							strTextureFileName = ppsl->m_vectorTexturePath[i];
							std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::const_iterator it = m_mapD3d9Texture.find(strTextureFileName);
							if(it == m_mapD3d9Texture.end())
							{
								//读文件加载纹理
								PicHandle = new PSPICHANDLESTRUCT(m_pRenderer);
								bool bres = LoadTextureFromFile(strTextureFileName, PicHandle);
								if(!bres)
								{
									delete PicHandle;
									m_nLastError = PSLLOADERROR_TEXTURE;

									if( !(m_logName.empty()) )
										XPLOG_PSMHZX(L"ParticleManager::EnsureParticleSystemResouce()-- LoadTextureFromFile()-FAILED, fileName: %s\n", strTextureFileName.c_str());
									return XPEARR_LOAD_ERROR;
								}
								PicHandle->dwTime = GetTickCount()/1000;
								RefHandle = new XHardRef<PSPICHANDLESTRUCT>(PicHandle);
								m_mapD3d9Texture.insert( std::make_pair(strTextureFileName, RefHandle) );

								m_fCurResourceSize += PicHandle->dataSize;
							}
							else
							{
								RefHandle = it->second;
								(*RefHandle)->dwTime = GetTickCount()/1000;
							}
							vectorD3DTexture.push_back(*RefHandle);
						}
						if (!InsertPslTexture(vectorD3DTexture, pslName))
						{
							return XPEARR_LOAD_ERROR;
						}
						pPS->SetPicHandleRefResource(vectorD3DTexture);
						SetParticleSysResouce(pPS, m_mapPslTexture[pslName]);
						return XPEARR_RESREADY;
					}
					else //异步读取资源// if(!async) //同步读取资源
					{ //检查提交任务列表
						pPS->m_restTexRes = 0;
						for(int i=0; i<cnt; i++)
						{
							strTextureFileName = ppsl->m_vectorTexturePath[i];
							std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::const_iterator it = m_mapD3d9Texture.find(strTextureFileName);
							if(it == m_mapD3d9Texture.end())
							{ //资源还没有加载进来...
								//检查任务是否需要提交
								if( !IsLoading(strTextureFileName) )
								{
									std::wstring filename;
									filename = strTextureFileName;
									std::transform( filename.begin(), filename.end(), filename.begin(), ::towlower);

									std::wstring subStr = L"\\";
									std::wstring ReplaceStr = L"/";
									std::wstring inStr = filename;

									filename = replace( inStr, subStr, ReplaceStr);
									filename = m_strPath + L"texture/" + filename;

									ParticleSysResourceReadTask* pTask = new ParticleSysResourceReadTask(filename, strTextureFileName, (CFileIOManager*)m_pFileIOMan, this);
									pTask->Submit();
								}
								InsertLoadingUrl(strTextureFileName, pPS);
								++(pPS->m_restTexRes);
							}
						}
						if (pPS->m_restTexRes == 0)// 所有的小图都加载好了，但是还没有大图
						{
							for(int i=0; i<cnt; i++)
							{
								std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::iterator pit = m_mapD3d9Texture.find(ppsl->m_vectorTexturePath[i]);
								RefHandle = pit->second;
								(*RefHandle)->dwTime = GetTickCount()/1000;
								vectorD3DTexture.push_back(*RefHandle);
							}
							if (!InsertPslTexture(vectorD3DTexture, pslName))
							{
								//加载失败了，通知ParticleEffect吧
								return XPEARR_LOAD_ERROR;
							}
							pPS->SetPicHandleRefResource(vectorD3DTexture);
							SetParticleSysResouce(pPS, m_mapPslTexture[pslName]);
							return XPEARR_RESREADY;
						}
						XPASSERT(pNotify && L"pNotify不能为空");
						pPS->SetParticleLoadingNotify(pNotify);
						return XPEARR_LOADING;
					}//if(!async) //同步读取资源
				}
				else
				{
					for(int i=0; i<cnt; i++)
					{
						strTextureFileName = ppsl->m_vectorTexturePath[i];
						std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::const_iterator iter = m_mapD3d9Texture.find(strTextureFileName);
						
						if(iter == m_mapD3d9Texture.end())
						{//
							std::wstring pslFileName = pPS->GetPslName();
							XPLOG_ERROR(L"(%s)对应的大纹理存在，小纹理(%s)不存在。逻辑错误！", pslFileName.c_str(), strTextureFileName.c_str());
							continue;
						}
						RefHandle = iter->second;
						(*RefHandle)->dwTime = GetTickCount()/1000;
						vectorD3DTexture.push_back(*RefHandle);
					}
					pPS->SetPicHandleRefResource(vectorD3DTexture);
					SetParticleSysResouce(pPS, it->second);
					return XPEARR_RESREADY;
				}
			}//if(pPS->GetLoadedResFlag())
		}
		else
		{
			//特殊粒子特效不需要EnsureParticleSystemResouce........
			return XPEARR_RESREADY;
		}
		//这里不需要return了，所有的if都有else并且return了
		//return XPEARR_LOAD_ERROR;
	}

	bool ParticleManager::UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta) //fTimeDelta单位：秒
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::UpdateParticleSystem(%d)--Failed\n", handle);
			return false;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();

		particleSys->SetViewPort(m_ViewPort);

		if( ppsl->m_nPSConfigReq > m_nComputerConfig)
			return false; //配置不符合要求..跳过该粒子

		if(m_bAutoGCFlag)
		{
			m_fgcTime += fTimeDelta;
			if( m_fgcTime > 10*60 )
			{
				GarbageCollect();
				m_fgcTime = 0.0f;
			}
		}

		return particleSys->Update(fTimeDelta);
	}

	XPEFFECT_BIND_TYPE ParticleManager::GetEffectBindType(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetSysLife(%d)--Failed\n", handle);
			return XPEBT_ORIGIN;
		}
		return it->second->GetEffectBindType();
	}

	bool ParticleManager::InsertParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetParticleSystemInfo(%d)--Failed\n", handle);
			return false;
		}

		it->second->InsertParticleNotify(pParticleNotify);
		return true;
	}

	bool ParticleManager::RemoveParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if ( m_mapParticleSystem.end() == it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetParticleSystemInfo(%d)--Failed\n", handle);
			return false;
		}

		it->second->RemoveParticleNotify(pParticleNotify);
		return true;
	}

	void ParticleManager::Init()
	{
	}

	void ParticleManager::Destroy()
	{
		for(std::map<ParticleSystemHandle, CParticleSystem*>::iterator it = m_mapParticleSystem.begin(), ie = m_mapParticleSystem.end(); it != ie; ++it)
		{
			XPSAFE_DELETE(it->second);
		}
		m_mapParticleSystem.clear();

		for(std::map<std::wstring, XHardRef<PSL>*>::iterator it = m_mapPSL.begin(), ie = m_mapPSL.end(); it != ie; ++it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::Destroy()--PSL-HardRef: %d\n", it->second->GetHardRefCount());
			XPSAFE_DELETE(it->second);
		}
		m_mapPSL.clear();

		for(std::map<std::wstring, std::vector<POINT>>::iterator it = m_mapPath.begin(), ie = m_mapPath.end(); it != ie; ++it)
		{
			it->second.clear();
		}
		m_mapPath.clear();

		for(std::map<std::wstring, SHAPE_LIST>::iterator it = m_mapShapeList.begin(), ie = m_mapShapeList.end(); it != ie; ++it)
		{
			it->second.clear();
		}
		m_mapShapeList.clear();

		for(std::map<std::wstring, XHardRef<PSTEXTUREINFOSTRUCT>*>::iterator it = m_mapPslTexture.begin(), ie = m_mapPslTexture.end(); it != ie; ++it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::Destroy()--TEXTURESTRUCT--HardRef: %d\n", it->second->GetHardRefCount());

			XPSAFE_DELETE(it->second);
		}
		m_mapPslTexture.clear();

		for(std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::iterator it = m_mapD3d9Texture.begin(), ie = m_mapD3d9Texture.end(); it != ie; ++it)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::Destroy()--PSPICHANDLESTRUCT--HardRef: %d\n", it->second->GetHardRefCount());

			XPSAFE_DELETE(it->second);
		}
		m_mapD3d9Texture.clear();

		m_psHandleSeed = 1;
	}

	void ParticleManager::SetParticlePath(const std::wstring& pslPath)
	{
		m_strPath = pslPath;
		if (*m_strPath.rbegin() != '/')
		{
			m_strPath = m_strPath + L"/";
		}
	}

	PSL_ERRORTYPE ParticleManager::GetLastError() const
	{
		return m_nLastError;
	}

	void ParticleManager::SetSystemConfig(XPCOMPUTERCONFIGTYPE nParam)
	{//设置机子配置--高2 中1 低0
		m_nComputerConfig = nParam;
	}

	bool ParticleManager::SetSpecialPsTexture( ParticleSystemHandle handle, PictureHandle pShapesTexture, float fu, float fv)
	{		
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetShapePsTexture(%d)--Failed\n", handle);
			return false;
		}

		CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CSpecialParticleSystem*>(it->second) )
			pPS->SetShapePsTexture(pShapesTexture, fu, fv);

		return false;		
	}

	bool ParticleManager::SetSpecialPsScale( ParticleSystemHandle handle, float fcx, float fcy)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetShapeScale(%d)--Failed\n", handle);
			return false;
		}

		CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CSpecialParticleSystem*>(it->second) )
		{
			pPS->SetShapePsScale(fcx, fcy);
			return true;
		}

		return false;
	}

	void ParticleManager::SetSpecialPsVertexs(ParticleSystemHandle handle, const SHAPE_LIST& shapelist)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetShapePsTexture(%d)--Failed\n", handle);
			return;
		}

		CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CSpecialParticleSystem*>(it->second) )
			pPS->SetShapePsVertexs(shapelist);
	}

	bool ParticleManager::SetSilentTime(ParticleSystemHandle handle, float ftime)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetSilentTime(%d)----Failed\n", handle);
			return false;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();
		if(ppsl->m_nAbsrtactPsl != 1)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetSilentTime(%d)--非转场特效--Failed\n", handle);

			return false;
		}

		CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CSpecialParticleSystem*>(particleSys) )
		{
			pPS->SetSilentTime(ftime);
			return true;
		}

		return false;
	}

	PictureHandle ParticleManager::GetSpecialPSTextureHandle(ParticleSystemHandle handle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetShapePSTextureHandle(%d)--Failed\n", handle);

			return INVALID_PICTURE_HANDLE;
		}

		CParticleSystem* particleSys = it->second;
		XHardRef<PSL> ppsl = particleSys->GetPSL();
		if(ppsl->m_nAbsrtactPsl != 1)
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::GetShapePSTextureHandle(%d)--非转场特效--Failed\n", handle);

			return INVALID_PICTURE_HANDLE;
		}

		CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CSpecialParticleSystem*>(particleSys) )
			return pPS->GetSpecialPSTextureHandle();

		return INVALID_PICTURE_HANDLE;
	}

	void ParticleManager::SetGCParam(float fMaxSize, float fTime)
	{
		m_fMaxSize = fMaxSize;
		m_fLastTime = fTime; 
	}

	//09-10-27
	//DeleteParticleSystem(handle) 外部释放CParticleSystem*
	//gc()释放超额的纹理资源和粒子
	//路径、顶点只有在关闭客户端的时候会释放..
	//
	void ParticleManager::GarbageCollect()
	{//M
		if(m_fCurResourceSize < m_fMaxSize)
			return; //不用释放

		if( !(m_logName.empty()) )
			XPLOG_PSMHZX(L"ParticleManager::GarbageCollect()\n");

		DWORD dwCurTime = GetTickCount()/1000;
		DWORD dwCriticalTime = (DWORD)(m_fLastTime*60);
		//最近一次使用是m_fLastTime分钟以前的资源并且已经没有特效在使用的才释放

		std::map<std::wstring, XHardRef<PSTEXTUREINFOSTRUCT>*>::iterator it = m_mapPslTexture.begin();
		while(it != m_mapPslTexture.end()) //大纹理
		{
			DWORD time = (*(it->second))->dwTime;
			if( (it->second->GetHardRefCount() == 1) && (dwCurTime-time > dwCriticalTime) )
			{
				m_fCurResourceSize -= (*(it->second))->dataSize;

				XPSAFE_DELETE(it->second);
				it = m_mapPslTexture.erase(it); //m_mapPslTexture.erase(it++);
			}
			else
				++it;
		}

		std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::iterator itPicHandle = m_mapD3d9Texture.begin();
		while(itPicHandle != m_mapD3d9Texture.end()) //小纹理
		{
			DWORD time = (*(itPicHandle->second))->dwTime;
			if( (itPicHandle->second->GetHardRefCount() == 1) && (dwCurTime-time > dwCriticalTime) )
			{
				m_fCurResourceSize -= (*(itPicHandle->second))->dataSize;

				XPSAFE_DELETE(itPicHandle->second);
				m_mapD3d9Texture.erase(itPicHandle++);
			}
			else
				++itPicHandle;
		}

		std::map<std::wstring, XHardRef<PSL>*>::iterator itPsl = m_mapPSL.begin();
		while(itPsl != m_mapPSL.end())
		{
		//	DWORD time = (*(itPsl->second))->dwTime; //粒子访问时间目前没有记录...
			if( (itPsl->second->GetHardRefCount() == 1) )// && (dwCurTime-time > dwCriticalTime)
			{
				m_fCurResourceSize -= (float)sizeof(PSL)/(float)(1024*1024);
				XPSAFE_DELETE(itPsl->second);
				m_mapPSL.erase(itPsl++);
			}
			else
				++itPsl;
		}
	}

	void ParticleManager::SetLogPath(const std::wstring& logPath)
	{
		std::wstring str = logPath;
		if(str.empty())
			m_logName = L"";
		else
		{
			if(*str.rbegin() != L'\\') //str[str.length()-1]
				str += L"\\";

			time_t timenow = time( NULL);
			struct tm times;
			localtime_s( &times, &timenow);
			TCHAR buffer[15];
			swprintf_s( buffer, L"_%04d-%02d-%02d", times.tm_year + 1900, times.tm_mon + 1, times.tm_mday);
			
			std::wstring strTime = buffer;
			m_logName = str + L"psParamInfo" + strTime + L".log";
		}
	}

	bool ParticleManager::SetKeyPointIndex(ParticleSystemHandle handle, int nIndex)
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(handle);
		if( m_mapParticleSystem.end() == it )
		{
			if( !(m_logName.empty()) )
				XPLOG_PSMHZX(L"ParticleManager::SetKeyPointIndex(%d)--Failed\n", handle);
			return false;
		}

		CGeneralParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<CGeneralParticleSystem*>(it->second) )
		{
			return pPS->SetKeyPointIndex(nIndex);
		}
		return false;
	}

	bool ParticleManager::OnResetDevice()
	{
		XDisplayMode dismode = m_pRenderer->GetDisplayMode();
		if(m_nBitsPixel != dismode.bitperpix)
		{//16位--32位切换时要重建缓存中的小纹理图片资源---
			m_nBitsPixel = dismode.bitperpix;

//			std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*> m_mapD3d9Texture; //图片名---小纹理
			//释放资源
			std::vector<std::wstring> textureFiles;

			for(std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*>::iterator it = m_mapD3d9Texture.begin(), ie = m_mapD3d9Texture.end(); it != ie; ++it)
			{
				XPLOG_INFO(L"ParticleManager::OnResetDevice()--PSPICHANDLESTRUCT--HardRef: %d\n", it->second->GetHardRefCount());

				m_fCurResourceSize -= (*(it->second))->dataSize;

				XPSAFE_DELETE(it->second);

				textureFiles.push_back(it->first);
			}
			m_mapD3d9Texture.clear();

			//
			//重新加载资源
			int cnt = (int)(textureFiles.size());
			for(int i=0; i<cnt; i++)
			{
				XHardRef<PSPICHANDLESTRUCT>* RefHandle = NULL;
				PSPICHANDLESTRUCT* PicHandle = NULL;
				//读文件加载纹理
				PicHandle = new PSPICHANDLESTRUCT(m_pRenderer);
				bool bres = LoadTextureFromFile(textureFiles[i], PicHandle);
				if(!bres)
				{
					delete PicHandle;
					m_nLastError = PSLLOADERROR_TEXTURE;

					XPLOG_ERROR(L"ParticleManager::EnsureParticleSystemResouce()-- LoadTextureFromFile()-FAILED, fileName: %s\n", textureFiles[i].c_str());
					return false;
				}
				PicHandle->dwTime = GetTickCount()/1000;
				RefHandle = new XHardRef<PSPICHANDLESTRUCT>(PicHandle);
				m_mapD3d9Texture.insert( std::make_pair(textureFiles[i], RefHandle) );

				m_fCurResourceSize += PicHandle->dataSize;
			}
			//
		}

		return true;
	}


	//编辑器专用..以下
	CParticleSystem* ParticleManager::GetParticleSysByHandle(ParticleSystemHandle hHandle) const
	{
		std::map<ParticleSystemHandle, CParticleSystem*>::const_iterator it = m_mapParticleSystem.find(hHandle);
		if ( m_mapParticleSystem.end() == it)
		{//INVALID_PS_HANDLE
			return NULL;
		}
		return it->second;
	}

	void ParticleManager::ClearAllResouce()
	{
		Destroy();
	}
	//以上
};