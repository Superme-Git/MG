namespace GUIEditor2
{
	partial class CFormMainFont
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
			System.Windows.Forms.ListViewItem listViewItem1 = new System.Windows.Forms.ListViewItem(new string[] {
            "无效",
            "",
            "",
            "",
            "",
            "",
            ""}, -1);
			System.Windows.Forms.ListViewItem listViewItem2 = new System.Windows.Forms.ListViewItem(new string[] {
            "正常",
            "",
            "",
            "",
            "",
            "",
            ""}, -1);
			System.Windows.Forms.ListViewItem listViewItem3 = new System.Windows.Forms.ListViewItem(new string[] {
            "焦点",
            "",
            "",
            "",
            "",
            "",
            ""}, -1);
			System.Windows.Forms.ListViewItem listViewItem4 = new System.Windows.Forms.ListViewItem(new string[] {
            "按下",
            "",
            "",
            "",
            "",
            "",
            ""}, -1);
			System.Windows.Forms.ListViewItem listViewItem5 = new System.Windows.Forms.ListViewItem(new string[] {
            "按焦",
            "",
            "",
            "",
            "",
            "",
            ""}, -1);
			this.lbList = new System.Windows.Forms.ListBox();
			this.lbName = new System.Windows.Forms.Label();
			this.lbFont = new System.Windows.Forms.Label();
			this.lbPreviewText = new System.Windows.Forms.Label();
			this.bnDelete = new System.Windows.Forms.Button();
			this.tbName = new System.Windows.Forms.TextBox();
			this.tbPreviewText = new System.Windows.Forms.TextBox();
			this.cbbFont = new System.Windows.Forms.ComboBox();
			this.lvState = new System.Windows.Forms.ListView();
			this.colState = new System.Windows.Forms.ColumnHeader();
			this.colColor = new System.Windows.Forms.ColumnHeader();
			this.colColor2 = new System.Windows.Forms.ColumnHeader();
			this.colEffectColor = new System.Windows.Forms.ColumnHeader();
			this.colUnderline = new System.Windows.Forms.ColumnHeader();
			this.colShadow = new System.Windows.Forms.ColumnHeader();
			this.colOutline = new System.Windows.Forms.ColumnHeader();
			this.gbPreview = new System.Windows.Forms.GroupBox();
			this.bnClose = new System.Windows.Forms.Button();
			this.bnNew = new System.Windows.Forms.Button();
			this.bnPreviewBackColor = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// lbList
			// 
			this.lbList.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
						| System.Windows.Forms.AnchorStyles.Left)));
			this.lbList.FormattingEnabled = true;
			this.lbList.ItemHeight = 14;
			this.lbList.Location = new System.Drawing.Point(12, 12);
			this.lbList.Name = "lbList";
			this.lbList.Size = new System.Drawing.Size(186, 522);
			this.lbList.Sorted = true;
			this.lbList.TabIndex = 0;
			this.lbList.SelectedIndexChanged += new System.EventHandler(this.lbList_SelectedIndexChanged);
			// 
			// lbName
			// 
			this.lbName.AutoSize = true;
			this.lbName.Location = new System.Drawing.Point(204, 15);
			this.lbName.Name = "lbName";
			this.lbName.Size = new System.Drawing.Size(91, 14);
			this.lbName.TabIndex = 1;
			this.lbName.Text = "字体样式名称";
			// 
			// lbFont
			// 
			this.lbFont.AutoSize = true;
			this.lbFont.Location = new System.Drawing.Point(204, 44);
			this.lbFont.Name = "lbFont";
			this.lbFont.Size = new System.Drawing.Size(49, 14);
			this.lbFont.TabIndex = 3;
			this.lbFont.Text = "字体ID";
			// 
			// lbPreviewText
			// 
			this.lbPreviewText.AutoSize = true;
			this.lbPreviewText.Location = new System.Drawing.Point(204, 73);
			this.lbPreviewText.Name = "lbPreviewText";
			this.lbPreviewText.Size = new System.Drawing.Size(63, 14);
			this.lbPreviewText.TabIndex = 5;
			this.lbPreviewText.Text = "预览文字";
			// 
			// bnDelete
			// 
			this.bnDelete.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.bnDelete.Location = new System.Drawing.Point(288, 502);
			this.bnDelete.Name = "bnDelete";
			this.bnDelete.Size = new System.Drawing.Size(75, 32);
			this.bnDelete.TabIndex = 10;
			this.bnDelete.Text = "删除(&D)";
			this.bnDelete.UseVisualStyleBackColor = true;
			this.bnDelete.Click += new System.EventHandler(this.bnDelete_Click);
			// 
			// tbName
			// 
			this.tbName.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.tbName.Location = new System.Drawing.Point(301, 12);
			this.tbName.MaxLength = 32;
			this.tbName.Name = "tbName";
			this.tbName.Size = new System.Drawing.Size(299, 23);
			this.tbName.TabIndex = 2;
			this.tbName.Leave += new System.EventHandler(this.tbName_Leave);
			// 
			// tbPreviewText
			// 
			this.tbPreviewText.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.tbPreviewText.Location = new System.Drawing.Point(301, 69);
			this.tbPreviewText.MaxLength = 32;
			this.tbPreviewText.Name = "tbPreviewText";
			this.tbPreviewText.Size = new System.Drawing.Size(215, 23);
			this.tbPreviewText.TabIndex = 6;
			this.tbPreviewText.Text = "mio测试012";
			this.tbPreviewText.TextChanged += new System.EventHandler(this.tbPreviewText_TextChanged);
			// 
			// cbbFont
			// 
			this.cbbFont.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.cbbFont.FormattingEnabled = true;
			this.cbbFont.Location = new System.Drawing.Point(301, 41);
			this.cbbFont.Name = "cbbFont";
			this.cbbFont.Size = new System.Drawing.Size(299, 22);
			this.cbbFont.Sorted = true;
			this.cbbFont.TabIndex = 4;
			this.cbbFont.SelectedIndexChanged += new System.EventHandler(this.cbbFont_Leave);
			this.cbbFont.Leave += new System.EventHandler(this.cbbFont_Leave);
			// 
			// lvState
			// 
			this.lvState.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.lvState.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.colState,
            this.colColor,
            this.colColor2,
            this.colEffectColor,
            this.colUnderline,
            this.colShadow,
            this.colOutline});
			this.lvState.GridLines = true;
			this.lvState.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
			this.lvState.HideSelection = false;
			this.lvState.Items.AddRange(new System.Windows.Forms.ListViewItem[] {
            listViewItem1,
            listViewItem2,
            listViewItem3,
            listViewItem4,
            listViewItem5});
			this.lvState.Location = new System.Drawing.Point(206, 98);
			this.lvState.MultiSelect = false;
			this.lvState.Name = "lvState";
			this.lvState.Scrollable = false;
			this.lvState.Size = new System.Drawing.Size(394, 99);
			this.lvState.TabIndex = 7;
			this.lvState.UseCompatibleStateImageBehavior = false;
			this.lvState.View = System.Windows.Forms.View.Details;
			this.lvState.MouseClick += new System.Windows.Forms.MouseEventHandler(this.lvState_MouseClick);
			// 
			// colState
			// 
			this.colState.Text = "状态";
			this.colState.Width = 36;
			// 
			// colColor
			// 
			this.colColor.Text = "文本色";
			this.colColor.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
			this.colColor.Width = 57;
			// 
			// colColor2
			// 
			this.colColor2.Text = "过渡色";
			this.colColor2.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
			this.colColor2.Width = 57;
			// 
			// colEffectColor
			// 
			this.colEffectColor.Text = "效果色";
			this.colEffectColor.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
			this.colEffectColor.Width = 57;
			// 
			// colUnderline
			// 
			this.colUnderline.Text = "下划线";
			this.colUnderline.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.colUnderline.Width = 57;
			// 
			// colShadow
			// 
			this.colShadow.Text = "阴影";
			this.colShadow.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.colShadow.Width = 43;
			// 
			// colOutline
			// 
			this.colOutline.Text = "描边";
			this.colOutline.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.colOutline.Width = 43;
			// 
			// gbPreview
			// 
			this.gbPreview.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
						| System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.gbPreview.Location = new System.Drawing.Point(206, 203);
			this.gbPreview.Name = "gbPreview";
			this.gbPreview.Size = new System.Drawing.Size(394, 293);
			this.gbPreview.TabIndex = 8;
			this.gbPreview.TabStop = false;
			this.gbPreview.Text = "预览";
			// 
			// bnClose
			// 
			this.bnClose.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.bnClose.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.bnClose.Location = new System.Drawing.Point(525, 502);
			this.bnClose.Name = "bnClose";
			this.bnClose.Size = new System.Drawing.Size(75, 32);
			this.bnClose.TabIndex = 12;
			this.bnClose.Text = "关闭(&C)";
			this.bnClose.UseVisualStyleBackColor = true;
			this.bnClose.Click += new System.EventHandler(this.bnClose_Click);
			// 
			// bnNew
			// 
			this.bnNew.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.bnNew.Location = new System.Drawing.Point(206, 502);
			this.bnNew.Name = "bnNew";
			this.bnNew.Size = new System.Drawing.Size(75, 32);
			this.bnNew.TabIndex = 9;
			this.bnNew.Text = "新建(&N)";
			this.bnNew.UseVisualStyleBackColor = true;
			this.bnNew.Click += new System.EventHandler(this.bnNew_Click);
			// 
			// bnPreviewBackColor
			// 
			this.bnPreviewBackColor.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
			this.bnPreviewBackColor.Location = new System.Drawing.Point(521, 68);
			this.bnPreviewBackColor.Name = "bnPreviewBackColor";
			this.bnPreviewBackColor.Size = new System.Drawing.Size(78, 23);
			this.bnPreviewBackColor.TabIndex = 9;
			this.bnPreviewBackColor.Text = "背景色(&B)";
			this.bnPreviewBackColor.UseVisualStyleBackColor = true;
			this.bnPreviewBackColor.Click += new System.EventHandler(this.bnPreviewBackColor_Click);
			// 
			// CFormMainFont
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(611, 548);
			this.Controls.Add(this.gbPreview);
			this.Controls.Add(this.lvState);
			this.Controls.Add(this.cbbFont);
			this.Controls.Add(this.tbPreviewText);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.bnClose);
			this.Controls.Add(this.bnDelete);
			this.Controls.Add(this.bnPreviewBackColor);
			this.Controls.Add(this.bnNew);
			this.Controls.Add(this.lbPreviewText);
			this.Controls.Add(this.lbFont);
			this.Controls.Add(this.lbName);
			this.Controls.Add(this.lbList);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.KeyPreview = true;
			this.MaximizeBox = false;
			this.Name = "CFormMainFont";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
			this.Text = "GUIEditor2 - 字体样式编辑";
			this.Shown += new System.EventHandler(this.CFormMainFont_Shown);
			this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.CFormMainFont_FormClosing);
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.ListBox lbList;
		private System.Windows.Forms.Label lbName;
		private System.Windows.Forms.Label lbFont;
		private System.Windows.Forms.Label lbPreviewText;
		private System.Windows.Forms.Button bnDelete;
		private System.Windows.Forms.TextBox tbName;
		private System.Windows.Forms.TextBox tbPreviewText;
		private System.Windows.Forms.ComboBox cbbFont;
		private System.Windows.Forms.ListView lvState;
		private System.Windows.Forms.ColumnHeader colState;
		private System.Windows.Forms.ColumnHeader colColor;
		private System.Windows.Forms.ColumnHeader colEffectColor;
		private System.Windows.Forms.ColumnHeader colUnderline;
		private System.Windows.Forms.ColumnHeader colShadow;
		private System.Windows.Forms.ColumnHeader colOutline;
		private System.Windows.Forms.GroupBox gbPreview;
		private System.Windows.Forms.Button bnClose;
		private System.Windows.Forms.Button bnNew;
		private System.Windows.Forms.Button bnPreviewBackColor;
		private System.Windows.Forms.ColumnHeader colColor2;
	}
}
