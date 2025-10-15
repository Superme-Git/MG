namespace GUIEditor2
{
	partial class CFormStatic
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
            this.lbText = new System.Windows.Forms.Label();
            this.tbText = new System.Windows.Forms.TextBox();
            this.lbClass = new System.Windows.Forms.Label();
            this.tbClass = new System.Windows.Forms.TextBox();
            this.cbCenter = new System.Windows.Forms.CheckBox();
            this.cbTransparent = new System.Windows.Forms.CheckBox();
            this.cbViewOnly = new System.Windows.Forms.CheckBox();
            this.tbTipInfo = new System.Windows.Forms.TextBox();
            this.lbTipInfo = new System.Windows.Forms.Label();
            this.bnColor = new System.Windows.Forms.Button();
            this.lbColor = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // lbName
            // 
            this.lbName.AutoSize = true;
            this.lbName.Location = new System.Drawing.Point(11, 39);
            this.lbName.Name = "lbName";
            this.lbName.Size = new System.Drawing.Size(29, 12);
            this.lbName.TabIndex = 2;
            this.lbName.Text = "名称";
            // 
            // tbName
            // 
            this.tbName.Location = new System.Drawing.Point(47, 36);
            this.tbName.Name = "tbName";
            this.tbName.Size = new System.Drawing.Size(129, 21);
            this.tbName.TabIndex = 3;
            this.tbName.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // lbType
            // 
            this.lbType.AutoSize = true;
            this.lbType.Location = new System.Drawing.Point(11, 13);
            this.lbType.Name = "lbType";
            this.lbType.Size = new System.Drawing.Size(29, 12);
            this.lbType.TabIndex = 0;
            this.lbType.Text = "类型";
            // 
            // tbType
            // 
            this.tbType.Location = new System.Drawing.Point(47, 11);
            this.tbType.Name = "tbType";
            this.tbType.ReadOnly = true;
            this.tbType.Size = new System.Drawing.Size(129, 21);
            this.tbType.TabIndex = 1;
            this.tbType.TabStop = false;
            this.tbType.Text = "静态框(Static)";
            // 
            // lbX
            // 
            this.lbX.AutoSize = true;
            this.lbX.Location = new System.Drawing.Point(29, 91);
            this.lbX.Name = "lbX";
            this.lbX.Size = new System.Drawing.Size(11, 12);
            this.lbX.TabIndex = 6;
            this.lbX.Text = "X";
            // 
            // tbX
            // 
            this.tbX.Location = new System.Drawing.Point(47, 88);
            this.tbX.Name = "tbX";
            this.tbX.Size = new System.Drawing.Size(49, 21);
            this.tbX.TabIndex = 7;
            this.tbX.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // lbW
            // 
            this.lbW.AutoSize = true;
            this.lbW.Location = new System.Drawing.Point(103, 91);
            this.lbW.Name = "lbW";
            this.lbW.Size = new System.Drawing.Size(17, 12);
            this.lbW.TabIndex = 10;
            this.lbW.Text = "宽";
            // 
            // tbW
            // 
            this.tbW.Location = new System.Drawing.Point(127, 88);
            this.tbW.Name = "tbW";
            this.tbW.Size = new System.Drawing.Size(49, 21);
            this.tbW.TabIndex = 11;
            this.tbW.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // lbY
            // 
            this.lbY.AutoSize = true;
            this.lbY.Location = new System.Drawing.Point(29, 116);
            this.lbY.Name = "lbY";
            this.lbY.Size = new System.Drawing.Size(11, 12);
            this.lbY.TabIndex = 8;
            this.lbY.Text = "Y";
            // 
            // lbH
            // 
            this.lbH.AutoSize = true;
            this.lbH.Location = new System.Drawing.Point(103, 116);
            this.lbH.Name = "lbH";
            this.lbH.Size = new System.Drawing.Size(17, 12);
            this.lbH.TabIndex = 12;
            this.lbH.Text = "高";
            // 
            // tbY
            // 
            this.tbY.Location = new System.Drawing.Point(47, 114);
            this.tbY.Name = "tbY";
            this.tbY.Size = new System.Drawing.Size(49, 21);
            this.tbY.TabIndex = 9;
            this.tbY.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // tbH
            // 
            this.tbH.Location = new System.Drawing.Point(127, 114);
            this.tbH.Name = "tbH";
            this.tbH.Size = new System.Drawing.Size(49, 21);
            this.tbH.TabIndex = 13;
            this.tbH.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // lbBkColor
            // 
            this.lbBkColor.AutoSize = true;
            this.lbBkColor.Location = new System.Drawing.Point(11, 153);
            this.lbBkColor.Name = "lbBkColor";
            this.lbBkColor.Size = new System.Drawing.Size(41, 12);
            this.lbBkColor.TabIndex = 14;
            this.lbBkColor.Text = "背景色";
            // 
            // bnBkColor
            // 
            this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.bnBkColor.Location = new System.Drawing.Point(60, 150);
            this.bnBkColor.Name = "bnBkColor";
            this.bnBkColor.Size = new System.Drawing.Size(116, 20);
            this.bnBkColor.TabIndex = 15;
            this.bnBkColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.bnBkColor.UseVisualStyleBackColor = true;
            this.bnBkColor.Click += new System.EventHandler(this.bnBkColor_Click);
            // 
            // lbText
            // 
            this.lbText.AutoSize = true;
            this.lbText.Location = new System.Drawing.Point(11, 217);
            this.lbText.Name = "lbText";
            this.lbText.Size = new System.Drawing.Size(29, 12);
            this.lbText.TabIndex = 26;
            this.lbText.Text = "文本";
            // 
            // tbText
            // 
            this.tbText.Location = new System.Drawing.Point(60, 214);
            this.tbText.Name = "tbText";
            this.tbText.Size = new System.Drawing.Size(116, 21);
            this.tbText.TabIndex = 27;
            this.tbText.TextChanged += new System.EventHandler(this.tbText_TextChanged);
            // 
            // lbClass
            // 
            this.lbClass.AutoSize = true;
            this.lbClass.Location = new System.Drawing.Point(11, 65);
            this.lbClass.Name = "lbClass";
            this.lbClass.Size = new System.Drawing.Size(29, 12);
            this.lbClass.TabIndex = 4;
            this.lbClass.Text = "类名";
            // 
            // tbClass
            // 
            this.tbClass.Location = new System.Drawing.Point(47, 62);
            this.tbClass.Name = "tbClass";
            this.tbClass.Size = new System.Drawing.Size(129, 21);
            this.tbClass.TabIndex = 5;
            this.tbClass.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // cbCenter
            // 
            this.cbCenter.AutoSize = true;
            this.cbCenter.Location = new System.Drawing.Point(13, 274);
            this.cbCenter.Name = "cbCenter";
            this.cbCenter.Size = new System.Drawing.Size(96, 16);
            this.cbCenter.TabIndex = 28;
            this.cbCenter.Text = "文字居中显示";
            this.cbCenter.UseVisualStyleBackColor = true;
            this.cbCenter.CheckedChanged += new System.EventHandler(this.cbCenter_CheckedChanged);
            // 
            // cbTransparent
            // 
            this.cbTransparent.AutoSize = true;
            this.cbTransparent.Checked = true;
            this.cbTransparent.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbTransparent.Enabled = false;
            this.cbTransparent.Location = new System.Drawing.Point(13, 295);
            this.cbTransparent.Name = "cbTransparent";
            this.cbTransparent.Size = new System.Drawing.Size(108, 16);
            this.cbTransparent.TabIndex = 29;
            this.cbTransparent.Text = "不响应鼠标消息";
            this.cbTransparent.UseVisualStyleBackColor = true;
            // 
            // cbViewOnly
            // 
            this.cbViewOnly.AutoSize = true;
            this.cbViewOnly.Location = new System.Drawing.Point(13, 317);
            this.cbViewOnly.Name = "cbViewOnly";
            this.cbViewOnly.Size = new System.Drawing.Size(60, 16);
            this.cbViewOnly.TabIndex = 30;
            this.cbViewOnly.Text = "只显示";
            this.cbViewOnly.UseVisualStyleBackColor = true;
            // 
            // tbTipInfo
            // 
            this.tbTipInfo.Location = new System.Drawing.Point(72, 332);
            this.tbTipInfo.Name = "tbTipInfo";
            this.tbTipInfo.Size = new System.Drawing.Size(104, 21);
            this.tbTipInfo.TabIndex = 32;
            // 
            // lbTipInfo
            // 
            this.lbTipInfo.AutoSize = true;
            this.lbTipInfo.Location = new System.Drawing.Point(11, 335);
            this.lbTipInfo.Name = "lbTipInfo";
            this.lbTipInfo.Size = new System.Drawing.Size(53, 12);
            this.lbTipInfo.TabIndex = 31;
            this.lbTipInfo.Text = "提示信息";
            // 
            // bnColor
            // 
            this.bnColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.bnColor.Location = new System.Drawing.Point(60, 181);
            this.bnColor.Name = "bnColor";
            this.bnColor.Size = new System.Drawing.Size(116, 20);
            this.bnColor.TabIndex = 34;
            this.bnColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.bnColor.UseVisualStyleBackColor = true;
            this.bnColor.Click += new System.EventHandler(this.bnColor_Click);
            // 
            // lbColor
            // 
            this.lbColor.AutoSize = true;
            this.lbColor.Location = new System.Drawing.Point(9, 185);
            this.lbColor.Name = "lbColor";
            this.lbColor.Size = new System.Drawing.Size(41, 12);
            this.lbColor.TabIndex = 33;
            this.lbColor.Text = "文本色";
            // 
            // CFormStatic
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(187, 370);
            this.Controls.Add(this.bnColor);
            this.Controls.Add(this.lbColor);
            this.Controls.Add(this.tbTipInfo);
            this.Controls.Add(this.lbTipInfo);
            this.Controls.Add(this.cbViewOnly);
            this.Controls.Add(this.cbTransparent);
            this.Controls.Add(this.cbCenter);
            this.Controls.Add(this.bnBkColor);
            this.Controls.Add(this.tbType);
            this.Controls.Add(this.tbH);
            this.Controls.Add(this.tbW);
            this.Controls.Add(this.tbY);
            this.Controls.Add(this.tbX);
            this.Controls.Add(this.tbText);
            this.Controls.Add(this.tbClass);
            this.Controls.Add(this.tbName);
            this.Controls.Add(this.lbH);
            this.Controls.Add(this.lbType);
            this.Controls.Add(this.lbY);
            this.Controls.Add(this.lbW);
            this.Controls.Add(this.lbX);
            this.Controls.Add(this.lbBkColor);
            this.Controls.Add(this.lbText);
            this.Controls.Add(this.lbClass);
            this.Controls.Add(this.lbName);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.KeyPreview = true;
            this.Name = "CFormStatic";
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
		private System.Windows.Forms.Label lbText;
		private System.Windows.Forms.TextBox tbText;
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.CheckBox cbCenter;
		private System.Windows.Forms.CheckBox cbTransparent;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
        private System.Windows.Forms.Button bnColor;
        private System.Windows.Forms.Label lbColor;
	}
}
