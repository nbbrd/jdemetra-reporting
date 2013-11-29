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

import ec.nbdemetra.anomalydetection.AnomalyItem;
import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tstoolkit.modelling.arima.CheckLast;
import ec.tstoolkit.modelling.arima.tramo.TramoSpecification;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class providing data to test the CheckLastReport
 * in iReport Designer
 * @author Mats Maggi
 */
public class AnomalyItemFactory {
    
    private static List<AnomalyPojo> valid = new ArrayList<AnomalyPojo>();
    private static List<AnomalyPojo> empty = new ArrayList<AnomalyPojo>();
    private static List<AnomalyPojo> invalid = new ArrayList<AnomalyPojo>();

    static {
        createBeanCollection();
    }
    
    public AnomalyItemFactory() {
    }

    public static List<AnomalyPojo> getValid() {
        return valid;
    }

    public static List<AnomalyPojo> getEmpty() {
        return empty;
    }

    public static List<AnomalyPojo> getInvalid() {
        return invalid;
    }

    public static void createBeanCollection() {
        TramoSpecification t = TramoSpecification.TR4.clone();
        valid = new ArrayList<AnomalyPojo>();
        empty = new ArrayList<AnomalyPojo>();
        invalid = new ArrayList<AnomalyPojo>();

        CheckLast c = new CheckLast(t.build());
        c.setBackCount(3);
        TsFactory f;

        for (int i = 0; i < 30; i++) {
            Ts ts = TsFactory.instance.createTs("Ts number " + (i + 1), null, TsData.random(TsFrequency.Monthly));
            AnomalyItem a = new AnomalyItem();
            a.setTs(ts);
            a.setBackCount(c.getBackCount());
            a.process(c);
            for (int j = 0; j < a.getBackCount(); j++) {
                if (a.isNotProcessable()) {
                    AnomalyPojo p = new AnomalyPojo(a.getTs().getName(),
                            null,
                            Double.NaN,
                            Double.NaN,
                            AnomalyPojo.Status.Invalid);
                    invalid.add(p);
                } else if (a.isInvalid()) {
                    AnomalyPojo p = new AnomalyPojo(a.getTs().getName(),
                            null,
                            Double.NaN,
                            Double.NaN,
                            AnomalyPojo.Status.Empty);
                    empty.add(p);
                } else {
                    AnomalyPojo p = new AnomalyPojo(a.getTs().getName(),
                            a.getTs().getTsData().getLastPeriod().minus(j),
                            a.getRelativeError(j),
                            a.getAbsoluteError(j),
                            AnomalyPojo.Status.Valid);
                    valid.add(p);
                }
            }
        }
    }

    public static void main(String[] args) {
        createBeanCollection();
    }
}
