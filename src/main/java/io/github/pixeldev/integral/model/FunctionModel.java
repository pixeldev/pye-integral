package io.github.pixeldev.integral.model;

public class FunctionModel {
    private final double mu;
    private final double sigma;

    public FunctionModel(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    public FunctionModel() {
        this.mu = 0;
        this.sigma = 0;
    }

    public double firstIntegral(double x) {
        double y, arg, aux;
        aux = (x - mu) / sigma;
        arg = -0.5 * aux * aux;
        y = ((1.0 / (sigma * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, arg));
        return y;
    }

    public double secondIntegral(double x) {
        double y, arg;
        arg = -0.5 * x * x;
        y = ((1 / (Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, arg));
        return y;
    }
}