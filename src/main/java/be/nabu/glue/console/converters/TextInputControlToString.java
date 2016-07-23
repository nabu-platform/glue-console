package be.nabu.glue.console.converters;

import javafx.scene.control.TextInputControl;
import be.nabu.libs.converter.api.ConverterProvider;

public class TextInputControlToString implements ConverterProvider<TextInputControl, String> {

	@Override
	public String convert(TextInputControl instance) {
		return instance == null ? null : (instance.getText() == null ? "" : instance.getText());
	}

	@Override
	public Class<TextInputControl> getSourceClass() {
		return TextInputControl.class;
	}

	@Override
	public Class<String> getTargetClass() {
		return String.class;
	}

}
