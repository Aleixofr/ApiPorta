package com.afr.ApiPorta.controller;

import com.afr.ApiPorta.model.Usuario;
import com.afr.ApiPorta.model.dto.UsuarioDto;
import com.afr.ApiPorta.model.dto.response.ApiResponse;
import com.afr.ApiPorta.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDto>>> getAllUsuarios(){
        List<UsuarioDto> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDto>> getUsuarioById(@PathVariable Long id){
        UsuarioDto usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UsuarioDto>> getUsuarioByEmail(@PathVariable String email){
        UsuarioDto usuario = usuarioService.getUsuarioByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }
}
