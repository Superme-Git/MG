using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormColor : Form
	{
		private byte m_a, m_r, m_g, m_b;

		public Color Color
		{
			get { return Color.FromArgb(m_a, m_r, m_g, m_b); }
			set
			{
				m_a = value.A; tbAlpha.Text = m_a.ToString();
				m_r = value.R; tbRed.Text	= m_r.ToString();
				m_g = value.G; tbGreen.Text = m_g.ToString();
				m_b = value.B; tbBlue.Text	= m_b.ToString();
			}
		}

		public Color PureColor
		{
			get { return Color.FromArgb(m_r, m_g, m_b); }
			set
			{
				m_r = value.R; tbRed.Text	= m_r.ToString();
				m_g = value.G; tbGreen.Text = m_g.ToString();
				m_b = value.B; tbBlue.Text	= m_b.ToString();
			}
		}

		public CFormColor()
		{
			InitializeComponent();
		}

		private void tbRed_TextChanged(object sender, EventArgs e)
		{
			if(!Byte.TryParse(tbRed.Text, out m_r))
				m_r = 0;
			lbPreview.BackColor = PureColor;
		}

		private void tbGreen_TextChanged(object sender, EventArgs e)
		{
			if(!Byte.TryParse(tbGreen.Text, out m_g))
				m_g = 0;
			lbPreview.BackColor = PureColor;
		}

		private void tbBlue_TextChanged(object sender, EventArgs e)
		{
			if(!Byte.TryParse(tbBlue.Text, out m_b))
				m_b = 0;
			lbPreview.BackColor = PureColor;
		}

		private void bnAdvance_Click(object sender, EventArgs e)
		{
			dlgColor.Color = Color;
			if(dlgColor.ShowDialog() == DialogResult.OK)
			{
				PureColor = dlgColor.Color;
				lbPreview.BackColor = PureColor;
				DialogResult = DialogResult.OK;
			}
		}

		private void bnOK_Click(object sender, EventArgs e)
		{
			if(!Byte.TryParse(tbAlpha.Text, out m_a))
				m_a = 0;

			DialogResult = DialogResult.OK;
		}

		private void tbRed_MouseEnter(object sender, EventArgs e)
		{
			tbRed.SelectAll();
			tbRed.Focus();
		}

		private void tbGreen_MouseEnter(object sender, EventArgs e)
		{
			tbGreen.SelectAll();
			tbGreen.Focus();
		}

		private void tbBlue_MouseEnter(object sender, EventArgs e)
		{
			tbBlue.SelectAll();
			tbBlue.Focus();
		}

		private void tbAlpha_MouseEnter(object sender, EventArgs e)
		{
			tbAlpha.SelectAll();
			tbAlpha.Focus();
		}
	}
}
