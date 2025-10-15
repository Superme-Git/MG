using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentTab : CComponentContainer
	{
		private readonly CFormMain m_mainform;
		private readonly CFormTab m_form;
		private CTabItem m_current;

		public class CTabItem
		{
			public readonly CComponentTab Tab;
			public CComponentTabContainer Container;
			public string Name = "";
			public string FontFormat = "";
			public IBrush BtnBrush0;
			public IBrush BtnBrush1;
			public IBrush BtnBrush2;
			public IBrush BtnBrush3;
			public IBrush BtnBrush4;
			public int PanelBorder;
			public string ScrollV = "";
			public string ScrollH = "";
			public IBrush BtnBrushNow;
			public int State = 1;

			public CTabItem(IRender render, CComponentTab tab)
			{
				Tab = tab;
				Container = new CComponentTabContainer(Tab);
				BtnBrush0 = render.CreateBrush();
				BtnBrush1 = render.CreateBrush();
				BtnBrush2 = render.CreateBrush();
				BtnBrush3 = render.CreateBrush();
				BtnBrush4 = render.CreateBrush();
				BtnBrushNow = BtnBrush1;
				BtnBrush1.SetColor(Color.FromArgb(255, 128, 128, 255));
				BtnBrush3.SetColor(Color.FromArgb(255, 0, 0, 255));
			}

			public CTabItem Clone(CComponentTab tab)
			{
				CTabItem one = new CTabItem(BtnBrush0.GetRender(), tab);
//				one.Container = Container.Clone();
				one.Name = Name;
				one.FontFormat = FontFormat;
				one.BtnBrush0 = BtnBrush0.Clone();
				one.BtnBrush1 = BtnBrush1.Clone();
				one.BtnBrush2 = BtnBrush2.Clone();
				one.BtnBrush3 = BtnBrush3.Clone();
				one.BtnBrush4 = BtnBrush4.Clone();
				one.PanelBorder = PanelBorder;
				one.ScrollV = ScrollV;
				one.ScrollH = ScrollH;
				one.BtnBrushNow = one.BtnBrush1;
				return one;
			}

			public override string ToString()
			{
				return '[' + Name + ']';
			}
		}

		public CFormTab Form
		{
			get { return m_form; }
		}

		public Color BkColor
		{
			get { return Brush.GetColor(); }
			set { Brush.SetColor(value); }
		}

		public CTabItem Current
		{
			get { return m_current; }
			set
			{
				if(m_current != null)
					m_current.Container.Parent = null;
				m_current = value;
				if(m_current != null)
				{
					m_current.Container.Parent = this;
					switch(m_form.Aspect)
					{
						default: m_current.Container.Rect = new Rectangle(0, m_form.HeadSize, Rect.Width, Rect.Height - m_form.HeadSize); break;
						case 1 : m_current.Container.Rect = new Rectangle(0, 0, Rect.Width, Rect.Height - m_form.HeadSize); break;
						case 2 : m_current.Container.Rect = new Rectangle(m_form.HeadSize, 0, Rect.Width - m_form.HeadSize, Rect.Height); break;
						case 3 : m_current.Container.Rect = new Rectangle(0, 0, Rect.Width - m_form.HeadSize, Rect.Height); break;
					}
				}
			}
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentTab(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(255, 255, 255, 255));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormTab(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.HeadSize = m_form.HeadSize;
			m_form.BtnWidth = m_form.BtnWidth;
			m_form.BtnHeight = m_form.BtnHeight;
			m_form.BtnBorder = m_form.BtnBorder;
			m_form.Aspect = 0;
		}

		public override CComponent Clone()
		{
			CComponentTab one = new CComponentTab(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.Brush = Brush.Clone();
			one.Form.Image1Name = m_form.Image1Name;
			one.Form.HeadSize = m_form.HeadSize;
			one.Form.BtnWidth = m_form.BtnWidth;
			one.Form.BtnHeight = m_form.BtnHeight;
			one.Form.BtnBorder = m_form.BtnBorder;
			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				one.Form.TabList.Items.Add(item.Clone(one));
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

			switch(m_form.Aspect)
			{
				default:
					rect.X = 0;
					rect.Y = m_form.HeadSize;
					rect.Height -= m_form.HeadSize;
					break;
				case 1:
					rect.X = 0;
					rect.Y = 0;
					rect.Height -= m_form.HeadSize;
					break;
				case 2:
					rect.X = m_form.HeadSize;
					rect.Y = 0;
					rect.Width -= m_form.HeadSize;
					break;
				case 3:
					rect.X = 0;
					rect.Y = 0;
					rect.Width -= m_form.HeadSize;
					break;
			}

			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				item.Container.Rect = rect;
			}

			Desktop.Control.Invalidate();
		}

		public override bool OnLButtonDown(Point pos)
		{
			if(Contain(pos))
			{
				Rectangle rect = AbsRect;
				int x = pos.X - rect.X;
				int y = pos.Y - rect.Y;
				int sel = -1;
				switch(m_form.Aspect)
				{
					default:
						if((y < m_form.HeadSize || y < m_form.BtnHeight) && x >= m_form.BtnBorder)
							sel = (m_form.BtnWidth > 0 ? (x - m_form.BtnBorder) / m_form.BtnWidth : 0);
						break;
					case 1:
						if((y > rect.Height - m_form.HeadSize || y > rect.Height - m_form.BtnHeight) && x >= m_form.BtnBorder)
							sel = (m_form.BtnWidth > 0 ? (x - m_form.BtnBorder) / m_form.BtnWidth : 0);
						break;
					case 2:
						if((x < m_form.HeadSize || x < m_form.BtnWidth) && y >= m_form.BtnBorder)
							sel = (m_form.BtnHeight > 0 ? (y - m_form.BtnBorder) / m_form.BtnHeight : 0);
						break;
					case 3:
						if((x > rect.Width - m_form.HeadSize || x > rect.Width - m_form.BtnWidth) && y >= m_form.BtnBorder)
							sel = (m_form.BtnHeight > 0 ? (y - m_form.BtnBorder) / m_form.BtnHeight : 0);
						break;
				}
				if(sel >= 0 && sel < m_form.TabList.Items.Count)
					m_form.TabList.SelectedIndex = sel;
			}

			return base.OnLButtonDown(pos);
		}
/*
		public override bool UpdateRect(int state, Size offset, CComponent top)
		{
			if(!base.UpdateRect(state, offset, top))	// 必须先移动父控件,否则子控件在响应OnMove/OnSize时相对位置会有偏差
				return false;

			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				foreach(CComponent it in item.Container.List)
					if(m_current != item && !it.Select && state == 9)	// 有Select属性的组件会被Desktop统一处理;只有移动时会跟着父窗口改变
						it.UpdateRect(state, offset, top);
			}

			return true;
		}
*/

		public override void ClearComponent()
		{
			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				item.Container.ClearComponent();
			}
			Current = null;

			base.ClearComponent();
		}

		public override void OnDraw()
		{
			base.OnDraw();

			if(Render == null || Hide)
				return;

			Rectangle rect = AbsRect;
			switch(m_form.Aspect)	// 标签宽高大于标头时居中,小于标头时与控件边缘对齐,并覆盖面板
			{
				default:
					if(m_form.HeadSize > m_form.BtnHeight)
						rect.Y += (m_form.HeadSize - m_form.BtnHeight) / 2;
					rect.X += m_form.BtnBorder;
					break;
				case 1:
					if(m_form.HeadSize > m_form.BtnHeight)
						rect.Y = rect.Bottom - (m_form.HeadSize + m_form.BtnHeight) / 2;
					else
						rect.Y = rect.Bottom - m_form.BtnHeight;
					rect.X += m_form.BtnBorder;
					break;
				case 2:
					if(m_form.HeadSize > m_form.BtnWidth)
						rect.X += (m_form.HeadSize - m_form.BtnWidth) / 2;
					rect.Y += m_form.BtnBorder;
					break;
				case 3:
					if(m_form.HeadSize > m_form.BtnWidth)
						rect.X = rect.Right - (m_form.HeadSize + m_form.BtnWidth) / 2;
					else
						rect.X = rect.Right - m_form.BtnWidth;
					rect.Y += m_form.BtnBorder;
					break;
			}
			rect.Width = m_form.BtnWidth;
			rect.Height = m_form.BtnHeight;
			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				Render.DrawRect(rect, item.BtnBrushNow);

				if(item.Name.Length > 0)
				{
					CFontProfile.CFontFormatParam fontformat = CFontProfile.FindFormat(item.FontFormat);
					if(fontformat != null)
					{
						IFont font = CFontProfile.GetFont(fontformat.fontid);
						if(font != null)
						{
							font.SetColor(fontformat.state[item.State].color);
							font.SetAlign(ETextAlign.H_CENTER | ETextAlign.V_CENTER);
							Render.DrawTextEx(rect, item.Name, font, fontformat.GetEffect(item.State), fontformat.state[item.State].effcolor);
						}
					}
				}

				if(m_form.Aspect < 2)
					rect.X += rect.Width;
				else
					rect.Y += rect.Height;
			}

			// 补画选择边框
			rect = AbsRect;
			if(Select && rect.Width > 0 && rect.Height > 0)
				Render.DrawBox(new Rectangle(rect.X, rect.Y, rect.Width, rect.Height),
							   Color.FromArgb(192, 255, 0, 255));
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("Tab");
			int sel = m_form.TabList.SelectedIndex;
			m_form.TabList.SelectedIndex = -1;
			base.SaveXML(doc, sub);
			sub.SetAttribute("headsize", m_form.HeadSize.ToString());
			sub.SetAttribute("btnwidth", m_form.BtnWidth.ToString());
			sub.SetAttribute("btnheight", m_form.BtnHeight.ToString());
			sub.SetAttribute("btnborder", m_form.BtnBorder.ToString());
			sub.SetAttribute("vertical", m_form.Aspect >= 2 ? "true" : "false");
			sub.SetAttribute("reverse", (m_form.Aspect & 1) == 1 ? "true" : "false");

			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				XmlElement button = doc.CreateElement("ButtonText");
				button.SetAttribute("n", item.Name);
				button.SetAttribute("i", item.FontFormat);
				SaveImage(doc, button, item.BtnBrush0, "Image0");
				SaveImage(doc, button, item.BtnBrush1, "Image1");
				SaveImage(doc, button, item.BtnBrush2, "Image2");
				SaveImage(doc, button, item.BtnBrush3, "Image3");
				SaveImage(doc, button, item.BtnBrush4, "Image4");
				sub.AppendChild(button);
			}
			XmlElement content = doc.CreateElement("Content");
			int i = 0;
			foreach(object obj in m_form.TabList.Items)
			{
				CTabItem item = obj as CTabItem;
				XmlElement panel = doc.CreateElement("Panel");
				panel.SetAttribute("border", item.PanelBorder.ToString());
				CScrollBarProfile.SaveScrollBar(doc, panel, item.ScrollV, item.ScrollH, Rect);
				item.Container.SaveXML(doc, panel);
				panel.RemoveAttribute("x");
				panel.RemoveAttribute("y");
				panel.RemoveAttribute("class");
				Current = item;
				panel.SetAttribute("n", Name + "_Panel" + i.ToString());
				content.AppendChild(panel);
				++i;
			}
			sub.AppendChild(content);
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);

			elem.AppendChild(sub);
			m_form.TabList.SelectedIndex = sel;
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.HeadSize = GetAttributeInt(elem, "headsize");
			m_form.BtnWidth = GetAttributeInt(elem, "btnwidth");
			m_form.BtnHeight = GetAttributeInt(elem, "btnheight");
			m_form.BtnBorder = GetAttributeInt(elem, "btnborder");
			m_form.Aspect = (GetAttributeBool(elem, "vertical") ? 2 : 0) + (GetAttributeBool(elem, "reverse") ? 1 : 0);
			m_form.N = Name;
			m_form.C = Class;
			m_form.Image1Name = Brush.GetImageFileName();
			m_form.BkColor = m_form.BkColor;
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");

			m_form.TabList.Items.Clear();
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node is XmlElement && (node.Name == "ButtonImage" || node.Name == "ButtonText"))	// 向后兼容
				{
					XmlElement sub = node as XmlElement;
					CComponentTab.CTabItem item = new CComponentTab.CTabItem(Render, this);
					item.Name = GetAttributeString(sub, "n");
					item.FontFormat = GetAttributeString(sub, "i");
					LoadImage(sub, item.BtnBrush0, "Image0");
					LoadImage(sub, item.BtnBrush1, "Image1");
					LoadImage(sub, item.BtnBrush2, "Image2");
					LoadImage(sub, item.BtnBrush3, "Image3");
					LoadImage(sub, item.BtnBrush4, "Image4");
					if(item.BtnBrush1.GetImageFileName() != null) item.BtnBrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
					if(item.BtnBrush3.GetImageFileName() != null) item.BtnBrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
				 	m_form.TabList.Items.Add(item);
				}
			}
			int i = 0;
			elem = elem["Content"];
			if(elem == null) return;
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node is XmlElement && node.Name == "Panel")
				{
					XmlElement sub = node as XmlElement;
					CComponentTab.CTabItem item = m_form.TabList.Items[i] as CComponentTab.CTabItem;
					item.PanelBorder = GetAttributeInt(sub, "border");
					foreach(XmlNode subnode in sub.ChildNodes)
					{
						if(subnode is XmlElement && subnode.Name == "ScrollBar")
						{
							bool isnew;
							string name = CScrollBarProfile.LoadXmlNewName(subnode as XmlElement, out isnew);
							if(name != null)
							{
								if((subnode as XmlElement).GetAttribute("vertical") == "true")
								{
									if(isnew)
										m_form.ScrollVControl.Items.Add(name);
									item.ScrollV = name;
								}
								else
								{
									if(isnew)
										m_form.ScrollHControl.Items.Add(name);
									item.ScrollH = name;
								}
							}
						}
					}
					item.Container.LoadXML(sub);
					item.Container.Parent = null;
					switch(m_form.Aspect)
					{
						default: item.Container.Rect = new Rectangle(0, m_form.HeadSize, Rect.Width, Rect.Height - m_form.HeadSize); break;
						case 1 : item.Container.Rect = new Rectangle(0, 0, Rect.Width, Rect.Height - m_form.HeadSize); break;
						case 2 : item.Container.Rect = new Rectangle(m_form.HeadSize, 0, Rect.Width - m_form.HeadSize, Rect.Height); break;
						case 3 : item.Container.Rect = new Rectangle(0, 0, Rect.Width - m_form.HeadSize, Rect.Height); break;
					}
					++i;
				}
			}
			if(m_form.TabList.Items.Count > 0)
				m_form.TabList.SelectedIndex = 0;
		}
	}

	public class CComponentTabContainer : CComponentContainer
	{
		private readonly CComponentTab m_tab;

		public override void Dispose()
		{
		}

		public CComponentTabContainer(CComponentTab tab) : base(null)
		{
			Desktop = tab.Desktop;
			Parent = tab;
			Render = tab.Render;
			Brush = Render.CreateBrush();
			Brush.SetColor(Color.FromArgb(192, 0, 128, 128));
			ListViewItem = null;
			Lock = true;

			m_tab = tab;
		}

		public override Form GetDialog()
		{
			return m_tab.GetDialog();
		}

		public new CComponentTabContainer Clone()
		{
			return base.Clone() as CComponentTabContainer;
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);

			Lock = true;
		}
	}
}
