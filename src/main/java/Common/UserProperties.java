package Common;

public class UserProperties {

    public boolean addLogon;
    public boolean configVarStock;
    public boolean editProd;
    public boolean extract;
    public boolean pcIn;
    public String name;

    public UserProperties(boolean addLogon, boolean configVarStock, boolean editProd, boolean extract, boolean pcIn, String name) {

        this.addLogon = addLogon;
        this.configVarStock = configVarStock;
        this.editProd = editProd;
        this.extract = extract;
        this.pcIn = pcIn;
        this.name = name;
    }
}
