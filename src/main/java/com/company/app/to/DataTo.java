package com.company.app.to;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.company.app.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Data Transfer Object used in this Demo application. This transfer object class is thread-safe because it is immutable
 * -- it does not have neither public constructors nor setters and new instances can be created only thru the static
 * inner class builder. This class anotated to allow jackson to use the builder to do marshalling and unmarshalling
 * activities.
 * 
 * 
 * @since 12/3/2018 8:05 AM
 *
 */
@JsonDeserialize(builder = DataTo.Builder.class)
public class DataTo {

    private final String id;
    private final String name;
    private final LocalDate date;
    private final BigDecimal amount;
    private final String customerName;
    private final String transactionType;
	private DataTo(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.date = builder.date;
		this.amount = builder.amount;
		this.customerName = builder.customerName;
		this.transactionType = builder.transactionType;
	}
	/**
	 * Creates builder to build {@link DataTo}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	/**
	 * Creates a builder to build {@link DataTo} and initialize it with the given object.
	 * @param dataTo to initialize the builder with
	 * @return created builder
	 */
	public static Builder builder(DataTo dataTo) {
		return new Builder(dataTo);
	}
	/**
	 * Builder to build {@link DataTo}.
	 */
	@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
	public static final class Builder {
		private String id;
		private String name;
		private LocalDate date;
		private BigDecimal amount;
		private String customerName;
		private String transactionType;

		private Builder() {
		}

		private Builder(DataTo dataTo) {
			this.id = dataTo.id;
			this.name = dataTo.name;
			this.date = dataTo.date;
			this.amount = dataTo.amount;
			this.customerName = dataTo.customerName;
			this.transactionType = dataTo.transactionType;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder date(LocalDate date) {
			this.date = date;
			return this;
		}

		public Builder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public Builder customerName(String customerName) {
			this.customerName = customerName;
			return this;
		}

		public Builder transactionType(String transactionType) {
			this.transactionType = transactionType;
			return this;
		}

		public DataTo build() {
			return new DataTo(this);
		}
		
	    @JsonIgnore
	    public String getDateFmt1Str() {
	        return date.format(DateUtil.DATE_FMT_MDYYYY);
	    }

	    @JsonIgnore
	    public String getDateFmt2Str() {
	        return date.format(DateUtil.DATE_FMT_YYYYMMDD);
	    }
	}
	
	
	
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public LocalDate getDate() {
		return date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	@Override
	public String toString() {
		return "DataTo [id=" + id + ", name=" + name + ", date=" + date + ", amount=" + amount + ", customerName="
				+ customerName + ", transactionType=" + transactionType + "]";
	}

   
}
