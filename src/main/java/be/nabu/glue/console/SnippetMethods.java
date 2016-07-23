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
import be.nabu.glue.ScriptUtils;
import be.nabu.glue.api.Script;
import be.nabu.glue.repositories.ResourceScript;
import be.nabu.jfx.control.ace.AceEditor;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ManageableContainer;
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
	
	public static void edit(String fullName) throws IOException {
		String currentSource = null;
		final Resource resource;
		final Script script = GlueConsoleController.getInstance().getSnippets().getScript(fullName);
		if (script != null) {
			InputStream source = script.getSource();
			try {
				byte[] bytes = IOUtils.toBytes(IOUtils.wrap(source));
				currentSource = new String(bytes, "UTF-8");
			}
			finally {
				source.close();
			}
			if (script instanceof ResourceScript) {
				resource = ((ResourceScript) script).getResource();
			}
			else {
				resource = null;
			}
		}
		else {
			currentSource = "";
			resource = null;
		}
		VBox vbox = new VBox();
		AceEditor editor = new AceEditor();
		editor.setReadOnly(resource == null && script != null);
		editor.setContent("text/x-glue", currentSource);
		if (resource != null || script == null) {
			editor.subscribe(AceEditor.SAVE, new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					try {
						Resource target = resource == null ? ResourceUtils.touch(GlueConsoleController.getInstance().getSnippets().getRoot(), fullName.replace('.', '/') + ".glue") : resource;
						if (target != null) {
							WritableContainer<ByteBuffer> writable = ((WritableResource) target).getWritable();
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
					}
					catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		HBox buttons = new HBox();
		if (resource != null) {
			Button delete = new Button("Delete");
			delete.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					ResourceContainer<?> parent = resource.getParent();
					if (parent instanceof ManageableContainer) {
						try {
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
		}
		vbox.getChildren().addAll(buttons, editor.getWebView());
		Tab tab = new Tab(fullName);
		tab.setContent(vbox);
		GlueConsoleController.getInstance().getTabVisual().getTabs().add(tab);
		GlueConsoleController.getInstance().getTabVisual().getSelectionModel().select(tab);
	}
	

}
