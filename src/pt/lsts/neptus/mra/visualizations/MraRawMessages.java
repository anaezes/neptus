/*
 * Copyright (c) 2004-2015 Universidade do Porto - Faculdade de Engenharia
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
 * Author: zp, Manuel Ribeiro
 * Dec 2, 2015
 */
package pt.lsts.neptus.mra.visualizations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import pt.lsts.imc.lsf.LsfIndex;
import pt.lsts.neptus.mra.MRAPanel;
import pt.lsts.neptus.mra.importers.IMraLogGroup;
import pt.lsts.neptus.plugins.PluginDescription;
import pt.lsts.neptus.plugins.messages.Java2sAutoTextField;
import pt.lsts.neptus.util.ImageUtils;
import pt.lsts.neptus.util.llf.MessageHtmlVisualization;
import pt.lsts.neptus.util.llf.RawMessagesTableModel;
import pt.lsts.neptus.util.llf.SortedComboBoxModel;

/**
 * @author zp
 * @author Manuel Ribeiro
 */
@PluginDescription(icon = "pt/lsts/neptus/mra/visualizations/doc-search.png")

public class MraRawMessages extends SimpleMRAVisualization {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private ArrayList<Integer> resultList = new ArrayList<>();
    private int finderNextIndex = -1;
    private int currFinderIndex = -1;
    private int currSelectedIndex = -1;
    private FinderFrame find;
    AbstractAction finderAction;

    public MraRawMessages(MRAPanel panel) {
        super(panel);
    }

    @Override
    public Type getType() {
        return Type.TABLE;
    }

    @Override
    public boolean canBeApplied(IMraLogGroup source) {
        return true;
    }

    @Override
    public String getName() {
        return "All Messages";
    }

    @Override
    public void onHide() {
        mraPanel.getActionMap().remove("finder");
        find.close();
    }

