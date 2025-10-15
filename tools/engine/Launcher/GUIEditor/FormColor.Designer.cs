namespace GUIEditor2
{
	partial class CFormColor
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
			this.lbPreview = new System.Windows.Forms.Label();
			this.lbRed = new System.Windows.Forms.Label();
			this.lbGreen = new System.Windows.Forms.Label();
			this.lbBlue = new System.Windows.Forms.Label();
			this.lbAlpha = new System.Windows.Forms.Label();
			this.tbRed = new System.Windows.Forms.TextBox();
			this.tbGreen = new System.Windows.Forms.TextBox();
			this.tbBlue = new System.Windows.Forms.TextBox();
			this.tbAlpha = new System.Windows.Forms.TextBox();
			this.bnOK = new System.Windows.Forms.Button();
			this.bnCancel = new System.Windows.Forms.Button();
			this.bnAdvance = new System.Windows.Forms.Button();
			this.dlgColor = new System.Windows.Forms.ColorDialog();
			this.SuspendLayout();
			// 
			// lbPreview
			// 
			this.lbPreview.BackColor = System.Drawing.Color.Black;
			this.lbPreview.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.lbPreview.Location = new System.Drawing.Point(12, 9);
			this.lbPreview.Name = "lbPreview";
			this.lbPreview.Size = new System.Drawing.Size(137, 107);
			this.lbPreview.TabIndex = 0;
			// 
			// lbRed
			// 
			this.lbRed.AutoSize = true;
			this.lbRed.Location = new System.Drawing.Point(155, 9);
			this.lbRed.Name = "lbRed";
			this.lbRed.Size = new System.Drawing.Size(56, 14);
			this.lbRed.TabIndex = 1;
			this.lbRed.Text = "&R(红色)";
			// 
			// lbGreen
			// 
			this.lbGreen.AutoSize = true;
			this.lbGreen.Location = new System.Drawing.Point(155, 38);
			this.lbGreen.Name = "lbGreen";
			this.lbGreen.Size = new System.Drawing.Size(56, 14);
			this.lbGreen.TabIndex = 3;
			this.lbGreen.Text = "&G(绿色)";
			// 
			// lbBlue
			// 
			this.lbBlue.AutoSize = true;
			this.lbBlue.Location = new System.Drawing.Point(155, 67);
			this.lbBlue.Name = "lbBlue";
			this.lbBlue.Size = new System.Drawing.Size(56, 14);
			this.lbBlue.TabIndex = 5;
			this.lbBlue.Text = "&B(蓝色)";
			// 
			// lbAlpha
			// 
			this.lbAlpha.AutoSize = true;
			this.lbAlpha.Location = new System.Drawing.Point(155, 96);
			this.lbAlpha.Name = "lbAlpha";
			this.lbAlpha.Size = new System.Drawing.Size(56, 14);
			this.lbAlpha.TabIndex = 7;
			this.lbAlpha.Text = "&A(透明)";
			// 
			// tbRed
			// 
			this.tbRed.Location = new System.Drawing.Point(217, 6);
			this.tbRed.Name = "tbRed";
			this.tbRed.Size = new System.Drawing.Size(63, 23);
			this.tbRed.TabIndex = 2;
			this.tbRed.TextChanged += new System.EventHandler(this.tbRed_TextChanged);
			this.tbRed.MouseEnter += new System.EventHandler(this.tbRed_MouseEnter);
			// 
			// tbGreen
			// 
			this.tbGreen.Location = new System.Drawing.Point(217, 35);
			this.tbGreen.Name = "tbGreen";
			this.tbGreen.Size = new System.Drawing.Size(63, 23);
			this.tbGreen.TabIndex = 4;
			this.tbGreen.TextChanged += new System.EventHandler(this.tbGreen_TextChanged);
			this.tbGreen.MouseEnter += new System.EventHandler(this.tbGreen_MouseEnter);
			// 
			// tbBlue
			// 
			this.tbBlue.Location = new System.Drawing.Point(217, 64);
			this.tbBlue.Name = "tbBlue";
			this.tbBlue.Size = new System.Drawing.Size(63, 23);
			this.tbBlue.TabIndex = 6;
			this.tbBlue.TextChanged += new System.EventHandler(this.tbBlue_TextChanged);
			this.tbBlue.MouseEnter += new System.EventHandler(this.tbBlue_MouseEnter);
			// 
			// tbAlpha
			// 
			this.tbAlpha.Location = new System.Drawing.Point(217, 93);
			this.tbAlpha.Name = "tbAlpha";
			this.tbAlpha.Size = new System.Drawing.Size(63, 23);
			this.tbAlpha.TabIndex = 8;
			this.tbAlpha.MouseEnter += new System.EventHandler(this.tbAlpha_MouseEnter);
			// 
			// bnOK
			// 
			this.bnOK.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.bnOK.Location = new System.Drawing.Point(12, 127);
			this.bnOK.Name = "bnOK";
			this.bnOK.Size = new System.Drawing.Size(75, 26);
			this.bnOK.TabIndex = 9;
			this.bnOK.Text = "确定(&O)";
			this.bnOK.UseVisualStyleBackColor = true;
			this.bnOK.Click += new System.EventHandler(this.bnOK_Click);
			// 
			// bnCancel
			// 
			this.bnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.bnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.bnCancel.Location = new System.Drawing.Point(200, 127);
			this.bnCancel.Name = "bnCancel";
			this.bnCancel.Size = new System.Drawing.Size(80, 26);
			this.bnCancel.TabIndex = 11;
			this.bnCancel.Text = "取消(&C)";
			this.bnCancel.UseVisualStyleBackColor = true;
			// 
			// bnAdvance
			// 
			this.bnAdvance.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
			this.bnAdvance.Location = new System.Drawing.Point(94, 127);
			this.bnAdvance.Name = "bnAdvance";
			this.bnAdvance.Size = new System.Drawing.Size(101, 26);
			this.bnAdvance.TabIndex = 10;
			this.bnAdvance.Text = "高级(&D)...";
			this.bnAdvance.UseVisualStyleBackColor = true;
			this.bnAdvance.Click += new System.EventHandler(this.bnAdvance_Click);
			// 
			// dlgColor
			// 
			this.dlgColor.AnyColor = true;
			this.dlgColor.FullOpen = true;
			this.dlgColor.ShowHelp = true;
			// 
			// CFormColor
			// 
			this.AcceptButton = this.bnOK;
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.AutoSize = true;
			this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(255)))), ((int)(((byte)(255)))));
			this.CancelButton = this.bnCancel;
			this.ClientSize = new System.Drawing.Size(295, 165);
			this.Controls.Add(this.bnAdvance);
			this.Controls.Add(this.bnCancel);
			this.Controls.Add(this.bnOK);
			this.Controls.Add(this.tbAlpha);
			this.Controls.Add(this.tbBlue);
			this.Controls.Add(this.tbGreen);
			this.Controls.Add(this.tbRed);
			this.Controls.Add(this.lbAlpha);
			this.Controls.Add(this.lbBlue);
			this.Controls.Add(this.lbGreen);
			this.Controls.Add(this.lbRed);
			this.Controls.Add(this.lbPreview);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "CFormColor";
			this.ShowInTaskbar = false;
			this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
			this.Text = "GUIEditor2 - 颜色编辑";
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label lbPreview;
		private System.Windows.Forms.Label lbRed;
		private System.Windows.Forms.Label lbGreen;
		private System.Windows.Forms.Label lbBlue;
		private System.Windows.Forms.Label lbAlpha;
		private System.Windows.Forms.TextBox tbRed;
		private System.Windows.Forms.TextBox tbGreen;
		private System.Windows.Forms.TextBox tbBlue;
		private System.Windows.Forms.TextBox tbAlpha;
		private System.Windows.Forms.Button bnOK;
		private System.Windows.Forms.Button bnCancel;
		private System.Windows.Forms.Button bnAdvance;
		private System.Windows.Forms.ColorDialog dlgColor;
	}
}
