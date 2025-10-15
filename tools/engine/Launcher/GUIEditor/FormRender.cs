using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;
using System.Text;
using System.IO;
using System.Diagnostics;

namespace GUIEditor2
{
	public partial class CFormRender : UserControl
	{
		private readonly CFormMain m_mainform;
		private readonly IRender m_render;
		private readonly CComponentDesktop m_desktop;
		private int m_scale = 1;
		private Point m_oripos;
		private Point m_oldpos;
		private Point m_oldscrpos;
		private bool m_modified = false;

		public ContextMenuStrip Menu
		{
			get { return mnNew; }
		}

		public IRender Render
		{
			get { return m_render; }
		}

		public CComponentDesktop Desktop
		{
			get { return m_desktop; }
		}

		public int RenderScale
		{
			get { return m_scale; }
			set
			{
				if(m_scale == value) return;
				m_oldscrpos = m_mainform.PanelContainer.AutoScrollPosition;
				Size size = m_mainform.PanelContainer.Size;
				m_oldscrpos.X = (-m_oldscrpos.X + size.Width  / 2) * value / m_scale - size.Width  / 2;
				m_oldscrpos.Y = (-m_oldscrpos.Y + size.Height / 2) * value / m_scale - size.Height / 2;
				Width  = Width  / m_scale * value;
				Height = Height / m_scale * value;
				m_scale = value;
				m_mainform.PanelContainer.AutoScrollPosition = m_oldscrpos;
				Invalidate();
			}
		}

		public Point OldScrPos
		{
			get { return m_oldscrpos; }
		}

		public bool Modified
		{
			get { return m_modified; }
			set { m_modified = value; }
		}

		public CFormRender(CFormMain mainform)
		{
			InitializeComponent();

			m_mainform = mainform;
			SetStyle(ControlStyles.Opaque, true);

			try
			{
				m_render = CRenderFactory.CreateSlimDXRender(this);
				m_desktop = new CComponentDesktop(m_mainform, this, m_mainform.List, m_render);
			}
			catch
			{
				MessageBox.Show("渲染引擎启动失败!\n" +
								"请确认是否安装了DirectX 9.0c\n" +
								"以及渲染引擎所需的库文件:\n" +
								"SlimDX.dll\n" +
								"d3dx9_41.dll",
								"GUIEditor2 - 文件异常", MessageBoxButtons.OK, MessageBoxIcon.Error);
				Environment.Exit(-1);
				return;
			}
		}

		protected override void OnPaint(PaintEventArgs e)
		{
			if(m_render.DrawBegin(this, Color.Black, m_scale, m_scale))
			{
				m_desktop.OnDraw();
				m_render.DrawEnd();
			}
		}

		protected override void OnLostFocus(EventArgs e)
		{
			m_desktop.HasAlt = false;
			m_desktop.HasCtrl = false;
			m_desktop.HasShift = false;
			Point pos = m_mainform.PanelContainer.AutoScrollPosition;
			pos.X = -pos.X;
			pos.Y = -pos.Y;
			m_mainform.ScrPos = pos;

			base.OnLeave(e);
		}

		protected override void OnGotFocus(EventArgs e)
		{
			base.OnGotFocus(e);

			m_mainform.PanelContainer.AutoScrollPosition = m_mainform.ScrPos;
		}

		protected override void OnMouseDown(MouseEventArgs e)
		{
			Point scrpos = m_mainform.PanelContainer.AutoScrollPosition;
			scrpos.X = -scrpos.X;
			scrpos.Y = -scrpos.Y;
			Focus();
			if(scrpos.X !=0 || scrpos.Y != 0)
				m_mainform.PanelContainer.AutoScrollPosition = scrpos;

			m_oripos = m_oldpos = PointToScreen(e.Location);

			Point pos = e.Location;
			pos.X /= m_scale;
			pos.Y /= m_scale;

				 if(e.Button == MouseButtons.Left ) m_desktop.OnLButtonDown(pos);
//			else if(e.Button == MouseButtons.Right) m_desktop.OnRButtonDown(pos);
		}

		protected override void OnMouseUp(MouseEventArgs e)
		{
			Point pos = e.Location;
			pos.X /= m_scale;
			pos.Y /= m_scale;

			if(e.Button == MouseButtons.Right && m_oripos == PointToScreen(e.Location))
			{
				m_desktop.State = 0;
				mnNew.Show(m_oripos);
			}

				 if(e.Button == MouseButtons.Left )	m_desktop.OnLButtonUp(pos);
//			else if(e.Button == MouseButtons.Right)	m_desktop.OnRButtonUp(pos);
		}

		protected override void OnMouseDoubleClick(MouseEventArgs e)
		{
			Point pos = e.Location;
			pos.X /= m_scale;
			pos.Y /= m_scale;

			m_desktop.OnLButtonDoubleClick(pos);
		}

		protected override void OnMouseMove(MouseEventArgs e)
		{
			if(e.Button == MouseButtons.Right)
			{
				Point scrpos = PointToScreen(e.Location);
				Point movpos = m_mainform.PanelContainer.AutoScrollPosition;
				movpos.X = -(movpos.X + scrpos.X - m_oldpos.X);
				movpos.Y = -(movpos.Y + scrpos.Y - m_oldpos.Y);
				m_mainform.PanelContainer.AutoScrollPosition = movpos;
				m_oldpos = scrpos;
			}

			Point pos = e.Location;
			pos.X /= m_scale;
			pos.Y /= m_scale;

			int button = 0;
			if(e.Button == MouseButtons.Left ) button  = 1;
//			if(e.Button == MouseButtons.Right) button += 2;
			m_desktop.OnMouseMove(pos, button);
		}

