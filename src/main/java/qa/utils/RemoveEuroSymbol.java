package qa.utils;

public class RemoveEuroSymbol {
    public String getAmountWithoutEuro(String amount){
        // Remove the Euro symbol and any leading/trailing whitespace
        amount = amount.replaceAll("[^0-9.,]", "").trim();

        // Replace any comma with a period to ensure it's a valid float representation
        amount = amount.replace(',', '.');

        return amount;
    }
}
