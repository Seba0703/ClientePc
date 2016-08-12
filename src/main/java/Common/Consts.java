package Common;

public class Consts {

    public final static String WELCOME_T = "Bienvenido";

    public final static String LOGIN_TITLE = "Login";

    // -> headers constantes
    public final static String USER = "user_id";
    public final static String PASS = "pass";
    public final static String CHECK_PASS = "check";
    public static final String PASS_NEW = "passNew";

    // -> paths
    public final static String LOGIN = "/login";
    public final static String LOGON = "/logon";
    public final static String MATERIALS_ID = "/mat_matID";
    public final static String MAT_ADD = "/mat_add";
    public final static String VARS_CONFIG = "/varsConfig";
    public final static String MAT_STATUS = "/mat_status";
    public final static String MAT_TRANSACTIONS = "/mat_transacDest";
    public final static String USERS_ID = "/userID";
    public final static String EDIT_PROD = "/mat_edit_prod";
    public static final String DELETE_PROD = "/deleteProd";
    public final static String MAT_TRANSACTIONS_CHANGE = "/mat_transacChange";
    public final static String SET_USERS = "/setUsers";

    // -> Request Methods
    public final static String POST = "POST";
    public final static String GET = "GET";
    public final static String PUT = "PUT";

    // JSONObject keys
    public final static String RESULT = "resultado";
    public final static String MATERIALS = "insumo_id";
    public final static String QUANTITY = "cantidad";
    public final static String DUE_DATE = "fechaVto";
    public final static String PRICE = "precio";
    public final static String TRANSACTION_DATE = "fechaTrans";
    public final static String STOCK_MAX = "MaxStock";
    public final static String STOCK_MIN = "MinStock";
    public final static String STOCK_SAFE = "SafeStock";
    public final static String STOCK_MULTIPLY = "MultiplyStock";
    public final static String INSUMOS = "insumos";
    public final static String TO_BUY = "paraComprar";
    public final static String TRANSACTION_TYPE = "tipoTrans";
    public final static String DESTINY = "destino";

    public final static String NEW_VALUE = "nuevo";
    public final static String MATERIALSnew = "newInsumo_id";
    public final static String QUANTITYnew = "newCantidad";
    public final static String PRICEnew = "newPrecio";
    public final static String TRANS_DATEnew = "newTransDate";
    public final static String DUE_DATEnew = "newFechaVto";
    public final static String USERnew = "newUser_id";

    // resultado de estado de stock
    public final static int RED = 0;
    public final static int YELLOW = 1;
    public final static int WHITE = 2;
    // color a los cambios
    public final static int WHITE_CHANGE = 2;
    public final static int GREEN_CHANGE = 1;
    public final static int RED_CHANGE = 0;

    //combo box constantes
    public final static String IN = "ENTRADA";
    public final static String OUT = "SALIDA";
    public final static String CHANGEold = "CAMBIO VIEJO";
    public final static String CHANGEnew = "CAMBIO NUEVO";
    public final static String EMPTY = null;
    public final static String NAZCA = "Nazca";
    public final static String EVA = "Eva";
    public final static String CELINA = "Celina";

    //user properties
    public final static String PROP_ADD_LOGON = "agregarUser";
    public final static String PROP_EXTRACT = "sacarInsumo";
    public final static String PROP_EDIT = "editarInsumo";
    public final static String PROP_CONFIG_STOCK_VARS = "configStockVars";
    public final static String PROP_PC_IN = "ingresoPC";

    public final static int FIRST_BUY_YEAR = 2000;
}