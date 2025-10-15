using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormTabApple : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentTabApple m_com;

		public string N
		{
			get { return tbName.Text; }
			set { m_com.Name = tbName.Text = value.Trim(); }
		}

		public string C
		{
			get { return tbClass.Text; }
			set { m_com.Class = tbClass.Text = value.Trim(); }
		}

		public int X
		{
			get { int x; return Int32.TryParse(tbX.Text, out x) ? x : 0; }
			set { tbX.Text = value.ToString(); }
		}

		public int Y
		{
			get { int y; return Int32.TryParse(tbY.Text, out y) ? y : 0; }
			set { tbY.Text = value.ToString(); }
		}

		public int W
		{
			get { int w; return Int32.TryParse(tbW.Text, out w) ? w : 20; }
			set { tbW.Text = value.ToString(); }
		}

		public int H
		{
			get { int h; return Int32.TryParse(tbH.Text, out h) ? h : 20; }
			set { tbH.Text = value.ToString(); }
		}

		public Color BkColor
		{
			get { return m_com.BkColor; }
			set
			{
				m_com.BkColor = value;
				bnBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public string Image1Name
		{
			get { return m_com.Brush.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush.GetImageFileName() && !m_com.Brush.SetImageFile(value))
				{
					MessageBox.Show("设置背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public int BtnRadiusSmall
		{
			get { int f; return Int32.TryParse(tbRadiusSmall.Text, out f) ? f : 25; }
			set { tbRadiusSmall.Text = value.ToString(); }
		}

		public int BtnRadiusBig
		{
			get { int f; return Int32.TryParse(tbRadiusBig.Text, out f) ? f : 50; }
			set { tbRadiusBig.Text = value.ToString(); }
		}

		public int Aspect	// 上,下,左,右: 0,1,2,3
		{
			get { return cbbAspect.SelectedIndex; }
			set { cbbAspect.SelectedIndex = value; }
		}

		public ListBox TabAppleList
		{
			get { return lbTabList; }
		}

		public CComponentTabApple.CTabAppleItem Current
		{
			get { return m_com.Current; }
			set
			{
				if(value != null)
				{
					bnTabDelete.Enabled = true;
					bnTabMoveUp.Enabled = true;
					bnTabMoveDown.Enabled = true;
					bnBtnImage0.Enabled = true;
					bnBtnImage1.Enabled = true;
					bnBtnImage2.Enabled = true;
					bnBtnImage3.Enabled = true;
					bnBtnImage5.Enabled = true;
					bnBtnImage6.Enabled = true;
					bnBtnImage7.Enabled = true;
					bnBtnImage8.Enabled = true;
					bnPanelBkColor.Enabled = true;
					bnPanelImage1.Enabled = true;
					if(m_com.Current != null)
						m_com.Current.BtnBrushNow = m_com.Current.BtnBrush1;
				}
				else if(value == null)
				{
					bnTabDelete.Enabled = false;
					bnTabMoveUp.Enabled = false;
					bnTabMoveDown.Enabled = false;
					bnBtnImage0.Enabled = false;
					bnBtnImage1.Enabled = false;
					bnBtnImage2.Enabled = false;
					bnBtnImage3.Enabled = false;
					bnBtnImage5.Enabled = false;
					bnBtnImage6.Enabled = false;
					bnBtnImage7.Enabled = false;
					bnBtnImage8.Enabled = false;
					bnPanelBkColor.Enabled = false;
					bnPanelImage1.Enabled = false;
				}
				m_com.Current = value;
				if(value != null)
				{
					BtnBrush0 = value.BtnBrush0;
					BtnBrush1 = value.BtnBrush1;
					BtnBrush2 = value.BtnBrush2;
					BtnBrush3 = value.BtnBrush3;
					BtnBrush5 = value.BtnBrush5;
					BtnBrush6 = value.BtnBrush6;
					BtnBrush7 = value.BtnBrush7;
					BtnBrush8 = value.BtnBrush8;
					PanelBkColor = value.Container.Brush.GetColor();
					PanelBrush1 = value.Container.Brush;
					m_com.Current.BtnBrushNow = value.BtnBrush3;
				}
			}
		}

		public IBrush BtnBrush0
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush0 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush0 = value;
				bnBtnImage0.Text = (m_com.Current.BtnBrush0 != null ? m_com.Current.BtnBrush0.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush0 = value;
			}
		}

		public IBrush BtnBrush1
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush1 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush1 = value;
				bnBtnImage1.Text = (m_com.Current.BtnBrush1 != null ? m_com.Current.BtnBrush1.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush1 = value;
			}
		}

		public IBrush BtnBrush2
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush2 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush2 = value;
				bnBtnImage2.Text = (m_com.Current.BtnBrush2 != null ? m_com.Current.BtnBrush2.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush2 = value;
			}
		}

		public IBrush BtnBrush3
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush3 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush3 = value;
				bnBtnImage3.Text = (m_com.Current.BtnBrush3 != null ? m_com.Current.BtnBrush3.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush3 = value;
			}
		}

		public IBrush BtnBrush5
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush5 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush5 = value;
				bnBtnImage5.Text = (m_com.Current.BtnBrush5 != null ? m_com.Current.BtnBrush5.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush5 = value;
			}
		}

		public IBrush BtnBrush6
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush6 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush6 = value;
				bnBtnImage6.Text = (m_com.Current.BtnBrush6 != null ? m_com.Current.BtnBrush6.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush6 = value;
			}
		}

		public IBrush BtnBrush7
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush7 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush7 = value;
				bnBtnImage7.Text = (m_com.Current.BtnBrush7 != null ? m_com.Current.BtnBrush7.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush7 = value;
			}
		}

		public IBrush BtnBrush8
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush8 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush8 = value;
				bnBtnImage8.Text = (m_com.Current.BtnBrush8 != null ? m_com.Current.BtnBrush8.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush8 = value;
			}
		}

		public string BtnImage0Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush0.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush0.GetImageFileName() && !m_com.Current.BtnBrush0.SetImageFile(value))
				{
					MessageBox.Show("设置小按钮图(无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage0.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BtnImage1Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush1.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush1.GetImageFileName() && !m_com.Current.BtnBrush1.SetImageFile(value))
				{
					MessageBox.Show("设置小按钮图(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
				if(value != null) BtnBrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
			}
		}

		public string BtnImage2Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush2.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush2.GetImageFileName() && !m_com.Current.BtnBrush2.SetImageFile(value))
				{
					MessageBox.Show("设置小按钮图(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage2.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BtnImage3Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush3.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush3.GetImageFileName() && !m_com.Current.BtnBrush3.SetImageFile(value))
				{
					MessageBox.Show("设置小按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
				if(value != null) BtnBrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
			}
		}

		public string BtnImage5Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush5.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush5.GetImageFileName() && !m_com.Current.BtnBrush5.SetImageFile(value))
				{
					MessageBox.Show("设置大按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage5.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BtnImage6Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush6.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush6.GetImageFileName() && !m_com.Current.BtnBrush6.SetImageFile(value))
				{
					MessageBox.Show("设置大按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage6.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BtnImage7Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush7.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush7.GetImageFileName() && !m_com.Current.BtnBrush7.SetImageFile(value))
				{
					MessageBox.Show("设置大按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage7.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BtnImage8Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush8.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush8.GetImageFileName() && !m_com.Current.BtnBrush8.SetImageFile(value))
				{
					MessageBox.Show("设置大按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage8.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public Color PanelBkColor
		{
			get { return m_com.Current != null ? m_com.Current.Container.Brush.GetColor() : new Color(); }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.Container.Brush.SetColor(value);
				bnPanelBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnPanelBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnPanelBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public IBrush PanelBrush1
		{
			get { return m_com.Current != null ? m_com.Current.Container.Brush : null; }
			set
			{
				if(m_com.Current == null)
				{
					bnPanelImage1.Text = "";
					return;
				}
				m_com.Current.Container.Brush = value;
				bnPanelImage1.Text = (m_com.Current.Container.Brush != null ? m_com.Current.Container.Brush.GetImageFileName() : "");
			}
		}

		public string PanelImage1Name
		{
			get { return m_com.Current != null ? m_com.Current.Container.Brush.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.Container.Brush.GetImageFileName() && !m_com.Current.Container.Brush.SetImageFile(value))
				{
					MessageBox.Show("设置面板背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnPanelImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public void GetBtnImage0Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush0.GetImageRect();
			m_com.Current.BtnBrush0.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush0.GetImageExpandMode();
		}

		public void GetBtnImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush1.GetImageRect();
			m_com.Current.BtnBrush1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush1.GetImageExpandMode();
		}

		public void GetBtnImage2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush2.GetImageRect();
			m_com.Current.BtnBrush2.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush2.GetImageExpandMode();
		}

		public void GetBtnImage3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush3.GetImageRect();
			m_com.Current.BtnBrush3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush3.GetImageExpandMode();
		}

		public void GetBtnImage5Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush5.GetImageRect();
			m_com.Current.BtnBrush5.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush5.GetImageExpandMode();
		}

		public void GetBtnImage6Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush6.GetImageRect();
			m_com.Current.BtnBrush6.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush6.GetImageExpandMode();
		}

		public void GetBtnImage7Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush7.GetImageRect();
			m_com.Current.BtnBrush7.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush7.GetImageExpandMode();
		}

		public void GetBtnImage8Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush8.GetImageRect();
			m_com.Current.BtnBrush8.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush8.GetImageExpandMode();
		}

		public void GetPanelImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.Container.Brush.GetImageRect();
			m_com.Current.Container.Brush.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.Container.Brush.GetImageExpandMode();
		}

		public void SetBtnImage0Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush0.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush1.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush2.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush3.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage5Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush5.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage6Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush6.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage7Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush7.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBtnImage8Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush8.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetPanelImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.Container.Brush.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public bool IsViewOnly
		{
			get { return cbViewOnly.Checked; }
			set { cbViewOnly.Checked = value; }
		}

		public string TipInfo
		{
			get { return tbTipInfo.Text; }
			set { tbTipInfo.Text = value; }
		}

		public CFormTabApple(CFormMain mainform, CComponentTabApple com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Enter)
			{
				tb_Leave(null, null);
			}
			base.OnKeyDown(e);
		}

		private void tb_Leave(object sender, EventArgs e)
		{
			N = N;
			C = C;

			m_com.Rect = new Rectangle(X, Y, W, H);
			X = m_com.Rect.X;
			Y = m_com.Rect.Y;
			W = m_com.Rect.Width;
			H = m_com.Rect.Height;

			BtnRadiusSmall = BtnRadiusSmall;
			BtnRadiusBig = BtnRadiusBig;

			m_mainform.Panel.Invalidate();
		}

		private void bnBkColor_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = PointToScreen(bnBkColor.Location);
			pos.X += bnBkColor.Width - formcolor.Width;
			pos.Y += bnBkColor.Height;
			formcolor.Location = pos;

			formcolor.Color = BkColor;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				BkColor = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImage1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				Image1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImage1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImage1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnTabAdd_Click(object sender, EventArgs e)
		{
			CComponentTabApple.CTabAppleItem item = new CComponentTabApple.CTabAppleItem(m_com.Render, m_com);
			item.Container = new CComponentTabAppleContainer(m_com);
			lbTabList.SelectedIndex = lbTabList.Items.Add(item);
		}

		private void bnTabDelete_Click(object sender, EventArgs e)
		{
			int i = lbTabList.SelectedIndex;
			if(i >= 0)
			{
				CComponentTabApple.CTabAppleItem item = lbTabList.Items[i] as CComponentTabApple.CTabAppleItem;

				lbTabList.Items.RemoveAt(i);
				if(i >= lbTabList.Items.Count) i = lbTabList.Items.Count - 1;
				lbTabList.SelectedIndex = i;

				item.Container.ClearComponent();
			}
		}

		private void bnTabMoveUp_Click(object sender, EventArgs e)
		{
			int i = lbTabList.SelectedIndex;
			if(i <= 0) return;
			object obj = lbTabList.Items[i];
			lbTabList.Items[i] = lbTabList.Items[i - 1];
			lbTabList.Items[i - 1] = obj;
			lbTabList.SelectedIndex = i - 1;
		}

		private void bnTabMoveDown_Click(object sender, EventArgs e)
		{
			int i = lbTabList.SelectedIndex;
			if(i < 0 || i >= lbTabList.Items.Count - 1) return;
			object obj = lbTabList.Items[i];
			lbTabList.Items[i] = lbTabList.Items[i + 1];
			lbTabList.Items[i + 1] = obj;
			lbTabList.SelectedIndex = i + 1;
		}

		private void lbTabList_SelectedIndexChanged(object sender, EventArgs e)
		{
			object obj = lbTabList.SelectedItem;
			Current = (obj != null ? obj as CComponentTabApple.CTabAppleItem : null);
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage0_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush0);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage0Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage0Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage0Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						BtnImage1Name = formimage.FileName;
						BtnImage2Name = formimage.FileName;
						BtnImage3Name = formimage.FileName;
						Rectangle rect = formimage.SelectRect;
						if(formimage.HasBlock)
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage1Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage2Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage3Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
						}
						else
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage1Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage2Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage3Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage1_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush1);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage1Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage2_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush2);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage2Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage2Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage2Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage3_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage3Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage5_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush5);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage5Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage5Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage5Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						BtnImage6Name = formimage.FileName;
						BtnImage7Name = formimage.FileName;
						BtnImage8Name = formimage.FileName;
						Rectangle rect = formimage.SelectRect;
						if(formimage.HasBlock)
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage6Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage7Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage8Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
						}
						else
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage6Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage7Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							SetBtnImage8Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage6_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush6);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage6Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage6Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage6Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage7_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush7);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage7Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage7Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage7Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnBtnImage8_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush8);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage8Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage8Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage8Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
//				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnPanelImage1_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.Container.Brush);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				PanelImage1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetPanelImage1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetPanelImage1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
				if(!hasoldimage) PanelBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void bnPanelBkColor_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormColor formcolor = new CFormColor();
			Point pos = bnPanelBkColor.Parent.PointToScreen(bnPanelBkColor.Location);
			pos.X += bnPanelBkColor.Width - formcolor.Width;
			pos.Y += bnPanelBkColor.Height;
			formcolor.Location = pos;

			formcolor.Color = PanelBkColor;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				PanelBkColor = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void cbbAspect_SelectedIndexChanged(object sender, EventArgs e)
		{
			m_com.OnSize();
		}

		private void bnBtnImage0_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
				Current.BtnBrushNow = Current.BtnBrush0;
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage1_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
				Current.BtnBrushNow = Current.BtnBrush1;
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage2_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
				Current.BtnBrushNow = Current.BtnBrush2;
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage3_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
				Current.BtnBrushNow = Current.BtnBrush3;
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage_MouseLeave(object sender, EventArgs e)
		{
			if(Current != null)
				Current.BtnBrushNow = Current.BtnBrush3;
			m_mainform.Panel.Invalidate();
		}
	}
}
