using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormButtonRadio : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentButtonRadio m_com;

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
			get { return m_com.Brush.GetColor(); }
			set
			{
				m_com.Brush0.SetColor(value);
				m_com.Brush1.SetColor(value);
				m_com.Brush2.SetColor(value);
				m_com.Brush3.SetColor(value);
				m_com.Brush4.SetColor(value);
				bnBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public string Image0Name
		{
			get { return m_com.Brush0.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush0.GetImageFileName() && !m_com.Brush0.SetImageFile(value))
				{
					MessageBox.Show("设置图片(无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置图片(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置图片(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置图片(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string Image4Name
		{
			get { return m_com.Brush4.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush4.GetImageFileName() && !m_com.Brush4.SetImageFile(value))
				{
					MessageBox.Show("设置图片(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage4.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string FontFormat
		{
			get { return cbbFont.Text.Trim(); }
			set { cbbFont.Text = value.Trim(); m_mainform.Panel.Invalidate(); }
		}

		public int BoxWidth
		{
			get { int f; return Int32.TryParse(tbBoxWidth.Text, out f) ? f : 20; }
			set { tbBoxWidth.Text = value.ToString(); }
		}

		public int GroupID
		{
			get { int f; return Int32.TryParse(tbGroupID.Text, out f) ? f : 0; }
			set { tbGroupID.Text = value.ToString(); }
		}

		public string T
		{
			get { return tbText.Text; }
			set { tbText.Text = value; }
		}

		public bool HasOnClick
		{
			get { return cbOnClick.Checked; }
			set { cbOnClick.Checked = value; }
		}

		public bool HasButton
		{
			get { return cbButton.Checked; }
			set { cbButton.Checked = value; }
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

		public CFormButtonRadio(CFormMain mainform, CComponentButtonRadio com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;
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

			BoxWidth = BoxWidth;
			GroupID = GroupID;

			m_mainform.Panel.Invalidate();
		}

		private void cbbFont_SelectedIndexChanged(object sender, EventArgs e)
		{
			FontFormat = FontFormat;
			m_mainform.Panel.Invalidate();
		}

		private void tbText_TextChanged(object sender, EventArgs e)
		{
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
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						Image1Name = formimage.FileName;
						Image2Name = formimage.FileName;
						Image3Name = formimage.FileName;
						Image4Name = formimage.FileName;
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
							if(imageorder == 0)
								rect.Y += rect.Height * 2;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImage4Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
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
							if(imageorder == 0)
								rect.Y += rect.Height * 2;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImage4Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
				m_com.SetBrush(0);
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
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrush(1);
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
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrush(2);
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
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrush(3);
			}
		}

		private void bnImage4_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Brush4);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				Image4Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImage4Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImage4Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrush(4);
			}
		}

		private void bnImage0_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrush(0);
		}

		private void bnImage1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrush(1);
		}

		private void bnImage2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrush(2);
		}

		private void bnImage3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrush(3);
		}

		private void bnImage4_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrush(4);
		}

		private void bnImage_MouseLeave(object sender, EventArgs e)
		{
			m_com.SetBrush(1);
		}

		private void cbbFont_DropDown(object sender, EventArgs e)
		{
			cbbFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbFont.Items.Add(name);
		}
	}
}
