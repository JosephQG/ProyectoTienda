package com.tienda.service;

import com.tienda.domain.Usuario;
import com.tienda.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }
    
    //busca usuario en la tabla usuarios,guarda la foto en un atributo de session
    //recupera los roles asociados del usuario y retoena la info como userDetails
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Se busca el usuario
        Usuario usuario= usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow( () -> new UsernameNotFoundException("Usuario no encontrado: "+username));
        
        //Si estamos aca se encontro el usuario... se guarda la foto
        session.removeAttribute("imagenUsuario");
        session.setAttribute("imagenUsuario", usuario.getRutaImagen());
        
        //Se recuperan los roles del usuario y efectivamente se hacer roles de seguridad
        var roles = usuario.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_"+rol.getRol()))
                .collect(Collectors.toSet());  
        //Se retorna el usuario con la informacion para que sea procesado en el login...
        return new User(usuario.getUsername(),usuario.getPassword(),roles);
    }
    
}
