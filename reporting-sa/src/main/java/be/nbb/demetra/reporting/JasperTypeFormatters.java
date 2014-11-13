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
package be.nbb.demetra.reporting;

import ec.tstoolkit.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mats Maggi
 */
public class JasperTypeFormatters {

    public static interface ITypeFormatter<T> {
        String format(T t);
    }
    
    public static final Map<Class, ITypeFormatter> map_ = new HashMap<>();
    
    public static synchronized <T> void register(Class<T> tclass, ITypeFormatter<T> fmt) {
        map_.put(tclass, fmt);
    }
    
    public static final ITypeFormatter defaultFormatter = new ITypeFormatter() {

        @Override
        public String format(Object t) {
            if (t instanceof Object[]) {
                Object[] a = (Object[]) t;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < a.length; ++i) {
                    builder.append(a[i]);
                    if (i != a.length - 1) {
                        builder.append(", ");
                    }
                }
                return builder.toString();
            } else {
                return t.toString();
            }
        }
    };
    
    static {
        register(String[].class, new ITypeFormatter<String[]>() {
            @Override
            public String format(String[] t) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < t.length; ++i) {
                    builder.append(t[i]);
                    if (i != t.length - 1) {
                        builder.append(", ");
                    }
                }
                return builder.toString();
            }
        });
        register(int[].class, new ITypeFormatter<int[]>() {
            @Override
            public String format(int[] t) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < t.length; ++i) {
                    builder.append(t[i]);
                    if (i != t.length - 1) {
                        builder.append(", ");
                    }
                }
                return builder.toString();
            }
        });
        register(double[].class, new ITypeFormatter<double[]>() {
            @Override
            public String format(double[] t) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < t.length; ++i) {
                    builder.append(t[i]);
                    if (i != t.length - 1) {
                        builder.append(", ");
                    }
                }
                return builder.toString();
            }
        });
        register(Parameter[].class, new ITypeFormatter<Parameter[]>() {
            @Override
            public String format(Parameter[] t) {
                StringBuilder builder = new StringBuilder();
                if (Parameter.isDefault(t)) {
                    builder.append(t.length).append(" coeff.");
                } else {
                    for (int i = 0; i < t.length; ++i) {
                        builder.append(t[i]);
                        if (i != t.length - 1) {
                            builder.append(", ");
                        }
                    }
                }
                return builder.toString();
            }
        });
    }
    
    public static ITypeFormatter getFormatter(Class c) {
        ITypeFormatter fmt = map_.get(c);
        if (fmt == null) {
            return defaultFormatter;
        } else {
            return fmt;
        }
    }
}
