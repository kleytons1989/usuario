package com.kleyton.usuario.business;

import com.kleyton.usuario.business.converter.UsuarioConverter;
import com.kleyton.usuario.business.dto.UsuarioDTO;
import com.kleyton.usuario.infrastructure.entity.Usuario;
import com.kleyton.usuario.infrastructure.exceptions.ConflictException;
import com.kleyton.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.kleyton.usuario.infrastructure.repository.UsuarioRepository;
import com.kleyton.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));


        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO( usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException(("Email já cadastrado" + email));
            }
        }catch (ConflictException e){
            throw new ConflictException(("Email já cadastrado" + e.getCause()));
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);

    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){

        // aqui buscamos o email do usuario atraves do token (tirar a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring( 7));

        //criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        //Busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        //Mesclou os dados que recebemos na requisição DTO com os dados do bancop de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuário convertido e depois pegou o retorno e converteu para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));


    }

}
