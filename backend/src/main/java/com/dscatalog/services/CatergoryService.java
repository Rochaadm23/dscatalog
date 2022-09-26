package com.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dscatalog.entities.Category;
import com.dscatalog.repositories.CategoryRepository;

@Service
public class CatergoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> findAll() {

		return categoryRepository.findAll();
	}
}
