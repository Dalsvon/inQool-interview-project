package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.models.dtos.*;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.SurfaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationMapper {

    private final UserMapper userMapper;

    private final CourtMapper courtMapper;

    @Autowired
    public ReservationMapper(UserMapper userMapper, CourtMapper courtMapper) {
        this.userMapper = userMapper;
        this.courtMapper = courtMapper;
    }

    public Reservation mapToEntity(ReservationCreateDTO dto) {
        Reservation reservation = new Reservation();

        reservation.setEndsAt(dto.getEndsAt());
        reservation.setStartsAt(dto.getStartsAt());
        reservation.setDoubles(dto.isDoubles());
        reservation.setId(0);

        return reservation;
    }

    public ReservationDTO mapToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();

        dto.setId(reservation.getId());
        dto.setDoubles(reservation.isDoubles());
        dto.setCost(reservation.getCost());
        dto.setEndsAt(reservation.getEndsAt());
        dto.setStartsAt(reservation.getStartsAt());
        dto.setDeletedAt(reservation.getDeletedAt());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setCourt(courtMapper.mapToDTO(reservation.getCourt()));
        dto.setUserId(reservation.getUser().getId());

        return dto;
    }

    public List<ReservationDTO> mapToDTOList(List<Reservation> reservations) {
        List<ReservationDTO> DTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            DTOs.add(mapToDTO(reservation));
        }
        return DTOs;
    }
}
