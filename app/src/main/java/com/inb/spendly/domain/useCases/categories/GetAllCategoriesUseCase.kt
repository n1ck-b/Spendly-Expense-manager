package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.entities.Category
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getAllCategories()
    }
}