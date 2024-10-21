package models;

/**
 * Klasa {@code Article} predstavlja model artikla u trgovini.
 * Sadrži osnovne informacije o artiklu kao što su ID artikla, naziv, cijena i put do slike.
 *
 * @author Andrej Markanjević
 */
public class Article {

    private int articleId;
    private String articleName;
    private float price;
    private String pathToPic;


    /**
     * Konstruktor klase {@code Article}.
     * Inicijalizira novi objekt artikla s navedenim ID-om, nazivom, cijenom i putem do slike.
     *
     * @param articleId   Jedinstveni identifikator artikla.
     * @param articleName Naziv artikla.
     * @param price       Cijena artikla.
     */
    public Article(int articleId, String articleName, float price) {
        this.articleId = articleId;
        this.articleName = articleName;
        this.price = price;
        this.pathToPic = "src/main/resources/unavailable.jpg";
    }

    public int getArticleId() {
        return articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public float getPrice() {
        return price;
    }

    public String getPathToPic() {
        return pathToPic;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", price=" + price +
                '}';
    }
}