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

import org.eclipse.lwm2m.core.node.LwM2mPath;
import org.eclipse.lwm2m.core.response.BootstrapFinishResponse;

/**
 * Request sent when bootstrap session is finished
 */
public class BootstrapFinishRequest implements DownlinkRequest<BootstrapFinishResponse> {

    @Override
    public LwM2mPath getPath() {
        // not targeting a node.
        return null;
    }

    @Override
    public void accept(DownlinkRequestVisitor visitor) {
        visitor.visit(this);
    }

}
