package com.pwrd.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class IO {
	private static IO instance = new IO();

	private BufferedReader in;
	private PrintWriter out;

	public static IO getInstance() {
		return instance;
	}

	private IO() {
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(new OutputStreamWriter(System.out));
		Runtime.getRuntime().addShutdownHook(new Thread("io.ShutdownHook") {
			@Override
			public void run() {
				try {
					if (in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (out != null)
					out.close();
			}
		});
	}

	public String readLine() throws IOException {
		return in.readLine();
	}

	public void print(boolean b) {
		out.print(b);
	}

	public void print(char c) {
		out.print(c);
	}

	public void print(char[] s) {
		out.print(s);
	}

	public void print(double d) {
		out.print(d);
	}

	public void print(float f) {
		out.print(f);
	}

	public void print(int i) {
		out.print(i);
	}

	public void print(long l) {
		out.print(l);
	}

	public void print(Object obj) {
		out.print(obj);
	}

	public void print(String s) {
		out.print(s);
	}

	public void println() {
		out.println();
	}

	public void println(boolean b) {
		out.println(b);
	}

	public void println(char c) {
		out.println(c);
	}

	public void println(char[] s) {
		out.println(s);
	}

	public void println(double d) {
		out.println(d);
	}

	public void println(float f) {
		out.println(f);
	}

	public void println(int i) {
		out.println(i);
	}

	public void println(long l) {
		out.println(l);
	}

	public void println(Object obj) {
		out.println(obj);
	}

	public void println(String s) {
		out.println(s);
	}

	public void flush() {
		out.flush();
	}

	public void setIn(InputStream is) {
		// if(in != null)
		// in.close();
		in = new BufferedReader(new InputStreamReader(is));
	}

	public void setIn(Reader r) {
		// if(in != null)
		// in.close();
		in = new BufferedReader(r);
	}

	public void setOut(OutputStream os) {
		// if(out != null)
		// out.close();
		out = new PrintWriter(new OutputStreamWriter(os));
	}

	public void setOut(Writer w) {
		// if(out != null)
		// out.close();
		out = new PrintWriter(w);
	}

	public void set(InputStream is, OutputStream os) {
		setIn(is);
		setOut(os);
	}

	public void set(Reader r, Writer w) {
		setIn(r);
		setOut(w);
	}

	public void closeIn() throws IOException {
		if (in != null) {
			in.close();
			in = null;
		}
	}

	public void closeOut() {
		if (out != null) {
			out.close();
			out = null;
		}
	}
}
