using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentTabApple : CComponentContainer
	{
		private readonly CFormMain m_mainform;
		private readonly CFormTabApple m_form;
		private CTabAppleItem m_current;

		public class CTabAppleItem
		{
			public readonly CComponentTabApple TabApple;
			public CComponentTabAppleContainer Container;
			public IBrush BtnBrush0;
			public IBrush BtnBrush1;
			public IBrush BtnBrush2;
			public IBrush BtnBrush3;
			public IBrush BtnBrush5;
			public IBrush BtnBrush6;
			public IBrush BtnBrush7;
			public IBrush BtnBrush8;
			public IBrush BtnBrushNow;

			public CTabAppleItem(IRender render, CComponentTabApple tabapple)
			{
				TabApple = tabapple;
				Container = new CComponentTabAppleContainer(TabApple);
				BtnBrush0 = render.CreateBrush();
				BtnBrush1 = render.CreateBrush();
				BtnBrush2 = render.CreateBrush();
				BtnBrush3 = render.CreateBrush();
				BtnBrush5 = render.CreateBrush();
				BtnBrush6 = render.CreateBrush();
				BtnBrush7 = render.CreateBrush();
				BtnBrush8 = render.CreateBrush();
				BtnBrushNow = BtnBrush1;
				BtnBrush1.SetColor(Color.FromArgb(255, 128, 128, 255));
				BtnBrush3.SetColor(Color.FromArgb(255, 0, 0, 255));
			}

			public CTabAppleItem Clone(CComponentTabApple tabapple)
			{
				CTabAppleItem one = new CTabAppleItem(BtnBrush0.GetRender(), tabapple);
//				one.Container = Container.Clone();
				one.BtnBrush0 = BtnBrush0.Clone();
				one.BtnBrush1 = BtnBrush1.Clone();
				one.BtnBrush2 = BtnBrush2.Clone();
				one.BtnBrush3 = BtnBrush3.Clone();
				one.BtnBrush5 = BtnBrush5.Clone();
				one.BtnBrush6 = BtnBrush6.Clone();
				one.BtnBrush7 = BtnBrush7.Clone();
				one.BtnBrush8 = BtnBrush8.Clone();
				one.BtnBrushNow = one.BtnBrush1;
				return one;
			}

			public override string ToString()
			{
				return "菜单项";
			}
		}

		public CFormTabApple Form
		{
			get { return m_form; }
		}

		public Color BkColor
		{
			get { return Brush.GetColor(); }
			set { Brush.SetColor(value); }
		}

		public CTabAppleItem Current
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
						default: m_current.Container.Rect = new Rectangle(0, m_form.BtnRadiusSmall * 2, Rect.Width, Rect.Height - m_form.BtnRadiusSmall * 2); break;
						case 1:  m_current.Container.Rect = new Rectangle(0, 0, Rect.Width, Rect.Height - m_form.BtnRadiusSmall * 2); break;
						case 2:  m_current.Container.Rect = new Rectangle(m_form.BtnRadiusSmall * 2, 0, Rect.Width - m_form.BtnRadiusSmall * 2, Rect.Height); break;
						case 3:  m_current.Container.Rect = new Rectangle(0, 0, Rect.Width - m_form.BtnRadiusSmall * 2, Rect.Height); break;
					}
				}
			}
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentTabApple(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Brush.SetColor(Color.FromArgb(255, 255, 255, 255));
			Name = name;
			m_mainform = mainform;
			m_form = new CFormTabApple(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BtnRadiusSmall = m_form.BtnRadiusSmall;
			m_form.BtnRadiusBig = m_form.BtnRadiusBig;
			m_form.Aspect = 1;
		}

		public override CComponent Clone()
		{
			CComponentTabApple one = new CComponentTabApple(Desktop, m_mainform, Name);
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
			one.Form.BtnRadiusSmall = m_form.BtnRadiusSmall;
			one.Form.BtnRadiusBig = m_form.BtnRadiusBig;
			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
				one.Form.TabAppleList.Items.Add(item.Clone(one));
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
					rect.Y = m_form.BtnRadiusSmall * 2;
					rect.Height -= m_form.BtnRadiusSmall * 2;
					break;
				case 1:
					rect.X = 0;
					rect.Y = 0;
					rect.Height -= m_form.BtnRadiusSmall * 2;
					break;
				case 2:
					rect.X = m_form.BtnRadiusSmall * 2;
					rect.Y = 0;
					rect.Width -= m_form.BtnRadiusSmall * 2;
					break;
				case 3:
					rect.X = 0;
					rect.Y = 0;
					rect.Width -= m_form.BtnRadiusSmall * 2;
					break;
			}

			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
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
						if(y < m_form.BtnRadiusSmall * 2)
							sel = (m_form.BtnRadiusSmall * 2 > 0 ? x / m_form.BtnRadiusSmall * 2 : 0);
						break;
					case 1:
						if(y > rect.Height - m_form.BtnRadiusSmall * 2)
							sel = (m_form.BtnRadiusSmall * 2 > 0 ? x / m_form.BtnRadiusSmall * 2 : 0);
						break;
					case 2:
						if(x < m_form.BtnRadiusSmall * 2)
							sel = (m_form.BtnRadiusSmall * 2 > 0 ? y / m_form.BtnRadiusSmall * 2 : 0);
						break;
					case 3:
						if(x > rect.Width - m_form.BtnRadiusSmall * 2)
							sel = (m_form.BtnRadiusSmall * 2 > 0 ? y / m_form.BtnRadiusSmall * 2 : 0);
						break;
				}
				if(sel >= 0 && sel < m_form.TabAppleList.Items.Count)
					m_form.TabAppleList.SelectedIndex = sel;
			}

			return base.OnLButtonDown(pos);
		}

