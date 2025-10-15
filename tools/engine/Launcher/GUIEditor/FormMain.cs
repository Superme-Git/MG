using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;
using System.Text;
using System.IO;

namespace GUIEditor2
{
	public partial class CFormMain : Form
	{
		private CFormRender m_panelcontrol;
		private string m_filename;
		private Point m_scrpos;
		private TextBox m_editbox;
		private ListViewItem m_edititem;
		private CFormMainFont m_formfont;
		private FormSetting m_setting;
		private string m_path_xml_ori = ".";
		private string m_path_xml = ".\\";
		private string m_path_image_ori = ".";
		private string m_path_image = ".\\";
		private string m_path_fontdef_ori = "fontdef.xml";
		private string m_path_fontdef = "fontdef.xml";
		private Size m_initsize;
		private int m_imageorder = 0;
		private string m_savecoding = "utf-16";
		private Color m_imagebkcolor = Color.FromArgb(255, 0, 0, 0);
		private Color m_fontbkcolor = Color.FromArgb(255, 0, 0, 0);
		private bool m_imagenoalpha = false;

		public class CImageParam 
		{
			public int x, y, w, h;
			public int h0, h1, w0, w1;
			public bool block, stretchw, stretchh;
		}

		private Dictionary<string, CImageParam> m_profile_image = new Dictionary<string,CImageParam>();

		public SplitterPanel PanelContainer
		{
			get { return spMain.Panel1; }
		}

		public CFormRender Panel
		{
			get { return m_panelcontrol; }
		}

		public ListView List
		{
			get { return lvList; }
		}

		public Point ScrPos
		{
			get { return m_scrpos; }
			set { m_scrpos = value; }
		}

		public Dictionary<string, CImageParam> ProfileImage
		{
			get { return m_profile_image; }
		}

		public string XMLPath
		{
			get { return m_path_xml; }
		}

		public string ImagePath
		{
			get { return m_path_image; }
		}

		public string FontDefPath
		{
			get { return m_path_fontdef; }
		}

		public int ImageOrder
		{
			get { return m_imageorder; }
		}

		public string SaveCoding
		{
			get { return m_savecoding; }
		}

		public Color ImageBkColor
		{
			get { return m_imagebkcolor; }
			set { m_imagebkcolor = value; }
		}

		public Color FontBkColor
		{
			get { return m_fontbkcolor; }
			set { m_fontbkcolor = value; }
		}

		public bool ImageNoAlpha
		{
			get { return m_imagenoalpha; }
			set { m_imagenoalpha = value; }
		}

		public string Filename
		{
			get { return m_filename; }
		}

		public CFormMain(string filename)
		{
			InitializeComponent();

			LoadProfile();

			m_filename = filename;

//			spMain.SplitterDistance = 600;
//			ClientSize = new Size(1065, 652);

			m_panelcontrol = new CFormRender(this);
			spMain.Panel1.Controls.Add(m_panelcontrol);
			spMain.Panel1.MouseWheel += new MouseEventHandler(Panel1_MouseWheel);
			miViewScale1X.Checked = true;

			CFontProfile.LoadFile(FontDefPath);
			CScrollBarProfile.LoadFile(XMLPath + "ScrollBarProfile.xml");

			m_editbox = new TextBox();
			m_editbox.Parent = lvList;
			m_editbox.BorderStyle = BorderStyle.None;
			m_editbox.LostFocus += new EventHandler(m_editbox_LostFocus);
			m_editbox.KeyDown += new KeyEventHandler(m_editbox_KeyDown);
			m_editbox.Hide();

			if(m_initsize.Width == 1024 && m_initsize.Height == 768)
				miEdit1024x768_Click(null, null);
		}

		public void ShowForm(Form form)
		{
			if(!spSub.Panel1.Controls.Contains(form))
			{
				spSub.Panel1.Controls.Clear();
				if(form != null)
				{
					form.Left = 0;
					form.Top = 0;
					spSub.Panel1.Controls.Add(form);
					if(!form.Visible)
						form.Show();
				}
			}
		}

		private void formMain_Load(object sender, EventArgs e)
		{
			if(m_filename == null || m_filename.Length <= 0)
			{
				miFileNew_Click(sender, e);
			}
			else
			{
				Text = "GUIEditor2 - 界面编辑器";
				m_panelcontrol.Modified = false;

				if(m_panelcontrol.Desktop.LoadXML(m_filename))
				{
                    Text = "GUIEditor2 - 界面编辑器 - " + m_filename;
					m_panelcontrol.Modified = false;
					m_panelcontrol.Invalidate();
				}
				else
					m_filename = null;
			}
		}

