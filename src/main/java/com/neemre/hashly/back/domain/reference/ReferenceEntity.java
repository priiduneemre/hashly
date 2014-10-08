package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.neemre.hashly.back.domain.Entity;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public abstract class ReferenceEntity extends Entity {

}