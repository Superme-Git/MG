using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormComboBox : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentComboBox m_com;

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
			get { return m_com.BrushBox.GetColor(); }
			set
			{
				m_com.BrushBox.SetColor(value);
				m_com.BrushList.SetColor(value);
				m_com.Brush0.SetColor(value);
				m_com.Brush1.SetColor(value);
				m_com.Brush2.SetColor(value);
				m_com.Brush3.SetColor(value);
			}
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

		public string ImageBoxName
		{
			get { return m_com.BrushBox.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushBox.GetImageFileName() && !m_com.BrushBox.SetImageFile(value))
				{
					MessageBox.Show("设置文本框背景失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageBox.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageListName
		{
			get { return m_com.BrushList.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushList.GetImageFileName() && !m_com.BrushList.SetImageFile(value))
				{
					MessageBox.Show("设置列表框框背景失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageList.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string Image0Name
		{
			get { return m_com.Brush0.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush0.GetImageFileName() && !m_com.Brush0.SetImageFile(value))
				{
					MessageBox.Show("设置按钮图片(无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage0.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string Image1Name
		{
			get { return m_com.Brush1.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush1.GetImageFileName() && !m_com.Brush1.SetImageFile(value))
				{
					MessageBox.Show("设置按钮图片(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string Image2Name
		{
			get { return m_com.Brush2.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush2.GetImageFileName() && !m_com.Brush2.SetImageFile(value))
				{
					MessageBox.Show("设置按钮图片(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage2.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string Image3Name
		{
			get { return m_com.Brush3.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush3.GetImageFileName() && !m_com.Brush3.SetImageFile(value))
				{
					MessageBox.Show("设置按钮图片(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public int BtnWidth
		{
			get { int f; return Int32.TryParse(tbBtnWidth.Text, out f) ? f : 20; }
			set { tbBtnWidth.Text = value.ToString(); }
		}

		public int ListBoxW
		{
			get { int f; return Int32.TryParse(tbListBoxW.Text, out f) ? f : 100; }
			set { tbListBoxW.Text = value.ToString(); }
		}

		public int ListBoxH
		{
			get { int f; return Int32.TryParse(tbListBoxH.Text, out f) ? f : 100; }
			set { tbListBoxH.Text = value.ToString(); }
		}

		public int Border
		{
			get { int f; return Int32.TryParse(tbBorder.Text, out f) ? f : 0; }
			set { tbBorder.Text = value.ToString(); }
		}

		public int LineHeight
		{
			get { int f; return Int32.TryParse(tbLineHeight.Text, out f) ? f : 20; }
			set { tbLineHeight.Text = value.ToString(); }
		}

		public string DataType
		{
			get { return cbbDataType.Text; }
			set { cbbDataType.SelectedItem = null; cbbDataType.Text = value; }
		}

		public string ScrollVName
		{
			get { return cbbScrollV.Text; }
			set { cbbScrollV.Text = value; }
		}

		public string ScrollHName
		{
			get { return cbbScrollH.Text; }
			set { cbbScrollH.Text = value; }
		}

		public ComboBox ScrollVControl
		{
			get { return cbbScrollV; }
		}

		public ComboBox ScrollHControl
		{
			get { return cbbScrollH; }
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

		public CFormComboBox(CFormMain mainform, CComponentComboBox com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;

			cbbScrollV.Items.Add("");
			foreach(string key in CScrollBarProfile.GetVKeys())
				cbbScrollV.Items.Add(key);

			cbbScrollH.Items.Add("");
			foreach(string key in CScrollBarProfile.GetHKeys())
				cbbScrollH.Items.Add(key);
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
			BtnWidth = BtnWidth;
			ListBoxW = ListBoxW;
			ListBoxH = ListBoxH;
			Border = Border;

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

		private void bnImageBox_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushBox);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageBoxName = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageBoxParam(formimage.SelectRect, formimage.W0, formimage.W1,
											   formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageBoxParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImageList_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushList);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageListName = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageListParam(formimage.SelectRect, formimage.W0, formimage.W1,
											    formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageListParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
			}
		}

		private void bnImage0_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush0);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				Image0Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImage0Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImage0Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						Image1Name = formimage.FileName;
						Image2Name = formimage.FileName;
						Image3Name = formimage.FileName;
						Rectangle rect = formimage.SelectRect;
						if(formimage.HasBlock)
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImage1Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImage2Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImage3Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
						}
						else
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImage1Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImage2Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImage3Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
			}
		}

		private void bnImage1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush1);
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
				}
				m_com.SetBrushButton(1);
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImage2_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush2);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				Image2Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImage2Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImage2Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
			}
		}

		private void bnImage3_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				Image3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImage3Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImage3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
			}
		}

		private void bnImage0_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushButton(0);
		}

		private void bnImage1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushButton(1);
		}

		private void bnImage2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushButton(2);
		}

		private void bnImage3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushButton(3);
		}

		private void bnImage_MouseLeave(object sender, EventArgs e)
		{
			m_com.SetBrushButton(1);
		}

		private void cbbFont_DropDown(object sender, EventArgs e)
		{
			cbbFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbFont.Items.Add(name);
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
	}
}