		private void CFormMain_FormClosing(object sender, FormClosingEventArgs e)
		{
			if(m_panelcontrol.Desktop.List.Count > 0 && m_panelcontrol.Modified)
			{
				DialogResult dr = MessageBox.Show("想保存文件吗?", "GUIEditor2 - 退出", MessageBoxButtons.YesNoCancel, MessageBoxIcon.Question);
				if(dr == DialogResult.Cancel)
				{
					e.Cancel = true;
					return;
				}
				else if(dr == DialogResult.Yes)
					miFileSave_Click(sender, e);
			}

			SaveProfile();
			m_panelcontrol.Render.Dispose();
		}

		protected override void OnSizeChanged(EventArgs e)
		{
			m_scrpos = spMain.Panel1.AutoScrollPosition;
			m_scrpos.X = -m_scrpos.X;
			m_scrpos.Y = -m_scrpos.Y;
			base.OnSizeChanged(e);
			spMain.Panel1.AutoScrollPosition = m_scrpos;
		}

		protected override void OnKeyDown(KeyEventArgs e)
		{
			if(e.KeyCode == Keys.F2)
				miFileSave_Click(null, null);
			else if(e.KeyCode == Keys.F3)
				miFileOpen_Click(null, null);
			else if(e.KeyCode == Keys.F4)
				miFileNew_Click(null, null);
			else
				base.OnKeyDown(e);
		}

		private bool CheckItemName()
		{
			Dictionary<string, object> dic = new Dictionary<string, object>();
			foreach(ListViewItem item in lvList.Items)
			{
				if(dic.ContainsKey(item.Text))
				{
					DialogResult dr = MessageBox.Show("有重复的控件名称(" + item.Text + "),是否仍要保存?",
						"GUIEditor2 - 警告", MessageBoxButtons.YesNo, MessageBoxIcon.Warning, MessageBoxDefaultButton.Button2);
					if(dr != DialogResult.Yes)
						return false;
					else
						break;
				}
				dic.Add(item.Text, null);
			}
			return true;
		}

		private void Panel1_MouseWheel(object sender, MouseEventArgs e)
		{
			spMain.Panel1.AutoScrollPosition = m_panelcontrol.OldScrPos;	// 不让滚轮使滚动条作用生效,恢复原位置
		}

		private void lvList_ItemSelectionChanged(object sender, ListViewItemSelectionChangedEventArgs e)
		{
			if(lvList.Focused)
			{
				m_panelcontrol.Modified = true;

				CComponent one = e.Item.Tag as CComponent;
				if(e.IsSelected && !one.Select)
					m_panelcontrol.Desktop.SelListAdd(one, true);
				else if(!e.IsSelected && one.Select)
					m_panelcontrol.Desktop.SelListDel(one);

				m_panelcontrol.Invalidate();
			}
		}

		private void lvList_MouseClick(object sender, MouseEventArgs e)
		{
			Point pos = e.Location;
			ListViewItem lvi = lvList.FocusedItem;
			Rectangle rect = lvi.Bounds;
			int pos1 = rect.X + lvList.Columns[0].Width;
			int pos2 = pos1 + lvList.Columns[1].Width;
			int pos3 = pos2 + lvList.Columns[2].Width;
			if(lvi != null && pos.X >= pos1)
			{
				if(pos.X < pos2)
					(lvi.Tag as CComponent).Lock = (lvi.SubItems[1].Text.Length <= 0);
				else
				{
					m_edititem = lvi;
					m_editbox.Location = new Point(pos2 + 6, rect.Y);
					m_editbox.Size = new Size(pos3 - pos2, rect.Height);
					m_editbox.Text = (m_edititem.Tag as CComponent).Comment;
					m_editbox.SelectAll();
					m_editbox.Show();
					m_editbox.Focus();
				}
			}
		}

		private void m_editbox_LostFocus(object sender, EventArgs e)
		{
			if(m_edititem != null)
			{
				(m_edititem.Tag as CComponent).Comment = m_editbox.Text;
				m_edititem = null;
			}
			m_editbox.Hide();
		}

