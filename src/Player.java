/**
 * Player.java
 * Stores information about the person playing.
 * In Phase 1 we only need the name. Later phases will add
 * playerId and email when we connect to the database.
 */
public class Player {

    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}