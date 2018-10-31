public class Currency {
    private int coinCod;
    private String coinName;
    private int coinValue;

    public Currency(int coinCod, String coinName, int coinValue){
        this.coinCod = coinCod;
        this.coinName = coinName;
        this.coinValue = coinValue;
    }

    public int getCoinCod() {
        return coinCod;
    }

    public String getCoinName() {
        return coinName;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(int coinValue) {
        this.coinValue = coinValue;
    }
}