		private void lvList_ColumnWidthChanging(object sender, ColumnWidthChangingEventArgs e)
		{
			if(m_edititem != null)
				m_editbox_LostFocus(null, null);
		}

		private void m_editbox_KeyDown(object sender, KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Enter)
			{
				m_editbox_LostFocus(null, null);
				lvList.Focus();
			}
		}

		private void lvList_KeyDown(object sender, KeyEventArgs e)
		{
			if(e.KeyCode == Keys.Delete)
				miEditDelete_Click(null, null);
			else if(e.KeyCode == Keys.C && e.Control)
				miEditCopy_Click(null, null);
			else if(e.KeyCode == Keys.A && e.Control)
				miEditSelectAll_Click(null, null);
		}

		private void spMain_Panel1_DragEnter(object sender, DragEventArgs e)
		{
			if(e.Data.GetDataPresent(DataFormats.FileDrop))
				e.Effect = DragDropEffects.Link;
			else
				e.Effect = DragDropEffects.None;
		}

		private void spMain_Panel1_DragDrop(object sender, DragEventArgs e)
		{
			string filename = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
			if(filename == null || filename.Length <= 0) return;

			if(m_panelcontrol.Desktop.List.Count > 0 && m_panelcontrol.Modified)
			{
				DialogResult dr = MessageBox.Show("想保存文件吗?", "GUIEditor2 - 打开", MessageBoxButtons.YesNoCancel, MessageBoxIcon.Question);
				if(dr == DialogResult.Cancel)
					return;
				else if(dr == DialogResult.Yes)
					miFileSave_Click(sender, e);
			}

			m_filename = filename;
			m_panelcontrol.Desktop.ClearComponent();
			miViewScale1X_Click(null, null);
            Text = "GUIEditor2 - 界面编辑器";
			m_panelcontrol.Modified = false;
			m_panelcontrol.Invalidate();

			CScrollBarProfile.Clear();
			CScrollBarProfile.LoadFile(XMLPath + "ScrollBarProfile.xml");

			if(m_panelcontrol.Desktop.LoadXML(m_filename))
			{
				m_filename = filename;
                Text = "GUIEditor2 - 界面编辑器 - " + m_filename;
				m_panelcontrol.Invalidate();
			}
			else
				m_filename = null;
		}

		private void mnMain_MenuActivate(object sender, EventArgs e)
		{
			m_panelcontrol.Desktop.HasAlt = false;
			m_panelcontrol.Desktop.HasCtrl = false;
			m_panelcontrol.Desktop.HasShift = false;
		}

		private void miFileNew_Click(object sender, EventArgs e)
		{
			if(m_panelcontrol.Desktop.List.Count > 0 && m_panelcontrol.Modified)
			{
				DialogResult dr = MessageBox.Show("想保存文件吗?", "GUIEditor2 - 新建", MessageBoxButtons.YesNoCancel, MessageBoxIcon.Question);
				if(dr == DialogResult.Cancel)
					return;
				else if(dr == DialogResult.Yes)
					miFileSave_Click(sender, e);
			}

			m_filename = null;
			m_panelcontrol.Desktop.ClearComponent();
			miViewScale1X_Click(null, null);
            Text = "GUIEditor2 - 界面编辑器";
			m_panelcontrol.Modified = false;
			m_panelcontrol.Invalidate();

			CScrollBarProfile.Clear();
			CScrollBarProfile.LoadFile(XMLPath + "ScrollBarProfile.xml");

			CComponentFrame one = new CComponentFrame(m_panelcontrol.Desktop, this, "Frame");
			one.Rect = new Rectangle(250, 100, 300, 400);
			m_panelcontrol.Invalidate();
		}

		private void miFileOpen_Click(object sender, EventArgs e)
		{
			if(m_panelcontrol.Desktop.List.Count > 0 && m_panelcontrol.Modified)
			{
				DialogResult dr = MessageBox.Show("想保存文件吗?", "GUIEditor2 - 打开", MessageBoxButtons.YesNoCancel, MessageBoxIcon.Question);
				if(dr == DialogResult.Cancel)
					return;
				else if(dr == DialogResult.Yes)
					miFileSave_Click(sender, e);
			}

			m_filename = null;
			m_panelcontrol.Desktop.ClearComponent();
			miViewScale1X_Click(null, null);
            Text = "GUIEditor2 - 界面编辑器";
			m_panelcontrol.Modified = false;
			m_panelcontrol.Invalidate();

			CScrollBarProfile.Clear();
			CScrollBarProfile.LoadFile(XMLPath + "ScrollBarProfile.xml");

            dlgOpenFile.InitialDirectory = Environment.CurrentDirectory;

			if(dlgOpenFile.ShowDialog() == DialogResult.OK)
			{
				Cursor.Current = Cursors.WaitCursor;
				m_filename = dlgOpenFile.FileName;
				if(m_panelcontrol.Desktop.LoadXML(m_filename))
				{
					Text = "GUIEditor2 - 界面编辑器 - " + m_filename;
					m_panelcontrol.Invalidate();
				}
				else
					m_filename = null;
				Cursor.Current = Cursors.Default;
			}
		}

