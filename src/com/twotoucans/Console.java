package com.twotoucans;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class Console {
	private PrintStream consolePrintStream;
	private ByteArrayOutputStream consoleStream;
	private JTextArea consoleView;
	
	public Console(JTextArea v) {
		consoleStream = new ByteArrayOutputStream();
		consolePrintStream = new PrintStream(consoleStream);
		consoleView = v;
	}
	public <T> void print(T txt) {
		consolePrintStream.print(txt);
		consoleView.setText(consoleStream.toString());
		consoleView.invalidate();
	}
	public <T> void println(T txt) {
		consolePrintStream.println(txt);
		consoleView.setText(consoleStream.toString());
		consoleView.invalidate();
	}
}