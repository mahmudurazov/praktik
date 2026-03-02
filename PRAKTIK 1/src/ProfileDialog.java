import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class ProfileDialog extends JDialog {
    private UserProfile profile;
    private boolean profileUpdated = false;

    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone;
    private JLabel avatarLabel;
    private JButton btnSave, btnChangeAvatar;

    public ProfileDialog(JFrame parent, UserProfile profile) {
        super(parent, "Профиль пользователя", true);
        this.profile = profile;

        setSize(450, 550);
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
        loadProfileData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Аватарка
        avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Круг
                g2d.setColor(new Color(52, 152, 219));
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Буква
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
                String initial = profile.getFirstName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(initial, x, y);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(120, 120));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatarLabel.setToolTipText("Нажмите для смены аватарки");
        avatarLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeAvatar();
            }
        });
        mainPanel.add(avatarLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        btnChangeAvatar = new JButton(" Изменить фото");
        btnChangeAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnChangeAvatar.setForeground(new Color(52, 152, 219));
        btnChangeAvatar.setContentAreaFilled(false);
        btnChangeAvatar.setBorderPainted(false);
        btnChangeAvatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangeAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChangeAvatar.addActionListener(e -> changeAvatar());
        mainPanel.add(btnChangeAvatar);
        mainPanel.add(Box.createVerticalStrut(25));

        // Форма
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        formPanel.add(createLabel("Имя:"));
        txtFirstName = createTextField();
        formPanel.add(txtFirstName);

        formPanel.add(createLabel("Фамилия:"));
        txtLastName = createTextField();
        formPanel.add(txtLastName);

        formPanel.add(createLabel("Email:"));
        txtEmail = createTextField();
        formPanel.add(txtEmail);

        formPanel.add(createLabel("Телефон:"));
        txtPhone = createTextField();
        formPanel.add(txtPhone);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 250));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Кнопки
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(new Color(236, 240, 241));

        btnSave = new JButton("Сохранить");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(new Color(52, 152, 219));
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> saveProfile());
        btnPanel.add(btnSave);

        JButton btnCancel = new JButton("Отмена");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(149, 165, 166));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnCancel);

        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnPanel);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(127, 140, 141));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private void loadProfileData() {
        txtFirstName.setText(profile.getFirstName());
        txtLastName.setText(profile.getLastName());
        txtEmail.setText(profile.getEmail());
        txtPhone.setText(profile.getPhone());
    }

    private void changeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите фото");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Изображения", "jpg", "jpeg", "png", "gif"
        ));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            profile.setAvatarPath(fileChooser.getSelectedFile().getAbsolutePath());
            avatarLabel.repaint();
            JOptionPane.showMessageDialog(this,
                    "Фото загружено!",
                    "Успешно",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveProfile() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Имя и фамилия обязательны!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmail(email);
        profile.setPhone(phone);

        profileUpdated = true;
        JOptionPane.showMessageDialog(this,
                "Профиль сохранён!",
                "Успешно",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean isProfileUpdated() {
        return profileUpdated;
    }
}