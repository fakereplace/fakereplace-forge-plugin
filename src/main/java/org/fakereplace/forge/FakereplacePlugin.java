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

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.ShellColor;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;

/**
 * @author Stuart Douglas
 */
@Alias("fakereplace")
@RequiresFacet({JavaSourceFacet.class, MavenCoreFacet.class})
@RequiresProject
@Help("A plugin that helps setting up Fakereplace")
public class FakereplacePlugin implements Plugin {

    private final Project project;
    private final Event<InstallFacets> installFacets;

    @Inject
    public FakereplacePlugin(final Project project, final Event<InstallFacets> event) {
        this.project = project;
        this.installFacets = event;
    }

    @DefaultCommand
    public void status(final PipeOut out) {
        if (project.hasFacet(FakereplaceFacet.class)) {
            out.println("Fakereplace is installed.");
        } else {
            out.println("Fakereplace is not installed. Use 'fakereplace setup' to get started.");
        }
    }

    @Command("setup")
    public void setup(final PipeOut out) {
        if (!project.hasFacet(FakereplaceFacet.class)) {
            installFacets.fire(new InstallFacets(FakereplaceFacet.class));
        }
        if (project.hasFacet(FakereplaceFacet.class)) {
            ShellMessages.success(out, "FakereplaceFacet is configured.");
        }
    }

    @Command("run")
    public void run() {
        final MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
        maven.executeMaven(new String[] {"package", "fakereplace:fakereplace"});
    }

    private void assertInstalled() {
        if (!project.hasFacet(FakereplaceFacet.class)) {
            throw new RuntimeException("FakereplaceFacet is not installed. Use 'fakereplace setup' to get started.");
        }
    }

    @Command("help")
    public void exampleDefaultCommand(@Option final String opt, final PipeOut pipeOut) {
        pipeOut.println(ShellColor.BLUE, "Once this facet is installed use 'fakereplace run' to build your project and run a hot replacement. " +
                "This command simply executes 'mvn package fakereplace:fakereplace'");
    }
}
