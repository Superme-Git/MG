namespace GUIEditor2
{
	partial class CFormListBox
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
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImageLine = new System.Windows.Forms.Button();
			this.lbLineHeight = new System.Windows.Forms.Label();
			this.tbLineHeight = new System.Windows.Forms.TextBox();
			this.cbMultiSelect = new System.Windows.Forms.CheckBox();
			this.cbbDataType = new System.Windows.Forms.ComboBox();
			this.lbDataType = new System.Windows.Forms.Label();
			this.cbOnSelChange = new System.Windows.Forms.CheckBox();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.lbBkImage3 = new System.Windows.Forms.Label();
			this.bnBkImage3 = new System.Windows.Forms.Button();
			this.lbBkImage1 = new System.Windows.Forms.Label();
			this.bnBkImage1 = new System.Windows.Forms.Button();
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
			this.tbType.Text = "列表框(ListBox)";
			// 
			// lbX
			// 
			this.lbX.AutoSize = true;
			this.lbX.Location = new System.Drawing.Point(33, 102);
			this.lbX.Name = "lbX";
			this.lbX.Size = new System.Drawing.Size(14, 14);
			this.lbX.TabIndex = 6;
			this.lbX.Text = "X";
			// 
			// tbX
			// 
			this.tbX.Location = new System.Drawing.Point(53, 99);
			this.tbX.Name = "tbX";
			this.tbX.Size = new System.Drawing.Size(55, 23);
			this.tbX.TabIndex = 7;
			this.tbX.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbW
			// 
			this.lbW.AutoSize = true;
			this.lbW.Location = new System.Drawing.Point(116, 102);
			this.lbW.Name = "lbW";
			this.lbW.Size = new System.Drawing.Size(21, 14);
			this.lbW.TabIndex = 10;
			this.lbW.Text = "宽";
			// 
			// tbW
			// 
			this.tbW.Location = new System.Drawing.Point(143, 99);
			this.tbW.Name = "tbW";
			this.tbW.Size = new System.Drawing.Size(55, 23);
			this.tbW.TabIndex = 11;
			this.tbW.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbY
			// 
			this.lbY.AutoSize = true;
			this.lbY.Location = new System.Drawing.Point(33, 131);
			this.lbY.Name = "lbY";
			this.lbY.Size = new System.Drawing.Size(14, 14);
			this.lbY.TabIndex = 8;
			this.lbY.Text = "Y";
			// 
			// lbH
			// 
			this.lbH.AutoSize = true;
			this.lbH.Location = new System.Drawing.Point(116, 131);
			this.lbH.Name = "lbH";
			this.lbH.Size = new System.Drawing.Size(21, 14);
			this.lbH.TabIndex = 12;
			this.lbH.Text = "高";
			// 
			// tbY
			// 
			this.tbY.Location = new System.Drawing.Point(53, 128);
			this.tbY.Name = "tbY";
			this.tbY.Size = new System.Drawing.Size(55, 23);
			this.tbY.TabIndex = 9;
			this.tbY.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// tbH
			// 
			this.tbH.Location = new System.Drawing.Point(143, 128);
			this.tbH.Name = "tbH";
			this.tbH.Size = new System.Drawing.Size(55, 23);
			this.tbH.TabIndex = 13;
			this.tbH.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbBkColor1
			// 
			this.lbBkColor1.AutoSize = true;
			this.lbBkColor1.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor1.Name = "lbBkColor1";
			this.lbBkColor1.Size = new System.Drawing.Size(77, 14);
			this.lbBkColor1.TabIndex = 16;
			this.lbBkColor1.Text = "焦点背景色";
			// 
			// bnBkColor1
			// 
			this.bnBkColor1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor1.Location = new System.Drawing.Point(94, 157);
			this.bnBkColor1.Name = "bnBkColor1";
			this.bnBkColor1.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor1.TabIndex = 17;
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
			this.lbBkColor3.TabIndex = 18;
			this.lbBkColor3.Text = "选中背景色";
			// 
			// bnBkColor3
			// 
			this.bnBkColor3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor3.Location = new System.Drawing.Point(94, 213);
			this.bnBkColor3.Name = "bnBkColor3";
			this.bnBkColor3.Size = new System.Drawing.Size(104, 22);
			this.bnBkColor3.TabIndex = 19;
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
			this.lbClass.TabIndex = 4;
			this.lbClass.Text = "类名";
			// 
			// tbClass
			// 
			this.tbClass.Location = new System.Drawing.Point(53, 70);
			this.tbClass.Name = "tbClass";
			this.tbClass.Size = new System.Drawing.Size(145, 23);
			this.tbClass.TabIndex = 5;
			this.tbClass.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbFont
			// 
			this.lbFont.AutoSize = true;
			this.lbFont.Location = new System.Drawing.Point(12, 301);
			this.lbFont.Name = "lbFont";
			this.lbFont.Size = new System.Drawing.Size(63, 14);
			this.lbFont.TabIndex = 26;
			this.lbFont.Text = "字体样式";
			// 
			// cbbFont
			// 
			this.cbbFont.DropDownHeight = 240;
			this.cbbFont.DropDownWidth = 155;
			this.cbbFont.IntegralHeight = false;
			this.cbbFont.Location = new System.Drawing.Point(81, 297);
			this.cbbFont.Name = "cbbFont";
			this.cbbFont.Size = new System.Drawing.Size(117, 22);
			this.cbbFont.Sorted = true;
			this.cbbFont.TabIndex = 27;
			this.cbbFont.Leave += new System.EventHandler(this.tb_Leave);
			this.cbbFont.DropDown += new System.EventHandler(this.cbbFont_DropDown);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 273);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(63, 14);
			this.lbImage1.TabIndex = 28;
			this.lbImage1.Text = "行背景图";
			// 
			// bnImageLine
			// 
			this.bnImageLine.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageLine.Location = new System.Drawing.Point(94, 269);
			this.bnImageLine.Name = "bnImageLine";
			this.bnImageLine.Size = new System.Drawing.Size(104, 22);
			this.bnImageLine.TabIndex = 29;
			this.bnImageLine.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImageLine.UseVisualStyleBackColor = true;
			this.bnImageLine.Click += new System.EventHandler(this.bnImageLine_Click);
			// 
			// lbLineHeight
			// 
			this.lbLineHeight.AutoSize = true;
			this.lbLineHeight.Location = new System.Drawing.Point(12, 330);
			this.lbLineHeight.Name = "lbLineHeight";
			this.lbLineHeight.Size = new System.Drawing.Size(35, 14);
			this.lbLineHeight.TabIndex = 30;
			this.lbLineHeight.Text = "行高";
			// 
			// tbLineHeight
			// 
			this.tbLineHeight.Location = new System.Drawing.Point(81, 326);
			this.tbLineHeight.Name = "tbLineHeight";
			this.tbLineHeight.Size = new System.Drawing.Size(117, 23);
			this.tbLineHeight.TabIndex = 31;
			this.tbLineHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbMultiSelect
			// 
			this.cbMultiSelect.AutoSize = true;
			this.cbMultiSelect.Location = new System.Drawing.Point(15, 383);
			this.cbMultiSelect.Name = "cbMultiSelect";
			this.cbMultiSelect.Size = new System.Drawing.Size(82, 18);
			this.cbMultiSelect.TabIndex = 33;
			this.cbMultiSelect.Text = "可以多选";
			this.cbMultiSelect.UseVisualStyleBackColor = true;
			// 
			// cbbDataType
			// 
			this.cbbDataType.FormattingEnabled = true;
			this.cbbDataType.Items.AddRange(new object[] {
            "text",
            "image"});
			this.cbbDataType.Location = new System.Drawing.Point(81, 355);
			this.cbbDataType.Name = "cbbDataType";
			this.cbbDataType.Size = new System.Drawing.Size(117, 22);
			this.cbbDataType.TabIndex = 33;
			// 
			// lbDataType
			// 
			this.lbDataType.AutoSize = true;
			this.lbDataType.Location = new System.Drawing.Point(12, 359);
			this.lbDataType.Name = "lbDataType";
			this.lbDataType.Size = new System.Drawing.Size(63, 14);
			this.lbDataType.TabIndex = 32;
			this.lbDataType.Text = "数据类型";
			// 
			// cbOnSelChange
			// 
			this.cbOnSelChange.AutoSize = true;
			this.cbOnSelChange.Location = new System.Drawing.Point(15, 407);
			this.cbOnSelChange.Name = "cbOnSelChange";
			this.cbOnSelChange.Size = new System.Drawing.Size(110, 18);
			this.cbOnSelChange.TabIndex = 34;
			this.cbOnSelChange.Text = "响应选择事件";
			this.cbOnSelChange.UseVisualStyleBackColor = true;
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 431);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 44;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 451);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 43;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 455);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 42;
			this.lbTipInfo.Text = "提示信息";
			// 
			// lbBkImage3
			// 
			this.lbBkImage3.AutoSize = true;
			this.lbBkImage3.Location = new System.Drawing.Point(12, 245);
			this.lbBkImage3.Name = "lbBkImage3";
			this.lbBkImage3.Size = new System.Drawing.Size(77, 14);
			this.lbBkImage3.TabIndex = 28;
			this.lbBkImage3.Text = "选中背景图";
			// 
			// bnBkImage3
			// 
			this.bnBkImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage3.Location = new System.Drawing.Point(94, 241);
			this.bnBkImage3.Name = "bnBkImage3";
			this.bnBkImage3.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage3.TabIndex = 29;
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
			this.lbBkImage1.TabIndex = 28;
			this.lbBkImage1.Text = "焦点背景图";
			// 
			// bnBkImage1
			// 
			this.bnBkImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkImage1.Location = new System.Drawing.Point(94, 185);
			this.bnBkImage1.Name = "bnBkImage1";
			this.bnBkImage1.Size = new System.Drawing.Size(104, 22);
			this.bnBkImage1.TabIndex = 29;
			this.bnBkImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkImage1.UseVisualStyleBackColor = true;
			this.bnBkImage1.Click += new System.EventHandler(this.bnBkImage1_Click);
			// 
			// CFormListBox
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 480);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbbDataType);
			this.Controls.Add(this.cbOnSelChange);
			this.Controls.Add(this.cbMultiSelect);
			this.Controls.Add(this.bnBkColor3);
			this.Controls.Add(this.bnBkImage1);
			this.Controls.Add(this.bnBkImage3);
			this.Controls.Add(this.bnImageLine);
			this.Controls.Add(this.bnBkColor1);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbLineHeight);
			this.Controls.Add(this.cbbFont);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbLineHeight);
			this.Controls.Add(this.lbFont);
			this.Controls.Add(this.lbBkImage1);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbBkImage3);
			this.Controls.Add(this.lbBkColor3);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbDataType);
			this.Controls.Add(this.lbBkColor1);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormListBox";
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
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImageLine;
		private System.Windows.Forms.Label lbLineHeight;
		private System.Windows.Forms.TextBox tbLineHeight;
		private System.Windows.Forms.CheckBox cbMultiSelect;
		private System.Windows.Forms.ComboBox cbbDataType;
		private System.Windows.Forms.Label lbDataType;
		private System.Windows.Forms.CheckBox cbOnSelChange;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
		private System.Windows.Forms.Label lbBkImage3;
		private System.Windows.Forms.Button bnBkImage3;
		private System.Windows.Forms.Label lbBkImage1;
		private System.Windows.Forms.Button bnBkImage1;
	}
}
