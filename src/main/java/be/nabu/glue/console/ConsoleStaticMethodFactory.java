package be.nabu.glue.console;

import java.util.ArrayList;
import java.util.List;

import be.nabu.glue.api.StaticMethodFactory;

public class ConsoleStaticMethodFactory implements StaticMethodFactory {

	@Override
	public List<Class<?>> getStaticMethodClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(GlueConsoleMethods.class);
		classes.add(ChartMethods.class);
		classes.add(SnippetMethods.class);
		return classes;
	}

}
