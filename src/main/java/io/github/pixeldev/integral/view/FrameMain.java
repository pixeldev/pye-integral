package io.github.pixeldev.integral.view;

import io.github.pixeldev.integral.controller.IntegrationRule;
import io.github.pixeldev.integral.controller.TrapezoidalIntegrationRule;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FrameMain extends JFrame {
    private JPanel westPanel;
    private JPanel centerPanel;
    private JPanel inputPanel;
    private JTextField valorA;
    private JTextField valorB;
    private JTextField valorSigma;
    private JTextField valorMu;
    private JTextField resultadoArea;
    private JComboBox<String> listOption;
    private JButton btnProcesar;
    private JButton btnGenerarPDF;
    private boolean isFirst;
    private IntegrationRule integrationService;

    public FrameMain() {
        super("Integración Numérica");
        this.integrationService = new TrapezoidalIntegrationRule();
        initializeUI();
    }

    private void initializeUI() {
        // Configuración del JFrame
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int height = pantalla.height;
        int width = pantalla.width;
        setSize(width - 300, height - 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Inicialización de Paneles
        westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout(10, 10));
        westPanel.setPreferredSize(new Dimension(300, 0));
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        westPanel.setBackground(Color.WHITE);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de Opciones (Parte Superior del Panel Oeste)
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout(5, 5));
        optionsPanel.setBackground(Color.WHITE);

        JLabel lblOption = new JLabel("Seleccione la Integral:");
        listOption = new JComboBox<>(new String[]{"Primera Integral", "Segunda Integral"});
        listOption.setSelectedIndex(0);

        optionsPanel.add(lblOption, BorderLayout.NORTH);
        optionsPanel.add(listOption, BorderLayout.CENTER);

        // Panel de Entrada (Parte Central del Panel Oeste)
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Parámetros"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo de Entrada para 'a'
        JLabel lblA = new JLabel("Valor de a:");
        valorA = new JTextField("0", 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblA, gbc);
        gbc.gridx = 1;
        inputPanel.add(valorA, gbc);

        // Campo de Entrada para 'b'
        JLabel lblB = new JLabel("Valor de b:");
        valorB = new JTextField("0", 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblB, gbc);
        gbc.gridx = 1;
        inputPanel.add(valorB, gbc);

        // Campo de Entrada para 'σ'
        JLabel lblSigma = new JLabel("Valor de σ:");
        valorSigma = new JTextField("0", 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblSigma, gbc);
        gbc.gridx = 1;
        inputPanel.add(valorSigma, gbc);

        // Campo de Entrada para 'μ'
        JLabel lblMu = new JLabel("Valor de μ:");
        valorMu = new JTextField("0", 10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblMu, gbc);
        gbc.gridx = 1;
        inputPanel.add(valorMu, gbc);

        // Panel de Botones (Parte Inferior del Panel Oeste)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(Color.WHITE);

        btnProcesar = new JButton("Aceptar");
        btnGenerarPDF = new JButton("Generar PDF");
        btnGenerarPDF.setVisible(false); // Solo visible cuando se muestra una tabla

        buttonsPanel.add(btnProcesar);
        buttonsPanel.add(btnGenerarPDF);

        // Agregar Subpaneles al Panel Oeste
        westPanel.add(optionsPanel, BorderLayout.NORTH);
        westPanel.add(inputPanel, BorderLayout.CENTER);
        westPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Agregar Paneles al JFrame
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Inicializar Listeners
        initializeListeners();

        // Configurar Visibilidad Inicial
        setVisible(true);
    }

    private void initializeListeners() {
        // Acción del Botón "Aceptar"
        btnProcesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarAccion();
            }
        });

        // Acción del Botón "Generar PDF"
        btnGenerarPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarPDFAccion();
            }
        });

        // Acción del ComboBox de Opciones
        listOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVista();
            }
        });
    }

    private void procesarAccion() {
        double sigma, mu;
        String aInput, bInput;
        try {
            aInput = valorA.getText().trim();
            bInput = valorB.getText().trim();
            sigma = Double.parseDouble(valorSigma.getText());
            mu = Double.parseDouble(valorMu.getText());
            String selectedItem = (String) listOption.getSelectedItem();

            if (selectedItem.equals("Primera Integral")) {
                mostrarGrafico(aInput, bInput, mu, sigma);
            } else {
                if (!bInput.equalsIgnoreCase("INF")) {
                    double b = Double.parseDouble(bInput);
                    mostrarTabla(b);
                } else {
                    JOptionPane.showMessageDialog(this, "Ingrese un valor válido para 'b'.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            String mensaje = "Error de formato en los siguientes campos: ";
            if (!esNumero(valorSigma.getText())) {
                mensaje += " σ";
            }
            if (!esNumero(valorMu.getText())) {
                mensaje += " μ";
            }
            if (!esNumero(valorA.getText())) {
                mensaje += " a";
            }
            if (!esNumero(valorB.getText()) && !valorB.getText().equalsIgnoreCase("INF")) {
                mensaje += " b";
            }
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarPDFAccion() {
        double bInicial = -6.0;
        double bFinal;
        try {
            bFinal = Double.parseDouble(valorB.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido para 'b'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double incremento = 0.01;
        String dest = "output.pdf";
        List<List<Double[]>> tablesData = calculateTablesData(bInicial, bFinal, incremento);
        try {
            generatePdfWithTables(dest, tablesData);
            JOptionPane.showMessageDialog(this, "PDF generado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVista() {
        String selectedItem = (String) listOption.getSelectedItem();
        if (selectedItem.equals("Primera Integral")) {
            // Mostrar campos a, b, sigma, mu
            inputPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblA = new JLabel("Valor de a:");
            valorA = new JTextField("0", 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(lblA, gbc);
            gbc.gridx = 1;
            inputPanel.add(valorA, gbc);

            JLabel lblB = new JLabel("Valor de b:");
            valorB = new JTextField("0", 10);
            gbc.gridx = 0;
            gbc.gridy = 1;
            inputPanel.add(lblB, gbc);
            gbc.gridx = 1;
            inputPanel.add(valorB, gbc);

            JLabel lblSigma = new JLabel("Valor de σ:");
            valorSigma = new JTextField("0", 10);
            gbc.gridx = 0;
            gbc.gridy = 2;
            inputPanel.add(lblSigma, gbc);
            gbc.gridx = 1;
            inputPanel.add(valorSigma, gbc);

            JLabel lblMu = new JLabel("Valor de μ:");
            valorMu = new JTextField("0", 10);
            gbc.gridx = 0;
            gbc.gridy = 3;
            inputPanel.add(lblMu, gbc);
            gbc.gridx = 1;
            inputPanel.add(valorMu, gbc);

            btnGenerarPDF.setVisible(false);
        } else {
            // Mostrar solo campo b
            inputPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblB = new JLabel("Valor de b:");
            valorB = new JTextField("0", 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(lblB, gbc);
            gbc.gridx = 1;
            inputPanel.add(valorB, gbc);

            btnGenerarPDF.setVisible(true);
        }
        inputPanel.revalidate();
        inputPanel.repaint();
        centerPanel.removeAll();
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void mostrarGrafico(String aInput, String bInput, double mu, double sigma) {
        centerPanel.removeAll();
        double area;
        String resultado;

        PanelGraphics graphics = new PanelGraphics(mu, sigma);

        double a, b;
        a = parseInput(aInput, "a");
        b = parseInput(bInput, "b");
        if (a == Double.MIN_VALUE || b == Double.MIN_VALUE) {
            return; // Error de parsing ya mostrado
        }

        if (a > b) {
            area = integrationService.calculateArea(b, a, mu, sigma);
        } else {
            area = integrationService.calculateArea(a, b, mu, sigma);
        }

        if (sigma <= 0) {
            JOptionPane.showMessageDialog(this, "El valor de σ debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (a == 20 && b == 20) {
            area = 1.0;
        }

        resultado = String.format("%.4f", area);

        JLabel lblArea = new JLabel("Área: ");
        resultadoArea = new JTextField(resultado);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(Color.WHITE);

        JPanel resultadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultadoPanel.add(lblArea);
        resultadoPanel.add(resultadoArea);

        graphics.getPlot().setPreferredSize(new Dimension(550, 360));

        centerPanel.add(graphics.getPlot(), BorderLayout.CENTER);
        centerPanel.add(resultadoPanel, BorderLayout.SOUTH);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void mostrarTabla(double bFinal) {
        centerPanel.removeAll();

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        List<Double[]> tableData = calculateTableData(-6.0, bFinal, 0.01);
        String[] columnNames = {"b", "Área"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Double[] rowData : tableData) {
            tableModel.addRow(new Object[]{
                    String.format("%.2f", rowData[0]),
                    String.format("%.16f", rowData[1])
            });
        }

        table.setModel(tableModel);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private List<Double[]> calculateTableData(double bInicial, double bFinal, double incremento) {
        List<Double[]> data = new ArrayList<>();
        double area;
        double epsilon = 1e-13;
        for (double b = bInicial; b <= bFinal; b += incremento) {
            if (Math.abs(b) < epsilon) {
                area = 0.5;
            } else {
                area = integrationService.calculateArea(bInicial, b);
            }
            data.add(new Double[]{b, area});
        }
        return data;
    }

    private void generatePdfWithTables(String dest, List<List<Double[]>> tablesData) throws IOException {
        PdfGenerator pdfGenerator = new PdfGenerator();
        pdfGenerator.generatePdfWithTables(dest, tablesData);
    }

    private List<List<Double[]>> calculateTablesData(double bInicial, double bFinal, double incremento) {
        List<List<Double[]>> tablesData = new ArrayList<>();
        List<Double[]> tableData = new ArrayList<>();
        int expectedRows = 21;

        BigDecimal b = BigDecimal.valueOf(bInicial);
        BigDecimal bEnd = BigDecimal.valueOf(bFinal);
        BigDecimal increment = BigDecimal.valueOf(incremento);

        while (b.compareTo(bEnd) <= 0) {
            double bValue = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
            double area = integrationService.calculateArea(bInicial, bValue);
            tableData.add(new Double[]{bValue, area});

            if (tableData.size() == expectedRows) {
                tablesData.add(new ArrayList<>(tableData));
                tableData.clear();
            }

            b = b.add(increment);
        }

        while (tableData.size() < expectedRows) {
            tableData.add(new Double[]{,});
        }

        tablesData.add(new ArrayList<>(tableData));

        return tablesData;
    }

    private double parseInput(String input, String fieldName) {
        if ("INF".equalsIgnoreCase(input)) {
            return 20;
        } else {
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en el valor de '" + fieldName + "'. Debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return Double.MIN_VALUE;
            }
        }
    }

    private boolean esNumero(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}