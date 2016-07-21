package be.nabu.glue.console;

import java.io.IOException;
import java.io.Writer;

import be.nabu.jfx.control.ace.AceEditor;
import javafx.application.Platform;

public class AceEditorWriter extends Writer {

	private AceEditor aceEditor;

	public AceEditorWriter(AceEditor aceEditor) {
		this.aceEditor = aceEditor;
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		final String string = new String(cbuf, off, len);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				aceEditor.append(string);
//				textarea.setScrollTop(Double.MAX_VALUE);
			}
		});
	}

	@Override
	public void flush() throws IOException {
		// do nothing
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}
}
