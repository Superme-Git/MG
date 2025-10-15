using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Drawing;

namespace GUIEditor2
{
	public partial class CFormMainFont : Form
	{
		private class CMessageFilter : IMessageFilter
		{
			private readonly CFormMainFont m_form;
			private readonly IntPtr m_hwnd;

			public CMessageFilter(CFormMainFont form)
			{
				m_form = form;
				m_hwnd = m_form.LvState.Handle;
			}

			public bool PreFilterMessage(ref Message m)
			{
				if(m.HWnd != m_hwnd || m.Msg != 0x0202)	// WM_LBUTTONUP
					return false;
				int x = (int)m.LParam & 0xffff;
				int y = (int)m.LParam >> 16;
				m_form.lvState_MouseClick(this, new MouseEventArgs(MouseButtons.Left, 0, x, y, 0));
				return false;
			}
		}

		private readonly CMessageFilter m_msgfilter;
		private CFormRenderFont m_panelcontrol;
		private readonly Dictionary<int, CFontProfile.CFontParam> m_profile_font = CFontProfile.FontProfile;
		private readonly Dictionary<string, CFontProfile.CFontFormatParam> m_profile_fontformat = CFontProfile.FontFormatProfile;
		private CFontProfile.CFontFormatParam m_nowitem;

		public CFormRenderFont Panel
		{
			get { return m_panelcontrol; }
		}

		public ListView LvState
		{
			get { return lvState; }
		}

		public string PreviewText
		{
			get { return tbPreviewText.Text; }
		}

		public string PreviewFontFormat
		{
			get { return lbList.SelectedItem as string; }
		}

		public Color BkColor
		{
			get { return Program.GetMainForm().FontBkColor; }
			set
			{
				Program.GetMainForm().FontBkColor = value;
				m_panelcontrol.Invalidate();
			}
		}

		public CFormMainFont()
		{
			InitializeComponent();

			for(int i = 0; i < 5; ++i)
				lvState.Items[i].UseItemStyleForSubItems = false;

			gbPreview.Controls.Add(m_panelcontrol = new CFormRenderFont(this));
			m_panelcontrol.Location = new Point(8, 20);

			m_msgfilter = new CMessageFilter(this);
			Application.AddMessageFilter(m_msgfilter);
		}

		private void CFormMainFont_Shown(object sender, EventArgs e)
		{
			lbList.Items.Clear();
			foreach(KeyValuePair<string, CFontProfile.CFontFormatParam> pair in m_profile_fontformat)
				lbList.Items.Add(pair.Key);

			cbbFont.Items.Clear();
			foreach(KeyValuePair<int, CFontProfile.CFontParam> pair in m_profile_font)
				cbbFont.Items.Add(pair.Value);
		}

		protected override void OnHandleDestroyed(EventArgs e)
		{
			Application.RemoveMessageFilter(m_msgfilter);
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Enter && tbName.Focused)
				tbName_Leave(null, null);
			base.OnKeyDown(e);
		}

		private void tbName_Leave(object sender, EventArgs e)
		{
			if(lbList.SelectedItem == null)
				return;
			string oldname = lbList.SelectedItem as string;
			string name = tbName.Text.Trim();
			if(name.Length <= 0)
			{
				MessageBox.Show("请输入字体样式名称", "警告", MessageBoxButtons.OK, MessageBoxIcon.Warning);
				tbName.Focus();
			}
			if(oldname != name && m_profile_fontformat.ContainsKey(name))
			{
				MessageBox.Show("此字体样式名称已经存在", "警告", MessageBoxButtons.OK, MessageBoxIcon.Warning);
				tbName.Text = oldname;
				tbName.Focus();
				return;
			}
			if(oldname != name)
			{
				m_profile_fontformat[name] = m_profile_fontformat[oldname];
				m_profile_fontformat.Remove(oldname);
				lbList.Items.Remove(oldname);
				lbList.Items.Add(name);
				lbList.SelectedItem = name;
			}
		}

