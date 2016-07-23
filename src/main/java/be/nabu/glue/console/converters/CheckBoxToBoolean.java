package be.nabu.glue.console.converters;

import javafx.scene.control.CheckBox;
import be.nabu.libs.converter.api.ConverterProvider;

public class CheckBoxToBoolean implements ConverterProvider<CheckBox, Boolean> {

	@Override
	public Boolean convert(CheckBox instance) {
		return instance != null && instance.isSelected();
	}

	@Override
	public Class<CheckBox> getSourceClass() {
		return CheckBox.class;
	}

	@Override
	public Class<Boolean> getTargetClass() {
		return Boolean.class;
	}

}
