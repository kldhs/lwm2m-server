/*******************************************************************************
 * Copyright (c) 2016 Sierra Wireless and others.
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
package org.eclipse.lwm2m.server.security;

import org.eclipse.lwm2m.core.request.Identity;
import org.eclipse.lwm2m.core.request.UplinkRequest;
import org.eclipse.lwm2m.server.registration.Registration;

/**
 * A default {@link Authorizer} implementation
 * <p>
 * It checks in {@link SecurityStore} if there is a corresponding {@link SecurityInfo} for this registration endpoint.
 * If there is a {@link SecurityInfo} it check the identity is correct, else it checks if the LWM2M client use an
 * unsecure connection.
 */
public class DefaultAuthorizer implements Authorizer {

    private SecurityStore securityStore;

    public DefaultAuthorizer(SecurityStore store) {
        securityStore = store;
    }

    @Override
    public boolean isAuthorized(UplinkRequest<?> request, Registration registration, Identity senderIdentity) {

        // do we have security information for this client?
        SecurityInfo expectedSecurityInfo = null;
        if (securityStore != null)
            expectedSecurityInfo = securityStore.getByEndpoint(registration.getEndpoint());
        return SecurityCheck.checkSecurityInfo(registration.getEndpoint(), senderIdentity, expectedSecurityInfo);
    }
}