		private void cbbFont_Leave(object sender, EventArgs e)
		{
			if(m_nowitem == null)
				return;

			m_nowitem.fontid = 0;
			Int32.TryParse(cbbFont.Text.Split(':')[0].Trim(), out m_nowitem.fontid);

			m_panelcontrol.Invalidate();
			Program.GetMainForm().Panel.Invalidate();
		}

		private void lbList_SelectedIndexChanged(object sender, EventArgs e)
		{
			string name = lbList.SelectedItem as string;
			if(name == null)
			{
				m_nowitem = null;
				return;
			}
			m_nowitem = m_profile_fontformat[name];

			tbName.Text = name;

			if(m_profile_font.ContainsKey(m_nowitem.fontid))
				cbbFont.Text = m_profile_font[m_nowitem.fontid].ToString();
			else
				cbbFont.Text = m_nowitem.fontid.ToString();

			for(int i = 0; i < 5; ++i)
			{
				ListViewItem lvi = lvState.Items[i];
				lvi.SubItems[1].BackColor = m_nowitem.state[i].color;
				lvi.SubItems[2].BackColor = m_nowitem.state[i].color2;
				lvi.SubItems[3].BackColor = m_nowitem.state[i].effcolor;
				lvi.SubItems[4].Text = (m_nowitem.state[i].underline ? "*" : "");
				lvi.SubItems[5].Text = (m_nowitem.state[i].shadow ? "*" : "");
				lvi.SubItems[6].Text = (m_nowitem.state[i].outline ? "*" : "");
			}

			m_panelcontrol.Invalidate();
			Program.GetMainForm().Panel.Invalidate();
		}

		private void bnNew_Click(object sender, EventArgs e)
		{
			string name = "新建样式";
			for(int i = 2; m_profile_fontformat.ContainsKey(name); ++i)
				name = "新建样式(" + i.ToString() + ')';

			CFontProfile.CFontFormatParam fp = new CFontProfile.CFontFormatParam();
			m_profile_fontformat.Add(name, fp);
			lbList.SelectedIndex = lbList.Items.Add(name);
			tbName.Focus();
			tbName.SelectAll();
		}

		private void bnDelete_Click(object sender, EventArgs e)
		{
			string name = lbList.SelectedItem as string;
			if(name == null)
			{
				MessageBox.Show("请先选择要删除的样式", "警告", MessageBoxButtons.OK, MessageBoxIcon.Warning);
				return;
			}
			lbList.Items.Remove(name);
			m_profile_fontformat.Remove(name);
		}

		private void tbPreviewText_TextChanged(object sender, EventArgs e)
		{
			if(m_panelcontrol != null)
			{
				m_panelcontrol.Invalidate();
				Program.GetMainForm().Panel.Invalidate();
			}
		}

