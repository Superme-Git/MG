using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentListBox : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormListBox m_form;

		private Color m_bkcolor1;
		private Color m_bkcolor3;
		private IBrush m_brushline;
		private IBrush m_bkbrush1;
		private IBrush m_bkbrush3;

		public CFormListBox Form
		{
			get { return m_form; }
		}

		public Color BkColor1
		{
			get { return m_bkcolor1; }
			set { m_bkcolor1 = value; }
		}

		public Color BkColor3
		{
			get { return m_bkcolor3; }
			set { m_bkcolor3 = value; }
		}

		public IBrush BrushLine
		{
			get { return m_brushline; }
			protected set { m_brushline = value; }
		}

		public IBrush BkBrush1
		{
			get { return m_bkbrush1; }
			protected set { m_bkbrush1 = value; }
		}

		public IBrush BkBrush3
		{
			get { return m_bkbrush3; }
			protected set { m_bkbrush3 = value; }
		}

		public void GetImageLineParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushline.GetImageRect();
			m_brushline.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushline.GetImageExpandMode();
		}

		public void SetImageLineParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushline.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void GetBkImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_bkbrush1.GetImageRect();
			m_bkbrush1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_bkbrush1.GetImageExpandMode();
		}

		public void SetBkImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_bkbrush1.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void GetBkImage3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_bkbrush3.GetImageRect();
			m_bkbrush3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_bkbrush3.GetImageExpandMode();
		}

		public void SetBkImage3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_bkbrush3.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentListBox(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(255, 255, 255, 255));
			Name = name;
			m_mainform = mainform;
			m_brushline = Brush; Brush = null;
			m_bkbrush1 = Render.CreateBrush(); m_bkbrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_bkbrush3 = Render.CreateBrush(); m_bkbrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_form = new CFormListBox(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor1 = m_brushline.GetColor();
			m_form.BkColor3 = m_brushline.GetColor();
			m_form.FontFormat = m_form.FontFormat;
			m_form.LineHeight = m_form.LineHeight;
			m_form.DataType = "text";
		}

		public override CComponent Clone()
		{
			CComponentListBox one = new CComponentListBox(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.BrushLine = BrushLine.Clone();
			one.Form.ImageLineName = m_form.ImageLineName;
			one.Form.BkColor1 = m_form.BkColor1;
			one.Form.BkColor3 = m_form.BkColor3;
			one.Form.BkImage1Name = m_form.BkImage1Name;
			one.Form.BkImage3Name = m_form.BkImage3Name;
			one.Form.FontFormat = m_form.FontFormat;
			one.Form.LineHeight = m_form.LineHeight;
			one.Form.DataType = m_form.DataType;
			one.Form.MultiSelect = m_form.MultiSelect;
			one.Form.OnSelChange = m_form.OnSelChange;
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
			if(Render != null)
			{
				Render.DrawRect(AbsRect, m_brushline);
			}

			base.OnDraw();
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("ListBox");
			SaveImage(doc, sub, m_brushline, "ImageLine");
			SaveImage(doc, sub, m_bkbrush1, "LineBkImage1");
			SaveImage(doc, sub, m_bkbrush3, "LineBkImage3");
			base.SaveXML(doc, sub);
			sub.SetAttribute("b1", Color2String(m_bkcolor1));
			sub.SetAttribute("b3", Color2String(m_bkcolor3));
			sub.SetAttribute("i", m_form.FontFormat);
			sub.SetAttribute("lineheight", m_form.LineHeight.ToString());
			sub.SetAttribute("datatype", m_form.DataType.ToString());
			sub.SetAttribute("multiselect", m_form.MultiSelect ? "true" : "false");
			sub.SetAttribute("onselchange", m_form.OnSelChange ? "true" : "false");
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			LoadImage(elem, m_brushline, "ImageLine");
			LoadImage(elem, m_bkbrush1, "LineBkImage1");
			LoadImage(elem, m_bkbrush3, "LineBkImage3");
			m_form.BkColor1 = GetAttributeColor(elem, "b1");
			m_form.BkColor3 = GetAttributeColor(elem, "b3");
			m_form.FontFormat = GetAttributeString(elem, "i");
			m_form.LineHeight = GetAttributeInt(elem, "lineheight");
			m_form.DataType = GetAttributeString(elem, "datatype");
			m_form.MultiSelect = GetAttributeBool(elem, "multiselect");
			m_form.OnSelChange = GetAttributeBool(elem, "onselchange");
			m_form.N = Name;
			m_form.C = Class;
			m_form.ImageLineName = m_brushline.GetImageFileName();
			m_form.BkImage1Name = m_bkbrush1.GetImageFileName();
			m_form.BkImage3Name = m_bkbrush3.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
