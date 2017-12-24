package com.pontoeletronico.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pontoeletronico.api.dtos.CadastroPFDto;
import com.pontoeletronico.api.dtos.CadastroPJDto;
import com.pontoeletronico.api.entities.Empresa;
import com.pontoeletronico.api.entities.Funcionario;
import com.pontoeletronico.api.enums.PerfilEnum;
import com.pontoeletronico.api.response.Response;
import com.pontoeletronico.api.services.EmpresaService;
import com.pontoeletronico.api.services.FuncionarioService;
import com.pontoeletronico.api.utils.PasswordUtils;

@RestController
@RequestMapping("api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
			
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPFController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> 
	cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		response.setData(this.converterCadastroPFDto(funcionario));
		return ResponseEntity.ok(response);
	}

	private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
		CadastroPFDto dto = new CadastroPFDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt().
			ifPresent(qtdHorasAlmoco -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
			qtdHorasTrabalho -> dto.setQtdHorasTrabalhadas(Optional.of(Float.toString(qtdHorasTrabalho))));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> dto.setValorHora(Optional.of(valorHora.toString())));
		
		return dto;
	}

	private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USER);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		cadastroPFDto.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhadas().ifPresent(qtdHorasTrabalhadas-> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhadas)));

		return funcionario;
	}


	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj()).
			ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existe")));
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf()).
			ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe")));
		
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail()).
		ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe")));
	}
}
