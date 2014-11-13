/*
 * Copyright 2014 National Bank of Belgium
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
package be.nbb.demetra.reporting.sa.tramoseats;

import be.nbb.demetra.reporting.JasperTypeFormatters;
import be.nbb.demetra.reporting.JasperTypeFormatters.ITypeFormatter;
import be.nbb.demetra.reporting.sa.pojo.KeyValuePojo;
import ec.nbdemetra.ui.awt.ExceptionPanel;
import ec.satoolkit.ISaSpecification;
import ec.tstoolkit.information.InformationSet;
import ec.ui.view.tsprocessing.DefaultItemUI;
import ec.ui.view.tsprocessing.IProcDocumentView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author Mats Maggi
 */
public class SpecAllJasperView<V extends IProcDocumentView<?>> extends DefaultItemUI<V, ISaSpecification> {

    @Override
    public JComponent getView(V host, ISaSpecification information) {
        InformationSet info = information.write(true);
        Map<String, Class> dictionary = new LinkedHashMap<>();
        info.fillDictionary(null, dictionary);

        List<KeyValuePojo> list = new ArrayList<>();
        for (Map.Entry<String, Class> entry : dictionary.entrySet()) {
            String k = entry.getKey();
            ITypeFormatter fmt = JasperTypeFormatters.getFormatter(entry.getValue());
            String v = fmt.format(info.search(k, Object.class));
            list.add(new KeyValuePojo(k, v));
        }

        InputStream in = SpecAllJasperView.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/tramoseats/specAll.jasper");

        JasperPrint jasper;
        try {
            jasper = JasperFillManager.fillReport(in, new HashMap(), new JRBeanCollectionDataSource(list));
            JRHtmlExporter exporter = new JRHtmlExporter();
            exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasper);
            exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            JRViewer viewer = new JRViewer(jasper);
            viewer.setZoomRatio(0.75f);
            return viewer;
        } catch (JRException ex) {
            return ExceptionPanel.create(ex);
        }
    }

}
