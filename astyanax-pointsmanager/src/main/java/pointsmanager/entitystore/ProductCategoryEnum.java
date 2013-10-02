package pointsmanager.entitystore;

public enum ProductCategoryEnum {
	FROZEN_FOODS(1),
	AMBIENT_FOODS(2),
	BABY_PRODUCT(2),
	CLOTIHING(3),
	ELECTRONICS(4),
	FURNITURES(5);
	
	private int value;
	
	private ProductCategoryEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static boolean isProductCategoryFound(String name) {
		String nameInLowerCase = name.toLowerCase();
		for (ProductCategoryEnum productCategory : values()) {
			if (productCategory.name().toLowerCase().equals(nameInLowerCase))
			return true;
		}
		return false;
	}
}
