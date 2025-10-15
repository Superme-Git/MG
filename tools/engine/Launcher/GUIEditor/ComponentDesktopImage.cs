using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public class CComponentDesktopImage : CComponentDesktop
	{
		private readonly CComponentSelect m_select;

		public CComponentSelect SelectComponent
		{
			get { return m_select; }
		}

		public CComponentDesktopImage(CFormMainImage form, CFormRenderImage control, IRender render) : base(null, control, null, render)
		{
			m_select = new CComponentSelect(this, form);
			m_select.Parent = this;
		}

		public override bool OnRButtonDown(Point pos)
		{
			return true;
		}

		public override bool OnRButtonUp(Point pos)
		{
			return true;
		}

		public override bool OnMouseMove(Point pos, int button)
		{
			base.OnMouseMove(pos, button);

			if(button > 0 && Selecting)
				m_select.Rect = SelectRect;

			return true;
		}
	}

	public class CComponentSelect : CComponent
	{
		private readonly CFormMainImage m_mainform;

		public override void Dispose()
		{
		}

		public CComponentSelect(CComponentDesktop desktop, CFormMainImage form) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(96, 0, 0, 255));
			m_mainform = form;
			m_mainform.SetXY(0, 0);
			m_mainform.SetWH(0, 0);
		}

		public override void OnMove()
		{
			base.OnMove();
			m_mainform.SetXY(Rect.X, Rect.Y);
		}

		public override void OnSize()
		{
			base.OnSize();
			m_mainform.SetWH(Rect.Width, Rect.Height);
		}

		public override void OnDraw()
		{
			base.OnDraw();

			if(m_mainform.HasBlock)
			{
				if(m_mainform.H0 < Rect.Height) Render.DrawHLine(new Point(Rect.X, Rect.Y      + m_mainform.H0    ), Rect.Width,  Color.FromArgb(192, 255, 0, 255));
				if(m_mainform.H1 < Rect.Height) Render.DrawHLine(new Point(Rect.X, Rect.Bottom - m_mainform.H1 - 1), Rect.Width,  Color.FromArgb(192, 255, 0, 255));
				if(m_mainform.W0 < Rect.Width ) Render.DrawVLine(new Point(Rect.Left   + m_mainform.W0    , Rect.Y), Rect.Height, Color.FromArgb(192, 255, 0, 255));
				if(m_mainform.W1 < Rect.Width ) Render.DrawVLine(new Point(Rect.Right  - m_mainform.W1 - 1, Rect.Y), Rect.Height, Color.FromArgb(192, 255, 0, 255));
			}
		}
	}
}
