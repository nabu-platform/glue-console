package be.nabu.glue.console;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.annotations.GlueParam;
import be.nabu.glue.api.Lambda;
import be.nabu.glue.console.gui.ExtendedTableView;
import be.nabu.glue.impl.GlueUtils;
import be.nabu.glue.impl.methods.FileMethods;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "console")
public class GlueConsoleMethods {
	
	public static void clear() {
		GlueConsoleController.getInstance().clear();
	}
	
	public static Object popup(@GlueParam(name = "node") Node node, Stage target, String title) {
		Scene scene;
		Stage stage;
		if (target == null) {
			stage = new Stage();
			stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ESCAPE) {
						stage.hide();
						event.consume();
					}
				}
			});
			scene = new Scene(new AnchorPane());	
			stage.initOwner(GlueConsoleController.getInstance().getStage());
			stage.setScene(scene);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		else {
			stage = target;
			scene = stage.getScene();
		}
		((AnchorPane) scene.getRoot()).getChildren().clear();
		((AnchorPane) scene.getRoot()).getChildren().addAll(node);
		stage.show();
		node.requestFocus();
		return stage;
	}
	
	public static Object display(@GlueParam(name = "node") Node node, @GlueParam(name = "target") Tab target, @GlueParam(name = "title") String title) {
		if (title == null) {
			title = target == null ? "Anonymous" : target.getText();
		}
		if (node instanceof Chart) {
			title = ((Chart) node).getTitle();
		}
		ScrollPane scroll = new ScrollPane();
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setContent(node);
		if (target == null) {
			target = new Tab(title);
			target.setClosable(true);
			GlueConsoleController.getInstance().getTabVisual().getTabs().add(target);
		}
		else {
			target.setText(title);
		}
		if (node instanceof Pane) {
			if (((Pane) node).minHeightProperty().isBound()) {
				((Pane) node).minHeightProperty().unbind();
			}
			((Pane) node).minHeightProperty().bind(scroll.heightProperty().subtract(50));
		}
		target.setContent(scroll);
		GlueConsoleController.getInstance().getTabVisual().getSelectionModel().select(target);
		return target;
	}
	
	public static Object align(Pane container, Node...nodes) {
		container.getChildren().addAll(nodes);
		return container;
	}
	
	public static VBox valign(Node...nodes){
		VBox vbox = new VBox();
		vbox.getChildren().addAll(nodes);
		return vbox;
	}
	
	public static HBox halign(Node...nodes){
		HBox hbox = new HBox();
		hbox.getChildren().addAll(nodes);
		return hbox;
	}
	
	public static Button button(String title, final Lambda lambda) {
		Button button = new Button(title);
		button.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void handle(ActionEvent event) {
				GlueUtils.calculate(lambda, GlueConsoleController.getInstance().getRuntime(), new ArrayList());
			}
		});
		return button;
	}
	
	public static TextInputControl text(@GlueParam(name = "text") String text, @GlueParam(name = "rows") Integer rows, @GlueParam(name = "columns") Integer columns, @GlueParam(name = "prompt") String prompt) {
		if (rows == null) {
			 rows = text == null ? 1 : (text.length() - text.replace("\n", "").length() + 1);
		}
		if (columns == null) {
			columns = 30;
		}
		if (rows == 1) {
			TextField textField = new TextField(text);
			textField.setPromptText(prompt);
			textField.prefColumnCountProperty().set(columns);
			return textField;
		}
		else {
			TextArea area = new TextArea(text);
			area.setPromptText(prompt);
			area.prefColumnCountProperty().set(columns);
			area.prefRowCountProperty().set(rows);
			return area;
		}
	}
	
	public static Label label(@GlueParam(name = "text") String text) {
		return new Label(text);
	}
	
	public static CheckBox checkbox(@GlueParam(name = "text") String text, @GlueParam(name = "checked") Boolean checked) {
		CheckBox checkBox = new CheckBox(text);
		checkBox.setSelected(checked != null && checked);
		return checkBox;
	}
	
	@GlueMethod(description = "Disables a certain element or part of an element")
	public static void disable(Node node, Integer row, Integer column) {

	}
	
	@GlueMethod(description = "Enables a certain element or part of an element")
	public static void enable(Node node, Integer row, Integer column) {
		
	}
	
	@GlueMethod(description = "Sets the value of an element, the enumeration (e.g. listview) or part of an element (e.g. table pinpoint row/column)")
	public static void set(Node node, Object value, Integer row, Integer column) {
		
	}
	
	public static ExtendedTableView table(Iterable<?> columns, Iterable<?> rows, Lambda...aggregators) {
		return new ExtendedTableView(columns, rows, aggregators);
	}
	
	public static Node fxml(String content) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.load(new ByteArrayInputStream(content.getBytes("UTF-8")));
		return loader.getRoot();
	}
	
	public static void style(String...files) throws IOException {
		for (String file : files) {
			URI uri = FileMethods.uri(file);
			GlueConsoleController.getInstance().getStage().getScene().getStylesheets().add(uri.toString());
		}
	}
}
