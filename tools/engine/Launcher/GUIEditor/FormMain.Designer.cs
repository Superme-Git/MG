namespace GUIEditor2
{
	partial class CFormMain
	{
		private System.ComponentModel.IContainer components = null;

		protected override void Dispose(bool disposing)
		{
			if(disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows 窗体设计器生成的代码

		private void InitializeComponent()
		{
            this.spMain = new System.Windows.Forms.SplitContainer();
            this.spSub = new System.Windows.Forms.SplitContainer();
            this.lvList = new System.Windows.Forms.ListView();
            this.colName = new System.Windows.Forms.ColumnHeader();
            this.colLock = new System.Windows.Forms.ColumnHeader();
            this.colComment = new System.Windows.Forms.ColumnHeader();
            this.mnMain = new System.Windows.Forms.MenuStrip();
            this.miFile = new System.Windows.Forms.ToolStripMenuItem();
            this.miFileNew = new System.Windows.Forms.ToolStripMenuItem();
            this.miFileOpen = new System.Windows.Forms.ToolStripMenuItem();
            this.miFileSave = new System.Windows.Forms.ToolStripMenuItem();
            this.miFileSaveAs = new System.Windows.Forms.ToolStripMenuItem();
            this.miFileSep0 = new System.Windows.Forms.ToolStripSeparator();
            this.miFileExit = new System.Windows.Forms.ToolStripMenuItem();
            this.miEdit = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditNew = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditSep10 = new System.Windows.Forms.ToolStripSeparator();
            this.miEditCopy = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditDelete = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditSep11 = new System.Windows.Forms.ToolStripSeparator();
            this.miEditSelectAll = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditSep12 = new System.Windows.Forms.ToolStripSeparator();
            this.miEditLock = new System.Windows.Forms.ToolStripMenuItem();
            this.miEditTop = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.miEdit800x600 = new System.Windows.Forms.ToolStripMenuItem();
            this.miEdit1024x768 = new System.Windows.Forms.ToolStripMenuItem();
            this.miView = new System.Windows.Forms.ToolStripMenuItem();
            this.miViewScale1X = new System.Windows.Forms.ToolStripMenuItem();
            this.miViewScale2X = new System.Windows.Forms.ToolStripMenuItem();
            this.miViewScale4X = new System.Windows.Forms.ToolStripMenuItem();
            this.miViewScale8X = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
            this.miViewForeImage = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlign = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignLeft = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignRight = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignSep0 = new System.Windows.Forms.ToolStripSeparator();
            this.miAlignUp = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignDown = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignSep1 = new System.Windows.Forms.ToolStripSeparator();
            this.miAlignMiddle = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignCenter = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignSep2 = new System.Windows.Forms.ToolStripSeparator();
            this.miAlignWidth = new System.Windows.Forms.ToolStripMenuItem();
            this.miAlignHeight = new System.Windows.Forms.ToolStripMenuItem();
            this.miOption = new System.Windows.Forms.ToolStripMenuItem();
            this.miOptionFontFormat = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.miOptionGC = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.miOptionSetting = new System.Windows.Forms.ToolStripMenuItem();
            this.miHelp = new System.Windows.Forms.ToolStripMenuItem();
            this.miHelpAbout = new System.Windows.Forms.ToolStripMenuItem();
            this.dlgOpenFile = new System.Windows.Forms.OpenFileDialog();
            this.dlgSaveFile = new System.Windows.Forms.SaveFileDialog();
            this.spMain.Panel2.SuspendLayout();
            this.spMain.SuspendLayout();
            this.spSub.Panel2.SuspendLayout();
            this.spSub.SuspendLayout();
            this.mnMain.SuspendLayout();
            this.SuspendLayout();
            // 
            // spMain
            // 
            this.spMain.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.spMain.Dock = System.Windows.Forms.DockStyle.Fill;
            this.spMain.FixedPanel = System.Windows.Forms.FixedPanel.Panel2;
            this.spMain.Location = new System.Drawing.Point(0, 24);
            this.spMain.Name = "spMain";
            // 
            // spMain.Panel1
            // 
            this.spMain.Panel1.AllowDrop = true;
            this.spMain.Panel1.AutoScroll = true;
            this.spMain.Panel1.BackColor = System.Drawing.Color.DimGray;
            this.spMain.Panel1.DragDrop += new System.Windows.Forms.DragEventHandler(this.spMain_Panel1_DragDrop);
            this.spMain.Panel1.DragEnter += new System.Windows.Forms.DragEventHandler(this.spMain_Panel1_DragEnter);
            // 
            // spMain.Panel2
            // 
            this.spMain.Panel2.Controls.Add(this.spSub);
            this.spMain.Size = new System.Drawing.Size(1075, 648);
            this.spMain.SplitterDistance = 838;
            this.spMain.SplitterWidth = 2;
            this.spMain.TabIndex = 1;
            // 
            // spSub
            // 
            this.spSub.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.spSub.Dock = System.Windows.Forms.DockStyle.Fill;
            this.spSub.FixedPanel = System.Windows.Forms.FixedPanel.Panel2;
            this.spSub.Location = new System.Drawing.Point(0, 0);
            this.spSub.Name = "spSub";
            this.spSub.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // spSub.Panel1
            // 
            this.spSub.Panel1.AutoScroll = true;
            this.spSub.Panel1MinSize = 100;
            // 
            // spSub.Panel2
            // 
            this.spSub.Panel2.Controls.Add(this.lvList);
            this.spSub.Size = new System.Drawing.Size(235, 648);
            this.spSub.SplitterDistance = 495;
            this.spSub.SplitterWidth = 3;
            this.spSub.TabIndex = 0;
            // 
            // lvList
            // 
            this.lvList.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.lvList.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.colName,
            this.colLock,
            this.colComment});
            this.lvList.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lvList.FullRowSelect = true;
            this.lvList.GridLines = true;
            this.lvList.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.lvList.HideSelection = false;
            this.lvList.Location = new System.Drawing.Point(0, 0);
            this.lvList.Name = "lvList";
            this.lvList.ShowGroups = false;
            this.lvList.Size = new System.Drawing.Size(231, 146);
            this.lvList.TabIndex = 0;
            this.lvList.UseCompatibleStateImageBehavior = false;
            this.lvList.View = System.Windows.Forms.View.Details;
            this.lvList.MouseClick += new System.Windows.Forms.MouseEventHandler(this.lvList_MouseClick);
            this.lvList.KeyDown += new System.Windows.Forms.KeyEventHandler(this.lvList_KeyDown);
            this.lvList.ItemSelectionChanged += new System.Windows.Forms.ListViewItemSelectionChangedEventHandler(this.lvList_ItemSelectionChanged);
            this.lvList.ColumnWidthChanging += new System.Windows.Forms.ColumnWidthChangingEventHandler(this.lvList_ColumnWidthChanging);
            // 
            // colName
            // 
            this.colName.Text = "控件名称";
            this.colName.Width = 90;
            // 
            // colLock
            // 
            this.colLock.Text = "锁";
            this.colLock.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.colLock.Width = 29;
            // 
            // colComment
            // 
            this.colComment.Text = "注释";
            this.colComment.Width = 88;
            // 
            // mnMain
            // 
            this.mnMain.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miFile,
            this.miEdit,
            this.miView,
            this.miAlign,
            this.miOption,
            this.miHelp});
            this.mnMain.Location = new System.Drawing.Point(0, 0);
            this.mnMain.Name = "mnMain";
            this.mnMain.Padding = new System.Windows.Forms.Padding(5, 2, 0, 2);
            this.mnMain.Size = new System.Drawing.Size(1075, 24);
            this.mnMain.TabIndex = 0;
            this.mnMain.Text = "主菜单";
            this.mnMain.MenuActivate += new System.EventHandler(this.mnMain_MenuActivate);
            // 
            // miFile
            // 
            this.miFile.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miFileNew,
            this.miFileOpen,
            this.miFileSave,
            this.miFileSaveAs,
            this.miFileSep0,
            this.miFileExit});
            this.miFile.Name = "miFile";
            this.miFile.Size = new System.Drawing.Size(59, 20);
            this.miFile.Text = "文件(&F)";
            // 
            // miFileNew
            // 
            this.miFileNew.Name = "miFileNew";
            this.miFileNew.ShortcutKeyDisplayString = "F4/Ctrl+N";
            this.miFileNew.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.N)));
            this.miFileNew.Size = new System.Drawing.Size(189, 22);
            this.miFileNew.Text = "新建(&N)";
            this.miFileNew.Click += new System.EventHandler(this.miFileNew_Click);
            // 
            // miFileOpen
            // 
            this.miFileOpen.Name = "miFileOpen";
            this.miFileOpen.ShortcutKeyDisplayString = "F3/Ctrl+O";
            this.miFileOpen.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.O)));
            this.miFileOpen.Size = new System.Drawing.Size(189, 22);
            this.miFileOpen.Text = "打开(&O)...";
            this.miFileOpen.Click += new System.EventHandler(this.miFileOpen_Click);
            // 
            // miFileSave
            // 
            this.miFileSave.Name = "miFileSave";
            this.miFileSave.ShortcutKeyDisplayString = "F2/Ctrl+S";
            this.miFileSave.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
            this.miFileSave.Size = new System.Drawing.Size(189, 22);
            this.miFileSave.Text = "保存(&S)";
            this.miFileSave.Click += new System.EventHandler(this.miFileSave_Click);
            // 
            // miFileSaveAs
            // 
            this.miFileSaveAs.Name = "miFileSaveAs";
            this.miFileSaveAs.Size = new System.Drawing.Size(189, 22);
            this.miFileSaveAs.Text = "另存为(&A)...";
            this.miFileSaveAs.Click += new System.EventHandler(this.miFileSaveAs_Click);
            // 
            // miFileSep0
            // 
            this.miFileSep0.Name = "miFileSep0";
            this.miFileSep0.Size = new System.Drawing.Size(186, 6);
            // 
            // miFileExit
            // 
            this.miFileExit.Name = "miFileExit";
            this.miFileExit.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Alt | System.Windows.Forms.Keys.F4)));
            this.miFileExit.Size = new System.Drawing.Size(189, 22);
            this.miFileExit.Text = "退出(&X)";
            this.miFileExit.Click += new System.EventHandler(this.miFileExit_Click);
            // 
            // miEdit
            // 
            this.miEdit.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miEditNew,
            this.miEditSep10,
            this.miEditCopy,
            this.miEditDelete,
            this.miEditSep11,
            this.miEditSelectAll,
            this.miEditSep12,
            this.miEditLock,
            this.miEditTop,
            this.toolStripSeparator1,
            this.miEdit800x600,
            this.miEdit1024x768});
            this.miEdit.Name = "miEdit";
            this.miEdit.Size = new System.Drawing.Size(59, 20);
            this.miEdit.Text = "编辑(&E)";
            // 
            // miEditNew
            // 
            this.miEditNew.Name = "miEditNew";
            this.miEditNew.ShortcutKeyDisplayString = "Q";
            this.miEditNew.Size = new System.Drawing.Size(201, 22);
            this.miEditNew.Text = "新建控件(&N)...";
            this.miEditNew.Click += new System.EventHandler(this.miEditNew_Click);
            // 
            // miEditSep10
            // 
            this.miEditSep10.Name = "miEditSep10";
            this.miEditSep10.Size = new System.Drawing.Size(198, 6);
            // 
            // miEditCopy
            // 
            this.miEditCopy.Name = "miEditCopy";
            this.miEditCopy.ShortcutKeyDisplayString = "Ctrl+C";
            this.miEditCopy.Size = new System.Drawing.Size(201, 22);
            this.miEditCopy.Text = "复制(&C)";
            this.miEditCopy.Click += new System.EventHandler(this.miEditCopy_Click);
            // 
            // miEditDelete
            // 
            this.miEditDelete.Name = "miEditDelete";
            this.miEditDelete.ShortcutKeyDisplayString = "Delete";
            this.miEditDelete.Size = new System.Drawing.Size(201, 22);
            this.miEditDelete.Text = "删除(&D)";
            this.miEditDelete.Click += new System.EventHandler(this.miEditDelete_Click);
            // 
            // miEditSep11
            // 
            this.miEditSep11.Name = "miEditSep11";
            this.miEditSep11.Size = new System.Drawing.Size(198, 6);
            // 
            // miEditSelectAll
            // 
            this.miEditSelectAll.Name = "miEditSelectAll";
            this.miEditSelectAll.ShortcutKeyDisplayString = "Ctrl+A";
            this.miEditSelectAll.Size = new System.Drawing.Size(201, 22);
            this.miEditSelectAll.Text = "全选(&A)";
            this.miEditSelectAll.Click += new System.EventHandler(this.miEditSelectAll_Click);
            // 
            // miEditSep12
            // 
            this.miEditSep12.Name = "miEditSep12";
            this.miEditSep12.Size = new System.Drawing.Size(198, 6);
            // 
            // miEditLock
            // 
            this.miEditLock.Name = "miEditLock";
            this.miEditLock.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.L)));
            this.miEditLock.Size = new System.Drawing.Size(201, 22);
            this.miEditLock.Text = "锁定/解锁(&L)";
            this.miEditLock.Click += new System.EventHandler(this.miEditLock_Click);
            // 
            // miEditTop
            // 
            this.miEditTop.Name = "miEditTop";
            this.miEditTop.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.T)));
            this.miEditTop.Size = new System.Drawing.Size(201, 22);
            this.miEditTop.Text = "置于最上层(&T)";
            this.miEditTop.Click += new System.EventHandler(this.miEditTop_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(198, 6);
            // 
            // miEdit800x600
            // 
            this.miEdit800x600.Checked = true;
            this.miEdit800x600.CheckState = System.Windows.Forms.CheckState.Checked;
            this.miEdit800x600.Name = "miEdit800x600";
            this.miEdit800x600.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D1)));
            this.miEdit800x600.Size = new System.Drawing.Size(201, 22);
            this.miEdit800x600.Text = " 800 x 600 模式";
            this.miEdit800x600.Click += new System.EventHandler(this.miEdit800x600_Click);
            // 
            // miEdit1024x768
            // 
            this.miEdit1024x768.Name = "miEdit1024x768";
            this.miEdit1024x768.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D2)));
            this.miEdit1024x768.Size = new System.Drawing.Size(201, 22);
            this.miEdit1024x768.Text = "1024 x 768 模式";
            this.miEdit1024x768.Click += new System.EventHandler(this.miEdit1024x768_Click);
            // 
            // miView
            // 
            this.miView.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miViewScale1X,
            this.miViewScale2X,
            this.miViewScale4X,
            this.miViewScale8X,
            this.toolStripSeparator4,
            this.miViewForeImage});
            this.miView.Name = "miView";
            this.miView.Size = new System.Drawing.Size(59, 20);
            this.miView.Text = "查看(&V)";
            // 
            // miViewScale1X
            // 
            this.miViewScale1X.Name = "miViewScale1X";
            this.miViewScale1X.ShortcutKeyDisplayString = "";
            this.miViewScale1X.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D1)));
            this.miViewScale1X.Size = new System.Drawing.Size(159, 22);
            this.miViewScale1X.Text = "缩放&1X";
            this.miViewScale1X.Click += new System.EventHandler(this.miViewScale1X_Click);
            // 
            // miViewScale2X
            // 
            this.miViewScale2X.Name = "miViewScale2X";
            this.miViewScale2X.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D2)));
            this.miViewScale2X.Size = new System.Drawing.Size(159, 22);
            this.miViewScale2X.Text = "缩放&2X";
            this.miViewScale2X.Click += new System.EventHandler(this.miViewScale2X_Click);
            // 
            // miViewScale4X
            // 
            this.miViewScale4X.Name = "miViewScale4X";
            this.miViewScale4X.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D3)));
            this.miViewScale4X.Size = new System.Drawing.Size(159, 22);
            this.miViewScale4X.Text = "缩放4X";
            this.miViewScale4X.Click += new System.EventHandler(this.miViewScale4X_Click);
            // 
            // miViewScale8X
            // 
            this.miViewScale8X.Name = "miViewScale8X";
            this.miViewScale8X.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D4)));
            this.miViewScale8X.Size = new System.Drawing.Size(159, 22);
            this.miViewScale8X.Text = "缩放&8X";
            this.miViewScale8X.Click += new System.EventHandler(this.miViewScale8X_Click);
            // 
            // toolStripSeparator4
            // 
            this.toolStripSeparator4.Name = "toolStripSeparator4";
            this.toolStripSeparator4.Size = new System.Drawing.Size(156, 6);
            // 
            // miViewForeImage
            // 
            this.miViewForeImage.Name = "miViewForeImage";
            this.miViewForeImage.ShortcutKeyDisplayString = "V";
            this.miViewForeImage.Size = new System.Drawing.Size(159, 22);
            this.miViewForeImage.Text = "显示前景图(&V)";
            this.miViewForeImage.Click += new System.EventHandler(this.miViewForeImage_Click);
            // 
            // miAlign
            // 
            this.miAlign.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miAlignLeft,
            this.miAlignRight,
            this.miAlignSep0,
            this.miAlignUp,
            this.miAlignDown,
            this.miAlignSep1,
            this.miAlignMiddle,
            this.miAlignCenter,
            this.miAlignSep2,
            this.miAlignWidth,
            this.miAlignHeight});
            this.miAlign.Name = "miAlign";
            this.miAlign.Size = new System.Drawing.Size(59, 20);
            this.miAlign.Text = "对齐(&A)";
            // 
            // miAlignLeft
            // 
            this.miAlignLeft.Name = "miAlignLeft";
            this.miAlignLeft.ShortcutKeys = System.Windows.Forms.Keys.F5;
            this.miAlignLeft.Size = new System.Drawing.Size(195, 22);
            this.miAlignLeft.Text = "左对齐(&L)";
            this.miAlignLeft.Click += new System.EventHandler(this.miAlignLeft_Click);
            // 
            // miAlignRight
            // 
            this.miAlignRight.Name = "miAlignRight";
            this.miAlignRight.ShortcutKeys = System.Windows.Forms.Keys.F6;
            this.miAlignRight.Size = new System.Drawing.Size(195, 22);
            this.miAlignRight.Text = "右对齐(&R)";
            this.miAlignRight.Click += new System.EventHandler(this.miAlignRight_Click);
            // 
            // miAlignSep0
            // 
            this.miAlignSep0.Name = "miAlignSep0";
            this.miAlignSep0.Size = new System.Drawing.Size(192, 6);
            // 
            // miAlignUp
            // 
            this.miAlignUp.Name = "miAlignUp";
            this.miAlignUp.ShortcutKeys = System.Windows.Forms.Keys.F7;
            this.miAlignUp.Size = new System.Drawing.Size(195, 22);
            this.miAlignUp.Text = "上对齐(&U)";
            this.miAlignUp.Click += new System.EventHandler(this.miAlignUp_Click);
            // 
            // miAlignDown
            // 
            this.miAlignDown.Name = "miAlignDown";
            this.miAlignDown.ShortcutKeys = System.Windows.Forms.Keys.F8;
            this.miAlignDown.Size = new System.Drawing.Size(195, 22);
            this.miAlignDown.Text = "下对齐(&D)";
            this.miAlignDown.Click += new System.EventHandler(this.miAlignDown_Click);
            // 
            // miAlignSep1
            // 
            this.miAlignSep1.Name = "miAlignSep1";
            this.miAlignSep1.Size = new System.Drawing.Size(192, 6);
            // 
            // miAlignMiddle
            // 
            this.miAlignMiddle.Name = "miAlignMiddle";
            this.miAlignMiddle.ShortcutKeys = System.Windows.Forms.Keys.F9;
            this.miAlignMiddle.Size = new System.Drawing.Size(195, 22);
            this.miAlignMiddle.Text = "水平居中(&M)";
            this.miAlignMiddle.Click += new System.EventHandler(this.miAlignMiddle_Click);
            // 
            // miAlignCenter
            // 
            this.miAlignCenter.Name = "miAlignCenter";
            this.miAlignCenter.ShortcutKeys = System.Windows.Forms.Keys.F10;
            this.miAlignCenter.Size = new System.Drawing.Size(195, 22);
            this.miAlignCenter.Text = "垂直居中(&C)";
            this.miAlignCenter.Click += new System.EventHandler(this.miAlignCenter_Click);
            // 
            // miAlignSep2
            // 
            this.miAlignSep2.Name = "miAlignSep2";
            this.miAlignSep2.Size = new System.Drawing.Size(192, 6);
            // 
            // miAlignWidth
            // 
            this.miAlignWidth.Name = "miAlignWidth";
            this.miAlignWidth.ShortcutKeys = System.Windows.Forms.Keys.F11;
            this.miAlignWidth.Size = new System.Drawing.Size(195, 22);
            this.miAlignWidth.Text = "与底层控件等宽(&W)";
            this.miAlignWidth.Click += new System.EventHandler(this.miAlignWidth_Click);
            // 
            // miAlignHeight
            // 
            this.miAlignHeight.Name = "miAlignHeight";
            this.miAlignHeight.ShortcutKeys = System.Windows.Forms.Keys.F12;
            this.miAlignHeight.Size = new System.Drawing.Size(195, 22);
            this.miAlignHeight.Text = "与底层控件等高(&H)";
            this.miAlignHeight.Click += new System.EventHandler(this.miAlignHeight_Click);
            // 
            // miOption
            // 
            this.miOption.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miOptionFontFormat,
            this.toolStripSeparator2,
            this.miOptionGC,
            this.toolStripSeparator3,
            this.miOptionSetting});
            this.miOption.Name = "miOption";
            this.miOption.Size = new System.Drawing.Size(59, 20);
            this.miOption.Text = "选项(&O)";
            // 
            // miOptionFontFormat
            // 
            this.miOptionFontFormat.Name = "miOptionFontFormat";
            this.miOptionFontFormat.Size = new System.Drawing.Size(196, 22);
            this.miOptionFontFormat.Text = "字体样式编辑(&F)...";
            this.miOptionFontFormat.Click += new System.EventHandler(this.miOptionFontFormat_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(193, 6);
            // 
            // miOptionGC
            // 
            this.miOptionGC.Name = "miOptionGC";
            this.miOptionGC.Size = new System.Drawing.Size(196, 22);
            this.miOptionGC.Text = "清理托管内存及资源(&M)";
            this.miOptionGC.Click += new System.EventHandler(this.miOptionGC_Click);
            // 
            // toolStripSeparator3
            // 
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            this.toolStripSeparator3.Size = new System.Drawing.Size(193, 6);
            // 
            // miOptionSetting
            // 
            this.miOptionSetting.Name = "miOptionSetting";
            this.miOptionSetting.Size = new System.Drawing.Size(196, 22);
            this.miOptionSetting.Text = "设置(&S)...";
            this.miOptionSetting.Click += new System.EventHandler(this.miOptionSetting_Click);
            // 
            // miHelp
            // 
            this.miHelp.Alignment = System.Windows.Forms.ToolStripItemAlignment.Right;
            this.miHelp.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miHelpAbout});
            this.miHelp.Name = "miHelp";
            this.miHelp.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.miHelp.Size = new System.Drawing.Size(59, 20);
            this.miHelp.Text = "帮助(&H)";
            // 
            // miHelpAbout
            // 
            this.miHelpAbout.Name = "miHelpAbout";
            this.miHelpAbout.ShortcutKeys = System.Windows.Forms.Keys.F1;
            this.miHelpAbout.Size = new System.Drawing.Size(129, 22);
            this.miHelpAbout.Text = "关于(&A)";
            this.miHelpAbout.Click += new System.EventHandler(this.miHelpAbout_Click);
            // 
            // dlgOpenFile
            // 
            this.dlgOpenFile.Filter = "GUI描述文件(*.xml)|*.xml|所有文件(*.*)|*.*";
            this.dlgOpenFile.RestoreDirectory = true;
            // 
            // dlgSaveFile
            // 
            this.dlgSaveFile.Filter = "GUI描述文件(*.xml)|*.xml|所有文件(*.*)|*.*";
            this.dlgSaveFile.RestoreDirectory = true;
            // 
            // CFormMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(1075, 672);
            this.Controls.Add(this.spMain);
            this.Controls.Add(this.mnMain);
            this.KeyPreview = true;
            this.MainMenuStrip = this.mnMain;
            this.Name = "CFormMain";
            this.Text = "界面编辑器";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.CFormMain_FormClosing);
            this.Load += new System.EventHandler(this.formMain_Load);
            this.spMain.Panel2.ResumeLayout(false);
            this.spMain.ResumeLayout(false);
            this.spSub.Panel2.ResumeLayout(false);
            this.spSub.ResumeLayout(false);
            this.mnMain.ResumeLayout(false);
            this.mnMain.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.SplitContainer spMain;
		private System.Windows.Forms.SplitContainer spSub;
		private System.Windows.Forms.ListView lvList;
		private System.Windows.Forms.MenuStrip mnMain;
		private System.Windows.Forms.ToolStripMenuItem miFile;
		private System.Windows.Forms.ToolStripMenuItem miEdit;
		private System.Windows.Forms.ToolStripMenuItem miView;
		private System.Windows.Forms.ToolStripMenuItem miOption;
		private System.Windows.Forms.ToolStripMenuItem miHelp;
		private System.Windows.Forms.ToolStripMenuItem miFileNew;
		private System.Windows.Forms.ToolStripMenuItem miFileOpen;
		private System.Windows.Forms.ToolStripMenuItem miFileSave;
		private System.Windows.Forms.ToolStripMenuItem miFileSaveAs;
		private System.Windows.Forms.ColumnHeader colName;
		private System.Windows.Forms.ColumnHeader colLock;
		private System.Windows.Forms.ToolStripSeparator miFileSep0;
		private System.Windows.Forms.ToolStripMenuItem miFileExit;
		private System.Windows.Forms.ToolStripMenuItem miEditCopy;
		private System.Windows.Forms.ToolStripMenuItem miEditDelete;
		private System.Windows.Forms.ToolStripSeparator miEditSep11;
		private System.Windows.Forms.ToolStripMenuItem miEditSelectAll;
		private System.Windows.Forms.ToolStripMenuItem miEditLock;
		private System.Windows.Forms.ToolStripMenuItem miViewScale1X;
		private System.Windows.Forms.ToolStripMenuItem miViewScale2X;
		private System.Windows.Forms.ToolStripMenuItem miViewScale4X;
		private System.Windows.Forms.ToolStripMenuItem miViewScale8X;
		private System.Windows.Forms.ToolStripMenuItem miHelpAbout;
		private System.Windows.Forms.OpenFileDialog dlgOpenFile;
		private System.Windows.Forms.SaveFileDialog dlgSaveFile;
		private System.Windows.Forms.ToolStripMenuItem miEditNew;
		private System.Windows.Forms.ToolStripSeparator miEditSep10;
		private System.Windows.Forms.ColumnHeader colComment;
		private System.Windows.Forms.ToolStripMenuItem miAlign;
		private System.Windows.Forms.ToolStripMenuItem miAlignLeft;
		private System.Windows.Forms.ToolStripMenuItem miAlignRight;
		private System.Windows.Forms.ToolStripMenuItem miAlignMiddle;
		private System.Windows.Forms.ToolStripSeparator miAlignSep0;
		private System.Windows.Forms.ToolStripMenuItem miAlignUp;
		private System.Windows.Forms.ToolStripMenuItem miAlignDown;
		private System.Windows.Forms.ToolStripMenuItem miAlignCenter;
		private System.Windows.Forms.ToolStripSeparator miAlignSep1;
		private System.Windows.Forms.ToolStripMenuItem miAlignWidth;
		private System.Windows.Forms.ToolStripMenuItem miAlignHeight;
		private System.Windows.Forms.ToolStripSeparator miAlignSep2;
		private System.Windows.Forms.ToolStripMenuItem miEditTop;
		private System.Windows.Forms.ToolStripSeparator miEditSep12;
		private System.Windows.Forms.ToolStripMenuItem miOptionFontFormat;
		private System.Windows.Forms.ToolStripMenuItem miOptionGC;
		private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
		private System.Windows.Forms.ToolStripMenuItem miEdit800x600;
		private System.Windows.Forms.ToolStripMenuItem miEdit1024x768;
		private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
		private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
		private System.Windows.Forms.ToolStripMenuItem miOptionSetting;
		private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
		private System.Windows.Forms.ToolStripMenuItem miViewForeImage;

	}
}
