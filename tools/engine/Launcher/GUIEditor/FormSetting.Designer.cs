namespace GUIEditor2
{
	partial class FormSetting
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
			this.lbInitSize = new System.Windows.Forms.Label();
			this.cbbInitSize = new System.Windows.Forms.ComboBox();
			this.bnOK = new System.Windows.Forms.Button();
			this.bnCancel = new System.Windows.Forms.Button();
			this.gbImageOrder = new System.Windows.Forms.GroupBox();
			this.rbImageOrder1 = new System.Windows.Forms.RadioButton();
			this.rbImageOrder0 = new System.Windows.Forms.RadioButton();
			this.gbSaveCoding = new System.Windows.Forms.GroupBox();
			this.rbUTF16 = new System.Windows.Forms.RadioButton();
			this.rbUTF8 = new System.Windows.Forms.RadioButton();
			this.rbGB2312 = new System.Windows.Forms.RadioButton();
			this.gbImageOrder.SuspendLayout();
			this.gbSaveCoding.SuspendLayout();
			this.SuspendLayout();
			// 
			// lbInitSize
			// 
			this.lbInitSize.AutoSize = true;
			this.lbInitSize.Location = new System.Drawing.Point(12, 15);
			this.lbInitSize.Name = "lbInitSize";
			this.lbInitSize.Size = new System.Drawing.Size(63, 14);
			this.lbInitSize.TabIndex = 0;
			this.lbInitSize.Text = "初始大小";
			// 
			// cbbInitSize
			// 
			this.cbbInitSize.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.cbbInitSize.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbbInitSize.FormattingEnabled = true;
			this.cbbInitSize.Location = new System.Drawing.Point(81, 12);
			this.cbbInitSize.Name = "cbbInitSize";
			this.cbbInitSize.Size = new System.Drawing.Size(155, 22);
			this.cbbInitSize.TabIndex = 1;
			// 
			// bnOK
			// 
			this.bnOK.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.bnOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.bnOK.Location = new System.Drawing.Point(12, 226);
			this.bnOK.Name = "bnOK";
			this.bnOK.Size = new System.Drawing.Size(84, 33);
			this.bnOK.TabIndex = 4;
			this.bnOK.Text = "确定(&O)";
			this.bnOK.UseVisualStyleBackColor = true;
			// 
			// bnCancel
			// 
			this.bnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.bnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.bnCancel.Location = new System.Drawing.Point(152, 226);
			this.bnCancel.Name = "bnCancel";
			this.bnCancel.Size = new System.Drawing.Size(84, 33);
			this.bnCancel.TabIndex = 5;
			this.bnCancel.Text = "取消(&C)";
			this.bnCancel.UseVisualStyleBackColor = true;
			// 
			// gbImageOrder
			// 
			this.gbImageOrder.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.gbImageOrder.Controls.Add(this.rbImageOrder1);
			this.gbImageOrder.Controls.Add(this.rbImageOrder0);
			this.gbImageOrder.Location = new System.Drawing.Point(12, 40);
			this.gbImageOrder.Name = "gbImageOrder";
			this.gbImageOrder.Size = new System.Drawing.Size(223, 75);
			this.gbImageOrder.TabIndex = 2;
			this.gbImageOrder.TabStop = false;
			this.gbImageOrder.Text = "多态图坐标生成顺序";
			// 
			// rbImageOrder1
			// 
			this.rbImageOrder1.AutoSize = true;
			this.rbImageOrder1.Location = new System.Drawing.Point(6, 46);
			this.rbImageOrder1.Name = "rbImageOrder1";
			this.rbImageOrder1.Size = new System.Drawing.Size(193, 18);
			this.rbImageOrder1.TabIndex = 1;
			this.rbImageOrder1.TabStop = true;
			this.rbImageOrder1.Text = "无效,正常,焦点,按下,按焦";
			this.rbImageOrder1.UseVisualStyleBackColor = true;
			// 
			// rbImageOrder0
			// 
			this.rbImageOrder0.AutoSize = true;
			this.rbImageOrder0.Location = new System.Drawing.Point(6, 22);
			this.rbImageOrder0.Name = "rbImageOrder0";
			this.rbImageOrder0.Size = new System.Drawing.Size(193, 18);
			this.rbImageOrder0.TabIndex = 0;
			this.rbImageOrder0.TabStop = true;
			this.rbImageOrder0.Text = "正常,焦点,按下,无效,按焦";
			this.rbImageOrder0.UseVisualStyleBackColor = true;
			// 
			// gbSaveCoding
			// 
			this.gbSaveCoding.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.gbSaveCoding.Controls.Add(this.rbGB2312);
			this.gbSaveCoding.Controls.Add(this.rbUTF16);
			this.gbSaveCoding.Controls.Add(this.rbUTF8);
			this.gbSaveCoding.Location = new System.Drawing.Point(12, 121);
			this.gbSaveCoding.Name = "gbSaveCoding";
			this.gbSaveCoding.Size = new System.Drawing.Size(223, 94);
			this.gbSaveCoding.TabIndex = 3;
			this.gbSaveCoding.TabStop = false;
			this.gbSaveCoding.Text = "保存文件编码";
			// 
			// rbUTF16
			// 
			this.rbUTF16.AutoSize = true;
			this.rbUTF16.Location = new System.Drawing.Point(6, 46);
			this.rbUTF16.Name = "rbUTF16";
			this.rbUTF16.Size = new System.Drawing.Size(67, 18);
			this.rbUTF16.TabIndex = 1;
			this.rbUTF16.TabStop = true;
			this.rbUTF16.Text = "UTF-16";
			this.rbUTF16.UseVisualStyleBackColor = true;
			// 
			// rbUTF8
			// 
			this.rbUTF8.AutoSize = true;
			this.rbUTF8.Location = new System.Drawing.Point(6, 22);
			this.rbUTF8.Name = "rbUTF8";
			this.rbUTF8.Size = new System.Drawing.Size(60, 18);
			this.rbUTF8.TabIndex = 0;
			this.rbUTF8.TabStop = true;
			this.rbUTF8.Text = "UTF-8";
			this.rbUTF8.UseVisualStyleBackColor = true;
			// 
			// rbGB2312
			// 
			this.rbGB2312.AutoSize = true;
			this.rbGB2312.Location = new System.Drawing.Point(6, 70);
			this.rbGB2312.Name = "rbGB2312";
			this.rbGB2312.Size = new System.Drawing.Size(67, 18);
			this.rbGB2312.TabIndex = 2;
			this.rbGB2312.TabStop = true;
			this.rbGB2312.Text = "GB2312";
			this.rbGB2312.UseVisualStyleBackColor = true;
			// 
			// FormSetting
			// 
			this.AcceptButton = this.bnOK;
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.CancelButton = this.bnCancel;
			this.ClientSize = new System.Drawing.Size(248, 271);
			this.Controls.Add(this.gbSaveCoding);
			this.Controls.Add(this.gbImageOrder);
			this.Controls.Add(this.bnCancel);
			this.Controls.Add(this.bnOK);
			this.Controls.Add(this.cbbInitSize);
			this.Controls.Add(this.lbInitSize);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "FormSetting";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
			this.Text = "GUIEditor2 - 设置";
			this.gbImageOrder.ResumeLayout(false);
			this.gbImageOrder.PerformLayout();
			this.gbSaveCoding.ResumeLayout(false);
			this.gbSaveCoding.PerformLayout();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Label lbInitSize;
		private System.Windows.Forms.ComboBox cbbInitSize;
		private System.Windows.Forms.Button bnOK;
		private System.Windows.Forms.Button bnCancel;
		private System.Windows.Forms.GroupBox gbImageOrder;
		private System.Windows.Forms.RadioButton rbImageOrder1;
		private System.Windows.Forms.RadioButton rbImageOrder0;
		private System.Windows.Forms.GroupBox gbSaveCoding;
		private System.Windows.Forms.RadioButton rbUTF16;
		private System.Windows.Forms.RadioButton rbUTF8;
		private System.Windows.Forms.RadioButton rbGB2312;

	}
}