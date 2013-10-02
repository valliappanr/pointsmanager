package pointsmanager.entitystore;

import java.util.Date;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

public class CategoryRowKey {
	
	@Component(ordinal=0) private String categoryId;
	
	@Component(ordinal=1) private Date date;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public static AnnotatedCompositeSerializer<CategoryRowKey> getCategoryrowkeyserializer() {
		return categoryRowKeySerializer;
	}

	private static final AnnotatedCompositeSerializer<CategoryRowKey> categoryRowKeySerializer = 
			new AnnotatedCompositeSerializer<CategoryRowKey>(CategoryRowKey.class);	
	
}
