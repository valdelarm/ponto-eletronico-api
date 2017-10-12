package com.pontoeletronico.api.services;

import java.util.Optional;

import com.pontoeletronico.api.entities.Empresa;

public interface EmpresaService {

	/**
	 * Busca uma empresa dado um cnpj.
	 * @param cnpj
	 * @return
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa.
	 * @param empresa
	 * @return
	 */
	Empresa persistir(Empresa empresa);
}
