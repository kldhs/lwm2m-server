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
package com.abupdate.iot.lwm2m.redis.serialization;

import org.eclipse.lwm2m.Link;
import org.eclipse.lwm2m.ResponseCode;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.response.CreateResponse;
import org.eclipse.lwm2m.core.response.DeleteResponse;
import org.eclipse.lwm2m.core.response.DiscoverResponse;
import org.eclipse.lwm2m.core.response.ExecuteResponse;
import org.eclipse.lwm2m.core.response.LwM2mResponse;
import org.eclipse.lwm2m.core.response.ObserveResponse;
import org.eclipse.lwm2m.core.response.ReadResponse;
import org.eclipse.lwm2m.core.response.WriteAttributesResponse;
import org.eclipse.lwm2m.core.response.WriteResponse;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 * Functions for serialize and deserialize a LWM2M response in JSON.
 */
public class ResponseSerDes {

    public static JsonObject jSerialize(LwM2mResponse r) {
        final JsonObject o = Json.object();
        o.add("code", r.getCode().toString());

        if (r.isFailure()) {
            o.add("errorMessage", r.getErrorMessage());
            return o;
        }

        if (r instanceof ReadResponse) {
            o.add("kind", "read");
            o.add("content", LwM2mNodeSerDes.jSerialize(((ReadResponse) r).getContent()));
        } else if (r instanceof ObserveResponse) {
            o.add("kind", "observe");
            o.add("content", LwM2mNodeSerDes.jSerialize(((ReadResponse) r).getContent()));
        } else if (r instanceof DiscoverResponse) {
            o.add("kind", "discover");
            o.add("objectLinks", Link.serialize(((DiscoverResponse) r).getObjectLinks()));
        } else if (r instanceof DeleteResponse) {
            o.add("kind", "delete");
        } else if (r instanceof ExecuteResponse) {
            o.add("kind", "execute");
        } else if (r instanceof WriteResponse) {
            o.add("kind", "write");
        } else if (r instanceof WriteAttributesResponse) {
            o.add("kind", "writeAttributes");
        } else if (r instanceof CreateResponse) {
            o.add("kind", "create");
            o.add("location", ((CreateResponse) r).getLocation());
        }

        return o;
    }

    public static String sSerialize(LwM2mResponse r) {
        return jSerialize(r).toString();
    }

    public static byte[] bSerialize(LwM2mResponse r) {
        return jSerialize(r).toString().getBytes();
    }

    public static LwM2mResponse deserialize(JsonObject o) {
        String sCode = o.getString("code", null);
        if (sCode == null)
            throw new IllegalStateException("Invalid response missing code attribute");
        ResponseCode code = Enum.valueOf(ResponseCode.class, sCode);

        String errorMessage = o.getString("errorMessage", null);

        String kind = o.getString("kind", null);
        switch (kind) {
            case "observe": {
                // TODO ser Observation
                LwM2mNode content = LwM2mNodeSerDes.deserialize((JsonObject) o.get("content"));
                return new ObserveResponse(code, content, null, null, errorMessage);
            }
            case "delete":
                return new DeleteResponse(code, errorMessage);
            case "discover":
                String objectLinks = o.getString("objectLinks", "");
                return new DiscoverResponse(code, Link.parse(objectLinks.getBytes()), errorMessage);
            case "create": {
                String location = o.getString("location", null);
                return new CreateResponse(code, location, errorMessage);
            }
            case "execute":
                return new ExecuteResponse(code, errorMessage);
            case "writeAttributes": {
                return new WriteAttributesResponse(code, errorMessage);
            }
            case "write": {
                return new WriteResponse(code, errorMessage);
            }
            case "read": {
                LwM2mNode content = LwM2mNodeSerDes.deserialize((JsonObject) o.get("content"));
                return new ReadResponse(code, content, errorMessage);
            }
            default:
                throw new IllegalStateException("Invalid response missing kind attribute");
        }
    }
}
