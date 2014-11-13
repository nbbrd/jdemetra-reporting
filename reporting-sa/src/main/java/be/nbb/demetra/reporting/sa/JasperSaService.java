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
package be.nbb.demetra.reporting.sa;

import be.nbb.demetra.reporting.sa.pojo.KeyValuePojo;
import ec.nbdemetra.sa.ISaReportFactory;
import ec.nbdemetra.ui.mru.SourceId;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.satoolkit.DecompositionMode;
import ec.tss.sa.SaItem;
import ec.tss.sa.SaProcessing;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.ProcQuality;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Service generating the Jasper report of the Sa Processing
 *
 * @author Mats Maggi
 */
@ServiceProvider(service = ISaReportFactory.class, position = 11)
public class JasperSaService implements ISaReportFactory {

    private static final DecimalFormat pc = new DecimalFormat();
    private static final DecimalFormat dec = new DecimalFormat();
    private static Collection outliers;
    private static Collection outliersAvg;

    @Override
    public String getReportName() {
        return "Jasper Report";
    }

    @Override
    public String getReportDescription() {
        return "Jasper Report version of the Seasonal Adjustment Processing";
    }

    @Override
    public boolean createReport(SaProcessing process) {
        Map parameters = new HashMap();
        pc.setMultiplier(100);
        pc.setMaximumFractionDigits(2);
        dec.setMaximumFractionDigits(2);

        SaProcessingPojo pojo = new SaProcessingPojo();

        pojo.setWorkspace(getWorkspace());
        pojo.setProcessing(process.getDocumentId());
        pojo.setNbSeries(process.size());
        pojo.setNbSuccEst(getNbSuccessfulEstimations(process));
        pojo.setSpecifications(getSpecs(process));
        pojo.setArima(getArimaModel(process));
        pojo.setModes(getDecompositionModes(process));

        getOutliers(process);
        pojo.setOutliers(outliers);
        pojo.setOutliersAvg(outliersAvg);

        pojo.setDiagnostics(createDiagnostics(process));

        parameters.put("pojo", pojo);

        createReport(parameters);

        return true;
    }

