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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mats Maggi
 */
public class SaReportDataFactory {
    
    public static SaProcessingPojo getPojo() {        
        SaProcessingPojo pojo = new SaProcessingPojo();
        
        pojo.setSpecifications(createSpecs());
        pojo.setArima(createArimaModel());
        pojo.setOutliers(createOutliers());
        pojo.setOutliersAvg(createOutliersAvg());
        pojo.setModes(createDecompositionMode());
        pojo.setDiagnostics(createDiagnostics());
        
        return pojo;
    }
    
    public static Collection createArimaModel() {
        List<String> arima = new ArrayList<String>();
        
        arima.add("(0,1,1)(0,1,0)\t2%");
        arima.add("(0,1,1)(0,1,1)\t60%");
        arima.add("(0,1,1)(1,1,1)\t2%");
        arima.add("(0,1,2)(0,1,1)\t4%");
        arima.add("(0,1,2)(1,1,1)\t2%");
        arima.add("(1,0,0)(0,1,1)\t2%");
        arima.add("(1,0,1)(0,1,1)\t2%");
        arima.add("(1,1,0)(0,1,1)\t2%");
        arima.add("(1,1,0)(1,1,1)\t4%");
        arima.add("(2,0,0)(0,1,1)\t2%");
        arima.add("(2,1,0)(0,1,1)\t2%");
        arima.add("(2,1,1)(0,1,1)\t8%");
        arima.add("(3,0,1)(0,1,1)\t4%");
        arima.add("(3,1,0)(1,1,1)\t2%");
        arima.add("(3,1,1)(0,1,1)\t2%");
        
        return arima;
    }
    
    public static Collection createSpecs() {
        List<String> specs = new ArrayList<String>();
        specs.add("TS[RSA4]\t25%");
        specs.add("TS[RSA5]\t75%");
        return specs;
    }
    
    public static Collection createOutliers() {
        List<KeyValuePojo> outliers = new ArrayList<KeyValuePojo>();
        
        outliers.add(new KeyValuePojo("AO", 43.97));
        outliers.add(new KeyValuePojo("LS", 35.46));
        outliers.add(new KeyValuePojo("TC", 20.57));
        
        return outliers;
    }
    
    public static Collection createOutliersAvg() {
        List<KeyValuePojo> outliers = new ArrayList<KeyValuePojo>();
        
        outliers.add(new KeyValuePojo("All", 2.82));
        outliers.add(new KeyValuePojo("AO", 1.24));
        outliers.add(new KeyValuePojo("LS", 1));
        outliers.add(new KeyValuePojo("TC", 0.58));
        
        return outliers;
    }
    
    public static Collection createDecompositionMode() {
        List<KeyValuePojo> modes = new ArrayList<KeyValuePojo>();
        
        modes.add(new KeyValuePojo("Undefined", 0));
        modes.add(new KeyValuePojo("Additive", 2));
        modes.add(new KeyValuePojo("Multiplicative", 98));
        modes.add(new KeyValuePojo("LogAdditive", 0));
        
        return modes;
    }
    
    public static Collection createDiagnostics() {
        List<KeyValuePojo> diag = new ArrayList<KeyValuePojo>();
        
        diag.add(new KeyValuePojo("Accepted", 0));
        diag.add(new KeyValuePojo("Undefined", 0));
        diag.add(new KeyValuePojo("Error", 0));
        diag.add(new KeyValuePojo("Severe", 8));
        diag.add(new KeyValuePojo("Bad", 0));
        diag.add(new KeyValuePojo("Uncertain", 20));
        diag.add(new KeyValuePojo("Good", 72));
        
        return diag;
    }
}
