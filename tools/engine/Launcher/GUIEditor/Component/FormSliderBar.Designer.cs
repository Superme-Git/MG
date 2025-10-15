namespace GUIEditor2
{
	partial class CFormSliderBar
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
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbImageSlot = new System.Windows.Forms.Label();
			this.lbImageBlock3 = new System.Windows.Forms.Label();
			this.lbImageBlock1 = new System.Windows.Forms.Label();
			this.lbImageBlock2 = new System.Windows.Forms.Label();
			this.bnImageSlot = new System.Windows.Forms.Button();
			this.bnImageBlock1 = new System.Windows.Forms.Button();
			this.bnImageBlock2 = new System.Windows.Forms.Button();
			this.bnImageBlock3 = new System.Windows.Forms.Button();
			this.cbVertical = new System.Windows.Forms.CheckBox();
			this.cbViewOnly = new System.Windows.Forms.CheckBox();
			this.tbTipInfo = new System.Windows.Forms.TextBox();
			this.lbTipInfo = new System.Windows.Forms.Label();
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
			this.tbType.Text = "滑动条(SliderBar)";
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
			// lbBkColor
			// 
			this.lbBkColor.AutoSize = true;
			this.lbBkColor.Location = new System.Drawing.Point(12, 161);
			this.lbBkColor.Name = "lbBkColor";
			this.lbBkColor.Size = new System.Drawing.Size(49, 14);
			this.lbBkColor.TabIndex = 15;
			this.lbBkColor.Text = "背景色";
			// 
			// bnBkColor
			// 
			this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor.Location = new System.Drawing.Point(67, 157);
			this.bnBkColor.Name = "bnBkColor";
			this.bnBkColor.Size = new System.Drawing.Size(131, 22);
			this.bnBkColor.TabIndex = 16;
			this.bnBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnBkColor.UseVisualStyleBackColor = true;
			this.bnBkColor.Click += new System.EventHandler(this.bnBkColor_Click);
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
			// lbImageSlot
			// 
			this.lbImageSlot.AutoSize = true;
			this.lbImageSlot.Location = new System.Drawing.Point(12, 189);
			this.lbImageSlot.Name = "lbImageSlot";
			this.lbImageSlot.Size = new System.Drawing.Size(21, 14);
			this.lbImageSlot.TabIndex = 17;
			this.lbImageSlot.Text = "槽";
			// 
			// lbImageBlock3
			// 
			this.lbImageBlock3.AutoSize = true;
			this.lbImageBlock3.Location = new System.Drawing.Point(12, 273);
			this.lbImageBlock3.Name = "lbImageBlock3";
			this.lbImageBlock3.Size = new System.Drawing.Size(63, 14);
			this.lbImageBlock3.TabIndex = 23;
			this.lbImageBlock3.Text = "块(按下)";
			// 
			// lbImageBlock1
			// 
			this.lbImageBlock1.AutoSize = true;
			this.lbImageBlock1.Location = new System.Drawing.Point(12, 217);
			this.lbImageBlock1.Name = "lbImageBlock1";
			this.lbImageBlock1.Size = new System.Drawing.Size(63, 14);
			this.lbImageBlock1.TabIndex = 19;
			this.lbImageBlock1.Text = "块(正常)";
			// 
			// lbImageBlock2
			// 
			this.lbImageBlock2.AutoSize = true;
			this.lbImageBlock2.Location = new System.Drawing.Point(12, 245);
			this.lbImageBlock2.Name = "lbImageBlock2";
			this.lbImageBlock2.Size = new System.Drawing.Size(63, 14);
			this.lbImageBlock2.TabIndex = 21;
			this.lbImageBlock2.Text = "块(焦点)";
			// 
			// bnImageSlot
			// 
			this.bnImageSlot.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageSlot.Location = new System.Drawing.Point(81, 185);
			this.bnImageSlot.Name = "bnImageSlot";
			this.bnImageSlot.Size = new System.Drawing.Size(117, 22);
			this.bnImageSlot.TabIndex = 18;
			this.bnImageSlot.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImageSlot.UseVisualStyleBackColor = true;
			this.bnImageSlot.Click += new System.EventHandler(this.bnImageSlot_Click);
			// 
			// bnImageBlock1
			// 
			this.bnImageBlock1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageBlock1.Location = new System.Drawing.Point(81, 213);
			this.bnImageBlock1.Name = "bnImageBlock1";
			this.bnImageBlock1.Size = new System.Drawing.Size(117, 22);
			this.bnImageBlock1.TabIndex = 20;
			this.bnImageBlock1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImageBlock1.UseVisualStyleBackColor = true;
			this.bnImageBlock1.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImageBlock1.Click += new System.EventHandler(this.bnImageBlock1_Click);
			this.bnImageBlock1.MouseEnter += new System.EventHandler(this.bnImageBlock1_MouseEnter);
			// 
			// bnImageBlock2
			// 
			this.bnImageBlock2.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageBlock2.Location = new System.Drawing.Point(81, 241);
			this.bnImageBlock2.Name = "bnImageBlock2";
			this.bnImageBlock2.Size = new System.Drawing.Size(117, 22);
			this.bnImageBlock2.TabIndex = 22;
			this.bnImageBlock2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImageBlock2.UseVisualStyleBackColor = true;
			this.bnImageBlock2.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImageBlock2.Click += new System.EventHandler(this.bnImageBlock2_Click);
			this.bnImageBlock2.MouseEnter += new System.EventHandler(this.bnImageBlock2_MouseEnter);
			// 
			// bnImageBlock3
			// 
			this.bnImageBlock3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImageBlock3.Location = new System.Drawing.Point(81, 269);
			this.bnImageBlock3.Name = "bnImageBlock3";
			this.bnImageBlock3.Size = new System.Drawing.Size(117, 22);
			this.bnImageBlock3.TabIndex = 24;
			this.bnImageBlock3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImageBlock3.UseVisualStyleBackColor = true;
			this.bnImageBlock3.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImageBlock3.Click += new System.EventHandler(this.bnImageBlock3_Click);
			this.bnImageBlock3.MouseEnter += new System.EventHandler(this.bnImageBlock3_MouseEnter);
			// 
			// cbVertical
			// 
			this.cbVertical.AutoSize = true;
			this.cbVertical.Checked = true;
			this.cbVertical.CheckState = System.Windows.Forms.CheckState.Checked;
			this.cbVertical.Location = new System.Drawing.Point(15, 297);
			this.cbVertical.Name = "cbVertical";
			this.cbVertical.Size = new System.Drawing.Size(96, 18);
			this.cbVertical.TabIndex = 25;
			this.cbVertical.Text = "垂直滚动条";
			this.cbVertical.UseVisualStyleBackColor = true;
			this.cbVertical.CheckedChanged += new System.EventHandler(this.cbVertical_CheckedChanged);
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 321);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 26;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 338);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 28;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 342);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 27;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormSliderBar
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 366);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.cbVertical);
			this.Controls.Add(this.bnImageBlock3);
			this.Controls.Add(this.bnImageBlock2);
			this.Controls.Add(this.bnImageBlock1);
			this.Controls.Add(this.bnImageSlot);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbImageBlock2);
			this.Controls.Add(this.lbImageBlock1);
			this.Controls.Add(this.lbImageBlock3);
			this.Controls.Add(this.lbImageSlot);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormSliderBar";
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
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbImageSlot;
		private System.Windows.Forms.Label lbImageBlock3;
		private System.Windows.Forms.Label lbImageBlock1;
		private System.Windows.Forms.Label lbImageBlock2;
		private System.Windows.Forms.Button bnImageSlot;
		private System.Windows.Forms.Button bnImageBlock1;
		private System.Windows.Forms.Button bnImageBlock2;
		private System.Windows.Forms.Button bnImageBlock3;
		private System.Windows.Forms.CheckBox cbVertical;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
