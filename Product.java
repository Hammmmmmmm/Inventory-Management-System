import java.time.LocalDateTime;
public class Product {
    private int id;
    private String productName;
    private String stockKeepingUnit;
    private int quantity;
    private Department department;
    private int supplierID;
    private float price;
    private LocalDateTime expiry;


    public enum Department {
        PRODUCE,
        MEAT,
        SEAFOOD,
        DELI,
        BAKERY,
        DAIRY,
        FROZEN,
        DRY,
        BAKING,
        SNACKS,
        PHARMACY,
        CLEANING,
        PET,
        ALCOHOL
    }
}
