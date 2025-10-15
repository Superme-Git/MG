#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xml.h"
#include "..\common\fileiomanager.h"
#include "..\renderer\renderer.h"
#include "..\engine\animanager.h"
#include "..\engine\configmanager.h"
#include "spineeffect.h"

namespace Nuclear
{
	static const int quadTriangles[6] = {0, 1, 2, 2, 3, 0};

	SpineEffect::SpineEffect(EngineBase *pEB) : Effect(pEB), m_coloration(1.0f, 1.0f, 1.0f, 1.0f), m_nPlayOnceTime(0), 
		m_nFrameTime(0), m_scaleType(XPEST_NOSCALE), m_pHoldAni(NULL), m_bScreenCrood(false), m_bHoldAni(false),m_alpha(1.0f)
	{
		m_defaultActName = "play";
		worldVertices = new float[1000]; // Max number of vertices per mesh.
	}

	SpineEffect::~SpineEffect(void)
	{
		//cancel notify
		if (m_state == XPES_EMPTY)
		{
			m_pEngine->GetAniManager()->RemoveLoadingNotify(L"/effect/" + m_name + SFI_ExtName, this);
		}

		delete [] worldVertices;
		if (m_pHoldAni)
			delete m_pHoldAni;
		if (m_pSkelAnim)
		{
			delete m_pSkelAnim;
		}
	}

