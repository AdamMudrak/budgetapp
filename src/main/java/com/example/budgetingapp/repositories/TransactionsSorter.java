package com.example.budgetingapp.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TransactionsSorter {
    private static final String DEFAULT_SORTING_FIELD = "transactionDate";

    public static Pageable getSortOrSortByDefault(Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            return pageable;
        }
        Sort defaultSort = Sort.by(Sort.Direction.DESC, DEFAULT_SORTING_FIELD);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
    }
}
