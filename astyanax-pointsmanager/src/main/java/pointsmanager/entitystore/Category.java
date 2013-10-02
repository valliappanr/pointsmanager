package pointsmanager.entitystore;

public enum Category {
	FROZEN_FOODS(1),
	AMBIENT_FOODS(2),
	BABY_PRODUCT(2),
	CLOTIHING(3),
	ELECTRONICS(4),
	FURNITURES(5);
	
	private int value;
	
	private Category(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
