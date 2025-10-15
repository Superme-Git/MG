namespace GUIEditor2
{
	partial class CFormDesktop
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
			this.lbType = new System.Windows.Forms.Label();
			this.tbType = new System.Windows.Forms.TextBox();
			this.lbBkColor = new System.Windows.Forms.Label();
			this.bnBkColor = new System.Windows.Forms.Button();
			this.lbImage1 = new System.Windows.Forms.Label();
			this.bnImage1 = new System.Windows.Forms.Button();
			this.lbNote = new System.Windows.Forms.Label();
			this.lbColor = new System.Windows.Forms.Label();
			this.lbImage = new System.Windows.Forms.Label();
			this.bnColor = new System.Windows.Forms.Button();
			this.bnImage = new System.Windows.Forms.Button();
			this.cbHasForeImage = new System.Windows.Forms.CheckBox();
			this.SuspendLayout();
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
			this.tbType.Text = "桌面(Desktop)";
			// 
			// lbBkColor
			// 
			this.lbBkColor.AutoSize = true;
			this.lbBkColor.Location = new System.Drawing.Point(12, 45);
			this.lbBkColor.Name = "lbBkColor";
			this.lbBkColor.Size = new System.Drawing.Size(49, 14);
			this.lbBkColor.TabIndex = 14;
			this.lbBkColor.Text = "背景色";
			// 
			// bnBkColor
			// 
			this.bnBkColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnBkColor.Location = new System.Drawing.Point(67, 41);
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
			this.lbImage1.Location = new System.Drawing.Point(12, 72);
			this.lbImage1.Name = "lbImage1";
			this.lbImage1.Size = new System.Drawing.Size(49, 14);
			this.lbImage1.TabIndex = 16;
			this.lbImage1.Text = "背景图";
			// 
			// bnImage1
			// 
			this.bnImage1.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage1.Location = new System.Drawing.Point(67, 69);
			this.bnImage1.Name = "bnImage1";
			this.bnImage1.Size = new System.Drawing.Size(131, 22);
			this.bnImage1.TabIndex = 17;
			this.bnImage1.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage1.UseVisualStyleBackColor = true;
			this.bnImage1.Click += new System.EventHandler(this.bnImage1_Click);
			// 
			// lbNote
			// 
			this.lbNote.Location = new System.Drawing.Point(12, 174);
			this.lbNote.Name = "lbNote";
			this.lbNote.Size = new System.Drawing.Size(186, 59);
			this.lbNote.TabIndex = 18;
			this.lbNote.Text = "说明：桌面的这些属性仅供编辑参考，不影响游戏中的界面。前景色和前景图配置不会被保存。";
			this.lbNote.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			// 
			// lbColor
			// 
			this.lbColor.AutoSize = true;
			this.lbColor.Location = new System.Drawing.Point(12, 101);
			this.lbColor.Name = "lbColor";
			this.lbColor.Size = new System.Drawing.Size(49, 14);
			this.lbColor.TabIndex = 14;
			this.lbColor.Text = "前景色";
			// 
			// lbImage
			// 
			this.lbImage.AutoSize = true;
			this.lbImage.Location = new System.Drawing.Point(12, 128);
			this.lbImage.Name = "lbImage";
			this.lbImage.Size = new System.Drawing.Size(49, 14);
			this.lbImage.TabIndex = 16;
			this.lbImage.Text = "前景图";
			// 
			// bnColor
			// 
			this.bnColor.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnColor.Location = new System.Drawing.Point(67, 97);
			this.bnColor.Name = "bnColor";
			this.bnColor.Size = new System.Drawing.Size(131, 22);
			this.bnColor.TabIndex = 15;
			this.bnColor.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			this.bnColor.UseVisualStyleBackColor = true;
			this.bnColor.Click += new System.EventHandler(this.bnColor_Click);
			// 
			// bnImage
			// 
			this.bnImage.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
			this.bnImage.Location = new System.Drawing.Point(67, 125);
			this.bnImage.Name = "bnImage";
			this.bnImage.Size = new System.Drawing.Size(131, 22);
			this.bnImage.TabIndex = 17;
			this.bnImage.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.bnImage.UseVisualStyleBackColor = true;
			this.bnImage.Click += new System.EventHandler(this.bnImage_Click);
			// 
			// cbHasForeImage
			// 
			this.cbHasForeImage.AutoSize = true;
			this.cbHasForeImage.Location = new System.Drawing.Point(15, 153);
			this.cbHasForeImage.Name = "cbHasForeImage";
			this.cbHasForeImage.Size = new System.Drawing.Size(82, 18);
			this.cbHasForeImage.TabIndex = 19;
			this.cbHasForeImage.Text = "显示前景";
			this.cbHasForeImage.UseVisualStyleBackColor = true;
			this.cbHasForeImage.CheckedChanged += new System.EventHandler(this.cbHasForeImage_CheckedChanged);
			// 
			// CFormDesktop
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.ClientSize = new System.Drawing.Size(210, 237);
			this.Controls.Add(this.cbHasForeImage);
			this.Controls.Add(this.lbNote);
			this.Controls.Add(this.bnImage);
			this.Controls.Add(this.bnImage1);
			this.Controls.Add(this.bnColor);
			this.Controls.Add(this.bnBkColor);
			this.Controls.Add(this.tbType);
			this.Controls.Add(this.lbImage);
			this.Controls.Add(this.lbType);
			this.Controls.Add(this.lbColor);
			this.Controls.Add(this.lbImage1);
			this.Controls.Add(this.lbBkColor);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.KeyPreview = true;
			this.Name = "CFormDesktop";
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label lbType;
		private System.Windows.Forms.TextBox tbType;
		private System.Windows.Forms.Label lbBkColor;
		private System.Windows.Forms.Button bnBkColor;
		private System.Windows.Forms.Label lbImage1;
		private System.Windows.Forms.Button bnImage1;
		private System.Windows.Forms.Label lbNote;
		private System.Windows.Forms.Label lbColor;
		private System.Windows.Forms.Label lbImage;
		private System.Windows.Forms.Button bnColor;
		private System.Windows.Forms.Button bnImage;
		private System.Windows.Forms.CheckBox cbHasForeImage;
	}
}
