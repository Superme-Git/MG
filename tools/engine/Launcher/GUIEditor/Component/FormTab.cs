using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormTab : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentTab m_com;

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

		public int HeadSize
		{
			get { int f; return Int32.TryParse(tbHeadSize.Text, out f) ? f : 20; }
			set { tbHeadSize.Text = value.ToString(); }
		}

		public int BtnWidth
		{
			get { int f; return Int32.TryParse(tbBtnWidth.Text, out f) ? f : 20; }
			set { tbBtnWidth.Text = value.ToString(); }
		}

		public int BtnHeight
		{
			get { int f; return Int32.TryParse(tbBtnHeight.Text, out f) ? f : 20; }
			set { tbBtnHeight.Text = value.ToString(); }
		}

		public int BtnBorder
		{
			get { int f; return Int32.TryParse(tbBtnBorder.Text, out f) ? f : 0; }
			set { tbBtnBorder.Text = value.ToString(); }
		}

		public int Aspect	// 上,下,左,右: 0,1,2,3
		{
			get { return cbbAspect.SelectedIndex; }
			set { cbbAspect.SelectedIndex = value; }
		}

		public ListBox TabList
		{
			get { return lbTabList; }
		}

		public CComponentTab.CTabItem Current
		{
			get { return m_com.Current; }
			set
			{
				if(value != null)
				{
					bnTabDelete.Enabled = true;
					bnTabMoveUp.Enabled = true;
					bnTabMoveDown.Enabled = true;
					tbColName.Enabled = true;
					cbbColFont.Enabled = true;
					bnBtnImage0.Enabled = true;
					bnBtnImage1.Enabled = true;
					bnBtnImage2.Enabled = true;
					bnBtnImage3.Enabled = true;
					bnBtnImage4.Enabled = true;
					bnPanelBkColor.Enabled = true;
					bnPanelImage1.Enabled = true;
					tbPanelBorder.Enabled = true;
					cbbScrollV.Enabled = true;
					cbbScrollH.Enabled = true;
					if(m_com.Current != null)
					{
						m_com.Current.State = 1;
						m_com.Current.BtnBrushNow = m_com.Current.BtnBrush1;
					}
				}
				else if(value == null)
				{
					bnTabDelete.Enabled = false;
					bnTabMoveUp.Enabled = false;
					bnTabMoveDown.Enabled = false;
					tbColName.Enabled = false;
					cbbColFont.Enabled = false;
					bnBtnImage0.Enabled = false;
					bnBtnImage1.Enabled = false;
					bnBtnImage2.Enabled = false;
					bnBtnImage3.Enabled = false;
					bnBtnImage4.Enabled = false;
					bnPanelBkColor.Enabled = false;
					bnPanelImage1.Enabled = false;
					tbPanelBorder.Enabled = false;
					cbbScrollV.Enabled = false;
					cbbScrollH.Enabled = false;
				}
				m_com.Current = value;
				if(value != null)
				{
					ColName = value.Name;
					ColFontFormat = value.FontFormat;
					BtnBrush0 = value.BtnBrush0;
					BtnBrush1 = value.BtnBrush1;
					BtnBrush2 = value.BtnBrush2;
					BtnBrush3 = value.BtnBrush3;
					BtnBrush4 = value.BtnBrush4;
					PanelBkColor = value.Container.Brush.GetColor();
					PanelBrush1 = value.Container.Brush;
					PanelBorder = value.PanelBorder;
					ScrollVName = value.ScrollV;
					ScrollHName = value.ScrollH;
					m_com.Current.State = 3;
					m_com.Current.BtnBrushNow = value.BtnBrush3;
				}
			}
		}

		public string ColName
		{
			get { return tbColName.Text; }
			set { tbColName.Text = value; if(m_com.Current != null) m_com.Current.Name = tbColName.Text; }
		}

		public string ColFontFormat
		{
			get { return cbbColFont.Text.Trim(); }
			set
			{
				cbbColFont.Text = value.Trim();
				m_mainform.Panel.Invalidate();
				if(m_com.Current != null) m_com.Current.FontFormat = value.Trim();
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

		public IBrush BtnBrush4
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush4 : null; }
			set
			{
				if(m_com.Current == null) return;
				m_com.Current.BtnBrush4 = value;
				bnBtnImage4.Text = (m_com.Current.BtnBrush4 != null ? m_com.Current.BtnBrush4.GetImageFileName() : "");
				if(m_com.Current != null) m_com.Current.BtnBrush4 = value;
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
					MessageBox.Show("设置按钮图(无效态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置按钮图(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置按钮图(焦点态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
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
					MessageBox.Show("设置按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage3.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
				if(value != null) BtnBrush3.SetColor(Color.FromArgb(255, 255, 255, 255));
			}
		}

		public string BtnImage4Name
		{
			get { return m_com.Current != null ? m_com.Current.BtnBrush4.GetImageFileName() : null; }
			set
			{
				if(m_com.Current == null) return;
				if(value != m_com.Current.BtnBrush4.GetImageFileName() && !m_com.Current.BtnBrush4.SetImageFile(value))
				{
					MessageBox.Show("设置按钮图(按下态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnBtnImage4.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
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

		public int PanelBorder
		{
			get { int f; return Int32.TryParse(tbPanelBorder.Text, out f) ? f : 0; }
			set { tbPanelBorder.Text = value.ToString(); if(m_com.Current != null) m_com.Current.PanelBorder = value; }
		}

		public string ScrollVName
		{
			get { return cbbScrollV.Text; }
			set { cbbScrollV.Text = value; if(m_com.Current != null) m_com.Current.ScrollV = value; }
		}

		public string ScrollHName
		{
			get { return cbbScrollH.Text; }
			set { cbbScrollH.Text = value; if(m_com.Current != null) m_com.Current.ScrollH = value; }
		}

		public ComboBox ScrollVControl
		{
			get { return cbbScrollV; }
		}

		public ComboBox ScrollHControl
		{
			get { return cbbScrollH; }
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

		public void GetBtnImage4Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			if(m_com.Current == null)
			{
				rect = new Rectangle();
				w0 = w1 = h0 = h1 = 0;
				mode = EExpandMode.H_NONE | EExpandMode.V_NONE;
				return;
			}
			rect = m_com.Current.BtnBrush4.GetImageRect();
			m_com.Current.BtnBrush4.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_com.Current.BtnBrush4.GetImageExpandMode();
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

		public void SetBtnImage4Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			if(m_com.Current == null) return;
			m_com.Current.BtnBrush4.SetImageBlock(rect, w0, w1, h0, h1, mode);
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

		public CFormTab(CFormMain mainform, CComponentTab com)
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
			{
				if(tbColName.Focused)
				{
					tbColName_Leave(null, null);
					tbColName.Focus();
				}
				else if(tbHeadSize.Focused)
					tb_HeadSizeLeave(null, null);
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

			HeadSize = HeadSize;
			BtnWidth = BtnWidth;
			BtnHeight = BtnHeight;
			BtnBorder = BtnBorder;

			PanelBorder = PanelBorder;
			ScrollVName = ScrollVName;
			ScrollHName = ScrollHName;

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
			CComponentTab.CTabItem item = new CComponentTab.CTabItem(m_com.Render, m_com);
			item.Container = new CComponentTabContainer(m_com);
			item.PanelBorder = PanelBorder;
			lbTabList.SelectedIndex = lbTabList.Items.Add(item);
		}

		private void bnTabDelete_Click(object sender, EventArgs e)
		{
			int i = lbTabList.SelectedIndex;
			if(i >= 0)
			{
				CComponentTab.CTabItem item = lbTabList.Items[i] as CComponentTab.CTabItem;

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
			Current = (obj != null ? obj as CComponentTab.CTabItem : null);
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
						BtnImage4Name = formimage.FileName;
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
							if(imageorder == 0)
								rect.Y += rect.Height * 2;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage4Param(rect, formimage.W0, formimage.W1, formimage.H0, formimage.H1, formimage.Mode);
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
							if(imageorder == 0)
								rect.Y += rect.Height * 2;
							else if(imageorder == 1)
								rect.Y += rect.Height;
							SetBtnImage4Param(rect, 0, 0, 0, 0, formimage.Mode);
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

		private void bnBtnImage4_Click(object sender, EventArgs e)
		{
			if(m_com.Current == null) return;
			CFormMainImage formimage = new CFormMainImage();
			bool hasoldimage = formimage.LoadBrush(m_com.Current.BtnBrush4);
			if(formimage.ShowDialog() == DialogResult.OK)
			{
				BtnImage4Name = formimage.FileName;
				if(formimage.FileName != null)
				{
					if(formimage.HasBlock)
					{
						SetBtnImage4Param(formimage.SelectRect, formimage.W0, formimage.W1,
										  formimage.H0, formimage.H1, formimage.Mode);
					}
					else
					{
						SetBtnImage4Param(formimage.SelectRect, 0, 0, 0, 0, formimage.Mode);
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

		private void tb_HeadSizeLeave(object sender, EventArgs e)
		{
			HeadSize = HeadSize;
			m_com.OnSize();
		}

		private void bnBtnImage0_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 0;
				Current.BtnBrushNow = Current.BtnBrush0;
			}
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage1_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 1;
				Current.BtnBrushNow = Current.BtnBrush1;
			}
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage2_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 2;
				Current.BtnBrushNow = Current.BtnBrush2;
			}
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage3_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 3;
				Current.BtnBrushNow = Current.BtnBrush3;
			}
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage4_MouseEnter(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 4;
				Current.BtnBrushNow = Current.BtnBrush4;
			}
			m_mainform.Panel.Invalidate();
		}

		private void bnBtnImage_MouseLeave(object sender, EventArgs e)
		{
			if(Current != null)
			{
				Current.State = 3;
				Current.BtnBrushNow = Current.BtnBrush3;
			}
			m_mainform.Panel.Invalidate();
		}

		private void cbbColFont_Leave(object sender, EventArgs e)
		{
			ColFontFormat = ColFontFormat;
			m_mainform.Panel.Invalidate();
		}

		private void tbColName_Leave(object sender, EventArgs e)
		{
			int i = lbTabList.SelectedIndex;
			if(i < 0) return;
			ColName = ColName;
			lbTabList.Items[i] = Current;
			m_mainform.Panel.Invalidate();
		}

		private void tbColName_TextChanged(object sender, EventArgs e)
		{
			ColName = ColName;
			m_mainform.Panel.Invalidate();
		}

		private void cbbColFont_DropDown(object sender, EventArgs e)
		{
			cbbColFont.Items.Clear();
			foreach(string name in CFontProfile.GetFormatKeys())
				cbbColFont.Items.Add(name);
		}
	}
}
