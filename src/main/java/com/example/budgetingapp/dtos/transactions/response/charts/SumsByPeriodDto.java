package com.example.budgetingapp.dtos.transactions.response.charts;

import com.example.budgetingapp.dtos.transactions.response.charts.inner.SumsByCategoryDto;
import java.time.LocalDate;
import java.util.List;

/** Used to prepare aggregated sum of transactions within a day, a month or a year,
 * and inside each of date entry there are separate sum values for each
 * category within the period */
public record SumsByPeriodDto(LocalDate localDate,
                              List<SumsByCategoryDto> sumsByCategory) {}
