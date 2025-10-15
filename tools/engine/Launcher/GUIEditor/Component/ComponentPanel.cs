using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentPanel : CComponentContainer
	{
		private readonly CFormMain m_mainform;
		private readonly CFormPanel m_form;

		public CFormPanel Form
		{
			get { return m_form; }
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentPanel(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 192, 192, 192));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormPanel(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
		}

		public override CComponent Clone()
		{
			CComponentPanel one = new CComponentPanel(Desktop, m_mainform, Name);
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
			one.Form.Border = m_form.Border;
			one.Form.ScrollVName = m_form.ScrollVName;
			one.Form.ScrollHName = m_form.ScrollHName;
			one.Form.NameSpace = m_form.NameSpace;
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
			XmlElement sub = doc.CreateElement("Panel");
			base.SaveXML(doc, sub);
			sub.SetAttribute("border", m_form.Border.ToString());
            if( !string.IsNullOrEmpty( m_form.NameSpace ) )
                sub.SetAttribute("namespace", m_form.NameSpace);
			CScrollBarProfile.SaveScrollBar(doc, sub, m_form.ScrollVName, m_form.ScrollHName, Rect);
            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.Border = GetAttributeInt(elem, "border");
			m_form.NameSpace = GetAttributeString(elem, "namespace");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image1Name = Brush.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");

			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node is XmlElement && node.Name == "ScrollBar")
				{
					bool isnew;
					string name = CScrollBarProfile.LoadXmlNewName(node as XmlElement, out isnew);
					if(name != null)
					{
						if((node as XmlElement).GetAttribute("vertical") == "true")
						{
							if(isnew)
								m_form.ScrollVControl.Items.Add(name);
							m_form.ScrollVName = name;
						}
						else
						{
							if(isnew)
								m_form.ScrollHControl.Items.Add(name);
							m_form.ScrollHName = name;
						}
					}
				}
			}
		}
	}
}
