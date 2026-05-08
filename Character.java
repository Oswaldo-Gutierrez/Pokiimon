// Modelo de datos para un personaje de Dragon Ball
public class Character {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    // Puedes agregar más campos según la respuesta de la API

    public Character(int id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}