		internal void lvState_MouseClick(object sender, MouseEventArgs e)
		{
			if(m_nowitem == null)
				return;
			Point pos = e.Location;
			ListViewItem lvi = lvState.GetItemAt(5, pos.Y);
			if(lvi == null)
				return;
			int i = lvi.Index;
			if(i < 0 || i >= 5)
				return;

			Rectangle rect = lvi.Bounds;
			int pos1 = rect.X + lvState.Columns[0].Width;
			int pos2 = pos1 + lvState.Columns[1].Width;
			int pos3 = pos2 + lvState.Columns[2].Width;
			int pos4 = pos3 + lvState.Columns[3].Width;
			int pos5 = pos4 + lvState.Columns[4].Width;
			int pos6 = pos5 + lvState.Columns[5].Width;
			int pos7 = pos6 + lvState.Columns[6].Width;
			if(pos.X < pos1 || pos.X >= pos7)
				return;

			if(pos.X < pos2)	// 文本色
			{
				CFormColor formcolor = new CFormColor();
				Point abspos = lvState.PointToScreen(lvi.Position);
				abspos.X += pos2 - formcolor.Width;
				abspos.Y += rect.Height;
				formcolor.Location = abspos;
				formcolor.Color = lvi.SubItems[1].BackColor;
				if(formcolor.ShowDialog(this) == DialogResult.OK)
				{
					lvi.SubItems[1].BackColor = formcolor.Color;
					m_nowitem.state[i].color = formcolor.Color;
					lvi.SubItems[2].BackColor = formcolor.Color;	// 过渡色也修改
					m_nowitem.state[i].color2 = formcolor.Color;	// 过渡色也修改
					m_panelcontrol.Invalidate();
					Program.GetMainForm().Panel.Invalidate();
				}
			}
			else if(pos.X < pos3)	// 过渡色
			{
				CFormColor formcolor = new CFormColor();
				Point abspos = lvState.PointToScreen(lvi.Position);
				abspos.X += pos3 - formcolor.Width;
				abspos.Y += rect.Height;
				formcolor.Location = abspos;
				formcolor.Color = lvi.SubItems[2].BackColor;
				if(formcolor.ShowDialog(this) == DialogResult.OK)
				{
					lvi.SubItems[2].BackColor = formcolor.Color;
					m_nowitem.state[i].color2 = formcolor.Color;
					m_panelcontrol.Invalidate();
					Program.GetMainForm().Panel.Invalidate();
				}
			}
			else if(pos.X < pos4)	// 效果色
			{
				CFormColor formcolor = new CFormColor();
				Point abspos = lvState.PointToScreen(lvi.Position);
				abspos.X += pos4 - formcolor.Width;
				abspos.Y += rect.Height;
				formcolor.Location = abspos;
				formcolor.Color = lvi.SubItems[3].BackColor;
				if(formcolor.ShowDialog(this) == DialogResult.OK)
				{
					lvi.SubItems[3].BackColor = formcolor.Color;
					m_nowitem.state[i].effcolor = formcolor.Color;
					m_panelcontrol.Invalidate();
					Program.GetMainForm().Panel.Invalidate();
				}
			}
			else if(pos.X < pos5)	// 下划线
			{
				m_nowitem.state[i].underline = !m_nowitem.state[i].underline;
				lvi.SubItems[4].Text = (m_nowitem.state[i].underline ? "*" : "");
				m_panelcontrol.Invalidate();
				Program.GetMainForm().Panel.Invalidate();
			}
			else if(pos.X < pos6)	// 阴影
			{
				if((m_nowitem.state[i].shadow = !m_nowitem.state[i].shadow))
					m_nowitem.state[i].outline = false;
				lvi.SubItems[5].Text = (m_nowitem.state[i].shadow ? "*" : "");
				lvi.SubItems[6].Text = (m_nowitem.state[i].outline ? "*" : "");
				m_panelcontrol.Invalidate();
				Program.GetMainForm().Panel.Invalidate();
			}
			else	// 描边
			{
				if((m_nowitem.state[i].outline = !m_nowitem.state[i].outline))
					m_nowitem.state[i].shadow = false;
				lvi.SubItems[5].Text = (m_nowitem.state[i].shadow ? "*" : "");
				lvi.SubItems[6].Text = (m_nowitem.state[i].outline ? "*" : "");
				m_panelcontrol.Invalidate();
				Program.GetMainForm().Panel.Invalidate();
			}
		}

		private void CFormMainFont_FormClosing(object sender, FormClosingEventArgs e)
		{
			Hide();
			Program.GetMainForm().SaveProfile();
			e.Cancel = true;
		}

		private void bnClose_Click(object sender, EventArgs e)
		{
			Hide();
			Program.GetMainForm().SaveProfile();
		}

		private void bnPreviewBackColor_Click(object sender, EventArgs e)
		{
			CFormColor formcolor = new CFormColor();
			Point abspos = PointToScreen(bnPreviewBackColor.Location);
			abspos.X += bnPreviewBackColor.Size.Width - formcolor.Width;
			abspos.Y += bnPreviewBackColor.Size.Height;
			formcolor.Location = abspos;
			formcolor.Color = BkColor;
			if(formcolor.ShowDialog(this) == DialogResult.OK)
			{
				BkColor = formcolor.Color;
			}
		}
	}
}
