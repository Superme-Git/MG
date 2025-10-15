using System;
using System.Drawing;
using System.Windows.Forms;
using System.Globalization;
using System.Collections.Generic;

namespace GUIEditor2
{
	public partial class CFormMainImage : Form
	{
		private static string s_path = null;
		private readonly Dictionary<string, CFormMain.CImageParam> m_profile_image;
		private CFormRenderImage m_panelcontrol;
		private Point m_scrpos;

		public SplitterPanel PanelContainer
		{
			get { return spImage.Panel1; }
		}

		public CFormRenderImage Panel
		{
			get { return m_panelcontrol; }
		}

		public bool HasImage
		{
			get { return cbImage.Checked; }
			set { cbImage.Checked = value; }
		}

		public bool HasBlock
		{
			get { return cbBlock.Checked; }
			set { cbBlock.Checked = value; }
		}

		public Color BkColor
		{
			get { return Program.GetMainForm().ImageBkColor; }
			set
			{
				Program.GetMainForm().ImageBkColor = value;
				bnBkColor.BackColor = value;
//				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
//				bnBkColor.Text = "A:" + value.A.ToString();
				m_panelcontrol.Invalidate();
			}
		}

		public string FileName
		{
			get	// 绝对路径
			{
				if(m_panelcontrol.Desktop != null)
					return m_panelcontrol.Desktop.Brush.GetImageFileName();
				else
					return null;
			}
			set	// 相对路径
			{
				if(m_panelcontrol.SetImage(value) && value != null)
					tbFileName.Text = value.Substring(s_path.Length);
				else if(m_panelcontrol.Desktop == null)
					tbFileName.Text = "";
			}
		}

		public Rectangle SelectRect
		{
			get
			{
				if(m_panelcontrol.Desktop != null)
					return m_panelcontrol.Desktop.SelectComponent.Rect;
				else
					return new Rectangle();
			}
			set
			{
				if(m_panelcontrol.Desktop != null)
					m_panelcontrol.Desktop.SelectComponent.Rect = value;
			}
		}

		public int W0
		{
			get { int x; return Int32.TryParse(tbBlockW0.Text, out x) ? x : 0; }
			set { tbBlockW0.Text = value.ToString(); }
		}

		public int W1
		{
			get { int x; return Int32.TryParse(tbBlockW1.Text, out x) ? x : 0; }
			set { tbBlockW1.Text = value.ToString(); }
		}

		public int H0
		{
			get { int x; return Int32.TryParse(tbBlockH0.Text, out x) ? x : 0; }
			set { tbBlockH0.Text = value.ToString(); }
		}

		public int H1
		{
			get { int x; return Int32.TryParse(tbBlockH1.Text, out x) ? x : 0; }
			set { tbBlockH1.Text = value.ToString(); }
		}

		public EExpandMode Mode
		{
			get
			{
				if(!HasBlock && !cbStretchW.Checked && !cbStretchH.Checked)
					return EExpandMode.H_TILE | EExpandMode.V_TILE;
				return (cbStretchW.Checked ? EExpandMode.H_STRETCH : EExpandMode.H_TILE) |
					   (cbStretchH.Checked ? EExpandMode.V_STRETCH : EExpandMode.V_TILE);
			}
			set
			{
				cbStretchW.Checked = ((value & EExpandMode.H_MASK) == EExpandMode.H_STRETCH);
				cbStretchH.Checked = ((value & EExpandMode.V_MASK) == EExpandMode.V_STRETCH);
			}
		}

		public bool NoAlpha
		{
			get { return Program.GetMainForm().ImageNoAlpha; }
			set
			{
				Program.GetMainForm().ImageNoAlpha = value;
				cbNoAlpha.Checked = value;
			}
		}

		public void SetXY(int x, int y)
		{
			tbSelectX.Text = x.ToString();
			tbSelectY.Text = y.ToString();
		}

