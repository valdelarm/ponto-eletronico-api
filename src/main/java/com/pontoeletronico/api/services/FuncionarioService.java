package com.pontoeletronico.api.services;

import java.util.Optional;

import com.pontoeletronico.api.entities.Funcionario;

public interface FuncionarioService {
	
	/**
	 * Persiste um funcionario na base de dados.
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca e retorna um funcinario dado um cpf.
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	
	/**
	 * Busca e retorna um funcinario dado um email.
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 * Busca e retorna um funcinario por ID.
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorId(Long id);
	

}
