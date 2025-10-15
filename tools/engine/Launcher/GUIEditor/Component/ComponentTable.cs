using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentTable : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormTable m_form;

		private Color m_bkcolor1;
		private Color m_bkcolor3;
		private Color m_gridcolor;
		private IBrush m_bkbrush1;
		private IBrush m_bkbrush3;

		private IBrush m_brushhead;
		private IBrush m_brushcell;

		public class CColItem
		{
			public string Name;
			public int Width;
			public Color BkColor = Color.FromArgb(0, 0, 0, 0);
			public IBrush Brush;

			public CColItem Clone()
			{
				CColItem one = new CColItem();
				one.Name = Name;
				one.Width = Width;
				one.BkColor = BkColor;
				one.Brush = Brush.Clone();
				return one;
			}

			public override string ToString()
			{
				return '[' + Name + ']';
			}
		}

		public CFormTable Form
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

		public Color GridColor
		{
			get { return m_gridcolor; }
			set { m_gridcolor = value; }
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

		public IBrush BrushHead
		{
			get { return m_brushhead; }
			protected set { m_brushhead = value; }
		}

		public IBrush BrushCell
		{
			get { return m_brushcell; }
			protected set { m_brushcell = value; }
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

		public void GetImageHeadParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushhead.GetImageRect();
			m_brushhead.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushhead.GetImageExpandMode();
		}

		public void SetImageHeadParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushhead.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void GetImageCellParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushcell.GetImageRect();
			m_brushcell.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushcell.GetImageExpandMode();
		}

		public void SetImageCellParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushcell.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentTable(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(255, 255, 255, 255));
			Name = name;
			m_mainform = mainform;
			m_bkbrush1 = Render.CreateBrush(); m_bkbrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_bkbrush3 = Render.CreateBrush(); m_bkbrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_brushhead = Render.CreateBrush(); m_brushhead.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_brushcell = Render.CreateBrush(); m_brushcell.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_form = new CFormTable(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor1 = Brush.GetColor();
			m_form.BkColor3 = Brush.GetColor();
			m_form.GridColor = Color.FromArgb(255, 0, 0, 0);
			m_form.HeadHeight = m_form.HeadHeight;
			m_form.LineHeight = m_form.LineHeight;
			m_form.DataType = "text";
		}

		public override CComponent Clone()
		{
			CComponentTable one = new CComponentTable(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.Brush = Brush.Clone();
			one.BrushHead = m_brushhead.Clone();
			one.BrushCell = m_brushcell.Clone();
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.BkImage1Name = m_form.BkImage1Name;
			one.Form.BkImage3Name = m_form.BkImage3Name;
			one.Form.ImageHeadName = m_form.ImageHeadName;
			one.Form.ImageCellName = m_form.ImageCellName;
			one.Form.BkColor1 = m_form.BkColor1;
			one.Form.BkColor3 = m_form.BkColor3;
			one.Form.GridColor = m_form.GridColor;
			one.Form.FontFormat = m_form.FontFormat;
			one.Form.HeadHeight = m_form.HeadHeight;
			one.Form.LineHeight = m_form.LineHeight;
			one.Form.DataType = m_form.DataType;
			one.Form.DragWidth = m_form.DragWidth;
			one.Form.OnSelChange = m_form.OnSelChange;
			one.Form.OnHeadClick = m_form.OnHeadClick;
			foreach(object obj in m_form.ColList.Items)
			{
				CColItem item = obj as CColItem;
				one.Form.ColList.Items.Add(item.Clone());
			}
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
			if(Render == null || Hide)
				return;

			Rectangle rect = AbsRect;

			if(Brush != null && Brush.GetImageFileName() != null)
				Render.DrawRect(rect, Brush);

			if(m_brushcell.GetImageFileName() != null && m_form.LineHeight > 0)
			{
				Rectangle cellrect = rect;
				cellrect.Height = m_form.LineHeight;
				foreach(object obj in m_form.ColList.Items)
				{
					CColItem item = obj as CColItem;
					cellrect.Width = item.Width;
					if(cellrect.X + cellrect.Width > rect.Right)
						break;
					for(cellrect.Y = rect.Y + m_form.HeadHeight; cellrect.Y + cellrect.Height <= rect.Bottom; cellrect.Y += cellrect.Height)
					{
						Render.DrawRect(cellrect, m_brushcell);
					}
					cellrect.X += cellrect.Width;
				}
			}

			int right = rect.Right;
			Point p0 = new Point(rect.X, rect.Top + m_form.HeadHeight);
			int vsize = rect.Height - m_form.HeadHeight;
			rect.Height = m_form.HeadHeight;
			foreach(object obj in m_form.ColList.Items)
			{
				CColItem item = obj as CColItem;
				rect.Width = item.Width;
				p0.X += item.Width;
				if(p0.X > right)
					break;
				if(p0.X < right)
					Render.DrawVLine(p0, vsize, m_gridcolor);

				if(m_brushhead.GetImageFileName() != null)
					Render.DrawRect(rect, m_brushhead);

				item.Brush.SetImageExpandMode(EExpandMode.H_CENTER | EExpandMode.V_CENTER);
				if(item.Brush.GetColor() != item.BkColor)
					item.Brush.SetColor(item.BkColor);
				Render.DrawRect(rect, item.Brush);

				if(item.Name.Length > 0)
				{
					CFontProfile.CFontFormatParam fontformat = CFontProfile.FindFormat(m_form.HeadFontFormat);
					if(fontformat != null)
					{
						IFont font = CFontProfile.GetFont(fontformat.fontid);
						if(font != null)
						{
							font.SetColor(fontformat.state[1].color);
							font.SetAlign(ETextAlign.H_CENTER | ETextAlign.V_CENTER);
							Render.DrawTextEx(rect, item.Name, font, fontformat.GetEffect(1), fontformat.state[1].effcolor);
						}
					}
				}

				rect.X += rect.Width;
			}

			rect = AbsRect;
			if(m_form.LineHeight > 0)
			{
				p0.X = rect.Left;  p0.Y = rect.Top + m_form.HeadHeight;
				int hsize = rect.Width;
				while(p0.Y - rect.Y < Rect.Height)
				{
					Render.DrawHLine(p0, hsize, m_gridcolor);
					p0.Y += m_form.LineHeight;
				}
			}

			// 补画选择边框
			if(Select && rect.Width > 0 && rect.Height > 0)
				Render.DrawBox(new Rectangle(rect.X, rect.Y, rect.Width, rect.Height),
							   Color.FromArgb(192, 255, 0, 255));
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("Table");
			base.SaveXML(doc, sub);
			sub.RemoveAttribute("b");
			sub.SetAttribute("b1", Color2String(m_bkcolor1));
			sub.SetAttribute("b3", Color2String(m_bkcolor3));
			sub.SetAttribute("gridcolor", Color2String(m_gridcolor));
			sub.SetAttribute("i", m_form.FontFormat);
			sub.SetAttribute("headfont", m_form.HeadFontFormat);
			sub.SetAttribute("headheight", m_form.HeadHeight.ToString());
			sub.SetAttribute("lineheight", m_form.LineHeight.ToString());
			sub.SetAttribute("datatype", m_form.DataType.ToString());
			sub.SetAttribute("dragwidth", m_form.DragWidth ? "true" : "false");
			sub.SetAttribute("onselchange", m_form.OnSelChange ? "true" : "false");
			sub.SetAttribute("onheadclick", m_form.OnHeadClick ? "true" : "false");
			SaveImage(doc, sub, m_bkbrush1, "LineBkImage1");
			SaveImage(doc, sub, m_bkbrush3, "LineBkImage3");
			SaveImage(doc, sub, m_brushhead, "ImageHead");
			SaveImage(doc, sub, m_brushcell, "ImageCell");

			foreach(object obj in m_form.ColList.Items)
			{
				CColItem item = obj as CColItem;
				XmlElement col = doc.CreateElement("Column");
				col.SetAttribute("w", item.Width.ToString());
				col.SetAttribute("b", Color2String(item.BkColor));
				col.SetAttribute("t", item.Name);
				SaveImage(doc, col, item.Brush, "Image1");
				sub.AppendChild(col);
			}
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			LoadImage(elem, m_bkbrush1, "LineBkImage1");
			LoadImage(elem, m_bkbrush3, "LineBkImage3");
			LoadImage(elem, m_brushhead, "ImageHead");
			LoadImage(elem, m_brushcell, "ImageCell");
			m_form.BkColor1 = GetAttributeColor(elem, "b1");
			m_form.BkColor3 = GetAttributeColor(elem, "b3");
			m_form.GridColor = GetAttributeColor(elem, "gridcolor", Color.FromArgb(255, 0, 0, 0));
			m_form.FontFormat = GetAttributeString(elem, "i");
			m_form.HeadFontFormat = GetAttributeString(elem, "headfont");
			m_form.HeadHeight = GetAttributeInt(elem, "headheight");
			m_form.LineHeight = GetAttributeInt(elem, "lineheight");
			m_form.DataType = GetAttributeString(elem, "datatype");
			m_form.DragWidth = GetAttributeBool(elem, "dragwidth");
			m_form.OnSelChange = GetAttributeBool(elem, "onselchange");
			m_form.OnHeadClick = GetAttributeBool(elem, "onheadclick");
			m_form.N = Name;
			m_form.C = Class;
			m_form.Image1Name = Brush.GetImageFileName();
			m_form.BkImage1Name = m_bkbrush1.GetImageFileName();
			m_form.BkImage3Name = m_bkbrush3.GetImageFileName();
			m_form.ImageHeadName = m_brushhead.GetImageFileName();
			m_form.ImageCellName = m_brushcell.GetImageFileName();
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");

			m_form.ColList.Items.Clear();
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node is XmlElement && node.Name == "Column")
				{
					XmlElement sub = node as XmlElement;
					CComponentTable.CColItem col = new CComponentTable.CColItem();
					col.Name = GetAttributeString(sub, "t");
					if(m_form.HeadFontFormat.Length <= 0)
						m_form.HeadFontFormat = GetAttributeString(sub, "i");
					col.Width = GetAttributeInt(sub, "w");
					col.BkColor = GetAttributeColor(sub, "b");
					col.Brush = Render.CreateBrush();
					LoadImage(sub, col.Brush, "Image1");
				 	m_form.ColList.Items.Add(col);
				}
			}
		}
	}
}
