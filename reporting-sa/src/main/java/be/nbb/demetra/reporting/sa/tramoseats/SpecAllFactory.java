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
package be.nbb.demetra.reporting.sa.tramoseats;

import be.nbb.demetra.reporting.sa.pojo.KeyValuePojo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mats Maggi
 */
public class SpecAllFactory {

    public static Collection createBeanCollection() {
        List<KeyValuePojo> list = new ArrayList<>();
        
        list.add(new KeyValuePojo("Book Title", "Java Tools"));
        list.add(new KeyValuePojo("Book Publisher", "Saviour Prakashan"));
        list.add(new KeyValuePojo("Book Published On", "25th December, 2001"));
        list.add(new KeyValuePojo("Book Author", "Vivek Shah"));

        return list;
    }
}
