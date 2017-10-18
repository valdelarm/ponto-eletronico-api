package com.pontoeletronico.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.pontoeletronico.api.entities.Lancamento;

public interface LancamentoService {

	
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	Lancamento persistir(Lancamento lancamento);
	
	void remover (Long id);
}
