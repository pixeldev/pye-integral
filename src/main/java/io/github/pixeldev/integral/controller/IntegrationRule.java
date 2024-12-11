package io.github.pixeldev.integral.controller;

public interface IntegrationRule {
    double calculateArea(double a, double b, double mu, double sigma);

    double calculateArea(double a, double b);
}