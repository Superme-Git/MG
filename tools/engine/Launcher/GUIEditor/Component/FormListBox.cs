using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormListBox : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentListBox m_com;

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

		public Color BkColor1
		{
			get { return m_com.BkColor1; }
			set
			{
				m_com.BkColor1 = value;
				bnBkColor1.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor1.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor1.Text = "A:" + value.A.ToString();
			}
		}

		public Color BkColor3
		{
			get { return m_com.BkColor3; }
			set
			{
				m_com.BkColor3 = value;
				bnBkColor3.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor3.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor3.Text = "A:" + value.A.ToString();
			}
		}

		public string FontFormat
		{
			get { return cbbFont.Text.Trim(); }
			set { cbbFont.Text = value.Trim(); m_mainform.Panel.Invalidate(); }
		}

		public string ImageLineName
		{
			get { return m_com.BrushLine.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushLine.GetImageFileName() && !m_com.BrushLine.SetImageFile(value))
				{
					MessageBox.Show("设置背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageLine.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BkImage1Name
		{
			get { return m_com.BkBrush1.GetImageFileName(); }
			set
			{
				if(value != m_com.BkBrush1.GetImageFileName() && !m_com.BkBrush1.SetImageFile(value))
				{
					MessageBox.Show("设置焦点背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBkImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string BkImage3Name
		{
			get { return m_com.BkBrush3.GetImageFileName(); }
			set
			{
				if(value != m_com.BkBrush3.GetImageFileName() && !m_com.BkBrush3.SetImageFile(value))
				{
					MessageBox.Show("设置选中背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBkImage3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public int LineHeight
		{
			get { int f; return Int32.TryParse(tbLineHeight.Text, out f) ? f : 0; }
			set { tbLineHeight.Text = value.ToString(); }
		}

		public string DataType
		{
			get { return cbbDataType.Text; }
			set { cbbDataType.SelectedItem = null; cbbDataType.Text = value; }
		}

		public bool MultiSelect
		{
			get { return cbMultiSelect.Checked; }
			set { cbMultiSelect.Checked = value; }
		}

		public bool OnSelChange
		{
			get { return cbOnSelChange.Checked; }
			set { cbOnSelChange.Checked = value; }
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

		public CFormListBox(CFormMain mainform, CComponentListBox com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;

			foreach(string name in CFontProfile.GetFormatKeys())
				cbbFont.Items.Add(name);
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Enter)
				tb_Leave(null, null);
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

			FontFormat = FontFormat;
			LineHeight = LineHeight;

			m_mainform.Panel.Invalidate();
		}

		private void bnBkColor1_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = PointToScreen(bnBkColor1.Location);
			pos.X += bnBkColor1.Width - formcolor.Width;
			pos.Y += bnBkColor1.Height;
			formcolor.Location = pos;

			formcolor.Color = BkColor1;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				BkColor1 = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnBkColor3_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = PointToScreen(bnBkColor3.Location);
			pos.X += bnBkColor3.Width - formcolor.Width;
			pos.Y += bnBkColor3.Height;
			formcolor.Location = pos;

			formcolor.Color = BkColor3;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				BkColor3 = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImageLine_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushLine);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageLineName = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageLineParam(formimage.SelectRect, formimage.W0, formimage.W1,
											    formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageLineParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage) m_com.BrushLine.SetColor(Color.FromArgb(255, 255, 255, 255));
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnBkImage1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BkBrush1);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BkImage1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetBkImage1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											   formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetBkImage1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage) m_com.BkBrush1.SetColor(Color.FromArgb(255, 255, 255, 255));
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnBkImage3_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BkBrush3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BkImage3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetBkImage3Param(formimage.SelectRect, formimage.W0, formimage.W1,
											   formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetBkImage3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage) m_com.BkBrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void cbbFont_DropDown(object sender, EventArgs e)
		{
			cbbFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbFont.Items.Add(name);
		}
	}
}
