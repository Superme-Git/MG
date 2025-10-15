namespace GUIEditor2
{
	partial class FormScrollBarApply
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
			this.tbLog = new System.Windows.Forms.TextBox();
			this.bnAnalyse = new System.Windows.Forms.Button();
			this.bnUpdate = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// tbLog
			// 
			this.tbLog.Location = new System.Drawing.Point(12, 12);
			this.tbLog.Multiline = true;
			this.tbLog.Name = "tbLog";
			this.tbLog.ReadOnly = true;
			this.tbLog.ScrollBars = System.Windows.Forms.ScrollBars.Both;
			this.tbLog.Size = new System.Drawing.Size(268, 202);
			this.tbLog.TabIndex = 0;
			this.tbLog.Text = "点击分析或更新会搜索程序目录\r\n及子目录下所有的界面文件.\r\n分析操作只做搜索,不会改写这些文件.\r\n更新操作无法恢复,请小心执行.";
			this.tbLog.WordWrap = false;
			// 
			// bnAnalyse
			// 
			this.bnAnalyse.Location = new System.Drawing.Point(12, 220);
			this.bnAnalyse.Name = "bnAnalyse";
			this.bnAnalyse.Size = new System.Drawing.Size(84, 31);
			this.bnAnalyse.TabIndex = 1;
			this.bnAnalyse.Text = "分析(&A)";
			this.bnAnalyse.UseVisualStyleBackColor = true;
			this.bnAnalyse.Click += new System.EventHandler(this.bnAnalyse_Click);
			// 
			// bnUpdate
			// 
			this.bnUpdate.Location = new System.Drawing.Point(196, 220);
			this.bnUpdate.Name = "bnUpdate";
			this.bnUpdate.Size = new System.Drawing.Size(84, 31);
			this.bnUpdate.TabIndex = 1;
			this.bnUpdate.Text = "更新(&U)";
			this.bnUpdate.UseVisualStyleBackColor = true;
			this.bnUpdate.Click += new System.EventHandler(this.bnUpdate_Click);
			// 
			// FormScrollBarApply
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(108F, 108F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
			this.ClientSize = new System.Drawing.Size(292, 259);
			this.Controls.Add(this.bnUpdate);
			this.Controls.Add(this.bnAnalyse);
			this.Controls.Add(this.tbLog);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "FormScrollBarApply";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
			this.Text = "滚动条应用到所有界面";
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.TextBox tbLog;
		private System.Windows.Forms.Button bnAnalyse;
		private System.Windows.Forms.Button bnUpdate;
	}
}
