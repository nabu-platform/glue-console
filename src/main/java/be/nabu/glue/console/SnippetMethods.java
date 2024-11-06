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
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import be.nabu.glue.api.Script;
import be.nabu.glue.core.repositories.ResourceScript;
import be.nabu.glue.utils.ScriptUtils;
import be.nabu.jfx.control.ace.AceEditor;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.WritableResource;
import be.nabu.utils.io.IOUtils;
import be.nabu.utils.io.api.ByteBuffer;
import be.nabu.utils.io.api.WritableContainer;

@MethodProviderClass(namespace = "snippet")
public class SnippetMethods {

	public static Iterable<Script> snippets() {
		return GlueConsoleController.getInstance().getSnippets();
	}
	
	public static Tab edit(String fullName, String resourceToEdit) throws IOException {
		final Resource resource;
		final Script script = GlueConsoleController.getInstance().getSnippets().getScript(fullName);
		
		if (resourceToEdit != null) {
			if (script == null) {
				throw new IOException("Could not find script: " + fullName);
			}
			else if (!(script instanceof ResourceScript)) {
				throw new IOException("Can not edit script: " + fullName);
			}
			else {
				ResourceContainer<?> parent = ((ResourceScript) script).getResource().getParent();
				resource = ResourceUtils.touch(parent, script.getName() + "/" + resourceToEdit);
			}
		}
		else {
			if (script == null) {
				resource = ResourceUtils.touch(GlueConsoleController.getInstance().getSnippets().getRoot(), fullName.replace('.', '/') + ".glue");
			}
			else if (!(script instanceof ResourceScript)) {
				throw new IOException("Can not edit script: " + fullName);
			}
			else {
				resource = ((ResourceScript) script).getResource();
			}
		}
		String currentSource = null;
		if (resource != null) {
			InputStream source = IOUtils.toInputStream(((ReadableResource) resource).getReadable());
			try {
				byte[] bytes = IOUtils.toBytes(IOUtils.wrap(source));
				currentSource = new String(bytes, "UTF-8");
			}
			finally {
				source.close();
			}
		}
		else {
			currentSource = "";
		}
		VBox vbox = new VBox();
		AceEditor editor = new AceEditor();
		System.out.println(resource + " = " + resource.getContentType());
		editor.setContent(resource.getContentType(), currentSource);
		editor.subscribe(AceEditor.SAVE, new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				try {
					WritableContainer<ByteBuffer> writable = ((WritableResource) resource).getWritable();
					try {
						writable.write(IOUtils.wrap(editor.getContent().getBytes("UTF-8"), true));
						ScriptUtils.getRoot(GlueConsoleController.getInstance().getSnippets()).refresh();
						// if you update the init script, refresh it
						if (fullName.equals(GlueConsoleController.getInstance().getInitialScriptName())) {
							GlueConsoleController.getInstance().resetInitial();
						}
					}
					finally {
						writable.close();
					}
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		HBox buttons = new HBox();
		Button delete = new Button("Delete");
		delete.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ResourceContainer<?> parent = resource.getParent();
				if (parent instanceof ManageableContainer) {
					try {
						// if it's a script, also remove the resource
						if (script != null && resourceToEdit == null) {
							((ManageableContainer<?>) parent).delete(script.getName());
						}
						((ManageableContainer<?>) parent).delete(resource.getName());
						ScriptUtils.getRoot(GlueConsoleController.getInstance().getSnippets()).refresh();
					}
					catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		buttons.getChildren().addAll(delete);
		vbox.getChildren().addAll(buttons, editor.getWebView());
		Tab tab = new Tab(fullName);
		tab.setContent(vbox);
		GlueConsoleController.getInstance().getTabVisual().getTabs().add(tab);
		GlueConsoleController.getInstance().getTabVisual().getSelectionModel().select(tab);
		return tab;
	}
	

}
