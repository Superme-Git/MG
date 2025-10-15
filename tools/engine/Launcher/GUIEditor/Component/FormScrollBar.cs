using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormScrollBar : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentScrollBar m_com;

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
			get { return m_com.BrushUp0.GetColor(); }
			set
			{
				m_com.BrushUp0.SetColor(value);
				m_com.BrushUp1.SetColor(value);
				m_com.BrushUp2.SetColor(value);
				m_com.BrushUp3.SetColor(value);
				m_com.BrushDown0.SetColor(value);
				m_com.BrushDown1.SetColor(value);
				m_com.BrushDown2.SetColor(value);
				m_com.BrushDown3.SetColor(value);
				m_com.BrushSlot.SetColor(value);
				m_com.BrushBlock1.SetColor(value);
				m_com.BrushBlock2.SetColor(value);
				m_com.BrushBlock3.SetColor(value);
				bnBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public string ImageUp0Name
		{
			get { return m_com.BrushUp0.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushUp0.GetImageFileName() && !m_com.BrushUp0.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向上按钮无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageUp0.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageUp1Name
		{
			get { return m_com.BrushUp1.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushUp1.GetImageFileName() && !m_com.BrushUp1.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向上按钮正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageUp1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageUp2Name
		{
			get { return m_com.BrushUp2.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushUp2.GetImageFileName() && !m_com.BrushUp2.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向上按钮焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageUp2.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageUp3Name
		{
			get { return m_com.BrushUp0.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushUp3.GetImageFileName() && !m_com.BrushUp3.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向上按钮按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageUp3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageDown0Name
		{
			get { return m_com.BrushDown0.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushDown0.GetImageFileName() && !m_com.BrushDown0.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向下按钮无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageDown0.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageDown1Name
		{
			get { return m_com.BrushDown1.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushDown1.GetImageFileName() && !m_com.BrushDown1.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向下按钮正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageDown1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageDown2Name
		{
			get { return m_com.BrushDown2.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushDown2.GetImageFileName() && !m_com.BrushDown2.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向下按钮焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageDown2.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageDown3Name
		{
			get { return m_com.BrushDown0.GetImageFileName(); }
			set
			{
				if(value != m_com.BrushDown3.GetImageFileName() && !m_com.BrushDown3.SetImageFile(value))
				{
					MessageBox.Show("设置图片(向下按钮按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageDown3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
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

		public bool IsAutoBlock
		{
			get { return cbAutoBlock.Checked; }
			set { cbAutoBlock.Checked = value; }
		}

		public string TipInfo
		{
			get { return tbTipInfo.Text; }
			set { tbTipInfo.Text = value; }
		}

		public CFormScrollBar(CFormMain mainform, CComponentScrollBar com)
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

		private void bnImageUp0_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushUp0);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageUp0Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageUp0Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageUp0Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						ImageUp1Name = formimage.FileName;
						ImageUp2Name = formimage.FileName;
						ImageUp3Name = formimage.FileName;
						Rectangle rect = formimage.SelectRect;
						if(formimage.HasBlock)
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImageUp1Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageUp2Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageUp3Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
						}
						else
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImageUp1Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageUp2Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageUp3Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
				m_com.SetBrushUp(0);
			}
		}

		private void bnImageUp1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushUp1);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageUp1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageUp1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageUp1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushUp(1);
			}
		}

		private void bnImageUp2_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushUp2);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageUp2Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageUp2Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageUp2Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushUp(2);
			}
		}

		private void bnImageUp3_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushUp3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageUp3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageUp3Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageUp3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushUp(3);
			}
		}

		private void bnImageDown0_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushDown0);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageDown0Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageDown0Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageDown0Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
					if(true)	// 自动调节
					{
						int imageorder = Program.GetMainForm().ImageOrder;
						ImageDown1Name = formimage.FileName;
						ImageDown2Name = formimage.FileName;
						ImageDown3Name = formimage.FileName;
						Rectangle rect = formimage.SelectRect;
						if(formimage.HasBlock)
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImageDown1Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageDown2Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageDown3Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
						}
						else
						{
							if(imageorder == 0)
								rect.Y -= rect.Height * 3;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							m_com.SetImageDown1Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageDown2Param(rect, 0, 0, 0, 0, formimage.Mode);
							rect.Y += rect.Height;
							m_com.SetImageDown3Param(rect, 0, 0, 0, 0, formimage.Mode);
						}
					}
				}
				m_com.SetBrushDown(0);
			}
		}

		private void bnImageDown1_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushDown1);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageDown1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageDown1Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageDown1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushDown(1);
			}
		}

		private void bnImageDown2_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushDown2);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageDown2Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageDown2Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageDown2Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushDown(2);
			}
		}

		private void bnImageDown3_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushDown3);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ImageDown3Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						m_com.SetImageDown3Param(formimage.SelectRect, formimage.W0, formimage.W1,
											 formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageDown3Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
					if(!hasoldimage)
						BkColor = Color.FromArgb(255, 255, 255, 255);
				}
				m_com.SetBrushDown(3);
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

		private void bnImageUp0_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushUp(0);
		}

		private void bnImageUp1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushUp(1);
		}

		private void bnImageUp2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushUp(2);
		}

		private void bnImageUp3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushUp(3);
		}

		private void bnImageDown0_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushDown(0);
		}

		private void bnImageDown1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushDown(1);
		}

		private void bnImageDown2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushDown(2);
		}

		private void bnImageDown3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushDown(3);
		}

		private void bnImageBlock1_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(1);
		}

		private void bnImageBlock2_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(2);
		}

		private void bnImageBlock3_MouseEnter(object sender, EventArgs e)
		{
			m_com.SetBrushBlock(3);
		}

		private void bnImage_MouseLeave(object sender, EventArgs e)
		{
			m_com.SetBrushUp(1);
			m_com.SetBrushDown(1);
			m_com.SetBrushBlock(1);
		}

		private void cbVertical_CheckedChanged(object sender, EventArgs e)
		{
			m_com.Desktop.Control.Invalidate();
		}

		private void bnApplyAll_Click(object sender, EventArgs e)
		{
			if(m_mainform.Filename == null || !m_mainform.Filename.EndsWith("ScrollBarProfile.xml", StringComparison.CurrentCultureIgnoreCase))
			{
				MessageBox.Show("此功能仅在编辑ScrollBarProfile.xml时有效");
				return;
			}

			m_mainform.miFileSave_Click(null, null);
			CScrollBarProfile.Clear();
			CScrollBarProfile.LoadFile(Program.GetMainForm().XMLPath + "ScrollBarProfile.xml");

			new FormScrollBarApply(N).ShowDialog();
		}
	}
}
