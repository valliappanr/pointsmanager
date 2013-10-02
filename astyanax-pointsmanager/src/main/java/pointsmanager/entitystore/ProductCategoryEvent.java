package pointsmanager.entitystore;

import java.util.Date;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

/**
 * Pojo Mapping Composite column name mapping for product category column family.
 * 
 * Row key is mapped to memberId : date
 * 
 */

public class ProductCategoryEvent {
	
	@Component(ordinal=0) private String memberId;
	@Component(ordinal=0) private Date date;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public static AnnotatedCompositeSerializer<ProductCategoryEvent> getCategoryeventserializer() {
		return categoryEventSerializer;
	}

	private static final AnnotatedCompositeSerializer<ProductCategoryEvent> categoryEventSerializer = 
			new AnnotatedCompositeSerializer<ProductCategoryEvent>(ProductCategoryEvent.class);	
	

}
