///=============================================================================
/// Product   : MHZX
/// Project   : Renderer
/// Module    : PathLubricator.h
///=============================================================================
/// Description :
/// 	���(Camera)·��ƽ����
///=============================================================================
#ifndef __Nuclear_cameramoother_H__
#define __Nuclear_cameramoother_H__

namespace Nuclear
{
	// ƽ��·���㷨������
	class Smoother
	{
	public:
		Smoother();

		void SetSmoothTime( float fSeconds );
		float GetSmoothTime( ) const { return m_smoothTime; }

		void SetTarget( float from, float to, float velocity, bool bResetVolocity );
		float GetPostion( float elapsed );

	protected:

		float smoothCD(float &vel, float elapse);

	protected:
		float m_from;
		float m_to;
		float m_velocity; // ��ʱû��ʹ��
		float m_lastVelocity;
		float m_smoothTime;
	};

	// ���(Camera)·��ƽ����
	class CameraSmoother
	{
	public:
		typedef float coord_type; // ��������
		CameraSmoother();

	public:
		// ��������ߴ�
		void SetWorldExt( coord_type ww, coord_type wh )
		{
			m_worldWidth = ww;
			m_worldHeight = wh;

			// ����View�ߴ�
			SetViewScale( m_fViewScale );
		}

		// �����ӿڳߴ�
		void SetViewportExt( coord_type vw, coord_type vh )
		{
			m_viewportWidth = vw;
			m_viewportHeight = vh;

			// ����View�ߴ�
			SetViewScale( m_fViewScale );
		}

		// ������ͼ���ű���
		void SetViewScale( float fScale );

		// ��ȡ��ͼ���ű���
		float GetViewScale() const { return m_fViewScale; }

		// ��������ƶ��ٶ�
		void SetPlayerSpeed( float playerSpeed ) { m_playerSpeed = playerSpeed; }

		// ���þ�ͷƽ��ʱ�����
		void SetSmoothTime( float fSeconds )
		{
			m_smoothX.SetSmoothTime( fSeconds );
			m_smoothY.SetSmoothTime( fSeconds );
		}

		// ��ȡ��ͷƽ��ʱ�����
		float GetSmoothTime() const { return m_smoothX.GetSmoothTime(); }

		// ������λ�������(����m_focusX/m_focusY)
		void SetFocus(coord_type x, coord_type y);

		// ��������ƶ���Ŀ�꽹��(����m_focusX/m_focusY)
		void FocusTo(coord_type x, coord_type y);

		// ��������ƶ���Ŀ�꽹��(ͨ�������Ľ�ɫλ�úͽ�ɫ���н�λ��ȷ��)
		void FocusTo(coord_type xRole, coord_type yRole, coord_type xRoleTo, coord_type yRoleTo);

		// if position changed, returns true, else returns false
		bool Update(float fCurrent);

		// �����ǰ�Ƿ񵽴�Ŀ��(������Ƿ����ȶ�״̬)
		bool IsArrival() const { return m_bArrival; }

		// ����ƶ�Ŀ�꽹�㱻����(��������ƶ�״̬)
		void ClearArrival() { m_bArrival = false; }

		// ��Ļ����㻻�㵽��������
		void ScreenToWorld( coord_type& x, coord_type &y ) const;
		
		// �������껻�㵽��Ļ�����
		void WorldToScreen( coord_type& x, coord_type &y ) const;

		// �����ǰ���ƶ�·��(��������)
		void GetCurrentPath( coord_type& xfrom, coord_type& yfrom, coord_type& xto, coord_type& yto ) const
		{
			xfrom = m_vxFrom;
			yfrom = m_vyFrom;
			xto = m_vxTarget;
			yto = m_vyTarget;
		}

		// �����ǰ�Ľ���λ��(��Ļ��������������)
		void GetCurrentFocus( coord_type& x, coord_type& y) const
		{
			x = m_vx + m_viewWidth / 2;
			y = m_vy + m_viewHeight / 2;
		}

		// ����������ƶ�·��(����ʹ��)
		void DrawPath() const;

	protected:

		// ������λ�������(������m_focusX/m_focusY)
		void setFocus(coord_type x, coord_type y);
		// ��������ƶ���Ŀ�꽹��(������m_focusX/m_focusY)
		void focusTo(coord_type x, coord_type y);

	protected:
		coord_type m_focusX;		// ��ͷ���Զ�λ�Ľ���(��һ���ܹ��ﵽ)
		coord_type m_focusY;		// 
		coord_type m_worldWidth;	// �����������ߴ�
		coord_type m_worldHeight;	//
		coord_type m_viewportWidth;	// �ӿڿ��
		coord_type m_viewportHeight;// �ӿڸ߶�
		coord_type m_viewWidth;		// ��ͼ��� - ���ӿڿ�����������
		coord_type m_viewHeight;	// ��ͼ�߶� - ���ӿڿ���������߶�
		float m_fViewScale;			// ��ͼ������ӿڵ����ű��� 1 - ������, 2 - �Ŵ�2��, <=0 ��Чֵ
		float m_playerSpeed;		// ����ٶ�

		coord_type m_vx;			// ��ͷ��ǰ����
		coord_type m_vy;			//
		coord_type m_vxFrom;		// ��ͷ�˶���� 
		coord_type m_vyFrom;		// 
		coord_type m_vxTarget;		// ��ͷ�˶��յ�
		coord_type m_vyTarget;		//
		coord_type m_vdx;			// ��ͷ�˶����� target - from
		coord_type m_vdy;			// 

		float m_fElapsedX;			// ��ͷƽ������ʱ��
		float m_fElapsedY;			//

		Smoother m_smoothX;			// ��ͷƽ���㷨������
		Smoother m_smoothY;			//

		bool m_bArrival;			// ��ͷ�Ƿ�ﵽĿ��
	};

} // namespace Nuclear

#endif