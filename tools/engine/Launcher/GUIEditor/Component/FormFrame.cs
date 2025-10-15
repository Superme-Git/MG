using System;
using System.Drawing;
using System.Windows.Forms;
using System.Collections.Generic;

namespace GUIEditor2
{
	public partial class CFormFrame : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentFrame m_com;

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
				if(m_com.Brush.GetImageFileName() != value && !m_com.Brush.SetImageFile(value))
				{
					MessageBox.Show("设置图片(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}

		public string MoveType
		{
			get { return cbbMoveType.Text; }
			set { cbbMoveType.Text = value; }
		}

		public int TitleHeight
		{
			get { int x; return Int32.TryParse(tbTitleHeight.Text, out x) ? x : 0; }
			set { tbTitleHeight.Text = value.ToString(); }
		}

		public string NotifyClass
		{
			get { return tbNotifyClass.Text; }
			set { tbNotifyClass.Text = value; }
		}

		public string OutDir
		{
			get { return tbOutDir.Text; }
			set { tbOutDir.Text = value; }
		}

		public string Include
		{
			get { return tbInclude.Text; }
			set { tbInclude.Text = value; }
		}

		public string NameSpace
		{
			get { return tbNameSpace.Text; }
			set { tbNameSpace.Text = value; }
		}

		public bool AutoClose
		{
			get { return cbAutoClose.Checked; }
			set { cbAutoClose.Checked = value; }
		}

		public bool PopUp
		{
			get { return cbPopUp.Checked; }
			set { cbPopUp.Checked = value; }
		}

		public bool HookEditKey
		{
			get { return cbHookEditKey.Checked; }
			set { cbHookEditKey.Checked = value; }
		}

		public string DefaultFocus
		{
			get { return cbbDefaultFocus.Text; }
			set { cbbDefaultFocus.Text = value; }
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

		public CFormFrame(CFormMain mainform, CComponentFrame com)
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

		private void cbbDefaultFocus_DropDown(object sender, EventArgs e)
		{
			List<string> list = new List<string>();
			m_com.GetAllEditComponentName(ref list);
			cbbDefaultFocus.Items.Clear();
			cbbDefaultFocus.Items.AddRange(list.ToArray());
		}
	}
}
