namespace GUIEditor2
{
	partial class CFormButtonImage
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
			this.lbImage0 = new System.Windows.Forms.Label();
			this.bnImage0 = new System.Windows.Forms.Button();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbImage2 = new System.Windows.Forms.Label();
			this.bnImage2 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.lbImage3 = new System.Windows.Forms.Label();
			this.bnImage3 = new System.Windows.Forms.Button();
			this.cbOnClick = new System.Windows.Forms.CheckBox();
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
			this.tbType.Text = "图像按钮(ButtonImage)";
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
			// lbImage0
			// 
			this.lbImage0.AutoSize = true;
			this.lbImage0.Location = new System.Drawing.Point(12, 189);
			this.lbImage0.Name = "lbImage0";
			this.lbImage0.Size = new System.Drawing.Size(91, 14);
			this.lbImage0.TabIndex = 16;
			this.lbImage0.Text = "图像(无效态)";
			// 
			// bnImage0
			// 
			this.bnImage0.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage0.Location = new System.Drawing.Point(101, 185);
			this.bnImage0.Name = "bnImage0";
			this.bnImage0.Size = new System.Drawing.Size(97, 22);
			this.bnImage0.TabIndex = 17;
			this.bnImage0.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage0.UseVisualStyleBackColor = true;
			this.bnImage0.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage0.Click += new System.EventHandler(this.bnImage0_Click);
			this.bnImage0.MouseEnter += new System.EventHandler(this.bnImage0_MouseEnter);
			// 
			// lbImage1
			// 
			this.lbImage1.AutoSize = true;
			this.lbImage1.Location = new System.Drawing.Point(12, 217);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(91, 14);
			this.lbImage1.TabIndex = 18;
			this.lbImage1.Text = "图像(正常态)";
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(101, 213);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(97, 22);
			this.bnImage1.TabIndex = 19;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
			this.bnImage1.MouseEnter += new System.EventHandler(this.bnImage1_MouseEnter);
			// 
			// lbImage2
			// 
			this.lbImage2.AutoSize = true;
			this.lbImage2.Location = new System.Drawing.Point(12, 245);
			this.lbImage2.Name = "lbImage2";
			this.lbImage2.Size = new System.Drawing.Size(91, 14);
			this.lbImage2.TabIndex = 20;
			this.lbImage2.Text = "图像(焦点态)";
			// 
			// bnImage2
			// 
			this.bnImage2.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage2.Location = new System.Drawing.Point(101, 241);
			this.bnImage2.Name = "bnImage2";
			this.bnImage2.Size = new System.Drawing.Size(97, 22);
			this.bnImage2.TabIndex = 21;
			this.bnImage2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage2.UseVisualStyleBackColor = true;
			this.bnImage2.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage2.Click += new System.EventHandler(this.bnImage2_Click);
			this.bnImage2.MouseEnter += new System.EventHandler(this.bnImage2_MouseEnter);
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
			// lbImage3
			// 
			this.lbImage3.AutoSize = true;
			this.lbImage3.Location = new System.Drawing.Point(12, 273);
			this.lbImage3.Name = "lbImage3";
			this.lbImage3.Size = new System.Drawing.Size(91, 14);
			this.lbImage3.TabIndex = 22;
			this.lbImage3.Text = "图像(按下态)";
			// 
			// bnImage3
			// 
			this.bnImage3.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage3.Location = new System.Drawing.Point(101, 269);
			this.bnImage3.Name = "bnImage3";
			this.bnImage3.Size = new System.Drawing.Size(97, 22);
			this.bnImage3.TabIndex = 23;
			this.bnImage3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage3.UseVisualStyleBackColor = true;
			this.bnImage3.MouseLeave += new System.EventHandler(this.bnImage_MouseLeave);
			this.bnImage3.Click += new System.EventHandler(this.bnImage3_Click);
			this.bnImage3.MouseEnter += new System.EventHandler(this.bnImage3_MouseEnter);
			// 
			// cbOnClick
			// 
			this.cbOnClick.AutoSize = true;
			this.cbOnClick.Checked = true;
			this.cbOnClick.CheckState = System.Windows.Forms.CheckState.Checked;
			this.cbOnClick.Location = new System.Drawing.Point(15, 297);
			this.cbOnClick.Name = "cbOnClick";
			this.cbOnClick.Size = new System.Drawing.Size(110, 18);
			this.cbOnClick.TabIndex = 24;
			this.cbOnClick.Text = "响应点击消息";
			this.cbOnClick.UseVisualStyleBackColor = true;
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 321);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 53;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 345);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 52;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 350);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 51;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormButtonImage
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 372);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbOnClick);
			this.Controls.Add(this.bnImage3);
			this.Controls.Add(this.bnImage2);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnImage0);
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
			this.Controls.Add(this.lbImage3);
			this.Controls.Add(this.lbImage2);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbImage0);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormButtonImage";
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
		private System.Windows.Forms.Label lbImage0;
		private System.Windows.Forms.Button bnImage0;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbImage2;
		private System.Windows.Forms.Button bnImage2;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbImage3;
		private System.Windows.Forms.Button bnImage3;
		private System.Windows.Forms.CheckBox cbOnClick;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
