package io.github.pixeldev.integral.view;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PdfGenerator {

    public void generatePdfWithTables(String dest, List<List<Double[]>> tablesData) throws IOException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        Table mainTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));
        mainTable.setWidth(UnitValue.createPercentValue(100));

        double epsilon = 1e-13;
        for (List<Double[]> tableData : tablesData) {
            Table subTable = new Table(UnitValue.createPercentArray(new float[]{0.3f, 0.7f}));
            subTable.setWidth(UnitValue.createPercentValue(100));

            subTable.addHeaderCell("b");
            subTable.addHeaderCell("Área");

            for (Double[] rowData : tableData) {
                System.out.println(Arrays.toString(rowData));
                if (rowData != null && rowData.length == 2) {
                    if (Math.abs(rowData[0]) < epsilon) {
                        rowData[0] = 0.00;
                        rowData[1] = 0.5;
                    }
                    subTable.addCell(String.format("b %.2f", rowData[0]));
                    subTable.addCell(rowData[1].toString());
                }
            }
            mainTable.addCell(new Cell().add(subTable));
        }

        document.add(mainTable);
        document.close();
    }
}