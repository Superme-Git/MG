using System;
using System.IO;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;
using System.Text;
using System.Runtime.InteropServices;

namespace GUIEditor2
{
	public class CComponentDesktop : CComponentContainer
	{
		private readonly CFormMain				m_mainform;
		private readonly Control				m_control;
		private readonly ListView				m_listview;
		private readonly LinkedList<CComponent> m_sellist = new LinkedList<CComponent>();
		private readonly CFormDesktop			m_form;

		private int			m_state;
		private CComponent	m_focus;
		private Point		m_oldpos;

		private Rectangle	m_selrect;
		private Point		m_selpos;
		private bool		m_seling;

		private bool		m_hasctrl;
		private bool		m_hasalt;
		private bool		m_hasshift;

		private readonly Dictionary<Size, Dictionary<CComponent, Point>> m_layout;
		private Size		m_size_now;
		private Size		m_size_default = new Size(800, 600);
		private Size[]		m_size_all = new Size[]{new Size(800, 600), new Size(1024, 768)};

		private IBrush		m_forebrush;		// 前景画刷(单一色/贴图)
		private bool		m_hasforeimage;

		private readonly List<int>	m_assist_x = new List<int>();	// 辅助线坐标集合
		private readonly List<int>	m_assist_y = new List<int>();

		public CFormMain MainForm
		{
			get { return m_mainform; }
		}

		public Control Control
		{
			get { return m_control; }
		}

		public ListView ListView
		{
			get { return m_listview; }
		}

		public LinkedList<CComponent> SelList
		{
			get { return m_sellist; }
		}

		public int State
		{
			get { return m_state; }
			set { m_state = value; }
		}

		public CComponent Focus
		{
			get { return m_focus; }
			set { m_focus = value; }
		}

		public Rectangle SelectRect
		{
			get { return m_selrect; }
			set { m_selrect = value; }
		}

		public Point SelectPos
		{
			get { return m_selpos; }
			set { m_selpos = value; }
		}

		public bool Selecting
		{
			get { return m_seling; }
			set { m_seling = value; }
		}

		public bool HasAlt
		{
			get { return m_hasalt; }
			set { m_hasalt = value; }
		}

		public bool HasCtrl
		{
			get { return m_hasctrl; }
			set { m_hasctrl = value; }
		}

		public bool HasShift
		{
			get { return m_hasshift; }
			set { m_hasshift = value; }
		}

		public Size NowSize
		{
			get { return m_size_now; }
		}

		public Size DefaultSize
		{
			get { return m_size_default; }
		}

		public Size[] AllSize
		{
			get { return m_size_all; }
		}

		public IBrush ForeBrush
		{
			get { return m_forebrush; }
		}

		public bool HasForeImage
		{
			get { return m_hasforeimage; }
			set { m_form.HasForeImage = m_hasforeimage = value; }
		}

		public void SelListClear(bool showform)
		{
			for(LinkedListNode<CComponent> node = m_sellist.First; node != null; node = node.Next)
			{
				CComponent one = node.Value;
				one.Select = false;
				if(one.ListViewItem != null && one.ListViewItem.Selected)
					one.ListViewItem.Selected = false;
			}
			m_sellist.Clear();
			if(showform && m_mainform != null)
				m_mainform.ShowForm(null);
		}

		public void SelListAdd(CComponent one, bool showform)
		{
			if(one != this && !one.Select)	// Desktop不能被选择
			{
				one.Select = true;
				if(one.ListViewItem != null && !one.ListViewItem.Selected)
					one.ListViewItem.Selected = true;
				if(!m_sellist.Contains(one))
					m_sellist.AddLast(one);
				if(showform && m_mainform != null)
					m_mainform.ShowForm(one.GetDialog());
			}
		}

