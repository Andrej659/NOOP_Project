package views;

import controllers.AdminController;
import models.Article;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Klasa {@code AdminPanel} predstavlja GUI prozor za pregled korisničkih podataka za administratora.
 * <p>
 * Ova klasa omogućuje administratorima pregled svih korisnika u sustavu, sortiranje korisnika,
 * te povratak na korisnički profil ili odjavu iz aplikacije.
 *
 * @author Andrej Markanjević
 */
public class AdminPanel extends JFrame {

    private JTextArea adminTextArea;
    private AdminController ac;
    private User user;
    private JPanel appBar, buttonPanel;
    private JButton backToProfileButton, logoutButton;
    private JScrollPane scrollPane;
    private JTable userTable;
    private DefaultTableModel tableModel;

    /**
     * Konstruktor klase {@code AdminPanel}.
     * <p>
     * Inicijalizira panel za pregled korisničkih podataka, postavlja GUI komponente i aktivira funkcionalnosti gumba.
     *
     * @param user Trenutno prijavljeni korisnik (administrator).
     */
    public AdminPanel(User user) {

        ac = new AdminController();
        this.user = user;
        initAdminFrame();
        layoutAdminFrame();
        activateAdminFrame();
    }

    /**
     * Inicijalizira sve GUI komponente potrebne za prikaz admin panela.
     */
    private void initAdminFrame(){

        appBar = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        backToProfileButton = new JButton("Back to Profile");
        logoutButton = new JButton("Logout");

        adminTextArea = new JTextArea();
        adminTextArea.setEditable(false);

        String[] columnNames = {"User ID", "Username", "Admin", "Email", "Address", "Order ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);

        scrollPane = new JScrollPane(userTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Postavlja raspored elemenata unutar admin panela.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutAdminFrame(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        buttonPanel.add(backToProfileButton);
        buttonPanel.add(logoutButton);
        appBar.add(buttonPanel, BorderLayout.NORTH);

        add(appBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        this.printUsers();

        setVisible(true);
    }

    /**
     * Aktivira funkcionalnosti unutar admin panela, dodajući akcije za gumbe.
     * <p>
     * Postavlja akcije za povratak na korisnički profil i odjavu.
     */
    private void activateAdminFrame(){
        backToProfileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserProfilePanel(user);
            }

        });
        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                user = null;
                dispose();
                new AppFrame();
            }

        });
    }

    /**
     * Ispisuje sve korisnike u tablici.
     */
    private void printUsers() {

        List<User> users = ac.getUsers();

        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.isAdmin(),
                    u.getEmail(),
                    u.getAddress(),
                    u.getOrders()
            });
        }
    }
}