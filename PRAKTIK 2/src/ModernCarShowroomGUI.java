import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ModernCarShowroomGUI extends JFrame {
    private CarCenter carCenter;
    private JTabbedPane tabbedPane;
    private JPanel pnlTotalCars, pnlTotalValue, pnlAvgPrice;
    private DefaultTableModel tableModel;
    private JTable carsTableInTab;

    // Modern Color Scheme - Teal/Purple Gradient
    private final Color PRIMARY_BG = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color ACCENT_TEAL = new Color(6, 182, 212);
    private final Color ACCENT_PURPLE = new Color(139, 92, 246);
    private final Color ACCENT_PINK = new Color(236, 72, 153);
    private final Color ACCENT_GREEN = new Color(16, 185, 129);
    private final Color ACCENT_ORANGE = new Color(245, 158, 11);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color GRADIENT_1 = new Color(99, 102, 241);
    private final Color GRADIENT_2 = new Color(168, 85, 247);
    private final Color GRADIENT_3 = new Color(236, 72, 153);

    public ModernCarShowroomGUI() {
        carCenter = new CarCenter("Modern Motors");
        addTestCars();

        setTitle("Modern Motors - Автомобили будущего");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void addTestCars() {
        Random random = new Random();
        String[] manufacturers = {"Tesla", "BMW", "Mercedes", "Audi", "Porsche", "Lexus"};
        String[] models = {"Model S", "iX", "EQS", "e-tron", "Taycan", "RZ"};
        CarCategory[] categories = {CarCategory.ELECTRIC, CarCategory.SUV, CarCategory.SEDAN, CarCategory.COUPE};

        for (int i = 0; i < 20; i++) {
            String vin = "MOD" + String.format("%05d", i);
            String manufacturer = manufacturers[random.nextInt(manufacturers.length)];
            String model = models[random.nextInt(models.length)];
            int year = random.nextInt(10) + 2016;
            int mileage = random.nextInt(150000);
            double price = random.nextInt(10000000) + 3000000;
            CarCategory category = categories[random.nextInt(categories.length)];

            carCenter.addVehicle(new Car(vin, model, manufacturer, year, mileage, price, category));
        }
    }

    private void initComponents() {
        getContentPane().setBackground(PRIMARY_BG);

        JPanel headerPanel = createModernHeader();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM); // ВКЛАДКИ ВНИЗУ
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPane.setPreferredSize(new Dimension(getWidth(), 60));

        UIManager.put("TabbedPane.background", new Color(30, 41, 59));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.selected", new Color(99, 102, 241));
        UIManager.put("TabbedPane.tabAreaBackground", new Color(30, 41, 59));
        UIManager.put("TabbedPane.contentAreaColor", PRIMARY_BG);
        UIManager.put("TabbedPane.selectedTabTitleForeground", Color.WHITE);
        UIManager.put("TabbedPane.unselectedTabTitleForeground", new Color(148, 163, 184));

        tabbedPane.addTab("Обзор", createDashboardPanel());
        tabbedPane.addTab("Автопарк", createInventoryPanel());
        tabbedPane.addTab("Поиск", createSearchPanel());
        tabbedPane.addTab("Аналитика", createAnalyticsPanel());
        tabbedPane.addTab("Добавить", createAddCarPanel());

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setBackgroundAt(i, new Color(30, 41, 59));
            tabbedPane.setForegroundAt(i, Color.red);
        }

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createModernHeader() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient1 = new GradientPaint(0, 0, GRADIENT_1, getWidth()/2, 0, GRADIENT_2);
                GradientPaint gradient2 = new GradientPaint(getWidth()/2, 0, GRADIENT_2, getWidth(), 0, GRADIENT_3);

                g2d.setPaint(gradient1);
                g2d.fillRect(0, 0, getWidth()/2, 100);
                g2d.setPaint(gradient2);
                g2d.fillRect(getWidth()/2, 0, getWidth()/2, 100);
            }
        };

        panel.setPreferredSize(new Dimension(getWidth(), 100));
        panel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("MODERN MOTORS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // СНАЧАЛА ТАБЛИЦА (сверху)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Текущий автопарк");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(ACCENT_TEAL);
        tablePanel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег", "Цена", "Категория"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        styleModernTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(CARD_BG);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel);
        panel.add(Box.createVerticalStrut(25));

        // ПОТОМ СТАТИСТИКА (снизу)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setBackground(PRIMARY_BG);
        statsPanel.setMaximumSize(new Dimension(1100, 150));

        pnlTotalCars = createModernStatCard("Всего авто", "0", new Color(6, 182, 212));
        pnlTotalValue = createModernStatCard("Общая стоимость", "0 RUB", new Color(16, 185, 129));
        pnlAvgPrice = createModernStatCard("Средняя цена", "0 RUB", new Color(236, 72, 153));

        statsPanel.add(pnlTotalCars);
        statsPanel.add(pnlTotalValue);
        statsPanel.add(pnlAvgPrice);

        panel.add(statsPanel);

        SwingUtilities.invokeLater(this::updateDashboard);
        return panel;
    }

    private JPanel createModernStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30),
                        getWidth(), getHeight(), new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 10)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setMaximumSize(new Dimension(350, 150));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void styleModernTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(42);
        table.setSelectionBackground(new Color(99, 102, 241, 40));
        table.setGridColor(new Color(51, 65, 85));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_LIGHT);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(99, 102, 241));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(99, 102, 241));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        };

        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(CARD_BG);
                setForeground(TEXT_LIGHT);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblManuf = new JLabel("Производитель:");
        lblManuf.setForeground(TEXT_LIGHT);
        filterPanel.add(lblManuf);

        JComboBox<String> comboManufacturer = new JComboBox<>(new String[]{"Все", "Tesla", "BMW", "Mercedes", "Audi", "Porsche", "Lexus"});
        comboManufacturer.setPreferredSize(new Dimension(160, 35));
        comboManufacturer.setBackground(PRIMARY_BG);
        comboManufacturer.setForeground(Color.red);
        filterPanel.add(comboManufacturer);

        filterPanel.add(Box.createHorizontalStrut(140));

        JLabel lblCat = new JLabel("Категория:");
        lblCat.setForeground(TEXT_LIGHT);
        filterPanel.add(lblCat);

        JComboBox<CarCategory> comboCategory = new JComboBox<>(CarCategory.values());
        comboCategory.setPreferredSize(new Dimension(160, 35));
        comboCategory.setBackground(PRIMARY_BG);
        comboCategory.setForeground(Color.red);
        filterPanel.add(comboCategory);

        JButton btnFilter = createModernButton("Фильтр", ACCENT_TEAL);
        btnFilter.addActionListener(e -> filterInventory(comboManufacturer.getSelectedItem().toString(),
                comboCategory.getSelectedItem()));
        filterPanel.add(btnFilter);

        JButton btnReset = createModernButton("Сброс", ACCENT_ORANGE);
        btnReset.addActionListener(e -> {
            comboManufacturer.setSelectedIndex(0);
            comboCategory.setSelectedIndex(0);
            updateInventoryTable(new ArrayList<>(carCenter.getVehiclesSortedByYear()));
        });
        filterPanel.add(btnReset);

        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег", "Цена", "Категория"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleModernTable(table);
        carsTableInTab = table;

        List<Car> vehicles = new ArrayList<>(carCenter.getVehiclesSortedByYear());
        for (Car vehicle : vehicles) {
            model.addRow(new Object[]{
                    vehicle.getVin(),
                    vehicle.getManufacturer(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    String.format("%,d", vehicle.getMileage()),
                    String.format("%,d", (int)vehicle.getPrice()),
                    vehicle.getCategory()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(CARD_BG);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void filterInventory(String manufacturer, Object category) {
        List<Car> filtered = carCenter.getVehiclesSortedByYear().stream()
                .filter(v -> manufacturer.equals("Все") || v.getManufacturer().equals(manufacturer))
                .filter(v -> category == null || v.getCategory() == category)
                .collect(Collectors.toList());

        updateInventoryTable(filtered);
    }

    private void updateInventoryTable(List<Car> vehicles) {
        if (carsTableInTab != null) {
            DefaultTableModel model = (DefaultTableModel) carsTableInTab.getModel();
            model.setRowCount(0);
            for (Car vehicle : vehicles) {
                model.addRow(new Object[]{
                        vehicle.getVin(),
                        vehicle.getManufacturer(),
                        vehicle.getModel(),
                        vehicle.getYear(),
                        String.format("%,d", vehicle.getMileage()),
                        String.format("%,d", (int)vehicle.getPrice()),
                        vehicle.getCategory()
                });
            }
        }
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        inputPanel.setBackground(CARD_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_TEAL, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblFromYear = new JLabel("С года:");
        lblFromYear.setForeground(TEXT_LIGHT);
        inputPanel.add(lblFromYear);

        JTextField txtFromYear = createModernTextField();
        inputPanel.add(txtFromYear);

        JLabel lblToYear = new JLabel("По год:");
        lblToYear.setForeground(TEXT_LIGHT);
        inputPanel.add(lblToYear);

        JTextField txtToYear = createModernTextField();
        inputPanel.add(txtToYear);

        JButton btnSearch = createModernButton("Поиск", ACCENT_GREEN);
        btnSearch.setPreferredSize(new Dimension(140, 40));
        btnSearch.addActionListener(e -> searchByYear(txtFromYear, txtToYear));
        inputPanel.add(btnSearch);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(CARD_BG);
        textArea.setForeground(TEXT_LIGHT);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textArea.setText("Введите диапазон лет для поиска\n\nПример:\nС года: 2018\nПо год: 2022");

        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField(8);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(120, 35));
        field.setBackground(PRIMARY_BG);
        field.setForeground(TEXT_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private void searchByYear(JTextField txtFrom, JTextField txtTo) {
        try {
            int fromYear = Integer.parseInt(txtFrom.getText().trim());
            int toYear = Integer.parseInt(txtTo.getText().trim());

            if (fromYear > toYear) {
                JOptionPane.showMessageDialog(this, "Год 'С года' не может быть больше 'По год'!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Car> filtered = carCenter.getVehiclesSortedByYear().stream()
                    .filter(v -> v.getYear() >= fromYear && v.getYear() <= toYear)
                    .collect(Collectors.toList());

            JTextArea textArea = null;
            Component[] components = ((JPanel) tabbedPane.getComponentAt(2)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    textArea = (JTextArea) ((JScrollPane) comp).getViewport().getView();
                    break;
                }
            }

            if (textArea != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("=============================================================\n");
                sb.append("  РЕЗУЛЬТАТЫ ПОИСКА: ").append(fromYear).append(" - ").append(toYear).append("\n");
                sb.append("=============================================================\n\n");
                sb.append("Найдено: ").append(filtered.size()).append(" автомобилей\n\n");

                int currentYear = 2025;
                int totalAge = 0;
                for (Car v : filtered) {
                    totalAge += (currentYear - v.getYear());
                }
                double avgAge = filtered.isEmpty() ? 0 : (double) totalAge / filtered.size();

                sb.append("Средний возраст: ").append(String.format("%.1f", avgAge)).append(" лет\n\n");
                sb.append("-------------------------------------------------------------\n");

                int num = 1;
                for (Car v : filtered) {
                    sb.append(String.format("%2d. %s %s (%d)\n", num++, v.getManufacturer(), v.getModel(), v.getYear()));
                    sb.append(String.format("    VIN: %s | Пробег: %,d км | Цена: %,d RUB | %s\n",
                            v.getVin(), v.getMileage(), (int)v.getPrice(), v.getCategory()));
                    sb.append("-------------------------------------------------------------\n");
                }

                textArea.setText(sb.toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите корректные года!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // СНАЧАЛА создаем textArea
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(CARD_BG);
        textArea.setForeground(TEXT_LIGHT);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textArea.setText("Настройте максимальный пробег и нажмите 'Выполнить анализ'\n\n" +
                "Будут показаны:\n" +
                "- Автомобили с пробегом менее указанного\n" +
                "- ТОП-3 самые дорогие\n" +
                "- Средний пробег\n" +
                "- Группировка по производителю");

        // Панель с ползунком и кнопкой
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(CARD_BG);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        controlPanel.setMaximumSize(new Dimension(900, 150));

        // Верхняя панель с меткой и ползунком
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        sliderPanel.setBackground(CARD_BG);
        sliderPanel.setMaximumSize(new Dimension(800, 60));

        JLabel lblMileage = new JLabel("Макс. пробег:");
        lblMileage.setForeground(TEXT_LIGHT);
        lblMileage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sliderPanel.add(lblMileage);

        JSlider sliderMileage = new JSlider(JSlider.HORIZONTAL, 0, 200000, 50000);
        sliderMileage.setPreferredSize(new Dimension(400, 50));
        sliderMileage.setBackground(CARD_BG);
        sliderMileage.setForeground(ACCENT_TEAL);
        sliderMileage.setPaintTicks(true);
        sliderMileage.setPaintLabels(true);
        sliderMileage.setMajorTickSpacing(50000);
        sliderMileage.setMinorTickSpacing(10000);
        sliderMileage.setSnapToTicks(true);

        JLabel lblMileageValue = new JLabel("50,000 км");
        lblMileageValue.setForeground(ACCENT_GREEN);
        lblMileageValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMileageValue.setPreferredSize(new Dimension(100, 30));

        sliderMileage.addChangeListener(e -> {
            int value = sliderMileage.getValue();
            lblMileageValue.setText(String.format("%,d км", value));
        });

        sliderPanel.add(sliderMileage);
        sliderPanel.add(lblMileageValue);

        // Нижняя панель с кнопкой
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setMaximumSize(new Dimension(800, 60));

        JButton btnExecute = createModernButton("Выполнить анализ", ACCENT_PURPLE);
        btnExecute.setPreferredSize(new Dimension(350, 50));
        btnExecute.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExecute.addActionListener(e -> executeAnalytics(textArea, sliderMileage.getValue()));

        buttonPanel.add(btnExecute);

        controlPanel.add(sliderPanel);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(buttonPanel);

        panel.add(controlPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private void executeAnalytics(JTextArea textArea, int maxMileage) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================================\n");
        sb.append("  АНАЛИТИКА АВТОПАРКА (Пробег < ");
        sb.append(String.format("%,d", maxMileage));
        sb.append(" км)\n");
        sb.append("=============================================================\n\n");

        List<Car> vehicles = new ArrayList<>(carCenter.getVehiclesSortedByYear());

        sb.append("1. АВТОМОБИЛИ С ПРОБЕГОМ < ");
        sb.append(String.format("%,d", maxMileage));
        sb.append(" КМ:\n");
        sb.append("-------------------------------------------------------------\n");
        List<Car> lowMileage = vehicles.stream()
                .filter(v -> v.getMileage() < maxMileage)
                .collect(Collectors.toList());
        sb.append("Найдено: ").append(lowMileage.size()).append(" автомобилей\n\n");
        for (Car v : lowMileage) {
            sb.append(String.format("   - %s %s (%d) - %,d км\n",
                    v.getManufacturer(), v.getModel(), v.getYear(), v.getMileage()));
        }
        sb.append("\n");

        sb.append("2. ТОП-3 САМЫЕ ДОРОГИЕ:\n");
        sb.append("-------------------------------------------------------------\n");
        List<Car> top3 = vehicles.stream()
                .sorted(Comparator.comparingDouble(Car::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
        for (int i = 0; i < top3.size(); i++) {
            sb.append(String.format("   %d. %s %s - %,d RUB\n",
                    i+1, top3.get(i).getManufacturer(), top3.get(i).getModel(), (int)top3.get(i).getPrice()));
        }
        sb.append("\n");

        double avgMileage = vehicles.stream()
                .mapToInt(Car::getMileage)
                .average()
                .orElse(0);
        sb.append("3. СРЕДНИЙ ПРОБЕГ: ").append(String.format("%,d", (int)avgMileage)).append(" км\n\n");

        sb.append("4. ГРУППИРОВКА ПО ПРОИЗВОДИТЕЛЮ:\n");
        sb.append("-------------------------------------------------------------\n");
        Map<String, List<Car>> byManufacturer = vehicles.stream()
                .collect(Collectors.groupingBy(Car::getManufacturer));
        byManufacturer.forEach((manufacturer, list) ->
                sb.append(String.format("   %s: %d автомобилей\n", manufacturer, list.size())));

        textArea.setText(sb.toString());
    }
    private JPanel createAddCarPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setMaximumSize(new Dimension(650, 380));

        JTextField txtVin = createModernTextField();
        JTextField txtModel = createModernTextField();
        JTextField txtManufacturer = createModernTextField();
        JTextField txtYear = createModernTextField();
        JTextField txtMileage = createModernTextField();
        JTextField txtPrice = createModernTextField();

        JComboBox<CarCategory> comboCategory = new JComboBox<>(CarCategory.values());
        comboCategory.setBackground(PRIMARY_BG);
        comboCategory.setForeground(TEXT_LIGHT);
        comboCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboCategory.setPreferredSize(new Dimension(200, 35));
        comboCategory.setOpaque(true);

        formPanel.add(createStyledLabel("VIN:"));
        formPanel.add(txtVin);
        formPanel.add(createStyledLabel("Модель:"));
        formPanel.add(txtModel);
        formPanel.add(createStyledLabel("Производитель:"));
        formPanel.add(txtManufacturer);
        formPanel.add(createStyledLabel("Год выпуска:"));
        formPanel.add(txtYear);
        formPanel.add(createStyledLabel("Пробег (км):"));
        formPanel.add(txtMileage);
        formPanel.add(createStyledLabel("Цена (RUB):"));
        formPanel.add(txtPrice);
        formPanel.add(createStyledLabel("Категория:"));
        formPanel.add(comboCategory);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton btnAdd = createModernButton("Добавить автомобиль", ACCENT_GREEN);
        btnAdd.setPreferredSize(new Dimension(270, 45));
        btnAdd.addActionListener(e -> addCar(txtVin, txtModel, txtManufacturer, txtYear, txtMileage, txtPrice, comboCategory));
        panel.add(btnAdd);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ACCENT_TEAL);
        return label;
    }

    private void addCar(JTextField txtVin, JTextField txtModel, JTextField txtManufacturer,
                        JTextField txtYear, JTextField txtMileage, JTextField txtPrice,
                        JComboBox<CarCategory> comboCategory) {
        try {
            Car car = new Car(
                    txtVin.getText().trim(),
                    txtModel.getText().trim(),
                    txtManufacturer.getText().trim(),
                    Integer.parseInt(txtYear.getText().trim()),
                    Integer.parseInt(txtMileage.getText().trim()),
                    Double.parseDouble(txtPrice.getText().trim()),
                    (CarCategory) comboCategory.getSelectedItem()
            );

            if (carCenter.addVehicle(car)) {
                JOptionPane.showMessageDialog(this, "Автомобиль успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);

                txtVin.setText("");
                txtModel.setText("");
                txtManufacturer.setText("");
                txtYear.setText("");
                txtMileage.setText("");
                txtPrice.setText("");

                updateDashboard();
                updateInventoryTable(new ArrayList<>(carCenter.getVehiclesSortedByYear()));
            } else {
                JOptionPane.showMessageDialog(this, "Автомобиль с таким VIN уже существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Проверьте формат чисел!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDashboard() {
        List<Car> vehicles = new ArrayList<>(carCenter.getVehiclesSortedByYear());

        if (pnlTotalCars != null) {
            Component[] components = pnlTotalCars.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 36) {
                        label.setText(String.valueOf(vehicles.size()));
                        label.setForeground(ACCENT_TEAL);
                    }
                }
            }
        }

        double totalValue = vehicles.stream().mapToDouble(Car::getPrice).sum();
        if (pnlTotalValue != null) {
            Component[] components = pnlTotalValue.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 36) {
                        label.setText(String.format("%,d", (int)totalValue) + " RUB");
                        label.setForeground(ACCENT_GREEN);
                    }
                }
            }
        }

        double avgPrice = vehicles.size() > 0 ? vehicles.stream().mapToDouble(Car::getPrice).average().orElse(0) : 0;
        if (pnlAvgPrice != null) {
            Component[] components = pnlAvgPrice.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 36) {
                        label.setText(String.format("%,d", (int)avgPrice) + " RUB");
                        label.setForeground(ACCENT_PINK);
                    }
                }
            }
        }

        tableModel.setRowCount(0);
        for (Car vehicle : vehicles) {
            tableModel.addRow(new Object[]{
                    vehicle.getVin(),
                    vehicle.getManufacturer(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    String.format("%,d", vehicle.getMileage()),
                    String.format("%,d", (int)vehicle.getPrice()),
                    vehicle.getCategory()
            });
        }
    }
}