		public void SelListDel(CComponent one)
		{
			one.Select = false;
			if(one.ListViewItem != null && one.ListViewItem.Selected)
				one.ListViewItem.Selected = false;
			m_sellist.Remove(one);
			if(m_mainform != null)
				m_mainform.ShowForm(null);
		}

		public void SelListAdd(CComponent one, Rectangle rect)	// 递归计算选择情况
		{
			if(one.ContainBy(rect))
			{
				if(one is CComponentTab)
				{
					CComponentTab tab = one as CComponentTab;
					foreach(object obj in tab.Form.TabList.Items)
					{
						CComponentTab.CTabItem item = obj as CComponentTab.CTabItem;
						LinkedList<CComponent> list = item.Container.List;
						for(LinkedListNode<CComponent> node = list.First; node != null; node = node.Next)
							SelListAdd(node.Value, rect);
					}
				}
				else if(one is CComponentContainer)
				{
					LinkedList<CComponent> list = (one as CComponentContainer).List;
					for(LinkedListNode<CComponent> node = list.First; node != null; node = node.Next)
						SelListAdd(node.Value, rect);
				}
				
				SelListAdd(one, false);
			}
			else if(one is CComponentContainer && one.IntersectWith(rect))
			{
				LinkedList<CComponent> list = (one as CComponentContainer).List;
				for(LinkedListNode<CComponent> node = list.First; node != null; node = node.Next)
					SelListAdd(node.Value, rect);
			}
		}

