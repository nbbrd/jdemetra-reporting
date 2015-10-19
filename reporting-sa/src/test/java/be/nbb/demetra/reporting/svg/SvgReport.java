/*
 * Copyright 2015 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package be.nbb.demetra.reporting.svg;

import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.ui.chart.TsXYDatasets;
import ec.util.chart.SeriesFunction;
import ec.util.chart.TimeSeriesChart;
import ec.util.chart.impl.LollipopColorScheme;
import ec.util.chart.swing.JTimeSeriesChart;
import ec.util.chart.swing.SwingColorSchemeSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.view.JasperViewer;
import org.openide.util.Exceptions;

/**
 * Demo of using a SVG JTimeSeriesChart in a Jasper Reports.
 * The generated SVG string is provided to the report which renders it
 * @author Mats Maggi
 */
public class SvgReport {

    public static void main(String[] args) {
        try {
            Map parameters = new HashMap();

            TsXYDatasets.Builder rslt = TsXYDatasets.builder();

            for (int i = 0; i < 5; i++) {
                rslt.add("Series " + (i + 1), TsData.random(TsFrequency.Monthly));
            }

            JTimeSeriesChart chart = new JTimeSeriesChart();
            chart.setSeriesRenderer(SeriesFunction.always(TimeSeriesChart.RendererType.SPLINE));
            chart.setColorSchemeSupport(SwingColorSchemeSupport.from(new LollipopColorScheme()));
            chart.setSize(802,531);
            chart.doLayout();
            chart.setDataset(rslt.build());

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            chart.writeImage("image/svg+xml", os);
            String aString = new String(os.toByteArray(), "UTF-8");
            
            parameters.put("SVG_STRING", aString);
            JasperPrint jasperPrint;
            InputStream in = SvgReport.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/svg/svgReport.jasper");
            jasperPrint = JasperFillManager.fillReport(in, parameters, new JREmptyDataSource());
            jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
