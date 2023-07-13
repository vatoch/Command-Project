package com.app.myproject.mapper;

import com.app.myproject.dto.CommandDTO;
import com.app.myproject.entity.Command;
import com.app.myproject.param.CommandParam;


public class CommandMapper {

    public static CommandDTO commandToDto(Command command) {
        return CommandDTO.builder().type(command.getType()).price(command.getPrice()).name(command.getName()).build();
    }

    public static Command commandFromParam(CommandParam commandParam) {
        return Command.builder().name(commandParam.getName()).price(commandParam.getPrice())
                .type(commandParam.getType()).build();
    }



}
