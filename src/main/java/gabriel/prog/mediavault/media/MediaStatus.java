package gabriel.prog.mediavault.media;

public enum MediaStatus {
    SEEN("Gesehen"),
    UNSEEN("Nicht gesehen");

    private final String displayName;

    MediaStatus(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MediaStatus fromDisplayName(String displayName) {
        for (MediaStatus status: values()) {
            if (status.displayName.equals(displayName)){
                return status;
            }
        }
        return UNSEEN;
    }

    @Override
    public String toString() {
        return displayName;
    }
}