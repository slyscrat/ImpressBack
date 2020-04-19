package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractDataBaseEntity {
/*
	@Version
	private Integer version;

*/
}