		public void SetWH(int w, int h)
		{
			tbSelectW.Text = w.ToString();
			tbSelectH.Text = h.ToString();
		}

		public bool LoadBrush(IBrush brush)
		{
			if((FileName = brush.GetImageFileName()) != null)
			{
				SelectRect = brush.GetImageRect();
				int w0, w1, h0, h1;
				brush.GetImageBlock(out w0, out w1, out h0, out h1);
				W0 = w0; W1 = w1;
				H0 = h0; H1 = h1;
				HasBlock = ((w0 | w1 | h0 | h1) != 0);
				Mode = brush.GetImageExpandMode();
				HasImage = true;
				return true;
			}
			HasImage = false;
			return false;
		}

		public CFormMainImage()
		{
			InitializeComponent();

			m_profile_image = Program.GetMainForm().ProfileImage;
			foreach(KeyValuePair<string, CFormMain.CImageParam> pair in m_profile_image)
				cbbProfile.Items.Add(pair.Key);

			m_panelcontrol = new CFormRenderImage(this);
			spImage.Panel1.Controls.Add(m_panelcontrol);

//			spImage.SplitterDistance = 515;

			cbImage.Checked = false;

			if(s_path == null)
				s_path = Program.GetMainForm().ImagePath;

			dlgOpenFile.InitialDirectory = s_path;

			BkColor = BkColor;
			NoAlpha = NoAlpha;
		}

		private void CFormMainImage_Load(object sender, EventArgs e)
		{
			spImage.Panel1.MouseWheel += new MouseEventHandler(Panel1_MouseWheel);

			if(!HasImage)
			{
				bnFileName_Click(sender, e);
				if(!HasImage)
					DialogResult = DialogResult.Cancel;
			}

			m_panelcontrol.Focus();
		}

		protected override void OnDeactivate(EventArgs e)
		{
			m_scrpos = spImage.Panel1.AutoScrollPosition;
			m_scrpos.X = -m_scrpos.X;
			m_scrpos.Y = -m_scrpos.Y;
			base.OnDeactivate(e);
		}

		protected override void OnActivated(EventArgs e)
		{
			base.OnActivated(e);
			spImage.Panel1.AutoScrollPosition = m_scrpos;
		}

		protected override void OnSizeChanged(EventArgs e)
		{
			m_scrpos = spImage.Panel1.AutoScrollPosition;
			m_scrpos.X = -m_scrpos.X;
			m_scrpos.Y = -m_scrpos.Y;
			base.OnSizeChanged(e);
			spImage.Panel1.AutoScrollPosition = m_scrpos;
		}

		private void Panel1_MouseWheel(object sender, MouseEventArgs e)
		{
			spImage.Panel1.AutoScrollPosition = m_panelcontrol.OldScrPos;	// 不让滚轮使滚动条作用生效,恢复原位置
		}

		private void cbImage_CheckedChanged(object sender, EventArgs e)
		{
			bool check = cbImage.Checked;
			tbFileName.Enabled = check;
			gbSelect.Enabled = check;
			gbBlock.Enabled = check;
			gbExpand.Enabled = check;
		}

		private void cbBlock_CheckedChanged(object sender, EventArgs e)
		{
			bool check = cbBlock.Checked;
			tbBlockW0.Enabled = check;
			tbBlockW1.Enabled = check;
			tbBlockH0.Enabled = check;
			tbBlockH1.Enabled = check;
			tb_Leave(null, null);
		}

