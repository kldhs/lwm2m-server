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
package org.eclipse.lwm2m.server.model;

import java.util.Collection;

import org.eclipse.lwm2m.core.model.LwM2mModel;
import org.eclipse.lwm2m.core.model.ObjectModel;
import org.eclipse.lwm2m.server.registration.Registration;

/**
 * A LwM2mModelProvider which uses only one model for all registered clients.
 */
public class StaticModelProvider implements LwM2mModelProvider {
    private final LwM2mModel model;

    public StaticModelProvider(Collection<ObjectModel> objects) {
        this(new LwM2mModel(objects));
    }

    public StaticModelProvider(LwM2mModel model) {
        this.model = model;
    }

    @Override
    public LwM2mModel getObjectModel(Registration registration) {
        // same model for all clients
        return model;
    }
}
