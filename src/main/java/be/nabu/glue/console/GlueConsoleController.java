package be.nabu.glue.console;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import be.nabu.glue.DynamicScript;
import be.nabu.glue.Main;
import be.nabu.glue.MultipleRepository;
import be.nabu.glue.ScriptRuntime;
import be.nabu.glue.api.ExecutorGroup;
import be.nabu.glue.api.Parser;
import be.nabu.glue.impl.EnvironmentLabelEvaluator;
import be.nabu.glue.impl.SimpleExecutionEnvironment;
import be.nabu.glue.impl.formatters.SimpleOutputFormatter;
import be.nabu.glue.impl.parsers.GlueParserProvider;
import be.nabu.jfx.control.ace.AceEditor;

public class GlueConsoleController implements Closeable, Initializable {

	private static GlueConsoleController instance;
	
	@FXML
	private MenuItem mniClose;
	
	private AceEditor history, current, log;
	
	@FXML
	private Menu mnuFile;
	
	@FXML
	private TabPane tabVisual;
	
	@FXML
	private VBox vbxScript;

	private ScriptRuntime runtime;
	
	private Parser parser;

	@Override
	public void close() throws IOException {
		// nothing yet
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		history = new AceEditor();
		current = new AceEditor();
		history.setContent("text/x-glue", "");
		current.setContent("text/x-glue", "");
		
		current.getWebView().prefHeight(350);
		current.getWebView().maxHeight(350);
		
		log = new AceEditor();
		log.setContent("text/plain", "");
		log.setReadOnly(true);

		Tab tab = new Tab("Log");
		tab.setContent(log.getWebView());
		tab.setClosable(false);
		tabVisual.getTabs().add(tab);
		
		vbxScript.getChildren().addAll(current.getWebView(), history.getWebView());
		VBox.setVgrow(log.getWebView(), Priority.NEVER);
		VBox.setVgrow(history.getWebView(), Priority.ALWAYS);
		VBox.setVgrow(current.getWebView(), Priority.NEVER);
		history.setReadOnly(true);
		
		current.setKeyCombination("commit", new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN));
		current.subscribe("commit", new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				String content = current.getContent();
				try {
					ExecutorGroup parsed = parser.parse(new StringReader(content));
					parsed.execute(runtime.getExecutionContext());
					history.setContent("text/x-glue", history.getContent() + content + "\n");
					current.setContent("text/x-glue", "");
				} 
				catch (Exception e) {
					StringWriter writer = new StringWriter();
					PrintWriter printer = new PrintWriter(writer);
					e.printStackTrace(printer);
					runtime.getFormatter().print(writer.toString());
				}
				// if we don't run it later, the focus will switch and the key up event of the enter is still recorded by the editor
				Platform.runLater(new Runnable() {
					public void run() {
						current.requestFocus();
					}
				});
				event.consume();
			}
		});
	}
	
	public void load(String...arguments) throws IOException, URISyntaxException {
		Charset charset = Main.getCharset(arguments);
		MultipleRepository repository = Main.buildRepository(charset, arguments);
		SimpleExecutionEnvironment environment = new SimpleExecutionEnvironment(Main.getEnvironmentName(arguments));
		Main.setArguments(environment, arguments);
		parser = new GlueParserProvider().newParser(repository, "console.glue");
		
		runtime = new ScriptRuntime(new DynamicScript(repository, parser), environment, false, null);
		runtime.setLabelEvaluator(new EnvironmentLabelEvaluator(Main.getLabel(arguments)));
		runtime.setFormatter(new SimpleOutputFormatter(new AceEditorWriter(log)));
		runtime.registerInThread();
	}
	
	public void clear() {
		Platform.runLater(new Runnable() {
			public void run() {
				runtime.getExecutionContext().getPipeline().clear();
				current.setContent("text/x-glue", "");
				history.setContent("text/x-glue", "");
				log.setContent("text/x-glue", "");
				current.requestFocus();
			}
		});
	}
	
	public static GlueConsoleController getInstance() {
		return instance;
	}

	public TabPane getTabVisual() {
		return tabVisual;
	}

	public ScriptRuntime getRuntime() {
		return runtime;
	}
	
}
