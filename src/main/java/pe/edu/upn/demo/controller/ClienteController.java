package pe.edu.upn.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pe.edu.upn.demo.entity.Cliente;
import pe.edu.upn.demo.entity.Usuario;
import pe.edu.upn.demo.service.ClienteService;
import pe.edu.upn.demo.service.UsuarioService;


@Controller
@RequestMapping("/cliente")
@SessionAttributes({"cliente","usuario"})

public class ClienteController {

@Autowired
private ClienteService clienteService;

@Autowired
private UsuarioService usuarioService;

@Autowired
    private PasswordEncoder passwordEncoder;

@GetMapping
public String inicio(Model model) {
 try {
  List<Cliente> clientes= clienteService.findAll();
  model.addAttribute("clientes", clientes);
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/cliente/inicio";
}

@GetMapping("/edit/{id}")
public String editar(@PathVariable("id") String id, Model model) {
 try {
  Optional<Cliente> optional = clienteService.findById(id);
  if (optional.isPresent()) {
   
   model.addAttribute("cliente", optional.get());
   
  } else {
   return "redirect:/cliente";
  }
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/cliente/edit";
}

@PostMapping("/save")
public String save(@ModelAttribute("cliente") Cliente cliente, Model model, SessionStatus status) {
 try {
  clienteService.save(cliente);
  status.setComplete();
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "redirect:/cliente";
}

@GetMapping("/nuevo")
public String nuevo(Model model) {
 Cliente cliente= new Cliente();
 model.addAttribute("cliente", cliente);
 try {

 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/cliente/nuevo";
}

@GetMapping("/del/{id}")
public String eliminar(@PathVariable("id") String id, Model model) {
 try {
  Optional<Cliente> cliente = clienteService.findById(id);
  if (cliente.isPresent()) {
   clienteService.deleteById(id);
  }
 } catch (Exception e) {
  model.addAttribute("dangerDel", "ERROR");
  try {
   List<Cliente> cliente = clienteService.findAll();
   model.addAttribute("cliente", cliente);
  } catch (Exception e2) {
   // TODO: handle exception
  }
  return "/cliente/inicio";
 }
 return "redirect:/cliente";
}

@GetMapping("/info/{id}")
public String info(@PathVariable("id") String id, Model model) {
 try {
  Optional<Cliente> cliente = clienteService.findById(id);
  if (cliente.isPresent()) {
   model.addAttribute("cliente", cliente.get());
  } else {
   return "redirect:/cliente";
  }
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/cliente/info";
}

@GetMapping("/{id}/nuevousuario")
public String nuevoUsuario(@PathVariable("id") String id, Model model) {
 Usuario usuario= new Usuario();
 try {
  Optional<Cliente> cliente= clienteService.findById(id);
  if (cliente.isPresent()) {
   usuario.setCliente(cliente.get());
   model.addAttribute("usuario", usuario);
  } else {
   return "redirect:/cliente";
  }
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/cliente/nuevousuario";
}

@PostMapping("/saveusuario")
public String saveUsuario(@ModelAttribute("usuario") Usuario usuario,
  Model model, SessionStatus status) {
 
 try {
  // Verificar que el username ya exista.
  Optional<Usuario> optional
   = usuarioService.findByUsername(usuario.getUsername());
  if(optional.isPresent()) {
   model.addAttribute("dangerRegister"
     , "ERROR - El username "
      + usuario.getUsername()
      + " ya existe ");
   return "/cliente";
  } else {
   usuario.setPassword(passwordEncoder
     .encode( usuario.getPassword() ));
   usuario.addAuthority("ROLE_TRABAJADOR");
   usuarioService.save(usuario);
   status.setComplete();
  }
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "redirect:/cliente";
}
}