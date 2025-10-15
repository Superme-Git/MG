#pragma once

#include <list>
#include <vector>
#include <set>
#include <math.h>

#ifndef EPSILON
#define EPSILON 0.00001f
#endif

// CBezierControl dialog
namespace bezier {
	const float NEAR_RADIUS = 6.0f;

	class INotify
	{
	public:
		virtual void OnSamplesReady(bool bReady) {}
		virtual void OnSampleCount(int sampleCount) {}
	};

	struct FPoint
	{
		float x;
		float y;
		FPoint() : x(0.0f), y(0.0f) {}
		FPoint(float _x, float _y) : x(_x), y(_y) {}
		FPoint(const POINT &pt) : x(static_cast<float>(pt.x)), y(static_cast<float>(pt.y)) {}

		inline bool Near(const FPoint &pt) const 
		{
			return (fabs(pt.x - x) + fabs(pt.y - y) < NEAR_RADIUS);
		}

		inline bool Near(const FPoint &pt, const FPoint &nearRadius) const 
		{
			return (fabs(pt.x - x) <= nearRadius.x && fabs(pt.y - y) <= nearRadius.y);
		}

		inline float ToLength(const FPoint &pt)
		{
			return sqrt((x - pt.x) * (x - pt.x) + (y - pt.y) * (y - pt.y));
		}

		FPoint& operator -= (const FPoint &pt)
		{
			x -= pt.x;
			y -= pt.y;
			return *this;
		}
		FPoint& operator += (const FPoint &pt)
		{
			x += pt.x;
			y += pt.y;
			return *this;
		}

		FPoint operator - (const FPoint &pt) const
		{
			return FPoint(x-pt.x, y-pt.y);
		}

		FPoint operator + (const FPoint &pt) const
		{
			return FPoint(x+pt.x, y+pt.y);
		}

		inline FPoint& operator = (const POINT &pt)
		{
			x = static_cast<float>(pt.x);
			y = static_cast<float>(pt.y);
			return (*this);
		}
		bool operator == (const FPoint& pt)
		{
			return (fabs(pt.x - x) < EPSILON && fabs(pt.y - y) < EPSILON);
		}
	};

	enum TangentPointTYPE {
		POINT_TYPE_SMOOTH = 0,
		POINT_TYPE_CORNER = 1
	};

	enum ControlPointType {
		CONTROL_TYPE_PREV = 0,
		CONTROL_TYPE_NEXT = 1,
		CONTROL_TYPE_NONE = 2
	};

	typedef size_t LineID;

	struct TangentPoint 
	{
		FPoint point;
		TangentPointTYPE type;
		FPoint controlPt[2];

		TangentPoint()
		{
			point.x = 0;
			point.y = 0;
			controlPt[0].x = 0;
			controlPt[0].y = 0;
			controlPt[1].x = 0;
			controlPt[1].y = 0;
			type = POINT_TYPE_CORNER;
		}

		TangentPoint& operator =(TangentPoint& rhs)
		{
			point = rhs.point;
			type = rhs.type;
			controlPt[0] = rhs.controlPt[0];
			controlPt[1] = rhs.controlPt[1];
			return *this;
		}
	};

	struct SamplePoint
	{
		FPoint point;
		bool inited;
		int id;
		float u;
		SamplePoint() : inited(false) {}
	};

	typedef std::list<TangentPoint> TangentPointContanter;
	typedef std::list<SamplePoint> SamplePointContanter;

	typedef std::vector<TangentPointContanter> TangentPointContanters;
	typedef std::vector<SamplePointContanter> SamplePointContanters;

	class CBezierControl : public CDialog
	{
		DECLARE_DYNAMIC(CBezierControl)
		//自定义类型
	private:
		enum TOOLS_STATE
		{
			MOUSE_STATE_ARROW = ID_BZ_TOOL_ARROW,
			MOUSE_STATE_ADD = ID_BZ_TOOL_ADD,
			MOUSE_STATE_REMOVE = ID_BZ_TOOL_REMOVE,
			MOUSE_STATE_CONVERT = ID_BZ_TOOL_CONVERT
		};

		enum EDIT_MODE
		{
			EDIT_MODE_NORMAL,
			EDIT_MODE_DRAG_POINT,
			EDIT_MODE_ADJUST_CONTROL,
			EDIT_MODE_DRAG_RECT
		};

		enum CLICK_STATE
		{
			CLICK_EMPTY_AREA,
			CLICK_A_SELECTED_POINT,
			CLICK_A_COMMON_POINT,
			CLICK_A_CONTROL_POINT
		};

		struct BezierParam
		{
			const FPoint* p0;
			const FPoint* p1;
			const FPoint* p2;
			const FPoint* p3;
		};