    @Override
    public void onShow() {
        mraPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), "finder");
        mraPanel.getActionMap().put("finder", finderAction);
    }

    @Override
    public JComponent getVisualization(IMraLogGroup source, double timestep) {
        final LsfIndex index = source.getLsfIndex();
        table = new JTable(new RawMessagesTableModel(source.getLsfIndex()));
        Color defColor = table.getSelectionBackground();

        find = setupFinder();

        finderAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                find.setVisible(true);
            }
        };

        mraPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), "finder");
        mraPanel.getActionMap().put("finder", finderAction);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 5, 5, 0));
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout());

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.EAST);
        panel_1.setLayout(new BorderLayout(2, 0));

        JButton highlightBtn = new JButton();

        highlightBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        highlightBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        highlightBtn.setIcon(ImageUtils.getIcon("images/buttons/lights.png"));

        highlightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                find.toggleHighlight();
            }
        });

        panel_1.add(highlightBtn, BorderLayout.EAST);

        JButton findBtn = new JButton();
        findBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        findBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        findBtn.setIcon(ImageUtils.getIcon("images/buttons/show.png"));

        findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!find.isVisible())
                    find.setVisible(true);
                else
                    find.setVisible(false);
            }
        });
        panel_1.add(findBtn, BorderLayout.WEST);



        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currSelectedIndex = table.getSelectedRow();
                table.setSelectionBackground(defColor);

                if (e.getClickCount() == 2) {
                    mraPanel.loadVisualization(new MessageHtmlVisualization(index.getMessage(table.getSelectedRow())),
                            true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        return contentPane;
    }
    
    /* Validates if a time (HH:mm) is within a specified limit (start and end)
     * @param target, time to be check against lower and upper bounds 
     * @return true if time is within limits, false otherwise
     */
    private boolean isBetweenTime(String target, String start, String end) {
        if (start.isEmpty() && end.isEmpty())
            return true;
        else
            if ((target.compareTo(start) >= 0) && (target.compareTo(end) <= 0))
                return true;

        return false;
    }

    /* Checks if there are messages in the list that have a specific type, source, source_entity, 
     * destination and are within specified time limits 
     * @param src, TYPE of the message
     * @param srcEnt, SOURCE_ENTITY of the message
     * @return true if there is at least one element, false otherwise
     */
    private boolean findMessage(String type, String src, String srcEnt, String dest, String time1, String time2) {
        if (!resultList.isEmpty()) {
            hasNext();
            return true;
        } else {

            table.setRowSelectionAllowed(true);
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            for (int row = 0; row < table.getRowCount(); row++) {
                String rowTime = (String) table.getValueAt(row, 1); //Time
                String rowType = (String) table.getValueAt(row, 2);
                String rowSrc = (String) table.getValueAt(row, 3); //Source
                String rowSrcEnt = (String) table.getValueAt(row, 4); //SourceEntity
                String rowDest = (String) table.getValueAt(row, 5); //Destination
                if (rowSrcEnt == null || rowSrcEnt.isEmpty())
                    rowSrcEnt = "empty";

                rowTime = rowTime.split("\\s+")[1]; //only hh:mm are important
                String[] hhmm = rowTime.split(":");
                String HHMM = hhmm[0].concat(":").concat(hhmm[1]);

                if (rowType.contains(type) || type.equals("<ANY>")) {
                    if (rowSrc.contains(src) || src.equals("<ANY>")) {
                        if (rowSrcEnt.contains(srcEnt) || srcEnt.equals("<ANY>")) {
                            if (rowDest.contains(dest) || dest.equals("<ANY>")) {
                                if (isBetweenTime(HHMM, time1, time2))
                                    resultList.add(row);
                            }
                        }
                    }
                }
            }

            if (!resultList.isEmpty()) {
                table.clearSelection();
                table.setSelectionBackground(Color.yellow);
                table.addRowSelectionInterval(resultList.get(0), resultList.get(0));
                table.scrollRectToVisible(new Rectangle(table.getCellRect(resultList.get(0), 0, true)));
                table.repaint();
                finderNextIndex++;
                return true;
            }

            return false;
        }
    }

    /* Checks if there's a previous element to be show 
     * @return true if there is, false otherwise
     */
    private boolean hasPrev() {
        if (finderNextIndex == 0 || finderNextIndex == -1)
            return false;

        if (currSelectedIndex <= currFinderIndex) {
            finderNextIndex--;
        }
        if (finderNextIndex >= resultList.size())
            return false;

        currFinderIndex = resultList.get(finderNextIndex);
        currSelectedIndex = currFinderIndex;
        table.scrollRectToVisible(new Rectangle(table.getCellRect(currFinderIndex, 0, true)));
        table.clearSelection();
        table.addRowSelectionInterval(currFinderIndex, currFinderIndex);
        table.setSelectionBackground(Color.yellow);

        return true;
    }
    
    /* Checks if there's a next element to be show 
     * @return true if there is, false otherwise
     */
    private boolean hasNext() {
        if ((finderNextIndex >= resultList.size() - 1) && currSelectedIndex >= currFinderIndex) 
            return false;

        if (currSelectedIndex >= currFinderIndex) {
            finderNextIndex++;
        }

        if (finderNextIndex < resultList.size()) {
            currFinderIndex = resultList.get(finderNextIndex);
            currSelectedIndex = currFinderIndex;

            table.scrollRectToVisible(new Rectangle(table.getCellRect(currFinderIndex, 0, true)));
            table.clearSelection();
            table.addRowSelectionInterval(currFinderIndex, currFinderIndex);
            table.setSelectionBackground(Color.yellow);

            return true;
        }

        return false;
    }
    
    /* Clears every result found and table selection 
     * 
     */
    private void clearSelection() {
        currFinderIndex = -1;
        finderNextIndex = -1;
        resultList.clear();
        table.clearSelection();
        table.repaint();
    }

    private FinderFrame setupFinder(){

        FinderFrame find = new FinderFrame();
        ArrayList<String> typeList = new ArrayList<>();

        for (int row = 0; row < table.getRowCount(); row++) {
            String type = (String) table.getValueAt(row, 2); //Type
            String src = (String) table.getValueAt(row, 3); //Source
            String srcEntity = (String) table.getValueAt(row, 4); //SourceEntity
            String dest = (String) table.getValueAt(row, 5); //Destination

            if (!typeList.contains(type))
                typeList.add(type);

            find.addItemToBox(find.sourceCBox, src);
            find.addItemToBox(find.sourceEntCBox, srcEntity);
            find.addItemToBox(find.destCBox, dest);
        }
        typeList.add("<ANY>");
        Collections.sort(typeList, String.CASE_INSENSITIVE_ORDER);
        find.initTypeField(typeList);

        return find;
    }

    private class FinderFrame extends JFrame {
        private static final long serialVersionUID = 1L;

        private Java2sAutoTextField typeTxt;
        private JComboBox<String> sourceCBox, sourceEntCBox, destCBox;
        private JTextField ts1, ts2;
        private JButton prevBtn, findBtn;
        private JLabel statusLbl;
        private boolean hightlighted = false;
        private boolean toValidateT1 = false;
        private boolean toValidateT2 = false;

        public FinderFrame() {
            initComponents();
        }

        /* Checks if string has valid time in format HH:mm
         * @param time, string to be validate
         * @returns true, if valid time, false otherwise
         */
        private boolean isValidTime(String time) {

            return (time.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$") || time.isEmpty());
        }

        /* Highlights found rows or clears every highlighted one
         * 
         */
        public void toggleHighlight() {
            if (!hightlighted) {
                if (!resultList.isEmpty()) {
                    table.clearSelection();
                    table.setSelectionBackground(Color.yellow);
                    for (Integer row : resultList) {
                        table.addRowSelectionInterval(row, row);
                    }
                    hightlighted = true;
                }
            }
            else {
                table.clearSelection();
                hightlighted = false;
            }
        }
        
        /* Turn this frame invisible and dispose
         * 
         */

        public void close() {
            setVisible(false); //you can't see me!
            dispose();
        }

        private void initComponents() {
            setTitle("Find");
            setSize(290, 195);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);

            getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));
            SortedComboBoxModel<String> model1 = new SortedComboBoxModel<String>(new String[] {"<ANY>"});
            SortedComboBoxModel<String> model2 = new SortedComboBoxModel<String>(new String[] {"<ANY>"});
            SortedComboBoxModel<String> model3 = new SortedComboBoxModel<String>(new String[] {"<ANY>"});

            JLabel typeLabel = new JLabel("Type:");
            JLabel sourceLabel = new JLabel("Source:");
            JLabel sourceEntCLabel = new JLabel("Src. Ent:");
            sourceCBox = new JComboBox<String>(model1);
            sourceEntCBox = new JComboBox<String>(model2);
            JLabel destLabel = new JLabel("Dest:");
            destCBox = new JComboBox<String>(model3);
            JLabel timeLbl = new JLabel("Time:");
            ts1 = new JTextField();
            JLabel separatorLbl = new JLabel("-");
            ts2 = new JTextField();
            JButton clearBtn = new JButton("Clear");
            prevBtn = new JButton("Prev.");
            findBtn = new JButton("Find");
            statusLbl = new JLabel();

            Font defFont = new Font("Dialog", Font.BOLD, 11);
            typeLabel.setFont(defFont);
            sourceLabel.setFont(defFont);
            sourceCBox.setFont(defFont);
            sourceEntCLabel.setFont(defFont);
            sourceEntCBox.setFont(defFont);
            destLabel.setFont(defFont);
            destCBox.setFont(defFont);
            timeLbl.setFont(defFont);
            ts1.setFont(defFont);
            separatorLbl.setFont(defFont);
            ts2.setFont(defFont);
            statusLbl.setFont(defFont);

            ts1.setMaximumSize(new Dimension(42, 18));
            ts1.setColumns(4);
            ts2.setMaximumSize(new Dimension(42, 18) );
            ts2.setColumns(4);

            prevBtn.setHorizontalAlignment(SwingConstants.RIGHT);
            findBtn.setHorizontalAlignment(SwingConstants.RIGHT);

            getContentPane().add(typeLabel, "cell 0 0,alignx trailing");
            getContentPane().add(sourceLabel, "cell 0 1,alignx trailing");
            getContentPane().add(sourceCBox, "cell 1 1,growx");
            getContentPane().add(sourceEntCLabel, "cell 0 2,alignx trailing");
            getContentPane().add(sourceEntCBox, "cell 1 2,growx");
            getContentPane().add(destLabel, "cell 0 3,alignx trailing");
            getContentPane().add(destCBox, "cell 1 3,growx");
            getContentPane().add(timeLbl, "cell 0 4,alignx trailing");
            getContentPane().add(ts1, "flowx,cell 1 4,growx");
            getContentPane().add(separatorLbl, "cell 1 4");
            getContentPane().add(ts2, "flowx,cell 1 4");
            getContentPane().add(clearBtn, "cell 0 5");
            getContentPane().add(prevBtn, "flowx,cell 1 5,alignx right");
            getContentPane().add(findBtn, "cell 1 5,alignx right");
            getContentPane().add(statusLbl, "cell 1 6,alignx right");

            clearBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearSelection();
                    findBtn.setText("Find");
                    statusLbl.setText("");
                    setSize(290, 195);
                }
            });

            prevBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    hasPrev();
                    updateStatus();

                }
            });

            findBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean found = findMessage(typeTxt.getText(), (String) sourceCBox.getSelectedItem(), 
                            (String) sourceEntCBox.getSelectedItem(), (String) destCBox.getSelectedItem(),
                            (String) ts1.getText(), (String) ts2.getText());
                    if (found) {
                        findBtn.setText("Next");
                        updateStatus();
                    } 
                    else {
                        JOptionPane.showMessageDialog(null,
                                "No message with current selected filter has been found.",
                                "Find",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            ts1.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    toValidateT1 = true;
                }

                public void focusLost(FocusEvent e) {
                    if (!isValidTime(ts1.getText()) && toValidateT1){
                        JOptionPane.showMessageDialog(null,
                                "No valid time format HH:mm entered.",
                                "Find",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    toValidateT1 = false;
                }

            });

            ts2.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    toValidateT2 = true;
                }

                public void focusLost(FocusEvent e) {
                    if (!isValidTime(ts2.getText()) && toValidateT2){
                        JOptionPane.showMessageDialog(null,
                                "No valid time format HH:mm entered.",
                                "Find",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    toValidateT2 = false;
                }

            });

        }

        /* Updates status label with number of found elements 
         * 
         */
        private void updateStatus() {
            setSize(290, 210);
            statusLbl.setText(finderNextIndex+1 + " of "+resultList.size());
        }
        
        /* Setup a special JTextField (called Java2sAutoTextField) that has autocomplete feature
         * @param items, list of items that will be suggested in the autocomplete feature
         */
        public void initTypeField(ArrayList<String> items) {
            typeTxt = new Java2sAutoTextField(items);
            typeTxt.setFont(new Font("Dialog", Font.PLAIN, 11));
            typeTxt.setColumns(10);
            typeTxt.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        typeTxt.selectAll();
                    }
                }
            });

            getContentPane().add(typeTxt, "cell 1 0,growx");
        }

        /* Adds items to a combobox validating them first
         * @param box, box to be populated
         * @param item, item to be added to box contents.
         */
        public void addItemToBox(JComboBox<String> box, String item) {
            if (((DefaultComboBoxModel<String>) box.getModel()).getIndexOf(item) == -1 && item != null) {
                box.addItem((String) item);
            }
        }
    }
}