		protected override void OnMouseWheel(MouseEventArgs e)
		{
			m_oldscrpos = m_mainform.PanelContainer.AutoScrollPosition;
			m_oldscrpos.X = -m_oldscrpos.X;
			m_oldscrpos.Y = -m_oldscrpos.Y;

			if(e.Delta > 0)
			{
				switch(RenderScale)
				{
					case 1: m_mainform.miViewScale2X_Click(null, null); break;
					case 2: m_mainform.miViewScale4X_Click(null, null); break;
					case 4: m_mainform.miViewScale8X_Click(null, null); break;
				}
			}
			else
			{
				switch(RenderScale)
				{
					case 2: m_mainform.miViewScale1X_Click(null, null); break;
					case 4: m_mainform.miViewScale2X_Click(null, null); break;
					case 8: m_mainform.miViewScale4X_Click(null, null); break;
				}
			}

			base.OnMouseWheel(e);
		}

		protected override bool IsInputKey(System.Windows.Forms.Keys key)
		{
			return true;
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Delete)
				m_mainform.miEditDelete_Click(null, null);
			else if(e.KeyCode == Keys.C && e.Control)
				m_mainform.miEditCopy_Click(null, null);
			else if(e.KeyCode == Keys.A && e.Control)
				m_mainform.miEditSelectAll_Click(null, null);
			else if(e.KeyCode == Keys.Q)
				PopupNewMenu();
			else if(e.KeyCode == Keys.V)
				m_mainform.miViewForeImage_Click(null, null);
			else
				m_desktop.OnKeyDown(e);
		}

		protected override void OnKeyUp(KeyEventArgs e)
		{
			m_desktop.OnKeyUp(e);
		}

		public void PopupNewMenu()
		{
			Point pos = PointToClient(Control.MousePosition);
			if(pos.X < 0 || pos.X >= Width || pos.Y < 0 || pos.Y >= Height)
			{
				pos.X = Width  / 2;
				pos.Y = Height / 2;
			}
			mnNew.Show(PointToScreen(pos));
		}

		private void mnNew_Closed(object sender, ToolStripDropDownClosedEventArgs e)
		{
			m_desktop.Selecting = false;
			Invalidate();
		}

		private void miNewBase_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentBase one = new CComponentBase(m_desktop, m_mainform, "Base");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewStatic_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentStatic one = new CComponentStatic(m_desktop, m_mainform, "Static");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewImage_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentImage one = new CComponentImage(m_desktop, m_mainform, "Image");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewScrollBar_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 20;
			if(rect.Height < 5) rect.Height = 100;

			CComponentScrollBar one = new CComponentScrollBar(m_desktop, m_mainform, "ScrollBar");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewTextBox_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentText one = new CComponentText(m_desktop, m_mainform, "Text");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewEditBox_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentEdit one = new CComponentEdit(m_desktop, m_mainform, "Edit");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewFrame_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 100;
			if(rect.Height < 5) rect.Height = 200;

			CComponentFrame one = new CComponentFrame(m_desktop, m_mainform, "Frame");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewButtonImage_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentButtonImage one = new CComponentButtonImage(m_desktop, m_mainform, "ButtonImage");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewButtonText_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 50;
			if(rect.Height < 5) rect.Height = 30;

			CComponentButtonText one = new CComponentButtonText(m_desktop, m_mainform, "ButtonText");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewButtonRadio_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 20;

			CComponentButtonRadio one = new CComponentButtonRadio(m_desktop, m_mainform, "ButtonRadio");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewButtonCheck_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 20;

			CComponentButtonCheck one = new CComponentButtonCheck(m_desktop, m_mainform, "ButtonCheck");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewProgressBar_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 20;

			CComponentProgressBar one = new CComponentProgressBar(m_desktop, m_mainform, "ProgressBar");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewComboBox_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 20;

			CComponentComboBox one = new CComponentComboBox(m_desktop, m_mainform, "ComboBox");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewListBox_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 80;

			CComponentListBox one = new CComponentListBox(m_desktop, m_mainform, "ListBox");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewTable_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 120;
			if(rect.Height < 5) rect.Height = 120;

			CComponentTable one = new CComponentTable(m_desktop, m_mainform, "Table");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewSliderBar_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 80;
			if(rect.Height < 5) rect.Height = 20;

			CComponentSliderBar one = new CComponentSliderBar(m_desktop, m_mainform, "SliderBar");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewPanel_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 120;
			if(rect.Height < 5) rect.Height = 120;

			CComponentPanel one = new CComponentPanel(m_desktop, m_mainform, "Panel");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewTab_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width  < 5) rect.Width  = 120;
			if(rect.Height < 5) rect.Height = 120;

			CComponentTab one = new CComponentTab(m_desktop, m_mainform, "Tab");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X/m_scale, PointToClient(m_oripos).Y/m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}

		private void miNewAppleMenu_Click(object sender, EventArgs e)
		{
			Modified = true;

			Rectangle rect = new Rectangle();	// = m_desktop.SelectRect;
			if(rect.Width < 5) rect.Width = 120;
			if(rect.Height < 5) rect.Height = 120;

			CComponentTabApple one = new CComponentTabApple(m_desktop, m_mainform, "AppleMenu");
			one.AbsRect = new Rectangle(PointToClient(m_oripos).X / m_scale, PointToClient(m_oripos).Y / m_scale, rect.Width, rect.Height);
			one.Parent = m_desktop.GetContainer(one);
			Invalidate();
		}
	}
}
