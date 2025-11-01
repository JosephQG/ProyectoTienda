package com.tienda.controller;
import com.tienda.service.ProductoService;
import com.tienda.service.CategoriaService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
   
    private final ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
   
    public ConsultaController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/consultas/listado";
    }
  
    @PostMapping("/consultaDerivada")
    public String consultaDerivada (@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaDerivada(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/consultas/listado";
    }
  
    @PostMapping("/consultaJPQL")
    public String consultaJPQL (@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/consultas/listado";
    }
  
    @PostMapping("/consultaSQL")
    public String consultaSQL (@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/consultas/listado";
    }
    
    @PostMapping("/consultaCategoria")
    public String consultaCategoria (@RequestParam() Integer idCategoria,
            Model model) {
        var productos = productoService.consultaPorCategoria(idCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("idCategoria", idCategoria);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/consultas/listado";
    }
  
}