    private static void createReport(Map parameters) {
        try {
            JasperPrint jasperPrint;
            parameters.put("SUBREPORT_DIR", "be/nbb/demetra/reporting/arima/");
            InputStream in = JasperSaService.class.getClassLoader().getResourceAsStream("be/nbb/demetra/reporting/arima/SaReport.jasper");
            jasperPrint = JasperFillManager.fillReport(in, parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static String getWorkspace() {
        SourceId sourceId = WorkspaceFactory.getInstance().getActiveWorkspace().getSourceId();
        if (sourceId != null) {
            return sourceId.getLabel();
        } else {
            return "";
        }
    }

    private static Collection getSpecs(SaProcessing processing) {
        List<String> specs = new ArrayList<>();
        Map<String, Integer> mmap = new HashMap<>();
        for (SaItem item : processing) {
            String m = item.getDomainSpecification().toLongString();
            Integer c = mmap.get(m);
            if (c == null) {
                mmap.put(m, 1);
            } else {
                mmap.put(m, c + 1);
            }
        }

        double sz = processing.size();
        for (Map.Entry<String, Integer> spec : mmap.entrySet()) {
            specs.add(spec.getKey() + "\t" + pc.format((spec.getValue() / sz)) + "%");
        }

        return specs;
    }

    private static Collection getArimaModel(SaProcessing processing) {
        List<String> arima = new ArrayList<>();

        SortedMap<String, Integer> nmodel = new TreeMap<>();
        double size = 0;
        for (SaItem item : processing) {
            CompositeResults rslt = item.process();
            if (rslt == null) {
                continue;
            }
            SarimaModel m = rslt.getData("arima", SarimaModel.class);
            if (m != null) {
                ++size;
                String n = m.getSpecification().toString();
                Integer c = nmodel.get(n);
                if (c == null) {
                    nmodel.put(n, 1);
                } else {
                    nmodel.put(n, c + 1);
                }
            }
        }
        if (size == 0) {
            return null;
        }
        for (Entry<String, Integer> entry : nmodel.entrySet()) {
            arima.add(entry.getKey() + "\t" + pc.format((entry.getValue() / size)) + "%");
        }

        return arima;
    }

    private static Collection getDecompositionModes(SaProcessing processing) {
        List<KeyValuePojo> decomp = new ArrayList<>();
        EnumMap<DecompositionMode, Integer> mmap = new EnumMap<>(DecompositionMode.class);
        for (SaItem item : processing) {
            CompositeResults rslt = item.process();
            if (rslt == null) {
                continue;
            }

            DecompositionMode mode = rslt.getData("decomposition.mode", DecompositionMode.class);
            if (mode != null) {
                Integer c = mmap.get(mode);
                if (c == null) {
                    mmap.put(mode, 1);
                } else {
                    mmap.put(mode, c + 1);
                }
            }
        }

        //out.write("Seas. present\t" + pc2.format(nseas / ((double) nseries)));
        for (DecompositionMode m : DecompositionMode.values()) {
            Integer n = mmap.get(m);
            decomp.add(new KeyValuePojo(m.name(),
                    (n != null ? n : 0) / ((double) processing.size())));
        }

        return decomp;
    }

    private static int getNbSuccessfulEstimations(SaProcessing processing) {
        int nseries = 0;
        for (SaItem item : processing) {
            CompositeResults rslt = item.process();
            if (rslt == null) {
                continue;
            }
            if (rslt.getData("sa", TsData.class) != null) {
                ++nseries;
            }
        }

        return nseries;
    }

    private static Collection createDiagnostics(SaProcessing processing) {
        List<KeyValuePojo> diag = new ArrayList<>();

        EnumMap<ProcQuality, Integer> qmap = new EnumMap<>(ProcQuality.class);
        double size = 0;
        for (SaItem item : processing) {
            CompositeResults rslt = item.process();
            if (rslt == null) {
                continue;
            }
            ProcQuality q = item.getQuality();
            if (q != null) {
                ++size;
                Integer c = qmap.get(q);
                if (c == null) {
                    qmap.put(q, 1);
                } else {
                    qmap.put(q, c + 1);
                }
            }
        }
        if (size == 0) {
            return null;
        }

        for (ProcQuality q : ProcQuality.values()) {
            Integer n = qmap.get(q);
            diag.add(new KeyValuePojo(q.name(), (n != null ? n : 0) / size));
        }

        return diag;
    }

    private static void getOutliers(SaProcessing processing) {
        outliers = new ArrayList<>();
        outliersAvg = new ArrayList<>();

        int ntot = 0, nao = 0, nls = 0, ntc = 0, nso = 0;
        for (SaItem item : processing) {
            CompositeResults rslt = item.process();
            if (rslt == null) {
                continue;
            }
            Integer n = rslt.getData("regression.nout", Integer.class);
            if (n != null) {
                ntot += n;
            }
            n = rslt.getData("regression.noutao", Integer.class);
            if (n != null) {
                nao += n;
            }
            n = rslt.getData("regression.noutls", Integer.class);
            if (n != null) {
                nls += n;
            }
            n = rslt.getData("regression.nouttc", Integer.class);
            if (n != null) {
                ntc += n;
            }
            n = rslt.getData("regression.noutso", Integer.class);
            if (n != null) {
                nso += n;
            }
        }
        if (ntot == 0) {
            return;
        }
        double dsize = processing.size();
        double dtot = ntot;

        outliersAvg.add(new KeyValuePojo("All", ntot / dsize));

        if (nao > 0) {
            outliersAvg.add(new KeyValuePojo("AO", nao / dsize));
        }
        if (nls > 0) {
            outliersAvg.add(new KeyValuePojo("LS", nls / dsize));
        }
        if (ntc > 0) {
            outliersAvg.add(new KeyValuePojo("TC", ntc / dsize));
        }
        if (nso > 0) {
            outliersAvg.add(new KeyValuePojo("SO", nso / dsize));
        }

        if (nao > 0) {
            outliers.add(new KeyValuePojo("AO", nao / dtot));
        }
        if (nls > 0) {
            outliers.add(new KeyValuePojo("LS", nls / dtot));
        }
        if (ntc > 0) {
            outliers.add(new KeyValuePojo("TC", ntc / dtot));
        }
        if (nso > 0) {
            outliers.add(new KeyValuePojo("SO", nso / dtot));
        }
    }
}
