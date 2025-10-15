using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentComboBox : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormComboBox m_form;

		private Color m_bkcolor1;
		private Color m_bkcolor3;
		private IBrush m_bkbrush1;
		private IBrush m_bkbrush3;

		private IBrush m_brushbox;
		private IBrush m_brushlist;
		private IBrush m_brush0;
		private IBrush m_brush1;
		private IBrush m_brush2;
		private IBrush m_brush3;
		
		private IBrush m_brushbtn;

		public CFormComboBox Form
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

		public IBrush BrushBox
		{
			get { return m_brushbox; }
			protected set { m_brushbox = value; }
		}

		public IBrush BrushList
		{
			get { return m_brushlist; }
			protected set { m_brushlist = value; }
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

		public void GetImageBoxParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushbox.GetImageRect();
			m_brushbox.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushbox.GetImageExpandMode();
		}

		public void SetImageBoxParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushbox.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

		public void GetImageListParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushlist.GetImageRect();
			m_brushlist.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushlist.GetImageExpandMode();
		}

		public void SetImageListParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushlist.SetImageBlock(rect, w0, w1, h0, h1, mode);
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

		public void SetBrushButton(int i)
		{
			switch(i)
			{
			case 0: m_brushbtn = m_brush0; break;
			case 1: m_brushbtn = m_brush1; break;
			case 2: m_brushbtn = m_brush2; break;
			case 3: m_brushbtn = m_brush3; break;
			default: goto case 1;
			}
			Desktop.Control.Invalidate();
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentComboBox(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Name = name;
			m_mainform = mainform;
			m_bkbrush1 = Render.CreateBrush(); m_bkbrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_bkbrush3 = Render.CreateBrush(); m_bkbrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_brushbox = Render.CreateBrush();
			m_brushlist = Render.CreateBrush();
			m_brush0 = Render.CreateBrush();
			m_brush1 = Brush; Brush = null;
			m_brush2 = Render.CreateBrush();
			m_brush3 = Render.CreateBrush();
			m_form = new CFormComboBox(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor1 = m_brush1.GetColor();
			m_form.BkColor3 = m_brush1.GetColor();
			m_form.BtnWidth = m_form.BtnWidth;
			m_form.ListBoxW = m_form.ListBoxW;
			m_form.ListBoxH = m_form.ListBoxH;
			m_form.LineHeight = m_form.LineHeight;
			m_form.DataType = "text";
			m_form.BkColor = Color.FromArgb(255, 255, 255, 255);
			SetBrushButton(1);
		}

		public override CComponent Clone()
		{
			CComponentComboBox one = new CComponentComboBox(Desktop, m_mainform, Name);
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
			one.BrushBox = BrushBox.Clone();
			one.BrushList = BrushList.Clone();
			one.Form.BkImage1Name = m_form.BkImage1Name;
			one.Form.BkImage3Name = m_form.BkImage3Name;
			one.Form.ImageBoxName = m_form.ImageBoxName;
			one.Form.ImageListName = m_form.ImageListName;
			one.Form.Image0Name = m_form.Image0Name;
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.Image2Name = m_form.Image2Name;
			one.Form.Image3Name = m_form.Image3Name;
			one.Form.BkColor1 = m_form.BkColor1;
			one.Form.BkColor3 = m_form.BkColor3;
			one.Form.FontFormat = m_form.FontFormat;
			one.Form.BtnWidth = m_form.BtnWidth;
			one.Form.ListBoxW = m_form.ListBoxW;
			one.Form.ListBoxH = m_form.ListBoxH;
			one.Form.Border = m_form.Border;
			one.Form.ScrollVName = m_form.ScrollVName;
			one.Form.ScrollHName = m_form.ScrollHName;
			one.Form.IsViewOnly = m_form.IsViewOnly;
			one.Form.TipInfo = m_form.TipInfo;
			SetBrushButton(1);
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
			if(Contain(pos) && pos.X > AbsRect.Right - m_form.BtnWidth && m_brushbtn != m_brush3)
				SetBrushButton(3);

			return base.OnLButtonDown(pos);
		}

		public override bool OnLButtonUp(Point pos)
		{
			if(Brush != m_brush1)
				SetBrushButton(1);

			return base.OnLButtonUp(pos);
		}

		public override void OnDraw()
		{
			if(Render != null)
			{
				Render.DrawRect(AbsRect, m_brushbox);
				if(m_brushbtn != null)
				{
					int x = (AbsRect.Width < m_form.BtnWidth ? AbsRect.X : AbsRect.Right - m_form.BtnWidth);
					int w = AbsRect.Right - x;
					Render.DrawRect(new Rectangle(x, AbsRect.Y, w, AbsRect.Height), m_brushbtn);
				}
			}

			base.OnDraw();
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("ComboBox");
			base.SaveXML(doc, sub);
			sub.SetAttribute("b1", Color2String(m_bkcolor1));
			sub.SetAttribute("b3", Color2String(m_bkcolor3));
			sub.SetAttribute("i", m_form.FontFormat);
			sub.SetAttribute("btnwidth", m_form.BtnWidth.ToString());
			sub.SetAttribute("listboxw", m_form.ListBoxW.ToString());
			sub.SetAttribute("listboxh", m_form.ListBoxH.ToString());
			sub.SetAttribute("border", m_form.Border.ToString());
			sub.SetAttribute("lineheight", m_form.LineHeight.ToString());
			sub.SetAttribute("datatype", m_form.DataType);
			sub.SetAttribute("onselchange", m_form.OnSelChange ? "true" : "false");
			SaveImage(doc, sub, m_bkbrush1, "LineBkImage1");
			SaveImage(doc, sub, m_bkbrush3, "LineBkImage3");
			SaveImage(doc, sub, m_brushbox, "ImageBox");
			SaveImage(doc, sub, m_brushlist, "ImageList");
			SaveImage(doc, sub, m_brush0, "Image0");
			SaveImage(doc, sub, m_brush1, "Image1");
			SaveImage(doc, sub, m_brush2, "Image2");
			SaveImage(doc, sub, m_brush3, "Image3");
			CScrollBarProfile.SaveScrollBar(doc, sub, m_form.ScrollVName, m_form.ScrollHName, Rect);
            if ( m_form.IsViewOnly )
			    sub.SetAttribute("viewonly", "true" );
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			LoadImage(elem, m_bkbrush1, "LineBkImage1");
			LoadImage(elem, m_bkbrush3, "LineBkImage3");
			LoadImage(elem, m_brushbox, "ImageBox");
			LoadImage(elem, m_brushlist, "ImageList");
			LoadImage(elem, m_brush0, "Image0");
			LoadImage(elem, m_brush1, "Image1");
			LoadImage(elem, m_brush2, "Image2");
			LoadImage(elem, m_brush3, "Image3");
			m_form.BkColor1 = GetAttributeColor(elem, "b1");
			m_form.BkColor3 = GetAttributeColor(elem, "b3");
			m_form.BkImage1Name = m_bkbrush1.GetImageFileName();
			m_form.BkImage3Name = m_bkbrush3.GetImageFileName();
			m_form.FontFormat = GetAttributeString(elem, "i");
			m_form.BtnWidth = GetAttributeInt(elem, "btnwidth");
			m_form.ListBoxW = GetAttributeInt(elem, "listboxw");
			m_form.ListBoxH = GetAttributeInt(elem, "listboxh");
			m_form.Border = GetAttributeInt(elem, "border");
			m_form.LineHeight = GetAttributeInt(elem, "lineheight");
			m_form.DataType = GetAttributeString(elem, "datatype");
			m_form.OnSelChange = GetAttributeBool(elem, "onselchange");
			m_form.N = Name;
			m_form.C = Class;
			m_form.ImageBoxName = m_brushbox.GetImageFileName();
			m_form.ImageListName = m_brushlist.GetImageFileName();
			m_form.Image0Name = m_brush0.GetImageFileName();
			m_form.Image1Name = m_brush1.GetImageFileName();
			m_form.Image2Name = m_brush2.GetImageFileName();
			m_form.Image3Name = m_brush3.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
			SetBrushButton(1);

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
