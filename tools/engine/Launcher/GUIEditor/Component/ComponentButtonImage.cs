using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentButtonImage : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormButtonImage m_form;

		private IBrush m_brush0;
		private IBrush m_brush1;
		private IBrush m_brush2;
		private IBrush m_brush3;

		public CFormButtonImage Form
		{
			get { return m_form; }
		}

		public IBrush Brush0
		{
			get { return m_brush0; }
			protected set { m_brush0 = value; }
		}

		public IBrush Brush1
		{
			get { return m_brush1; }
			protected set { m_brush1 = value; }
		}

		public IBrush Brush2
		{
			get { return m_brush2; }
			protected set { m_brush2 = value; }
		}

		public IBrush Brush3
		{
			get { return m_brush3; }
			protected set { m_brush3 = value; }
		}

		public void GetImage0Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush0.GetImageRect();
			m_brush0.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush0.GetImageExpandMode();
		}

		public void SetImage0Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush0.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

		public new void GetImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush1.GetImageRect();
			m_brush1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush1.GetImageExpandMode();
		}

		public new void SetImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush1.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

		public void GetImage2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush2.GetImageRect();
			m_brush2.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush2.GetImageExpandMode();
		}

		public void SetImage2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush2.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

		public void GetImage3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush3.GetImageRect();
			m_brush3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush3.GetImageExpandMode();
		}

		public void SetImage3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush3.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

		public void SetBrush(int i)
		{
			switch(i)
			{
			case 0: Brush = m_brush0; break;
			case 1: Brush = m_brush1; break;
			case 2: Brush = m_brush2; break;
			case 3: Brush = m_brush3; break;
			default: goto case 1;
			}
			Desktop.Control.Invalidate();
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentButtonImage(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 255, 128, 128));
			Name = name;
			m_mainform = mainform;
			m_brush0 = Render.CreateBrush();
			m_brush1 = Brush;
			m_brush2 = Render.CreateBrush();
			m_brush3 = Render.CreateBrush();
			m_form = new CFormButtonImage(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
			SetBrush(1);
		}

		public override CComponent Clone()
		{
			CComponentButtonImage one = new CComponentButtonImage(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.Brush0 = Brush0.Clone();
			one.Brush1 = Brush1.Clone();
			one.Brush2 = Brush2.Clone();
			one.Brush3 = Brush3.Clone();
			one.Form.Image0Name = m_form.Image0Name;
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.Image2Name = m_form.Image2Name;
			one.Form.Image3Name = m_form.Image3Name;
			one.Form.BkColor = m_form.BkColor;
			one.Form.IsViewOnly = m_form.IsViewOnly;
			one.Form.TipInfo = m_form.TipInfo;
			one.SetBrush(1);
			return one;
		}

		public override Form GetDialog()
		{
			return m_form;
		}

		public override void OnMove()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
		}

		public override void OnSize()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
			m_form.W = rect.Width;
			m_form.H = rect.Height;
		}

		public override bool OnLButtonDown(Point pos)
		{
			if(Contain(pos) && Brush != m_brush3)
				SetBrush(3);

			return base.OnLButtonDown(pos);
		}

		public override bool OnLButtonUp(Point pos)
		{
			if(Brush != m_brush1)
				SetBrush(1);

			return base.OnLButtonUp(pos);
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			SetBrush(1);
			XmlElement sub = doc.CreateElement("ButtonImage");
			base.SaveXML(doc, sub);
			SaveImage(doc, sub, m_brush0, "Image0");
			SaveImage(doc, sub, m_brush2, "Image2");
			SaveImage(doc, sub, m_brush3, "Image3");
            if( m_form.HasOnClick )
			    sub.SetAttribute("onclick", "true" );

            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );

			sub.SetAttribute("tipinfo", m_form.TipInfo);

			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			LoadImage(elem, m_brush0, "Image0");
			LoadImage(elem, m_brush2, "Image2");
			LoadImage(elem, m_brush3, "Image3");
			m_form.HasOnClick = (GetAttributeString(elem, "onclick") == "true");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image0Name = m_brush0.GetImageFileName();
			m_form.Image1Name = m_brush1.GetImageFileName();
			m_form.Image2Name = m_brush2.GetImageFileName();
			m_form.Image3Name = m_brush3.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
			SetBrush(1);
		}
	}
}
