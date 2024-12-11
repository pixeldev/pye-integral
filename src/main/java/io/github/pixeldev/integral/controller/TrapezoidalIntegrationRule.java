package io.github.pixeldev.integral.controller;

import io.github.pixeldev.integral.model.FunctionModel;

public class TrapezoidalIntegrationRule implements IntegrationRule {
    private double n = 100;

    @Override
    public double calculateArea(double a, double b, double mu, double sigma) {
        double delta, area, xi;
        FunctionModel normal = new FunctionModel(mu, sigma);
        area = 0;
        delta = (b - a) / n;

        for (int i = 1; i < n; i++) {
            xi = a + i * delta;
            area += normal.firstIntegral(xi);
        }

        xi = (normal.firstIntegral(a) + normal.firstIntegral(b)) / 2;
        area += xi;
        area = area * delta;

        return area;
    }

    @Override
    public double calculateArea(double a, double b) {
        double delta, area, xi;
        FunctionModel normal = new FunctionModel();
        area = 0;
        delta = (b - a) / n;

        for (int i = 1; i < n; i++) {
            xi = a + i * delta;
            area += normal.secondIntegral(xi);
        }

        xi = (normal.secondIntegral(a) + normal.secondIntegral(b)) / 2;
        area += xi;
        area = area * delta;

        return area;
    }

    public void setN(double n) {
        this.n = n;
    }
}
