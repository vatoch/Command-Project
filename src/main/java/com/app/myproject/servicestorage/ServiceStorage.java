package com.app.myproject.servicestorage;

import com.app.myproject.service.commandservice.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceStorage {

    private final Map<String, GenericService> map;
    @Autowired
    public ServiceStorage(List<GenericService> services) {
        map = new HashMap<>();
        for(GenericService service : services) {
            map.put(service.getName(),service);
        }
    }

    public Map<String, GenericService> getMap() {
        return map;
    }
}
