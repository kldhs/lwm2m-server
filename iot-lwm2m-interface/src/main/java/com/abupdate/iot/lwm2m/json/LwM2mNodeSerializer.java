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
package com.abupdate.iot.lwm2m.json;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.node.LwM2mObject;
import org.eclipse.lwm2m.core.node.LwM2mObjectInstance;
import org.eclipse.lwm2m.core.node.LwM2mResource;
import org.eclipse.lwm2m.util.Hex;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LwM2mNodeSerializer implements JsonSerializer<LwM2mNode> {

    @Override
    public JsonElement serialize(LwM2mNode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject element = new JsonObject();

        element.addProperty("id", src.getId());

        if (typeOfSrc == LwM2mObject.class) {
            element.add("instances", context.serialize(((LwM2mObject) src).getInstances().values()));
        } else if (typeOfSrc == LwM2mObjectInstance.class) {
            element.add("resources", context.serialize(((LwM2mObjectInstance) src).getResources().values()));
        } else if (LwM2mResource.class.isAssignableFrom((Class<?>) typeOfSrc)) {
            LwM2mResource rsc = (LwM2mResource) src;
            if (rsc.isMultiInstances()) {
                JsonObject values = new JsonObject();
                for (Entry<Integer, ?> entry : rsc.getValues().entrySet()) {
                    if (rsc.getType() == org.eclipse.lwm2m.core.model.ResourceModel.Type.OPAQUE) {
                        values.add(entry.getKey().toString(),
                                context.serialize(Hex.encodeHex((byte[]) entry.getValue())));
                    } else {
                        values.add(entry.getKey().toString(), context.serialize(entry.getValue()));
                    }
                }
                element.add("values", values);
            } else {
                if (rsc.getType() == org.eclipse.lwm2m.core.model.ResourceModel.Type.OPAQUE) {
                    element.add("value", context.serialize(Hex.encodeHex((byte[]) rsc.getValue())));
                } else {
                    element.add("value", context.serialize(rsc.getValue()));
                }
            }
        }

        return element;
    }
}
