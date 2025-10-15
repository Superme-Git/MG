using System;

namespace MEngineTest
{
	public class TestApp : MEngine.IApp
	{
		private MEngine.Engine m_engine;
		private MEngine.IRenderer m_renderer;

		public override bool OnInit()
		{
			m_engine = MEngine.Engine.GetEngine();
			m_renderer = m_engine.GetRenderer();

			return true;
		}

		public override void OnIdle()
		{
		}

		public override bool OnExit()
		{
			return true;
		}

		public override bool OnBeforeRender()
		{
			return true;
		}

		public override void OnPostRender()
		{
			m_renderer.DrawBox(100, 100, 300, 200, System.Drawing.Color.FromArgb(0, 0, 255), 1);

			m_renderer.DrawLine(150, 150, 200, 300, System.Drawing.Color.FromArgb(0, 0, 0));

			m_renderer.DrawTriangle(400, 100, 300, 200, 500, 400, System.Drawing.Color.FromArgb(255, 0, 0));
		}
		
		public override bool OnWindowsMessage(IntPtr hwnd, UInt32 msg, UInt32 wParam, UInt32 lParam)
		{
			return false;
		}
	}

	static class Program
	{
		[STAThread]
		static void Main()
		{
			MEngine.Engine.GetEngine().Run(new MEngine.EngineParameter("MEngineClass", "MEngineTitle", 800, 600, new TestApp()));
			MEngine.Engine.GetEngine().Exit();
		}
	}
}
