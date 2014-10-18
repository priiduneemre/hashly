package com.neemre.hashly.backend.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.neemre.hashly.backend.domain.Entity;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public abstract class ReferenceEntity extends Entity {

}