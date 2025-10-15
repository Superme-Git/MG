using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentText : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormText m_form;

		public CFormText Form
		{
			get { return m_form; }
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentText(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 128, 128, 255));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormText(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
		}

		public override CComponent Clone()
		{
			CComponentText one = new CComponentText(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.BkColor = m_form.BkColor;
			one.Form.T = m_form.T;
			one.Form.FontFormat = m_form.FontFormat;
			one.Form.HasSelect = m_form.HasSelect;
			one.Form.HasPassword = m_form.HasPassword;
			one.Form.IsMultiLine = m_form.IsMultiLine;
			one.Form.ScrollName = m_form.ScrollName;
			one.Brush = Brush.Clone();
			one.Form.IsViewOnly = m_form.IsViewOnly;
			one.Form.TipInfo = m_form.TipInfo;
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

		public override void OnDraw()
		{
			base.OnDraw();

			if(m_form.T.Length > 0)
			{
				CFontProfile.CFontFormatParam fontformat = CFontProfile.FindFormat(m_form.FontFormat);
				if(fontformat != null)
				{
					IFont font = CFontProfile.GetFont(fontformat.fontid);
					if(font != null)
					{
						font.SetColor(fontformat.state[1].color);
						font.SetAlign(ETextAlign.H_LEFT | ETextAlign.V_TOP);
						Render.DrawTextEx(AbsRect, m_form.T, font, fontformat.GetEffect(1), fontformat.state[1].effcolor);
					}
				}
			}
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("Text");
			base.SaveXML(doc, sub);
			sub.SetAttribute("i", m_form.FontFormat);
			sub.SetAttribute("t", m_form.T);
            if( m_form.IsRawArea )
			    sub.SetAttribute("rawarea", "true" );
            if( m_form.IsMultiLine )
			    sub.SetAttribute("multiline", "true" );
            if( m_form.HasSelect )
			    sub.SetAttribute("select", "true" );
            if( m_form.HasPassword  )
			    sub.SetAttribute("password", "true" );
			CScrollBarProfile.SaveScrollBar(doc, sub, m_form.ScrollName, null, Rect);
            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.FontFormat = GetAttributeString(elem, "i");
			m_form.T = GetAttributeString(elem, "t");
			m_form.IsRawArea = GetAttributeBool(elem, "rawarea");
			m_form.IsMultiLine = GetAttributeBool(elem, "multiline");
			m_form.HasSelect = GetAttributeBool(elem, "select");
			m_form.HasPassword = GetAttributeBool(elem, "password");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image1Name = Brush.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");

			XmlElement scrxml = elem["ScrollBar"];
			if(scrxml != null)
			{
				bool isnew;
				string name = CScrollBarProfile.LoadXmlNewName(scrxml, out isnew);
				if(name != null)
				{
					if(isnew)
						m_form.ScrollControl.Items.Add(name);
					m_form.ScrollName = name;
				}
			}
		}
	}
}
