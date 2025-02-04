package com.example.budgetingapp.mappers;

import com.example.budgetingapp.config.MapperConfig;
import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.Transfer;
import java.time.LocalDate;
import java.util.List;
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
        transfer.setTransactionDate(LocalDate.parse(requestTransactionDto.transactionDate()));
    }

    @Mapping(source = "fromAccount.id", target = "fromAccountId")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    @Mapping(source = "fromAccount.name", target = "fromAccountName")
    @Mapping(source = "toAccount.name", target = "toAccountName")
    TransferResponseDto toTransferDto(Transfer transfer);

    List<TransferResponseDto> toTransferDtoList(List<Transfer> transfers);
}
