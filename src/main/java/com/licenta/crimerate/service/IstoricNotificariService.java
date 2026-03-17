package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IstoricNotificariDto;
import com.licenta.crimerate.entity.IstoricNotificari;
import com.licenta.crimerate.repository.IstoricNotificariRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IstoricNotificariService {

    private final IstoricNotificariRepository notificariRepository;

    public IstoricNotificariService(IstoricNotificariRepository notificariRepository) {
        this.notificariRepository = notificariRepository;
    }

    public List<IstoricNotificariDto> getNotificariByUtilizator(Integer utilizatorId) {
        return notificariRepository.findByUtilizatorId(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IstoricNotificariDto> getNotificariNecitite(Integer utilizatorId) {
        return notificariRepository.findByUtilizatorIdAndCitit(utilizatorId, false).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private IstoricNotificariDto mapToDto(IstoricNotificari notificare) {
        IstoricNotificariDto dto = new IstoricNotificariDto();
        dto.setId(notificare.getId());
        dto.setMesaj(notificare.getMesaj());
        dto.setCitit(notificare.getCitit());
        dto.setDataNotificare(notificare.getDataNotificare());
        return dto;
    }
}
