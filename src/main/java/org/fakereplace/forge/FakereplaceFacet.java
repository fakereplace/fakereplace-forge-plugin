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

import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.maven.MavenPluginFacet;
import org.jboss.forge.maven.plugins.MavenPlugin;
import org.jboss.forge.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.shell.ShellPrintWriter;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.RequiresFacet;

/**
 * @author Stuart Douglas
 */
@Alias("org.fakereplace")
@RequiresFacet({DependencyFacet.class, MavenPluginFacet.class})
public class FakereplaceFacet extends BaseFacet {

    public static final String FAKEREPLACE_VERSION_PROP_NAME = "version.fakereplace";
    public static final String FAKEREPLACE_VERSION_PROP = "${" + FAKEREPLACE_VERSION_PROP_NAME + "}";

    @Inject
    private ShellPrompt shell;

    @Inject
    private ShellPrintWriter writer;

    @Override
    public boolean install() {
        writer.println();
        final DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
        final MavenPluginFacet plugins = project.getFacet(MavenPluginFacet.class);
        DependencyBuilder dependency = createFakereplaceDependency();
        if (!plugins.hasPlugin(dependency)) {
            final List<Dependency> dependencies = dependencyFacet.resolveAvailableVersions(dependency);
            final Dependency fakereplace = shell.promptChoiceTyped("Which version of Fakereplace do you want to install?", dependencies, DependencyUtil.getLatestNonSnapshotVersion(dependencies));
            dependencyFacet.setProperty(FAKEREPLACE_VERSION_PROP_NAME, fakereplace.getVersion());

            dependency.setVersion(FAKEREPLACE_VERSION_PROP);
            final MavenPlugin plugin = MavenPluginBuilder.create().setDependency(dependency);
            plugins.addPlugin(plugin);
        }
        return true;
    }

    @Override
    public boolean isInstalled() {
        if (getProject().hasFacet(MavenPluginFacet.class)) {
            final MavenPluginFacet plugins = project.getFacet(MavenPluginFacet.class);
            final DependencyBuilder fakereplace = createFakereplaceDependency();
            if (plugins.hasPlugin(fakereplace)) {
                return true;
            }
        }
        return false;
    }
    private DependencyBuilder createFakereplaceDependency() {
        return DependencyBuilder.create()
                .setGroupId("org.fakereplace")
                .setArtifactId("fakereplace-maven-plugin");
    }
}
