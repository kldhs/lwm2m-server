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
package org.eclipse.lwm2m.core.response;

import org.eclipse.lwm2m.ResponseCode;

public class CreateResponse extends AbstractLwM2mResponse {

    private String location;

    public CreateResponse(ResponseCode code, String location, String errorMessage) {
        this(code, location, errorMessage, null);
    }

    public CreateResponse(ResponseCode code, String location, String errorMessage, Object coapResponse) {
        super(code, errorMessage, coapResponse);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean isSuccess() {
        return getCode() == ResponseCode.CREATED;
    }

    @Override
    public String toString() {
        if (errorMessage != null)
            return String.format("CreateResponse [code=%s, errormessage=%s]", code, errorMessage);
        else
            return String.format("CreateResponse [code=%s, location=%s]", code, location);
    }

    // Syntactic sugar static constructors :

    public static CreateResponse success(String location) {
        return new CreateResponse(ResponseCode.CREATED, location, null);
    }

    public static CreateResponse badRequest(String errorMessage) {
        return new CreateResponse(ResponseCode.BAD_REQUEST, null, errorMessage);
    }

    public static CreateResponse notFound() {
        return new CreateResponse(ResponseCode.NOT_FOUND, null, null);
    }

    public static CreateResponse unauthorized() {
        return new CreateResponse(ResponseCode.UNAUTHORIZED, null, null);
    }

    public static CreateResponse methodNotAllowed() {
        return new CreateResponse(ResponseCode.METHOD_NOT_ALLOWED, null, null);
    }

    public static CreateResponse unsupportedContentFormat() {
        return new CreateResponse(ResponseCode.UNSUPPORTED_CONTENT_FORMAT, null, null);
    }

    public static CreateResponse internalServerError(String errorMessage) {
        return new CreateResponse(ResponseCode.INTERNAL_SERVER_ERROR, null, errorMessage);
    }
}
