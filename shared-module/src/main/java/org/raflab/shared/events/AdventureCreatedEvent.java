package org.raflab.shared.events;

public class AdventureCreatedEvent {

    private Long adventureId;
    private String title;
    private String difficulty;
    private String categoryName;

    public AdventureCreatedEvent() {
    }

    public AdventureCreatedEvent(Long adventureId, String title, String difficulty, String categoryName) {
        this.adventureId = adventureId;
        this.title = title;
        this.difficulty = difficulty;
        this.categoryName = categoryName;
    }

    public Long getAdventureId() {
        return adventureId;
    }

    public void setAdventureId(Long adventureId) {
        this.adventureId = adventureId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
