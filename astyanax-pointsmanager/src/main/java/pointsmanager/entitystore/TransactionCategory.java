package pointsmanager.entitystore;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionCategory {
	
	@Id
	private TransactionCategoryRowKey rowKey;
	
	@Column
	private Date date;
	
	@Column
	private String value;
	
	
	
	public TransactionCategoryRowKey getRowKey() {
		return rowKey;
	}



	public void setRowKey(TransactionCategoryRowKey rowKey) {
		this.rowKey = rowKey;
	}


	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public static class TransactionCategoryRowKey implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2265918752028902062L;

		private String memberId;
		
		private Date date;

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
	}

}
