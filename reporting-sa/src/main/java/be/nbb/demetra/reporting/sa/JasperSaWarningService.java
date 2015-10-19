/*
 * Copyright 2013 National Bank of Belgium
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
package be.nbb.demetra.reporting.sa;

import be.nbb.demetra.reporting.sa.warnings.SaWarningsDataSource;
import ec.nbdemetra.sa.ISaReportFactory;
import ec.nbdemetra.ui.DemetraUiIcon;
import ec.nbdemetra.ui.MonikerUI;
import ec.tss.sa.SaProcessing;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Service generating the Jasper report of warnings 
 * resulting of the SA processing of Ts
 *
 * @author Mats Maggi
 */
@ServiceProvider(service = ISaReportFactory.class, position = 15)
public class JasperSaWarningService implements ISaReportFactory {

    @Override
    public String getReportName() {
        return "Jasper Report Warnings";
    }

    @Override
    public String getReportDescription() {
        return "Jasper Report version of the Seasonal Adjustment Processing displaying warnings";
    }

    @Override
    public boolean createReport(SaProcessing process) {
        if (!process.isEmpty()) {
            try {
                Map parameters = new HashMap();
                MonikerUI mui = MonikerUI.getDefault();
                ImageIcon docIcon = ((ImageIcon)mui.getIcon(process.get(0).getMoniker()));
                parameters.put("docIcon", docIcon == null ? null : docIcon.getImage());
                parameters.put("warningIcon", DemetraUiIcon.WARNING.getImageIcon().getImage());
                JasperPrint jasperPrint;
                InputStream in = JasperSaWarningService.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/sa/warnings/SaWarningReport.jasper");
                jasperPrint = JasperFillManager.fillReport(in, parameters, new SaWarningsDataSource(process));
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
            return true;
        } else {
            return false;
        }
    }
}
