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
package org.eclipse.lwm2m.core.response;

import org.eclipse.lwm2m.ResponseCode;

/**
 * Response to a bootstrap finish request from the bootstrap server.
 */
public class BootstrapFinishResponse extends AbstractLwM2mResponse {

    public BootstrapFinishResponse(ResponseCode code, String errorMessage) {
        this(code, errorMessage, null);
    }

    public BootstrapFinishResponse(ResponseCode code, String errorMessage, Object coapResponse) {
        super(code, errorMessage, coapResponse);
    }

    @Override
    public boolean isSuccess() {
        return getCode() == ResponseCode.CHANGED;
    }

    @Override
    public String toString() {
        if (errorMessage != null)
            return String.format("BootstrapFinishResponse [code=%s, errormessage=%s]", code, errorMessage);
        else
            return String.format("BootstrapFinishResponse [code=%s]", code);
    }

    // Syntactic sugar static constructors :

    public static BootstrapFinishResponse success() {
        return new BootstrapFinishResponse(ResponseCode.CHANGED, null);
    }

    public static BootstrapFinishResponse badRequest(String errorMessage) {
        return new BootstrapFinishResponse(ResponseCode.BAD_REQUEST, errorMessage);
    }

    public static BootstrapFinishResponse internalServerError(String errorMessage) {
        return new BootstrapFinishResponse(ResponseCode.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
