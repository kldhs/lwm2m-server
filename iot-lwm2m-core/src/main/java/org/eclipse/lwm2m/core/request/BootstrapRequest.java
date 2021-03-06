/*******************************************************************************
 * Copyright (c) 2015 Sierra Wireless and others.
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
package org.eclipse.lwm2m.core.request;

import org.eclipse.lwm2m.core.request.exception.InvalidRequestException;
import org.eclipse.lwm2m.core.response.BootstrapResponse;

/**
 * The request to send to start a bootstrap session
 */
public class BootstrapRequest implements UplinkRequest<BootstrapResponse> {

    private final String endpointName;

    public BootstrapRequest(String endpointName) throws InvalidRequestException {
        if (endpointName == null || endpointName.isEmpty())
            throw new InvalidRequestException("endpoint is mandatory");

        this.endpointName = endpointName;
    }

    public String getEndpointName() {
        return endpointName;
    }

    @Override
    public void accept(UplinkRequestVisitor visitor) {
        visitor.visit(this);
    }
}
