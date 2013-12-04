/*
 * Copyright 2013 National Bank of Belgium
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
package be.nbb.demetra.reporting.anomaly;

import ec.nbdemetra.anomalydetection.report.ICheckLastReportFactory;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * 
 * @author Mats Maggi
 */
@ServiceProvider(service = ICheckLastReportFactory.class, position = 10)
public class JasperAnomalyService implements ICheckLastReportFactory {

    @Override
    public String getReportName() {
        return "Jasper Report";
    }

    @Override
    public String getReportDescription() {
        return "Rich report version of the Check Last processing";
    }

    @Override
    public boolean createReport(Map parameters) {
        try {
            JasperPrint jasperPrint;
            InputStream in = JasperAnomalyService.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/checklast/CheckLastReport.jasper");
            jasperPrint = JasperFillManager.fillReport(in, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
            return true;
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getReportName();
    }
}
