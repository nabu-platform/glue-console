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

import javafx.scene.control.TextInputControl;
import be.nabu.glue.api.ExecutionContext;
import be.nabu.libs.evaluator.EvaluationException;
import be.nabu.libs.evaluator.QueryPart;
import be.nabu.libs.evaluator.QueryPart.Type;
import be.nabu.libs.evaluator.api.operations.OperationExecutor;
import be.nabu.libs.evaluator.impl.ClassicOperation;

public class ControlExecutor implements OperationExecutor {

	@Override
	public boolean support(Object leftOperand, Type operator, Object rightOperand) {
		return (leftOperand instanceof TextInputControl || rightOperand instanceof TextInputControl) && (operator != QueryPart.Type.EQUALS && operator != QueryPart.Type.NOT_EQUALS && operator != QueryPart.Type.NOT);
	}

	@Override
	public Object calculate(Object leftOperand, Type operator, Object rightOperand) {
		ClassicOperation<ExecutionContext> classic = new ClassicOperation<ExecutionContext>();
		classic.getParts().add(new QueryPart(Type.STRING, leftOperand instanceof TextInputControl ? ((TextInputControl) leftOperand).getText() : leftOperand));
		classic.getParts().add(new QueryPart(operator, operator.toString()));
		classic.getParts().add(new QueryPart(Type.STRING, rightOperand instanceof TextInputControl ? ((TextInputControl) rightOperand).getText() : rightOperand));
		try {
			return classic.evaluate(null);
		}
		catch (EvaluationException e) {
			throw new RuntimeException(e);
		}
	}

}
