/*******************************************************************************
 * Copyright (c) 2017 Sierra Wireless and others.
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

import org.eclipse.lwm2m.core.model.ResourceModel.Type;
import org.eclipse.lwm2m.core.node.LwM2mPath;

/**
 * Helper to convert value "magically" from one type to another.
 */
public interface LwM2mValueConverter {

    /**
     * Convert the given value to the expected type given in parameter.
     *
     * @param value        the value to convert
     * @param currentType  the current type of the value
     * @param expectedType the type expected
     * @param resourcePath the path of the concerned resource
     * @throws CodecException the value is not convertible.
     */
    Object convertValue(Object value, Type currentType, Type expectedType, LwM2mPath resourcePath);

}
