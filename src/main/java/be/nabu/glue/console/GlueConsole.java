package be.nabu.glue.console;

import java.io.IOException;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GlueConsole extends Application {

	private GlueConsoleController controller;
	private static String[] arguments;

	public static void main(String...arguments) throws IOException, URISyntaxException {
		GlueConsole.arguments = arguments;
		if (System.getProperty("version") == null) {
			System.setProperty("version", "2");
		}
		launch(arguments);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("console.fxml"));
		loader.load();
		
		controller = loader.getController();
		controller.load(arguments);
		
		Parent root = loader.getRoot();
		Scene scene = new Scene(root);
		stage.initStyle(StageStyle.DECORATED);
//		scene.getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Glue Console");
		stage.setMaximized(true);
		stage.setResizable(true);
//		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.png");
//		try {
//			stage.getIcons().add(new Image(stream));
//		}
//		finally {
//			stream.close();
//		}
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		controller.close();
		super.stop();
	}

}
