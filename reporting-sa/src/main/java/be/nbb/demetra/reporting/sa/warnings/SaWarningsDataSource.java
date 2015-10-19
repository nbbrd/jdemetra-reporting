/*
 * Copyright 2014 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or â€“ as soon they will be approved 
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
package be.nbb.demetra.reporting.sa.warnings;

import ec.satoolkit.tramoseats.TramoSeatsSpecification;
import ec.satoolkit.x13.X13Specification;
import ec.tss.Ts;
import ec.tss.sa.EstimationPolicyType;
import ec.tss.sa.SaProcessing;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.ui.chart.TsXYDatasets;
import ec.util.chart.SeriesFunction;
import ec.util.chart.SeriesPredicate;
import ec.util.chart.TimeSeriesChart;
import ec.util.chart.impl.LollipopColorScheme;
import ec.util.chart.swing.JTimeSeriesChart;
import ec.util.chart.swing.SwingColorSchemeSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.openide.util.Exceptions;

/**
 * DataSource used by a Jasper Report. This provides items of a given SaBatch
 * processing as input for the report
 *
 * @author Mats Maggi
 */
public class SaWarningsDataSource implements JRDataSource {

    private final SaProcessing items;
    private int counter = -1;

    public SaWarningsDataSource(SaProcessing items) {
        this.items = items;
    }

    @Override
    public boolean next() throws JRException {
        if (counter < items.size() - 1) {
            counter++;
            return true;
        }
        return false;
    }

    /**
     * This method makes the matching between the report fields and the values
     * in the Sa processing item
     *
     * @param jrf Report field
     * @return Value corresponding to the given report field
     * @throws JRException
     */
    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        switch (jrf.getName()) {
            case "Name":
                return MultiLineNameUtil.join(items.get(counter).getTs().getName());
            case "Method":
                return items.get(counter).getDomainSpecification().toString();
            case "MethodColor":
                if (items.get(counter).getDomainSpecification() instanceof TramoSeatsSpecification) {
                    return "Blue";
                } else if (items.get(counter).getDomainSpecification() instanceof X13Specification) {
                    return "Magenta";
                }
            case "Estimation":
                EstimationPolicyType ept = items.get(counter).getEstimationPolicy();
                if (items.get(counter).getEstimationSpecification() == null) {
                    return null;
                } else if (ept == EstimationPolicyType.FixedParameters) {
                    return "Current";
                } else if (ept == EstimationPolicyType.Complete) {
                    return "Concurrent";
                } else {
                    return ept.name();
                }
            case "Status":
                return items.get(counter).getStatus().toString();
            case "Priority":
                return items.get(counter).getPriority();
            case "Quality":
                return items.get(counter).getQuality().toString();
            case "Warnings":
                return Arrays.asList(items.get(counter).getWarnings());
            case "Svg":
                return createSvg(items.get(counter).getTs());
            default:
                return "";
        }
    }

    private String createSvg(Ts ts) {
        try {
            TsXYDatasets.Builder rslt = TsXYDatasets.builder();
            rslt.add(ts.getName(), ts.getTsData());
            JTimeSeriesChart chart = new JTimeSeriesChart();
            chart.setSeriesRenderer(SeriesFunction.always(TimeSeriesChart.RendererType.SPLINE));
            chart.setColorSchemeSupport(SwingColorSchemeSupport.from(new LollipopColorScheme()));
            chart.setSize(555, 164);
            chart.setLegendVisibilityPredicate(SeriesPredicate.alwaysFalse());
            chart.doLayout();
            chart.setDataset(rslt.build());
            
            // Generate SVG string
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            chart.writeImage("image/svg+xml", os);
            return new String(os.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "";
    }
}
