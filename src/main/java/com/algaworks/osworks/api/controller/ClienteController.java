package com.algaworks.osworks.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.CodeFlow.ClinitAdder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;

@RestController
@RequestMapping("/clientes")																		//Marcação de mapeamento de rota
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository; 													// Faz a injeção do repositorio 
	
	@GetMapping					
	public List<Cliente> lista() {
		return clienteRepository.findAll();															//Faz uma busca de todos os clientes
	}
	
	@GetMapping("/{clienteId}")																		// Marcação de mapeamento de rota
	public ResponseEntity<Cliente> buscar(@PathVariable  Long clienteId) {  
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);  						//Faz uma busca por ID de Cliente.
		
		if(cliente.isPresent()) {										 							// Verifica  se cliente existe.
			return ResponseEntity.ok(cliente.get()); 												//Retorna Status OK no HTTP quando cliente existir.
		}
		
		return ResponseEntity.notFound().build();   												//Retorna 404 no Status HTTP caso o cliente não exista.
	}
	
	@PostMapping																					// Marcação de mapeamento de Post(criação)
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody  Cliente cliente) {
		return 	clienteRepository.save(cliente);													//Salva Novo cliente no Banco 
		
	}
	
	@PutMapping("/{clienteId}")																		// Marcação de mapeamento de Put(Atualização)
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente){
		if (!clienteRepository.existsById(clienteId)) { 											//Verifica se cliente não existe
			return ResponseEntity.notFound().build();												//Retorna 404 notFound caso não exista.
		}
		cliente.setId(clienteId);																	//Passa o ID do cliente a ser atualizado
		cliente = clienteRepository.save(cliente);													//Salva Atualização de cliente no banco.
		
		return ResponseEntity.ok(cliente);															//Retorna OK 200 
	}
	
	@DeleteMapping("/{clienteId}")																	// Marcação de mapeamento de Delete
	public ResponseEntity<Void> remover(@PathVariable Long clienteId){
		
		if (!clienteRepository.existsById(clienteId)) {												//Verifica se cliente não existe
			return ResponseEntity.notFound().build();												//Retorna 404 notFound caso não exista.
		}
		clienteRepository.deleteById(clienteId);													//Deleta cliente do banco 
		return ResponseEntity.noContent().build();													//Retorna NOCONTENT  204
		 
	}

}
