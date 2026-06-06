package com.inb.spendly.domain.useCases.categories

import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import kotlinx.coroutines.flow.Flow

class GetExpensesByCategoriesUseCase(
    val repository: CategoryRepository
) {

    operator fun invoke(startDate: Long, endDate: Long): Flow<List<CategoryWithFilteredExpenses>> {
        return repository.getAllExpensesByCategories(startDate, endDate)
    }

}