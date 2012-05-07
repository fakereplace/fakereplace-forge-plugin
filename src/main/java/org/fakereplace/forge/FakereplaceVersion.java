/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.fakereplace.forge;

/**
 * @author Stuart Douglas
 */
public enum FakereplaceVersion {

    FAKEREPLACE_1_0_0_Alpha1("org.fakereplace:fakereplace-plugin", "1.0.0.Alpha1");

    private final String ga;
    private final String version;

    private FakereplaceVersion(final String ga, final String version) {
        this.ga = ga;
        this.version = version;
    }

    public String getGa() {
        return ga;
    }

    public String getVersion() {
        return version;
    }
}
