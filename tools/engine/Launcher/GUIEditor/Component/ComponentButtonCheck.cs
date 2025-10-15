using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentButtonCheck : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormButtonCheck m_form;

		private IBrush m_brush0;
		private IBrush m_brush1;
		private IBrush m_brush2;
		private IBrush m_brush3;
		private IBrush m_brush4;
		private int m_state = 1;

		public CFormButtonCheck Form
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

		public IBrush Brush4
		{
			get { return m_brush4; }
			protected set { m_brush4 = value; }
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
		}

		public void GetImage4Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush4.GetImageRect();
			m_brush4.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush4.GetImageExpandMode();
		}

		public void SetImage4Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush4.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBrush(int i)
		{
			switch(i)
			{
			case 0: Brush = m_brush0; break;
			case 1: Brush = m_brush1; break;
			case 2: Brush = m_brush2; break;
			case 3: Brush = m_brush3; break;
			case 4: Brush = m_brush4; break;
			default: goto case 1;
			}
			m_state = i;
			Desktop.Control.Invalidate();
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentButtonCheck(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 255, 128, 128));
			Name = name;
			m_mainform = mainform;
			m_brush0 = Brush.Clone();
			m_brush1 = Brush.Clone();
			m_brush2 = Brush.Clone();
			m_brush3 = Brush.Clone();
			m_brush4 = Brush.Clone();
			m_form = new CFormButtonCheck(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
			m_form.BoxWidth = m_form.BoxWidth;
			SetBrush(1);
		}

		public override CComponent Clone()
		{
			CComponentButtonCheck one = new CComponentButtonCheck(Desktop, m_mainform, Name);
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
			one.Brush4 = Brush4.Clone();
			one.Form.Image0Name = m_form.Image0Name;
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.Image2Name = m_form.Image2Name;
			one.Form.Image3Name = m_form.Image3Name;
			one.Form.Image4Name = m_form.Image4Name;
			one.Form.BkColor = m_form.BkColor;
			one.Form.T = m_form.T;
			one.Form.FontFormat = m_form.FontFormat;
			one.Form.BoxWidth = m_form.BoxWidth;
			one.Form.HasOnClick = m_form.HasOnClick;
			one.Form.HasButton = m_form.HasButton;
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
			m_form.X = Rect.X;
			m_form.Y = Rect.Y;
		}

		public override void OnSize()
		{
			m_form.X = Rect.X;
			m_form.Y = Rect.Y;
			m_form.W = Rect.Width;
			m_form.H = Rect.Height;
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

		public override void OnDraw()
		{
			if(Render != null)
			{
				Rectangle rect = AbsRect;

				if(Brush != m_brush0 && Brush != m_brush1)
				{
					if(Brush1.GetImageFileName() != null && !m_form.HasButton)
					{
						int w = m_brush1.GetImageRect().Width;
						w = (w < rect.Width ? w : rect.Width);
						Render.DrawRect(new Rectangle(rect.X, rect.Y, w, rect.Height), m_brush1);
					}
					else
					{
						Render.DrawRect(rect, m_brush1);
					}
				}

				int textposx = 0;
				if(Brush.GetImageFileName() != null && !m_form.HasButton)
				{
					int ww = Brush.GetImageRect().Width;
					ww = (ww < rect.Width ? ww : rect.Width);
					Render.DrawRect(new Rectangle(rect.X, rect.Y, ww, rect.Height), Brush);
					textposx = ww;
				}
				else
				{
					Render.DrawRect(rect, Brush);
				}

				if(m_form.T.Length > 0)
				{
					CFontProfile.CFontFormatParam fontformat = CFontProfile.FindFormat(m_form.FontFormat);
					if(fontformat != null)
					{
						IFont font = CFontProfile.GetFont(fontformat.fontid);
						if(font != null)
						{
							font.SetColor(fontformat.state[m_state].color);
							if(!m_form.HasButton)
								font.SetAlign(ETextAlign.H_LEFT | ETextAlign.V_CENTER);
							else
							{
								font.SetAlign(ETextAlign.H_CENTER | ETextAlign.V_CENTER);
								textposx = 0;
							}
							Render.DrawTextEx(new Rectangle(rect.X + textposx, rect.Y, rect.Width - textposx, rect.Height),
											  m_form.T, font, fontformat.GetEffect(m_state), fontformat.state[m_state].effcolor);
						}
					}
				}

				if(Select && rect.Width > 0 && rect.Height > 0)
					Render.DrawBox(new Rectangle(rect.X, rect.Y, rect.Width, rect.Height),
								   Color.FromArgb(192, 255, 0, 255));
			}
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			SetBrush(1);
			XmlElement sub = doc.CreateElement("ButtonCheck");
			base.SaveXML(doc, sub);
			sub.SetAttribute("i", m_form.FontFormat);
			sub.SetAttribute("boxwidth", m_form.BoxWidth.ToString());
			sub.SetAttribute("t", m_form.T);
			sub.SetAttribute("onclick", m_form.HasOnClick ? "true" : "false");
			sub.SetAttribute("button", m_form.HasButton ? "true" : "false");
			SaveImage(doc, sub, m_brush0, "Image0");
			SaveImage(doc, sub, m_brush2, "Image2");
			SaveImage(doc, sub, m_brush3, "Image3");
			SaveImage(doc, sub, m_brush4, "Image4");
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			LoadImage(elem, m_brush0, "Image0");
			LoadImage(elem, m_brush1, "Image1");
			LoadImage(elem, m_brush2, "Image2");
			LoadImage(elem, m_brush3, "Image3");
			LoadImage(elem, m_brush4, "Image4");
			m_form.FontFormat = GetAttributeString(elem, "i");
			m_form.BoxWidth = GetAttributeInt(elem, "boxwidth");
			m_form.T = GetAttributeString(elem, "t");
			m_form.HasOnClick = GetAttributeBool(elem, "onclick");
			m_form.HasButton = GetAttributeBool(elem, "button");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image0Name = m_brush0.GetImageFileName();
			m_form.Image1Name = m_brush1.GetImageFileName();
			m_form.Image2Name = m_brush2.GetImageFileName();
			m_form.Image3Name = m_brush3.GetImageFileName();
			m_form.Image4Name = m_brush4.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
			SetBrush(1);
		}
	}
}
