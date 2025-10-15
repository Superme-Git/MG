using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentStatic : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormStatic m_form;
        private Color m_fontColor;

        public Color FontColor
        {
            get { return m_fontColor; }
            set { m_fontColor = value; }
        }

		public CFormStatic Form
		{
			get { return m_form; }
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentStatic(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 96, 96, 96));
            Name = name;
			m_mainform = mainform;
			m_form = new CFormStatic(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
            m_form.FontColor = Color.FromArgb(255, 0, 0, 0);			
		}

		public override CComponent Clone()
		{
			CComponentStatic one = new CComponentStatic(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
            one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			//one.Form.FontFormat = m_form.FontFormat;
            one.Form.FontColor = m_fontColor;
			one.Form.T = m_form.T;
			one.Form.Center = m_form.Center;
			one.Form.Transparent = m_form.Transparent;
			one.OnSize();
//			one.Form.Image1Name = m_form.Image1Name;
			one.Form.BkColor = m_form.BkColor;
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
                IFont font = Render.CreateFont();
                if (font != null)
                {
                    font.SetFont("SimSun", 12);
                    font.SetColor(FontColor);
                    font.SetAlign((m_form.Center ? ETextAlign.H_CENTER : ETextAlign.H_LEFT) |
                                      ETextAlign.V_CENTER |
                                      ETextAlign.SINGLELINE);
                    Render.DrawText(AbsRect, m_form.T, font);
                }
			}
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("Static");
			base.SaveXML(doc, sub);
			// sub.SetAttribute("i", m_form.FontFormat);
            sub.SetAttribute("c", Color2String(m_fontColor));
			sub.SetAttribute("t", m_form.T);
            if( m_form.Center )
			    sub.SetAttribute("center", "true" );
            if( m_form.Transparent )
			    sub.SetAttribute("transparent", "true" );            
            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true");

			sub.SetAttribute("tipinfo", m_form.TipInfo);

			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
//			m_form.Image1Name = Brush.GetImageFileName();
			//m_form.FontFormat = GetAttributeString(elem, "i");

            m_form.FontColor = GetAttributeColor(elem, "c");
			m_form.T = GetAttributeString(elem, "t");
			m_form.Center = GetAttributeBool(elem, "center");
			m_form.Transparent = (GetAttributeString(elem, "transparent") == "true");
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
