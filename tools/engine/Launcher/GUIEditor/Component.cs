using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;
using System.Collections.Generic;

namespace GUIEditor2
{
	public abstract class CComponent : IDisposable
	{
		private const int				BORDER_SIZE = 3;	// 用于调整组件大小的边缘最大距离

		private CComponentDesktop		m_desktop;			// 所属desktop组件容器
		private IRender					m_render;			// 渲染器
		private IBrush					m_brush;			// 背景画刷(单一色/贴图)  Image1 提供定义
        private ListViewItem			m_lvi;				// 对应列表框的项

		private CComponentContainer		m_parent;			// 父组件容器
		private string					m_name = "";		// 名称
		private string					m_class = "";		// 类名
		private string					m_comment = "";		// 注释
		private Rectangle				m_rect;				// 相对位置(相对于父控件)

		private bool					m_select;			// 此组件是否是选择的状态
		private bool					m_lock;				// 是否锁定控件位置及大小
		private bool					m_hide;				// 是否不显示控件
        private IDictionary<String, String> m_xmlAttributes = null;  // 含用户自定义的控件设置。

		public CComponentDesktop Desktop
		{
			get { return m_desktop; }
			protected set { m_desktop = value; }
		}

		public IRender Render
		{
			get { return m_render; }
			protected set { m_render = value; }
		}

		public IBrush Brush
		{
			get { return m_brush; }
			set { m_brush = value; }
		}

		public ListViewItem ListViewItem
		{
			get { return m_lvi; }
			protected set { m_lvi = value; }
		}

		public CComponentContainer Parent
		{
			get { return m_parent; }
			set
			{
				if(m_parent == value)
					return;
				if(this is CComponentTabContainer && value != null && !(value is CComponentTab))
					return;
				if(value != null && value.Desktop != m_desktop)	// 不同desktop的不能有关系
					return;
				for(CComponent one = value; one != null; one = one.m_parent)
					if(one == this)		// 防止出现环
						return;
				m_rect = AbsRect;
				if(m_parent != null)
					m_parent.List.Remove(this);
				m_parent = value;
				if(m_parent != null)
					m_parent.List.AddFirst(this);
				AbsRect = m_rect;
			}
		}

		public string Name
		{
			get { return m_name; }
			set
			{
				m_name = (value != null ? value : "");
				if(m_lvi != null)
					m_lvi.Text = m_name;
			}
		}

		public string Class
		{
			get { return m_class; }
			set	{ m_class = (value != null ? value : ""); }
		}

		public Rectangle Rect
		{
			get { return m_rect; }
			set
			{
				Rectangle oldrect = m_rect;
				m_rect = value;
				if(oldrect.Location != m_rect.Location) OnMove();
				if(oldrect.Size		!= m_rect.Size)		OnSize();
			}
		}

		public Rectangle AbsRect
		{
			get
			{
				return m_parent == null ? m_rect : new Rectangle(m_rect.X + m_parent.AbsRect.X,
																 m_rect.Y + m_parent.AbsRect.Y,
																 m_rect.Width, m_rect.Height);
			}
			set
			{
				if(m_parent == null)
					Rect = value;
				else
				{
					Rectangle oldrect = m_rect;
					m_rect.X = value.X - m_parent.AbsRect.X;
					m_rect.Y = value.Y - m_parent.AbsRect.Y;
					m_rect.Width  = value.Width;
					m_rect.Height = value.Height;
					if(oldrect.Location != m_rect.Location)	OnMove();
					if(oldrect.Size		!= m_rect.Size)		OnSize();
				}
			}
		}

		public bool Select
		{
			get { return m_select; }
			set
			{
				bool needrefresh = (m_select != value);
				m_select = value;
				if(needrefresh)
					m_desktop.Control.Invalidate();
			}
		}

		public bool Lock
		{
			get { return m_lock; }
			set
			{
				m_lock = value;
				if(m_lvi != null)
					m_lvi.SubItems[1].Text = (m_lock ? "*" : "");
			}
		}

		public string Comment
		{
			get { return m_comment; }
			set
			{
				m_comment = value;
				if(m_lvi != null)
					m_lvi.SubItems[2].Text = m_comment;
			}
		}

