package com.example.springboot.controller;

import com.example.springboot.dto.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;


    /*
    * Endpoint responsável por criar produto
    * */
    @PostMapping("/create/product")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {

        //instancia um novo objeto que será inserido no banco de dados
        var productModel = new ProductModel();

        //copia as informações do DTO que e o objeto que vem no parametro para o objeto que será inserido no banco
        //os atributos precisam ter nomes iguais para realizar a copia
        BeanUtils.copyProperties(productRecordDto, productModel);

        //primeiro parte ele define que irá retornar um response entity com status de criado 201
        //já no corpo (body) ele utiliza o repositório que tem a função de salvar
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    /*
    * Endpoint responsável por alterar produto
    * */
    @PutMapping("update/product")
    public ResponseEntity<ProductModel> updateProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    /*
    * Endpoint responsável por deletar produto com base no id
    * */
    @GetMapping("delete/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {

        //esse tipo de objeto optional pode ser instanciado ou não, depende se o produto existir na base
        Optional<ProductModel> product = productRepository.findById(id);

        //valida se este está vazio
        if (product.isEmpty()) {
            //caso nao exista ele retorna não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado");
        }
        //deleta produto
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    /*
    * Endpoint responsável por retornar lista de produtos sem paginação
    * */
    @GetMapping("get/product")
    public ResponseEntity<List<ProductModel>> getProduct() {

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    /*
    * Endpoint responsável por retornar apenas um produto com base no id dele
    * */
    @GetMapping("getone/product/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {

        //inicia um objeto opcional
        Optional<ProductModel> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    /*
    * Endpoint responsável por retornar
    * */
    @GetMapping("pageable/product")
    public Page<ProductModel> getProductPageable(Pageable pageable) {

        return productRepository.findAll(pageable);
    }

}
