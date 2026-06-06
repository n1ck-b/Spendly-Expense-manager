package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(categoryId: Long) {
        categoryRepository.deleteCategory(categoryId)
    }

}