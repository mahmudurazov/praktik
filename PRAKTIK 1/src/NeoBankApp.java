import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

public class NeoBankApp extends JFrame {
    private BankAccount account;
    private BankAccount otherAccount;
    private UserProfile userProfile;

    private JLabel lblNumber, lblOwner, lblBalance, lblOpenDate, lblStatus;
    private JTextField txtDepositAmount, txtWithdrawAmount, txtTransferAmount;
    private JButton btnDeposit, btnWithdraw, btnTransfer, btnBlock, btnRefresh;
    private JLabel lblStatusMessage;
    private JLabel avatarLabel;

    // === КРАСИВАЯ ЦВЕТОВАЯ СХЕМА ===
    private final Color PRIMARY_COLOR = new Color(102, 126, 234);
    private final Color PRIMARY_DARK = new Color(88, 101, 242);
    private final Color SECONDARY_COLOR = new Color(236, 72, 153);
    private final Color ACCENT_COLOR = new Color(139, 92, 246);
    private final Color BG_COLOR = new Color(241, 245, 249);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_DARK = new Color(15, 23, 42);
    private final Color TEXT_LIGHT = new Color(71, 85, 105);
    private final Color SUCCESS_COLOR = new Color(34, 197, 94);

    public NeoBankApp() {
        account = new BankAccount("Клиент");
        otherAccount = new BankAccount("Получатель");
        otherAccount.deposit(100000);

        userProfile = new UserProfile("Иван", "Иванов", "ivan@example.com", "+7 (999) 000-00-00");

        setTitle("NeoBank");
        setSize(520, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();

        JOptionPane.showMessageDialog(this,
                "Добро пожаловать, " + userProfile.getFirstName() + "!\n\n" +
                        "Номер счёта для перевода:\n" + otherAccount.getNumber(),
                "NeoBank",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {
        // === КРАСИВЫЙ ФОН С ГРАДИЕНТОМ И ДЕКОРАТИВНЫМИ ЭЛЕМЕНТАМИ ===
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


// Простой градиент с двумя цветами
                        GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(230, 231, 245),
                        0, getHeight(), new Color(226, 232, 240)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

// Декоративные круги
                g2d.setColor(new Color(102, 126, 234, 20));
                g2d.fillOval(-150, 50, 400, 400);

                g2d.setColor(new Color(236, 72, 153, 15));
                g2d.fillOval(250, 350, 300, 300);

                g2d.setColor(new Color(139, 92, 246, 12));
                g2d.fillOval(50, 600, 250, 250);

                g2d.setColor(new Color(59, 130, 246, 18));
                g2d.fillOval(350, 100, 120, 120);

                g2d.setColor(new Color(16, 185, 129, 10));
                g2d.fillOval(150, 450, 100, 100);
            }
        };

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false); // Важно! Чтобы фон панели был прозрачным

        // === ВЕРХНЯЯ ПАНЕЛЬ С АВАТАРКОЙ ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255, 200));
        headerPanel.setMaximumSize(new Dimension(480, 100));

        // Аватарка (кликабельная)
        avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Градиентный круг
                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Буква имени
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
                String initial = userProfile.getFirstName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(initial, x, y);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatarLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openProfileDialog();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                avatarLabel.setToolTipText("Нажмите для просмотра профиля");
            }
        });

        // Информация о пользователе
        JPanel userInfoPanel = new JPanel(new GridLayout(2, 1));
        userInfoPanel.setBackground(new Color(255, 255, 255, 0));

        JLabel lblUserName = new JLabel(userProfile.getFullName());
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUserName.setForeground(TEXT_DARK);

        JLabel lblUserEmail = new JLabel(userProfile.getEmail());
        lblUserEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUserEmail.setForeground(TEXT_LIGHT);

        userInfoPanel.add(lblUserName);
        userInfoPanel.add(lblUserEmail);

        headerPanel.add(avatarLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Кнопка настроек
        JButton btnSettings = new JButton("⚙");
        btnSettings.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnSettings.setForeground(TEXT_LIGHT);
        btnSettings.setContentAreaFilled(false);
        btnSettings.setBorderPainted(false);
        btnSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSettings.addActionListener(e -> openProfileDialog());
        headerPanel.add(btnSettings, BorderLayout.EAST);

        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // === КАРТОЧКА БАЛАНСА (ГРАДИЕНТ) ===
        JPanel balanceCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Красивый градиент
                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), getHeight(), ACCENT_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Блик сверху
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 20, 20);
            }
        };

        balanceCard.setLayout(null);
        balanceCard.setPreferredSize(new Dimension(480, 160));
        balanceCard.setMaximumSize(new Dimension(480, 160));

        JLabel lblBalanceTitle = new JLabel("ВАШ БАЛАНС");
        lblBalanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBalanceTitle.setForeground(new Color(255, 255, 255, 200));
        lblBalanceTitle.setBounds(20, 20, 200, 20);
        balanceCard.add(lblBalanceTitle);

        lblBalance = new JLabel("0 ₽");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setBounds(20, 45, 250, 55);
        balanceCard.add(lblBalance);

        JLabel lblCardInfo = new JLabel("**** " + account.getNumber().substring(4));
        lblCardInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCardInfo.setForeground(new Color(255, 255, 255, 180));
        lblCardInfo.setBounds(20, 115, 200, 20);
        balanceCard.add(lblCardInfo);

        // Иконка карты
        JPanel cardIconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(5, 5, 45, 30, 5, 5);
                g2d.setColor(new Color(255, 215, 0, 180));
                g2d.fillRect(10, 10, 12, 8);
            }
        };
        cardIconPanel.setBounds(410, 20, 55, 40);
        cardIconPanel.setOpaque(false);
        balanceCard.add(cardIconPanel);

        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(balanceCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // === ИНФОРМАЦИЯ О СЧЁТЕ ===
        JPanel infoCard = createWhiteCard(new GridLayout(4, 2, 15, 10));
        infoCard.setPreferredSize(new Dimension(480, 170));
        infoCard.setMaximumSize(new Dimension(480, 170));

        infoCard.add(createInfoLabel(" Номер счёта:"));
        lblNumber = createInfoValue("");
        infoCard.add(lblNumber);

        infoCard.add(createInfoLabel(" Владелец:"));
        lblOwner = createInfoValue("");
        infoCard.add(lblOwner);

        infoCard.add(createInfoLabel(" Дата открытия:"));
        lblOpenDate = createInfoValue("");
        infoCard.add(lblOpenDate);

        infoCard.add(createInfoLabel(" Статус:"));
        lblStatus = createInfoValue("");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoCard.add(lblStatus);

        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(infoCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // === ОПЕРАЦИИ ===
        JPanel operationsCard = new JPanel();
        operationsCard.setLayout(new BoxLayout(operationsCard, BoxLayout.Y_AXIS));
        operationsCard.setBackground(CARD_COLOR);
        operationsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        operationsCard.setMaximumSize(new Dimension(480, 340));

        JLabel lblOpsTitle = new JLabel("Операции");
        lblOpsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOpsTitle.setForeground(TEXT_DARK);
        lblOpsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        operationsCard.add(lblOpsTitle);
        operationsCard.add(Box.createVerticalStrut(20));

        operationsCard.add(createButtonRow("Пополнить", txtDepositAmount = new JTextField(10),
                btnDeposit = createButton("Пополнить", PRIMARY_COLOR)));
        btnDeposit.addActionListener(e -> deposit());
        operationsCard.add(Box.createVerticalStrut(15));

        operationsCard.add(createButtonRow("Снять", txtWithdrawAmount = new JTextField(10),
                btnWithdraw = createButton("Снять", SECONDARY_COLOR)));
        btnWithdraw.addActionListener(e -> withdraw());
        operationsCard.add(Box.createVerticalStrut(15));

        JPanel transferRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        transferRow.setOpaque(false);
        transferRow.setMaximumSize(new Dimension(420, 40));

        txtTransferAmount = new JTextField(8);
        txtTransferAmount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTransferAmount.setPreferredSize(new Dimension(100, 35));
        txtTransferAmount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        transferRow.add(txtTransferAmount);

        JLabel lblArrow = new JLabel("→");
        lblArrow.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblArrow.setForeground(PRIMARY_COLOR);
        transferRow.add(lblArrow);

        JLabel lblAccNum = new JLabel(otherAccount.getNumber().substring(0, 4) + "...");
        lblAccNum.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAccNum.setForeground(TEXT_LIGHT);
        transferRow.add(lblAccNum);

        btnTransfer = createButton("↗", ACCENT_COLOR);
        btnTransfer.setPreferredSize(new Dimension(50, 35));
        btnTransfer.addActionListener(e -> transfer());
        transferRow.add(btnTransfer);

        operationsCard.add(transferRow);
        operationsCard.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        btnBlock = createButton("Блок", new Color(245, 158, 11));
        btnBlock.addActionListener(e -> toggleBlock());
        btnPanel.add(btnBlock);

        btnRefresh = createButton("Обновить", new Color(100, 116, 139));
        btnRefresh.setPreferredSize(new Dimension(90, 35));
        btnRefresh.addActionListener(e -> refreshData());
        btnPanel.add(btnRefresh);

        operationsCard.add(btnPanel);
        operationsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(operationsCard);
        mainPanel.add(Box.createVerticalStrut(20));

        lblStatusMessage = new JLabel("Готово к работе", SwingConstants.CENTER);
        lblStatusMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatusMessage.setForeground(PRIMARY_COLOR);
        lblStatusMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStatusMessage);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
    }

    private void openProfileDialog() {
        ProfileDialog dialog = new ProfileDialog(this, userProfile);
        dialog.setVisible(true);

        if (dialog.isProfileUpdated()) {
            avatarLabel.repaint();
            refreshData();
        }
    }

    private JPanel createWhiteCard(LayoutManager layout) {
        JPanel card = new JPanel(layout);
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_LIGHT);
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JPanel createButtonRow(String labelText, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(420, 40));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_LIGHT);
        panel.add(label);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(120, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(textField);

        button.setPreferredSize(new Dimension(110, 35));
        panel.add(button);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void refreshData() {
        lblNumber.setText(account.getNumber());
        lblOwner.setText(userProfile.getFullName());
        lblBalance.setText(String.format("%,d ₽", account.getBalance()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        lblOpenDate.setText(account.getOpenDate().format(formatter));

        if (account.isBlocked()) {
            lblStatus.setText("Заблокирован");
            lblStatus.setForeground(Color.RED);
        } else {
            lblStatus.setText("Активен");
            lblStatus.setForeground(SUCCESS_COLOR);
        }

        boolean blocked = account.isBlocked();
        btnDeposit.setEnabled(!blocked);
        btnWithdraw.setEnabled(!blocked);
        btnTransfer.setEnabled(!blocked);
    }

    private void deposit() {
        try {
            String text = txtDepositAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.deposit(amount)) {
                showMessage("+" + amount + " ₽", SUCCESS_COLOR);
                refreshData();
                txtDepositAmount.setText("");
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void withdraw() {
        try {
            String text = txtWithdrawAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.withdraw(amount)) {
                showMessage("-" + amount + " ₽", SUCCESS_COLOR);
                refreshData();
                txtWithdrawAmount.setText("");
            } else {
                showMessage("Недостаточно средств", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void transfer() {
        try {
            String text = txtTransferAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.transfer(otherAccount, amount)) {
                showMessage("Перевод " + amount + " ₽", SUCCESS_COLOR);
                refreshData();
                txtTransferAmount.setText("");
            } else {
                showMessage("Ошибка перевода", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void toggleBlock() {
        account.setBlocked(!account.isBlocked());
        if (account.isBlocked()) {
            showMessage("Счёт заблокирован", Color.RED);
        } else {
            showMessage("Счёт разблокирован", SUCCESS_COLOR);
        }
        refreshData();
    }

    private void showMessage(String text, Color color) {
        lblStatusMessage.setText(text);
        lblStatusMessage.setForeground(color);
    }
}