		private void bnOK_Click(object sender, EventArgs e)
		{
			if(cbBlock.Checked)
			{
				int w = SelectRect.Width  - W0 - W1;
				int h = SelectRect.Height - H0 - H1;
				if(!cbStretchW.Checked && w < 8 || !cbStretchH.Checked && h < 8)
				{
					DialogResult dr = MessageBox.Show("九宫格中心区域的宽或高过小(小于8)，\n" +
													  "在非拉伸模式下可能会导致渲染效率降低或渲染出错，\n" +
													  "你确认要使用这些值吗？\n" +
													  "\n" +
													  "提示：如果平铺和拉伸的效果相同，推荐选择拉伸模式。",
						"GUIEditor2 - 警告", MessageBoxButtons.YesNo, MessageBoxIcon.Warning, MessageBoxDefaultButton.Button2);
					if(dr != DialogResult.Yes)
						return;
				}
			}
			if(!tbFileName.Enabled)
			{
				m_panelcontrol.Desktop.Brush.SetImageFile(null);
				tbFileName.Text = "";
			}
			DialogResult = DialogResult.OK;
		}

		private void bnCancel_Click(object sender, EventArgs e)
		{
			DialogResult = DialogResult.Cancel;
		}

		private void bnFileName_Click(object sender, EventArgs e)
		{
			string fullpath = System.IO.Path.GetFullPath(dlgOpenFile.InitialDirectory);
			if(dlgOpenFile.ShowDialog() == DialogResult.OK)
			{
				if(!dlgOpenFile.FileName.StartsWith(fullpath, false, CultureInfo.CurrentCulture))
				{
					MessageBox.Show("图像文件所在位置必须在规定的目录或子目录中:\n" + fullpath, "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					return;
				}
				s_path = dlgOpenFile.FileName.Substring(0, fullpath.Length);
				FileName = dlgOpenFile.FileName;
				if(FileName != null)
					cbImage.Checked = true;
			}
		}

		private void bnSelectClear_Click(object sender, EventArgs e)
		{
			SelectRect = new Rectangle(0, 0, 0, 0);
			m_panelcontrol.Invalidate();
		}

		private void bnSelectAll_Click(object sender, EventArgs e)
		{
			SelectRect = new Rectangle(0, 0, m_panelcontrol.Desktop.Rect.Width, m_panelcontrol.Desktop.Rect.Height);
			m_panelcontrol.Invalidate();
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			base.OnKeyDown(e);
			if(e.KeyCode == Keys.Enter)
			{
				tb_Leave(null, null);
				if(m_panelcontrol.Focused)
					DialogResult = DialogResult.OK;
			}
			else if(e.KeyCode == Keys.Escape)
				DialogResult = DialogResult.Cancel;
			else if(e.KeyCode == Keys.Delete && m_panelcontrol.Focused)
				bnSelectClear_Click(null, null);
			else if(e.KeyCode == Keys.A && e.Control && m_panelcontrol.Focused)
				bnSelectAll_Click(null, null);
		}

		private void tb_Leave(object sender, EventArgs e)
		{
			int x, y, w, h;
			if(!Int32.TryParse(tbSelectX.Text, out x)) { x = 0; } tbSelectX.Text = x.ToString();
			if(!Int32.TryParse(tbSelectY.Text, out y)) { y = 0; } tbSelectY.Text = y.ToString();
			if(!Int32.TryParse(tbSelectW.Text, out w)) { w = 0; } tbSelectW.Text = w.ToString();
			if(!Int32.TryParse(tbSelectH.Text, out h)) { h = 0; } tbSelectH.Text = h.ToString();
			SelectRect = new Rectangle(x, y, w, h);

			if(HasBlock)
			{
				if(!Int32.TryParse(tbBlockW0.Text, out x)) { x = 0; } tbBlockW0.Text = x.ToString();
				if(!Int32.TryParse(tbBlockW1.Text, out y)) { y = 0; } tbBlockW1.Text = y.ToString();
				if(!Int32.TryParse(tbBlockH0.Text, out w)) { w = 0; } tbBlockH0.Text = w.ToString();
				if(!Int32.TryParse(tbBlockH1.Text, out h)) { h = 0; } tbBlockH1.Text = h.ToString();
				W0 = x; W1 = y; H0 = w; H1 = h;
			}

			m_panelcontrol.Invalidate();
		}

		private void tbBlockH0_MouseEnter(object sender, EventArgs e)
		{
			tbBlockH0.SelectAll();
			tbBlockH0.Focus();
		}

		private void tbBlockH1_MouseEnter(object sender, EventArgs e)
		{
			tbBlockH1.SelectAll();
			tbBlockH1.Focus();
		}

		private void tbBlockW0_MouseEnter(object sender, EventArgs e)
		{
			tbBlockW0.SelectAll();
			tbBlockW0.Focus();
		}

		private void tbBlockW1_MouseEnter(object sender, EventArgs e)
		{
			tbBlockW1.SelectAll();
			tbBlockW1.Focus();
		}

		private void tbSelectX_MouseEnter(object sender, EventArgs e)
		{
			tbSelectX.SelectAll();
			tbSelectX.Focus();
		}

		private void tbSelectY_MouseEnter(object sender, EventArgs e)
		{
			tbSelectY.SelectAll();
			tbSelectY.Focus();
		}

		private void tbSelectW_MouseEnter(object sender, EventArgs e)
		{
			tbSelectW.SelectAll();
			tbSelectW.Focus();
		}

		private void tbSelectH_MouseEnter(object sender, EventArgs e)
		{
			tbSelectH.SelectAll();
			tbSelectH.Focus();
		}

		private void cbbProfile_SelectedIndexChanged(object sender, EventArgs e)
		{
			string name = cbbProfile.Text.Trim();
			if(!m_profile_image.ContainsKey(name))
				return;

			CFormMain.CImageParam ip = m_profile_image[name];
			SelectRect = new Rectangle(ip.x, ip.y, ip.w, ip.h);
			W0 = ip.w0; W1 = ip.w1;
			H0 = ip.h0; H1 = ip.h1;
			HasBlock = ip.block;
			cbStretchW.Checked = ip.stretchw;
			cbStretchH.Checked = ip.stretchh;

			m_panelcontrol.Invalidate();
		}

		private void bnProfileSave_Click(object sender, EventArgs e)
		{
			string name = cbbProfile.Text.Trim();
			if(name.Length <= 0)
			{
				MessageBox.Show("请输入方案名称", "警告", MessageBoxButtons.OK, MessageBoxIcon.Warning);
				cbbProfile.Focus();
				return;
			}

			CFormMain.CImageParam ip = new CFormMain.CImageParam();
			ip.x = SelectRect.X;
			ip.y = SelectRect.Y;
			ip.w = SelectRect.Width;
			ip.h = SelectRect.Height;
			ip.w0 = W0;
			ip.w1 = W1;
			ip.h0 = H0;
			ip.h1 = H0;
			ip.block = HasBlock;
			ip.stretchw = cbStretchW.Checked;
			ip.stretchh = cbStretchH.Checked;

			if(!m_profile_image.ContainsKey(name))
				cbbProfile.Items.Add(name);			
			m_profile_image[name] = ip;
		}

		private void bnProfileDelete_Click(object sender, EventArgs e)
		{
			string name = cbbProfile.Text.Trim();
			if(name.Length <= 0)
			{
				MessageBox.Show("请输入方案名称", "警告", MessageBoxButtons.OK, MessageBoxIcon.Warning);
				cbbProfile.Focus();
				return;
			}
			cbbProfile.Items.Remove(name);
			m_profile_image.Remove(name);
		}

		private void bnBkColor_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = spImage.Panel2.PointToScreen(bnBkColor.Location);
			pos.X += bnBkColor.Width - formcolor.Width;
			pos.Y += bnBkColor.Height;
			formcolor.Location = pos;

			formcolor.Color = BkColor;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				BkColor = formcolor.Color;
			}
		}

		private void cbNoAlpha_CheckedChanged(object sender, EventArgs e)
		{
			NoAlpha = cbNoAlpha.Checked;
			if(m_panelcontrol.Desktop != null)
				m_panelcontrol.Desktop.Brush.SetAlphaBlend(!cbNoAlpha.Checked);
			m_panelcontrol.Invalidate();
		}
	}
}
