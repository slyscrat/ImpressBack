package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Version;

import javax.persistence.MappedSuperclass;

@Data
//@All
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractDataBaseEntity {
	// TODO : del
	/*@Version
	private Integer version;*/
}
