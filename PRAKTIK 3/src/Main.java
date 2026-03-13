import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// 1. Кастомная непроверяемая ошибка
class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

// 2. Перечисление цен
enum Prices {
    Эконом(100),
    СТАНДАРТ(200),
    ПРО(300),
    ЛУКС(500),
    УЛТРА_ЛУКС(1000);

    private final int price;

    Prices(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

// 3. Абстрактный класс Room
abstract class Room {
    protected int roomNumber;
    protected int maxCapacity;
    protected int pricePerNight;
    protected boolean isBooked;

    public Room(int roomNumber, int maxCapacity, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, Prices price) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = price.getPrice();
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    @Override
    public String toString() {
        return "Комната " + roomNumber + " | " + maxCapacity + " мест | $" + pricePerNight +
                " | " + (isBooked ? "ЗАНЯТА" : "Свободна");
    }

    public abstract String getType();
}

// 4. Класс EconomyRoom
class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public EconomyRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Эконом";
    }
}

// 5. Абстрактный класс ProRoom
abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public ProRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public ProRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }
}

// 6. Класс StandardRoom
class StandardRoom extends ProRoom {
    public StandardRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public StandardRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Стандарт";
    }
}

// 7. Класс LuxRoom
class LuxRoom extends ProRoom {
    public LuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public LuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Лукс";
    }
}

// 8. Класс UltraLuxRoom
class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "УлтраЛукс";
    }
}

// 9. Интерфейс RoomService
interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

// 10. Интерфейс LuxRoomService
interface LuxRoomService<T extends LuxRoom> extends RoomService<T> {
    void foodDelivery(T room);
}

// 11. Реализация RoomService
class RoomServiceImpl<T extends Room> implements RoomService<T> {

    @Override
    public void clean(T room) {
        room.setBooked(false);
    }

    @Override
    public void reserve(T room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Комната N" + room.getRoomNumber() + " уже забронирована!");
        }
        room.setBooked(true);
    }

    @Override
    public void free(T room) {
        room.setBooked(false);
    }
}

// 12. Реализация LuxRoomService
class LuxRoomServiceImpl<T extends LuxRoom> implements LuxRoomService<T> {

    private final RoomService<T> baseService = new RoomServiceImpl<>();

    @Override
    public void clean(T room) {
        baseService.clean(room);
    }

    @Override
    public void reserve(T room) {
        baseService.reserve(room);
    }

    @Override
    public void free(T room) {
        baseService.free(room);
    }

    @Override
    public void foodDelivery(T room) {
        // Доставка еды доступна
    }
}

// 13. Главное окно приложения
public class Main extends JFrame {
    private List<Room> rooms;
    private RoomServiceImpl<Room> roomService;
    private LuxRoomServiceImpl<LuxRoom> luxRoomService;

    private JComboBox<String> roomTypeCombo;
    private JTextField roomNumberField;
    private JTextField capacityField;
    private JComboBox<Prices> priceCombo;

    private JPanel roomsPanel;
    private JLabel statusLabel;
    private JLabel statsLabel;
    private JComboBox<String> actionRoomCombo;

