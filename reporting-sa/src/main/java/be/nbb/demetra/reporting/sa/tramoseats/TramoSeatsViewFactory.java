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

import ec.tss.sa.documents.TramoSeatsDocument;
import ec.ui.view.tsprocessing.ProcDocumentItemFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mats Maggi
 */
public class TramoSeatsViewFactory {
    
    @ServiceProvider(service = ProcDocumentItemFactory.class, position = 100000, supersedes = {"ec.nbdemetra.tramoseats.ui.TramoSeatsViewFactory$SpecAllFactory"})
    public static class SpecAllFactory extends SaDocumentViewFactory.SpecAllFactory<TramoSeatsDocument> {

        public SpecAllFactory() {
            super(TramoSeatsDocument.class);
        }
    }
}
