package com.afr.ApiPorta.service;

import com.afr.ApiPorta.model.CodigoPostal;
import com.afr.ApiPorta.model.Role;
import com.afr.ApiPorta.model.Usuario;
import com.afr.ApiPorta.model.Vendedor;
import com.afr.ApiPorta.model.dto.Auth.AuthResponse;
import com.afr.ApiPorta.model.dto.Auth.RegisterRequest;
import com.afr.ApiPorta.model.dto.UsuarioDto;
import com.afr.ApiPorta.model.dto.VendedorDto;
import com.afr.ApiPorta.repository.UsuarioRepository;
import com.afr.ApiPorta.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Credenciales invalidas")
        );

        if(!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(usuario);

        UsuarioDto usuarioDto = convertUsuarioToDto(usuario);

        return new AuthResponse(token, usuarioDto);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if(usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setRole(request.getRole());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (request.getRole() == Role.VENDEDOR) {
            createVendedorProfile(usuarioGuardado, request);
        }

        String token = jwtService.generateToken(usuarioGuardado);

        Usuario usuarioCompleto = usuarioRepository.findById(usuarioGuardado.getId())
                .orElseThrow(() -> new RuntimeException("Error al recuperar usuario"));

        UsuarioDto usuarioDto = convertUsuarioToDto(usuarioCompleto);

        return new AuthResponse(token, usuarioDto);
    }

    private void createVendedorProfile(Usuario usuario, RegisterRequest request) {
        Vendedor vendedor = new Vendedor();
        vendedor.setUsuario(usuario);
        vendedor.setNombre(request.getNegocio() != null ? request.getNegocio() : request.getNombre());
        vendedor.setDescripcion(request.getDescripcion());
        vendedor.setDireccion(request.getDireccion());
        vendedor.setIsActive(true);

        vendedorRepository.save(vendedor);
    }

    private UsuarioDto convertUsuarioToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setTelefono(usuario.getTelefono());
        dto.setRole(usuario.getRole());

        if (usuario.getVendedor() != null) {
            VendedorDto vendedorDto = new VendedorDto();
            vendedorDto.setId(usuario.getVendedor().getId());
            vendedorDto.setNombre(usuario.getVendedor().getNombre());
            vendedorDto.setDescripcion(usuario.getVendedor().getDescripcion());
            vendedorDto.setDireccion(usuario.getVendedor().getDireccion());
            vendedorDto.setIsActive(usuario.getVendedor().getIsActive());

            // Obtener códigos postales si existen
            if (usuario.getVendedor().getZonaPostal() != null) {
                vendedorDto.setCodigosPostales(
                        usuario.getVendedor().getZonaPostal()
                                .stream()
                                .map(CodigoPostal::getCodigo)
                                .toList()
                );
            }

            dto.setVendedor(vendedorDto);
        }

        return dto;
    }
}
