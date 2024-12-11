package io.github.pixeldev.integral.view;

import io.github.pixeldev.integral.model.FunctionModel;
import org.math.plot.Plot2DPanel;

import javax.swing.*;

public class PanelGraphics extends JPanel {
    private final Plot2DPanel plot;

    public PanelGraphics(double mu, double sigma) {
        double[] x = new double[100];
        double[] y = new double[100];
        FunctionModel normal = new FunctionModel(mu, sigma);
        for (int i = 0; i < 100; i++) {
            x[i] = i / 10.0 - 5.0;
            y[i] = normal.firstIntegral(x[i]);
        }
        plot = new Plot2DPanel();
        plot.addLinePlot("y = x^2", x, y);
    }

    public Plot2DPanel getPlot() {
        return plot;
    }
}