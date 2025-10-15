namespace GUIEditor2
{
	partial class CFormFrame
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
			this.cbAutoClose = new System.Windows.Forms.CheckBox();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbClass = new System.Windows.Forms.Label();
			this.tbClass = new System.Windows.Forms.TextBox();
			this.cbbMoveType = new System.Windows.Forms.ComboBox();
			this.lbMoveType = new System.Windows.Forms.Label();
			this.lbNotifyClass = new System.Windows.Forms.Label();
			this.tbNotifyClass = new System.Windows.Forms.TextBox();
			this.lbOutDir = new System.Windows.Forms.Label();
			this.tbOutDir = new System.Windows.Forms.TextBox();
			this.lbInclude = new System.Windows.Forms.Label();
			this.tbInclude = new System.Windows.Forms.TextBox();
			this.cbPopUp = new System.Windows.Forms.CheckBox();
			this.cbHookEditKey = new System.Windows.Forms.CheckBox();
			this.lbTitleHeight = new System.Windows.Forms.Label();
			this.tbTitleHeight = new System.Windows.Forms.TextBox();
			this.lbNameSpace = new System.Windows.Forms.Label();
			this.tbNameSpace = new System.Windows.Forms.TextBox();
			this.lbDefaultFocus = new System.Windows.Forms.Label();
			this.cbbDefaultFocus = new System.Windows.Forms.ComboBox();
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
			this.tbType.Text = "窗口(Frame)";
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
			// cbAutoClose
			// 
			this.cbAutoClose.AutoSize = true;
			this.cbAutoClose.Location = new System.Drawing.Point(15, 414);
			this.cbAutoClose.Name = "cbAutoClose";
			this.cbAutoClose.Size = new System.Drawing.Size(82, 18);
			this.cbAutoClose.TabIndex = 28;
			this.cbAutoClose.Text = "自动关闭";
			this.cbAutoClose.UseVisualStyleBackColor = true;
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(67, 185);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(131, 22);
			this.bnImage1.TabIndex = 17;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
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
			// cbbMoveType
			// 
			this.cbbMoveType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbMoveType.FormattingEnabled = true;
			this.cbbMoveType.Items.AddRange(new object[] {
            "none",
            "title",
            "body",
            "all"});
			this.cbbMoveType.Location = new System.Drawing.Point(81, 213);
			this.cbbMoveType.Name = "cbbMoveType";
			this.cbbMoveType.Size = new System.Drawing.Size(117, 22);
			this.cbbMoveType.TabIndex = 19;
			// 
			// lbMoveType
			// 
			this.lbMoveType.AutoSize = true;
			this.lbMoveType.Location = new System.Drawing.Point(12, 216);
			this.lbMoveType.Name = "lbMoveType";
			this.lbMoveType.Size = new System.Drawing.Size(63, 14);
			this.lbMoveType.TabIndex = 18;
			this.lbMoveType.Text = "移动类型";
			// 
			// lbNotifyClass
			// 
			this.lbNotifyClass.AutoSize = true;
			this.lbNotifyClass.Location = new System.Drawing.Point(12, 274);
			this.lbNotifyClass.Name = "lbNotifyClass";
			this.lbNotifyClass.Size = new System.Drawing.Size(49, 14);
			this.lbNotifyClass.TabIndex = 22;
			this.lbNotifyClass.Text = "通知类";
			// 
			// tbNotifyClass
			// 
			this.tbNotifyClass.Location = new System.Drawing.Point(81, 270);
			this.tbNotifyClass.Name = "tbNotifyClass";
			this.tbNotifyClass.Size = new System.Drawing.Size(117, 23);
			this.tbNotifyClass.TabIndex = 23;
			this.tbNotifyClass.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbOutDir
			// 
			this.lbOutDir.AutoSize = true;
			this.lbOutDir.Location = new System.Drawing.Point(12, 303);
			this.lbOutDir.Name = "lbOutDir";
			this.lbOutDir.Size = new System.Drawing.Size(63, 14);
			this.lbOutDir.TabIndex = 24;
			this.lbOutDir.Text = "输出目录";
			// 
			// tbOutDir
			// 
			this.tbOutDir.Location = new System.Drawing.Point(81, 299);
			this.tbOutDir.Name = "tbOutDir";
			this.tbOutDir.Size = new System.Drawing.Size(117, 23);
			this.tbOutDir.TabIndex = 25;
			this.tbOutDir.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbInclude
			// 
			this.lbInclude.AutoSize = true;
			this.lbInclude.Location = new System.Drawing.Point(12, 332);
			this.lbInclude.Name = "lbInclude";
			this.lbInclude.Size = new System.Drawing.Size(63, 14);
			this.lbInclude.TabIndex = 26;
			this.lbInclude.Text = "包含目录";
			// 
			// tbInclude
			// 
			this.tbInclude.Location = new System.Drawing.Point(81, 328);
			this.tbInclude.Name = "tbInclude";
			this.tbInclude.Size = new System.Drawing.Size(117, 23);
			this.tbInclude.TabIndex = 27;
			this.tbInclude.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// cbPopUp
			// 
			this.cbPopUp.AutoSize = true;
			this.cbPopUp.Location = new System.Drawing.Point(15, 438);
			this.cbPopUp.Name = "cbPopUp";
			this.cbPopUp.Size = new System.Drawing.Size(96, 18);
			this.cbPopUp.TabIndex = 29;
			this.cbPopUp.Text = "弹出式窗口";
			this.cbPopUp.UseVisualStyleBackColor = true;
			// 
			// cbHookEditKey
			// 
			this.cbHookEditKey.AutoSize = true;
			this.cbHookEditKey.Location = new System.Drawing.Point(15, 462);
			this.cbHookEditKey.Name = "cbHookEditKey";
			this.cbHookEditKey.Size = new System.Drawing.Size(110, 18);
			this.cbHookEditKey.TabIndex = 30;
			this.cbHookEditKey.Text = "处理键盘消息";
			this.cbHookEditKey.UseVisualStyleBackColor = true;
			// 
			// lbTitleHeight
			// 
			this.lbTitleHeight.AutoSize = true;
			this.lbTitleHeight.Location = new System.Drawing.Point(12, 245);
			this.lbTitleHeight.Name = "lbTitleHeight";
			this.lbTitleHeight.Size = new System.Drawing.Size(63, 14);
			this.lbTitleHeight.TabIndex = 20;
			this.lbTitleHeight.Text = "标题栏高";
			// 
			// tbTitleHeight
			// 
			this.tbTitleHeight.Location = new System.Drawing.Point(81, 241);
			this.tbTitleHeight.Name = "tbTitleHeight";
			this.tbTitleHeight.Size = new System.Drawing.Size(117, 23);
			this.tbTitleHeight.TabIndex = 21;
			this.tbTitleHeight.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbNameSpace
			// 
			this.lbNameSpace.AutoSize = true;
			this.lbNameSpace.Location = new System.Drawing.Point(12, 361);
			this.lbNameSpace.Name = "lbNameSpace";
			this.lbNameSpace.Size = new System.Drawing.Size(63, 14);
			this.lbNameSpace.TabIndex = 26;
			this.lbNameSpace.Text = "命名空间";
			// 
			// tbNameSpace
			// 
			this.tbNameSpace.Location = new System.Drawing.Point(81, 357);
			this.tbNameSpace.Name = "tbNameSpace";
			this.tbNameSpace.Size = new System.Drawing.Size(117, 23);
			this.tbNameSpace.TabIndex = 27;
			this.tbNameSpace.Leave += new System.EventHandler(this.tb_Leave);
			// 
			// lbDefaultFocus
			// 
			this.lbDefaultFocus.AutoSize = true;
			this.lbDefaultFocus.Location = new System.Drawing.Point(12, 389);
			this.lbDefaultFocus.Name = "lbDefaultFocus";
			this.lbDefaultFocus.Size = new System.Drawing.Size(63, 14);
			this.lbDefaultFocus.TabIndex = 18;
			this.lbDefaultFocus.Text = "默认焦点";
			// 
			// cbbDefaultFocus
			// 
			this.cbbDefaultFocus.FormattingEnabled = true;
			this.cbbDefaultFocus.Items.AddRange(new object[] {
            "none",
            "title",
            "body",
            "all"});
			this.cbbDefaultFocus.Location = new System.Drawing.Point(81, 386);
			this.cbbDefaultFocus.Name = "cbbDefaultFocus";
			this.cbbDefaultFocus.Size = new System.Drawing.Size(117, 22);
			this.cbbDefaultFocus.TabIndex = 19;
			this.cbbDefaultFocus.DropDown += new System.EventHandler(this.cbbDefaultFocus_DropDown);
			// 
			// cbViewOnly
			// 
			this.cbViewOnly.AutoSize = true;
			this.cbViewOnly.Location = new System.Drawing.Point(15, 486);
			this.cbViewOnly.Name = "cbViewOnly";
			this.cbViewOnly.Size = new System.Drawing.Size(68, 18);
			this.cbViewOnly.TabIndex = 44;
			this.cbViewOnly.Text = "只显示";
			this.cbViewOnly.UseVisualStyleBackColor = true;
			// 
			// tbTipInfo
			// 
			this.tbTipInfo.Location = new System.Drawing.Point(81, 504);
			this.tbTipInfo.Name = "tbTipInfo";
			this.tbTipInfo.Size = new System.Drawing.Size(117, 23);
			this.tbTipInfo.TabIndex = 43;
			// 
			// lbTipInfo
			// 
			this.lbTipInfo.AutoSize = true;
			this.lbTipInfo.Location = new System.Drawing.Point(12, 509);
			this.lbTipInfo.Name = "lbTipInfo";
			this.lbTipInfo.Size = new System.Drawing.Size(63, 14);
			this.lbTipInfo.TabIndex = 42;
			this.lbTipInfo.Text = "提示信息";
			// 
			// CFormFrame
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 529);
			this.Controls.Add(this.cbViewOnly);
			this.Controls.Add(this.tbTipInfo);
			this.Controls.Add(this.lbTipInfo);
			this.Controls.Add(this.cbbDefaultFocus);
			this.Controls.Add(this.cbbMoveType);
			this.Controls.Add(this.cbHookEditKey);
			this.Controls.Add(this.cbPopUp);
			this.Controls.Add(this.cbAutoClose);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.tbH);
			this.Controls.Add(this.tbW);
			this.Controls.Add(this.tbY);
			this.Controls.Add(this.tbTitleHeight);
			this.Controls.Add(this.tbX);
			this.Controls.Add(this.tbNameSpace);
			this.Controls.Add(this.tbInclude);
			this.Controls.Add(this.tbOutDir);
			this.Controls.Add(this.tbNotifyClass);
			this.Controls.Add(this.tbClass);
			this.Controls.Add(this.tbName);
			this.Controls.Add(this.lbH);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbY);
			this.Controls.Add(this.lbW);
			this.Controls.Add(this.lbTitleHeight);
			this.Controls.Add(this.lbX);
			this.Controls.Add(this.lbDefaultFocus);
			this.Controls.Add(this.lbMoveType);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbNameSpace);
			this.Controls.Add(this.lbBkColor);
			this.Controls.Add(this.lbInclude);
			this.Controls.Add(this.lbOutDir);
			this.Controls.Add(this.lbNotifyClass);
			this.Controls.Add(this.lbClass);
			this.Controls.Add(this.lbName);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormFrame";
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
		private System.Windows.Forms.CheckBox cbAutoClose;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.ComboBox cbbMoveType;
		private System.Windows.Forms.Label lbMoveType;
		private System.Windows.Forms.Label lbNotifyClass;
		private System.Windows.Forms.TextBox tbNotifyClass;
		private System.Windows.Forms.Label lbOutDir;
		private System.Windows.Forms.TextBox tbOutDir;
		private System.Windows.Forms.Label lbInclude;
		private System.Windows.Forms.TextBox tbInclude;
		private System.Windows.Forms.CheckBox cbPopUp;
		private System.Windows.Forms.CheckBox cbHookEditKey;
		private System.Windows.Forms.Label lbTitleHeight;
		private System.Windows.Forms.TextBox tbTitleHeight;
		private System.Windows.Forms.Label lbNameSpace;
		private System.Windows.Forms.TextBox tbNameSpace;
		private System.Windows.Forms.Label lbDefaultFocus;
		private System.Windows.Forms.ComboBox cbbDefaultFocus;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
	}
}
