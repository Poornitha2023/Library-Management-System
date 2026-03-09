import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/* ================= DATABASE ================= */

class DB {
    static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb",
                "root",
                "Poori123@"); // change password
    }
}

/* ================= STYLE ================= */

class Style {
    // ── Colour Palette ──
    static Color bgDark = new Color(27, 42, 74); // #1B2A4A dark navy
    static Color bgMedium = new Color(36, 55, 99); // header bar shade
    static Color bgLight = new Color(240, 242, 245); // #F0F2F5 off-white
    static Color primary = new Color(0, 121, 107); // #00796B teal
    static Color accent = new Color(255, 179, 0); // #FFB300 warm gold
    static Color cardBg = Color.WHITE;
    static Color textDark = new Color(33, 37, 41);
    static Color textLight = new Color(245, 245, 245);
    static Color danger = new Color(211, 47, 47);
    static Color success = new Color(56, 142, 60);

    // ── Fonts ──
    static Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    static Font headFont = new Font("Segoe UI", Font.BOLD, 20);
    static Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
    static Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);
    static Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
    static Font smallFont = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Helpers ──
    static JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(labelFont);
        l.setForeground(textDark);
        return l;
    }

    static JTextField createField() {
        JTextField t = new JTextField();
        t.setFont(fieldFont);
        t.setPreferredSize(new Dimension(220, 36));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return t;
    }

    static JPasswordField createPasswordField() {
        JPasswordField p = new JPasswordField();
        p.setFont(fieldFont);
        p.setPreferredSize(new Dimension(220, 36));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return p;
    }

    /** Rounded white card panel */
    static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(0, 0, 0, 25));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    /** Header bar with title for feature windows */
    static JPanel createHeader(String title) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        header.setBackground(bgDark);
        header.setPreferredSize(new Dimension(0, 56));
        JLabel lbl = new JLabel(title);
        lbl.setFont(headFont);
        lbl.setForeground(textLight);
        header.add(lbl);
        return header;
    }

    /** Style a JTable consistently */
    static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(labelFont);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 60));
        table.setSelectionForeground(textDark);

        JTableHeader th = table.getTableHeader();
        th.setFont(btnFont);
        th.setPreferredSize(new Dimension(0, 40));
        th.setReorderingAllowed(false);

        // Custom header renderer for different column colors
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(textLight);
                // Set different background colors for each column
                switch (column) {
                    case 0: setBackground(new Color(21, 101, 192)); break; // Blue for ID
                    case 1: setBackground(new Color(56, 142, 60)); break; // Green for Title
                    case 2: setBackground(new Color(230, 126, 34)); break; // Orange for Author
                    case 3: setBackground(new Color(142, 68, 173)); break; // Purple for Quantity
                    default: setBackground(bgDark);
                }
                return this;
            }
        });

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}

/* ─────── Rounded Button with Hover ─────── */

class StyledButton extends JButton {
    private Color baseColor;

    public StyledButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        setFont(Style.btnFont);
        setBackground(baseColor);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, 42));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setBackground(baseColor.darker());
                repaint();
            }

            public void mouseExited(MouseEvent evt) {
                setBackground(baseColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
        super.paintComponent(g2);
        g2.dispose();
    }
}

/* ─────── Dashboard Tile Button ─────── */

class TileButton extends JButton {
    private Color baseColor;

    public TileButton(String emoji, String label, Color baseColor) {
        super("<html><center>" + emoji + "<br><span style='font-size:12px'>"
                + label + "</span></center></html>");
        this.baseColor = baseColor;
        setFont(Style.btnFont);
        setForeground(Color.WHITE);
        setBackground(baseColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(180, 110));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(baseColor.brighter());
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
        g2.dispose();
        super.paintComponent(g);
    }
}

/* ================= LOGIN ================= */

class LoginPage {
    static void show() {
        JFrame f = new JFrame("LMS - Secure Login");
        f.setSize(900, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        // ── Left Panel with Dark Background and Text ──
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(45, 55, 65)); // Dark teal-grey
        leftPanel.setPreferredSize(new Dimension(380, 500));
        
        GridBagConstraints gc_left = new GridBagConstraints();
        gc_left.anchor = GridBagConstraints.CENTER;
        
        JLabel digitalLabel = new JLabel("DIGITAL");
        digitalLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        digitalLabel.setForeground(new Color(255, 179, 0)); // Gold color
        leftPanel.add(digitalLabel, gc_left);
        
        gc_left.gridy = 1;
        JLabel libraryLabel = new JLabel("LIBRARY");
        libraryLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        libraryLabel.setForeground(Color.WHITE);
        leftPanel.add(libraryLabel, gc_left);
        
        f.add(leftPanel, BorderLayout.WEST);

        // ── Right Panel with Card ──
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Style.bgLight);

