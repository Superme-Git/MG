namespace GUIEditor2
{
	partial class CFormProgressBar
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
            this.lbClass = new System.Windows.Forms.Label();
            this.tbClass = new System.Windows.Forms.TextBox();
            this.lbImageBlock = new System.Windows.Forms.Label();
            this.bnImageBlock = new System.Windows.Forms.Button();
            this.cbViewOnly = new System.Windows.Forms.CheckBox();
            this.tbTipInfo = new System.Windows.Forms.TextBox();
            this.lbTipInfo = new System.Windows.Forms.Label();
            this.bnImageBackground = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.bnColor = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.cbShowPercent = new System.Windows.Forms.CheckBox();
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
            this.tbType.Text = "进度条(ProgressBar)";
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
            this.lbW.TabIndex = 8;
            this.lbW.Text = "宽";
            // 
            // tbW
            // 
            this.tbW.Location = new System.Drawing.Point(127, 88);
            this.tbW.Name = "tbW";
            this.tbW.Size = new System.Drawing.Size(49, 21);
            this.tbW.TabIndex = 9;
            this.tbW.Leave += new System.EventHandler(this.tb_Leave);
            // 
            // lbY
            // 
            this.lbY.AutoSize = true;
            this.lbY.Location = new System.Drawing.Point(29, 116);
            this.lbY.Name = "lbY";
            this.lbY.Size = new System.Drawing.Size(11, 12);
            this.lbY.TabIndex = 10;
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
            this.tbY.TabIndex = 11;
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
            // lbImageBlock
            // 
            this.lbImageBlock.AutoSize = true;
            this.lbImageBlock.Location = new System.Drawing.Point(11, 185);
            this.lbImageBlock.Name = "lbImageBlock";
            this.lbImageBlock.Size = new System.Drawing.Size(41, 12);
            this.lbImageBlock.TabIndex = 18;
            this.lbImageBlock.Text = "进度块";
            // 
            // bnImageBlock
            // 
            this.bnImageBlock.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.bnImageBlock.Location = new System.Drawing.Point(60, 181);
            this.bnImageBlock.Name = "bnImageBlock";
            this.bnImageBlock.Size = new System.Drawing.Size(116, 20);
            this.bnImageBlock.TabIndex = 19;
            this.bnImageBlock.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.bnImageBlock.UseVisualStyleBackColor = true;
            this.bnImageBlock.Click += new System.EventHandler(this.bnImageBlock_Click);
            // 
            // cbViewOnly
            // 
            this.cbViewOnly.AutoSize = true;
            this.cbViewOnly.Location = new System.Drawing.Point(14, 303);
            this.cbViewOnly.Name = "cbViewOnly";
            this.cbViewOnly.Size = new System.Drawing.Size(60, 16);
            this.cbViewOnly.TabIndex = 20;
            this.cbViewOnly.Text = "只显示";
            this.cbViewOnly.UseVisualStyleBackColor = true;
            // 
            // tbTipInfo
            // 
            this.tbTipInfo.Location = new System.Drawing.Point(72, 267);
            this.tbTipInfo.Name = "tbTipInfo";
            this.tbTipInfo.Size = new System.Drawing.Size(104, 21);
            this.tbTipInfo.TabIndex = 22;
            // 
            // lbTipInfo
            // 
            this.lbTipInfo.AutoSize = true;
            this.lbTipInfo.Location = new System.Drawing.Point(11, 271);
            this.lbTipInfo.Name = "lbTipInfo";
            this.lbTipInfo.Size = new System.Drawing.Size(53, 12);
            this.lbTipInfo.TabIndex = 21;
            this.lbTipInfo.Text = "提示信息";
            // 
            // bnImageBackground
            // 
            this.bnImageBackground.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.bnImageBackground.Location = new System.Drawing.Point(60, 157);
            this.bnImageBackground.Name = "bnImageBackground";
            this.bnImageBackground.Size = new System.Drawing.Size(116, 20);
            this.bnImageBackground.TabIndex = 17;
            this.bnImageBackground.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.bnImageBackground.UseVisualStyleBackColor = true;
            this.bnImageBackground.Click += new System.EventHandler(this.bnImageBackground_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(11, 161);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 12);
            this.label1.TabIndex = 16;
            this.label1.Text = "背景图";
            // 
            // bnColor
            // 
            this.bnColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.bnColor.Location = new System.Drawing.Point(60, 231);
            this.bnColor.Name = "bnColor";
            this.bnColor.Size = new System.Drawing.Size(115, 20);
            this.bnColor.TabIndex = 24;
            this.bnColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.bnColor.UseVisualStyleBackColor = true;
            this.bnColor.Click += new System.EventHandler(this.bnColor_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(7, 235);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 23;
            this.label2.Text = "文字颜色";
            // 
            // cbShowPercent
            // 
            this.cbShowPercent.AutoSize = true;
            this.cbShowPercent.Location = new System.Drawing.Point(12, 211);
            this.cbShowPercent.Name = "cbShowPercent";
            this.cbShowPercent.Size = new System.Drawing.Size(132, 16);
            this.cbShowPercent.TabIndex = 25;
            this.cbShowPercent.Text = "显示进度百分比数值";
            this.cbShowPercent.UseVisualStyleBackColor = true;
            this.cbShowPercent.CheckedChanged += new System.EventHandler(this.cbShowPercent_CheckedChanged);
            // 
            // CFormProgressBar
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(187, 511);
            this.Controls.Add(this.cbShowPercent);
            this.Controls.Add(this.bnColor);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.bnImageBackground);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.cbViewOnly);
            this.Controls.Add(this.tbTipInfo);
            this.Controls.Add(this.lbTipInfo);
            this.Controls.Add(this.bnImageBlock);
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
            this.Controls.Add(this.lbImageBlock);
            this.Controls.Add(this.lbClass);
            this.Controls.Add(this.lbName);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.KeyPreview = true;
            this.Name = "CFormProgressBar";
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
		private System.Windows.Forms.Label lbClass;
		private System.Windows.Forms.TextBox tbClass;
		private System.Windows.Forms.Label lbImageBlock;
		private System.Windows.Forms.Button bnImageBlock;
		private System.Windows.Forms.CheckBox cbViewOnly;
		private System.Windows.Forms.TextBox tbTipInfo;
		private System.Windows.Forms.Label lbTipInfo;
        private System.Windows.Forms.Button bnImageBackground;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button bnColor;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.CheckBox cbShowPercent;
	}
}
