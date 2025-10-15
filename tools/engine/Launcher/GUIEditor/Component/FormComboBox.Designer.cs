namespace GUIEditor2
{
	partial class CFormComboBox
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
			this.lbName = new System.Windows.Forms.Label();
			this.tbName = new System.Windows.Forms.TextBox();
			this.lbType = new System.Windows.Forms.Label();
			this.tbType = new System.Windows.Forms.TextBox();
			this.lbX = new System.Windows.Forms.Label();
			this.tbX = new System.Windows.Forms.TextBox();
			this.lbW = new System.Windows.Forms.Label();
			this.tbW = new System.Windows.Forms.TextBox();
			this.lbY = new System.Windows.Forms.Label();
			this.lbH = new System.Windows.Forms.Label();
			this.tbY = new System.Windows.Forms.TextBox();
			this.tbH = new System.Windows.Forms.TextBox();
			this.lbBkColor1 = new System.Windows.Forms.Label();
			this.bnBkColor1 = new System.Windows.Forms.Button();
			this.lbBkColor3 = new System.Windows.Forms.Label();
			this.bnBkColor3 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbFont = new System.Windows.Forms.Label();
			this.cbbFont = new System.Windows.Forms.ComboBox();
			this.cbbScrollV = new System.Windows.Forms.ComboBox();
			this.gbScrollV = new System.Windows.Forms.GroupBox();
			this.gbScrollH = new System.Windows.Forms.GroupBox();
			this.cbbScrollH = new System.Windows.Forms.ComboBox();
			this.lbImageBox = new System.Windows.Forms.Label();
			this.bnImageBox = new System.Windows.Forms.Button();
			this.lbImageList = new System.Windows.Forms.Label();
			this.bnImageList = new System.Windows.Forms.Button();
			this.lbImage0 = new System.Windows.Forms.Label();
			this.bnImage0 = new System.Windows.Forms.Button();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.lbImage2 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.bnImage2 = new System.Windows.Forms.Button();
			this.lbImage3 = new System.Windows.Forms.Label();
			this.bnImage3 = new System.Windows.Forms.Button();
			this.lbBtnWidth = new System.Windows.Forms.Label();
			this.tbBtnWidth = new System.Windows.Forms.TextBox();
			this.lbListBoxW = new System.Windows.Forms.Label();
			this.tbListBoxW = new System.Windows.Forms.TextBox();
			this.lbListBoxH = new System.Windows.Forms.Label();
			this.tbListBoxH = new System.Windows.Forms.TextBox();
			this.lbBorder = new System.Windows.Forms.Label();
			this.tbBorder = new System.Windows.Forms.TextBox();
			this.cbOnSelChange = new System.Windows.Forms.CheckBox();
			this.cbbDataType = new System.Windows.Forms.ComboBox();
			this.tbLineHeight = new System.Windows.Forms.TextBox();
			this.lbLineHeight = new System.Windows.Forms.Label();
			this.lbDataType = new System.Windows.Forms.Label();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.bnBkImage1 = new System.Windows.Forms.Button();
			this.bnBkImage3 = new System.Windows.Forms.Button();
			this.lbBkImage1 = new System.Windows.Forms.Label();
			this.lbBkImage3 = new System.Windows.Forms.Label();
			this.gbScrollV.SuspendLayout();
			this.gbScrollH.SuspendLayout();
			this.SuspendLayout();
			// 
			// lbName
			// 
			this.lbName.AutoSize = true;
			this.lbName.Location = new System.Drawing.Point(12, 44);
			this.lbName.Name = "lbName";
			this.lbName.Size = new System.Drawing.Size(35, 14);
			this.lbName.TabIndex = 2;
			this.lbName.Text = "名称";
			// 
			// tbName
			// 
			this.tbName.Location = new System.Drawing.Point(53, 41);
			this.tbName.Name = "tbName";
			this.tbName.Size = new System.Drawing.Size(145, 23);
			this.tbName.TabIndex = 3;
			this.tbName.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbType
			// 
			this.lbType.AutoSize = true;
			this.lbType.Location = new System.Drawing.Point(12, 15);
			this.lbType.Name = "lbType";
			this.lbType.Size = new System.Drawing.Size(35, 14);
			this.lbType.TabIndex = 0;
			this.lbType.Text = "类型";
			// 
			// tbType
			// 
			this.tbType.Location = new System.Drawing.Point(53, 12);
			this.tbType.Name = "tbType";
			this.tbType.ReadOnly = true;
			this.tbType.Size = new System.Drawing.Size(145, 23);
			this.tbType.TabIndex = 1;
			this.tbType.TabStop = false;
			this.tbType.Text = "组合框(ComboBox)";
			// 
			// lbX
			// 
			this.lbX.AutoSize = true;
			this.lbX.Location = new System.Drawing.Point(33, 102);
			this.lbX.Name = "lbX";
			this.lbX.Size = new System.Drawing.Size(14, 14);
			this.lbX.TabIndex = 7;
			this.lbX.Text = "X";
			// 
			// tbX
			// 
			this.tbX.Location = new System.Drawing.Point(53, 99);
			this.tbX.Name = "tbX";
			this.tbX.Size = new System.Drawing.Size(55, 23);
			this.tbX.TabIndex = 8;
			this.tbX.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbW
			// 
			this.lbW.AutoSize = true;
			this.lbW.Location = new System.Drawing.Point(116, 102);
			this.lbW.Name = "lbW";
			this.lbW.Size = new System.Drawing.Size(21, 14);
			this.lbW.TabIndex = 11;
			this.lbW.Text = "宽";
			// 
			// tbW
			// 
			this.tbW.Location = new System.Drawing.Point(143, 99);
			this.tbW.Name = "tbW";
			this.tbW.Size = new System.Drawing.Size(55, 23);
			this.tbW.TabIndex = 12;
			this.tbW.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbY
			// 
			this.lbY.AutoSize = true;
			this.lbY.Location = new System.Drawing.Point(33, 131);
			this.lbY.Name = "lbY";
			this.lbY.Size = new System.Drawing.Size(14, 14);
			this.lbY.TabIndex = 9;
			this.lbY.Text = "Y";
			// 
			// lbH
			// 
			this.lbH.AutoSize = true;
			this.lbH.Location = new System.Drawing.Point(116, 131);
			this.lbH.Name = "lbH";
			this.lbH.Size = new System.Drawing.Size(21, 14);
			this.lbH.TabIndex = 13;
			this.lbH.Text = "高";
			// 
			// tbY
			// 
			this.tbY.Location = new System.Drawing.Point(53, 128);
			this.tbY.Name = "tbY";
			this.tbY.Size = new System.Drawing.Size(55, 23);
			this.tbY.TabIndex = 10;
			this.tbY.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// tbH
			// 
			this.tbH.Location = new System.Drawing.Point(143, 128);
			this.tbH.Name = "tbH";
			this.tbH.Size = new System.Drawing.Size(55, 23);
			this.tbH.TabIndex = 14;
			this.tbH.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbBkColor1
			// 
			this.lbBkColor1.AutoSize = true;
			this.lbBkColor1.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor1.Name = "lbBkColor1";
			this.lbBkColor1.Size = new System.Drawing.Size(77, 14);
			this.lbBkColor1.TabIndex = 15;
			this.lbBkColor1.Text = "焦点背景色";
			// 
			// bnBkColor1
			// 
			this.bnBkColor1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor1.Location = new System.Drawing.Point(94, 157);
			this.bnBkColor1.Name = "bnBkColor1";
			this.bnBkColor1.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor1.TabIndex = 16;
			this.bnBkColor1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor1.UseVisualStyleBackColor = true;
			this.bnBkColor1.Click += new System.EventHandler(this.bnBkColor1_Click);
			// 
			// lbBkColor3
			// 
			this.lbBkColor3.AutoSize = true;
			this.lbBkColor3.Location = new System.Drawing.Point(12, 217);
			this.lbBkColor3.Name = "lbBkColor3";
			this.lbBkColor3.Size = new System.Drawing.Size(77, 14);
			this.lbBkColor3.TabIndex = 17;
			this.lbBkColor3.Text = "选中背景色";
			// 
			// bnBkColor3
			// 
			this.bnBkColor3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor3.Location = new System.Drawing.Point(94, 213);
			this.bnBkColor3.Name = "bnBkColor3";
			this.bnBkColor3.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor3.TabIndex = 18;
			this.bnBkColor3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor3.UseVisualStyleBackColor = true;
			this.bnBkColor3.Click += new System.EventHandler(this.bnBkColor3_Click);
			// 
			// lbClass
			// 
			this.lbClass.AutoSize = true;
			this.lbClass.Location = new System.Drawing.Point(12, 73);
			this.lbClass.Name = "lbClass";
			this.lbClass.Size = new System.Drawing.Size(35, 14);
			this.lbClass.TabIndex = 5;
			this.lbClass.Text = "类名";
			// 
			// tbClass
			// 
			this.tbClass.Location = new System.Drawing.Point(53, 70);
			this.tbClass.Name = "tbClass";
			this.tbClass.Size = new System.Drawing.Size(145, 23);
			this.tbClass.TabIndex = 6;
			this.tbClass.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbFont
			// 
			this.lbFont.AutoSize = true;
			this.lbFont.Location = new System.Drawing.Point(12, 273);
			this.lbFont.Name = "lbFont";
			this.lbFont.Size = new System.Drawing.Size(63, 14);
			this.lbFont.TabIndex = 25;
			this.lbFont.Text = "字体样式";
			// 
			// cbbFont
			// 
			this.cbbFont.DropDownHeight = 240;
			this.cbbFont.DropDownWidth = 155;
			this.cbbFont.IntegralHeight = false;
			this.cbbFont.Location = new System.Drawing.Point(81, 269);
			this.cbbFont.Name = "cbbFont";
			this.cbbFont.Size = new System.Drawing.Size(117, 22);
			this.cbbFont.Sorted = true;
			this.cbbFont.TabIndex = 26;
			this.cbbFont.Leave += new System.EventHandler(this.tb_Leave);
			this.cbbFont.DropDown += new System.EventHandler(this.cbbFont_DropDown);
			// 
			// cbbScrollV
			// 
			this.cbbScrollV.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollV.FormattingEnabled = true;
			this.cbbScrollV.Location = new System.Drawing.Point(9, 22);
			this.cbbScrollV.Name = "cbbScrollV";
			this.cbbScrollV.Size = new System.Drawing.Size(181, 22);
			this.cbbScrollV.Sorted = true;
			this.cbbScrollV.TabIndex = 0;
			// 
			// gbScrollV
			// 
			this.gbScrollV.Controls.Add(this.cbbScrollV);
			this.gbScrollV.Location = new System.Drawing.Point(6, 639);
			this.gbScrollV.Name = "gbScrollV";
			this.gbScrollV.Size = new System.Drawing.Size(197, 61);
			this.gbScrollV.TabIndex = 51;
			this.gbScrollV.TabStop = false;
			this.gbScrollV.Text = "垂直滚动条";
			// 
			// gbScrollH
			// 
			this.gbScrollH.Controls.Add(this.cbbScrollH);
			this.gbScrollH.Location = new System.Drawing.Point(6, 706);
			this.gbScrollH.Name = "gbScrollH";
			this.gbScrollH.Size = new System.Drawing.Size(197, 51);
			this.gbScrollH.TabIndex = 52;
			this.gbScrollH.TabStop = false;
			this.gbScrollH.Text = "水平滚动条";
			// 
			// cbbScrollH
			// 
			this.cbbScrollH.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScrollH.FormattingEnabled = true;
			this.cbbScrollH.Location = new System.Drawing.Point(9, 22);
			this.cbbScrollH.Name = "cbbScrollH";
			this.cbbScrollH.Size = new System.Drawing.Size(181, 22);
			this.cbbScrollH.Sorted = true;
			this.cbbScrollH.TabIndex = 0;
			// 
			// lbImageBox
			// 
			this.lbImageBox.AutoSize = true;
			this.lbImageBox.Location = new System.Drawing.Point(12, 302);
			this.lbImageBox.Name = "lbImageBox";
			this.lbImageBox.Size = new System.Drawing.Size(77, 14);
			this.lbImageBox.TabIndex = 27;
			this.lbImageBox.Text = "文本框背景";
			// 
			// bnImageBox
			// 
			this.bnImageBox.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageBox.Location = new System.Drawing.Point(94, 298);
			this.bnImageBox.Name = "bnImageBox";
			this.bnImageBox.Size = new System.Drawing.Size(104, 22);
			this.bnImageBox.TabIndex = 28;
			this.bnImageBox.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImageBox.UseVisualStyleBackColor = true;
			this.bnImageBox.Click += new System.EventHandler(this.bnImageBox_Click);
			// 
			// lbImageList
			// 
			this.lbImageList.AutoSize = true;
			this.lbImageList.Location = new System.Drawing.Point(12, 330);
			this.lbImageList.Name = "lbImageList";
			this.lbImageList.Size = new System.Drawing.Size(77, 14);
			this.lbImageList.TabIndex = 29;
			this.lbImageList.Text = "列表框背景";
			// 
			// bnImageList
			// 
			this.bnImageList.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageList.Location = new System.Drawing.Point(94, 326);
			this.bnImageList.Name = "bnImageList";
			this.bnImageList.Size = new System.Drawing.Size(104, 22);
			this.bnImageList.TabIndex = 30;
			this.bnImageList.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImageList.UseVisualStyleBackColor = true;
			this.bnImageList.Click += new System.EventHandler(this.bnImageList_Click);
			// 
			// lbImage0
			// 
			this.lbImage0.AutoSize = true;
			this.lbImage0.Location = new System.Drawing.Point(12, 358);
			this.lbImage0.Name = "lbImage0";
			this.lbImage0.Size = new System.Drawing.Size(77, 14);
			this.lbImage0.TabIndex = 31;
			this.lbImage0.Text = "按钮无效态";
			// 
			// bnImage0
			// 
			this.bnImage0.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage0.Location = new System.Drawing.Point(94, 354);
			this.bnImage0.Name = "bnImage0";
			this.bnImage0.Size = new System.Drawing.Size(104, 22);
			this.bnImage0.TabIndex = 32;
			this.bnImage0.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage0.UseVisualStyleBackColor = true;
			this.bnImage0.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage0.Click += new System.EventHandler(this.bnImage0_Click);
			this.bnImage0.MouseEnter += new System.EventHandler(this.bnImage0_MouseEnter);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 386);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(77, 14);
			this.lbImage1.TabIndex = 33;
			this.lbImage1.Text = "按钮正常态";
			// 
			// lbImage2
			// 
			this.lbImage2.AutoSize = true;
			this.lbImage2.Location = new System.Drawing.Point(12, 414);
			this.lbImage2.Name = "lbImage2";
			this.lbImage2.Size = new System.Drawing.Size(77, 14);
			this.lbImage2.TabIndex = 35;
			this.lbImage2.Text = "按钮焦点态";
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(94, 382);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(104, 22);
			this.bnImage1.TabIndex = 34;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
			this.bnImage1.MouseEnter += new System.EventHandler(this.bnImage1_MouseEnter);
			// 
			// bnImage2
			// 
			this.bnImage2.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage2.Location = new System.Drawing.Point(94, 410);
			this.bnImage2.Name = "bnImage2";
			this.bnImage2.Size = new System.Drawing.Size(104, 22);
			this.bnImage2.TabIndex = 36;
			this.bnImage2.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage2.UseVisualStyleBackColor = true;
			this.bnImage2.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage2.Click += new System.EventHandler(this.bnImage2_Click);
			this.bnImage2.MouseEnter += new System.EventHandler(this.bnImage2_MouseEnter);
			// 
			// lbImage3
			// 
			this.lbImage3.AutoSize = true;
			this.lbImage3.Location = new System.Drawing.Point(12, 442);
			this.lbImage3.Name = "lbImage3";
			this.lbImage3.Size = new System.Drawing.Size(77, 14);
			this.lbImage3.TabIndex = 37;
			this.lbImage3.Text = "按钮按下态";
			// 
			// bnImage3
			// 
			this.bnImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage3.Location = new System.Drawing.Point(94, 438);
			this.bnImage3.Name = "bnImage3";
			this.bnImage3.Size = new System.Drawing.Size(104, 22);
			this.bnImage3.TabIndex = 38;
			this.bnImage3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage3.UseVisualStyleBackColor = true;
			this.bnImage3.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage3.Click += new System.EventHandler(this.bnImage3_Click);
			this.bnImage3.MouseEnter += new System.EventHandler(this.bnImage3_MouseEnter);
			// 
			// lbBtnWidth
			// 
			this.lbBtnWidth.AutoSize = true;
			this.lbBtnWidth.Location = new System.Drawing.Point(12, 470);
			this.lbBtnWidth.Name = "lbBtnWidth";
			this.lbBtnWidth.Size = new System.Drawing.Size(63, 14);
			this.lbBtnWidth.TabIndex = 39;
			this.lbBtnWidth.Text = "按钮宽度";
			// 
			// tbBtnWidth
			// 
			this.tbBtnWidth.Location = new System.Drawing.Point(94, 466);
			this.tbBtnWidth.Name = "tbBtnWidth";
			this.tbBtnWidth.Size = new System.Drawing.Size(104, 23);
			this.tbBtnWidth.TabIndex = 40;
			this.tbBtnWidth.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbListBoxW
			// 
			this.lbListBoxW.AutoSize = true;
			this.lbListBoxW.Location = new System.Drawing.Point(12, 499);
			this.lbListBoxW.Name = "lbListBoxW";
			this.lbListBoxW.Size = new System.Drawing.Size(77, 14);
			this.lbListBoxW.TabIndex = 41;
			this.lbListBoxW.Text = "列表框宽度";
			// 
			// tbListBoxW
			// 
			this.tbListBoxW.Location = new System.Drawing.Point(94, 495);
			this.tbListBoxW.Name = "tbListBoxW";
			this.tbListBoxW.Size = new System.Drawing.Size(104, 23);
			this.tbListBoxW.TabIndex = 42;
			this.tbListBoxW.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbListBoxH
			// 
			this.lbListBoxH.AutoSize = true;
			this.lbListBoxH.Location = new System.Drawing.Point(12, 528);
			this.lbListBoxH.Name = "lbListBoxH";
			this.lbListBoxH.Size = new System.Drawing.Size(77, 14);
			this.lbListBoxH.TabIndex = 43;
			this.lbListBoxH.Text = "列表框高度";
			// 
			// tbListBoxH
			// 
			this.tbListBoxH.Location = new System.Drawing.Point(94, 524);
			this.tbListBoxH.Name = "tbListBoxH";
			this.tbListBoxH.Size = new System.Drawing.Size(104, 23);
			this.tbListBoxH.TabIndex = 44;
			this.tbListBoxH.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbBorder
			// 
			this.lbBorder.AutoSize = true;
			this.lbBorder.Location = new System.Drawing.Point(12, 557);
			this.lbBorder.Name = "lbBorder";
			this.lbBorder.Size = new System.Drawing.Size(35, 14);
			this.lbBorder.TabIndex = 45;
			this.lbBorder.Text = "边距";
			// 
			// tbBorder
			// 
			this.tbBorder.Location = new System.Drawing.Point(94, 553);
			this.tbBorder.Name = "tbBorder";
			this.tbBorder.Size = new System.Drawing.Size(104, 23);
			this.tbBorder.TabIndex = 46;
			this.tbBorder.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbOnSelChange
			// 
			this.cbOnSelChange.AutoSize = true;
			this.cbOnSelChange.Location = new System.Drawing.Point(15, 763);
			this.cbOnSelChange.Name = "cbOnSelChange";
			this.cbOnSelChange.Size = new System.Drawing.Size(110, 18);
			this.cbOnSelChange.TabIndex = 53;
			this.cbOnSelChange.Text = "响应选择事件";
			this.cbOnSelChange.UseVisualStyleBackColor = true;
			// 
			// cbbDataType
			// 
			this.cbbDataType.FormattingEnabled = true;
			this.cbbDataType.Items.AddRange(new object[] {
            "text",
            "image"});
			this.cbbDataType.Location = new System.Drawing.Point(94, 611);
			this.cbbDataType.Name = "cbbDataType";
			this.cbbDataType.Size = new System.Drawing.Size(104, 22);
			this.cbbDataType.TabIndex = 50;
			// 
			// tbLineHeight
			// 
			this.tbLineHeight.Location = new System.Drawing.Point(94, 582);
			this.tbLineHeight.Name = "tbLineHeight";
			this.tbLineHeight.Size = new System.Drawing.Size(104, 23);
			this.tbLineHeight.TabIndex = 48;
			this.tbLineHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbLineHeight
			// 
			this.lbLineHeight.AutoSize = true;
			this.lbLineHeight.Location = new System.Drawing.Point(12, 586);
			this.lbLineHeight.Name = "lbLineHeight";
			this.lbLineHeight.Size = new System.Drawing.Size(35, 14);
			this.lbLineHeight.TabIndex = 47;
			this.lbLineHeight.Text = "行高";
			// 
			// lbDataType
			// 
			this.lbDataType.AutoSize = true;
			this.lbDataType.Location = new System.Drawing.Point(12, 615);
			this.lbDataType.Name = "lbDataType";
			this.lbDataType.Size = new System.Drawing.Size(63, 14);
			this.lbDataType.TabIndex = 49;
			this.lbDataType.Text = "数据类型";
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 787);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 56;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 805);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 55;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 809);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 54;
			this.lbTipInfo.Text = "提示信息";
			// 
			// bnBkImage1
			// 
			this.bnBkImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage1.Location = new System.Drawing.Point(94, 185);
			this.bnBkImage1.Name = "bnBkImage1";
			this.bnBkImage1.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage1.TabIndex = 60;
			this.bnBkImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkImage1.UseVisualStyleBackColor = true;
			this.bnBkImage1.Click += new System.EventHandler(this.bnBkImage1_Click);
			// 
			// bnBkImage3
			// 
			this.bnBkImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage3.Location = new System.Drawing.Point(94, 241);
			this.bnBkImage3.Name = "bnBkImage3";
			this.bnBkImage3.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage3.TabIndex = 58;
			this.bnBkImage3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkImage3.UseVisualStyleBackColor = true;
			this.bnBkImage3.Click += new System.EventHandler(this.bnBkImage3_Click);
			// 
			// lbBkImage1
			// 
			this.lbBkImage1.AutoSize = true;
			this.lbBkImage1.Location = new System.Drawing.Point(12, 189);
			this.lbBkImage1.Name = "lbBkImage1";
			this.lbBkImage1.Size = new System.Drawing.Size(77, 14);
			this.lbBkImage1.TabIndex = 59;
			this.lbBkImage1.Text = "焦点背景图";
			// 
			// lbBkImage3
			// 
			this.lbBkImage3.AutoSize = true;
			this.lbBkImage3.Location = new System.Drawing.Point(12, 245);
			this.lbBkImage3.Name = "lbBkImage3";
			this.lbBkImage3.Size = new System.Drawing.Size(77, 14);
			this.lbBkImage3.TabIndex = 57;
			this.lbBkImage3.Text = "选中背景图";
			// 
			// CFormComboBox
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 836);
			this.Controls.Add(this.bnBkImage1);
			this.Controls.Add(this.bnBkImage3);
			this.Controls.Add(this.lbBkImage1);
			this.Controls.Add(this.lbBkImage3);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbbDataType);
			this.Controls.Add(this.tbLineHeight);
			this.Controls.Add(this.lbLineHeight);
			this.Controls.Add(this.lbDataType);
			this.Controls.Add(this.cbOnSelChange);
			this.Controls.Add(this.gbScrollH);
			this.Controls.Add(this.gbScrollV);
			this.Controls.Add(this.bnBkColor3);
			this.Controls.Add(this.bnImage3);
			this.Controls.Add(this.bnImage2);
			this.Controls.Add(this.bnImage0);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnImageList);
			this.Controls.Add(this.bnImageBox);
			this.Controls.Add(this.bnBkColor1);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbBorder);
			this.Controls.Add(this.tbListBoxH);
			this.Controls.Add(this.tbListBoxW);
			this.Controls.Add(this.tbBtnWidth);
			this.Controls.Add(this.cbbFont);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbBorder);
			this.Controls.Add(this.lbListBoxH);
			this.Controls.Add(this.lbListBoxW);
			this.Controls.Add(this.lbBtnWidth);
			this.Controls.Add(this.lbFont);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImage3);
			this.Controls.Add(this.lbImage2);
			this.Controls.Add(this.lbImage0);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbImageList);
			this.Controls.Add(this.lbBkColor3);
			this.Controls.Add(this.lbImageBox);
			this.Controls.Add(this.lbBkColor1);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormComboBox";
			this.gbScrollV.ResumeLayout(false);
			this.gbScrollH.ResumeLayout(false);
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label lbName;
		private System.Windows.Forms.TextBox tbName;
		private System.Windows.Forms.Label lbType;
		private System.Windows.Forms.TextBox tbType;
		private System.Windows.Forms.Label lbX;
		private System.Windows.Forms.TextBox tbX;
		private System.Windows.Forms.Label lbW;
		private System.Windows.Forms.TextBox tbW;
		private System.Windows.Forms.Label lbY;
		private System.Windows.Forms.Label lbH;
		private System.Windows.Forms.TextBox tbY;
		private System.Windows.Forms.TextBox tbH;
		private System.Windows.Forms.Label lbBkColor1;
		private System.Windows.Forms.Button bnBkColor1;
		private System.Windows.Forms.Label lbBkColor3;
		private System.Windows.Forms.Button bnBkColor3;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbFont;
		private System.Windows.Forms.ComboBox cbbFont;
		private System.Windows.Forms.ComboBox cbbScrollV;
		private System.Windows.Forms.GroupBox gbScrollV;
		private System.Windows.Forms.GroupBox gbScrollH;
		private System.Windows.Forms.ComboBox cbbScrollH;
		private System.Windows.Forms.Label lbImageBox;
		private System.Windows.Forms.Button bnImageBox;
		private System.Windows.Forms.Label lbImageList;
		private System.Windows.Forms.Button bnImageList;
		private System.Windows.Forms.Label lbImage0;
		private System.Windows.Forms.Button bnImage0;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Label lbImage2;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Button bnImage2;
		private System.Windows.Forms.Label lbImage3;
		private System.Windows.Forms.Button bnImage3;
		private System.Windows.Forms.Label lbBtnWidth;
		private System.Windows.Forms.TextBox tbBtnWidth;
		private System.Windows.Forms.Label lbListBoxW;
		private System.Windows.Forms.TextBox tbListBoxW;
		private System.Windows.Forms.Label lbListBoxH;
		private System.Windows.Forms.TextBox tbListBoxH;
		private System.Windows.Forms.Label lbBorder;
		private System.Windows.Forms.TextBox tbBorder;
		private System.Windows.Forms.CheckBox cbOnSelChange;
		private System.Windows.Forms.ComboBox cbbDataType;
		private System.Windows.Forms.TextBox tbLineHeight;
		private System.Windows.Forms.Label lbLineHeight;
		private System.Windows.Forms.Label lbDataType;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
		private System.Windows.Forms.Button bnBkImage1;
		private System.Windows.Forms.Button bnBkImage3;
		private System.Windows.Forms.Label lbBkImage1;
		private System.Windows.Forms.Label lbBkImage3;
	}
}
