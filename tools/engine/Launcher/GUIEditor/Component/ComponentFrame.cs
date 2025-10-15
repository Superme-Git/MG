using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentFrame : CComponentContainer
	{
		private readonly CFormMain m_mainform;
		private readonly CFormFrame m_form;

		public CFormFrame Form
		{
			get { return m_form; }
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentFrame(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(192, 192, 192, 192));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormFrame(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Brush.GetColor();
		}

		public override CComponent Clone()
		{
			CComponentFrame one = new CComponentFrame(Desktop, m_mainform, Name);
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
			one.Form.MoveType = m_form.MoveType;
			one.Form.TitleHeight = m_form.TitleHeight;
			one.Form.NotifyClass = m_form.NotifyClass;
			one.Form.OutDir = m_form.OutDir;
			one.Form.Include = m_form.Include;
			one.Form.NameSpace = m_form.NameSpace;
			one.Form.AutoClose = m_form.AutoClose;
			one.Form.PopUp = m_form.PopUp;
			one.Form.HookEditKey = m_form.HookEditKey;
			one.Form.DefaultFocus = m_form.DefaultFocus;
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
			XmlElement sub = doc.CreateElement("Frame");
			base.SaveXML(doc, sub);
			
            if( !string.IsNullOrEmpty( m_form.MoveType ) )
                sub.SetAttribute("movetype", m_form.MoveType);

            if( m_form.TitleHeight > 0 )
			    sub.SetAttribute("titleheight", m_form.TitleHeight.ToString());
            
            if( !string.IsNullOrEmpty( m_form.NotifyClass ) )
			    sub.SetAttribute("notifyclass", m_form.NotifyClass);

            if( !string.IsNullOrEmpty( m_form.OutDir ))
			    sub.SetAttribute("outdir", m_form.OutDir);
            if (!string.IsNullOrEmpty(m_form.Include))
			    sub.SetAttribute("include", m_form.Include);
            if (!string.IsNullOrEmpty(m_form.NameSpace))
			    sub.SetAttribute("namespace", m_form.NameSpace);

            if( m_form.AutoClose )
			    sub.SetAttribute("autoclose", "true" );

            if( m_form.PopUp )
			    sub.SetAttribute("popup", "true" );

            if( m_form.HookEditKey )
			    sub.SetAttribute("hookeditkey", "true" );
			
            if( !string.IsNullOrEmpty( m_form.DefaultFocus ))
                sub.SetAttribute("defaultfocus", m_form.DefaultFocus);

            if( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );

			sub.SetAttribute("tipinfo", m_form.TipInfo);

			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.MoveType = GetAttributeString(elem, "movetype");
			m_form.TitleHeight = GetAttributeInt(elem, "titleheight");
			m_form.NotifyClass = GetAttributeString(elem, "notifyclass");
			m_form.OutDir = GetAttributeString(elem, "outdir");
			m_form.Include = GetAttributeString(elem, "include");
			m_form.NameSpace = GetAttributeString(elem, "namespace");
			m_form.AutoClose = (GetAttributeString(elem, "autoclose") == "true");
			m_form.PopUp = (GetAttributeString(elem, "popup") == "true");
			m_form.HookEditKey = (GetAttributeString(elem, "hookeditkey") == "true");
			m_form.DefaultFocus = GetAttributeString(elem, "defaultfocus");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = Brush.GetColor();
			m_form.Image1Name = Brush.GetImageFileName();		
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