		public void miFileSave_Click(object sender, EventArgs e)
		{
			if(!CheckItemName())
				return;

			if(m_filename == null)
			{
				if(dlgSaveFile.ShowDialog() == DialogResult.OK)
					m_filename = dlgSaveFile.FileName;
				else
					return;
			}
			if(m_panelcontrol.Desktop.SaveXML(m_filename))
			{
				Text = "GUIEditor2 - " + m_filename;
				m_panelcontrol.Modified = false;
			}
		}

		private void miFileSaveAs_Click(object sender, EventArgs e)
		{
			if(!CheckItemName())
				return;

			if(dlgSaveFile.ShowDialog() == DialogResult.OK)
			{
				m_filename = dlgSaveFile.FileName;
				if(m_panelcontrol.Desktop.SaveXML(m_filename))
				{
					Text = "GUIEditor2 - " + m_filename;
					m_panelcontrol.Modified = false;
				}
			}
		}

		private void miFileExit_Click(object sender, EventArgs e)
		{
			Close();
		}

		private void miEditNew_Click(object sender, EventArgs e)
		{
			m_panelcontrol.PopupNewMenu();
		}

		internal void miEditCopy_Click(object sender, EventArgs e)
		{
			for(LinkedListNode<CComponent> node = m_panelcontrol.Desktop.SelList.First; node != null; node = node.Next)
				node.Value.Clone();
			m_panelcontrol.Invalidate();
		}

		internal void miEditDelete_Click(object sender, EventArgs e)
		{
			LinkedList<CComponent> list = m_panelcontrol.Desktop.SelList;
			LinkedListNode<CComponent> next = null;
			for(LinkedListNode<CComponent> node = list.First; node != null; node = next)
			{
				next = node.Next;
				if(!node.Value.Lock && !(node.Value is CComponentTabContainer))	// 不能这样删除TAB中的页面
					m_panelcontrol.Desktop.DelComponent(node.Value);
			}
		}

		internal void miEditSelectAll_Click(object sender, EventArgs e)
		{
			m_panelcontrol.Desktop.SelListAddAll();
		}

		private void miEditLock_Click(object sender, EventArgs e)
		{
			LinkedList<CComponent> list = m_panelcontrol.Desktop.SelList;
			for(LinkedListNode<CComponent> node = list.First; node != null; node = node.Next)
			{
				CComponent one = node.Value;
				one.Lock = !one.Lock;
			}
		}

		private void miEditTop_Click(object sender, EventArgs e)
		{
			LinkedList<CComponent> list = m_panelcontrol.Desktop.SelList;
			for(LinkedListNode<CComponent> node = list.First; node != null; node = node.Next)
			{
				CComponent one = node.Value;
				if(one.Parent != null)
				{
					one.Parent = null;
					one.Parent = m_panelcontrol.Desktop.GetContainer(one);
				}
			}
			m_panelcontrol.Invalidate();
		}

		private void miEdit800x600_Click(object sender, EventArgs e)
		{
			if(miEdit800x600.Checked) return;
			miEdit800x600.Checked = true;
			miEdit1024x768.Checked = false;
			miViewScale1X_Click(null, null);
			m_panelcontrol.Desktop.ChangeLayout(new Size(800, 600));
			m_panelcontrol.Invalidate();
		}

		private void miEdit1024x768_Click(object sender, EventArgs e)
		{
			if(miEdit1024x768.Checked) return;
			miEdit1024x768.Checked = true;
			miEdit800x600.Checked = false;
			miViewScale1X_Click(null, null);
			m_panelcontrol.Desktop.ChangeLayout(new Size(1024, 768));
			m_panelcontrol.Invalidate();
		}

