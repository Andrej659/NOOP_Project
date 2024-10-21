package models;

import java.sql.*;
import java.util.*;


/**
 * Klasa {@code Database} predstavlja našu bazu podataka s kojom je povezana.
 * <p>
 * Ova klasa obavlja sve funkcije vezane uz bazu podataka, što sporazumijeva spremanje, učitavanje, ažuriranje i brisanje
 * podataka u bazi podataka.
 *
 * @author Andrej Markanjević
 */
public class DataBase {

    private Connection con;

    /**
     * Konstruktor klase {@code Database}.
     */
    public DataBase() {
    }

    /**
     * Ova metoda se aktivira svaki put kad se controller aktivira, ona nas spaja s bazom podataka.
     */
    public void connect() throws SQLException {

        System.out.println("Connecting to database...");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //server was shutdown after the project

            System.out.println("Connected to -> " + con.toString());
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load driver!!!");
        }
    }

    /**
     * Ova metoda nas odspaja od baze podataka.
     */
    public void disconnect() throws SQLException {

        con.close();
        System.out.println("Disconnected from DB...");

    }

    /**
     * Učitava sve korisnike iz baze podataka.
     * <p>
     * Ova metoda dohvaća sve korisnike iz tablice "Users" u bazi podataka i dodaje ih u listu korisnika.
     *
     * @throws SQLException u slučaju greške prilikom izvršavanja SQL upita.
     */
    public List<User> loadAllUsersFromDB() throws SQLException {

        List<User> users = new ArrayList<>();

        if(con != null) {

            System.out.println("Loading from DB...");
            String slctSQL = "select userId, username, password, email, address, admin from Users order by username";
            PreparedStatement slcStm = con.prepareStatement(slctSQL);

            ResultSet slcResult = slcStm.executeQuery();

            while(slcResult.next()) {

                User user = new User(slcResult.getInt(1),
                        slcResult.getString(2),
                        slcResult.getString(3),
                        slcResult.getString(4),
                        slcResult.getString(5),
                        slcResult.getBoolean(6));

                user.setOrders(loadUserOrdersFromDB(user.getUserId()));
                users.add(user);
            }

            slcResult.close();
            slcStm.close();
        }
        return users;
    }

    /**
     * Kreira novi jedinstveni ID za tablicu u bazi podataka.
     * <p>
     * Na temelju postojećih ID-eva u tablici, ova metoda vraća novi, jedinstveni ID koji se može koristiti za umetanje novih zapisa.
     *
     * @param table Ime tablice iz koje se dohvaćaju ID-evi.
     * @param val   Naziv stupca ID-a.
     * @return Novi ID, ili -1 ako dođe do greške.
     * @throws SQLException u slučaju greške prilikom izvršavanja SQL upita.
     */
    public int createNewID(String table, String val) throws SQLException {

        if(con != null) {
            PreparedStatement stm = con.prepareStatement("select " + val + " from " + table);
            ResultSet r = stm.executeQuery();

            int max = 0;
            while(r.next()) {
                int id = r.getInt(1);
                if(id > max) {
                    max = id;
                }
            }
            max++;

            stm.close();
            return max;
        }

        return -1;
    }

    /**
     * Dodaje novog korisnika u bazu ako ne postoji korisnik s istim korisničkim imenom.
     *
     * @param username Korisničko ime
     * @param password Lozinka korisnika
     * @param email Email adresa korisnika
     * @param address Adresa korisnika
     * @param admin Da li je korisnik admin ("true" ili "false")
     * @return true ako je korisnik uspješno dodan, false ako korisničko ime već postoji
     * @throws SQLException U slučaju greške s bazom podataka
     */
    public boolean addUser(String username, String password, String email, String address, String admin) throws SQLException {

        if (!checkIfUserExists(username)) {
            System.out.println("Username already exists!");
            return false;
        }

        int id = createNewID("Users","userId");
        String insertUserQuery = "INSERT INTO Users (username, password, email, address, userId, admin) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = con.prepareStatement(insertUserQuery)) {

            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, email);
            insertStmt.setString(4, address);
            insertStmt.setInt(5, id);
            insertStmt.setString(6, admin);

            System.out.println("Saving a new user into the database --> Name: " + username + " | ID: " + id);

            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;  // Vraća true ako je dodavanje uspješno
        }
    }

    /**
     * Provjerava u bazi podatak postoji li već korisnik pod
     * ovim korisničkim imenom.
     *
     * @param username Korisničko ime korisnika.
     * @return true ako korisnik ne postoji, ako postoji vraća false.
     * @throws SQLException U slučaju greške prilikom izvršavanja SQL upita.
     */
    private boolean checkIfUserExists(String username) {

        String checkUserQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkUserQuery)) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    /**
     * Učitava korisnika iz baze podataka na temelju korisničkog imena.
     * <p>
     * Ako korisnik s danim korisničkim imenom postoji i lozinka je točna vraća objekt {@code User}.
     * U suprotnom vraća {@code null}.
     *
     * @param username Korisničko ime korisnika.
     * @param inputPassword Lozinka unesena u text box.
     * @return Objekt korisnika ako postoji u bazi, inače {@code null}.
     * @throws SQLException U slučaju greške prilikom izvršavanja SQL upita.
     */
    public User loadAndAuthUserByUsernameFromDB(String username, String inputPassword) throws SQLException {

        if(con != null) {

            System.out.println("Loading user from DB...");
            String slctSQL = "select userId, username, password, email, address, admin from Users where username = ?";
            PreparedStatement slcStm = con.prepareStatement(slctSQL);
            slcStm.setString(1, username);

            ResultSet slcResult = slcStm.executeQuery();

            if(!slcResult.next()) {
                return null;
            }

            int userId = slcResult.getInt(1);
            String usernm = slcResult.getString(2);
            String password = slcResult.getString(3);

            if (!password.equals(inputPassword)){
                System.out.println("Wrong password...");
                return null;
            }

            String email = slcResult.getString(4);
            String address = slcResult.getString(5);
            String st = slcResult.getString(6);

            boolean admin;
            if (st.equals("true")){
                admin = true;
            }else{
               admin = false;
            }
            User user = new User(userId, usernm, password, email, address, admin);

            slcResult.close();
            slcStm.close();

            return user;
        }

        System.out.println("No user by that username");
        return null;
    }

    /**
     * Učitava korisnika iz baze podataka na temelju ID-a korisnika.
     * <p>
     * Ako korisnik s danim ID-om postoji, vraća objekt {@code User}.
     * U suprotnom vraća {@code null}.
     *
     * @param id Jedinstveni identifikator korisnika.
     * @return Objekt korisnika ako postoji u bazi, inače {@code null}.
     * @throws SQLException U slučaju greške prilikom izvršavanja SQL upita.
     */
    public User loadUserByIDFromDB(int id) throws SQLException {

        if(con != null) {

            String slctSQL = "select userId, username, password, email, address, admin from Users where userId = ?";
            PreparedStatement slcStm = con.prepareStatement(slctSQL);
            slcStm.setInt(1, id);

            ResultSet slcResult = slcStm.executeQuery();

            if(!slcResult.next()) {
                return null;
            }

            int userId = slcResult.getInt(1);
            String username = slcResult.getString(2);
            String password = slcResult.getString(3);
            String email = slcResult.getString(4);
            String address = slcResult.getString(5);
            boolean admin = Boolean.parseBoolean(slcResult.getString(6));
            User user = new User(userId, username, password, email, address, admin);

            slcResult.close();
            slcStm.close();

            return user;
        }

        return null;
    }


    /**
     * Učitava sve artikle iz baze podataka.
     * <p>
     * Dohvaća podatke o artiklima iz tablice "Articles" u bazi podataka i dodaje ih u listu artikala.
     *
     * @throws SQLException U slučaju greške prilikom izvršavanja SQL upita.
     */
    public List<Article> loadArticlesFromDB() throws SQLException {

        List<Article> articles = new ArrayList<>();

        if(con != null) {
            System.out.println("Loading from DB...");
            String slctSQL = "select articleId, articleName, price from Articles";
            PreparedStatement slcStm = con.prepareStatement(slctSQL);

            ResultSet slcResult = slcStm.executeQuery();

            while(slcResult.next()) {
                int articleId = slcResult.getInt(1);
                String articleName = slcResult.getString(2);
                float price = slcResult.getFloat(3);
                Article article = new Article(articleId, articleName, price);
                articles.add(article);
            }

            slcResult.close();
            slcStm.close();
        }
        return articles;
    }


    /**
     * Učitava sve narudžbe iz baze podataka za određenog korisnika.
     *
     * @param userId Id korisnika čije se narudžbe učitavaju.
     * @return Strukturu podataka koja predstavlja narudžbe.
     */
    public List<Integer> loadUserOrdersFromDB(int userId) {

        List<Integer> orders = new ArrayList<>();
        String query = "SELECT orderId FROM `CartOrder` WHERE userId = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    orders.add(orderId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while loading orders: " + e.getMessage());
        }

        return orders;
    }


    /**
     * Učitava sve artikle po svakoj narudžbi koja je dodana u listu koja se
     * prima u parametru.
     *
     * @param orderIds Lista ID-eva narudžbi.
     * @return Strukturu podataka koja predstavlja narudžbe i njihove artikle.
     */
    public Map<Integer,Map<Article, Integer>> loadArticlesFromUserOrders(List<Integer> orderIds) throws SQLException {

        Map<Integer,Map<Article, Integer>> orders = new HashMap<>();

        String query = "SELECT OrderArticles.orderId, Articles.articleId, Articles.articleName, Articles.price, OrderArticles.numOfArticles " +
                "FROM OrderArticles " +
                "JOIN Articles ON OrderArticles.articleId = Articles.articleId " +
                "WHERE OrderArticles.orderId = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            for (int orderId : orderIds) {
                statement.setInt(1, orderId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    Map<Article, Integer> articles = new HashMap<>();

                    while (resultSet.next()) {
                        int articleId = resultSet.getInt("articleId");
                        String name = resultSet.getString("articleName");
                        float price = resultSet.getFloat("price");
                        int numOfArticles = resultSet.getInt("numOfArticles");

                        Article article = new Article(articleId, name, price);
                        articles.put(article, numOfArticles);
                    }
                    orders.put(orderId,articles);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while loading articles: " + e.getMessage());
            throw e;
        }

        return orders;
    }


    /**
     * Ova metoda šalje upit u bazu podataka koji dodaje novi red u tablicu 'CartOrder',
     * koja spaja narudžbe s korisnicima.
     *
     * @param orderId ID stvorene narudžbe.
     * @param userId ID prijavljenog korisnika.
     */
    public void addOrderToUser(int userId, int orderId) throws SQLException {

        String query = "INSERT INTO `CartOrder` (userId, orderId) VALUES ( ? , ? )";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, orderId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order successfully added for user with ID: " + userId);
            } else {
                System.out.println("No order was added.");
            }
        } catch (SQLException e) {
            System.err.println("Error while adding order: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Ova metoda prvo poziva metodu {@code addOrderToUser}, te nakon toga
     * tu narudžbu, odnosno njene artikle i količine šalje u bazu podataka
     * koji dodaje novi red u tablicu 'OrderArticles',
     * ta tablica spaja narudžbe s njenim artiklima.
     *
     * @param articles Struktura podataka koja predstavlja količine artikla i same artikle koje dodajemo u narudžbu..
     * @param userId ID prijavljenog korisnika.
     */
    public void addArticles2Order(Map<Article, Integer> articles, int userId) throws SQLException {

        int orderId = createNewID("CartOrder", "orderId");
        addOrderToUser(userId, orderId);

        String query = "INSERT INTO OrderArticles (numOfArticles, orderId, articleId) VALUES (?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            for (Map.Entry<Article, Integer> entry : articles.entrySet()) {
                Article article = entry.getKey();
                int quantity = entry.getValue();

                statement.setInt(1, quantity);
                statement.setInt(2, orderId);
                statement.setInt(3, article.getArticleId());

                statement.executeUpdate();
            }
            System.out.println("All articles successfully added to order with ID: " + orderId);
        } catch (SQLException e) {
            System.err.println("Error while adding articles to order: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Ova metoda dodaje nove artikle u bazu podataka
     *
     * @param articleID ID artikla koji se stvara.
     * @param articleName Naziv artikla koji se dodaje.
     * @param articlePrice Cijena artikla koji se dodaje.
     */
    public void addArticleToDB(int articleID, String articleName, float articlePrice) throws SQLException {

        if (getArticleByName(articleName) != null){
            return;
        }

        if (con != null) {
            String sql = "INSERT INTO Articles (articleId, articleName, price) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                pstmt.setInt(1, articleID);
                pstmt.setString(2, articleName);
                pstmt.setFloat(3, articlePrice);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Artikl uspješno dodan u bazu podataka.");
                }

            } catch (SQLException e) {
                System.out.println("Greška prilikom dodavanja artikla u bazu podataka: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Nema aktivne veze s bazom podataka.");
        }
    }

    public void updateUser(User user) throws SQLException {

        String sql = "UPDATE Users SET username = ?, password = ?, email = ?, address = ?, admin = ? WHERE userId = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getAddress());
            statement.setString(5, Boolean.toString(user.isAdmin()));
            statement.setInt(6, user.getUserId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User data updated successfully.");
            } else {
                System.out.println("No user found with the given userId.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating user data.");
        }
    }

    /**
     * Dohvaća artikl iz baze podataka na temelju naziva artikla.
     *
     * @param articleName Naziv artikla koji treba dohvatiti
     * @return Article objekt ako postoji, inače null
     * @throws SQLException U slučaju greške s bazom podataka
     */
    public Article getArticleByName(String articleName) throws SQLException {

        String query = "SELECT articleId, articleName, price FROM Articles WHERE articleName = ?";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, articleName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int articleId = resultSet.getInt("articleId");
                    String name = resultSet.getString("articleName");
                    float price = resultSet.getFloat("price");

                    return new Article(articleId, name, price);
                } else {
                    return null;
                }
            }
        }
    }
}