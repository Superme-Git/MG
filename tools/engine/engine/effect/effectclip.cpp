#include "stdafx.h"
#include "..\common\log.h"
#include "effectclip.h"
#include "effectmanager.h"

namespace Nuclear
{
	const wchar_t EffectClip::KEY_NAMES[EffectClip::MAX_KEY_NAME][EffectClip::KEY_NAME_LEN] =
	{
		L"pos_keys",
		L"scale_keys",
		L"rotation_keys",
		L"alpha_keys",
		L"color_keys",
		L"divide_keys"
	};


	static const wchar_t TOOLS_DATA[]		= L"TOOLS_DATA";


	void ToolsKPVector::Load( XMLIO::CINode& node)
	{
		XMLIO::CNodeList kpNodeList;
		node.GetChildren( kpNodeList);

		AbstractEffectClip::KeyPoint kp;
		for ( XMLIO::CNodeList::iterator i = kpNodeList.begin(), e = kpNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& kpNode = *i;
			if (kpNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if ( kpNode.GetName() != L"t_kp")
				continue;

			kp.point[0].x = (float)kpNode.GetFloatAttribute( L"kp_x0");
			kp.point[0].y = (float)kpNode.GetFloatAttribute( L"kp_y0");
			kp.point[1].x = (float)kpNode.GetFloatAttribute( L"kp_x1");
			kp.point[1].y = (float)kpNode.GetFloatAttribute( L"kp_y1");
			kp.point[2].x = (float)kpNode.GetFloatAttribute( L"kp_x2");
			kp.point[2].y = (float)kpNode.GetFloatAttribute( L"kp_y2");

			kp.t = kpNode.GetIntAttribute( L"t");

			push_back( kp);
		}
	}

	void ToolsKPVector::Save( XMLIO::CONode& node)
	{
		for ( iterator i = begin(), e = end(); i != e; ++i)
		{
			const AbstractEffectClip::KeyPoint& kp = *i;

			XMLIO::CONode kpNode;
			node.CreateSubElement( L"t_kp", kpNode);

			kpNode.SetAttribute( L"kp_x0", kp.point[0].x);
			kpNode.SetAttribute( L"kp_y0", kp.point[0].y);
			kpNode.SetAttribute( L"kp_x1", kp.point[1].x);
			kpNode.SetAttribute( L"kp_y1", kp.point[1].y);
			kpNode.SetAttribute( L"kp_x2", kp.point[2].x);
			kpNode.SetAttribute( L"kp_y2", kp.point[2].y);

			kpNode.SetAttribute( L"t", kp.t);
		}
	}




	void ToolsSPVector::Load( XMLIO::CINode& node)
	{
		XMLIO::CNodeList kpNodeList;
		node.GetChildren( kpNodeList);

		AbstractEffectClip::SamplePoint sp;
		for ( XMLIO::CNodeList::iterator i = kpNodeList.begin(), e = kpNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& spNode = *i;
			if (spNode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if ( spNode.GetName() != L"t_sp")
				continue;

			sp.id = spNode.GetIntAttribute( L"id");
			sp.scalar = (float)spNode.GetFloatAttribute( L"scalar");

			push_back( sp);
		}
	}

	void ToolsSPVector::Save( XMLIO::CONode& node)
	{
		for ( iterator i = begin(), e = end(); i != e; ++i)
		{
			const AbstractEffectClip::SamplePoint& sp = *i;

			XMLIO::CONode spNode;
			node.CreateSubElement( L"t_sp", spNode);

			spNode.SetAttribute( L"id", sp.id);
			spNode.SetAttribute( L"scalar", sp.scalar);
		}
	}



	EffectClip::EffectClip()
		: m_control( NULL), m_curKeyIndex( 0), m_resetAni( false), m_isValid(false)
	{

	}

	EffectClip::~EffectClip()
	{
		Release();
	}

	void EffectClip::Release()
	{
		XPSAFE_DELETE( m_ani);

		m_curKeyIndex = 0;
		m_isValid = false;

		m_posTable.Clear();
		m_scaleTable.Clear();
		m_rotationTable.Clear();
		m_colorTable.Clear();
		m_alphaTable.Clear();
		m_divideTable.Clear();
	}

	int EffectClip::Load( XMLIO::CINode& node, EngineBase* pEB, int flag/* = 0*/)
	{
		Release();

		m_startFrame = node.GetIntAttribute( L"s_f");
		m_endFrame = node.GetIntAttribute( L"e_f");
		m_layer = node.GetIntAttribute( L"layer");
		if ( m_layer < 0)
			m_layer = 0;
		if ( m_layer >= GEffect::MAX_CLIP_LAYER)
			m_layer = GEffect::MAX_CLIP_LAYER - 1;

		if (node.GetName() == L"clip")	//普通特效，不是绑定文件
		{
			std::wstring strRes;
			if (! node.GetAttribute(L"r_f",strRes) )
				return ET_LOAD_ATTRIBUTE_FAILD;
			XPASSERT(pEB);
			m_ani = pEB->GetEffectManager()->CreateEffect(strRes, false);
			if ( !m_ani)
				return ET_LOAD_NO_RES;

			if (m_ani->LoadAttribute(node))
				return ET_LOAD_RES_FAILD;
		}
		

		XMLIO::CNodeList keyNodeList;
		node.GetChildren( keyNodeList);
		for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& keyNode = *i;
			if (keyNode.GetType() != XMLIO::NT_ELEMENT)
				continue;

			std::wstring keyName = keyNode.GetName();
			if ( keyName == KEY_NAMES[KT_POS])
				m_posTable << &keyNode;
			else if ( keyName == KEY_NAMES[KT_ALPHA])
				m_alphaTable << &keyNode;
			else if ( keyName == KEY_NAMES[KT_SCALE])
				m_scaleTable << &keyNode;
			else if ( keyName == KEY_NAMES[KT_ROTATION])
				m_rotationTable << &keyNode;
			else if ( keyName == KEY_NAMES[KT_COLOR])
				m_colorTable << &keyNode;
			else if ( keyName == KEY_NAMES[KT_DIVIDE])
				m_divideTable << &keyNode;
			// 编辑器数据
			else if ( flag & EFFECT_IO_EDIT && keyName == TOOLS_DATA)
			{
				XMLIO::CNodeList toolsKeyNodeList;
				keyNode.GetChildren( toolsKeyNodeList);
				for ( XMLIO::CNodeList::iterator j = toolsKeyNodeList.begin(), k = toolsKeyNodeList.end(); j != k; ++j)
				{
					XMLIO::CINode& tooksKeyNode = *j;
					if (tooksKeyNode.GetType() != XMLIO::NT_ELEMENT)
						continue;
					std::wstring keyName = tooksKeyNode.GetName();
					for ( AbstractEffectClip::KeyType kt = KT_POS; kt < MAX_KEY_NAME; kt = (AbstractEffectClip::KeyType)((int)kt + 1))
					{
						if ( keyName == KEY_NAMES[kt])
						{
							m_toolsKeyPoints[kt].Load( tooksKeyNode);
							m_toolsSamplePoints[kt].Load( tooksKeyNode);
							break;
						}
					}
				}

			}

		}

		return ET_S_OK;

	}

	template< class KEYTABLE>
	inline void SaveKeyTable( XMLIO::CONode& node, KEYTABLE& table, AbstractEffectClip::KeyType kt)
	{
		if ( table.GetKeyCount())
		{
			XMLIO::CONode keyNode;
			node.CreateSubElement( EffectClip::KEY_NAMES[kt], keyNode);
			table >> &keyNode;
		}
	}

	int EffectClip::Save( XMLIO::CONode& node, int flag/* = 0*/)
	{
		node.SetAttribute( L"s_f", m_startFrame);
		node.SetAttribute( L"e_f", m_endFrame);
		node.SetAttribute( L"layer", m_layer);
		if ( m_ani)
		{
#if _DEBUG
		const std::wstring& strName =  m_ani->GetName();
#endif
			node.SetAttribute( L"r_f", m_ani->GetName());
			int ret = m_ani->SaveAttribute( node);
			if ( ret)
				return ret;
		}

		SaveKeyTable( node, m_posTable, KT_POS);
		SaveKeyTable( node, m_alphaTable, KT_ALPHA);
		SaveKeyTable( node, m_scaleTable, KT_SCALE);
		SaveKeyTable( node, m_rotationTable, KT_ROTATION);
		SaveKeyTable( node, m_colorTable, KT_COLOR);
		SaveKeyTable( node, m_divideTable, KT_DIVIDE);

		// 编辑器数据
		if ( flag & EFFECT_IO_EDIT)
		{
			XMLIO::CONode toolsKeyNode;
			node.CreateSubElement( TOOLS_DATA, toolsKeyNode);
			for ( AbstractEffectClip::KeyType kt = KT_POS; kt < MAX_KEY_NAME; kt = (AbstractEffectClip::KeyType)((int)kt + 1))
			{
				XMLIO::CONode keyNode;
				toolsKeyNode.CreateSubElement( KEY_NAMES[kt], keyNode);
				m_toolsKeyPoints[kt].Save( keyNode);
				m_toolsSamplePoints[kt].Save( keyNode);

			}
		}

		return NO_ERROR;
	}


	int EffectClip::Update( float curFrame, DWORD tickTime, const FPOINT& parentPos, const FPOINT& parentScale, 
		const float& parentRotation, const XPVECTOR4& parentColor,  XPDIRECTION dir)
	{
		m_isValid = false;
		if ( !m_ani)
			return -1;

		float startFrame = (float)m_startFrame;
		float endFrame = (float)m_endFrame;

		if ( curFrame < startFrame)
			return 0;

		XPEffectState as = m_ani->GetPlayState();
		if ( (curFrame > endFrame + 1.0f && m_endFrame > 0) )		// 加上1.0f是因为允许还没更新到下一帧动画继续播放，左闭右闭的问题，真讨厌
		{
			if ( as != XPES_STOP)
				m_ani->Stop();
			return 0;
		}


		//if ( as == XPES_STOP && m_resetAni && tickTime > 0.0f)	//为啥tickTime要>0.0f?
		if ( as == XPES_STOP && m_resetAni)
		{
			//if ( m_ani->GetType() != XPET_AUDIO || curFrame == startFrame)//为啥要不等于AUDIO呢？
			if ( curFrame == startFrame)
				m_ani->Play();
			m_resetAni = false;
		}

		m_curKeyIndex = 0;			// TODO优化
		m_isValid = true;

		bool posNoControl = true;
		bool scaleNoControl = true;
		bool rotateNoControl = true;
		bool colorNoControl = true;
		bool alphaNoControl = true;
		XPVECTOR4 controlColor( 1.0f, 1.0f, 1.0f, 1.0f);

		if ( m_control)
		{
			FPOINT pos;
			if ( m_control->GetPos( pos))
			{
				m_ani->SetTranslation(pos);
				posNoControl = false;
			}

			FPOINT scale;
			if ( m_control->GetScale( scale))
			{
				m_ani->SetScale(scale);
				scaleNoControl = false;
			}

			float radian;
			if ( m_control->GetRotationRadian( radian))
			{
				m_ani->SetRotationRadian(radian);
				rotateNoControl = false;
			}

			XPCOLOR clr;
			if ( m_control->GetColor( clr))
			{
				controlColor = clr.ToXPVECTOR4();
				colorNoControl = false;
			}

			float alpha;
			if ( m_control->GetAlpha( alpha))
			{
				controlColor.w *= alpha;
				alphaNoControl = false;
			}
		}

		// 更新位置
		if ( posNoControl)
		{
			FPOINT pos( parentPos);

			if ( !m_posTable.Empty())
			{
				//TODO 优化起始pos
				FPOINT keyPos = m_posTable.Interpolation( 0, curFrame - m_startFrame);
				keyPos.x *= parentScale.x;
				keyPos.y *= parentScale.y;
				pos += keyPos;
			}

			m_ani->SetTranslation(pos);
		}

		// 更新缩放
		if ( scaleNoControl)
		{
			FPOINT scale( parentScale);

			if ( !m_scaleTable.Empty())
			{
				FPOINT keyScale = m_scaleTable.Interpolation( 0, curFrame - m_startFrame);
				scale.x *= keyScale.x;
				scale.y *= keyScale.y;
			}

			m_ani->SetScale(scale);
		}

		// 更新旋转
		if ( rotateNoControl)
		{
			float rotation( parentRotation);

			if ( !m_rotationTable.Empty())
			{
				float keyAngle = m_rotationTable.Interpolation( 0, curFrame - m_startFrame);
				rotation += keyAngle;
			}

			m_ani->SetRotationRadian( rotation);
		}

		//这里可以优化一下，用得着矩阵么？
		FPOINT trans = m_ani->GetTranslation() + m_ani->GetLocation() - parentPos;
		trans.x *= parentScale.x;
		trans.y *= parentScale.y;
		FPOINT newtrans;
		XPMATRIX3 mat;
		XPMatrixRotationOrigin(&mat, parentRotation);
		XPVec3TransformNormal(&newtrans, &trans, &mat);
		m_ani->SetTranslation(parentPos + newtrans - m_ani->GetLocation());

		


		// 更新颜色
		{
			XPVECTOR4 color = parentColor;

			if ( colorNoControl)
			{
				if ( !m_colorTable.Empty())
				{
					XPVECTOR4 keyColor = m_colorTable.Interpolation( 0, curFrame - m_startFrame);
					color.x *= keyColor.x;
					color.y *= keyColor.y;
					color.z *= keyColor.z;
					color.w *= keyColor.w;
				}
			}
			else
				color = controlColor;


			if ( alphaNoControl)
			{
				if ( !m_alphaTable.Empty())
				{
					float keyAlpha = m_alphaTable.Interpolation( 0, curFrame - m_startFrame);
					color.w *= keyAlpha;
				}
			}
			else
				color.w = controlColor.w;

			m_ani->Colorate(color);
		}

		// 更新分割位置
		if ((m_ani->GetType() == XPET_GEFFECT) && (!m_divideTable.Empty()))
		{
			dynamic_cast<GEffect*>(m_ani)->SetDivideLayer((int)m_divideTable.Interpolation( 0, curFrame - m_startFrame));
		}

		//方向
		m_ani->SetDirection(dir);



		m_ani->Update( tickTime);

		return 0;
	}


	void EffectClip::Render(Canvas* canvas)
	{
		if ( !m_isValid)
			return;
		if ( m_ani)
		{
			m_ani->Render(canvas);
		}
	}
}
