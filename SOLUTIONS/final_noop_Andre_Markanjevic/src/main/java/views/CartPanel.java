package views;

import controllers.CartController;
import models.Article;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;


/**
 * Klasa {@code CartPanel} predstavlja panel za prikaz sadržaja korisničke košarice u aplikaciji.
 * <p>
 * Ovaj panel omogućuje korisnicima pregled artikala koje su dodali u košaricu, prikazuje njihovu količinu i ukupnu cijenu,
 * te omogućuje korisnicima povratak u trgovinu ili odjavu iz aplikacije.
 *
 * @author Andrej Markanjević
 */
public class CartPanel extends JFrame {

    private JPanel appBar, buttonPanel;
    private JButton backToShopButton, logoutButton;
    private User user;
    private CartController cc = new CartController();
    private DefaultTableModel tableModel;
    private JTable cartTable;

    /**
     * Konstruktor klase {@code CartPanel}.
     * <p>
     * Inicijalizira panel za prikaz narudžbi.
     *
     * @param user Trenutno prijavljeni korisnik.
     */
    public CartPanel(User user) {

        this.user = user;

        initCartFrame();
        layoutCartFrame();
        activateCartFrame();

    }

    /**
     * Inicijalizira sve GUI komponente potrebne za prikaz narudžbi.
     */
    private void initCartFrame(){

        appBar = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        backToShopButton = new JButton("Back to Shop");
        logoutButton = new JButton("Logout");

    }

    /**
     * Postavlja raspored elemenata unutar prozora narudžbi.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutCartFrame() {

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        buttonPanel.add(backToShopButton);
        buttonPanel.add(logoutButton);
        appBar.add(buttonPanel, BorderLayout.NORTH);
        add(appBar, BorderLayout.NORTH);

        setUpTheCart();
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new BorderLayout());
        add(totalPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Aktivira funkcionalnosti prozora narudžbi, dodajući akcije za gumbe.
     * <p>
     * Postavlja akcije za povratak u trgovinu ili odjavu korisnika.
     */
    private void activateCartFrame(){
        backToShopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ShopPanel(user);
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
     * Sortira narudžbe po ID-u, te vraća pripadnu strukturu podataka.
     *
     * @param orders Prima strukturu podataka u kojoj su spremljene narudžbe.
     */
    private Map<Integer, Map<Article, Integer>> sortOrders(Map<Integer, Map<Article, Integer>> orders){

        Map<Integer, Map<Article, Integer>> sortedOrders = orders.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Map<Article, Integer>>comparingByKey())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        return sortedOrders;
    }

    /**
     * Postavlja sadržaj košarice, prikazujući artikle, njihovu količinu i cijenu.
     * <p>
     * Svaki artikal prikazuje se s odgovarajućom slikom, nazivom, količinom i cijenom, a na kraju se prikazuje ukupna cijena.
     */
    private void setUpTheCart() {
        String[] columnNames = {"Order ID", "Article", "Unit Price", "Quantity", "Total price",};

        tableModel = new DefaultTableModel(columnNames, 0);

        try {
            Map<Integer, Map<Article, Integer>> articlesFromOrders = cc.getUserOrders(user.getUserId());
            articlesFromOrders = sortOrders(articlesFromOrders);

            float totalValueSpent = 0;

            for (Map.Entry<Integer, Map<Article, Integer>> orderEntry : articlesFromOrders.entrySet()) {
                Map<Article, Integer> articlesMap = orderEntry.getValue();

                float total = 0;

                tableModel.addRow(new Object[]{
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////"
                });

                for (Map.Entry<Article, Integer> entry : articlesMap.entrySet()) {
                    Article article = entry.getKey();
                    int quantity = entry.getValue();

                    if (quantity != 0) {
                        float unitPrice = article.getPrice();
                        float totalPrice = unitPrice * quantity;
                        total += totalPrice;

                        tableModel.addRow(new Object[]{
                                orderEntry.getKey(),
                                article.getArticleName(),
                                String.format("$%.2f", unitPrice),
                                quantity,
                                String.format("$%.2f", totalPrice)
                        });
                    }
                }

                totalValueSpent += total;

                tableModel.addRow(new Object[]{
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////",
                        "///////////////////////////////"
                });

                tableModel.addRow(new Object[]{
                        "-------------------------",
                        "-------------------------",
                        "----------------------->",
                        "Total: ",
                        String.format("$%.2f", total)
                });
            }

            tableModel.addRow(new Object[]{
                    "-------------------------",
                    "-------------------------",
                    "----------------------->",
                    "User's total: ",
                    String.format("$%.2f", totalValueSpent)
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        cartTable = new JTable(tableModel);
    }
}