        // ── Image Panel ──
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Style.bgLight);
        try {
            ImageIcon icon = new ImageIcon(new URL("https://raw.githubusercontent.com/poori-ypoor/library-management-system/main/library.jpg"));
            Image img = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imagePanel.add(imgLabel);
        } catch (Exception e) {
            // If image fails to load, just skip
        }
        rightPanel.add(imagePanel, BorderLayout.NORTH);

        // ── Card ──
        JPanel card = Style.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(350, 380));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 16, 8, 16);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel title = new JLabel("Account Login", SwingConstants.CENTER);
        title.setFont(Style.titleFont);
        title.setForeground(Style.bgDark);
        card.add(title, gc);

        // Username
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("USERID"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        JTextField userField = Style.createField();
        card.add(userField, gc);

        // Password
        gc.gridy = 2;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("PASSWORD"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        JPasswordField passField = Style.createPasswordField();
        card.add(passField, gc);

        // Role
        gc.gridy = 3;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("LOG IN AS"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        String[] roles = { "Admin", "Student" };
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(Style.fieldFont);
        roleBox.setPreferredSize(new Dimension(220, 36));
        card.add(roleBox, gc);

        // Login button
        gc.gridy = 4;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(20, 16, 8, 16);
        StyledButton loginBtn = new StyledButton("LOGIN", new Color(25, 118, 210));
        loginBtn.setPreferredSize(new Dimension(250, 44));
        card.add(loginBtn, gc);

        rightPanel.add(card, BorderLayout.CENTER);
        f.add(rightPanel, BorderLayout.CENTER);

        // ── Action ──
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(f, "Please enter both username and password.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (roleBox.getSelectedItem().equals("Admin")) {
                if (user.equals("admin") && pass.equals("admin123")) {
                    f.dispose();
                    AdminDashboard.show();
                } else {
                    JOptionPane.showMessageDialog(f, "Invalid Admin credentials.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try (Connection con = DB.getConnection();
                        PreparedStatement ps = con.prepareStatement(
                                "SELECT * FROM students WHERE student_id=? AND password=?")) {
                    ps.setString(1, user);
                    ps.setString(2, pass);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            f.dispose();
                            StudentDashboard.show(user);
                        } else {
                            JOptionPane.showMessageDialog(f, "Invalid Student credentials.",
                                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, "Database error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        f.setVisible(true);
    }
}

/* ================= ADMIN DASHBOARD ================= */

class AdminDashboard {
    static void show() {
        JFrame f = new JFrame("Admin Dashboard");
        f.setSize(760, 520);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.bgDark);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel title = new JLabel("\uD83D\uDCDA  Admin Dashboard");
        title.setFont(Style.titleFont);
        title.setForeground(Style.textLight);
        header.add(title, BorderLayout.WEST);

        StyledButton logoutBtn = new StyledButton("Logout", Style.danger);
        logoutBtn.setPreferredSize(new Dimension(110, 38));
        JPanel logoutWrap = new JPanel(new GridBagLayout());
        logoutWrap.setOpaque(false);
        logoutWrap.add(logoutBtn);
        header.add(logoutWrap, BorderLayout.EAST);

        f.add(header, BorderLayout.NORTH);

        // ── Centered Grid of Tiles ──
        JPanel grid = new JPanel(new GridLayout(2, 3, 24, 24));
        grid.setOpaque(false);

        TileButton addB = new TileButton("\uD83D\uDCD7", "Add Book", Style.primary);
        TileButton viewB = new TileButton("\uD83D\uDCDA", "View Books", new Color(21, 101, 192));
        TileButton addS = new TileButton("\uD83D\uDC64", "Add Student", new Color(0, 121, 107));
        TileButton viewS = new TileButton("\uD83D\uDC65", "View Students", new Color(69, 90, 100));
        TileButton issue = new TileButton("\uD83D\uDCE4", "Issue Book", new Color(230, 126, 34));
        TileButton ret = new TileButton("\uD83D\uDCE5", "Return Book", new Color(142, 68, 173));

        grid.add(addB);
        grid.add(viewB);
        grid.add(addS);
        grid.add(viewS);
        grid.add(issue);
        grid.add(ret);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        wrapper.add(grid);
        f.add(wrapper, BorderLayout.CENTER);

        // ── Actions ──
        addB.addActionListener(e -> BookUI.addBook());
        viewB.addActionListener(e -> BookUI.viewBooks());
        addS.addActionListener(e -> StudentUI.addStudent());
        viewS.addActionListener(e -> StudentUI.viewStudents());
        issue.addActionListener(e -> IssueUI.issueBook());
        ret.addActionListener(e -> IssueUI.returnBook());
        logoutBtn.addActionListener(e -> {
            f.dispose();
            LoginPage.show();
        });

        f.setVisible(true);
    }
}

/* ================= STUDENT DASHBOARD ================= */

class StudentDashboard {
    static void show(String id) {
        JFrame f = new JFrame("Student Dashboard");
        f.setSize(700, 480);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.bgDark);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel title = new JLabel("\uD83D\uDCDA  Welcome, Student " + id);
        title.setFont(Style.headFont);
        title.setForeground(Style.textLight);
        header.add(title, BorderLayout.WEST);

        StyledButton logoutBtn = new StyledButton("Logout", Style.danger);
        logoutBtn.setPreferredSize(new Dimension(110, 38));
        JPanel logoutWrap = new JPanel(new GridBagLayout());
        logoutWrap.setOpaque(false);
        logoutWrap.add(logoutBtn);
        header.add(logoutWrap, BorderLayout.EAST);

        f.add(header, BorderLayout.NORTH);

        // ── Centered Grid of Tiles ──
        JPanel grid = new JPanel(new GridLayout(2, 2, 24, 24));
        grid.setOpaque(false);

        TileButton viewBtn = new TileButton("\uD83D\uDCDA", "View Books", new Color(21, 101, 192));
        TileButton myBtn = new TileButton("\uD83D\uDCD6", "My Books", Style.primary);
        TileButton notifyBtn = new TileButton("\uD83D\uDD14", "Notifications", new Color(230, 126, 34));
        TileButton duesBtn = new TileButton("\uD83D\uDCB0", "Check Dues", new Color(142, 68, 173));

        grid.add(viewBtn);
        grid.add(myBtn);
        grid.add(notifyBtn);
        grid.add(duesBtn);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        wrapper.add(grid);
        f.add(wrapper, BorderLayout.CENTER);

        // ── Actions ──
        viewBtn.addActionListener(e -> BookUI.viewBooks());
        myBtn.addActionListener(e -> IssueUI.myBooks(id));
        notifyBtn.addActionListener(e -> IssueUI.notifications(id));
        duesBtn.addActionListener(e -> DueUI.checkDues(id));
        logoutBtn.addActionListener(e -> {
            f.dispose();
            LoginPage.show();
        });

        f.setVisible(true);
    }
}

/* ================= BOOK UI ================= */

class BookUI {

    static void addBook() {
        JFrame f = new JFrame("Add Book");
        f.setSize(500, 420);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new GridBagLayout());

        JPanel card = Style.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 340));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 14, 8, 14);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel heading = new JLabel("\uD83D\uDCD7 Add New Book", SwingConstants.CENTER);
        heading.setFont(Style.headFont);
        heading.setForeground(Style.bgDark);
        card.add(heading, gc);

        gc.gridwidth = 1;
        JTextField idF = Style.createField();
        JTextField nameF = Style.createField();
        JTextField authorF = Style.createField();
        JTextField qtyF = Style.createField();

        String[] labels = { "Book ID", "Title", "Author", "Quantity" };
        JTextField[] fields = { idF, nameF, authorF, qtyF };

        for (int i = 0; i < labels.length; i++) {
            gc.gridy = i + 1;
            gc.gridx = 0;
            gc.anchor = GridBagConstraints.LINE_END;
            card.add(Style.createLabel(labels[i]), gc);
            gc.gridx = 1;
            gc.anchor = GridBagConstraints.LINE_START;
            card.add(fields[i], gc);
        }

        gc.gridy = labels.length + 1;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(18, 14, 8, 14);
        StyledButton addBtn = new StyledButton("  Add Book  ", Style.primary);
        card.add(addBtn, gc);

        f.add(card);

        addBtn.addActionListener(e -> {
            try (Connection con = DB.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO books VALUES(?,?,?,?)")) {
                ps.setString(1, idF.getText().trim());
                ps.setString(2, nameF.getText().trim());
                ps.setString(3, authorF.getText().trim());
                ps.setInt(4, Integer.parseInt(qtyF.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Book added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                f.dispose();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(f, "Quantity must be a number.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.setVisible(true);
    }

    static void viewBooks() {
        JFrame f = new JFrame("View Books");
        f.setSize(700, 460);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        f.add(Style.createHeader("\uD83D\uDCDA All Books"), BorderLayout.NORTH);

        String[] cols = { "ID", "Title", "Author", "Quantity" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        Style.styleTable(table);

        try (Connection con = DB.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Error loading books: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        sp.getViewport().setBackground(Color.WHITE);
        f.add(sp, BorderLayout.CENTER);
        f.setVisible(true);
    }
}

/* ================= STUDENT UI ================= */

class StudentUI {
    static void addStudent() {
        JFrame f = new JFrame("Add Student");
        f.setSize(480, 360);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new GridBagLayout());

        JPanel card = Style.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 280));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 14, 8, 14);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel heading = new JLabel("\uD83D\uDC64 Add New Student", SwingConstants.CENTER);
        heading.setFont(Style.headFont);
        heading.setForeground(Style.bgDark);
        card.add(heading, gc);

        gc.gridwidth = 1;
        JTextField idF = Style.createField();
        JTextField nameF = Style.createField();
        JTextField passF = Style.createField();

        String[] labels = { "Student ID", "Name", "Password" };
        JTextField[] fields = { idF, nameF, passF };

        for (int i = 0; i < labels.length; i++) {
            gc.gridy = i + 1;
            gc.gridx = 0;
            gc.anchor = GridBagConstraints.LINE_END;
            card.add(Style.createLabel(labels[i]), gc);
            gc.gridx = 1;
            gc.anchor = GridBagConstraints.LINE_START;
            card.add(fields[i], gc);
        }

        gc.gridy = labels.length + 1;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(18, 14, 8, 14);
        StyledButton addBtn = new StyledButton("  Add Student  ", Style.primary);
        card.add(addBtn, gc);

        f.add(card);

        addBtn.addActionListener(e -> {
            try (Connection con = DB.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO students VALUES(?,?,?)")) {
                ps.setString(1, idF.getText().trim());
                ps.setString(2, nameF.getText().trim());
                ps.setString(3, passF.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Student added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.setVisible(true);
    }

    static void viewStudents() {
        JFrame f = new JFrame("View Students");
        f.setSize(600, 400);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        f.add(Style.createHeader("\uD83D\uDC65 All Students"), BorderLayout.NORTH);

        String[] cols = { "Student ID", "Name" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        Style.styleTable(table);

        try (Connection con = DB.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT student_id, name FROM students")) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString(1), rs.getString(2)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Error loading students: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        sp.getViewport().setBackground(Color.WHITE);
        f.add(sp, BorderLayout.CENTER);
        f.setVisible(true);
    }
}

/* ================= ISSUE UI ================= */

class IssueUI {

    static void issueBook() {
        JFrame f = new JFrame("Issue Book");
        f.setSize(480, 320);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new GridBagLayout());

        JPanel card = Style.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 240));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 14, 8, 14);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel heading = new JLabel("\uD83D\uDCE4 Issue Book", SwingConstants.CENTER);
        heading.setFont(Style.headFont);
        heading.setForeground(Style.bgDark);
        card.add(heading, gc);

        gc.gridwidth = 1;
        JTextField bookF = Style.createField();
        JTextField studentF = Style.createField();

        gc.gridy = 1;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("Book ID"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        card.add(bookF, gc);

        gc.gridy = 2;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("Student ID"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        card.add(studentF, gc);

        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(18, 14, 8, 14);
        StyledButton issueBtn = new StyledButton("  Issue Book  ", new Color(230, 126, 34));
        card.add(issueBtn, gc);

        f.add(card);

        issueBtn.addActionListener(e -> {
            try (Connection con = DB.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO issues VALUES(?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 7 DAY))")) {
                ps.setString(1, bookF.getText().trim());
                ps.setString(2, studentF.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Book issued successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.setVisible(true);
    }

    static void returnBook() {
        // ── Card-style return form instead of raw JOptionPane ──
        JFrame f = new JFrame("Return Book");
        f.setSize(450, 280);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new GridBagLayout());

        JPanel card = Style.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(370, 200));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 14, 8, 14);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel heading = new JLabel("\uD83D\uDCE5 Return Book", SwingConstants.CENTER);
        heading.setFont(Style.headFont);
        heading.setForeground(Style.bgDark);
        card.add(heading, gc);

        gc.gridwidth = 1;
        JTextField bookF = Style.createField();
        gc.gridy = 1;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        card.add(Style.createLabel("Book ID"), gc);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        card.add(bookF, gc);

        gc.gridy = 2;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(18, 14, 8, 14);
        StyledButton retBtn = new StyledButton("  Return  ", new Color(142, 68, 173));
        card.add(retBtn, gc);

        f.add(card);

        retBtn.addActionListener(e -> {
            String bookId = bookF.getText().trim();
            if (bookId.isEmpty()) {
                JOptionPane.showMessageDialog(f, "Please enter a Book ID.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try (Connection con = DB.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM issues WHERE book_id=?")) {
                ps.setString(1, bookId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(f, "Book returned successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, "No active issue found for Book ID: " + bookId,
                            "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.setVisible(true);
    }

    static void myBooks(String id) {
        JFrame f = new JFrame("My Books");
        f.setSize(620, 400);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        f.add(Style.createHeader("\uD83D\uDCD6 My Issued Books"), BorderLayout.NORTH);

        String[] cols = { "Book ID", "Return Date" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        Style.styleTable(table);

        try (Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT book_id, return_date FROM issues WHERE student_id=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[] {
                            rs.getString(1), rs.getDate(2)
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Error loading books: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(f, "You have no issued books.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        sp.getViewport().setBackground(Color.WHITE);
        f.add(sp, BorderLayout.CENTER);
        f.setVisible(true);
    }

    static void notifications(String id) {
        try (Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT book_id, return_date FROM issues WHERE student_id=?")) {
            ps.setString(1, id);
            StringBuilder msg = new StringBuilder();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate d = rs.getDate(2).toLocalDate();
                    if (d.minusDays(2).isBefore(LocalDate.now())) {
                        msg.append("⚠ Return \"").append(rs.getString(1))
                                .append("\" before ").append(d).append("\n");
                    }
                }
            }
            JOptionPane.showMessageDialog(null,
                    msg.length() == 0 ? "✅ No pending notifications." : msg.toString(),
                    "Notifications", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/* ================= DUE UI ================= */

class DueUI {
    static void checkDues(String id) {
        JFrame f = new JFrame("Check Dues");
        f.setSize(700, 440);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Style.bgLight);
        f.setLayout(new BorderLayout());

        f.add(Style.createHeader("\uD83D\uDCB0 Dues & Fines"), BorderLayout.NORTH);

        String[] cols = { "Book ID", "Due Date", "Overdue Days", "Fine (₹)" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int overdueDays = (int) getModel().getValueAt(row, 2);
                if (!isRowSelected(row)) {
                    if (overdueDays > 0) {
                        c.setBackground(new Color(255, 205, 210)); // red – overdue
                    } else if (overdueDays >= -2) {
                        c.setBackground(new Color(255, 236, 179)); // amber – due soon
                    } else {
                        c.setBackground(new Color(200, 230, 201)); // green – safe
                    }
                    c.setForeground(Style.textDark);
                }
                return c;
            }
        };
        Style.styleTable(table);

        boolean hasDues = false;
        try (Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT book_id, return_date, DATEDIFF(CURDATE(), return_date) AS overdue_days FROM issues WHERE student_id=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String bookId = rs.getString(1);
                    Date returnDate = rs.getDate(2);
                    int overdueDays = rs.getInt(3);
                    int fine = overdueDays > 0 ? overdueDays * 5 : 0;
                    if (fine > 0)
                        hasDues = true;
                    model.addRow(new Object[] { bookId, returnDate, overdueDays, fine });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!hasDues && model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(f, "No dues pending! \uD83C\uDF89",
                    "All Clear", JOptionPane.INFORMATION_MESSAGE);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        sp.getViewport().setBackground(Color.WHITE);
        f.add(sp, BorderLayout.CENTER);
        f.setVisible(true);
    }
}

/* ================= MAIN ================= */

public class LibraryManagementSystem {
    public static void main(String[] args) {
        // Use system look-and-feel for native rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(LoginPage::show);
    }
}