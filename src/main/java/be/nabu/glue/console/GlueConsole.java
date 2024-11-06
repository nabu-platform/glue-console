/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
		controller.setStage(stage);
		
		Parent root = loader.getRoot();
		Scene scene = new Scene(root);
		stage.initStyle(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.setTitle("Glue Console");
		stage.setMaximized(true);
		stage.setResizable(true);
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		controller.close();
		super.stop();
	}

}
