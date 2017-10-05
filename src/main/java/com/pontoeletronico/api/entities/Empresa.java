package com.pontoeletronico.api.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "EMPRESA")
public class Empresa implements Serializable {
    private static final long serialVersionUID = 3960436649365666213L;
}
