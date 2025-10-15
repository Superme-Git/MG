using System;
using System.Windows.Forms;

namespace GUIEditor2
{
	static class Program
	{
		// private const string VERSION = "1.3.6.5"; 
        private const string VERSION = "2.0.0.0"; 

		private static CFormMain ms_formmain;

		public static string GetVersion()
		{
			return VERSION;
		}

		public static CFormMain GetMainForm()
		{
			return ms_formmain;
		}

		[STAThread]
		static void Main(string[] args)
		{
            // Environment.CurrentDirectory = 
			// System.IO.Directory.SetCurrentDirectory(Application.StartupPath);
			Application.EnableVisualStyles();

            // if defaultValue is true, use GDI+ to render the text,
            // otherwise,  use GDI to render the text.
			Application.SetCompatibleTextRenderingDefault(false);

			string filename = (args.Length > 0 ? args[0] : null);
			Application.Run(ms_formmain = new CFormMain(filename));
		}
	}
}
