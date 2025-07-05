package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.ReservationMapper;
import cz.svonavec.tennis.models.dtos.ReservationCreateDTO;
import cz.svonavec.tennis.models.dtos.ReservationDTO;
import cz.svonavec.tennis.models.dtos.ReservationUpdateDTO;
import cz.svonavec.tennis.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReservationFacade {
    private final ReservationMapper reservationMapper;

    private final ReservationService reservationService;

    @Autowired
    public ReservationFacade (ReservationMapper reservationMapper, ReservationService reservationService) {
        this.reservationMapper = reservationMapper;
        this.reservationService =reservationService;
    }

    @Transactional(readOnly = true)
    public ReservationDTO findById(long id) {
        return reservationMapper.mapToDTO(reservationService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll() {
        return reservationMapper.mapToDTOList(reservationService.findAll());
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findByCourt(long courtId) {
        return reservationMapper.mapToDTOList(reservationService.findByCourt(courtId));
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findByPhone(String phoneNumber, boolean futureOnly) {
        return reservationMapper.mapToDTOList(reservationService.findByPhone(phoneNumber, futureOnly));
    }

    @Transactional
    public BigDecimal create(ReservationCreateDTO dto) {
        return reservationService.create(reservationMapper.mapToEntity(dto), dto.getPhoneNumber(), dto.getCourtId());
    }

    @Transactional
    public ReservationDTO update(ReservationUpdateDTO dto) {
        return reservationMapper.mapToDTO(reservationService.update(dto.getId(), dto.getStartsAt(), dto.getEndsAt(), dto.getDoubles(), dto.getCost()));
    }

    @Transactional
    public ReservationDTO delete(long id) {
        return reservationMapper.mapToDTO(reservationService.delete(id));
    }
}
