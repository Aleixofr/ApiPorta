package com.afr.ApiPorta.service;

import com.afr.ApiPorta.model.Usuario;
import com.afr.ApiPorta.model.dto.Auth.AuthResponse;
import com.afr.ApiPorta.model.dto.Auth.LoginRequest;
import com.afr.ApiPorta.model.dto.Auth.RegisterRequest;
import com.afr.ApiPorta.model.dto.UsuarioDto;
import com.afr.ApiPorta.model.dto.VendedorDto;
import com.afr.ApiPorta.repository.UsuarioRepository;
import com.afr.ApiPorta.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Método requerido por Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_" + usuario.getRole().name())
                .build();
    }

    public List<UsuarioDto> getAllUsuarios(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UsuarioDto getUsuarioById(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );

        return convertToDto(usuario);
    }

    public UsuarioDto getUsuarioByEmail(String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );

        return convertToDto(usuario);
    }

    /**
     * Transformer usuario -> usuarioDto
     * @param usuario
     * @return UsuarioDto
     */
    private UsuarioDto convertToDto(Usuario usuario){
        UsuarioDto dto = new UsuarioDto();

        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setTelefono(usuario.getTelefono());
        dto.setRole(usuario.getRole());

        if(usuario.getVendedor() != null){
            VendedorDto vendedorDto = new VendedorDto();
            vendedorDto.setId(usuario.getVendedor().getId());
            vendedorDto.setNombre(usuario.getVendedor().getNombre());
            vendedorDto.setDescripcion(usuario.getVendedor().getDescripcion());
            vendedorDto.setDireccion(usuario.getVendedor().getDireccion());
            vendedorDto.setIsActive(usuario.getVendedor().getIsActive());
            dto.setVendedor(vendedorDto);
        }

        return dto;
    }
}