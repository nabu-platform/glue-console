package be.nabu.glue.console;

import java.util.List;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import be.nabu.glue.annotations.GlueParam;
import be.nabu.glue.console.plugins.Plotter;
import be.nabu.glue.impl.GlueUtils;
import be.nabu.glue.impl.methods.v2.SeriesMethods;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "chart")
public class ChartMethods {
	
	@SuppressWarnings("rawtypes")
	public static XYChart line(@GlueParam(name = "title") String title, @GlueParam(name = "x") String x, @GlueParam(name = "y") String y, @GlueParam(name = "plot") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildLineChart(x, y, title, resolved.toArray(new Series[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static XYChart scatter(@GlueParam(name = "title") String title, @GlueParam(name = "x") String x, @GlueParam(name = "y") String y, @GlueParam(name = "plot") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildScatterChart(x, y, title, resolved.toArray(new Series[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static XYChart area(@GlueParam(name = "title") String title, @GlueParam(name = "x") String x, @GlueParam(name = "y") String y, @GlueParam(name = "plot") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildAreaChart(x, y, title, resolved.toArray(new Series[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static XYChart bubble(@GlueParam(name = "title") String title, @GlueParam(name = "x") String x, @GlueParam(name = "y") String y, @GlueParam(name = "plot") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildBubbleChart(x, y, title, resolved.toArray(new Series[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static XYChart bar(@GlueParam(name = "title") String title, @GlueParam(name = "x") String x, @GlueParam(name = "y") String y, @GlueParam(name = "plot") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildBarChart(x, y, title, resolved.toArray(new Series[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static PieChart pie(@GlueParam(name = "title") String title, @GlueParam(name = "plot") Series plot) {
		return Plotter.buildPieChart(title, plot);
	}
	
	@SuppressWarnings("rawtypes")
	public static Series plot(@GlueParam(name = "name") String name, @GlueParam(name = "data") Object...objects) {
		List<?> resolved = SeriesMethods.resolve(GlueUtils.toSeries(objects));
		return Plotter.buildSeries(name, resolved);
	}
}
