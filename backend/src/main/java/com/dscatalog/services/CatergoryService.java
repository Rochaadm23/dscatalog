package com.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.dto.CategoryDTO;
import com.dscatalog.entities.Category;
import com.dscatalog.repositories.CategoryRepository;
import com.dscatalog.services.exception.EntityNotFoundException;

@Service
public class CatergoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = categoryRepository.findAll();

		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {

		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));

		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO objDto) {
		Category entity = new Category();
		entity.setName(objDto.getName());
		entity = categoryRepository.save(entity);

		return new CategoryDTO(entity);
	}
}
