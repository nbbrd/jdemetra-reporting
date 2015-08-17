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
import be.nbb.demetra.reporting.sa.pojo.KeyValuePojo;
import ec.satoolkit.ISaSpecification;
import ec.tss.html.IHtmlElement;
import ec.tss.sa.documents.SaDocument;
import ec.tstoolkit.information.InformationSet;
import ec.ui.view.tsprocessing.HtmlItemUI;
import ec.ui.view.tsprocessing.IProcDocumentView;
import static ec.ui.view.tsprocessing.sa.SaDocumentViewFactory.INPUT_SPEC;
import ec.ui.view.tsprocessing.sa.SaDocumentViewFactory.ItemFactory;
import static ec.ui.view.tsprocessing.sa.SaDocumentViewFactory.specExtractor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

/**
 *
 * @author Mats Maggi
 */
public class SaDocumentViewFactory {

    protected static class SpecAllFactory<D extends SaDocument<? extends ISaSpecification>>
            extends ItemFactory<D, ISaSpecification> {

        protected SpecAllFactory(Class<D> documentType) {
            super(documentType, INPUT_SPEC, specExtractor(), new HtmlItemUI<IProcDocumentView<D>, ISaSpecification>() {

                @Override
                protected IHtmlElement getHtmlElement(IProcDocumentView<D> host, ISaSpecification information) {
                    InformationSet info = information.write(true);
                    Map<String, Class> dictionary = new LinkedHashMap<>();
                    info.fillDictionary(null, dictionary);

                    List<KeyValuePojo> list = new ArrayList<>();
                    for (Map.Entry<String, Class> entry : dictionary.entrySet()) {
                        String k = entry.getKey();
                        JasperTypeFormatters.ITypeFormatter fmt = JasperTypeFormatters.getFormatter(entry.getValue());
                        String v = fmt.format(info.search(k, Object.class));
                        list.add(new KeyValuePojo(k, v));
                    }

                    InputStream in = SpecAllJasperView.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/tramoseats/specAll.jasper");

                    JasperPrint jasper;
                    try {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        jasper = JasperFillManager.fillReport(in, new HashMap(), new JRBeanCollectionDataSource(list));

                        HtmlExporter exporter = new HtmlExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasper));
                        SimpleHtmlExporterConfiguration cfg = new SimpleHtmlExporterConfiguration();
                        exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                        exporter.setConfiguration(cfg);
                        exporter.exportReport();
                    } catch (JRException ex) {
                        return null;
                    }
                    return null;
                }
            });
        }
    }
}