	void SpineEffect::OnLoadAniReady(const std::wstring &filename)
	{
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NULL)
		{
			m_state = XPES_EMPTY;
			XPLOG_ERROR(L"加载资源失败：%s\n", filename.c_str());
			NotifyAsyncLoadResult(false);
			return;
		}
		m_state = XPES_STOP;
		if (m_bHoldAni)
		{
			if (m_pHoldAni)
				delete m_pHoldAni;
			m_pHoldAni = new XHardRef<XAni>(hardani);
		}
		Play();
		NotifyAsyncLoadResult(true);
	}

	void SpineEffect::OnLoadAniBaseReady(const std::wstring &filename)
	{
	}

	XPEffectAssureResResult SpineEffect::AssureResource(bool async)
	{
		if( LoadEffect(m_name) == false )
		{
			m_state = XPES_EMPTY;
			XPLOG_ERROR(L"加载资源失败：%s\n", m_name.c_str());
			return XPEARR_LOAD_ERROR;
		}
		m_state = XPES_STOP;
		Play();
		Update(0);
		//包围盒
		float left = 0;
		float right = 0;
		float top = 0;
		float bottom = 0;

		// 获取本地（或称相对）包围盒前，要确保 m_pSkelAnim 位置为 0, 0

		float skelX = m_pSkelAnim->getPositionX();
		float skelY = m_pSkelAnim->getPositionY();
		m_pSkelAnim->setPosition(0, 0);

		m_pSkelAnim->boundingBox(&left,&top,&right,&bottom);

		m_pSkelAnim->setPosition(skelX, skelY);

		m_relBBox.left = left;
		m_relBBox.top = top;
		m_relBBox.right = right;
		m_relBBox.bottom = bottom;
		return XPEARR_RESREADY;
	}
	bool SpineEffect::LoadEffect(const std::wstring &name)
	{
		if (name.empty())
			return false;

		Release();

		std::wstring uri = L"/effect/" + name;

		std::wstring pathSkeletonDataFile = uri + L".json";
		std::wstring pathAtlasFile = uri + SPINE_ExtName;
		std::wstring pathPngFile = uri + L".png";

		CFileIOManager *pFIOMan = m_pEngine->GetFileIOManager();
		Renderer *pRenderer = m_pEngine->GetRenderer();

		XBuffer skeletonDataBuffer;
		XBuffer atlasBuffer;
		XBuffer pngBuffer;

		bool bLoadSkeletonData = pFIOMan->GetFileImage(pathSkeletonDataFile, skeletonDataBuffer);
		bool bLoadAtlas = pFIOMan->GetFileImage(pathAtlasFile, atlasBuffer);
		bool bLoadPng = pFIOMan->GetFileImage(pathPngFile, pngBuffer);
		m_texture = pRenderer->LoadPicture(pathPngFile);

		int pitch = 0;
		int width = 0;
		int height = 0;
		pRenderer->GetPictureInfo(m_texture,pitch,width,height);

		if (bLoadSkeletonData && bLoadAtlas && bLoadPng
			&& skeletonDataBuffer.size() > 0 && atlasBuffer.size() > 0 && pngBuffer.size() > 0)
		{
			m_pSkelAnim = spine::SkeletonAnimation::createWithRawData((const char*)skeletonDataBuffer.constbegin(), skeletonDataBuffer.size(),
				(const char*)atlasBuffer.constbegin(), atlasBuffer.size(), (int*)m_texture, width, height,1);
			if (m_pSkelAnim)
			{
				//特效的持续时间
				m_pSkelAnim->setAnimation(0, m_defaultActName.c_str(), m_playMode == XPPM_LOOP ? true: false);
				m_totoalTimes = int(1000 * m_pSkelAnim->getAnimationDuration(0, m_defaultActName.c_str()));
				m_state = XPES_PLAYING;
				return true;
			}
		}

		return false;
	}

	void SpineEffect::Release()
	{
		if (m_state != XPES_STOP) 
			return;
		m_ani = XWeakRef<XAni>();
		if (m_bHoldAni)
		{
			if (m_pHoldAni)
				delete m_pHoldAni;
			m_pHoldAni = NULL;
			m_bHoldAni = false;
		}
		m_state = XPES_EMPTY;
	}

	void SpineEffect::SetFps(float fps)
	{
		if (fps > 0)
		{
			if (m_state != XPES_EMPTY)
			{
				CheckHardAni();
				XHardRef<XAni> hardani = m_ani;
				m_nPlayOnceTime = static_cast<DWORD>(hardani->GetFrameCount() * 1000 / fps);
			}
			m_nFrameTime = static_cast<DWORD>(1000.0f / fps);
		}
		
	}

	int SpineEffect::LoadAttribute(XMLIO::CINode& node)
	{
		float fps = (float)node.GetFloatAttribute( L"fps");
		SetFps( fps);
		m_pos.x = node.GetIntAttribute( L"rel_x");
		m_pos.y = node.GetIntAttribute( L"rel_y");
		m_vertexColor.data = node.GetUIntAttribute( L"v_c", 0xFFFFFFFF);

		// 播放控制
		m_playMode = (XPPLAY_MODE)node.GetIntAttribute( L"p_m");
		if ( IsLoopNum())
			m_nLoopNum = node.GetIntAttribute( L"l_n" );
		else if ( IsPlayTime())
			m_nPlayTime = node.GetIntAttribute( L"p_t" );

		return 0;
	}

	int SpineEffect::SaveAttribute(XMLIO::CONode& node)
	{
		node.SetAttribute( L"fps", GetFps());
		node.SetAttribute( L"rel_x", m_pos.x);
		node.SetAttribute( L"rel_y", m_pos.y);
		node.SetAttribute( L"v_c", (unsigned int)m_vertexColor.data);

		// 播放控制
		node.SetAttribute( L"p_m", (int)m_playMode);
		if ( IsLoopNum())
			node.SetAttribute( L"l_n", m_nLoopNum);
		else if( IsPlayTime())
			node.SetAttribute( L"p_t", m_nPlayTime);	

		return 0;
	}

	bool SpineEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
		}

		m_nCurTime += tickTime;

		if (m_nCurTime > m_totoalTimes)
		{
			NotifyEnd();
			if (m_state != XPES_PLAYING) return true;
			++m_curLoopNum;
			switch (m_playMode)
			{
			case XPPM_LOOPNUM:
				if (m_curLoopNum >= m_nLoopNum)
				{
					Stop();
					return false;
				}
			case XPPM_LOOP:
				m_nCurTime = 0;
				break;
			default:
				Stop();
				return false;
			}
		}

		if (m_pSkelAnim)
		{
			CRECT vp = m_pEngine->GetViewport();
			m_pSkelAnim->setPosition(m_pos.x - vp.left, m_pos.y - vp.top);
			m_pSkelAnim->update(tickTime*0.001f);
		}
		return true;
	}

	//播放状态控制
	bool SpineEffect::Play()
	{
		if (Effect::Play())
		{
			m_nCurFrame = 0;
			return true;
		}
		return false;
	}

	void SpineEffect::CheckHardAni()
	{
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP )
		{
			//这里是同步的，但是一般来说不会调用到
			m_ani = m_pEngine->GetAniManager()->GetAni(L"/effect/" + m_name + SFI_ExtName, false);
		}
	}

	bool SpineEffect::GetBase(XPBase &base)
	{
		if (m_state==XPES_EMPTY)
			return false;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			return hardani->GetBase(base);
		}
		return false;
	}

	void SpineEffect::SetEffectBindType(XPEFFECT_BIND_TYPE t)
	{
		if (m_state==XPES_EMPTY)
			return;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			hardani->SetEffectBindType(t);
		}
	}
	XPEFFECT_BIND_TYPE SpineEffect::GetEffectBindType() const
	{
		if (m_state==XPES_EMPTY)
			return XPEBT_NULL;
		const_cast<SpineEffect*>(this)->CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			return hardani->GetEffectBindType();
		}
		return XPEBT_NULL;
	}

	void SpineEffect::HoldRes()
	{
		if (m_bHoldAni)
			return;
		m_bHoldAni = true;
		if (m_state==XPES_EMPTY)
			return;
		if (m_pHoldAni)
			delete m_pHoldAni;
		m_pHoldAni = new XHardRef<XAni>(m_ani);
	}

	//! 渲染
	bool SpineEffect::Render(Canvas* canvas)
	{
		if (m_state == XPES_STOP || m_state == XPES_EMPTY)
			return false;
		if (m_pSkelAnim)
		{
			CRECT vp = m_pEngine->GetViewport();

			float left = 0;
			float right = 0;
			float top = 0;
			float bottom = 0;
			m_pSkelAnim->boundingBox(&left,&top,&right,&bottom);
			left += vp.left;
			right += right - left + vp.right;
			top += vp.top;
			bottom += bottom - top + vp.top;
			CRECT boundBox(left, top, right, bottom);

			CRECT resRect;
			//if (vp.IsCross(boundBox, resRect))
			{
				spSkeleton* skeleton = m_pSkelAnim->skeleton;


				DrawPictureParam dpParam;
				dpParam.colorCount = 1;
				XPMATRIX3 resultM, tmpM;
				XPMatrixIdentity(&resultM);

				XPMatrixScaling(&tmpM,1,-1);
				resultM *= tmpM;

				CRECT viewport = m_pEngine->GetViewport();
				if (m_bScreenCrood)
				{
					viewport.left = 0;
					viewport.top = 0;
				}
				if (m_scaleType == XPEST_NOSCALE)
				{
					XPVECTOR2 pos(m_pos.x + m_translate.x - viewport.left, 
						m_pos.y + m_translate.y - viewport.top);
					XPMatrixTranslation(&tmpM, &pos);
				}
				else
				{
					XPVECTOR2 pos((m_pos.x + m_translate.x - viewport.left) * m_pEngine->GetWorldScale(), 
						(m_pos.y + m_translate.y - viewport.top) * m_pEngine->GetWorldScale());
					XPMatrixTranslation(&tmpM, &pos);
				}
				resultM *= tmpM;
				dpParam.pMatrix = &resultM;

				for (int i = 0, n = skeleton->slotsCount; i < n; i++)
				{
					spSlot* slot = skeleton->drawOrder[i];
					if (!slot->attachment) continue;

					const float* uvs = NULL;
					int verticesCount = 0;
					const int* triangles = NULL;
					int trianglesCount = 0;

					switch (slot->attachment->type)
					{
					case SP_ATTACHMENT_REGION: 
						{
							spRegionAttachment* attachment = (spRegionAttachment*)slot->attachment;
							spRegionAttachment_computeWorldVertices(attachment, slot->bone, worldVertices);
							uvs = attachment->uvs;
							verticesCount = 4;
							triangles = quadTriangles;
							trianglesCount = 6;
						}break;
					case SP_ATTACHMENT_MESH: 
						{
							spMeshAttachment* attachment = (spMeshAttachment*)slot->attachment;
							spMeshAttachment_computeWorldVertices(attachment, slot, worldVertices);
							uvs = attachment->uvs;
							verticesCount = attachment->verticesCount;
							triangles = attachment->triangles;
							trianglesCount = attachment->trianglesCount;
						}break;
					case SP_ATTACHMENT_SKINNED_MESH: 
						{
							spSkinnedMeshAttachment* attachment = (spSkinnedMeshAttachment*)slot->attachment;
							spSkinnedMeshAttachment_computeWorldVertices(attachment, slot, worldVertices);
							uvs = attachment->uvs;
							verticesCount = attachment->uvsCount;
							triangles =attachment->triangles;
							trianglesCount = attachment->trianglesCount;
						}break;
					}

					if( m_texture != NULL )
					{						
						XPVECTOR2* vb = new XPVECTOR2[verticesCount];
						WORD* ib = new WORD[trianglesCount];
						TCT1VERTEX* vec = new TCT1VERTEX[verticesCount];

						for( int m = 0; m < verticesCount; m++ )
						{
							vb[m].x = worldVertices[m*2];
							vb[m].y = worldVertices[m*2+1];

							XPVECTOR2 temp;
							XPVec3TransformCoord(&temp, &vb[m],dpParam.pMatrix);
							
							vec[m].x = temp.x;
							vec[m].y = temp.y;
							vec[m].z = 0.5;
							vec[m].u = uvs[m*2];
							vec[m].v = uvs[m*2+1];
							vec[m].rhw = 1.0f;
							vec[m].c = 0xffffffff;
						}
						for( int m = 0; m < trianglesCount; m++ )
						{
							ib[m] = triangles[m];
						}
						m_pEngine->GetRenderer()->DrawIndexVerticeBuffer(m_texture,vec,ib,verticesCount,trianglesCount);

						delete [] vb;
						delete [] ib;
						delete [] vec;
					}
				}
			}
			return true;
		}
		return false;
	}
}