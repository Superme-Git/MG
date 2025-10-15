#include "../common/nulog.h"
#include "nueffectclip.h"
#include "nueffectmanager.h"
#include "nuxapeffect.h"

#include "../../dependencies/LJXML//Include/LJXML.hpp"

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


	void ToolsKPVector::Load(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		LJXML::LJXML_NodeList kpNodeList;
		pNode->GetSubNodeList(kpNodeList);

		AbstractEffectClip::KeyPoint kp;
		for (size_t i = 0; i < kpNodeList.size(); i++)
		{
			if (kpNodeList[i]->type() != rapidxml::node_element)
				continue;

			if (std::wstring(kpNodeList[i]->name()).compare(L"t_kp") != 0)
				continue;

			kp.point[0].x = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_x0")->value());
			kp.point[0].y = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_y0")->value());
			kp.point[1].x = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_x1")->value());
			kp.point[1].y = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_y1")->value());
			kp.point[2].x = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_x2")->value());
			kp.point[2].y = (float)_wtof(kpNodeList[i]->first_attribute(L"kp_y2")->value());

			kp.t = _wtoi(kpNodeList[i]->first_attribute(L"t")->value());

			push_back( kp);
		}
	}

	void ToolsSPVector::Load(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		LJXML::LJXML_NodeList kpNodeList;
		pNode->GetSubNodeList(kpNodeList);

		AbstractEffectClip::SamplePoint sp;
		for (size_t i = 0; i < kpNodeList.size(); i++)
		{
			if (kpNodeList[i]->type() != rapidxml::node_element)
				continue;

			if (std::wstring(kpNodeList[i]->name()).compare(L"t_sp") != 0)
				continue;

			sp.id = (float)_wtoi(kpNodeList[i]->first_attribute(L"id")->value());
			sp.scalar = (float)_wtof(kpNodeList[i]->first_attribute(L"scalar")->value());

			push_back( sp);
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

	int EffectClip::Load(LJXML::LJXML_Node<LJXML::Char>* pNode, EngineBase* pEB, int flag/* = 0*/)
	{
		Release();

		m_startFrame = _wtoi(pNode->first_attribute(L"s_f")->value());
		m_endFrame = _wtoi(pNode->first_attribute(L"e_f")->value());
		m_layer = _wtoi(pNode->first_attribute(L"layer")->value());
		if ( m_layer < 0)
			m_layer = 0;
		if ( m_layer >= GEffect::MAX_CLIP_LAYER)
			m_layer = GEffect::MAX_CLIP_LAYER - 1;

		if (std::wstring(pNode->name()).compare(L"clip") == 0)	//普通特效，不是绑定文件
		{
			std::wstring strRes = pNode->first_attribute(L"r_f")->value();
			if (strRes.empty())
				return ET_LOAD_ATTRIBUTE_FAILD;

			XPASSERT(pEB);
			m_ani = pEB->GetEffectManager()->CreateEffect(strRes, false);
			if ( !m_ani)
				return ET_LOAD_NO_RES;	//这里对ani不存在无效，因为ani不存在的话就会load一个Error的图片进去，去掉error.ani就好使了

			if (m_ani->LoadAttribute(pNode))
				return ET_LOAD_RES_FAILD;
		}

		LJXML::LJXML_NodeList keyNodeList;
		pNode->GetSubNodeList(keyNodeList);
		for (size_t i = 0; i < keyNodeList.size(); i++)
		{
			if (keyNodeList[i]->type() != rapidxml::node_element)
				continue;

			std::wstring keyName = keyNodeList[i]->name();//keyNode.GetName();
			if (keyName == KEY_NAMES[KT_POS])
				m_posTable << keyNodeList[i];
			else if (keyName == KEY_NAMES[KT_ALPHA])
				m_alphaTable << keyNodeList[i];
			else if (keyName == KEY_NAMES[KT_SCALE])
				m_scaleTable << keyNodeList[i];
			else if (keyName == KEY_NAMES[KT_ROTATION])
				m_rotationTable << keyNodeList[i];
			else if (keyName == KEY_NAMES[KT_COLOR])
				m_colorTable << keyNodeList[i];
			else if (keyName == KEY_NAMES[KT_DIVIDE])
				m_divideTable << keyNodeList[i];

			// 编辑器数据
			else if (flag & EFFECT_IO_EDIT && keyName == TOOLS_DATA)
			{
				LJXML::LJXML_NodeList toolsKeyNodeList;
				keyNodeList[i]->GetSubNodeList(toolsKeyNodeList);
				for (size_t j = 0; j < toolsKeyNodeList.size() - 1; j++)
				{
					if (toolsKeyNodeList[j]->type() != rapidxml::node_element)
						continue;

					std::wstring keyName2 = toolsKeyNodeList[j]->name();

					int ValSide = KEYNAME::MAX_KEY_NAME;
					for (AbstractEffectClip::KeyType kt = KT_POS; kt < ValSide; kt = (AbstractEffectClip::KeyType)((int)kt + 1))
					{
						if (keyName2 == KEY_NAMES[kt])
						{
							m_toolsKeyPoints[kt].Load(toolsKeyNodeList[j]);
							m_toolsSamplePoints[kt].Load(toolsKeyNodeList[j]);
							break;
						}
					}
				}

			}

		}

		return ET_S_OK;
	}

	int EffectClip::Update( float curFrame, DWORD tickTime, const NuclearFPoint& parentPos, const NuclearFPoint& parentScale, 
		const float& parentRotation, const NuclearVector4& parentColor,  NuclearDirection dir)
	{
		m_isValid = false;
		if ( !m_ani)
			return -1;

		float startFrame = (float)m_startFrame;
		float endFrame = (float)m_endFrame;

		if ( curFrame < startFrame)
			return 0;

		Nuclear_EffectState as = m_ani->GetPlayState();
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
		NuclearVector4 controlColor( 1.0f, 1.0f, 1.0f, 1.0f);

		if ( m_control)
		{
			NuclearFPoint pos;
			if ( m_control->GetPos( pos))
			{
				m_ani->SetTranslation(pos);
				posNoControl = false;
			}

			NuclearFPoint scale;
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

			NuclearColor clr;
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
			NuclearFPoint pos( parentPos);

			if ( !m_posTable.Empty())
			{
				//TODO 优化起始pos
				NuclearFPoint keyPos = m_posTable.Interpolation( 0, curFrame - m_startFrame);
				keyPos.x *= parentScale.x;
				keyPos.y *= parentScale.y;
				pos += keyPos;
			}

			m_ani->SetTranslation(pos);
		}

		// 更新缩放
		if ( scaleNoControl)
		{
			NuclearFPoint scale( parentScale);

			if ( !m_scaleTable.Empty())
			{
				NuclearFPoint keyScale = m_scaleTable.Interpolation( 0, curFrame - m_startFrame);
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
		NuclearFPoint trans = m_ani->GetTranslation() + m_ani->GetLocation() - parentPos;
		trans.x *= parentScale.x;
		trans.y *= parentScale.y;
		NuclearFPoint newtrans;
		NuclearMatrix3 mat;
		XPMatrixRotationOrigin(&mat, parentRotation);
		XPVec3TransformNormal(&newtrans, &trans, &mat);
		m_ani->SetTranslation(parentPos + newtrans - m_ani->GetLocation());

		// 更新颜色
		{
			NuclearVector4 color = parentColor;

			if ( colorNoControl)
			{
				if ( !m_colorTable.Empty())
				{
					NuclearVector4 keyColor = m_colorTable.Interpolation( 0, curFrame - m_startFrame);
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


	void EffectClip::Render(Canvas* canvas, bool realRender)
	{
		if ( !m_isValid)
			return;
		if ( m_ani)
		{
			m_ani->Render(canvas, realRender);
		}
	}

	void EffectClip::Clone(EffectClip* p, EngineBase* pEB, int flag)
	{
		p->Release();
		p->SetStartFrame(m_startFrame);
		p->SetEndFrame(m_endFrame);
		p->SetLayer(m_layer);

		if (m_ani)
		{
			std::wstring strName = m_ani->GetName();
			Effect* dstAni = pEB->GetEffectManager()->CreateEffect(strName, false);
			m_ani->Clone(dstAni);
			p->SetAnimation(dstAni);
		}

		NuclearFPoint v;
		int i = 0, nKeyCount = 0, frame = 0;

		nKeyCount = GetKeyCount(AbstractEffectClip::KT_POS);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetPosKeyValue(i, frame, v);
			p->AddPosKey(frame, v);
		}

		nKeyCount = GetKeyCount(AbstractEffectClip::KT_SCALE);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetScaleKeyValue(i, frame, v);
			p->AddScaleKey(frame, v);
		}

		float f = 0.0f;
		nKeyCount = GetKeyCount(AbstractEffectClip::KT_ROTATION);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetRotationKeyValue(i, frame, f);
			p->AddRotationKey(frame, f);
		}

		nKeyCount = GetKeyCount(AbstractEffectClip::KT_ALPHA);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetAlphaKeyValue(i, frame, f);
			p->AddAlphaKey(frame, f);
		}

		NuclearColor vC;
		nKeyCount = GetKeyCount(AbstractEffectClip::KT_COLOR);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetColorKeyValue(i, frame, vC);
			p->AddColorKey(frame, vC);
		}

		nKeyCount = GetKeyCount(AbstractEffectClip::KT_DIVIDE);
		for (i = 0; i < nKeyCount; ++i)
		{
			GetDivideKeyValue(i, frame, f);
			p->AddDivideKey(frame, f);
		}
	}
}