		struct SelectedPoint
		{
			TangentPointContanter::iterator iter;
			TangentPoint oldState;
			LineID lineID;
			bool operator< (const SelectedPoint& sp) const
			{
				if (lineID != sp.lineID)
					return lineID < sp.lineID;
				if (iter->point.x != sp.iter->point.x)
					return iter->point.x < sp.iter->point.x;
				return iter->point.y < sp.iter->point.y;
			}

			bool operator== (const SelectedPoint& sp) const
			{
				return ((lineID == sp.lineID) && (iter == sp.iter));
			}
		};

		typedef std::set<SelectedPoint> SelectedPoints;
		typedef std::vector<SamplePointContanter::iterator> SelectedSamplePoints;

	public:
		// Dialog Data
		enum { IDD = IDD_BEZIER_CONTROL };

		enum WORK_MODE {
			WORK_MODE_CURVE,
			WORK_MODE_SAMPLEING
		};

		//属性
	private:
		bool m_bIsSamplesReady;
		EDIT_MODE m_editMode;
		FPoint m_ptSelectPos;
		CStatic m_StaticX;
		CStatic m_StaticY;
		CStatic m_StaticFilter;
		CComboBox m_ComboFilter;
		CEdit m_EditX;
		CEdit m_EditY;
		CToolBar m_wndToolbar;
		float m_fMinX;
		float m_fMaxX;
		float m_fMinY;
		float m_fMaxY;
		CRect m_rectClient;
		int m_nClientWidth;
		int m_nClientHeight;
		TOOLS_STATE m_ToolsState;
		TangentPointContanters m_TPCTangentPoints;
		CPoint m_ptMouseDownPoint;
		CPoint m_ptMousePoint;
		bool m_bIsLButtonDown;
		bool m_bIsCtrlDown;
		FPoint m_NearRadius;
		INotify* m_pNotify;
		LineID m_filter;

		std::vector<CString> m_lineNames;

		SelectedPoints m_selectedPoints;
		ControlPointType m_selectedControlPoint;

		WORK_MODE m_WorkMode;
		SamplePointContanters m_SPCSamplePoints;
		SelectedSamplePoints m_selectedSamplePoints;
		int m_nSampleCount;
		bool m_bIsSamplePointWillBeDelete;

		std::set<int>m_setHasSamplePoint;

	public:
		float unitX;
		float unitY;
		bool xMustInt;
		COLORREF bgColor;
		COLORREF axisColor;
		COLORREF controlPointColor;
		COLORREF tangentPointColor;
		COLORREF selectedPointColor;
		COLORREF sampleLineColor;
		COLORREF selectRectColor;
		std::vector<COLORREF> vectorCurveColor;

		//方法
	private:
		CBezierControl &operator = (const CBezierControl& x);
		virtual void ClearSamplePoints()
		{
			if (m_bIsSamplesReady || m_SPCSamplePoints.size() > 0)
			{
				m_bIsSamplesReady = false;
				if (m_pNotify)
				{
					m_pNotify->OnSamplesReady(m_bIsSamplesReady);
				}
				m_SPCSamplePoints.clear();
				m_selectedSamplePoints.clear();
				m_setHasSamplePoint.clear();
			}
		}
		virtual void DrawCtrl(CDC* pDC);
		virtual void DrawAxis(CDC* pDC);
		virtual void DrawCurve(CDC* pDC);
		virtual void DrawCtrlPoint(CDC* pDC);
		virtual void DrawSamplePoint(CDC* pDC);
		virtual void DrawSelectRect(CDC* pDC);
		virtual void ResetSize();
		virtual void MoveSelectedPoint(FPoint logicVector, bool changeOldState);
		virtual void AdjustControlPoint(FPoint logicVector, ControlPointType type, bool changeOldState);
		virtual CLICK_STATE CheckSelectPoint(const FPoint &logicPoint, SelectedPoint& outInfo, ControlPointType& type);
		virtual void SelectPoints(FPoint logicTopLeft, FPoint logicBottomRight);
		virtual bool CheckSelectSamplePoint(const FPoint &logicPoint);
		virtual void ConvertControlPoint(const FPoint &logicPoint, bool changeOldState);
		virtual void AdjSelectedSamplePoint(float x);
		virtual FPoint Computer3BezierCurvePoint(float u, const BezierParam &p);
		virtual bool ComputerNear3BezierCurvePoint(float x, const BezierParam &p, float u1, float u2, float &u);
		virtual void SetControlPointFromEditCtrl();
		virtual void SetEditCtrlFromControlPoint();
		virtual void SetSamplePointFromEditCtrl();
		virtual void SetEditCtrlFromSamplePoint();
		virtual void ResetNearRadius();
		virtual bool FindCtrlPoint(LineID lineID, int pointId, TangentPointContanter::iterator& outIter);
		virtual void ResetFilter();
		virtual inline void TransToClient(const FPoint& in, FPoint &out) const
		{
			out.x = (in.x - m_fMinX) * m_nClientWidth / (m_fMaxX - m_fMinX) + 2;
			out.y = (m_fMaxY - in.y) * m_nClientHeight / (m_fMaxY - m_fMinY) - 1;
		}

