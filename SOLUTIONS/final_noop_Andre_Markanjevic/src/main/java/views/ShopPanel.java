package views;

import controllers.ShopController;
import models.Article;
import models.User;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa {@code ShopPanel} predstavlja glavni prozor aplikacije koji prikazuje artikle dostupne u trgovini.
 * <p>
 * Korisnici mogu pregledavati artikle, dodavati ih u košaricu, prelaziti na svoj profil ili pregledavati košaricu.
 *
 * @author Andrej Markanjević
 */

public class ShopPanel extends JFrame {

    private JPanel panel, appBar, buttonPanel, articlesPanel, izlogPanel, leftPanel, rightPanel;
    private JButton profileButton, cartButton, logoutButton, addArticle, addToCart;
    private User user;
    private ShopController sc;
    private JLabel imageLabel, nameLabel, priceLabel;
    private Map<Integer, Integer> catalog;


    /**
     * Konstruktor klase {@code ShopPanel}.
     * <p>
     * Inicijalizira panel za prikaz artikala, učitava popis artikala i dodaje osnovne funkcionalnosti.
     *
     * @param user Trenutno prijavljeni korisnik.
     */
    public ShopPanel(User user) {

        this.sc = new ShopController();
        this.user = user;
        this.catalog = new HashMap<>();
        makeCatalog(sc.getArticles());

        initShopFrame();
        layoutShopFrame();
        activateShopFrame();

    }

    /**
     * Inicijalizira sve GUI komponente potrebne za prikaz prozora trgovine.
     */
    private void initShopFrame(){

        appBar = new JPanel();
        buttonPanel = new JPanel();
        panel = new JPanel(new BorderLayout());
        profileButton = new JButton("My profile");
        cartButton = new JButton("My cart");
        logoutButton = new JButton("Logout");
        articlesPanel = new JPanel();
        addArticle = new JButton("Add article");
        addToCart = new JButton("Add to cart");
    }

    /**
     * Postavlja raspored elemenata unutar prozora trgovine.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutShopFrame(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        setLocationRelativeTo(null);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buttonPanel.add(profileButton);
        buttonPanel.add(cartButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(addArticle);
        buttonPanel.add(addToCart);

        panel.add(buttonPanel,BorderLayout.NORTH);

        appBar.setLayout(new BoxLayout(appBar, BoxLayout.Y_AXIS));

        List<Article> articles = sc.getArticles();
        for (Article a : articles) {
            appBar.add(createArticlePanel(a));
        }

        JScrollPane scrollableAppBar = new JScrollPane(appBar);
        scrollableAppBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableAppBar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollableAppBar, BorderLayout.CENTER);

        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        panel.add(new JScrollPane(articlesPanel), BorderLayout.SOUTH);

        setContentPane(panel);

        setVisible(true);
    }

    /**
     * Aktivira funkcionalnosti prozora trgovine, dodajući akcije za gumbe.
     * <p>
     * Postavlja akcije za prijelaz na profil, košaricu i odjavu.
     */
    private void activateShopFrame(){

        profileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openProfileFrame();
            }

        });
        cartButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openCartFrame();
            }

        });
        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                user = null;
                openAppFrame();
            }

        });
        addArticle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openArticleFrame();
            }

        });
        addToCart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                finishTheOrder();
            }

        });
    }

    private void openProfileFrame(){
        dispose();
        new UserProfilePanel(user);
    }

    private void openAppFrame(){
        dispose();
        new AppFrame();
    }

    private void openCartFrame(){
        dispose();
        new CartPanel(user);
    }

    private void openArticleFrame(){
        dispose();
        new AddArticlePanel(user);
    }

    /**
     * Ova metoda dohvaća kvantitetu artikla koji se dodaju u narudžbu te se narudžba finalizira.
     */
    private void finishTheOrder(){
        Map<Article, Integer> articlesToAdd = articlesToAdd();

        if (articlesToAdd != null){
            sc.addOrder(user.getUserId(),articlesToAdd);
            dispose();
            new ShopPanel(user);
        }
    }


    /**
     * Prima objekte artikala kojima dodaje prazne vrijednosti,
     * stvara neku vrstu kataloga.
     *
     * @param articles Artikli koji se ubacuju u katalog.
     */
    private void makeCatalog(List<Article> articles){

        for (Article a: articles){
            catalog.put(a.getArticleId(),0);
        }
    }

    /**
     * Stvara panel za pojedini artikal s njegovom slikom, nazivom, cijenom i spinnerom za količinu.
     *
     * @param article Artikal za koji se stvara panel.
     * @return {@code JPanel} s informacijama o artiklu.
     */
    private JPanel createArticlePanel(Article article) {

        izlogPanel = new JPanel(new BorderLayout());

        izlogPanel.setMaximumSize(new Dimension(400, 100));

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        imageLabel = new JLabel(new ImageIcon(article.getPathToPic()));
        nameLabel = new JLabel(article.getArticleName());
        leftPanel.add(imageLabel);
        leftPanel.add(nameLabel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        priceLabel = new JLabel("Price: €" + article.getPrice());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        rightPanel.add(priceLabel);
        rightPanel.add(quantitySpinner);

        izlogPanel.add(leftPanel, BorderLayout.WEST);
        izlogPanel.add(rightPanel, BorderLayout.EAST);

        quantitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Ispis trenutne vrijednosti spinnera
                int value = (int) quantitySpinner.getValue();
                catalog.put(article.getArticleId(),value);
            }
        });
        return izlogPanel;
    }

    /**
     * Ova metoda prolazi kroz katalog i prati koje smo sve artikle dodali u cart,
     * te ih vraća kao strukturu podataka.
     *
     * @return Strukturu podataka koja prikazuje cart, sadrži artikle i količine.
     */
    private Map<Article, Integer> articlesToAdd(){

        Map<Article, Integer> cart = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : catalog.entrySet()) {
            if (entry.getValue() != 0){
                Article art = sc.getArticleById(entry.getKey());
                cart.put(art,entry.getValue());
            }
        }
        return cart.isEmpty() ? null : cart;
    }
}