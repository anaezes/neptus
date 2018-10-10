package pt.lsts.neptus.searchlogs;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import pt.lsts.neptus.i18n.I18n;
import pt.lsts.neptus.util.GuiUtils;
import pt.lsts.neptus.util.conf.ConfigFetch;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NeptusSearchLogs extends JFrame implements ActionListener {
    private static final String SEARCH_LOGS_TITLE = I18n.text("Neptus Logs Search");

    private static final String URL = "http://localhost:8001/";
    private static final String USER_AGENT = "java";

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

    private JTextField distanceMinField;
    private JTextField distanceMaxField;
    private JTextField durMinField;
    private JTextField durMaxField;
    private JTextField zMinField;
    private JTextField zMaxField;

    private JPanel resultsGrid;
    private  JDialog dialogPopup;
    private JLabel dialogLabel;

    private JTable resultsTable;
    private JScrollPane jScrollPaneResults;

    private URL url;
    private HttpURLConnection con;
    private ParameterStringBuilder stringBuilder;

    private Multimap<String, String> parametersToSearch;



    /**
     * Constructor
     */
    public NeptusSearchLogs() {
        super(SEARCH_LOGS_TITLE);

        //initConnectionServer();
        stringBuilder = new ParameterStringBuilder();

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

        //Errors and warnings display
        dialogPopup = new JDialog();
        dialogPopup.setUndecorated(true);
        dialogLabel = new JLabel("");

        pack();
        setVisible(true);
    }

    private void initConnectionServer() {
        try {
            url = new URL("http://localhost:8001/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequestToServer(Multimap<String, String> parameters) throws IOException {

        URL url_request = new URL(URL + ParameterStringBuilder.getParamsString(parameters));
        System.out.println(URL + ParameterStringBuilder.getParamsString(parameters));

        HttpURLConnection con = (HttpURLConnection) url_request.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString().trim();
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

        resultsGrid = new JPanel(new BorderLayout());

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

        zMinField = new JTextField();
        zMinField.setText("");
        zMinField.setColumns(10);
        zMinField.setAlignmentY(JTextField.LEFT);
        zMinPnl.add(zMinField);

        ZPanel.add(zMinPnl);

        JPanel zMaxPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        zMaxPnl.setBorder(BorderFactory.createTitledBorder("Z Max"));

        zMaxField = new JTextField();
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

        durMinField = new JTextField();
        durMinField.setText("");
        durMinField.setColumns(10);
        durMinField.setAlignmentY(JTextField.LEFT);
        durationMin.add(durMinField);

        durationPanel.add(durationMin);

        JPanel durationMax = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationMax.setBorder(BorderFactory.createTitledBorder("Duration Max"));


        durMaxField = new JTextField();
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

        distanceMinField = new JTextField();
        distanceMinField.setText("");
        distanceMinField.setColumns(10);
        distanceMinField.setAlignmentY(JTextField.LEFT);
        distanceMin.add(distanceMinField);

        distancePanel.add(distanceMin);

        JPanel distanceMax = new JPanel(new FlowLayout(FlowLayout.LEFT));
        distanceMax.setBorder(BorderFactory.createTitledBorder("Distance Max"));

        distanceMaxField = new JTextField();
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

        yearSelectAll = new JCheckBox("Select all");
        yearSelectAll.addActionListener(this);
        yearBox.add(yearSelectAll);

        yearBox.add(new JSeparator(SwingConstants.HORIZONTAL));


        Multimap<String, String> parameters = ArrayListMultimap.create();
        parameters.put("all-years", "only");

        String[] years = null;

        try {
            String tmp = getRequestToServer(parameters);
            years = getResponseParams(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        vehiclesTypeSelectAll = new JCheckBox("Select all");
        vehiclesTypeSelectAll.addActionListener(this);
        vehiclesTypeBox.add(vehiclesTypeSelectAll);

        vehiclesTypeBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        Multimap<String, String> parameters =  ArrayListMultimap.create();
        parameters.put("all-types", "only");

        String[] types = null;

        try {
            String tmp = getRequestToServer(parameters);
            types = getResponseParams(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        vehiclesTypeCheckBox = new ArrayList<JCheckBox>();
        for(int i = 0; i < types.length; i++) {
            vehiclesTypeCheckBox.add(new JCheckBox(types[i]));
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

        vehiclesNamesSelectAll = new JCheckBox("Select all");
        vehiclesNamesSelectAll.addActionListener(this);
        vehiclesNamesBox.add(vehiclesNamesSelectAll);

        vehiclesNamesBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        Multimap<String, String> parameters =  ArrayListMultimap.create();
        parameters.put("all-vehicles", "only");

        String[] vehicles = null;

        try {
            String tmp = getRequestToServer(parameters);
            vehicles = getResponseParams(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private String cutHeader(String response) {
        String match = "OK";
        int position = response.indexOf(match);

        return response.substring(position + 3);
    }

    private String[] getResponseParams(String response) {

        String result = cutHeader(response);

        result = result.replace("[", "");
        result = result.replace("]", "");
        result = result.replace("'", "");
        result = result.replace("(", "");
        result = result.replace(")", "");
        result = result.replace(",", "");

        return result.split(" ");
    }


    private ArrayList<String> getResponseLogs(String response) {
        String logs = cutHeader(response);

        Pattern pattern = Pattern.compile("('([a-zA-Z0-9-_]*\\/)*Data.lsf.gz', " +
                "'[a-zA-Z0-9-]+', '[a-z]{3}', ([0-9]{4}|'unknown'), " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?+, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?, " +
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)(, |)");

        Matcher matcher = pattern.matcher(logs);

        ArrayList<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    private String[] getResponseErrorsWarnings(String response) {

        String tmp = cutHeader(response);

        tmp = tmp.replace("[", "");
        tmp = tmp.replace("]", "");

        String[] result = tmp.split("\", ");

        if(result.length == 2) {
            result[0] = result[0].substring(2);
            result[1] = result[1].substring(1, result[1].length() - 2);
        }

        return result;
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
                for (int i = 0; i < yearCheckBox.size(); i++)
                    yearCheckBox.get(i).setSelected(true);
            } else {
                for (int i = 0; i < yearCheckBox.size(); i++)
                    yearCheckBox.get(i).setSelected(false);
            }
        }

        if(e.getSource() == searchBtn) {

            if(parametersToSearch == null)
                parametersToSearch = ArrayListMultimap.create();
            else
                parametersToSearch.clear();

            for (JCheckBox box : vehiclesNamesCheckBox) {
                if(box.isSelected())
                    parametersToSearch.put("vehicle",box.getText());
            }

            for (JCheckBox box : yearCheckBox) {
                if(box.isSelected())
                    parametersToSearch.put("year",box.getText());
            }

            for (JCheckBox box : vehiclesTypeCheckBox) {
                if(box.isSelected())
                    parametersToSearch.put("type",box.getText());
            }


            if(!distanceMinField.getText().isEmpty()) {
                parametersToSearch.removeAll("minDistTravelled");
                parametersToSearch.put("minDistTravelled", distanceMinField.getText());
            }

            if(!distanceMaxField.getText().isEmpty()) {
                parametersToSearch.removeAll("maxDistTravelled");
                parametersToSearch.put("maxDistTravelled", distanceMaxField.getText());
            }

            if(!durMinField.getText().isEmpty()) {
                parametersToSearch.removeAll("minDuration");
                parametersToSearch.put("minDuration", durMinField.getText());
            }

            if(!durMaxField.getText().isEmpty()) {
                parametersToSearch.removeAll("maxDuration");
                parametersToSearch.put("maxDuration", durMaxField.getText());
            }

            if(!zMinField.getText().isEmpty()) {
                parametersToSearch.removeAll("minDepth");
                parametersToSearch.put("minDepth", zMinField.getText());
            }

            if(!zMaxField.getText().isEmpty()) {
                parametersToSearch.removeAll("maxDepth");
                parametersToSearch.put("maxDepth", zMaxField.getText());
            }


            ArrayList<String> logs = null;

            try {
                String tmp = getRequestToServer(parametersToSearch);
                logs = getResponseLogs(tmp);
            } catch (IOException error) {
                error.printStackTrace();
            }

            showLogs(logs);

        }

        if(e.getSource() == mapBtn) {
            JOptionPane.showMessageDialog(null, "Under construction...");
        }

        if(e.getSource() == openToMraBtn) {
            JOptionPane.showMessageDialog(null, "Under construction...");
        }
    }

    private void showLogs(ArrayList<String> logs) {

        String rowData[][] = new String[logs.size()][11];

        for (int i = 0; i < logs.size(); i++) {
            String tmp[] = logs.get(i).split(", ");
            rowData[i] = tmp;
        }

        String columnNames[] = {"Name", "Vehicle", "Type", "Year", "Distance", "Latitude", "Longitude", "Date", "Duration", "Depth", "Altitude"};

        if(resultsTable == null) {
            resultsTable = new JTable(new DefaultTableModel(rowData, columnNames)) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            //todo para fazer download verificar quais estão selecionadas
            resultsTable.setRowSelectionAllowed(true);
            resultsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            addListennerToTable();

            jScrollPaneResults = new JScrollPane((resultsTable));
            jScrollPaneResults.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneResults.setPreferredSize(new Dimension (1200, 570));
            resultsGrid.add(jScrollPaneResults);
            setVisible(true);
        }

        else {
            DefaultTableModel model = (DefaultTableModel)resultsTable.getModel();
            model.setDataVector(rowData, columnNames);
            resultsTable.setModel(model);
        }

        pack();
        resultsTable.revalidate();
        resultsGrid.revalidate();
     }

    private void addListennerToTable() {

        resultsTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){displayPopup(e);}
            @Override
            public void mouseReleased(MouseEvent e){hidePopup(e);}
        });
    }

    private void hidePopup(MouseEvent e) {
        dialogPopup.addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e){
                dialogPopup.setVisible(false);
            }
        });
    }

    //TODO GUARDAR CRITÉRIOS DE PESQUISA ATUAIS E FALTA SENSORES
    private void displayPopup(MouseEvent e){
        int selectedRow = resultsTable.rowAtPoint(e.getPoint());
        JTable table =(JTable) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        //Map<String, String> parameters = new HashMap<>();
        if(resultsTable.getValueAt(row,0).toString() == null)
            return;

        parametersToSearch.removeAll("name");
        parametersToSearch.put("name", resultsTable.getValueAt(row,0).toString());

        String[] otherInfo = null;

        try {
            String tmp = getRequestToServer(parametersToSearch);
            otherInfo = getResponseErrorsWarnings(tmp);
        } catch (IOException error) {
            error.printStackTrace();
        }

        if(otherInfo.length != 2)
            return;

        dialogLabel.setText("<html><b>Errors</b>< " + otherInfo[0] + " <br><b>Warnings</b> " + otherInfo[1]);

        int rowHeight = resultsTable.getRowHeight();
        Point tableLocation = resultsTable.getLocationOnScreen();

        dialogPopup.add(dialogLabel);
        JPanel contentPane = (JPanel)dialogPopup.getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
        dialogPopup.pack();
        dialogPopup.setBounds(tableLocation.x,tableLocation.y +((selectedRow+1)*rowHeight),(int)resultsTable.getWidth(), (int)resultsTable.getRowHeight()*5);
        dialogPopup.requestFocus();
        dialogPopup.setVisible(true);
    }
}