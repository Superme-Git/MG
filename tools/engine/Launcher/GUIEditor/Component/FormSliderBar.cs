using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormSliderBar : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentSliderBar m_com;

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
			get { return m_com.BrushSlot.GetColor(); }
			set
			{
				m_com.BrushSlot.SetColor(value);
				m_com.BrushBlock1.SetColor(value);
				m_com.BrushBlock2.SetColor(value);
				m_com.BrushBlock3.SetColor(value);
				bnBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public string ImageSlotName
		{
			get { return m_com.BrushSlot.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushSlot.GetImageFileName() && !m_com.BrushSlot.SetImageFile(value))
				{
					MessageBox.Show("设置图片(滑动槽)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageSlot.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageBlock1Name
		{
			get { return m_com.BrushBlock1.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushBlock1.GetImageFileName() && !m_com.BrushBlock1.SetImageFile(value))
				{
					MessageBox.Show("设置图片(滑动块正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageBlock1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageBlock2Name
		{
			get { return m_com.BrushBlock2.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushBlock2.GetImageFileName() && !m_com.BrushBlock2.SetImageFile(value))
				{
					MessageBox.Show("设置图片(滑动块焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageBlock2.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageBlock3Name
		{
			get { return m_com.BrushBlock3.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushBlock3.GetImageFileName() && !m_com.BrushBlock3.SetImageFile(value))
				{
					MessageBox.Show("设置图片(滑动块按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageBlock3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}
		
		public bool Vertical
		{
			get { return cbVertical.Checked; }
			set { cbVertical.Checked = value; m_com.Desktop.Control.Invalidate(); }
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

		public CFormSliderBar(CFormMain mainform, CComponentSliderBar com)
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

		private void bnImageSlot_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushSlot);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageSlotName = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageSlotParam(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageSlotParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImageBlock1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushBlock1);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageBlock1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageBlock1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageBlock1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushBlock(1);
			}
		}

		private void bnImageBlock2_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushBlock2);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageBlock2Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageBlock2Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageBlock2Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushBlock(2);
			}
		}

		private void bnImageBlock3_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushBlock3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageBlock3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageBlock3Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageBlock3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushBlock(3);
			}
		}

		private void bnImageBlock1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(0);
		}

		private void bnImageBlock2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(1);
		}

		private void bnImageBlock3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(2);
		}

		private void bnImage_MouseLeave(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(1);
		}

		private void cbVertical_CheckedChanged(object sender, EventArgs e)
		{
			m_com.Desktop.Control.Invalidate();
		}
	}
}
