package com.app.myproject.service;


import com.app.myproject.dto.CommandDTO;
import com.app.myproject.entity.Command;
import com.app.myproject.mapper.CommandMapper;
import com.app.myproject.param.CommandParam;
import com.app.myproject.repo.CommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandService {

    private final CommandRepository commandRepository;

    public void addCommand(CommandParam commandParam) {
        Command command = CommandMapper.commandFromParam(commandParam);
        commandRepository.save(command);
    }


    public Page<CommandDTO> getAllCommands(Pageable pageable) {
        return commandRepository.allCommands(pageable).map(CommandMapper::commandToDto);
    }


}