		internal void miViewScale1X_Click(object sender, EventArgs e)
		{
			miViewScale1X.Checked = true;
			miViewScale2X.Checked = false;
			miViewScale4X.Checked = false;
			miViewScale8X.Checked = false;
			m_panelcontrol.RenderScale = 1;
		}

		internal void miViewScale2X_Click(object sender, EventArgs e)
		{
			miViewScale1X.Checked = false;
			miViewScale2X.Checked = true;
			miViewScale4X.Checked = false;
			miViewScale8X.Checked = false;
			m_panelcontrol.RenderScale = 2;
		}

		internal void miViewScale4X_Click(object sender, EventArgs e)
		{
			miViewScale1X.Checked = false;
			miViewScale2X.Checked = false;
			miViewScale4X.Checked = true;
			miViewScale8X.Checked = false;
			m_panelcontrol.RenderScale = 4;
		}

		internal void miViewScale8X_Click(object sender, EventArgs e)
		{
			miViewScale1X.Checked = false;
			miViewScale2X.Checked = false;
			miViewScale4X.Checked = false;
			miViewScale8X.Checked = true;
			m_panelcontrol.RenderScale = 8;
		}

		internal void miViewForeImage_Click(object sender, EventArgs e)
		{
			if(!m_panelcontrol.Desktop.HasForeImage)
			{
				m_panelcontrol.Desktop.HasForeImage = true;
				miViewForeImage.Checked = true;
			}
			else
			{
				m_panelcontrol.Desktop.HasForeImage = false;
				miViewForeImage.Checked = false;
			}

			m_panelcontrol.Invalidate();
		}

