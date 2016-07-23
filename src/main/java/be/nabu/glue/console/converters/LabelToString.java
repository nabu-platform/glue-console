package be.nabu.glue.console.converters;

import javafx.scene.control.Label;
import be.nabu.libs.converter.api.ConverterProvider;

public class LabelToString implements ConverterProvider<Label, String> {

	@Override
	public String convert(Label instance) {
		return instance == null ? null : (instance.getText() == null ? "" : instance.getText());
	}

	@Override
	public Class<Label> getSourceClass() {
		return Label.class;
	}

	@Override
	public Class<String> getTargetClass() {
		return String.class;
	}

}