		public bool Hide
		{
			get { return m_hide; }
			set
			{
				bool needrefresh = (m_hide != value);
				m_hide = value;
				if(needrefresh)
					m_desktop.Control.Invalidate();
			}
		}

		public abstract void Dispose();

		public CComponent(CComponentDesktop desktop)
		{
			if(desktop != null)
			{
				m_desktop = desktop;
				Parent = desktop;
				m_render = m_desktop.Render;
				if(m_render != null)
					m_brush = m_render.CreateBrush();
				if(m_desktop.ListView != null)
				{
					m_lvi = m_desktop.ListView.Items.Add("");
					m_lvi.SubItems.Add("");
					m_lvi.SubItems.Add("");
					m_lvi.Tag = this;
				}
			}
		}

		public bool HasChild(CComponent one)
		{
			for(; one != null; one = one.Parent)
				if(one == this)
					return true;
			return false;
		}

		public bool Contain(Point pos)
		{
			return AbsRect.Contains(pos);
		}

		public bool Contain(Rectangle rect)
		{
			return AbsRect.Contains(rect);
		}

		public bool Contain(CComponent one)
		{
			return AbsRect.Contains(one.AbsRect);
		}

		public bool ContainBy(Rectangle rect)
		{
			return rect.Contains(AbsRect);
		}

		public bool ContainBy(CComponent one)
		{
			return one.AbsRect.Contains(AbsRect);
		}

		public bool IntersectWith(Rectangle rect)
		{
			return AbsRect.IntersectsWith(rect);
		}

		public virtual CComponent Clone()
		{
			return null;
		}

		public virtual bool OnLButtonDown(Point pos)
		{
			return false;
		}

		public virtual bool OnLButtonUp(Point pos)
		{
			return false;
		}

		public virtual bool OnLButtonDoubleClick(Point pos)
		{
			return false;
		}

		public virtual bool OnRButtonDown(Point pos)
		{
			return false;
		}

		public virtual bool OnRButtonUp(Point pos)
		{
			return false;
		}

        public virtual void OnKeyDown(KeyEventArgs e)
        {
            return;
        }

		public virtual bool OnMouseMove(Point pos, int button)	// button: 1:左键 2:右键 3:左右键
		{
			int state = GetMouseState(pos);
			if(state > 0 && m_desktop != null)
			{
				m_desktop.OnFocus(this, pos, state);
				return true;
			}

			return false;
		}

		public virtual void OnMove()
		{
		}

		public virtual void OnSize()
		{
		}

		public virtual void OnDraw()
		{
			if(m_render != null && !m_hide)
			{
				Rectangle rect = AbsRect;

				if(m_brush != null)
					m_render.DrawRect(rect, m_brush);

				if(m_select && rect.Width > 0 && rect.Height > 0)
					m_render.DrawBox(new Rectangle(rect.X, rect.Y, rect.Width, rect.Height),
									 Color.FromArgb(192, 255, 0, 255));
			}
		}

		public virtual Form GetDialog()	// 返回设置其属性的对话框
		{
			return null;
		}

		public void GetImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brush.GetImageRect();
			m_brush.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brush.GetImageExpandMode();
		}

