package com.entity;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.record.InstrumentRecord;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "INSTRUMENT_PRICE_MODIFIER")
@DynamicInsert
@DynamicUpdate
@NamedNativeQuery(name = "InstrumentPriceModifier.findAll",
					query = "SELECT i.ID as id, i.NAME as NAME, i.MULTIPLIER as multiplier FROM INSTRUMENT_PRICE_MODIFIER i WHERE NAME = ?",
					resultSetMapping = "Mapping.InstrumentRecord")
@SqlResultSetMapping(name = "Mapping.InstrumentRecord",
   						classes = @ConstructorResult(targetClass = InstrumentRecord.class,
                        columns = {@ColumnResult(name = "id", type = Integer.class), 
                        			@ColumnResult(name = "name", type = String.class), 
                        			@ColumnResult(name = "multiplier", type = Double.class)})
)
public class InstrumentPriceModifier {

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", length = 255)
	private String name;
	
	@Column(name = "INSTRUMENT_DATE")
	private Date date;

	@Column(name = "MULTIPLIER")
	private Double multiplier;
	
}