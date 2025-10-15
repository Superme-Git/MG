#ifndef __Nuclear_EFFECTCLIP_H
#define __Nuclear_EFFECTCLIP_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "geffect.h"
#include "effectkeytable.h"

namespace Nuclear
{
	class ToolsKPVector
		: public AbstractEffectClip::KP_VECTOR
	{
	public:
		void Load( XMLIO::CINode& node);
		void Save( XMLIO::CONode& node);
	};


	class ToolsSPVector
		: public AbstractEffectClip::SP_VECTOR
	{
	public:
		void Load( XMLIO::CINode& node);
		void Save( XMLIO::CONode& node);
	};


	class EffectClip
		: public AbstractEffectClip
	{
		friend class GEffect;

	public:
		enum
		{
			KEY_NAME_LEN = 16,
			MAX_KEY_NAME = AbstractEffectClip::KT_DIVIDE + 1,
		};

		enum ERROR_TYPE
		{
			ET_S_OK = 0,
			ET_LOAD_NO_RES = -1,
			ET_LOAD_RES_FAILD = -2,
			ET_LOAD_ATTRIBUTE_FAILD = -3,
			//SAVE_RES_FAILD = -1,
		};

		static const wchar_t KEY_NAMES[MAX_KEY_NAME][KEY_NAME_LEN];

	public:
		EffectClip();
		~EffectClip();

		int Load( XMLIO::CINode& node, EngineBase* pEB, int flag = 0 );
		int Save( XMLIO::CONode& node, int flag = 0 );

		void Release();

	public:
		int		Update( float curFrame, DWORD tickTime, const FPOINT& parentPos, const FPOINT& parentScale, 
			const float& parentRotation, const XPVECTOR4& parentColor, XPDIRECTION dir);

		void	Render( Canvas* canvas );

	protected:
		CPosKeyTable		m_posTable;			//位置
		CScaleKeyTable		m_scaleTable;		//缩放
		CRotationKeyTable	m_rotationTable;	//旋转
		CColorKeyTable		m_colorTable;		//颜色（RGB）
		CAlphaKeyTable		m_alphaTable;		//Alpha
		CDivideKeyTable		m_divideTable;		//人所在的层（只在绑定特效文件里面有）

		static const int	KEY_TABLE_NUM = 6;

		//! 编辑时用的曲线信息
		ToolsKPVector		m_toolsKeyPoints[KEY_TABLE_NUM];
		ToolsSPVector		m_toolsSamplePoints[KEY_TABLE_NUM];

		const IEffectClipControl*	m_control;

	protected:			// 有关运行时变量
		mutable int			m_curKeyIndex;
		bool				m_isValid;

		bool				m_resetAni;

	//protected:
	//	const wchar_t* GetClipName();


	public:
		void ResetAni()
		{
			m_resetAni = true;
		}

		void ToolSetValid( float curFrame)
		{
			if ( curFrame >= (float)m_startFrame)
				m_isValid = true;
		}

		virtual unsigned int	GetKeyCount( AbstractEffectClip::KeyType kt) const
		{
			switch( kt)
			{
			case AbstractEffectClip::KT_POS:
				return m_posTable.GetKeyCount();
				break;
			case AbstractEffectClip::KT_SCALE:
				return m_scaleTable.GetKeyCount();
				break;
			case AbstractEffectClip::KT_ROTATION:
				return m_rotationTable.GetKeyCount();
				break;
			case AbstractEffectClip::KT_ALPHA:
				return m_alphaTable.GetKeyCount();
				break;
			case AbstractEffectClip::KT_COLOR:
				return m_colorTable.GetKeyCount();
				break;
			case AbstractEffectClip::KT_DIVIDE:
				return m_divideTable.GetKeyCount();
				break;
			}

			return 0;
		}

		virtual bool	GetPosKeyValue( unsigned int index, int& frame, FPOINT& v) const
		{
			const CPosKeyValue* key = m_posTable.GetKey( index);
			if ( !key)
				return false;

			frame = (int)key->GetFrame();
			v = key->GetValue();
			return true;
		}

		virtual bool	GetScaleKeyValue( unsigned int index, int& frame, FPOINT& v) const
		{
			const CScaleKeyValue* key = m_scaleTable.GetKey( index);
			if ( !key)
				return false;

			frame = (int)key->GetFrame();
			v = key->GetValue();
			return true;
		}

		virtual bool	GetRotationKeyValue( unsigned int index, int& frame, float& v) const
		{
			const CRotationKeyValue* key = m_rotationTable.GetKey( index);
			if ( !key)
				return false;

			frame = (int)key->GetFrame();
			v = key->GetValue();
			return true;
		}

		virtual bool	GetAlphaKeyValue( unsigned int index, int& frame, float& v) const
		{
			const CAlphaKeyValue* key = m_alphaTable.GetKey( index);
			if ( !key)
				return false;

			frame = (int)key->GetFrame();
			v = key->GetValue();
			return true;
		}

		virtual bool	GetColorKeyValue( unsigned int index, int& frame, XPCOLOR& v) const
		{
			const CColorKeyValue* key = m_colorTable.GetKey( index);
			if ( !key)
				return false;
			frame = (int)key->GetFrame();
			v.Reset(key->GetValue());
			
			return true;
		}

		virtual bool	GetDivideKeyValue( unsigned int index, int& frame, float& v) const
		{
			const CDivideKeyValue* key = m_divideTable.GetKey( index);
			if ( !key)
				return false;

			frame = (int)key->GetFrame();
			v = key->GetValue();
			return true;
		}

		virtual bool	SetPosKeyValue( unsigned int index, const FPOINT& v)
		{
			CPosKeyValue* key = m_posTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue( v);
			return true;
		}

		virtual bool	SetScaleKeyValue( unsigned int index, const FPOINT& v)
		{
			CScaleKeyValue* key = m_scaleTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue( v);
			return true;
		}

		virtual bool	SetRotationKeyValue( unsigned int index, float v)
		{
			CRotationKeyValue* key = m_rotationTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue( v);
			return true;
		}

		virtual bool	SetAlphaKeyValue( unsigned int index, float v)
		{
			CAlphaKeyValue* key = m_alphaTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue( v);
			return true;
		}

		virtual bool	SetColorKeyValue( unsigned int index, const XPCOLOR& v)
		{
			CColorKeyValue* key = m_colorTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue(v.ToXPVECTOR4());

			return true;
		}

		virtual int		GetPosKeyValue( int frame, FPOINT& v) const
		{
			int keyCount = m_posTable.GetKeyCount();
			int loopFrame;
			FPOINT loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetPosKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}

		virtual bool	SetDivideKeyValue( unsigned int index, float v)
		{
			CDivideKeyValue* key = m_divideTable.GetKey( index);
			if ( !key)
				return false;

			key->SetValue( v);
			return true;
		}

		virtual int		GetScaleKeyValue( int frame, FPOINT& v) const
		{
			int keyCount = m_scaleTable.GetKeyCount();
			int loopFrame;
			FPOINT loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetScaleKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}
		virtual int		GetRotationKeyValue( int frame, float& v) const
		{
			int keyCount = m_rotationTable.GetKeyCount();
			int loopFrame;
			float loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetRotationKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}
		virtual int		GetAlphaKeyValue( int frame, float& v) const
		{
			int keyCount = m_alphaTable.GetKeyCount();
			int loopFrame;
			float loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetAlphaKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}
		virtual int		GetColorKeyValue( int frame, XPCOLOR& v) const
		{
			int keyCount = m_colorTable.GetKeyCount();
			int loopFrame;
			XPCOLOR loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetColorKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}

		virtual int		GetDivideKeyValue( int frame, float& v) const
		{
			int keyCount = m_divideTable.GetKeyCount();
			int loopFrame;
			float loopV;
			for ( int i = 0; i < keyCount; ++i)
			{
				if ( GetDivideKeyValue( i, loopFrame, loopV))
				{
					if ( loopFrame == frame)
					{
						v = loopV;
						return i;
					}
				}
			}
			return -1;
		}


		//! 在指定帧增加一个KEY,frame是一个相对编号,相对于本个clips的起始编号,返回Key的编号
		virtual unsigned int	AddPosKey( int frame, const FPOINT& v)
		{
			return m_posTable.Insert( CPosKeyValue( (float)frame, v));
		}

		virtual unsigned int	AddScaleKey( int frame, const FPOINT& v)
		{
			return m_scaleTable.Insert( CScaleKeyValue( (float)frame, v));
		}

		virtual unsigned int	AddRotationKey( int frame, float v)
		{
			return m_rotationTable.Insert( CRotationKeyValue( (float)frame, v));
		}

		virtual unsigned int	AddAlphaKey( int frame, float v)
		{
			return m_alphaTable.Insert( CAlphaKeyValue( (float)frame, v));
		}

		virtual unsigned int	AddColorKey( int frame, const XPCOLOR& v)
		{
			return m_colorTable.Insert( CColorKeyValue( (float)frame, v.ToXPVECTOR4()));
		}

		virtual unsigned int	AddDivideKey( int frame, float v)
		{
			return m_divideTable.Insert( CDivideKeyValue( (float)frame, v));
		}

		//! 清除指定编号的关键帧,注意,清除后可能后续影响编号
		virtual bool	RemoveKey( KeyType kt, unsigned int index)
		{
			switch( kt)
			{
			case KT_POS:
				m_posTable.Remove( index);
				break;
			case KT_SCALE:
				m_scaleTable.Remove( index);
				break;
			case KT_ROTATION:
				m_rotationTable.Remove( index);
				break;
			case KT_ALPHA:
				m_alphaTable.Remove( index);
				break;
			case KT_COLOR:
				m_colorTable.Remove( index);
				break;
			case KT_DIVIDE:
				m_divideTable.Remove( index);
				break;
			}

			return false;
		}

		//! 清除指定类型的所有关键帧
		virtual void	RemoveKeys( KeyType kt)
		{
			switch( kt)
			{
			case KT_POS:
				m_posTable.Clear();
				break;
			case KT_SCALE:
				m_scaleTable.Clear();
				break;
			case KT_ROTATION:
				m_rotationTable.Clear();
				break;
			case KT_ALPHA:
				m_alphaTable.Clear();
				break;
			case KT_COLOR:
				m_colorTable.Clear();
				break;
			case KT_DIVIDE:
				m_divideTable.Clear();
				break;
			}
		}


		virtual const KP_VECTOR& GetToolsKeyPoints( KeyType kt) const
		{
			return m_toolsKeyPoints[kt];
		}
		virtual const SP_VECTOR& GetToolsSamplePoints( KeyType kt) const
		{
			return m_toolsSamplePoints[kt];
		}
		virtual void SetToolsKeyPoints( KeyType kt, const KP_VECTOR& keyPoints)
		{
			m_toolsKeyPoints[kt].clear();
			std::copy( keyPoints.begin(), keyPoints.end(), std::back_inserter(m_toolsKeyPoints[kt]));
		}
		virtual void SetToolsSamplePoints( KeyType kt, const SP_VECTOR& samPoints)
		{
			m_toolsSamplePoints[kt].clear();
			std::copy( samPoints.begin(), samPoints.end(), std::back_inserter(m_toolsSamplePoints[kt]));
		}
		virtual void ClearToolsKeyPoints( KeyType kt)
		{
			m_toolsKeyPoints[kt].clear();
		}
		virtual void ClearSamplePoints( KeyType kt)
		{
			m_toolsSamplePoints[kt].clear();
		}

		// 设置控制器,有控制器时曲线变换不起作用
		virtual void SetControl( const IEffectClipControl* control)
		{
			m_control = control;
		}

		virtual const IEffectClipControl* GetControl() const
		{
			return m_control;
		}

	};


}

#endif