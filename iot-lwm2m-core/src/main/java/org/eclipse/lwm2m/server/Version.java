/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.lwm2m.server;

public class Version {
    public static String value;

    public final static String getValue() {
        if (value == null) {
            Package p = Version.class.getPackage();
            String version = p.getImplementationVersion();
            if (version != null)
                value = version;
            else
                value = "unknown";
        }
        return value;
    }
}
