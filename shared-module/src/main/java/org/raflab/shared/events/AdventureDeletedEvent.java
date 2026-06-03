package org.raflab.shared.events;

public class AdventureDeletedEvent {

    private Long adventureId;
    private String title;

    public AdventureDeletedEvent() {
    }

    public AdventureDeletedEvent(Long adventureId, String title) {
        this.adventureId = adventureId;
        this.title = title;
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
}