		virtual inline void TransToLogic(const FPoint& in, FPoint &out) const
		{
			out.x = (in.x - 2) * (m_fMaxX - m_fMinX) / m_nClientWidth + m_fMinX;
			out.y = m_fMaxY - (m_fMaxY - m_fMinY) * (in.y + 1) / m_nClientHeight;
		}

		virtual inline void TransToLogicVector(const FPoint& in, FPoint &out) const
		{
			out.x = in.x * (m_fMaxX - m_fMinX) / m_nClientWidth;
			out.y = in.y * (m_fMinY - m_fMaxY) / m_nClientHeight;
		}
	public:
		CBezierControl(CWnd* pParent = NULL);   // standard constructor
		virtual ~CBezierControl();

		void SetNotify(INotify *pNotify)
		{
			m_pNotify = pNotify;
		}
		virtual void SetLineName(LineID id, CString name) { 
			if (id < m_lineNames.size())
			{
				m_lineNames[id] = name;
				ResetFilter();
			}
		}

		virtual CString GetLineName(LineID id) const { 
			if (id < m_lineNames.size())
			{
				return m_lineNames[id];
			}
			return L"";
		}


		virtual void SetLineCount(size_t count);
		virtual inline size_t GetLineCount() const { return m_TPCTangentPoints.size(); }

		virtual void SetMinX(float minX);
		virtual void SetMaxX(float maxX);
		virtual void SetMinY(float minY);
		virtual void SetMaxY(float maxY);

		virtual float GetMinX() const { return m_fMinX; }
		virtual float GetMaxX() const { return m_fMaxX; }
		virtual float GetMinY() const { return m_fMinY; }
		virtual float GetMaxY() const { return m_fMaxY; }

		const TangentPointContanters& GetTangentPoints() const { return m_TPCTangentPoints; }
		virtual void ModifyStartPoint(LineID line, float y, const FPoint &nextCtrlPt);
		virtual void ModifyEndPoint(LineID line, float y, const FPoint &prevCtrlPt);
		virtual void CleanPoints(LineID line);
		virtual void CleanAllLinePoints();
		virtual bool AddPoint(LineID line, const TangentPoint& tp);
		virtual inline bool AddPoint(LineID line, const FPoint& point)
		{
			TangentPoint tp;
			tp.point = point;
			tp.controlPt[0] = point;
			tp.controlPt[1] = point;
			return AddPoint(line, tp);
		}

		/////////////////////////////
		virtual void SetWorkMode(WORK_MODE workMode)
		{
			switch (workMode)
			{
			case WORK_MODE_CURVE:
				m_StaticFilter.ShowWindow(SW_SHOW);
				m_ComboFilter.ShowWindow(SW_SHOW);
				m_wndToolbar.ShowWindow(SW_SHOW);
				m_EditY.ShowWindow(SW_SHOW);
				m_StaticY.ShowWindow(SW_SHOW);
				m_editMode = EDIT_MODE_NORMAL;
				SetEditCtrlFromControlPoint();
				break;
			case WORK_MODE_SAMPLEING:
				m_bIsSamplePointWillBeDelete = false;
				m_StaticFilter.ShowWindow(SW_HIDE);
				m_ComboFilter.ShowWindow(SW_HIDE);
				m_wndToolbar.ShowWindow(SW_HIDE);
				m_EditY.ShowWindow(SW_HIDE);
				m_StaticY.ShowWindow(SW_HIDE);
				SetSampleCount(m_nSampleCount);
				SetEditCtrlFromSamplePoint();
				break;
			}
			m_WorkMode = workMode;
			Invalidate();
		}
		virtual void SetSampleCount(int count);
		virtual bool SetSamples(const SamplePointContanter &spc);
		inline virtual int GetSampleCount() const
		{
			return m_nSampleCount;
		}
		const SamplePointContanters& GetSamplePoints() const { return m_SPCSamplePoints; }

	protected:
		virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
		virtual void OnOK() {};
		virtual void OnCancel() {};
		DECLARE_MESSAGE_MAP()
	public:
		virtual BOOL OnInitDialog();
		virtual BOOL PreTranslateMessage(MSG* pMsg);
		afx_msg void OnPaint();
		afx_msg void OnSize(UINT nType, int cx, int cy);
		afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
		afx_msg void OnToolBar(UINT nID);
		afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
		afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
		afx_msg void OnMouseMove(UINT nFlags, CPoint point);
		afx_msg void OnEnKillfocusBzEditX();
		afx_msg void OnEnKillfocusBzEditY();
		afx_msg void OnCbnSelchangeBzComboFilter();
};
}