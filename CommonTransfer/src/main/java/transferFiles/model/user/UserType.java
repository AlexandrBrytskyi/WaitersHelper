package transferFiles.model.user;


public enum UserType {

    ADMIN, WAITER, COLD_KITCHEN_COCK, BARMEN, HOT_KITCHEN_COCK, MANGAL_COCK;

    public static UserType[] valuesWhoCoock() {
        return new UserType[]{COLD_KITCHEN_COCK, BARMEN, HOT_KITCHEN_COCK, MANGAL_COCK};
    }
}
