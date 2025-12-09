package LexBeautyConsulting.demo.services;

import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.repository.UsuarioRepository;
import LexBeautyConsulting.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuarios u = usuarioRepository.findByEmail(email).orElse(null);

        if (u == null) {
            throw new UsernameNotFoundException("El correo electrónico " + email + " no existe en el sistema.");
        }

        return withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(u.getRoles().getNombreRol())
                .build();
    }


    public List<Usuarios> getUsuarios(boolean soloActivos) {
        return usuarioRepository.findAll();
    }

    public Optional<Usuarios> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    public void save(Usuarios usuario, MultipartFile imagenFile, boolean crear) {
        usuarioRepository.save(usuario);
    }

    public void delete(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }


    @Transactional
    public void saveClientes(Usuarios u) {
        if (usuarioRepository.existsByEmail(u.getEmail())) {
            throw new IllegalStateException("El correo electrónico ya existe en el sistema.");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoles(rolRepository.findByNombreRol("ROLE_CLIENTE"));
        usuarioRepository.save(u);
    }

    public void saveAdministrador(Usuarios u) {
        if (usuarioRepository.existsByEmail(u.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya existe en el sistema.");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoles(rolRepository.findByNombreRol("ROLE_ADMIN"));
        usuarioRepository.save(u);
    }

    public Optional<Usuarios> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}