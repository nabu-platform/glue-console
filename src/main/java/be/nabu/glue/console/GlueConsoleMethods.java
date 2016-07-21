package be.nabu.glue.console;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import be.nabu.glue.api.Lambda;
import be.nabu.glue.impl.GlueUtils;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "console")
public class GlueConsoleMethods {
	
	public static void clear() {
		GlueConsoleController.getInstance().clear();
	}
	
	public static Object display(Node node, Tab target) {
		String title = target == null ? "Anonymous" : target.getText();
		if (node instanceof Chart) {
			title = ((Chart) node).getTitle();
		}
		ScrollPane scroll = new ScrollPane();
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setContent(node);
		if (target == null) {
			target = new Tab(title);
		}
		else {
			target.setText(title);
		}
		target.setContent(scroll);
		target.setClosable(true);
		GlueConsoleController.getInstance().getTabVisual().getTabs().add(target);
		GlueConsoleController.getInstance().getTabVisual().getSelectionModel().select(target);
		return target;
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
}
