package org.raflab.shared.events;

public class GuideCreatedEvent {

    private Long guideId;
    private String firstName;
    private String lastName;

    public GuideCreatedEvent() {
    }

    public GuideCreatedEvent(Long guideId, String firstName, String lastName) {
        this.guideId = guideId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
