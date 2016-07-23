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
