namespace GUIEditor2
{
	partial class CFormEdit
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
			this.lbBkColor = new System.Windows.Forms.Label();
			this.bnBkColor = new System.Windows.Forms.Button();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbFont = new System.Windows.Forms.Label();
			this.cbbFont = new System.Windows.Forms.ComboBox();
			this.cbPassword = new System.Windows.Forms.CheckBox();
			this.cbSelect = new System.Windows.Forms.CheckBox();
			this.tbText = new System.Windows.Forms.TextBox();
			this.gbText = new System.Windows.Forms.GroupBox();
			this.cbNumber = new System.Windows.Forms.CheckBox();
			this.lbLimit = new System.Windows.Forms.Label();
			this.tbLimit = new System.Windows.Forms.TextBox();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
			this.gbScroll = new System.Windows.Forms.GroupBox();
			this.cbbScroll = new System.Windows.Forms.ComboBox();
			this.rbMultiLine = new System.Windows.Forms.RadioButton();
			this.rbSingleLine = new System.Windows.Forms.RadioButton();
			this.rbRawArea = new System.Windows.Forms.RadioButton();
			this.gbText.SuspendLayout();
			this.gbScroll.SuspendLayout();
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
			this.tbType.Text = "编辑框(Edit)";
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
			// lbBkColor
			// 
			this.lbBkColor.AutoSize = true;
			this.lbBkColor.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor.Name = "lbBkColor";
			this.lbBkColor.Size = new System.Drawing.Size(49, 14);
			this.lbBkColor.TabIndex = 14;
			this.lbBkColor.Text = "背景色";
			// 
			// bnBkColor
			// 
			this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor.Location = new System.Drawing.Point(67, 157);
			this.bnBkColor.Name = "bnBkColor";
			this.bnBkColor.Size = new System.Drawing.Size(131, 22);
			this.bnBkColor.TabIndex = 15;
			this.bnBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor.UseVisualStyleBackColor = true;
			this.bnBkColor.Click += new System.EventHandler(this.bnBkColor_Click);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 188);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(49, 14);
			this.lbImage1.TabIndex = 16;
			this.lbImage1.Text = "背景图";
			// 
			// bnImage1
			// 
			this.bnImage1.Enabled = false;
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(67, 185);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(131, 22);
			this.bnImage1.TabIndex = 17;
			this.bnImage1.Text = "(不可用)";
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
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
			this.lbFont.Location = new System.Drawing.Point(12, 216);
			this.lbFont.Name = "lbFont";
			this.lbFont.Size = new System.Drawing.Size(63, 14);
			this.lbFont.TabIndex = 18;
			this.lbFont.Text = "字体样式";
			// 
			// cbbFont
			// 
			this.cbbFont.DropDownHeight = 240;
			this.cbbFont.DropDownWidth = 155;
			this.cbbFont.IntegralHeight = false;
			this.cbbFont.Location = new System.Drawing.Point(81, 213);
			this.cbbFont.Name = "cbbFont";
			this.cbbFont.Size = new System.Drawing.Size(117, 22);
			this.cbbFont.Sorted = true;
			this.cbbFont.TabIndex = 19;
			this.cbbFont.SelectedIndexChanged += new System.EventHandler(this.cbbFont_SelectedIndexChanged);
			this.cbbFont.Leave += new System.EventHandler(this.tb_Leave);
			this.cbbFont.DropDown += new System.EventHandler(this.cbbFont_DropDown);
			// 
			// cbPassword
			// 
			this.cbPassword.AutoSize = true;
			this.cbPassword.Location = new System.Drawing.Point(123, 0);
			this.cbPassword.Name = "cbPassword";
			this.cbPassword.Size = new System.Drawing.Size(68, 18);
			this.cbPassword.TabIndex = 1;
			this.cbPassword.Text = "密码框";
			this.cbPassword.UseVisualStyleBackColor = true;
			// 
			// cbSelect
			// 
			this.cbSelect.AutoSize = true;
			this.cbSelect.Location = new System.Drawing.Point(49, 0);
			this.cbSelect.Name = "cbSelect";
			this.cbSelect.Size = new System.Drawing.Size(68, 18);
			this.cbSelect.TabIndex = 0;
			this.cbSelect.Text = "可选择";
			this.cbSelect.UseVisualStyleBackColor = true;
			// 
			// tbText
			// 
			this.tbText.Location = new System.Drawing.Point(8, 20);
			this.tbText.Multiline = true;
			this.tbText.Name = "tbText";
			this.tbText.Size = new System.Drawing.Size(182, 59);
			this.tbText.TabIndex = 2;
			this.tbText.TextChanged += new System.EventHandler(this.tbText_TextChanged);
			// 
			// gbText
			// 
			this.gbText.Controls.Add(this.tbText);
			this.gbText.Controls.Add(this.cbPassword);
			this.gbText.Controls.Add(this.cbNumber);
			this.gbText.Controls.Add(this.cbSelect);
			this.gbText.Controls.Add(this.lbLimit);
			this.gbText.Controls.Add(this.tbLimit);
			this.gbText.Location = new System.Drawing.Point(8, 242);
			this.gbText.Name = "gbText";
			this.gbText.Size = new System.Drawing.Size(197, 138);
			this.gbText.TabIndex = 20;
			this.gbText.TabStop = false;
			this.gbText.Text = "文本";
			// 
			// cbNumber
			// 
			this.cbNumber.AutoSize = true;
			this.cbNumber.Location = new System.Drawing.Point(9, 114);
			this.cbNumber.Name = "cbNumber";
			this.cbNumber.Size = new System.Drawing.Size(124, 18);
			this.cbNumber.TabIndex = 5;
			this.cbNumber.Text = "只允许输入数字";
			this.cbNumber.UseVisualStyleBackColor = true;
			// 
			// lbLimit
			// 
			this.lbLimit.AutoSize = true;
			this.lbLimit.Location = new System.Drawing.Point(4, 89);
			this.lbLimit.Name = "lbLimit";
			this.lbLimit.Size = new System.Drawing.Size(77, 14);
			this.lbLimit.TabIndex = 3;
			this.lbLimit.Text = "限制字符数";
			// 
			// tbLimit
			// 
			this.tbLimit.Location = new System.Drawing.Point(87, 85);
			this.tbLimit.Name = "tbLimit";
			this.tbLimit.Size = new System.Drawing.Size(103, 23);
			this.tbLimit.TabIndex = 4;
			this.tbLimit.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 516);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 22;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 535);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 24;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 539);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 23;
			this.lbTipInfo.Text = "提示信息";
			// 
			// gbScroll
			// 
			this.gbScroll.Controls.Add(this.cbbScroll);
			this.gbScroll.Controls.Add(this.rbMultiLine);
			this.gbScroll.Controls.Add(this.rbSingleLine);
			this.gbScroll.Controls.Add(this.rbRawArea);
			this.gbScroll.Location = new System.Drawing.Point(8, 386);
			this.gbScroll.Name = "gbScroll";
			this.gbScroll.Size = new System.Drawing.Size(197, 124);
			this.gbScroll.TabIndex = 21;
			this.gbScroll.TabStop = false;
			this.gbScroll.Text = "类型";
			// 
			// cbbScroll
			// 
			this.cbbScroll.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbScroll.Enabled = false;
			this.cbbScroll.FormattingEnabled = true;
			this.cbbScroll.Location = new System.Drawing.Point(28, 94);
			this.cbbScroll.Name = "cbbScroll";
			this.cbbScroll.Size = new System.Drawing.Size(162, 22);
			this.cbbScroll.Sorted = true;
			this.cbbScroll.TabIndex = 4;
			// 
			// rbMultiLine
			// 
			this.rbMultiLine.AutoSize = true;
			this.rbMultiLine.Location = new System.Drawing.Point(7, 70);
			this.rbMultiLine.Name = "rbMultiLine";
			this.rbMultiLine.Size = new System.Drawing.Size(179, 18);
			this.rbMultiLine.TabIndex = 2;
			this.rbMultiLine.TabStop = true;
			this.rbMultiLine.Text = "多行编辑框(垂直滚动条)";
			this.rbMultiLine.UseVisualStyleBackColor = true;
			this.rbMultiLine.CheckedChanged += new System.EventHandler(this.rbMultiLine_CheckedChanged);
			// 
			// rbSingleLine
			// 
			this.rbSingleLine.AutoSize = true;
			this.rbSingleLine.Checked = true;
			this.rbSingleLine.Location = new System.Drawing.Point(7, 46);
			this.rbSingleLine.Name = "rbSingleLine";
			this.rbSingleLine.Size = new System.Drawing.Size(95, 18);
			this.rbSingleLine.TabIndex = 1;
			this.rbSingleLine.TabStop = true;
			this.rbSingleLine.Text = "单行编辑框";
			this.rbSingleLine.UseVisualStyleBackColor = true;
			// 
			// rbRawArea
			// 
			this.rbRawArea.AutoSize = true;
			this.rbRawArea.Location = new System.Drawing.Point(7, 22);
			this.rbRawArea.Name = "rbRawArea";
			this.rbRawArea.Size = new System.Drawing.Size(95, 18);
			this.rbRawArea.TabIndex = 0;
			this.rbRawArea.TabStop = true;
			this.rbRawArea.Text = "原始编辑框";
			this.rbRawArea.UseVisualStyleBackColor = true;
			// 
			// CFormEdit
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 563);
			this.Controls.Add(this.gbScroll);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.gbText);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.cbbFont);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbFont);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormEdit";
			this.gbText.ResumeLayout(false);
			this.gbText.PerformLayout();
			this.gbScroll.ResumeLayout(false);
			this.gbScroll.PerformLayout();
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
		private System.Windows.Forms.Label lbBkColor;
		private System.Windows.Forms.Button bnBkColor;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbFont;
		private System.Windows.Forms.ComboBox cbbFont;
		private System.Windows.Forms.CheckBox cbPassword;
		private System.Windows.Forms.CheckBox cbSelect;
		private System.Windows.Forms.TextBox tbText;
		private System.Windows.Forms.GroupBox gbText;
		private System.Windows.Forms.CheckBox cbNumber;
		private System.Windows.Forms.Label lbLimit;
		private System.Windows.Forms.TextBox tbLimit;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
		private System.Windows.Forms.GroupBox gbScroll;
		private System.Windows.Forms.ComboBox cbbScroll;
		private System.Windows.Forms.RadioButton rbMultiLine;
		private System.Windows.Forms.RadioButton rbSingleLine;
		private System.Windows.Forms.RadioButton rbRawArea;
	}
}
