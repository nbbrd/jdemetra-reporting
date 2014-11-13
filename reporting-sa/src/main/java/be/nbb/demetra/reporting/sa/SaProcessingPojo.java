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

import java.util.Collection;

/**
 * Object used to pass sa processing data to Jasper Report
 *
 * @author Mats Maggi
 */
public class SaProcessingPojo {

    private String workspace;
    private String processing;
    private String metadata;
    private int nbSeries;
    private int nbSuccEst;

    private Collection specifications;
    private Collection arima;
    private Collection outliers;
    private Collection outliersAvg;

    private Collection modes;
    private Collection diagnostics;

    public SaProcessingPojo() {
        workspace = "Workspace-1";
        processing = "Processing_1";
        metadata = "";
        nbSeries = 30;
        nbSuccEst = 27;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public int getNbSeries() {
        return nbSeries;
    }

    public void setNbSeries(int nbSeries) {
        this.nbSeries = nbSeries;
    }

    public int getNbSuccEst() {
        return nbSuccEst;
    }

    public void setNbSuccEst(int nbSuccEst) {
        this.nbSuccEst = nbSuccEst;
    }

    public Collection getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Collection specifications) {
        this.specifications = specifications;
    }

    public Collection getArima() {
        return arima;
    }

    public void setArima(Collection arima) {
        this.arima = arima;
    }

    public Collection getOutliers() {
        return outliers;
    }

    public void setOutliers(Collection outliers) {
        this.outliers = outliers;
    }

    public Collection getOutliersAvg() {
        return outliersAvg;
    }

    public void setOutliersAvg(Collection outliersAvg) {
        this.outliersAvg = outliersAvg;
    }

    public Collection getModes() {
        return modes;
    }

    public void setModes(Collection modes) {
        this.modes = modes;
    }

    public Collection getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(Collection diagnostics) {
        this.diagnostics = diagnostics;
    }
}
