package com.springboot.clienteapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.clienteapp.models.entity.Ciudad;
import com.springboot.clienteapp.models.entity.Cliente;
import com.springboot.clienteapp.models.service.ICiudadService;
import com.springboot.clienteapp.models.service.IClienteService;

@Controller
@RequestMapping("/views/clientes")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private ICiudadService ciudadService;
	
	@GetMapping("/")
	public String listarClientes(Model model) {
		List<Cliente> listadoClientes = clienteService.listarTodos();
		
		model.addAttribute("titulo", "Lista de Clientes");
		model.addAttribute("clientes", listadoClientes);
		return "/views/clientes/listar";
	}
	
	@GetMapping("/create")
	public String crear(Model model) {
		
		Cliente cliente = new Cliente();
		List<Ciudad> listCiudades = ciudadService.listaCiudades();
		
		model.addAttribute("titulo", "Formulario: Nuevo Cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("ciudades", listCiudades);
		
		return "/views/clientes/crear";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
		List<Ciudad> listCiudades = ciudadService.listaCiudades();
		
		if(result.hasErrors()) {			
			
			model.addAttribute("titulo", "Formulario: Nuevo Cliente");
			model.addAttribute("cliente", cliente);
			model.addAttribute("ciudades", listCiudades);
			System.out.println("Verificar registro, campos incorrectos");
			
			return "/views/clientes/crear";
			
		}
		
		clienteService.guardar(cliente);
		System.out.println("Registro correcto");
		
		return "redirect:/views/clientes/";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Long idCliente, Model model) {
		
		Cliente cliente = null;
		
		if(idCliente > 0) {
			cliente = clienteService.buscarPorId(idCliente);
			
			if (cliente == null ) {
				System.out.println("ERROR: El id no existe");
				return "redirect:/views/clientes/";
				
			}
			
		}else {
			System.out.println("ERROR: El id no es válido");
			return "redirect:/views/clientes/";
			
		}
		
		List<Ciudad> listCiudades = ciudadService.listaCiudades();
		
		model.addAttribute("titulo", "Formulario: Editar Cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("ciudades", listCiudades);
		
		return "/views/clientes/crear";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long idCliente) {
		
		Cliente cliente = null;
		
		if(idCliente > 0) {
			cliente = clienteService.buscarPorId(idCliente);
			
			if (cliente == null ) {
				System.out.println("ERROR: El id no existe");
				return "redirect:/views/clientes/";
				
			}
			
		}else {
			System.out.println("ERROR: El id no es válido");
			return "redirect:/views/clientes/";
			
		}
		
		clienteService.eliminar(idCliente);
		System.out.println("Eliminado con éxito");
		
		return "redirect:/views/clientes/";
	}
}
