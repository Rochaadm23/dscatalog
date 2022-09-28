package com.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.dto.CategoryDTO;
import com.dscatalog.dto.ProductDTO;
import com.dscatalog.entities.Category;
import com.dscatalog.entities.Product;
import com.dscatalog.repositories.CategoryRepository;
import com.dscatalog.repositories.ProductRepository;
import com.dscatalog.services.exception.DatabaseException;
import com.dscatalog.services.exception.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = productRepository.findAll(pageRequest);

		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO objDto) {
		Product entity = new Product();
		copyDtoToEntity(objDto, entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO objDto) {
		try {
			Product entity = productRepository.getReferenceById(id);
			copyDtoToEntity(objDto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);

		} catch (javax.persistence.EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}

	}

	private void copyDtoToEntity(ProductDTO objDto, Product entity) {
		entity.setName(objDto.getName());
		entity.setDescription(objDto.getDescription());
		entity.setDate(objDto.getDate());
		entity.setImgUrl(objDto.getImgUrl());
		entity.setPrice(objDto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto : objDto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}
}