		private void miAlignLeft_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
				one.Rect = new Rectangle(0, one.Rect.Y, one.Rect.Width, one.Rect.Height);
			m_panelcontrol.Invalidate();
		}

		private void miAlignRight_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int right = (parent != null ? parent.Rect.Width : one.Desktop.Rect.Width);
				one.Rect = new Rectangle(right - one.Rect.Width, one.Rect.Y, one.Rect.Width, one.Rect.Height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miAlignUp_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
				one.Rect = new Rectangle(one.Rect.X, 0, one.Rect.Width, one.Rect.Height);
			m_panelcontrol.Invalidate();
		}

		private void miAlignDown_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int bottom = (parent != null ? parent.Rect.Height : one.Desktop.Rect.Height);
				one.Rect = new Rectangle(one.Rect.X, bottom - one.Rect.Height, one.Rect.Width, one.Rect.Height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miAlignMiddle_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int right = (parent != null ? parent.Rect.Width : one.Desktop.Rect.Width);
				one.Rect = new Rectangle((right - one.Rect.Width) / 2, one.Rect.Y, one.Rect.Width, one.Rect.Height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miAlignCenter_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int bottom = (parent != null ? parent.Rect.Height : one.Desktop.Rect.Height);
				one.Rect = new Rectangle(one.Rect.X, (bottom - one.Rect.Height) / 2, one.Rect.Width, one.Rect.Height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miAlignWidth_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int width = (parent != null ? parent.Rect.Width : one.Desktop.Rect.Width);
				one.Rect = new Rectangle(0, one.Rect.Y, width, one.Rect.Height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miAlignHeight_Click(object sender, EventArgs e)
		{
			foreach(CComponent one in m_panelcontrol.Desktop.SelList)
			{
				CComponent parent = one.Parent;
				int height = (parent != null ? parent.Rect.Height : one.Desktop.Rect.Height);
				one.Rect = new Rectangle(one.Rect.X, 0, one.Rect.Width, height);
			}
			m_panelcontrol.Invalidate();
		}

		private void miOptionFontFormat_Click(object sender, EventArgs e)
		{
			if(m_formfont == null)
				m_formfont = new CFormMainFont();
			m_formfont.Show();
			m_formfont.BringToFront();
			m_formfont.Activate();
		}

		private void miOptionGC_Click(object sender, EventArgs e)
		{
			Cursor.Current = Cursors.WaitCursor;
			SRenderState state_old = m_panelcontrol.Render.GetState();
			GC.Collect();
			GC.WaitForPendingFinalizers();
			GC.Collect();
			GC.WaitForPendingFinalizers();
			SRenderState state_new = m_panelcontrol.Render.GetState();
			Cursor.Current = Cursors.Default;
			MessageBox.Show("已清理托管内存      : " + (state_old.UsedManagedMem - state_new.UsedManagedMem) / 1024 + " KB\n" +
							"已清理纹理内存      : " + (state_new.AvailableTextureMem - state_old.AvailableTextureMem) / 1024 / 1024 + " MB\n" +
							"已清理纹理数量      : " + (state_old.TextureObjectAliveNum - state_new.TextureObjectAliveNum) + '\n' +
							"已清理字体数量      : " + (state_old.FontObjectAliveNum - state_new.FontObjectAliveNum) + '\n' +
							"已清理DX COM对象数量: " + (state_old.DxComObjectNum - state_new.DxComObjectNum),
							"GUIEditor2 - 清理内存完成", MessageBoxButtons.OK, MessageBoxIcon.Information);
		}

		private void miOptionSetting_Click(object sender, EventArgs e)
		{
			if(m_setting == null)
			{
				m_setting = new FormSetting();

				int i = 0;
				foreach(Size size in m_panelcontrol.Desktop.AllSize)
				{
					if(size == m_initsize)
					{
						m_setting.InitSizeIndex = i;
						break;
					}
					++i;
				}

				m_setting.ImageOrder = m_imageorder;
				m_setting.SaveCoding = m_savecoding;
			}

			if(m_setting.ShowDialog() == DialogResult.OK)
			{
				m_initsize = m_panelcontrol.Desktop.AllSize[m_setting.InitSizeIndex];
				m_imageorder = m_setting.ImageOrder;
				m_savecoding = m_setting.SaveCoding;
			}
		}

		internal bool SaveProfile()
		{
#if !DEBUG
			try
			{
#endif
			XmlTextWriter xtw = new XmlTextWriter(Path.GetFullPath("GUIEditor2.xml"), Encoding.UTF8);
			xtw.Formatting = Formatting.Indented;
			xtw.WriteStartDocument(true);
				xtw.WriteStartElement("Config");
					xtw.WriteStartElement("InitSize");
						xtw.WriteAttributeString("w", m_initsize.Width.ToString());
						xtw.WriteAttributeString("h", m_initsize.Height.ToString());
					xtw.WriteEndElement();
					xtw.WriteStartElement("ImageOrder");
						xtw.WriteAttributeString("value", m_imageorder.ToString());
					xtw.WriteEndElement();
					xtw.WriteStartElement("SaveCoding");
						xtw.WriteAttributeString("value", m_savecoding.ToString());
					xtw.WriteEndElement();
					xtw.WriteStartElement("ImageBkColor");
						xtw.WriteAttributeString("value", CComponent.Color2String(m_imagebkcolor));
					xtw.WriteEndElement();
					xtw.WriteStartElement("FontBkColor");
						xtw.WriteAttributeString("value", CComponent.Color2String(m_fontbkcolor));
					xtw.WriteEndElement();
					xtw.WriteStartElement("ImageNoAlpha");
						xtw.WriteAttributeString("value", m_imagenoalpha ? "true" : "false");
					xtw.WriteEndElement();
					xtw.WriteStartElement("Path");
						xtw.WriteAttributeString("xml", m_path_xml_ori.ToString());
						xtw.WriteAttributeString("image", m_path_image_ori.ToString());
						xtw.WriteAttributeString("fontdef", m_path_fontdef_ori.ToString());
					xtw.WriteEndElement();
					xtw.WriteStartElement("ImageProfile");
						foreach(KeyValuePair<string, CImageParam> pair in m_profile_image)
						{
							xtw.WriteStartElement("Profile");
								xtw.WriteAttributeString("name", pair.Key.ToString());
								xtw.WriteAttributeString("x", pair.Value.x.ToString());
								xtw.WriteAttributeString("y", pair.Value.y.ToString());
								xtw.WriteAttributeString("w", pair.Value.w.ToString());
								xtw.WriteAttributeString("h", pair.Value.h.ToString());
								xtw.WriteAttributeString("w0", pair.Value.w0.ToString());
								xtw.WriteAttributeString("w1", pair.Value.w1.ToString());
								xtw.WriteAttributeString("h0", pair.Value.h0.ToString());
								xtw.WriteAttributeString("h1", pair.Value.h1.ToString());
								xtw.WriteAttributeString("block", pair.Value.block ? "true" : "false");
								xtw.WriteAttributeString("stretchw", pair.Value.stretchw ? "true" : "false");
								xtw.WriteAttributeString("stretchh", pair.Value.stretchh ? "true" : "false");
							xtw.WriteEndElement();
						}
					xtw.WriteEndElement();
					xtw.WriteStartElement("FontProfile");
						foreach(KeyValuePair<string, CFontProfile.CFontFormatParam> pair in CFontProfile.FontFormatProfile)
						{
							xtw.WriteStartElement("Profile");
								xtw.WriteAttributeString("name", pair.Key.ToString());
								xtw.WriteAttributeString("fontid", pair.Value.fontid.ToString());
								for(int i = 0; i < 5; ++i)
								{
									xtw.WriteStartElement("State");
										xtw.WriteAttributeString("id", i.ToString());
                                        xtw.WriteAttributeString("color", CComponent.Color2String(pair.Value.state[i].color));
                                        xtw.WriteAttributeString("color2", CComponent.Color2String(pair.Value.state[i].color2));
                                        xtw.WriteAttributeString("effcolor", CComponent.Color2String(pair.Value.state[i].effcolor));
										xtw.WriteAttributeString("underline", pair.Value.state[i].underline ? "true" : "false");
										xtw.WriteAttributeString("shadow", pair.Value.state[i].shadow ? "true" : "false");
										xtw.WriteAttributeString("outline", pair.Value.state[i].outline ? "true" : "false");
									xtw.WriteEndElement();
								}
							xtw.WriteEndElement();
						}
					xtw.WriteEndElement();
				xtw.WriteEndElement();
			xtw.WriteEndDocument();
			xtw.Flush();
			xtw.Close();
			return true;
#if !DEBUG
			}
			catch(Exception ex)
			{
				MessageBox.Show(ex.Message, "GUIEditor2 - 异常", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return false;
			}
#endif
		}

		private bool LoadProfile()
		{
			try
			{
				XmlDocument doc = new XmlDocument();
				doc.Load(Path.GetFullPath("GUIEditor2.xml"));
				XmlElement config = doc["Config"];
				if(config != null)
				{
					XmlElement initsize = config["InitSize"];
					if(initsize != null)
					{
						int w, h;
						Int32.TryParse(initsize.GetAttribute("w"), out w);
						Int32.TryParse(initsize.GetAttribute("h"), out h);
						if(w <= 0 || h <= 0)
						{
							m_initsize.Width = 800;
							m_initsize.Height = 600;
						}
						else
						{
							m_initsize.Width = w;
							m_initsize.Height = h;
						}
					}
					XmlElement imageorder = config["ImageOrder"];
					if(imageorder != null)
						Int32.TryParse(imageorder.GetAttribute("value"), out m_imageorder);
					XmlElement savecoding = config["SaveCoding"];
					if(savecoding != null)
						m_savecoding = savecoding.GetAttribute("value");
					XmlElement imagebkcolor = config["ImageBkColor"];
					if(imagebkcolor != null)
						m_imagebkcolor = CComponent.GetAttributeColor(imagebkcolor, "value");
					XmlElement fontbkcolor = config["FontBkColor"];
					if(fontbkcolor != null)
						m_fontbkcolor = CComponent.GetAttributeColor(fontbkcolor, "value");
					XmlElement imagenoalpha = config["ImageNoAlpha"];
					if(imagenoalpha != null)
						m_imagenoalpha = CComponent.GetAttributeBool(imagenoalpha, "value");
					XmlElement path = config["Path"];
					if(path != null)
					{
						m_path_xml = m_path_xml_ori = path.GetAttribute("xml");
						if(m_path_xml == null || m_path_xml.Length < 1) m_path_xml = m_path_xml_ori = ".";
						m_path_xml = System.IO.Path.GetFullPath(m_path_xml.Replace('/', '\\'));
						if(!m_path_xml.EndsWith("\\")) m_path_xml += "\\";

						m_path_image = m_path_image_ori = path.GetAttribute("image");
						if(m_path_image == null || m_path_image.Length < 1) m_path_image = m_path_image_ori = ".";
						m_path_image = System.IO.Path.GetFullPath(m_path_image.Replace('/', '\\'));
						if(!m_path_image.EndsWith("\\")) m_path_image += "\\";

						m_path_fontdef = m_path_fontdef_ori = path.GetAttribute("fontdef");
						if(m_path_fontdef == null || m_path_fontdef.Length < 1) m_path_fontdef = m_path_fontdef_ori = "fontdef.xml";
						m_path_fontdef = System.IO.Path.GetFullPath(m_path_fontdef.Replace('/', '\\'));

						dlgOpenFile.InitialDirectory = m_path_xml;
						dlgSaveFile.InitialDirectory = m_path_xml;
					}
					XmlElement image = config["ImageProfile"];
					if(image != null)
					{
						foreach(XmlNode node in image.ChildNodes)
						{
							if(node is XmlElement && node.Name == "Profile")
							{
								XmlElement profile = node as XmlElement;
								CImageParam ip = new CImageParam();
								string name = profile.GetAttribute("name");
								Int32.TryParse(profile.GetAttribute("x"), out ip.x);
								Int32.TryParse(profile.GetAttribute("y"), out ip.y);
								Int32.TryParse(profile.GetAttribute("w"), out ip.w);
								Int32.TryParse(profile.GetAttribute("h"), out ip.h);
								Int32.TryParse(profile.GetAttribute("w0"), out ip.w0);
								Int32.TryParse(profile.GetAttribute("w1"), out ip.w1);
								Int32.TryParse(profile.GetAttribute("h0"), out ip.h0);
								Int32.TryParse(profile.GetAttribute("h1"), out ip.h1);
								ip.block = (profile.GetAttribute("block") == "true");
								ip.stretchw = (profile.GetAttribute("stretchw") == "true");
								ip.stretchh = (profile.GetAttribute("stretchh") == "true");
								if(name != null && !m_profile_image.ContainsKey(name))
									m_profile_image.Add(name, ip);
							}
						}
					}
					XmlElement font = config["FontProfile"];
					if(font != null)
					{
						foreach(XmlNode node in font.ChildNodes)
						{
							if(node is XmlElement && node.Name == "Profile")
							{
								XmlElement profile = node as XmlElement;
								CFontProfile.CFontFormatParam fp = new CFontProfile.CFontFormatParam();
								string name = profile.GetAttribute("name");
								Int32.TryParse(profile.GetAttribute("fontid"), out fp.fontid);
								foreach(XmlNode node2 in profile.ChildNodes)
								{
									if(node2 is XmlElement && node2.Name == "State")
									{
										XmlElement state = node2 as XmlElement;
										int id;
										if(Int32.TryParse(state.GetAttribute("id"), out id) && id >= 0 && id < 5)
										{
                                            fp.state[id].color = CComponent.String2Color(state.GetAttribute("color"));
                                            fp.state[id].color2 = (state.HasAttribute("color2") ? CComponent.String2Color(state.GetAttribute("color2")) : fp.state[id].color);
                                            fp.state[id].effcolor = CComponent.String2Color(state.GetAttribute("effcolor"));
											fp.state[id].underline = (state.GetAttribute("underline") == "true");
											fp.state[id].shadow = (state.GetAttribute("shadow") == "true");
											fp.state[id].outline = (state.GetAttribute("outline") == "true");
										}
									}
								}
								if(name != null && !CFontProfile.FontFormatProfile.ContainsKey(name))
									CFontProfile.FontFormatProfile.Add(name, fp);
							}
						}
					}
				}
				return true;
			}
			catch
			{
				return false;
			}
		}

		private void miHelpAbout_Click(object sender, EventArgs e)
		{
			SRenderState state = m_panelcontrol.Render.GetState();
			MessageBox.Show("GUIEditor2 (" + Program.GetVersion() + ")\n\n" +
                            "Liu Dongxue @ LocoJoy 2.0.0.0\n" +
							"ZhangLu @ LocoJoy 1.3.6.5\n\n" +

							"[渲染引擎状态]\n" +
							"虚拟内存占用  : " + state.UsedVirtualMem / 1024 / 1024 + " MB\n" +
							"物理内存占用  : " + state.UsedPhysicalMem / 1024 / 1024 + " MB\n" +
							"托管内存占用  : " + state.UsedManagedMem / 1024 / 1024 + " MB\n" +
							"可用纹理内存  : " + state.AvailableTextureMem / 1024 / 1024 + " MB\n" +
							"纹理数量      : " + state.TextureObjectAliveNum + '/' + state.TextureObjectAllNum + '\n' +
							"字体数量      : " + state.FontObjectAliveNum + '/' + state.FontObjectAllNum + '\n' +
							"DX COM对象数量: " + state.DxComObjectNum,
							"GUIEditor2 - 关于",
							MessageBoxButtons.OK,
							MessageBoxIcon.Information);
		}
	}
}
