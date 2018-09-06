package pt.lsts.neptus.searchlogs;
import com.kitfox.svg.pathcmd.Horizontal;
import pt.lsts.neptus.i18n.I18n;
import pt.lsts.neptus.util.GuiUtils;
import pt.lsts.neptus.util.ImageUtils;
import pt.lsts.neptus.util.conf.ConfigFetch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class NeptusSearchLogs extends JFrame implements ActionListener {
    private static final String SEARCH_LOGS_TITLE = I18n.text("Neptus Logs Search");
    private JCheckBox vehiclesNamesSelectAll;
    private Box vehiclesNamesBox;
    private ArrayList<JCheckBox> vehiclesNamesCheckBox;

    private JCheckBox vehiclesTypeSelectAll;
    private Box vehiclesTypeBox;
    private ArrayList<JCheckBox> vehiclesTypeCheckBox;

    private JCheckBox yearSelectAll;
    private Box yearBox;
    private ArrayList<JCheckBox> yearCheckBox;

    private JButton mapBtn;
    private JButton searchBtn;
    private JButton openToMraBtn;


    /**
     * Constructor
     */
    public NeptusSearchLogs() {
        super(SEARCH_LOGS_TITLE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ConfigFetch.class.getResource("/images/neptus-icon.png")));
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GuiUtils.centerOnScreen(this);

        Container myPanel = this.getContentPane();

        GroupLayout groupLayout = new GroupLayout(myPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        setLayout(groupLayout);

        //Settings
        JPanel settings = new JPanel(new BorderLayout());
        settings.setBackground(Color.red);
        settings.setPreferredSize(new Dimension(1200,50));

        //Results
        JPanel results = new JPanel(new BorderLayout());
        results.setPreferredSize(new Dimension(1200,570));

        // Total
        JPanel total = new JPanel(new BorderLayout());
        total.setPreferredSize(new Dimension(1200,50));

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(settings).addComponent(results).addComponent(total)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(settings))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(results))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(total)));

        settings.add(addSettingsGrid());
        results.add(addResultsGrid());
        total.add(addTotalGrid());

        pack();
        setVisible(true);
    }

    private JPanel addTotalGrid() {

        JPanel resultsGrid = new JPanel(new GridLayout(0,9));

        JPanel totalDistancePnl = new JPanel();
        JLabel totalDistanceLbl = new JLabel("Total Distance");
        Font font = totalDistanceLbl.getFont();
        totalDistanceLbl.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        totalDistancePnl.add(totalDistanceLbl);

        JTextField totalDistanceField = new JTextField();
        totalDistanceField.setText("xxx");
        totalDistanceField.setColumns(10);
        totalDistancePnl.add(totalDistanceField);

        resultsGrid.add(totalDistancePnl);
        resultsGrid.add(new JPanel());

        JPanel totalDurationPnl = new JPanel();
        JLabel totalDurationLbl = new JLabel("Total Duration");
        totalDurationLbl.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        totalDurationPnl.add(totalDurationLbl);

        JTextField totalDurationField = new JTextField();
        totalDurationField.setText("xxx");
        totalDurationField.setColumns(10);
        totalDurationPnl.add(totalDurationField);

        resultsGrid.add(totalDurationPnl);
        resultsGrid.add(new JPanel());

        JPanel minZPnl = new JPanel();
        JLabel minZLbl = new JLabel("Min Z");
        minZLbl.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        minZPnl.add(minZLbl);

        JTextField minZField = new JTextField();
        minZField.setText("xxx");
        minZField.setColumns(10);
        minZPnl.add(minZField);

        resultsGrid.add(minZPnl);
        resultsGrid.add(new JPanel());

        JPanel maxZPnl = new JPanel();
        JLabel maxZLbl = new JLabel("Max Z");
        maxZLbl.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        maxZPnl.add(maxZLbl);

        JTextField maxZField = new JTextField();
        maxZField.setText("xxx");
        maxZField.setColumns(10);
        maxZPnl.add(maxZField);

        resultsGrid.add(maxZPnl);
        resultsGrid.add(new JPanel());

        openToMraBtn = new JButton("Open to MRA");
        openToMraBtn.addActionListener(this);
        resultsGrid.add(openToMraBtn);

        return resultsGrid;
    }

    private JPanel addResultsGrid() {

        JPanel resultsGrid = new JPanel(new BorderLayout());

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension (1200, 570));
        resultsGrid.add(jScrollPane);

        return resultsGrid;
    }

    private JPanel addSettingsGrid() {

        JPanel settingsGrid = new JPanel(new GridLayout(0,7));

        // Name
        settingsGrid.add(addSettingsVehicleName());

        // Type
        settingsGrid.add(addSettingsVehicleType());

        // Year
        settingsGrid.add(addSettingsYear());

        //Distance
        settingsGrid.add(addSettingsDistance());

        //Duration
        settingsGrid.add(addSettingsDuration());

        //Depth
        settingsGrid.add(addSettingsZ());

        //Settings - etc
        settingsGrid.add(addSettingsEtc());

        return settingsGrid;
    }

    private JPanel addSettingsEtc() {

        JPanel etcPanel = new JPanel();

        etcPanel.setLayout(new BoxLayout(etcPanel, BoxLayout.Y_AXIS));

        JPanel containsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        containsPanel.setBorder(BorderFactory.createTitledBorder("Contains"));
        JTextField containsField = new JTextField();
        containsField.setText("");
        containsField.setColumns(10);
        containsField.setAlignmentY(JTextField.LEFT);
        containsPanel.add(containsField);

        etcPanel.add(containsPanel);

        JPanel submap = new JPanel(new FlowLayout());

        mapBtn = new JButton();
        mapBtn.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/mapSearch.png")));
        mapBtn.addActionListener(this);
        submap.add(mapBtn);

        submap.add(new JPanel());

        searchBtn = new JButton("Search");
        searchBtn.addActionListener(this);
        submap.add(searchBtn);

        etcPanel.add(Box.createVerticalStrut(8));
        etcPanel.add(submap);

        return etcPanel;
    }

    private JPanel addSettingsZ() {
        JPanel ZPanel = new JPanel(new GridLayout(2,0));

        JPanel zMinPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        zMinPnl.setBorder(BorderFactory.createTitledBorder("Z Min"));
        JTextField zMinField = new JTextField();
        zMinField.setText("");
        zMinField.setColumns(10);
        zMinField.setAlignmentY(JTextField.LEFT);
        zMinPnl.add(zMinField);

        ZPanel.add(zMinPnl);

        JPanel zMaxPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        zMaxPnl.setBorder(BorderFactory.createTitledBorder("Z Max"));

        JTextField zMaxField = new JTextField();
        zMaxField.setText("");
        zMaxField.setColumns(10);
        zMaxField.setAlignmentY(JTextField.LEFT);
        zMaxPnl.add(zMaxField);

        ZPanel.add(zMaxPnl);

        return ZPanel;
    }

    private JPanel addSettingsDuration() {
        JPanel durationPanel = new JPanel(new GridLayout(2,0));

        JPanel durationMin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationMin.setBorder(BorderFactory.createTitledBorder("Duration Min"));

        JTextField durMinField = new JTextField();
        durMinField.setText("");
        durMinField.setColumns(10);
        durMinField.setAlignmentY(JTextField.LEFT);
        durationMin.add(durMinField);

        durationPanel.add(durationMin);

        JPanel durationMax = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationMax.setBorder(BorderFactory.createTitledBorder("Duration Max"));

        JTextField durMaxField = new JTextField();
        durMaxField.setText("");
        durMaxField.setColumns(10);
        durMinField.setAlignmentY(JTextField.LEFT);
        durationMax.add(durMaxField);

        durationPanel.add(durationMax);

        return durationPanel;
    }

    private JPanel addSettingsDistance() {
        JPanel distancePanel = new JPanel(new GridLayout(2,0));

        JPanel distanceMin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distanceMin.setBorder(BorderFactory.createTitledBorder("Distance Min"));

        JTextField distanceMinField = new JTextField();
        distanceMinField.setText("");
        distanceMinField.setColumns(10);
        distanceMinField.setAlignmentY(JTextField.LEFT);
        distanceMin.add(distanceMinField);

        distancePanel.add(distanceMin);

        JPanel distanceMax = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distanceMax.setBorder(BorderFactory.createTitledBorder("Distance Max"));

        JTextField distanceMaxField = new JTextField();
        distanceMaxField.setText("");
        distanceMaxField.setColumns(10);
        distanceMaxField.setAlignmentY(JTextField.LEFT);
        distanceMax.add(distanceMaxField);

        distancePanel.add(distanceMax);

        return distancePanel;
    }

    private JPanel addSettingsYear() {
        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearPanel.setBorder(BorderFactory.createTitledBorder("Year"));
        yearBox = Box.createVerticalBox();

        //todo select all when this is checked
        yearSelectAll = new JCheckBox("Select all");
        yearSelectAll.addActionListener(this);
        yearBox.add(yearSelectAll);

        yearBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        // todo add year of logs dynamically
        String[] years = {"2018",
                "2017",
                "2016",
                "2015"};

        yearCheckBox = new ArrayList<JCheckBox>();
        for(int i = 0; i < years.length; i++) {
            yearCheckBox.add(new JCheckBox(years[i]));
            yearBox.add(yearCheckBox.get(i));
        }

        JScrollPane jScrollPane = new JScrollPane(yearBox);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension (140, 75));
        yearPanel.add(jScrollPane);

        return yearPanel;
    }

    private JPanel addSettingsVehicleType() {
        JPanel vehicleTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vehicleTypePanel.setBorder(BorderFactory.createTitledBorder("Vehicle Type"));
        vehiclesTypeBox = Box.createVerticalBox();

        //todo select all when this is checked
        vehiclesTypeSelectAll = new JCheckBox("Select all");
        vehiclesTypeSelectAll.addActionListener(this);
        vehiclesTypeBox.add(vehiclesTypeSelectAll);

        vehiclesTypeBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        // todo add type of vehicles dynamically
        String[] vehiclesType = {"type-1",
                "Type-2",
                "Type-3",
                "Type-4"};

        vehiclesTypeCheckBox = new ArrayList<JCheckBox>();
        for(int i = 0; i < vehiclesType.length; i++) {
            vehiclesTypeCheckBox.add(new JCheckBox(vehiclesType[i]));
            vehiclesTypeBox.add(vehiclesTypeCheckBox.get(i));
        }

        JScrollPane jScrollPane = new JScrollPane(vehiclesTypeBox);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension (140, 75));
        vehicleTypePanel.add(jScrollPane);

        return vehicleTypePanel;

    }

    private JPanel addSettingsVehicleName() {
        //Settings - Vehicle name
        JPanel vehicleNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vehicleNamePanel.setBorder(BorderFactory.createTitledBorder("Vehicle Name"));
        vehiclesNamesBox = Box.createVerticalBox();

        //todo select all when this is checked
        vehiclesNamesSelectAll = new JCheckBox("Select all");
        vehiclesNamesSelectAll.addActionListener(this);
        vehiclesNamesBox.add(vehiclesNamesSelectAll);

        vehiclesNamesBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        // todo add names of vehicles dynamically
        String[] vehicles = {"Lauv-noptilus-1",
                "Lauv-noptilus-2",
                "Lauv-noptilus-3",
                "Lauv-xplore-1",
                "Lauv-explore-2",
                "Lauv-noptilus-2",
                "Lauv-noptilus-3",
                "Lauv-xplore-1",
                "Lauv-explore-2",
                "Lauv-noptilus-2",
                "Lauv-noptilus-3",
                "Lauv-xplore-1",
                "Lauv-explore-2"};

        vehiclesNamesCheckBox = new ArrayList<JCheckBox>();
        for(int i = 0; i < vehicles.length; i++) {
            vehiclesNamesCheckBox.add(new JCheckBox(vehicles[i]));
            vehiclesNamesBox.add(vehiclesNamesCheckBox.get(i));
        }

        JScrollPane jScrollPane = new JScrollPane(vehiclesNamesBox);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension (140, 75));
        vehicleNamePanel.add(jScrollPane);

        return vehicleNamePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vehiclesNamesSelectAll) {
            if (vehiclesNamesSelectAll.isSelected()) {
                for (int i = 0; i < vehiclesNamesCheckBox.size(); i++)
                    vehiclesNamesCheckBox.get(i).setSelected(true);
            } else {
                for (int i = 0; i < vehiclesNamesCheckBox.size(); i++)
                    vehiclesNamesCheckBox.get(i).setSelected(false);
            }
        }

        if(e.getSource() == vehiclesTypeSelectAll) {
            if (vehiclesTypeSelectAll.isSelected()) {
                for (int i = 0; i < vehiclesTypeCheckBox.size(); i++)
                    vehiclesTypeCheckBox.get(i).setSelected(true);
            } else {
                for (int i = 0; i < vehiclesNamesCheckBox.size(); i++)
                    vehiclesTypeCheckBox.get(i).setSelected(false);
            }
        }

        if(e.getSource() == yearSelectAll) {
            if (yearSelectAll.isSelected()) {
                for (int i = 0; i < vehiclesNamesCheckBox.size(); i++)
                    yearCheckBox.get(i).setSelected(true);
            } else {
                for (int i = 0; i < vehiclesNamesCheckBox.size(); i++)
                    yearCheckBox.get(i).setSelected(false);
            }
        }

        if(e.getSource() == searchBtn) {
           //todo search logs
            JOptionPane.showMessageDialog(null, "Work ?");
        }

        if(e.getSource() == mapBtn) {
            JOptionPane.showMessageDialog(null, "Under construction...");
        }

        if(e.getSource() == openToMraBtn) {
            JOptionPane.showMessageDialog(null, "Under construction...");
        }
    }
}
