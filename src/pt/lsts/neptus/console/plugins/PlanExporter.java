/*
 * Copyright (c) 2004-2014 Universidade do Porto - Faculdade de Engenharia
 * Laboratório de Sistemas e Tecnologia Subaquática (LSTS)
 * All rights reserved.
 * Rua Dr. Roberto Frias s/n, sala I203, 4200-465 Porto, Portugal
 *
 * This file is part of Neptus, Command and Control Framework.
 *
 * Commercial Licence Usage
 * Licencees holding valid commercial Neptus licences may use this file
 * in accordance with the commercial licence agreement provided with the
 * Software or, alternatively, in accordance with the terms contained in a
 * written agreement between you and Universidade do Porto. For licensing
 * terms, conditions, and further information contact lsts@fe.up.pt.
 *
 * European Union Public Licence - EUPL v.1.1 Usage
 * Alternatively, this file may be used under the terms of the EUPL,
 * Version 1.1 only (the "Licence"), appearing in the file LICENSE.md
 * included in the packaging of this file. You may not use this work
 * except in compliance with the Licence. Unless required by applicable
 * law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific
 * language governing permissions and limitations at
 * https://www.lsts.pt/neptus/licence.
 *
 * For more information please see <http://lsts.fe.up.pt/neptus>.
 *
 * Author: zp
 * Jul 25, 2014
 */
package pt.lsts.neptus.console.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import pt.lsts.neptus.NeptusLog;
import pt.lsts.neptus.console.ConsoleLayout;
import pt.lsts.neptus.console.ConsolePanel;
import pt.lsts.neptus.plugins.PluginDescription;
import pt.lsts.neptus.plugins.PluginsRepository;
import pt.lsts.neptus.types.mission.plan.IPlanFileExporter;
import pt.lsts.neptus.util.GuiUtils;

/**
 * @author zp
 *
 */
@PluginDescription
public class PlanExporter extends ConsolePanel {
    private static final long serialVersionUID = 5471633324196554012L;

    /**
     * @param console
     */
    public PlanExporter(ConsoleLayout console) {
        super(console);
    }

    @Override
    public void cleanSubPanel() {

    }

    @Override
    public void initSubPanel() {
        for (Class<? extends IPlanFileExporter> exporter : PluginsRepository.listExtensions(IPlanFileExporter.class).values()) {
            try {
                final IPlanFileExporter exp = exporter.newInstance();
                addMenuItem("Tools>Export Plan>"+exp.getExporterName(), null, new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileFilter(GuiUtils.getCustomFileFilter(exp.getExporterName()+" files", exp.validExtensions()));
                        chooser.setDialogTitle("Select destination file");
                        int op = chooser.showSaveDialog(getConsole());
                        if (op != JFileChooser.APPROVE_OPTION)
                            return;
                        try {
                            exp.exportToFile(getConsole().getPlan(), chooser.getSelectedFile());                            
                        }
                        catch (Exception ex) {
                            GuiUtils.errorMessage(getConsole(), ex);
                        }                        
                    }
                });
            }
            catch (Exception e) {
                NeptusLog.pub().error(e);
            }
        }
    }

}
