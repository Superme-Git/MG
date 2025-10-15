namespace GUIEditor2
{
	partial class CFormMainImage
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
			this.spImage = new System.Windows.Forms.SplitContainer();
			this.bnBkColor = new System.Windows.Forms.Button();
			this.cbNoAlpha = new System.Windows.Forms.CheckBox();
			this.gbProfile = new System.Windows.Forms.GroupBox();
			this.bnProfileDelete = new System.Windows.Forms.Button();
			this.bnProfileSave = new System.Windows.Forms.Button();
			this.cbbProfile = new System.Windows.Forms.ComboBox();
			this.lbBkColor = new System.Windows.Forms.Label();
			this.lbNote = new System.Windows.Forms.Label();
			this.gbImageFile = new System.Windows.Forms.GroupBox();
			this.cbImage = new System.Windows.Forms.CheckBox();
			this.tbFileName = new System.Windows.Forms.TextBox();
			this.bnFileName = new System.Windows.Forms.Button();
			this.bnCancel = new System.Windows.Forms.Button();
			this.bnOK = new System.Windows.Forms.Button();
			this.gbExpand = new System.Windows.Forms.GroupBox();
			this.cbStretchH = new System.Windows.Forms.CheckBox();
			this.cbStretchW = new System.Windows.Forms.CheckBox();
			this.gbBlock = new System.Windows.Forms.GroupBox();
			this.cbBlock = new System.Windows.Forms.CheckBox();
			this.lbBlockH1 = new System.Windows.Forms.Label();
			this.lbBlockW1 = new System.Windows.Forms.Label();
			this.lbBlockW0 = new System.Windows.Forms.Label();
			this.lbBlockH0 = new System.Windows.Forms.Label();
			this.tbBlockH1 = new System.Windows.Forms.TextBox();
			this.tbBlockW1 = new System.Windows.Forms.TextBox();
			this.tbBlockW0 = new System.Windows.Forms.TextBox();
			this.tbBlockH0 = new System.Windows.Forms.TextBox();
			this.gbSelect = new System.Windows.Forms.GroupBox();
			this.bnSelectAll = new System.Windows.Forms.Button();
			this.bnSelectClear = new System.Windows.Forms.Button();
			this.lbSelectY = new System.Windows.Forms.Label();
			this.lbSelectH = new System.Windows.Forms.Label();
			this.lbSelectW = new System.Windows.Forms.Label();
			this.lbSelectX = new System.Windows.Forms.Label();
			this.tbSelectY = new System.Windows.Forms.TextBox();
			this.tbSelectH = new System.Windows.Forms.TextBox();
			this.tbSelectW = new System.Windows.Forms.TextBox();
			this.tbSelectX = new System.Windows.Forms.TextBox();
			this.dlgOpenFile = new System.Windows.Forms.OpenFileDialog();
			this.spImage.Panel2.SuspendLayout();
			this.spImage.SuspendLayout();
			this.gbProfile.SuspendLayout();
			this.gbImageFile.SuspendLayout();
			this.gbExpand.SuspendLayout();
			this.gbBlock.SuspendLayout();
			this.gbSelect.SuspendLayout();
			this.SuspendLayout();
			// 
			// spImage
			// 
			this.spImage.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.spImage.Dock = System.Windows.Forms.DockStyle.Fill;
			this.spImage.FixedPanel = System.Windows.Forms.FixedPanel.Panel2;
			this.spImage.Location = new System.Drawing.Point(0, 0);
			this.spImage.Name = "spImage";
			// 
			// spImage.Panel1
			// 
			this.spImage.Panel1.AutoScroll = true;
			this.spImage.Panel1MinSize = 100;
			// 
			// spImage.Panel2
			// 
			this.spImage.Panel2.AutoScroll = true;
			this.spImage.Panel2.Controls.Add(this.bnBkColor);
			this.spImage.Panel2.Controls.Add(this.cbNoAlpha);
			this.spImage.Panel2.Controls.Add(this.gbProfile);
			this.spImage.Panel2.Controls.Add(this.lbBkColor);
			this.spImage.Panel2.Controls.Add(this.lbNote);
			this.spImage.Panel2.Controls.Add(this.gbImageFile);
			this.spImage.Panel2.Controls.Add(this.bnCancel);
			this.spImage.Panel2.Controls.Add(this.bnOK);
			this.spImage.Panel2.Controls.Add(this.gbExpand);
			this.spImage.Panel2.Controls.Add(this.gbBlock);
			this.spImage.Panel2.Controls.Add(this.gbSelect);
			this.spImage.Panel2MinSize = 100;
			this.spImage.Size = new System.Drawing.Size(892, 576);
			this.spImage.SplitterDistance = 587;
			this.spImage.SplitterWidth = 2;
			this.spImage.TabIndex = 0;
			// 
			// bnBkColor
			// 
			this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor.Location = new System.Drawing.Point(120, 421);
			this.bnBkColor.Name = "bnBkColor";
			this.bnBkColor.Size = new System.Drawing.Size(170, 22);
			this.bnBkColor.TabIndex = 17;
			this.bnBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor.UseVisualStyleBackColor = true;
			this.bnBkColor.Click += new System.EventHandler(this.bnBkColor_Click);
			// 
			// cbNoAlpha
			// 
			this.cbNoAlpha.AutoSize = true;
			this.cbNoAlpha.Location = new System.Drawing.Point(138, 449);
			this.cbNoAlpha.Name = "cbNoAlpha";
			this.cbNoAlpha.Size = new System.Drawing.Size(152, 18);
			this.cbNoAlpha.TabIndex = 0;
			this.cbNoAlpha.Text = "不使用图像透明通道";
			this.cbNoAlpha.UseVisualStyleBackColor = true;
			this.cbNoAlpha.CheckedChanged += new System.EventHandler(this.cbNoAlpha_CheckedChanged);
			// 
			// gbProfile
			// 
			this.gbProfile.Controls.Add(this.bnProfileDelete);
			this.gbProfile.Controls.Add(this.bnProfileSave);
			this.gbProfile.Controls.Add(this.cbbProfile);
			this.gbProfile.Location = new System.Drawing.Point(6, 359);
			this.gbProfile.Name = "gbProfile";
			this.gbProfile.Size = new System.Drawing.Size(290, 56);
			this.gbProfile.TabIndex = 4;
			this.gbProfile.TabStop = false;
			this.gbProfile.Text = "方案";
			// 
			// bnProfileDelete
			// 
			this.bnProfileDelete.Location = new System.Drawing.Point(232, 21);
			this.bnProfileDelete.Name = "bnProfileDelete";
			this.bnProfileDelete.Size = new System.Drawing.Size(52, 22);
			this.bnProfileDelete.TabIndex = 2;
			this.bnProfileDelete.Text = "删除";
			this.bnProfileDelete.UseVisualStyleBackColor = true;
			this.bnProfileDelete.Click += new System.EventHandler(this.bnProfileDelete_Click);
			// 
			// bnProfileSave
			// 
			this.bnProfileSave.Location = new System.Drawing.Point(174, 21);
			this.bnProfileSave.Name = "bnProfileSave";
			this.bnProfileSave.Size = new System.Drawing.Size(52, 22);
			this.bnProfileSave.TabIndex = 1;
			this.bnProfileSave.Text = "保存";
			this.bnProfileSave.UseVisualStyleBackColor = true;
			this.bnProfileSave.Click += new System.EventHandler(this.bnProfileSave_Click);
			// 
			// cbbProfile
			// 
			this.cbbProfile.FormattingEnabled = true;
			this.cbbProfile.Location = new System.Drawing.Point(6, 22);
			this.cbbProfile.Name = "cbbProfile";
			this.cbbProfile.Size = new System.Drawing.Size(162, 22);
			this.cbbProfile.TabIndex = 0;
			this.cbbProfile.SelectedIndexChanged += new System.EventHandler(this.cbbProfile_SelectedIndexChanged);
			// 
			// lbBkColor
			// 
			this.lbBkColor.AutoSize = true;
			this.lbBkColor.Location = new System.Drawing.Point(9, 425);
			this.lbBkColor.Name = "lbBkColor";
			this.lbBkColor.Size = new System.Drawing.Size(105, 14);
			this.lbBkColor.TabIndex = 16;
			this.lbBkColor.Text = "背景色(透明色)";
			// 
			// lbNote
			// 
			this.lbNote.Location = new System.Drawing.Point(9, 506);
			this.lbNote.Name = "lbNote";
			this.lbNote.Size = new System.Drawing.Size(281, 35);
			this.lbNote.TabIndex = 7;
			this.lbNote.Text = "说明：部分控件的大小会随图像选区的大小自动改变(使用九宫格除外)。";
			// 
			// gbImageFile
			// 
			this.gbImageFile.Controls.Add(this.cbImage);
			this.gbImageFile.Controls.Add(this.tbFileName);
			this.gbImageFile.Controls.Add(this.bnFileName);
			this.gbImageFile.Location = new System.Drawing.Point(6, 10);
			this.gbImageFile.Name = "gbImageFile";
			this.gbImageFile.Size = new System.Drawing.Size(290, 58);
			this.gbImageFile.TabIndex = 0;
			this.gbImageFile.TabStop = false;
			// 
			// cbImage
			// 
			this.cbImage.AutoSize = true;
			this.cbImage.Checked = true;
			this.cbImage.CheckState = System.Windows.Forms.CheckState.Checked;
			this.cbImage.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(0)))), ((int)(((byte)(192)))));
			this.cbImage.Location = new System.Drawing.Point(6, 0);
			this.cbImage.Name = "cbImage";
			this.cbImage.Size = new System.Drawing.Size(103, 18);
			this.cbImage.TabIndex = 0;
			this.cbImage.Text = "图像文件(&F)";
			this.cbImage.UseVisualStyleBackColor = true;
			this.cbImage.CheckedChanged += new System.EventHandler(this.cbImage_CheckedChanged);
			// 
			// tbFileName
			// 
			this.tbFileName.Location = new System.Drawing.Point(0, 22);
			this.tbFileName.Name = "tbFileName";
			this.tbFileName.ReadOnly = true;
			this.tbFileName.Size = new System.Drawing.Size(242, 23);
			this.tbFileName.TabIndex = 1;
			// 
			// bnFileName
			// 
			this.bnFileName.Location = new System.Drawing.Point(246, 21);
			this.bnFileName.Name = "bnFileName";
			this.bnFileName.Size = new System.Drawing.Size(38, 23);
			this.bnFileName.TabIndex = 2;
			this.bnFileName.Text = "...";
			this.bnFileName.UseVisualStyleBackColor = true;
			this.bnFileName.Click += new System.EventHandler(this.bnFileName_Click);
			// 
			// bnCancel
			// 
			this.bnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.bnCancel.Location = new System.Drawing.Point(189, 473);
			this.bnCancel.Name = "bnCancel";
			this.bnCancel.Size = new System.Drawing.Size(101, 30);
			this.bnCancel.TabIndex = 6;
			this.bnCancel.Text = "取消(&C)";
			this.bnCancel.UseVisualStyleBackColor = true;
			this.bnCancel.Click += new System.EventHandler(this.bnCancel_Click);
			// 
			// bnOK
			// 
			this.bnOK.Location = new System.Drawing.Point(13, 473);
			this.bnOK.Name = "bnOK";
			this.bnOK.Size = new System.Drawing.Size(101, 30);
			this.bnOK.TabIndex = 5;
			this.bnOK.Text = "确定(&O)";
			this.bnOK.UseVisualStyleBackColor = true;
			this.bnOK.Click += new System.EventHandler(this.bnOK_Click);
			// 
			// gbExpand
			// 
			this.gbExpand.Controls.Add(this.cbStretchH);
			this.gbExpand.Controls.Add(this.cbStretchW);
			this.gbExpand.Location = new System.Drawing.Point(6, 303);
			this.gbExpand.Name = "gbExpand";
			this.gbExpand.Size = new System.Drawing.Size(290, 50);
			this.gbExpand.TabIndex = 3;
			this.gbExpand.TabStop = false;
			this.gbExpand.Text = "扩展模式(&E)(默认为平铺)";
			// 
			// cbStretchH
			// 
			this.cbStretchH.AutoSize = true;
			this.cbStretchH.Location = new System.Drawing.Point(141, 22);
			this.cbStretchH.Name = "cbStretchH";
			this.cbStretchH.Size = new System.Drawing.Size(82, 18);
			this.cbStretchH.TabIndex = 1;
			this.cbStretchH.Text = "垂直拉伸";
			this.cbStretchH.UseVisualStyleBackColor = true;
			// 
			// cbStretchW
			// 
			this.cbStretchW.AutoSize = true;
			this.cbStretchW.Location = new System.Drawing.Point(11, 22);
			this.cbStretchW.Name = "cbStretchW";
			this.cbStretchW.Size = new System.Drawing.Size(82, 18);
			this.cbStretchW.TabIndex = 0;
			this.cbStretchW.Text = "水平拉伸";
			this.cbStretchW.UseVisualStyleBackColor = true;
			// 
			// gbBlock
			// 
			this.gbBlock.Controls.Add(this.cbBlock);
			this.gbBlock.Controls.Add(this.lbBlockH1);
			this.gbBlock.Controls.Add(this.lbBlockW1);
			this.gbBlock.Controls.Add(this.lbBlockW0);
			this.gbBlock.Controls.Add(this.lbBlockH0);
			this.gbBlock.Controls.Add(this.tbBlockH1);
			this.gbBlock.Controls.Add(this.tbBlockW1);
			this.gbBlock.Controls.Add(this.tbBlockW0);
			this.gbBlock.Controls.Add(this.tbBlockH0);
			this.gbBlock.Location = new System.Drawing.Point(6, 166);
			this.gbBlock.Name = "gbBlock";
			this.gbBlock.Size = new System.Drawing.Size(290, 131);
			this.gbBlock.TabIndex = 2;
			this.gbBlock.TabStop = false;
			// 
			// cbBlock
			// 
			this.cbBlock.AutoSize = true;
			this.cbBlock.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(0)))), ((int)(((byte)(192)))));
			this.cbBlock.Location = new System.Drawing.Point(6, 0);
			this.cbBlock.Name = "cbBlock";
			this.cbBlock.Size = new System.Drawing.Size(117, 18);
			this.cbBlock.TabIndex = 0;
			this.cbBlock.Text = "使用九宫格(&B)";
			this.cbBlock.UseVisualStyleBackColor = true;
			this.cbBlock.CheckedChanged += new System.EventHandler(this.cbBlock_CheckedChanged);
			// 
			// lbBlockH1
			// 
			this.lbBlockH1.AutoSize = true;
			this.lbBlockH1.Location = new System.Drawing.Point(121, 107);
			this.lbBlockH1.Name = "lbBlockH1";
			this.lbBlockH1.Size = new System.Drawing.Size(49, 14);
			this.lbBlockH1.TabIndex = 3;
			this.lbBlockH1.Text = "下边距";
			// 
			// lbBlockW1
			// 
			this.lbBlockW1.AutoSize = true;
			this.lbBlockW1.Location = new System.Drawing.Point(226, 62);
			this.lbBlockW1.Name = "lbBlockW1";
			this.lbBlockW1.Size = new System.Drawing.Size(49, 14);
			this.lbBlockW1.TabIndex = 7;
			this.lbBlockW1.Text = "右边距";
			// 
			// lbBlockW0
			// 
			this.lbBlockW0.AutoSize = true;
			this.lbBlockW0.Location = new System.Drawing.Point(14, 62);
			this.lbBlockW0.Name = "lbBlockW0";
			this.lbBlockW0.Size = new System.Drawing.Size(49, 14);
			this.lbBlockW0.TabIndex = 5;
			this.lbBlockW0.Text = "左边距";
			// 
			// lbBlockH0
			// 
			this.lbBlockH0.AutoSize = true;
			this.lbBlockH0.Location = new System.Drawing.Point(121, 19);
			this.lbBlockH0.Name = "lbBlockH0";
			this.lbBlockH0.Size = new System.Drawing.Size(49, 14);
			this.lbBlockH0.TabIndex = 1;
			this.lbBlockH0.Text = "上边距";
			// 
			// tbBlockH1
			// 
			this.tbBlockH1.Enabled = false;
			this.tbBlockH1.Location = new System.Drawing.Point(118, 80);
			this.tbBlockH1.Name = "tbBlockH1";
			this.tbBlockH1.Size = new System.Drawing.Size(53, 23);
			this.tbBlockH1.TabIndex = 4;
			this.tbBlockH1.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.tbBlockH1.Leave += new System.EventHandler(this.tb_Leave);
			this.tbBlockH1.MouseEnter += new System.EventHandler(this.tbBlockH1_MouseEnter);
			// 
			// tbBlockW1
			// 
			this.tbBlockW1.Enabled = false;
			this.tbBlockW1.Location = new System.Drawing.Point(170, 58);
			this.tbBlockW1.Name = "tbBlockW1";
			this.tbBlockW1.Size = new System.Drawing.Size(54, 23);
			this.tbBlockW1.TabIndex = 8;
			this.tbBlockW1.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.tbBlockW1.Leave += new System.EventHandler(this.tb_Leave);
			this.tbBlockW1.MouseEnter += new System.EventHandler(this.tbBlockW1_MouseEnter);
			// 
			// tbBlockW0
			// 
			this.tbBlockW0.Enabled = false;
			this.tbBlockW0.Location = new System.Drawing.Point(66, 58);
			this.tbBlockW0.Name = "tbBlockW0";
			this.tbBlockW0.Size = new System.Drawing.Size(53, 23);
			this.tbBlockW0.TabIndex = 6;
			this.tbBlockW0.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.tbBlockW0.Leave += new System.EventHandler(this.tb_Leave);
			this.tbBlockW0.MouseEnter += new System.EventHandler(this.tbBlockW0_MouseEnter);
			// 
			// tbBlockH0
			// 
			this.tbBlockH0.Enabled = false;
			this.tbBlockH0.Location = new System.Drawing.Point(118, 36);
			this.tbBlockH0.Name = "tbBlockH0";
			this.tbBlockH0.Size = new System.Drawing.Size(53, 23);
			this.tbBlockH0.TabIndex = 2;
			this.tbBlockH0.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
			this.tbBlockH0.Leave += new System.EventHandler(this.tb_Leave);
			this.tbBlockH0.MouseEnter += new System.EventHandler(this.tbBlockH0_MouseEnter);
			// 
			// gbSelect
			// 
			this.gbSelect.Controls.Add(this.bnSelectAll);
			this.gbSelect.Controls.Add(this.bnSelectClear);
			this.gbSelect.Controls.Add(this.lbSelectY);
			this.gbSelect.Controls.Add(this.lbSelectH);
			this.gbSelect.Controls.Add(this.lbSelectW);
			this.gbSelect.Controls.Add(this.lbSelectX);
			this.gbSelect.Controls.Add(this.tbSelectY);
			this.gbSelect.Controls.Add(this.tbSelectH);
			this.gbSelect.Controls.Add(this.tbSelectW);
			this.gbSelect.Controls.Add(this.tbSelectX);
			this.gbSelect.Location = new System.Drawing.Point(6, 74);
			this.gbSelect.Name = "gbSelect";
			this.gbSelect.Size = new System.Drawing.Size(290, 86);
			this.gbSelect.TabIndex = 1;
			this.gbSelect.TabStop = false;
			this.gbSelect.Text = "图像选区(&S)";
			// 
			// bnSelectAll
			// 
			this.bnSelectAll.Location = new System.Drawing.Point(182, 48);
			this.bnSelectAll.Name = "bnSelectAll";
			this.bnSelectAll.Size = new System.Drawing.Size(102, 24);
			this.bnSelectAll.TabIndex = 9;
			this.bnSelectAll.Text = "选择全部(&A)";
			this.bnSelectAll.UseVisualStyleBackColor = true;
			this.bnSelectAll.Click += new System.EventHandler(this.bnSelectAll_Click);
			// 
			// bnSelectClear
			// 
			this.bnSelectClear.Location = new System.Drawing.Point(181, 19);
			this.bnSelectClear.Name = "bnSelectClear";
			this.bnSelectClear.Size = new System.Drawing.Size(103, 24);
			this.bnSelectClear.TabIndex = 8;
			this.bnSelectClear.Text = "清除选区(&D)";
			this.bnSelectClear.UseVisualStyleBackColor = true;
			this.bnSelectClear.Click += new System.EventHandler(this.bnSelectClear_Click);
			// 
			// lbSelectY
			// 
			this.lbSelectY.AutoSize = true;
			this.lbSelectY.Location = new System.Drawing.Point(15, 52);
			this.lbSelectY.Name = "lbSelectY";
			this.lbSelectY.Size = new System.Drawing.Size(14, 14);
			this.lbSelectY.TabIndex = 2;
			this.lbSelectY.Text = "Y";
			// 
			// lbSelectH
			// 
			this.lbSelectH.AutoSize = true;
			this.lbSelectH.Location = new System.Drawing.Point(94, 52);
			this.lbSelectH.Name = "lbSelectH";
			this.lbSelectH.Size = new System.Drawing.Size(21, 14);
			this.lbSelectH.TabIndex = 6;
			this.lbSelectH.Text = "高";
			// 
			// lbSelectW
			// 
			this.lbSelectW.AutoSize = true;
			this.lbSelectW.Location = new System.Drawing.Point(94, 22);
			this.lbSelectW.Name = "lbSelectW";
			this.lbSelectW.Size = new System.Drawing.Size(21, 14);
			this.lbSelectW.TabIndex = 4;
			this.lbSelectW.Text = "宽";
			// 
			// lbSelectX
			// 
			this.lbSelectX.AutoSize = true;
			this.lbSelectX.Location = new System.Drawing.Point(15, 22);
			this.lbSelectX.Name = "lbSelectX";
			this.lbSelectX.Size = new System.Drawing.Size(14, 14);
			this.lbSelectX.TabIndex = 0;
			this.lbSelectX.Text = "X";
			// 
			// tbSelectY
			// 
			this.tbSelectY.Location = new System.Drawing.Point(32, 48);
			this.tbSelectY.Name = "tbSelectY";
			this.tbSelectY.Size = new System.Drawing.Size(53, 23);
			this.tbSelectY.TabIndex = 3;
			this.tbSelectY.Leave += new System.EventHandler(this.tb_Leave);
			this.tbSelectY.MouseEnter += new System.EventHandler(this.tbSelectY_MouseEnter);
			// 
			// tbSelectH
			// 
			this.tbSelectH.Location = new System.Drawing.Point(118, 48);
			this.tbSelectH.Name = "tbSelectH";
			this.tbSelectH.Size = new System.Drawing.Size(53, 23);
			this.tbSelectH.TabIndex = 7;
			this.tbSelectH.Leave += new System.EventHandler(this.tb_Leave);
			this.tbSelectH.MouseEnter += new System.EventHandler(this.tbSelectH_MouseEnter);
			// 
			// tbSelectW
			// 
			this.tbSelectW.Location = new System.Drawing.Point(118, 19);
			this.tbSelectW.Name = "tbSelectW";
			this.tbSelectW.Size = new System.Drawing.Size(53, 23);
			this.tbSelectW.TabIndex = 5;
			this.tbSelectW.Leave += new System.EventHandler(this.tb_Leave);
			this.tbSelectW.MouseEnter += new System.EventHandler(this.tbSelectW_MouseEnter);
			// 
			// tbSelectX
			// 
			this.tbSelectX.Location = new System.Drawing.Point(32, 19);
			this.tbSelectX.Name = "tbSelectX";
			this.tbSelectX.Size = new System.Drawing.Size(53, 23);
			this.tbSelectX.TabIndex = 1;
			this.tbSelectX.Leave += new System.EventHandler(this.tb_Leave);
			this.tbSelectX.MouseEnter += new System.EventHandler(this.tbSelectX_MouseEnter);
			// 
			// dlgOpenFile
			// 
			this.dlgOpenFile.Filter = "图像文件|*.bmp;*.dds;*.dib;*.tga;*.png;*.jpg|所有文件|*.*";
			this.dlgOpenFile.RestoreDirectory = true;
			this.dlgOpenFile.ValidateNames = false;
			// 
			// CFormMainImage
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.CancelButton = this.bnCancel;
			this.ClientSize = new System.Drawing.Size(892, 576);
			this.Controls.Add(this.spImage);
			this.KeyPreview = true;
			this.MinimumSize = new System.Drawing.Size(600, 525);
			this.Name = "CFormMainImage";
			this.ShowInTaskbar = false;
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
			this.Text = "GUIEditor2 - 图像编辑";
			this.Load += new System.EventHandler(this.CFormMainImage_Load);
			this.spImage.Panel2.ResumeLayout(false);
			this.spImage.Panel2.PerformLayout();
			this.spImage.ResumeLayout(false);
			this.gbProfile.ResumeLayout(false);
			this.gbImageFile.ResumeLayout(false);
			this.gbImageFile.PerformLayout();
			this.gbExpand.ResumeLayout(false);
			this.gbExpand.PerformLayout();
			this.gbBlock.ResumeLayout(false);
			this.gbBlock.PerformLayout();
			this.gbSelect.ResumeLayout(false);
			this.gbSelect.PerformLayout();
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.SplitContainer spImage;
		private System.Windows.Forms.Button bnFileName;
		private System.Windows.Forms.TextBox tbFileName;
		private System.Windows.Forms.GroupBox gbSelect;
		private System.Windows.Forms.Label lbSelectY;
		private System.Windows.Forms.Label lbSelectH;
		private System.Windows.Forms.Label lbSelectW;
		private System.Windows.Forms.Label lbSelectX;
		private System.Windows.Forms.TextBox tbSelectY;
		private System.Windows.Forms.TextBox tbSelectH;
		private System.Windows.Forms.TextBox tbSelectW;
		private System.Windows.Forms.TextBox tbSelectX;
		private System.Windows.Forms.GroupBox gbBlock;
		private System.Windows.Forms.Label lbBlockH1;
		private System.Windows.Forms.Label lbBlockW1;
		private System.Windows.Forms.Label lbBlockW0;
		private System.Windows.Forms.Label lbBlockH0;
		private System.Windows.Forms.TextBox tbBlockH1;
		private System.Windows.Forms.TextBox tbBlockW1;
		private System.Windows.Forms.TextBox tbBlockW0;
		private System.Windows.Forms.TextBox tbBlockH0;
		private System.Windows.Forms.Button bnCancel;
		private System.Windows.Forms.Button bnOK;
		private System.Windows.Forms.GroupBox gbExpand;
		private System.Windows.Forms.GroupBox gbImageFile;
		private System.Windows.Forms.CheckBox cbBlock;
		private System.Windows.Forms.CheckBox cbStretchH;
		private System.Windows.Forms.CheckBox cbStretchW;
		private System.Windows.Forms.OpenFileDialog dlgOpenFile;
		private System.Windows.Forms.Button bnSelectClear;
		private System.Windows.Forms.Button bnSelectAll;
		private System.Windows.Forms.CheckBox cbImage;
		private System.Windows.Forms.Label lbNote;
		private System.Windows.Forms.GroupBox gbProfile;
		private System.Windows.Forms.ComboBox cbbProfile;
		private System.Windows.Forms.Button bnProfileDelete;
		private System.Windows.Forms.Button bnProfileSave;
		private System.Windows.Forms.Button bnBkColor;
		private System.Windows.Forms.Label lbBkColor;
		private System.Windows.Forms.CheckBox cbNoAlpha;

	}
}
