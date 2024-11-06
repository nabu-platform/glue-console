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

package be.nabu.glue.console.plugins;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import be.nabu.glue.core.api.Lambda;
import be.nabu.glue.core.impl.GlueUtils;
import be.nabu.glue.utils.ScriptRuntime;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.TriangleMesh;

public class Plotter {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart.Series buildSeries(String name, Lambda lambda, int from, int to) {
		XYChart.Series series = new XYChart.Series();
		series.setName(name);
		ScriptRuntime runtime = ScriptRuntime.getRuntime();
		for (int x = from; x < to; x++) {
			series.getData().add(new XYChart.Data(x, GlueUtils.calculate(lambda, runtime, Arrays.asList(x))));
		}
		return series;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart.Series buildSeries(String name, Iterable<?> iterable) {
		XYChart.Series series = new XYChart.Series();
		series.setName(name);
		int counter = 0;
		for (Object entry : iterable) {
			Object x, y;
			if (entry instanceof Iterable) {
				Iterator iterator = ((Iterable) entry).iterator();
				x = iterator.hasNext() ? iterator.next() : 0;
				y = iterator.hasNext() ? iterator.next() : 0;
			}
			else {
				x = "" + counter++;
				y = entry;
			}
			series.getData().add(new XYChart.Data(x, y));
		}
		return series;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart buildLineChart(String labelX, String labelY, String title, XYChart.Series...series) {
		Axis xAxis = ((XYChart.Data) series[0].getData().get(0)).getXValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		Axis yAxis = ((XYChart.Data) series[0].getData().get(0)).getYValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		if (labelX != null) {
			xAxis.setLabel(labelX);
		}
		if (labelY != null) {
			yAxis.setLabel(labelY);
		}
		LineChart chart = new LineChart(xAxis, yAxis);
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Series entry : series) {
			chart.getData().add(entry);
		}
		return chart;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart buildAreaChart(String labelX, String labelY, String title, XYChart.Series...series) {
		Axis xAxis = ((XYChart.Data) series[0].getData().get(0)).getXValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		Axis yAxis = ((XYChart.Data) series[0].getData().get(0)).getYValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		if (labelX != null) {
			xAxis.setLabel(labelX);
		}
		if (labelY != null) {
			yAxis.setLabel(labelY);
		}
		AreaChart chart = new AreaChart(xAxis, yAxis);
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Series entry : series) {
			chart.getData().add(entry);
		}
		return chart;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart buildBubbleChart(String labelX, String labelY, String title, XYChart.Series...series) {
		Axis xAxis = ((XYChart.Data) series[0].getData().get(0)).getXValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		Axis yAxis = ((XYChart.Data) series[0].getData().get(0)).getYValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		if (labelX != null) {
			xAxis.setLabel(labelX);
		}
		if (labelY != null) {
			yAxis.setLabel(labelY);
		}
		BubbleChart chart = new BubbleChart(xAxis, yAxis);
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Series entry : series) {
			chart.getData().add(entry);
		}
		return chart;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart buildScatterChart(String labelX, String labelY, String title, XYChart.Series...series) {
		Axis xAxis = ((XYChart.Data) series[0].getData().get(0)).getXValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		Axis yAxis = ((XYChart.Data) series[0].getData().get(0)).getYValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		if (labelX != null) {
			xAxis.setLabel(labelX);
		}
		if (labelY != null) {
			yAxis.setLabel(labelY);
		}
		ScatterChart chart = new ScatterChart(xAxis, yAxis);
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Series entry : series) {
			chart.getData().add(entry);
		}
		return chart;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static XYChart buildBarChart(String labelX, String labelY, String title, XYChart.Series...series) {
		Axis xAxis = new CategoryAxis();
		Axis yAxis = ((XYChart.Data) series[0].getData().get(0)).getYValue() instanceof Number ? new NumberAxis() : new CategoryAxis();
		if (labelX != null) {
			xAxis.setLabel(labelX);
		}
		if (labelY != null) {
			yAxis.setLabel(labelY);
		}
		BarChart chart = new BarChart(xAxis, yAxis);
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Series entry : series) {
			chart.getData().add(entry);
		}
		return chart;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static PieChart buildPieChart(String title, XYChart.Series series) {
		PieChart chart = new PieChart();
		if (title != null) {
			chart.setTitle(title);
		}
		for (XYChart.Data data : (List<XYChart.Data>) series.getData()) {
			chart.getData().add(new PieChart.Data(data.getXValue().toString(), ((Number) data.getYValue()).doubleValue()));
		}
		return chart;
	}
	
	@SuppressWarnings("rawtypes")
	public static TriangleMesh buildMesh(Iterable<?> iterable) {
		TriangleMesh mesh = new TriangleMesh();
		int counter = 0;
		for (Object entry : iterable) {
			if (entry instanceof Iterable) {
				Iterator iterator = ((Iterable<?>) entry).iterator();
				mesh.getPoints().addAll(GlueUtils.convert(iterator.next(), Float.class), GlueUtils.convert(iterator.next(), Float.class), GlueUtils.convert(iterator.next(), Float.class));
			}
			else {
				mesh.getPoints().addAll(counter, GlueUtils.convert(entry, Float.class), counter);
			}
		}
		return mesh;
	}
}
