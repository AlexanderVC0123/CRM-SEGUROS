package com.crm.seguro.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.seguro.dto.ClienteDTO;
import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.entity.Rol;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.service.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") //Permitir accesso desde frontend
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioActual(Authentication authentication){
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private boolean esAdmin(Usuario usuario){
        return usuario.getRol() == Rol.ADMIN;
    }

    private Usuario resolverUsuarioAsignado(Cliente cliente, Usuario usuarioActual){
        // Si es admin y manda un usuario dentro del cliente, puede asignarlo a ese comercial.
        if (esAdmin(usuarioActual) && cliente.getUsuario() != null && cliente.getUsuario().getId() != null) {
            return usuarioRepository.findById(cliente.getUsuario().getId())
            .orElseThrow(() -> new RuntimeException("Comercial no encontrado"));
        }

        // Si no es admin, el cliente siempre queda en su propia cartera.
        return usuarioActual;
    }

    @GetMapping
    public Page<ClienteDTO> obtenerClientes(Pageable pageable, Authentication authentication){
        Usuario usuario = getUsuarioActual(authentication);

        if (esAdmin(usuario)) {
            return clienteService.obtenerTodos(pageable);
        }

        return clienteService.obtenerTodosPorUsuario(usuario.getId(), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id, Authentication authentication){
        Usuario usuario = getUsuarioActual(authentication);

        if (esAdmin(usuario)) {
            return clienteService.obtenerPorId(id)
            .map(DTOMapper::toClienteDTO)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
        }

        return clienteService.obtenerPorIdYUsuario(id, usuario.getId())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody Cliente cliente, Authentication authentication){
        Usuario usuario = getUsuarioActual(authentication);
        Usuario usuarioAsignado = resolverUsuarioAsignado(cliente, usuario);

        // El cliente queda asociado al comercial que corresponda segun el rol.
        Cliente guardado = clienteService.guardarCliente(cliente, usuarioAsignado);

        return ResponseEntity.ok(DTOMapper.toClienteDTO(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody Cliente datos, Authentication authentication){
        Usuario usuario = getUsuarioActual(authentication);

        Cliente existente = esAdmin(usuario)
        ? clienteService.obtenerPorId(id).orElse(null)
        : clienteService.obtenerPorId(id)
            .filter(cliente -> cliente.getUsuario() != null && cliente.getUsuario().getId().equals(usuario.getId()))
            .orElse(null);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioAsignado = resolverUsuarioAsignado(datos, usuario);
        Cliente actualizado = clienteService.actualizarCliente(existente, datos, usuarioAsignado);

        return ResponseEntity.ok(DTOMapper.toClienteDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id, Authentication authentication){
        Usuario usuario = getUsuarioActual(authentication);

        if (esAdmin(usuario)) {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        }

        boolean eliminado = clienteService.eliminarCliente(id, usuario.getId());

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