		public void SetImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brush.SetImageBlock(rect, w0, w1, h0, h1, mode);
			if(mode == (EExpandMode.H_TILE|EExpandMode.V_TILE) && !(this is CComponentDesktop))
				Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
		}

        // Nuclear/Patcher uses color with RGBA format.
        public static string Color2String(Color color)
        {
            return color.R.ToString() + ',' + color.G.ToString() + ',' + color.B.ToString() + ',' + color.A.ToString();
        }

        public static Color String2Color(string str)
        {
            int a = 0, r = 0, g = 0, b = 0;
            string[] strs = str.Split(',');
            if (strs.Length >= 1) Int32.TryParse(strs[0], out r);
            if (strs.Length >= 2) Int32.TryParse(strs[1], out g);
            if (strs.Length >= 3) Int32.TryParse(strs[2], out b);
            if (strs.Length >= 4) Int32.TryParse(strs[3], out a);
            return Color.FromArgb(a, r, g, b);
        }


		public static int GetAttributeInt(XmlElement elem, string key)
		{
			string value = elem.GetAttribute(key);
			if(value == null) return 0;
			int val = 0;
			Int32.TryParse(value, out val);
			return val;
		}

		public static string GetAttributeString(XmlElement elem, string key)
		{
			string value = elem.GetAttribute(key);
			return value != null ? value : "";
		}

		public static Color GetAttributeColor(XmlElement elem, string key)
		{
			string value = elem.GetAttribute(key);
			return value != null && value.Length > 0 ? String2Color(value) : Color.FromArgb(255, 255, 255, 255);
		}

		public static Color GetAttributeColor(XmlElement elem, string key, Color default_color)
		{
			string value = elem.GetAttribute(key);
			return value != null && value.Length > 0 ? String2Color(value) : default_color;
		}

		public static bool GetAttributeBool(XmlElement elem, string key)
		{
			return elem.GetAttribute(key) == "true";
		}

		public void SaveImage(XmlDocument doc, XmlElement elem, IBrush brush, string tag)
		{
			if(brush == null) return;
			string filename = brush.GetImageFileName();
			if(filename != null && filename.Length > 0)
			{
				Rectangle rect = brush.GetImageRect();
				int w0, w1, h0, h1;
				brush.GetImageBlock(out w0, out w1, out h0, out h1);
				EExpandMode mode = brush.GetImageExpandMode();
				XmlElement image = doc.CreateElement(tag);
				image.SetAttribute("f", filename.Substring(Environment.CurrentDirectory.Length + 1));
				image.SetAttribute("x", rect.X.ToString());
				image.SetAttribute("y", rect.Y.ToString());
				image.SetAttribute("w", rect.Width.ToString());
				image.SetAttribute("h", rect.Height.ToString());
                if (w0 != 0 || w1 != 0 || h0 != 0 || h1 != 0)
                {
                    image.SetAttribute("w0", w0.ToString());
                    image.SetAttribute("w1", w1.ToString());
                    image.SetAttribute("h0", h0.ToString());
                    image.SetAttribute("h1", h1.ToString());
                }
				image.SetAttribute("mw", (mode & EExpandMode.H_MASK) == EExpandMode.H_STRETCH ? "stretch" : "tile");
				image.SetAttribute("mh", (mode & EExpandMode.V_MASK) == EExpandMode.V_STRETCH ? "stretch" : "tile");
				elem.AppendChild(image);
			}
		}

		public void LoadImage(XmlElement elem, IBrush brush, string tag)
		{
			XmlElement image = elem[tag];
			if(image != null)
			{
                string filename = Environment.CurrentDirectory + '\\' + GetAttributeString(image, "f");
				int x = GetAttributeInt(image, "x");
				int y = GetAttributeInt(image, "y");
				int w = GetAttributeInt(image, "w");
				int h = GetAttributeInt(image, "h");
				int w0 = GetAttributeInt(image, "w0");
				int w1 = GetAttributeInt(image, "w1");
				int h0 = GetAttributeInt(image, "h0");
				int h1 = GetAttributeInt(image, "h1");
				EExpandMode mode = (GetAttributeString(image, "mw") == "stretch" ? EExpandMode.H_STRETCH : EExpandMode.H_TILE) |
								   (GetAttributeString(image, "mh") == "stretch" ? EExpandMode.V_STRETCH : EExpandMode.V_TILE);
				if(filename.Length > 0)
					brush.SetImageFile(filename);
				
                if (w0 == 0 && w1 == 0 && h0 == 0 && h1 == 0)
                    brush.SetImageRect(new Rectangle(x, y, w, h), mode);
                else
                    brush.SetImageBlock(new Rectangle(x, y, w, h), w0, w1, h0, h1, mode);
			}
		}

		public virtual void SaveXML(XmlDocument doc, XmlElement elem)
		{
            if( m_xmlAttributes == null )
                m_xmlAttributes = new Dictionary<string, string>();

            m_xmlAttributes["n"] = m_name;
            
            if (string.IsNullOrEmpty(m_class))
                m_xmlAttributes.Remove("class");
            else
                m_xmlAttributes["class"] = m_class;

            if (string.IsNullOrEmpty(m_comment))
                m_xmlAttributes.Remove("comment");
            else
                m_xmlAttributes["comment"] = m_comment;

            if (m_lock)
                m_xmlAttributes["lock"] = "true";
            else
                m_xmlAttributes.Remove("lock");
			
            //elem.SetAttribute("n", m_name);
            //elem.SetAttribute("class", m_class);
            //elem.SetAttribute("comment", m_comment);
            //elem.SetAttribute("lock", m_lock ? "true" : "false");

			if(m_parent == m_desktop)
			{
				LinkedList<KeyValuePair<Size, Point>> poses;
				m_desktop.GetLayout(this, out poses);
				foreach(KeyValuePair<Size, Point> pos in poses)
				{
					if(pos.Key != m_desktop.DefaultSize)
					{
                        //elem.SetAttribute("x_" + pos.Key.Width + '_' + pos.Key.Height, pos.Value.X.ToString());
                        //elem.SetAttribute("y_" + pos.Key.Width + '_' + pos.Key.Height, pos.Value.Y.ToString());
                        m_xmlAttributes["x_" + pos.Key.Width + '_' + pos.Key.Height] = pos.Value.X.ToString(); 
						m_xmlAttributes["y_" + pos.Key.Width + '_' + pos.Key.Height] = pos.Value.Y.ToString();
					}
					else
					{
                        //elem.SetAttribute("x", pos.Value.X.ToString());
                        //elem.SetAttribute("y", pos.Value.Y.ToString());
                        m_xmlAttributes["x"] = pos.Value.X.ToString();
                        m_xmlAttributes["y"] = pos.Value.Y.ToString();

					}
				}
			}
			else
			{
                //elem.SetAttribute("x", m_rect.X.ToString());
                //elem.SetAttribute("y", m_rect.Y.ToString());
                m_xmlAttributes["x"] = m_rect.X.ToString();
                m_xmlAttributes["y"] = m_rect.Y.ToString();
			}
			if(this == m_desktop)
			{
                //elem.SetAttribute("w", m_desktop.DefaultSize.Width.ToString());
                //elem.SetAttribute("h", m_desktop.DefaultSize.Height.ToString());

                m_xmlAttributes["w"] = m_desktop.DefaultSize.Width.ToString();
                m_xmlAttributes["h"] = m_desktop.DefaultSize.Height.ToString();
			}
			else
            {
                //elem.SetAttribute("w", m_rect.Width.ToString());
                //elem.SetAttribute("h", m_rect.Height.ToString());
                m_xmlAttributes["w"] = m_rect.Width.ToString();
                m_xmlAttributes["h"] = m_rect.Height.ToString();
            }

			if(m_brush != null)
			{
				//elem.SetAttribute("b", Color2String(m_brush.GetColor()));
                m_xmlAttributes["b"] = Color2String(m_brush.GetColor());
				SaveImage(doc, elem, m_brush, "Image1");
			}

            foreach( KeyValuePair<String, String> attr in m_xmlAttributes )
            {
                elem.SetAttribute( attr.Key, attr.Value );
            }
		}

		public virtual void LoadXML(XmlElement elem)
		{
			m_name = GetAttributeString(elem, "n");
			m_class = GetAttributeString(elem, "class");
			m_comment = GetAttributeString(elem, "comment");
			m_lock = GetAttributeBool(elem, "lock");
			m_rect.X = GetAttributeInt(elem, "x");
			m_rect.Y = GetAttributeInt(elem, "y");
			m_rect.Width = GetAttributeInt(elem, "w");
			m_rect.Height = GetAttributeInt(elem, "h");
			if(m_parent == m_desktop)
			{
				foreach(Size size in m_desktop.AllSize)
				{
					string strx = "x_" + size.Width + '_' + size.Height;
					string stry = "y_" + size.Width + '_' + size.Height;
					if(elem.HasAttribute(strx) && elem.HasAttribute(stry))
					{
						int x = GetAttributeInt(elem, strx);
						int y = GetAttributeInt(elem, stry);
						m_desktop.SetLayout(this, size, new Point(x, y));
					}
					else
					{
						m_desktop.SetLayout(this, size, new Point(m_rect.X, m_rect.Y));
					}
				}
			}

			OnSize();

			if(m_brush != null)
			{
				m_brush.SetColor(GetAttributeColor(elem, "b"));
				LoadImage(elem, m_brush, "Image1");
			}

			Comment = Comment;
			Lock = Lock;

            // 将所有属性加载到自定义的配置表中
            if (elem.Attributes.Count > 0)
            {
                m_xmlAttributes = new Dictionary<string, string>();
            }

            for (int c = 0; c < elem.Attributes.Count; c++)
            {
                XmlNode xn = elem.Attributes.Item(c);
                m_xmlAttributes.Add( xn.Name, xn.Value );
            }
		}

        public int GetMouseState(Point pos)	// 返回鼠标对此组件的状态(0:无,1-8:八方向,9:中心)
		{
			Rectangle rect = AbsRect;
			int state;

					 if(pos.X >= rect.Right  + BORDER_SIZE)	state = 0;
			else	 if(pos.X >= rect.Right  - BORDER_SIZE)
			{
					 if(pos.Y >= rect.Bottom + BORDER_SIZE)	state = 0;
				else if(pos.Y >= rect.Bottom - BORDER_SIZE)	state = 8;	// 右下
				else if(pos.Y >= rect.Top	 + BORDER_SIZE)	state = 7;	// 右
				else if(pos.Y >= rect.Top	 - BORDER_SIZE)	state = 6;	// 右上
				else										state = 0;
			}
			else	 if(pos.X >= rect.Left   + BORDER_SIZE)
			{
					 if(pos.Y >= rect.Bottom + BORDER_SIZE)	state = 0;
				else if(pos.Y >= rect.Bottom - BORDER_SIZE)	state = 5;	// 下
				else if(pos.Y >= rect.Top	 + BORDER_SIZE)	state = 9;	// 中心
				else if(pos.Y >= rect.Top	 - BORDER_SIZE)	state = 4;	// 上
				else										state = 0;
			}
			else	 if(pos.X >= rect.Left   - BORDER_SIZE)
			{
					 if(pos.Y >= rect.Bottom + BORDER_SIZE)	state = 0;
				else if(pos.Y >= rect.Bottom - BORDER_SIZE)	state = 3;	// 左下
				else if(pos.Y >= rect.Top    + BORDER_SIZE)	state = 2;	// 左
				else if(pos.Y >= rect.Top    - BORDER_SIZE)	state = 1;	// 左上
				else										state = 0;
			}
			else											state = 0;

			if(m_lock && state < 9) state = 0;
			return state;
		}

		public virtual bool UpdateRect(int state, Size offset, CComponent top)	// 根据状态和偏移更新位置和大小
		{
			if(state <= 0 || m_lock && (state != 9 || top == this)) return false;

			Point oldpos = m_rect.Location;
			Size oldsize = m_rect.Size;

			switch(state)
			{
			case 1:
				if(m_rect.Width  - offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  -= offset.Width ; m_rect.X += offset.Width ;
				if(m_rect.Height - offset.Height < 0) offset.Height =  m_rect.Height;
				m_rect.Height -= offset.Height; m_rect.Y += offset.Height;
				break;
			case 2:
				if(m_rect.Width  - offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  -= offset.Width ; m_rect.X += offset.Width ;
				break;
			case 3:
				if(m_rect.Width  - offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  -= offset.Width ; m_rect.X += offset.Width ;
				if(m_rect.Height + offset.Height < 0) offset.Height = -m_rect.Height;
				m_rect.Height += offset.Height;
				break;
			case 4:
				if(m_rect.Height - offset.Height < 0) offset.Height =  m_rect.Height;
				m_rect.Height -= offset.Height; m_rect.Y += offset.Height;
				break;
			case 5:
				if(m_rect.Height + offset.Height < 0) offset.Height = -m_rect.Height;
				m_rect.Height += offset.Height;
				break;
			case 6:
				if(m_rect.Height - offset.Height < 0) offset.Height =  m_rect.Height;
				m_rect.Height -= offset.Height; m_rect.Y += offset.Height;
				if(m_rect.Width  + offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  += offset.Width ;
				break;
			case 7:
				if(m_rect.Width  + offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  += offset.Width ;
				break;
			case 8:
				if(m_rect.Width  + offset.Width  < 0) offset.Width  =  m_rect.Width ;
				m_rect.Width  += offset.Width ;
				if(m_rect.Height + offset.Height < 0) offset.Height = -m_rect.Height;
				m_rect.Height += offset.Height;
				break;
			case 9:
				m_rect.X += offset.Width;
				m_rect.Y += offset.Height;
				break;
			}

			if(oldpos  != m_rect.Location) OnMove();
			if(oldsize != m_rect.Size)	   OnSize();

			return true;
		}
	}
}
