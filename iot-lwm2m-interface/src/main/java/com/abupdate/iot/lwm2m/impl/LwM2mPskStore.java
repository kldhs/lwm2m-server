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
package com.abupdate.iot.lwm2m.impl;

import java.net.InetSocketAddress;
import java.util.Arrays;

import org.eclipse.californium.scandium.dtls.pskstore.PskStore;
import org.eclipse.californium.scandium.util.ServerNames;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationStore;
import org.eclipse.lwm2m.server.security.SecurityInfo;
import org.eclipse.lwm2m.server.security.SecurityStore;

public class LwM2mPskStore implements PskStore {

    private SecurityStore securityStore;
    private RegistrationStore registrationStore;

    public LwM2mPskStore(SecurityStore securityStore) {
        this(securityStore, null);
    }

    public LwM2mPskStore(SecurityStore securityStore, RegistrationStore registrationStore) {
        this.securityStore = securityStore;
        this.registrationStore = registrationStore;
    }

    @Override
    public byte[] getKey(String identity) {
        if (securityStore == null)
            return null;

        SecurityInfo info = securityStore.getByIdentity(identity);
        if (info == null || info.getPreSharedKey() == null) {
            return null;
        } else {
            // defensive copy
            return Arrays.copyOf(info.getPreSharedKey(), info.getPreSharedKey().length);
        }
    }

    @Override
    public byte[] getKey(ServerNames serverNames, String identity) {
        // serverNames is not supported
        return getKey(identity);
    }

    @Override
    public String getIdentity(InetSocketAddress inetAddress) {
        if (registrationStore == null)
            return null;

        Registration registration = registrationStore.getRegistrationByAdress(inetAddress);
        if (registration != null) {
            SecurityInfo securityInfo = securityStore.getByEndpoint(registration.getEndpoint());
            if (securityInfo != null) {
                return securityInfo.getIdentity();
            }
            return null;
        }
        return null;
    }
}
