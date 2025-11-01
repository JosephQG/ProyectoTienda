package com.tienda.repository;

import com.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    //Consulta derivada que obtiene los productos de un rango de precio ordenados por precio ascendentemente
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioInf, BigDecimal precioSup);

    //Consulta JPQL que obtiene los productos de un rango de precio ordenados por precio ascendentemente
    @Query(value = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> ConsultaJPQL(BigDecimal precioInf, BigDecimal precioSup);

    //Consulta SQL que obtiene los productos de un rango de precio ordenados por precio ascendentemente
    @Query(nativeQuery = true,
            value = "SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> ConsultaSQL(BigDecimal precioInf, BigDecimal precioSup);

    // Consulta derivada que obtiene los productos de una categoría específica
    public List<Producto> findByCategoriaIdCategoria(Integer idCategoria);

}