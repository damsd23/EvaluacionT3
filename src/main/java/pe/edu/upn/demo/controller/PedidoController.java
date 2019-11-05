package pe.edu.upn.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pe.edu.upn.demo.entity.Pedidos;
import pe.edu.upn.demo.entity.Platos;
import pe.edu.upn.demo.service.PedidosService;
import pe.edu.upn.demo.service.PlatosService;

@Controller
@RequestMapping("/pedidos")
@SessionAttributes( {"plato", "pedidos" , "" , "" , } )
public class PedidoController {

@Autowired
private PlatosService platosService;


@Autowired
private PedidosService pedidosService;

@GetMapping
public String inicio(Model model) {
 try {
  List<Pedidos> pedidos = pedidosService.findAll();
  model.addAttribute("pedidos", pedidos);
 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/pedido/inicio";
}

@PostMapping("/save")
public String save(@ModelAttribute("plato") Pedidos pedido,
  Model model, SessionStatus status) {
 try {
  //System.out.println(pedido.getDescripcion());
  pedidosService.save(pedido);
  status.setComplete();
 
 } catch (Exception e) {
  System.out.println(e.getMessage());
 }
 return "redirect:/pedido";
}

@GetMapping("/nuevo")
public String nuevo(Model model) {
 Pedidos pedido= new Pedidos();
 model.addAttribute("pedido", pedido);
 try {

 } catch (Exception e) {
  // TODO: handle exception
 }
 return "/pedido/nuevo";
}

@GetMapping("/del/{id}")
public String eliminar(@PathVariable("id") String id, Model model) {
 try {
  Optional<Pedidos> pedido = pedidosService.findById(id);
  if(pedido.isPresent()) {
   pedidosService.deleteById(id);
  }
 } catch (Exception e) {
 
  model.addAttribute("dangerDel", "ERROR - Violación contra el principio de Integridad referencia");
  try {
   List<Platos> platos = platosService.findAll();
   model.addAttribute("platos", platos);
   
   
   
   
  } catch (Exception e2) {
   // TODO: handle exception
  }
  return "/pedido/inicio";
 }
 return "redirect:/pedido";
}



}
