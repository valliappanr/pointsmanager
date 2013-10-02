package pointsmanager.entitystore;

import java.util.Date;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

public class CategoryEvent {
	
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

	public static AnnotatedCompositeSerializer<CategoryEvent> getCategoryeventserializer() {
		return categoryEventSerializer;
	}

	private static final AnnotatedCompositeSerializer<CategoryEvent> categoryEventSerializer = 
			new AnnotatedCompositeSerializer<CategoryEvent>(CategoryEvent.class);	
	

}