		public void SelListAddAll()
		{
			Rectangle rect = new Rectangle(Int32.MinValue/2, Int32.MinValue/2, Int32.MaxValue, Int32.MaxValue);
			for(LinkedListNode<CComponent> node = List.First; node != null; node = node.Next)
				SelListAdd(node.Value, rect);
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentDesktop(CFormMain form, Control control, ListView listview, IRender render) : base(null)
		{
			m_mainform = form;
			m_control = control;
			m_listview = listview;

			Desktop = this;
			Render = render;
			Brush = Render.CreateBrush();

			Rect = new Rectangle(0, 0, m_control.Width, m_control.Height);
			Brush.SetColor(Color.FromArgb(255, 0, 0, 0));

			m_form = new CFormDesktop(m_mainform, this);
			m_form.TopLevel = false;
			m_form.BkColor = m_form.BkColor;

			m_layout = new Dictionary<Size, Dictionary<CComponent, Point>>();
			m_layout.Add(new Size( 800, 600), new Dictionary<CComponent, Point>());
			m_layout.Add(new Size(1024, 768), new Dictionary<CComponent, Point>());
			m_size_now.Width = 800;
			m_size_now.Height = 600;

			m_forebrush = Render.CreateBrush();
			m_forebrush.SetColor(Color.FromArgb(128, 0, 0, 0));
			m_form.Color = m_form.Color;
		}

		// 保存当前的布局到m_layout中
		public void SaveLayout()
		{
			Dictionary<CComponent, Point> layout_now = m_layout[m_size_now];
			layout_now.Clear();
			foreach(CComponent node in List)
			{
				Rectangle rect = node.Rect;
				if(layout_now.ContainsKey(node))
					layout_now[node] = new Point(rect.X, rect.Y);
				else
					layout_now.Add(node, new Point(rect.X, rect.Y));
			}
		}

		// 应用m_layout到当前的布局中
		private void LoadLayout()
		{
			Dictionary<CComponent, Point> layout_now = m_layout[m_size_now];
			foreach(CComponent node in List)
			{
				if(layout_now.ContainsKey(node))
				{
					Rectangle rect = node.Rect;
					Point pos = layout_now[node];
					node.Rect = new Rectangle(pos.X, pos.Y, rect.Width, rect.Height);
				}
			}

			Rect = new Rectangle(0, 0, m_size_now.Width, m_size_now.Height);
		}

		// 切换布局
		public bool ChangeLayout(Size size)
		{
			if(m_size_now == size)
				return true;
			if(!m_layout.ContainsKey(size))
				return false;

			SaveLayout();
			m_size_now = size;
			m_mainform.Panel.Size = size;
			LoadLayout();

			return true;
		}

		public void GetLayout(CComponent com, out LinkedList<KeyValuePair<Size, Point>> poses)
		{
			poses = new LinkedList<KeyValuePair<Size,Point>>();
			foreach(KeyValuePair<Size, Dictionary<CComponent, Point>> layout in m_layout)
			{
				if(layout.Value.ContainsKey(com))
					poses.AddLast(new KeyValuePair<Size, Point>(layout.Key, layout.Value[com]));
			}
		}

		public void SetLayout(CComponent com, Size size, Point pos)
		{
			Dictionary<CComponent, Point> layout = null;
			if(m_layout.ContainsKey(size))
			{
				layout = m_layout[size];
				if(layout.ContainsKey(com))
				{
					layout[com] = pos;
					return;
				}
			}
			else
			{
				layout = new Dictionary<CComponent,Point>();
				m_layout.Add(size, layout);
			}
			layout.Add(com, pos);
		}

		public void DelLayout(CComponent com)
		{
			foreach(KeyValuePair<Size, Dictionary<CComponent, Point>> layout in m_layout)
				layout.Value.Remove(com);
		}

		public override Form GetDialog()
		{
			return m_form;
		}

		private void UpdateAssist(CComponentContainer con)
		{
			foreach(CComponent com in con.List)
			{
				if(com != m_focus)
				{
					Rectangle rect = com.AbsRect;
					m_assist_x.Add(rect.X);
					m_assist_x.Add(rect.Right);
					m_assist_y.Add(rect.Y);
					m_assist_y.Add(rect.Bottom);
					if(com is CComponentContainer)
						UpdateAssist(com as CComponentContainer);
				}
			}
		}

		public override bool OnLButtonDown(Point pos)
		{
			m_oldpos = pos;

			if(m_state == 0)					// 可能从某种特定位置(如菜单)直接点击到组件
			{				
				CComponent one = GetComponent(pos);
				if(one != null && one != this)
				{
					int state = one.GetMouseState(pos);
					if(state > 0)
						OnFocus(one, pos, state);
				}
			}

			if(m_state == 0 || m_hasalt)		// 开始拉选框
			{
				SelListClear(true);
				m_selrect.Location = m_selpos = pos;
				m_selrect.Width = m_selrect.Height = 0;
				m_seling = true;
				m_state = 0;

				if(!m_hasalt && m_mainform != null)
					m_mainform.ShowForm(GetDialog());

				return true;
			}

			SetCursor(m_state);

			if(m_focus != null)
			{
				if(m_focus.Parent != null && !m_focus.Lock)	// 父位置置前
				{
					m_focus.Parent.List.Remove(m_focus);
					m_focus.Parent.List.AddFirst(m_focus);
				}

				if(m_hasctrl)						// 按下CTRL表示添加或删除所选控件
				{
					if(!m_focus.Select)
						SelListAdd(m_focus, true);
					else
						SelListDel(m_focus);
					return true;
				}

				if(!m_focus.Select)	// 如果当前控件没有选取
				{
					SelListClear(false);
					SelListAdd(m_focus, true);
				}
				else
				{
					if(m_mainform != null)
						m_mainform.ShowForm(m_focus.GetDialog());
				}

				UpdateAssist(this);
				m_assist_x.Sort();
				m_assist_y.Sort();
			}

			m_control.Invalidate();

			return base.OnLButtonDown(pos);
		}
		
		public override bool OnLButtonUp(Point pos)
		{
			if(m_seling)
			{
				m_seling = false;
				m_control.Invalidate();
			}
			m_assist_x.Clear();
			m_assist_y.Clear();
			m_control.Invalidate();			// 清除辅助线

			return base.OnLButtonUp(pos);
		}

		public override bool OnRButtonDown(Point pos)
		{
			if(!m_hasalt && base.OnRButtonDown(pos))
				return true;

			SelListClear(true);
			m_selrect.Location = m_selpos = pos;
			m_selrect.Width = m_selrect.Height = 0;
			m_seling = true;
			m_state = 0;
			return true;
		}
	
		public override bool OnRButtonUp(Point pos)
		{
			m_seling = false;
//			m_control.Invalidate();			// 清除掉选框

			return base.OnRButtonUp(pos);
		}

		public override bool OnMouseMove(Point pos, int button)
		{
			if(button == 1 && m_state > 0)
			{
				Size size = new Size(pos.X - m_oldpos.X, pos.Y - m_oldpos.Y);
				foreach(CComponent it in m_sellist)
				{
					if(m_mainform != null)
						m_mainform.Panel.Modified = true;

					for(CComponent one = it.Parent; ; one = one.Parent)
					{
						if(one is CComponentDesktop)
						{
							it.UpdateRect(m_state, size, it);
							break;
						}
						if(one == null || one.Select)
							break;
					}
				}
				m_oldpos = pos;

				foreach(CComponent it in m_sellist)
				{
					for(CComponent one = it.Parent; ; one = one.Parent)
					{
						if(one is CComponentDesktop)
						{
							it.Parent = GetContainer(it);
							break;
						}
						if(one == null || one.Select)
							break;
					}
				}

				m_control.Invalidate();
				SetCursor(m_state);
				return true;
			}
			
			if(button >= 1 && m_seling)
			{
				if(m_selpos.X < pos.X) { m_selrect.X = m_selpos.X; m_selrect.Width  = pos.X - m_selpos.X; }
				else				   { m_selrect.X = pos.X;	   m_selrect.Width  = m_selpos.X - pos.X; }
				if(m_selpos.Y < pos.Y) { m_selrect.Y = m_selpos.Y; m_selrect.Height = pos.Y - m_selpos.Y; }
				else				   { m_selrect.Y = pos.Y;	   m_selrect.Height = m_selpos.Y - pos.Y; }

				if(button == 1)
				{
					SelListClear(false);
					SelListAdd(this, m_selrect);	// 计算所选组件
				}

				m_control.Invalidate();

				return true;
			}

			m_state = 0;
			m_seling = false;
			return base.OnMouseMove(pos, button);
		}

		public override void OnDraw()
		{
			base.OnDraw();

			if(Render != null)
			{
				if(m_seling && m_selrect.Width > 0 && m_selrect.Height > 0)
					Render.DrawBox(new Rectangle(m_selrect.X, m_selrect.Y, m_selrect.Width, m_selrect.Height),
								   Color.FromArgb(192, 255, 0, 255));

				if(m_focus != null)
				{
					Rectangle rect = m_focus.AbsRect;
					Color color = Color.FromArgb(96, 255, 255, 255);
					if(m_assist_x.Contains(rect.X))
						Render.DrawVLine(new Point(rect.X, 0), Rect.Height, color);
					if(m_assist_x.Contains(rect.Right))
						Render.DrawVLine(new Point(rect.Right, 0), Rect.Height, color);
					if(m_assist_y.Contains(rect.Y))
						Render.DrawHLine(new Point(0, rect.Y), Rect.Width, color);
					if(m_assist_y.Contains(rect.Bottom))
						Render.DrawHLine(new Point(0, rect.Bottom), Rect.Width, color);
				}

				if(m_hasforeimage && m_forebrush.GetImageFileName() != null)
					Render.DrawRect(m_forebrush.GetImageRect(), m_forebrush);
			}
		}

		public override CComponentContainer GetContainer(CComponent one)
		{
			foreach(CComponent it in List)
			{
				if(it is CComponentContainer)
				{
					CComponentContainer ret = (it as CComponentContainer).GetContainer(one);
					if(ret != null)
						return ret;
				}					
			}
			return this;
		}

		public virtual void OnFocus(CComponent one, Point pos, int state)
		{
			if(one != this)
			{
				m_state = state;
				m_focus = one;
				m_oldpos = pos;
				SetCursor(m_state);
			}
		}

		protected void SetCursor(int state)
		{
			switch(state)
			{
			case 1:	case 8: Cursor.Current = Cursors.SizeNWSE;	break;
			case 2:	case 7: Cursor.Current = Cursors.SizeWE;	break;
			case 3:	case 6: Cursor.Current = Cursors.SizeNESW;	break;
			case 4:	case 5: Cursor.Current = Cursors.SizeNS;	break;
			}
		}

        public override void OnKeyDown(KeyEventArgs e)
		{
			m_hasctrl  = e.Control;
			m_hasalt   = e.Alt;
			m_hasshift = e.Shift;

			Size size = new Size();

            ///
            /// 根据用户是否按下ALT键、CTRL键，确定方向键按下时控制的操作方式。
            /// ALT键摁下时，移动距离为8，否则为1。
            /// CTL键摁下时，方向键调整控件的尺寸而非位置。
            ///
			int state = 0;
			switch(e.KeyCode)
			{
			case Keys.Left:		size.Width  = (m_hasalt ? -8 : -1); state = (m_hasctrl ? 7 : 9); break;
			case Keys.Right:	size.Width  = (m_hasalt ?  8 :  1); state = (m_hasctrl ? 7 : 9); break;
			case Keys.Up:		size.Height = (m_hasalt ? -8 : -1); state = (m_hasctrl ? 5 : 9); break;
			case Keys.Down:		size.Height = (m_hasalt ?  8 :  1); state = (m_hasctrl ? 5 : 9); break;
			}

            if (state > 0)
            {
                foreach (CComponent it in m_sellist)
                {
                    it.UpdateRect(state, size, it);
                    it.Parent = GetContainer(it);
                }
                m_control.Invalidate();
            }
            else
            {
                if (m_sellist.Count > 0)
                {
                    foreach (CComponent it in m_sellist)
                    {
                        it.OnKeyDown(e);
                    }
                }
                else if (m_focus != null)
                {
                    m_focus.OnKeyDown(e);
                }

                m_control.Invalidate();
            }
		}

		public virtual void OnKeyUp(KeyEventArgs e)
		{
			m_hasctrl  = e.Control;
			m_hasalt   = e.Alt;
			m_hasshift = e.Shift;
		}

		public bool SaveXML(string filename)
		{
#if !DEBUG
			try
			{
#endif
				SaveLayout();

				XmlDocument doc = new XmlDocument();
				doc.AppendChild(doc.CreateXmlDeclaration("1.0", m_mainform.SaveCoding, "yes"));
				XmlElement desktop = doc.CreateElement("Desktop");
				desktop.SetAttribute("version", Program.GetVersion());
				base.SaveXML(doc, desktop);
				doc.AppendChild(desktop);
				doc.Save(filename);
				return true;
#if !DEBUG
			}
			catch(Exception ex)
			{
				MessageBox.Show(ex.Message, "GUIEditor2 - 异常", MessageBoxButtons.OK, MessageBoxIcon.Error);				
				return false;
			}
#endif
		}
		public bool LoadXML(string filename)
		{
#if !DEBUG
			try
			{
#endif
				XmlDocument doc = new XmlDocument();
				doc.Load(filename);
				XmlElement desktop = doc["Desktop"];
				if(desktop != null)
				{
					m_form.Image1Name = null;
					base.LoadXML(desktop);
					m_form.BkColor = m_form.BkColor;
					m_form.Image1Name = m_form.Image1Name;
				}

				LoadLayout();
				return true;
#if !DEBUG
			}
			catch(Exception ex)
			{
				MessageBox.Show(ex.Message, "GUIEditor2 - 异常", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return false;
			}
#endif
		}
	}
}
