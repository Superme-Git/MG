using System;
using System.Drawing;
using System.Windows.Forms;
using System.Collections.Generic;

namespace GUIEditor2
{
	public partial class CFormStatic : Form
	{
		private readonly CFormMain m_mainform;
		private readonly CComponentStatic m_com;

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

        public Color FontColor
        {
            get { return m_com.FontColor; }
            set
            {
                m_com.FontColor = value;
                bnColor.ForeColor = Color.FromArgb(value.R, value.G, value.B);
                bnColor.BackColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
                bnColor.Text = "R:" + value.R.ToString() + " G:" + value.G.ToString() + " B:" + value.B.ToString();
            }
        }

		public Color BkColor
		{
			get { return m_com.Brush.GetColor(); }
			set
			{
				m_com.Brush.SetColor(value);
				bnBkColor.BackColor = Color.FromArgb(value.R, value.G, value.B);
				bnBkColor.ForeColor = (value.R + value.G + value.B > 255 * 3 / 2 ? Color.Black : Color.White);
                bnBkColor.Text = "R:" + value.R.ToString() + " G:" + value.G.ToString() + " B:" + value.B.ToString();
			}
		}

/*
		public string Image1Name
		{
			get { return m_com.Brush.GetImageFileName(); }
			set
			{
				if(value != m_com.Brush.GetImageFileName() && !m_com.Brush.SetImageFile(value))
				{
					MessageBox.Show("设置图片(正常态)失败", "GUIEditor2 - 错误", MessageBoxButtons.OK, MessageBoxIcon.Error);
					value = null;
				}
				bnImage1.Text = (value != null ? value.Substring(m_mainform.ImagePath.Length) : "");
			}
		}
*/

        //public string FontFormat
        //{
        //    get { return cbbFont.Text.Trim(); }
        //    set { cbbFont.Text = value.Trim(); m_mainform.Panel.Invalidate(); }
        //}

		public string T
		{
			get { return tbText.Text; }
			set { tbText.Text = value; }
		}

		public bool Center
		{
			get { return cbCenter.Checked ; }
			set { cbCenter.Checked = value; }
		}

		public bool Transparent
		{
			get { return cbTransparent.Checked; }
			set { cbTransparent.Checked = value; }
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

		public CFormStatic(CFormMain mainform, CComponentStatic com)
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

        //private void cbbFont_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //    FontFormat = FontFormat;
        //    m_mainform.Panel.Invalidate();
        //}

		private void tbText_TextChanged(object sender, EventArgs e)
		{
			m_mainform.Panel.Invalidate();
		}

		private void cbCenter_CheckedChanged(object sender, EventArgs e)
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

        private void bnColor_Click(object sender, EventArgs e)
        {
            CFormColor formcolor = new CFormColor();
            Point pos = PointToScreen(bnColor.Location);
            pos.X += bnColor.Width - formcolor.Width;
            pos.Y += bnColor.Height;
            formcolor.Location = pos;
            formcolor.Color = FontColor;
            if (formcolor.ShowDialog(this) == DialogResult.OK)
            {
                FontColor = formcolor.Color;
                m_mainform.Panel.Invalidate();
            }
        }
/*
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
*/

        //private void cbbFont_DropDown(object sender, EventArgs e)
        //{
        //    cbbFont.Items.Clear();
        //    foreach(string name in CFontProfile.GetFormatKeys())
        //        cbbFont.Items.Add(name);
        //}
	}
}
