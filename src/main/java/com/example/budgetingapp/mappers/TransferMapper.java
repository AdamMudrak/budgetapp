package com.example.budgetingapp.mappers;

import static com.example.budgetingapp.constants.Constants.DATE_PATTERN;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.Transfer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TransferMapper {

    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(source = "fromAccountId", target = "fromAccount.id")
    @Mapping(source = "toAccountId", target = "toAccount.id")
    Transfer toTransfer(TransferRequestDto requestTransactionDto);

    @AfterMapping
    default void setTransactionDate(@MappingTarget Transfer transfer,
                                    TransferRequestDto requestTransactionDto) {
        transfer.setTransactionDate(LocalDate.parse(requestTransactionDto.getTransactionDate(),
                DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }

    @Mapping(source = "fromAccount.id", target = "fromAccountId")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    TransferResponseDto toTransferDto(Transfer transfer);
}
