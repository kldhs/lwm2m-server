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
package org.eclipse.lwm2m.core.node.codec;

import java.util.List;

import org.eclipse.lwm2m.core.model.LwM2mModel;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.node.LwM2mPath;
import org.eclipse.lwm2m.core.node.TimestampedLwM2mNode;
import org.eclipse.lwm2m.core.request.ContentFormat;

public interface LwM2mNodeDecoder {

    /**
     * Deserializes a binary content into a {@link LwM2mNode}.
     * <p>
     * The type of the returned node depends on the path argument.
     *
     * @param content the content
     * @param format  the content format
     * @param path    the path of the node to build
     * @param model   the collection of supported object models
     * @return the resulting node
     * @throws CodecException if there payload is malformed.
     */
    LwM2mNode decode(byte[] content, ContentFormat format, LwM2mPath path, LwM2mModel model) throws CodecException;

    /**
     * Deserializes a binary content into a {@link LwM2mNode} of the expected type.
     *
     * @param content   the content
     * @param format    the content format
     * @param path      the path of the node to build
     * @param model     the collection of supported object models
     * @param nodeClass the class of the {@link LwM2mNode} to decode
     * @return the resulting node
     * @throws CodecException if there payload is malformed.
     */
    <T extends LwM2mNode> T decode(byte[] content, ContentFormat format, LwM2mPath path, LwM2mModel model,
                                   Class<T> nodeClass) throws CodecException;

    /**
     * Deserializes a binary content into a list of time-stamped {@link LwM2mNode} ordering by time-stamp.
     *
     * @param content the content
     * @param format  the content format
     * @param path    the path of the node to build
     * @param model   the collection of supported object models
     * @return the resulting list of time-stamped {@link LwM2mNode} ordering by time-stamp
     * @throws CodecException if there payload is malformed.
     */
    List<TimestampedLwM2mNode> decodeTimestampedData(byte[] content, ContentFormat format, LwM2mPath path,
                                                     LwM2mModel model) throws CodecException;

    /**
     * return true is the given {@link ContentFormat} is supported
     */
    boolean isSupported(ContentFormat format);

}