    // Цветовая схема
    private final Color PRIMARY_COLOR = new Color(88, 86, 214);
    private final Color SECONDARY_COLOR = new Color(118, 111, 252);
    private final Color ACCENT_COLOR = new Color(236, 72, 153);
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);
    private final Color WARNING_COLOR = new Color(245, 158, 11);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color BG_COLOR = new Color(249, 250, 251);
    private final Color CARD_BG = Color.WHITE;

    public Main() {
        rooms = new ArrayList<>();
        roomService = new RoomServiceImpl<>();
        luxRoomService = new LuxRoomServiceImpl<>();

        setTitle("Hotel Management System - Premium Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_COLOR);

        // Header Panel с градиентом
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Left Panel - Create Room
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0.4;
        mainContentPanel.add(createCreateRoomPanel(), gbc);

        // Right Panel - Actions
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0.4;
        mainContentPanel.add(createActionsPanel(), gbc);

        // Bottom Panel - Rooms List
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        mainContentPanel.add(createRoomsListPanel(), gbc);

        add(mainContentPanel, BorderLayout.CENTER);

        // Status Bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Градиентный фон
                GradientPaint gp = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Декоративные круги
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(50, 20, 100, 100);
                g2d.fillOval(getWidth() - 150, 40, 80, 80);
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 100));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Hotel Management System", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JLabel subtitleLabel = new JLabel("Premium Room Management Interface", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        header.add(textPanel, BorderLayout.CENTER);

        statsLabel = new JLabel("Rooms: 0 | Free: 0 | Booked: 0", SwingConstants.RIGHT);
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsLabel.setForeground(Color.WHITE);
        header.add(statsLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createCreateRoomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(4, 0, 0, 0, PRIMARY_COLOR),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Заголовок секции
        JLabel headerLabel = new JLabel("Создание новой комнаты");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(PRIMARY_COLOR);
        panel.add(headerLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Тип комнаты
        panel.add(createStyledLabel("Тип комнаты:"), gbc);
        gbc.gridx = 1;
        roomTypeCombo = createStyledComboBox(new String[]{"Эконом", "Стандарт", "Лукс", "УлтраЛукс"});
        panel.add(roomTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createStyledLabel("Номер:"), gbc);
        gbc.gridx = 1;
        roomNumberField = createStyledTextField();
        panel.add(roomNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createStyledLabel("Вместимость:"), gbc);
        gbc.gridx = 1;
        capacityField = createStyledTextField();
        panel.add(capacityField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createStyledLabel("Категория цены:"), gbc);
        gbc.gridx = 1;
        priceCombo = createStyledComboBox(Prices.values());
        panel.add(priceCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton createButton = createModernButton("Создать комнату", SUCCESS_COLOR);
        createButton.addActionListener(e -> createRoom());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(4, 0, 0, 0, ACCENT_COLOR),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        // Заголовок секции
        JLabel headerLabel = new JLabel("Управление комнатами");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(ACCENT_COLOR);
        panel.add(headerLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(createStyledLabel("Выберите комнату:"), gbc);

        gbc.gridy++;
        actionRoomCombo = createStyledComboBox(new String[]{});
        actionRoomCombo.setPreferredSize(new Dimension(0, 40));
        panel.add(actionRoomCombo, gbc);

        // Кнопки действий
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 5, 10, 5);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setBackground(CARD_BG);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton reserveBtn = createModernButton("Забронировать", DANGER_COLOR);
        reserveBtn.addActionListener(e -> reserveRoom());
        buttonsPanel.add(reserveBtn);

        JButton freeBtn = createModernButton("Освободить", SUCCESS_COLOR);
        freeBtn.addActionListener(e -> freeRoom());
        buttonsPanel.add(freeBtn);

        JButton cleanBtn = createModernButton("Уборка", WARNING_COLOR);
        cleanBtn.addActionListener(e -> cleanRoom());
        buttonsPanel.add(cleanBtn);

        JButton foodBtn = createModernButton("Доставка еды", new Color(139, 92, 246));
        foodBtn.addActionListener(e -> foodDelivery());
        buttonsPanel.add(foodBtn);

        panel.add(buttonsPanel, gbc);

        return panel;
    }

    private JPanel createRoomsListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(4, 0, 0, 0, new Color(16, 185, 129)),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                )
        ));

        // Заголовок
        JLabel headerLabel = new JLabel("Список всех комнат");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(SUCCESS_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);

        // Панель для комнат с прокруткой
        roomsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        roomsPanel.setBackground(BG_COLOR);
        roomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(roomsPanel);
        scrollPane.setBackground(BG_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(PRIMARY_COLOR);
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        statusLabel = new JLabel("Готов к работе", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(Color.WHITE);

        JLabel versionLabel = new JLabel("v2.0 Premium Edition", SwingConstants.RIGHT);
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        versionLabel.setForeground(new Color(255, 255, 255, 150));

        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(versionLabel, BorderLayout.EAST);

        return statusBar;
    }

    // Helper методы для создания стилизованных компонентов
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(new Color(249, 250, 251));
        field.setPreferredSize(new Dimension(0, 40));
        return field;
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(0, 40));
        combo.setBackground(Color.WHITE);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                if (isSelected) {
                    label.setBackground(PRIMARY_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(55, 65, 81));
                }
                return label;
            }
        });
        return combo;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(0, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Эффект при наведении
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
            int capacity = capacityField.getText().trim().isEmpty() ?
                    (int)(Math.random() * 4) + 1 : Integer.parseInt(capacityField.getText().trim());
            Prices price = (Prices) priceCombo.getSelectedItem();
            String type = (String) roomTypeCombo.getSelectedItem();

            Room room = null;
            switch (type) {
                case "Эконом":
                    room = new EconomyRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Стандарт":
                    room = new StandardRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Лукс":
                    room = new LuxRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "УлтраЛукс":
                    room = new UltraLuxRoom(roomNumber, capacity, price.getPrice());
                    break;
            }

            if (room != null) {
                rooms.add(room);
                updateRoomCombo();
                updateRoomsList();
                updateHeaderStats();
                showStatus("Комната " + roomNumber + " (" + type + ") успешно создана", SUCCESS_COLOR);

                roomNumberField.setText("");
                capacityField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Пожалуйста, введите корректные числовые значения",
                    "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE);
            showStatus("Ошибка при создании комнаты", DANGER_COLOR);
        }
    }

    private void updateRoomCombo() {
        actionRoomCombo.removeAllItems();
        for (Room room : rooms) {
            String status = room.isBooked() ? "ЗАНЯТА" : "Свободна";
            actionRoomCombo.addItem("N" + room.getRoomNumber() + " - " + room.getType() + " - " + status);
        }
    }

    private void updateRoomsList() {
        roomsPanel.removeAll();

        if (rooms.isEmpty()) {
            JLabel emptyLabel = new JLabel("Комнаты пока не созданы. Создайте первую комнату!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(156, 163, 175));
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
            roomsPanel.add(emptyLabel);
        } else {
            for (Room room : rooms) {
                JPanel roomCard = createRoomCard(room);
                roomsPanel.add(roomCard);
            }
        }

        roomsPanel.revalidate();
        roomsPanel.repaint();
    }

    private JPanel createRoomCard(Room room) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Левая часть - информация
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setOpaque(false);

        infoPanel.add(createInfoLabel("Номер:"));
        infoPanel.add(createInfoValue(String.valueOf(room.getRoomNumber())));

        infoPanel.add(createInfoLabel("Тип:"));
        infoPanel.add(createInfoValue(room.getType()));

        infoPanel.add(createInfoLabel("Вместимость:"));
        infoPanel.add(createInfoValue(room.getMaxCapacity() + " чел."));

        infoPanel.add(createInfoLabel("Цена:"));
        infoPanel.add(createInfoValue("$" + room.getPricePerNight()));

        infoPanel.add(createInfoLabel("Статус:"));
        JLabel statusLabel = new JLabel(room.isBooked() ? "ЗАНЯТА" : "Свободна");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(room.isBooked() ? DANGER_COLOR : SUCCESS_COLOR);
        infoPanel.add(statusLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Правая часть - индикатор типа
        JPanel typePanel = new JPanel();
        typePanel.setOpaque(false);
        typePanel.setPreferredSize(new Dimension(100, 80));

        Color typeColor = getTypeColor(room.getType());
        JLabel typeBadge = new JLabel(room.getType(), SwingConstants.CENTER);
        typeBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeBadge.setForeground(Color.WHITE);
        typeBadge.setOpaque(true);
        typeBadge.setBackground(typeColor);
        typeBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(typeColor.darker(), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        typePanel.add(typeBadge);
        card.add(typePanel, BorderLayout.EAST);

        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(107, 114, 128));
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(17, 24, 39));
        return label;
    }

    private Color getTypeColor(String type) {
        switch (type) {
            case "Economy": return new Color(107, 114, 128);
            case "Standard": return new Color(59, 130, 246);
            case "Lux": return new Color(168, 85, 247);
            case "UltraLux": return new Color(236, 72, 153);
            default: return Color.GRAY;
        }
    }

    private void updateHeaderStats() {
        int total = rooms.size();
        long booked = rooms.stream().filter(Room::isBooked).count();
        long free = total - booked;

        if (statsLabel != null) {
            statsLabel.setText(String.format("Rooms: %d | Free: %d | Booked: %d",
                    total, free, booked));
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color == DANGER_COLOR ? new Color(254, 202, 202) : Color.WHITE);
    }

    private void reserveRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Выберите комнату", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Room room = rooms.get(actionRoomCombo.getSelectedIndex());
            roomService.reserve(room);
            updateRoomCombo();
            updateRoomsList();
            updateHeaderStats();
            showStatus("Комната " + room.getRoomNumber() + " забронирована", SUCCESS_COLOR);
            JOptionPane.showMessageDialog(this,
                    "Комната " + room.getRoomNumber() + " успешно забронирована!",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RoomAlreadyBookedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            showStatus(e.getMessage(), DANGER_COLOR);
        }
    }

    private void freeRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Выберите комнату", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.free(room);
        updateRoomCombo();
        updateRoomsList();
        updateHeaderStats();
        showStatus("Комната " + room.getRoomNumber() + " освобождена", SUCCESS_COLOR);
        JOptionPane.showMessageDialog(this,
                "Комната " + room.getRoomNumber() + " освобождена",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cleanRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Выберите комнату", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.clean(room);
        updateRoomCombo();
        updateRoomsList();
        updateHeaderStats();
        showStatus("В комнате " + room.getRoomNumber() + " проведена уборка", new Color(245, 158, 11));
        JOptionPane.showMessageDialog(this,
                "В комнате " + room.getRoomNumber() + " проведена уборка",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void foodDelivery() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Выберите комнату", "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());

        if (!(room instanceof LuxRoom)) {
            JOptionPane.showMessageDialog(this,
                    "Доставка еды доступна только для номеров категорий Lux и UltraLux!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            showStatus("Доставка еды недоступна для " + room.getType(), DANGER_COLOR);
            return;
        }

        luxRoomService.foodDelivery((LuxRoom) room);
        showStatus("Заказана доставка еды в комнату " + room.getRoomNumber(), new Color(139, 92, 246));
        JOptionPane.showMessageDialog(this,
                "Заказана доставка еды в комнату " + room.getRoomNumber() + "\n" +
                        "Тип номера: " + room.getType() + "\n" +
                        "Приятного аппетита!",
                "Доставка еды",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}