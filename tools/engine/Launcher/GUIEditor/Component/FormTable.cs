using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormTable : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentTable m_com;

		private CComponentTable.CColItem m_nowitem;

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

		public Color GridColor
		{
			get { return m_com.GridColor; }
			set
			{
				m_com.GridColor = value;
				bnGridColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnGridColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnGridColor.Text = "A:" + value.A.ToString();
			}
		}

		public string HeadFontFormat
		{
			get { return cbbHeadFont.Text.Trim(); }
			set	{ cbbHeadFont.Text = value.Trim(); m_mainform.Panel.Invalidate(); }
		}

		public string FontFormat
		{
			get { return cbbFont.Text.Trim(); }
			set { cbbFont.Text = value.Trim(); m_mainform.Panel.Invalidate(); }
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

		public string ImageHeadName
		{
			get { return m_com.BrushHead.GetImageFileName(); }
			set
			{
				if (value != m_com.BrushHead.GetImageFileName() && !m_com.BrushHead.SetImageFile(value))
				{
					MessageBox.Show("设置表头图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageHead.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string ImageCellName
		{
			get { return m_com.BrushCell.GetImageFileName(); }
			set
			{
				if (value != m_com.BrushCell.GetImageFileName() && !m_com.BrushCell.SetImageFile(value))
				{
					MessageBox.Show("设置表体图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImageCell.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
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

		public int HeadHeight
		{
			get { int f; return Int32.TryParse(tbHeadHeight.Text, out f) ? f : 20; }
			set { tbHeadHeight.Text = value.ToString(); }
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

		public bool DragWidth
		{
			get { return cbDragWidth.Checked; }
			set { cbDragWidth.Checked = value; }
		}

		public bool OnSelChange
		{
			get { return cbOnSelChange.Checked; }
			set { cbOnSelChange.Checked = value; }
		}

		public bool OnHeadClick
		{
			get { return cbOnHeadClick.Checked; }
			set { cbOnHeadClick.Checked = value; }
		}

		public ListBox ColList
		{
			get { return lbColList; }
		}

		public CComponentTable.CColItem NowItem
		{
			get { return m_nowitem; }
			set
			{
				if(m_nowitem == null && value != null)
				{
					bnColDelete.Enabled = true;
					bnColMoveUp.Enabled = true;
					bnColMoveDown.Enabled = true;
					tbColName.Enabled = true;
					tbColWidth.Enabled = true;
					bnColBkColor.Enabled = true;
					bnColImage1.Enabled = true;
				}
				else if(m_nowitem != null && value == null)
				{
					bnColDelete.Enabled = false;
					bnColMoveUp.Enabled = false;
					bnColMoveDown.Enabled = false;
					tbColName.Enabled = false;
					tbColWidth.Enabled = false;
					bnColBkColor.Enabled = false;
					bnColImage1.Enabled = false;
				}
				m_nowitem = value;
				if(value != null)
				{
					ColName = value.Name;
					ColWidth = value.Width;
					ColBkColor = value.BkColor;
					ColBrush = value.Brush;
				}
			}
		}

		public string ColName
		{
			get { return tbColName.Text; }
			set { tbColName.Text = value; if(m_nowitem != null) m_nowitem.Name = tbColName.Text; }
		}

		public int ColWidth
		{
			get { int w; return Int32.TryParse(tbColWidth.Text, out w) ? w : 50; }
			set { tbColWidth.Text = value.ToString(); if(m_nowitem != null) m_nowitem.Width = value; }
		}

		public Color ColBkColor
		{
			get { return m_nowitem != null ? m_nowitem.BkColor : Color.FromArgb(0, 0, 0, 0); }
			set
			{
				if(m_nowitem == null) return;
				m_nowitem.BkColor = value;
				bnColBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnColBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
				bnColBkColor.Text = "A:" + value.A.ToString();
			}
		}

		public IBrush ColBrush
		{
			get { return m_nowitem != null ? m_nowitem.Brush : null; }
			set
			{
				if(m_nowitem == null) return;
				m_nowitem.Brush = value;
				bnColImage1.Text = (m_nowitem.Brush != null ? m_nowitem.Brush.GetImageFileName() : "");
				if(m_nowitem != null) m_nowitem.Brush = value;
			}
		}

		public string ColImage1Name
		{
			get { return m_nowitem != null ? m_nowitem.Brush.GetImageFileName() : null; }
			set
			{
				if(m_nowitem == null) return;
				if(value != m_nowitem.Brush.GetImageFileName() && !m_nowitem.Brush.SetImageFile(value))
				{
					MessageBox.Show("设置列名背景图失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnColImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public void GetColImage1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_nowitem == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_nowitem.Brush.GetImageRect();
			m_nowitem.Brush.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_nowitem.Brush.GetImageExpandMode();
		}

		public void SetColImage1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_nowitem == null) return;
			m_nowitem.Brush.SetImageBlock(rect, w0, w1, h0, h1, mode);
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

		public CFormTable(CFormMain mainform, CComponentTable com)
		{
			InitializeComponent();

			m_mainform = mainform;
			m_com = com;
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Enter)
			{
				if(tbColName.Focused)
				{
					tb_ColNameLeave(null, null);
					tbColName.Focus();
				}
				else if(tbColWidth.Focused)
				{
					tb_ColWidthLeave(null, null);
					tbColWidth.Focus();
				}
				else
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

			HeadFontFormat = HeadFontFormat;
			FontFormat = FontFormat;
			HeadHeight = HeadHeight;
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

		private void bnGridColor_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point pos = PointToScreen(bnGridColor.Location);
			pos.X += bnGridColor.Width - formcolor.Width;
			pos.Y += bnGridColor.Height;
			formcolor.Location = pos;

			formcolor.Color = GridColor;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				GridColor = formcolor.Color;
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
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImageHead_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushHead);
			if (formimage.ShowDialog() == DialogResult.OK)
			{
				ImageHeadName = formimage.FileName;
				if (formimage.FileName != null)
				{
					if (formimage.HasBlock)
					{
						m_com.SetImageHeadParam(formimage.SelectRect, formimage.W0, formimage.W1,
											    formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageHeadParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnImageCell_Click(object sender, EventArgs e)
		{
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.BrushCell);
			if (formimage.ShowDialog() == DialogResult.OK)
			{
				ImageCellName = formimage.FileName;
				if (formimage.FileName != null)
				{
					if (formimage.HasBlock)
					{
						m_com.SetImageCellParam(formimage.SelectRect, formimage.W0, formimage.W1,
											    formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						m_com.SetImageCellParam(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnColAdd_Click(object sender, EventArgs e)
		{
			CComponentTable.CColItem col = new CComponentTable.CColItem();
			col.Name = "列名";
			col.Width = ColWidth;
			col.BkColor = ColBkColor;
			col.Brush = m_com.Render.CreateBrush();
			lbColList.SelectedIndex = lbColList.Items.Add(col);
			tbColName.SelectAll();
			tbColName.Focus();
			m_mainform.Panel.Invalidate();
		}

		private void bnColDelete_Click(object sender, EventArgs e)
		{
			int i = lbColList.SelectedIndex;
			if(i >= 0)
			{
				lbColList.Items.RemoveAt(i);
				if(i >= lbColList.Items.Count) i = lbColList.Items.Count - 1;
				lbColList.SelectedIndex = i;
			}
		}

		private void bnColMoveUp_Click(object sender, EventArgs e)
		{
			int i = lbColList.SelectedIndex;
			if(i <= 0) return;
			object obj = lbColList.Items[i];
			lbColList.Items[i] = lbColList.Items[i - 1];
			lbColList.Items[i - 1] = obj;
			lbColList.SelectedIndex = i - 1;
		}

		private void bnColMoveDown_Click(object sender, EventArgs e)
		{
			int i = lbColList.SelectedIndex;
			if(i < 0 || i >= lbColList.Items.Count - 1) return;
			object obj = lbColList.Items[i];
			lbColList.Items[i] = lbColList.Items[i + 1];
			lbColList.Items[i + 1] = obj;
			lbColList.SelectedIndex = i + 1;
		}

		private void lbColList_SelectedIndexChanged(object sender, EventArgs e)
		{
			object obj = lbColList.SelectedItem;
			if(obj != null)
				NowItem = obj as CComponentTable.CColItem;
			else
				NowItem = null;
		}

		private void bnColBkColor_Click(object sender, EventArgs e)
		{
			if(m_nowitem == null) return;
			CFormColor formcolor = new CFormColor();
			Point pos = bnColBkColor.Parent.PointToScreen(bnColBkColor.Location);
			pos.X += bnColBkColor.Width - formcolor.Width;
			pos.Y += bnColBkColor.Height;
			formcolor.Location = pos;

			formcolor.Color = ColBkColor;

			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				ColBkColor = formcolor.Color;
				m_mainform.Panel.Invalidate();
			}
		}

		private void bnColImage1_Click(object sender, EventArgs e)
		{
			if(m_nowitem == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_nowitem.Brush);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				ColImage1Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetColImage1Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetColImage1Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
					}
				}
				if(!hasoldimage) ColBkColor = Color.FromArgb(255, 255, 255, 255);
			}
		}

		private void tb_ColLeave(object sender, EventArgs e)
		{
			ColName = ColName;
			m_mainform.Panel.Invalidate();
		}

		private void tb_ColWidthLeave(object sender, EventArgs e)
		{
			ColWidth = ColWidth;
			m_mainform.Panel.Invalidate();
		}

		private void tb_ColNameLeave(object sender, EventArgs e)
		{
			int i = lbColList.SelectedIndex;
			if(i < 0) return;
			ColName = ColName;
			lbColList.Items[i] = m_nowitem;
			m_mainform.Panel.Invalidate();
		}

		private void cbbHeadFont_DropDown(object sender, EventArgs e)
		{
			cbbHeadFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbHeadFont.Items.Add(name);
		}

		private void cbbFont_DropDown(object sender, EventArgs e)
		{
			cbbFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbFont.Items.Add(name);
		}

		private void cbbHeadFont_SelectedIndexChanged(object sender, EventArgs e)
		{
			m_mainform.Panel.Invalidate();
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
