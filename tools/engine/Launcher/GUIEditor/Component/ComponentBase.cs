using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentBase : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormBase m_form;

		public CFormBase Form
		{
			get { return m_form; }
		}

		public CComponentBase(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 128, 255, 128));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormBase(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = m_form.BkColor;
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public override CComponent Clone()
		{
			CComponentBase one = new CComponentBase(Desktop, m_mainform, Name);
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
			one.Form.Transparent = m_form.Transparent;
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

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("Base");
			base.SaveXML(doc, sub);
            if( m_form.Transparent )
			    sub.SetAttribute("transparent", "true" );
            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );

			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image1Name = Brush.GetImageFileName();
            m_form.Transparent = GetAttributeBool(elem, "transparent");
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
