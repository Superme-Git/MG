using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormRenderImage : UserControl
	{
		private readonly CFormMainImage m_form;
		private readonly IRender m_render;
		private CComponentDesktopImage m_desktop;
		private int m_scale = 1;
		private Point m_oripos;
		private Point m_oldpos;
		private Point m_oldscrpos;

		public IRender Render
		{
			get { return m_render; }
		}

		public CComponentDesktopImage Desktop
		{
			get { return m_desktop; }
		}

		public int RenderScale
		{
			get { return m_scale; }
			set
			{
				if(m_scale == value) return;
				m_oldscrpos = m_form.PanelContainer.AutoScrollPosition;
				Size size = m_form.PanelContainer.Size;
				m_oldscrpos.X = (-m_oldscrpos.X + size.Width  / 2) * value / m_scale - size.Width  / 2;
				m_oldscrpos.Y = (-m_oldscrpos.Y + size.Height / 2) * value / m_scale - size.Height / 2;
				Width  = Width  / m_scale * value;
				Height = Height / m_scale * value;
				m_scale = value;
				m_form.PanelContainer.AutoScrollPosition = m_oldscrpos;
				Invalidate();
			}
		}

		public Point OldScrPos
		{
			get { return m_oldscrpos; }
		}

/*
		private int Round(int v)
		{
			if(v <= 0) return 0;
			int i; --v;
			for(i = 0; v != 0; ++i)
				v >>= 1;
			return 1 << i;
		}
*/

		public bool SetImage(string filename)
		{
			if(filename == null)
			{
				Visible = false;
				if(m_desktop != null && m_desktop.Brush != null)
					m_desktop.Brush.SetImageFile(null);
				return true;
			}

			if(m_desktop != null && m_desktop.Brush != null && filename == m_desktop.Brush.GetImageFileName())
				return true;

			int w, h;
			if(!CRenderFactory.GetImageSize(filename, out w, out h))
			{
				MessageBox.Show("无法打开图像文件或无法获取图像大小", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return false;
			}
			if(w > 2048 || h > 2048)
			{
				MessageBox.Show("此图像文件的像素尺寸是" + w + 'x' + h + "\n编辑器不支持宽或高大于2048像素的图像", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return false;
			}

			miScale1X_Click(null, null);
			if(w > 512 || h > 512)
				m_form.WindowState = FormWindowState.Maximized;

			Width  = w;
			Height = h;
			m_desktop = new CComponentDesktopImage(m_form, this, m_render);

			if(!m_desktop.Brush.SetImageFile(filename))
			{
				Visible = false;
				MessageBox.Show("无法读取图像数据", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return false;
			}
			m_desktop.Brush.SetColor(Color.FromArgb(255, 255, 255, 255));
			m_desktop.Brush.SetAlphaBlend(!m_form.NoAlpha);

			Visible = true;
			Invalidate();
			return true;
		}

		public CFormRenderImage(CFormMainImage form)
		{
			InitializeComponent();

			m_form = form;
			m_render = Program.GetMainForm().Panel.Render;
			Visible = false;
		}

		protected override void OnLoad(EventArgs e)
		{
			SetStyle(ControlStyles.Opaque, true);
			miScale1X.Checked = true;
		}

		protected override void OnPaint(PaintEventArgs e)
		{
			if(m_render != null && m_desktop != null && Visible)
			{
				if(m_render.DrawBegin(this, m_form.BkColor, m_scale, m_scale))
				{
					m_desktop.OnDraw();
					m_render.DrawEnd();
				}
			}
		}

		protected override void OnMouseDown(MouseEventArgs e)
		{
			Point scrpos = m_form.PanelContainer.AutoScrollPosition;
			scrpos.X = -scrpos.X;
			scrpos.Y = -scrpos.Y;
			Focus();
			m_form.PanelContainer.AutoScrollPosition = scrpos;

			m_oripos = m_oldpos = PointToScreen(e.Location);

			if(m_desktop != null)
			{
				Point pos = e.Location;
				pos.X /= m_scale;
				pos.Y /= m_scale;

					 if(e.Button == MouseButtons.Left ) m_desktop.OnLButtonDown(pos);
//				else if(e.Button == MouseButtons.Right) m_desktop.OnRButtonDown(pos);
			}
		}

		protected override void OnMouseUp(MouseEventArgs e)
		{
			if(m_desktop != null)
			{
				Point pos = e.Location;
				pos.X /= m_scale;
				pos.Y /= m_scale;

				if(e.Button == MouseButtons.Right && m_oripos == PointToScreen(e.Location))
				{
					mnImage.Show(m_oripos);
				}

					 if(e.Button == MouseButtons.Left )	m_desktop.OnLButtonUp(pos);
//				else if(e.Button == MouseButtons.Right)	m_desktop.OnRButtonUp(pos);
			}
		}

		protected override void OnMouseDoubleClick(MouseEventArgs e)
		{
			if(m_desktop != null)
			{
				Point pos = e.Location;
				pos.X /= m_scale;
				pos.Y /= m_scale;

				m_desktop.OnLButtonDoubleClick(pos);
			}
		}

		protected override void OnMouseMove(MouseEventArgs e)
		{
			if(e.Button == MouseButtons.Right)
			{
				Point scrpos = PointToScreen(e.Location);
				Point movpos = m_form.PanelContainer.AutoScrollPosition;
				movpos.X = -(movpos.X + scrpos.X - m_oldpos.X);
				movpos.Y = -(movpos.Y + scrpos.Y - m_oldpos.Y);
				m_form.PanelContainer.AutoScrollPosition = movpos;
				m_oldpos = scrpos;
			}

			if(m_desktop != null)
			{
				Point pos = e.Location;
				pos.X /= m_scale;
				pos.Y /= m_scale;

				int button = 0;
				if(e.Button == MouseButtons.Left ) button  = 1;
//				if(e.Button == MouseButtons.Right) button += 2;
				m_desktop.OnMouseMove(pos, button);
			}
		}

		protected override void OnMouseWheel(MouseEventArgs e)
		{
			m_oldscrpos = m_form.PanelContainer.AutoScrollPosition;
			m_oldscrpos.X = -m_oldscrpos.X;
			m_oldscrpos.Y = -m_oldscrpos.Y;

			if(e.Delta > 0)
			{
				switch(RenderScale)
				{
					case 1: miScale2X_Click(null, null); break;
					case 2: miScale4X_Click(null, null); break;
					case 4: miScale8X_Click(null, null); break;
				}
			}
			else
			{
				switch(RenderScale)
				{
					case 2: miScale1X_Click(null, null); break;
					case 4: miScale2X_Click(null, null); break;
					case 8: miScale4X_Click(null, null); break;
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
			if(m_desktop != null)
				m_desktop.OnKeyDown(e);
		}

		protected override void OnKeyUp(KeyEventArgs e)
		{
			if(m_desktop != null)
				m_desktop.OnKeyUp(e);
		}

		private void miScale1X_Click(object sender, EventArgs e)
		{
			miScale1X.Checked = true;
			miScale2X.Checked = false;
			miScale4X.Checked = false;
			miScale8X.Checked = false;
			RenderScale = 1;
		}

		private void miScale2X_Click(object sender, EventArgs e)
		{
			miScale1X.Checked = false;
			miScale2X.Checked = true;
			miScale4X.Checked = false;
			miScale8X.Checked = false;
			if(RenderScale < 2)
				m_form.WindowState = FormWindowState.Maximized;
			RenderScale = 2;
		}

		private void miScale4X_Click(object sender, EventArgs e)
		{
			miScale1X.Checked = false;
			miScale2X.Checked = false;
			miScale4X.Checked = true;
			miScale8X.Checked = false;
			if(RenderScale < 4)
				m_form.WindowState = FormWindowState.Maximized;
			RenderScale = 4;
		}

		private void miScale8X_Click(object sender, EventArgs e)
		{
			miScale1X.Checked = false;
			miScale2X.Checked = false;
			miScale4X.Checked = false;
			miScale8X.Checked = true;
			if(RenderScale < 8)
				m_form.WindowState = FormWindowState.Maximized;
			RenderScale = 8;
		}
	}
}
