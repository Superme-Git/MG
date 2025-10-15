namespace GUIEditor2
{
	partial class CFormRenderImage
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

		#region 组件设计器生成的代码

		private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container();
			this.mnImage = new System.Windows.Forms.ContextMenuStrip(this.components);
			this.miScale1X = new System.Windows.Forms.ToolStripMenuItem();
			this.miScale2X = new System.Windows.Forms.ToolStripMenuItem();
			this.miScale4X = new System.Windows.Forms.ToolStripMenuItem();
			this.miScale8X = new System.Windows.Forms.ToolStripMenuItem();
			this.miSep0 = new System.Windows.Forms.ToolStripSeparator();
			this.miCancel = new System.Windows.Forms.ToolStripMenuItem();
			this.mnImage.SuspendLayout();
			this.SuspendLayout();
			// 
			// mnImage
			// 
			this.mnImage.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.miScale1X,
            this.miScale2X,
            this.miScale4X,
            this.miScale8X,
            this.miSep0,
            this.miCancel});
			this.mnImage.Name = "mnImage";
			this.mnImage.Size = new System.Drawing.Size(145, 120);
			// 
			// miScale1X
			// 
			this.miScale1X.Name = "miScale1X";
			this.miScale1X.Size = new System.Drawing.Size(144, 22);
			this.miScale1X.Text = "缩放&1X";
			this.miScale1X.Click += new System.EventHandler(this.miScale1X_Click);
			// 
			// miScale2X
			// 
			this.miScale2X.Name = "miScale2X";
			this.miScale2X.Size = new System.Drawing.Size(144, 22);
			this.miScale2X.Text = "缩放&2X";
			this.miScale2X.Click += new System.EventHandler(this.miScale2X_Click);
			// 
			// miScale4X
			// 
			this.miScale4X.Name = "miScale4X";
			this.miScale4X.Size = new System.Drawing.Size(144, 22);
			this.miScale4X.Text = "缩放&4X";
			this.miScale4X.Click += new System.EventHandler(this.miScale4X_Click);
			// 
			// miScale8X
			// 
			this.miScale8X.Name = "miScale8X";
			this.miScale8X.Size = new System.Drawing.Size(144, 22);
			this.miScale8X.Text = "缩放&8X";
			this.miScale8X.Click += new System.EventHandler(this.miScale8X_Click);
			// 
			// miSep0
			// 
			this.miSep0.Name = "miSep0";
			this.miSep0.Size = new System.Drawing.Size(141, 6);
			// 
			// miCancel
			// 
			this.miCancel.Name = "miCancel";
			this.miCancel.ShortcutKeyDisplayString = "";
			this.miCancel.Size = new System.Drawing.Size(144, 22);
			this.miCancel.Text = "取消(ESC)";
			// 
			// CFormRenderImage
			// 
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
			this.BackColor = System.Drawing.Color.Black;
			this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
			this.Margin = new System.Windows.Forms.Padding(0);
			this.Name = "CFormRenderImage";
			this.Size = new System.Drawing.Size(256, 256);
			this.mnImage.ResumeLayout(false);
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.ContextMenuStrip mnImage;
		private System.Windows.Forms.ToolStripMenuItem miScale1X;
		private System.Windows.Forms.ToolStripMenuItem miScale2X;
		private System.Windows.Forms.ToolStripMenuItem miScale4X;
		private System.Windows.Forms.ToolStripMenuItem miScale8X;
		private System.Windows.Forms.ToolStripSeparator miSep0;
		private System.Windows.Forms.ToolStripMenuItem miCancel;

	}
}
