public class Constants {

    private Constants() {
        throw new IllegalAccessError("Utility class");
    }

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static final String CREATE_USER = "/api/auth/register";

    public static final String DELETE_USER = "/api/auth/user";

    public static final String USER_AUTH = "/api/auth/login";

    public static final String USER_CHANGE = "/api/auth/user";

    public static final String CREATE_ORDER = "/api/orders";

    public static final String GET_INGREDIENT = "/api/ingredients";

    public static final String GET_ORDERS = "/api/orders";



}
