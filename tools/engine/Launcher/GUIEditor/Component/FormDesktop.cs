using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormDesktop : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentDesktop m_com;

		public Color BkColor
		{
			get { return m_com.Brush.GetColor(); }
			set
			{
				m_com.Brush.SetColor(value);
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

		public Color Color
		{
			get { return m_com.ForeBrush.GetColor(); }
			set
			{
				m_com.ForeBrush.SetColor(value);
				bnColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnColor.Text = "A:" + value.A.ToString();
			}
		}

		public string ImageName
		{
			get { return m_com.ForeBrush.GetImageFileName(); }
			set
			{
				if(value != m_com.ForeBrush.GetImageFileName() && !m_com.ForeBrush.SetImageFile(value))
				{
					MessageBox.Show("设置前景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public bool HasForeImage
		{
			get { return cbHasForeImage.Checked; }
			set { cbHasForeImage.Checked = value; }
		}

		public CFormDesktop(CFormMain mainform, CComponentDesktop com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;
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

		private void bnColor_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = PointToScreen(bnColor.Location);
			pos.X += bnColor.Width - formcolor.Width;
			pos.Y += bnColor.Height;
			formcolor.Location = pos;

			formcolor.Color = Color;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				Color = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImage_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.ForeBrush);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageName = formimage.FileName;
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
						Color = Color.FromArgb(128, 255, 255, 255);
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void cbHasForeImage_CheckedChanged(object sender, EventArgs e)
		{
			m_com.HasForeImage = cbHasForeImage.Checked;
			m_mainform.Panel.Invalidate();
		}
	}
}
