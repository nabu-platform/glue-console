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