/*
		public override bool UpdateRect(int state, Size offset, CComponent top)
		{
			if(!base.UpdateRect(state, offset, top))	// 必须先移动父控件,否则子控件在响应OnMove/OnSize时相对位置会有偏差
				return false;

			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
				foreach(CComponent it in item.Container.List)
					if(m_current != item && !it.Select && state == 9)	// 有Select属性的组件会被Desktop统一处理;只有移动时会跟着父窗口改变
						it.UpdateRect(state, offset, top);
			}

			return true;
		}
*/

		public override void ClearComponent()
		{
			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
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
				case 1: rect.Y = rect.Bottom - m_form.BtnRadiusSmall * 2; break;
				case 3: rect.X = rect.Right  - m_form.BtnRadiusSmall * 2; break;
			}
			rect.Width  = m_form.BtnRadiusSmall * 2;
			rect.Height = m_form.BtnRadiusSmall * 2;
			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
				Render.DrawRect(rect, item.BtnBrushNow);

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
			XmlElement sub = doc.CreateElement("AppleMenu");
			int sel = m_form.TabAppleList.SelectedIndex;
			m_form.TabAppleList.SelectedIndex = -1;
			base.SaveXML(doc, sub);
			sub.SetAttribute("radius0", m_form.BtnRadiusSmall.ToString());
			sub.SetAttribute("radius1", m_form.BtnRadiusBig.ToString());
			sub.SetAttribute("vertical", m_form.Aspect >= 2 ? "true" : "false");
			sub.SetAttribute("reverse", (m_form.Aspect & 1) == 1 ? "true" : "false");

			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
				XmlElement button = doc.CreateElement("ButtonImage");
				SaveImage(doc, button, item.BtnBrush0, "Image0");
				SaveImage(doc, button, item.BtnBrush1, "Image1");
				SaveImage(doc, button, item.BtnBrush2, "Image2");
				SaveImage(doc, button, item.BtnBrush3, "Image3");
				SaveImage(doc, button, item.BtnBrush5, "Image5");
				SaveImage(doc, button, item.BtnBrush6, "Image6");
				SaveImage(doc, button, item.BtnBrush7, "Image7");
				SaveImage(doc, button, item.BtnBrush8, "Image8");
				sub.AppendChild(button);
			}
			XmlElement content = doc.CreateElement("Content");
			int i = 0;
			foreach(object obj in m_form.TabAppleList.Items)
			{
				CTabAppleItem item = obj as CTabAppleItem;
				XmlElement panel = doc.CreateElement("Panel");
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
			m_form.TabAppleList.SelectedIndex = sel;
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.BtnRadiusSmall = GetAttributeInt(elem, "radius0");
			m_form.BtnRadiusBig = GetAttributeInt(elem, "radius1");
			m_form.Aspect = (GetAttributeBool(elem, "vertical") ? 2 : 0) + (GetAttributeBool(elem, "reverse") ? 1 : 0);
			m_form.N = Name;
			m_form.C = Class;
			m_form.Image1Name = Brush.GetImageFileName();
			m_form.BkColor = m_form.BkColor;
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");

			m_form.TabAppleList.Items.Clear();
			foreach(XmlNode node in elem.ChildNodes)
			{
				if(node is XmlElement && node.Name == "ButtonImage")
				{
					XmlElement sub = node as XmlElement;
					CComponentTabApple.CTabAppleItem item = new CComponentTabApple.CTabAppleItem(Render, this);
					LoadImage(sub, item.BtnBrush0, "Image0");
					LoadImage(sub, item.BtnBrush1, "Image1");
					LoadImage(sub, item.BtnBrush2, "Image2");
					LoadImage(sub, item.BtnBrush3, "Image3");
					LoadImage(sub, item.BtnBrush5, "Image5");
					LoadImage(sub, item.BtnBrush6, "Image6");
					LoadImage(sub, item.BtnBrush7, "Image7");
					LoadImage(sub, item.BtnBrush8, "Image8");
					if(item.BtnBrush1.GetImageFileName() != null) item.BtnBrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
					if(item.BtnBrush3.GetImageFileName() != null) item.BtnBrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
					m_form.TabAppleList.Items.Add(item);
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
					CComponentTabApple.CTabAppleItem item = m_form.TabAppleList.Items[i] as CComponentTabApple.CTabAppleItem;
					item.Container.LoadXML(sub);
					item.Container.Parent = null;
					switch(m_form.Aspect)
					{
						default: item.Container.Rect = new Rectangle(0, m_form.BtnRadiusSmall * 2, Rect.Width, Rect.Height - m_form.BtnRadiusSmall * 2); break;
						case 1:  item.Container.Rect = new Rectangle(0, 0, Rect.Width, Rect.Height - m_form.BtnRadiusSmall * 2); break;
						case 2:  item.Container.Rect = new Rectangle(m_form.BtnRadiusSmall * 2, 0, Rect.Width - m_form.BtnRadiusSmall * 2, Rect.Height); break;
						case 3:  item.Container.Rect = new Rectangle(0, 0, Rect.Width - m_form.BtnRadiusSmall * 2, Rect.Height); break;
					}
					++i;
				}
			}
			if(m_form.TabAppleList.Items.Count > 0)
				m_form.TabAppleList.SelectedIndex = 0;
		}
	}

	public class CComponentTabAppleContainer : CComponentContainer
	{
		private readonly CComponentTabApple m_tabapple;

		public override void Dispose()
		{
		}

		public CComponentTabAppleContainer(CComponentTabApple tabapple)
			: base(null)
		{
			Desktop = tabapple.Desktop;
			Parent = tabapple;
			Render = tabapple.Render;
			Brush = Render.CreateBrush();
			Brush.SetColor(Color.FromArgb(192, 0, 128, 128));
			ListViewItem = null;
			Lock = true;

			m_tabapple = tabapple;
		}

		public override Form GetDialog()
		{
			return m_tabapple.GetDialog();
		}

		public new CComponentTabAppleContainer Clone()
		{
			return base.Clone() as CComponentTabAppleContainer;
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);

			Lock = true;
		}
	}
}
